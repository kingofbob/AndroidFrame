package com.template.project.activity;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.template.project.R;
import com.template.project.adapters.NavigationListAdapter;
import com.template.project.base.BaseMainFragment;
import com.template.project.constants.GCMConst;
import com.template.project.constants.SyncDateConst;
import com.template.project.constants.UserConst;
import com.template.project.fragments.AboutUsFragment;
import com.template.project.fragments.ContactUsFragment;
import com.template.project.fragments.EventFilterFragment;
import com.template.project.fragments.EventMainFragment;
import com.template.project.fragments.MainFragment;
import com.template.project.fragments.NewsFragment;
import com.template.project.fragments.PrivacyPolicyFragment;
import com.template.project.fragments.ProfileFragment;
import com.template.project.fragments.QRReaderFragment;
import com.template.project.fragments.SettingsFragment;
import com.template.project.fragments.TermsAndConditionsFragment;
import com.template.project.fragments.VirtualTourFragment;
import com.template.project.functions.UploadActivity;
import com.template.project.gcmservices.RegistrationIntentService;
import com.template.project.listobjects.NavigationRowObject;
import com.template.project.objects.GeneralPayload;
import com.template.project.objects.LoginObj;
import com.template.project.objects.MainActivityAction;
import com.template.project.utils.GlobalConfig;
import com.template.project.utils.QuickUtils;
import com.template.project.utils.TinyDB;
import com.txusballesteros.bubbles.BubbleLayout;
import com.txusballesteros.bubbles.BubblesManager;
import com.txusballesteros.bubbles.OnInitializedCallback;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;
import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class MainActivity extends SupportActivity implements BaseMainFragment.OnFragmentOpenDrawerListener,
        EventFilterFragment.OnFilterApplyListener {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;
    private TextView mTvName;
    private ImageView mImgNav;
    private BubbleLayout bubbleView;

    private static final int DRAWER_TIMER = 300;

    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawer;
    @Bind(R.id.logout_layout)
    LinearLayout logoutLayout;
    @Bind(R.id.nav_view)
    NavigationView mNavigationView;
    //    @Bind(R.id.name)
//    TextView nameText;
//    @Bind(R.id.profile_image)
//    CircleImageView profileImage;
    @Bind(R.id.lst_menu_items)
    StickyListHeadersListView navigationListView;

//    @Bind(R.id.qrscanner_button)
//    ImageView qrButton;

    @Bind(R.id.progress)
    AVLoadingIndicatorView progressBar;

    private boolean runThread = true;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;

    private NavigationListAdapter navAdapter;
    private int selectedId = 0;
    private Integer selectionId = 0;

    private BubblesManager bubblesManager;

    @Bind(R.id.container)FrameLayout containerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        QuickUtils.setStatusBarTranslucent(MainActivity.this, true);
        QuickUtils.addStatusBarGab(MainActivity.this, navigationListView);
        GlobalConfig.getInstance().getEventBus().register(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        progressBar.hide();

        if (savedInstanceState == null) {
            loadRootFragment(R.id.container, MainFragment.newInstance());
        }

        initView();


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(GCMConst.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {

                } else {

                }
            }
        };

        registerReceiver();

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

        logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDrawer.isDrawerOpen(GravityCompat.START)) {
                    mDrawer.closeDrawer(GravityCompat.START);
                }

                mDrawer.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        QuickUtils.alertDialog(MainActivity.this, MainActivity.this.getString(R.string.sure_logout), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                logout(MainActivity.this);
                            }
                        });

                    }
                }, DRAWER_TIMER);
            }
        });

        generateNavUI();

        runUICheckThread();
        initDrawerSelections();
    }



    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        // 设置默认Fragment动画  默认竖向(和安卓5.0以上的动画相同)
//        return super.onCreateFragmentAnimator();
        // 设置横向(和安卓4.x动画相同)
        return new DefaultHorizontalAnimator();
        // 设置自定义动画
//        return new FragmentAnimator(enter,exit,popEnter,popExit);
    }

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(GCMConst.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(MainActivity.class.getSimpleName(), "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


    private void initView() {
        QuickUtils.setProgressBar(progressBar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        mDrawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    public static void logout(Context context) {
        Log.d(MainActivity.class.getSimpleName(), "Logging out...");
        new TinyDB(context).putString(UserConst.USERNAME, "");
        new TinyDB(context).putObject(UserConst.USER, "");
        new TinyDB(context).putObject(UserConst.USER_DETAILS, "");
        new TinyDB(context).putString(SyncDateConst.LOGIN_DATA, "");
        new TinyDB(context).putString(UserConst.USER_OBJECT_CACHE, "");

        new TinyDB(context).putString(SyncDateConst.ALL_EVENT_DATA, "");


        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ( (Activity)context).startActivity(intent);


        ( (Activity)context).finish();

    }

    @Override
    public void onBackPressedSupport() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {

            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                pop();
            } else {
                if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
                    finish();
                } else {
                    TOUCH_TIME = System.currentTimeMillis();
                    Toast.makeText(this, R.string.press_again_exit, Toast.LENGTH_SHORT).show();
                }
            }

            if (getCurrentFragment() instanceof MainFragment){
                selectedId = R.string.home;
                generateNavUI();
            }


        }
    }

    /**
     * 打开抽屉
     */
    @Override
    public void onOpenDrawer() {
        if (!mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.openDrawer(GravityCompat.START);
        }
    }

    private void initDrawerSelections(){
        mDrawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                selectedMenuAction();
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    private void selectedMenuAction(){
        if (selectionId ==null )
            return;

        switch (selectionId) {

            case R.string.upload_log:
                UploadActivity.uploadLogs(MainActivity.this, progressBar);
                break;

            case R.string.home:
                MainFragment mainFragment = findFragment(MainFragment.class);
//                        Bundle newBundle = new Bundle();
//                        newBundle.putString("from", "主页-->来自:" + topFragment.getClass().getSimpleName());
//                        mainFragment.putNewBundle(newBundle);

                start(mainFragment, SupportFragment.SINGLETASK);

                break;
            case R.string.imu_news:
                NewsFragment newsFragment = findFragment(NewsFragment.class);
                if (newsFragment == null) {
                    popTo(MainFragment.class, false, new Runnable() {
                        @Override
                        public void run() {
                            start(NewsFragment.newInstance());
                        }
                    });
                } else {
                    // 如果已经在栈内,则以SingleTask模式start,也可以用popTo
                    start(newsFragment, SupportFragment.SINGLETASK);
                }
                break;

            case R.string.profile:
                ProfileFragment profileFragment = findFragment(ProfileFragment.class);
                if (profileFragment == null) {
                    popTo(MainFragment.class, false, new Runnable() {
                        @Override
                        public void run() {
                            start(ProfileFragment.newInstance(0));
                        }
                    });
                } else {
                    // 如果已经在栈内,则以SingleTask模式start
                    start(profileFragment, SupportFragment.SINGLETASK);
                }

                break;

            case R.id.nav_change_password:


                break;



            case R.string.events:
                EventMainFragment eventMainFragment = findFragment(EventMainFragment.class);
                if (eventMainFragment == null) {
                    popTo(MainFragment.class, false, new Runnable() {
                        @Override
                        public void run() {
                            start(EventMainFragment.newInstance(0));
                        }
                    });
                } else {
                    // 如果已经在栈内,则以SingleTask模式start,也可以用popTo
                    start(eventMainFragment, SupportFragment.SINGLETASK);
                }

                break;

            case R.string.privacy_policy:
                start(PrivacyPolicyFragment.newInstance());

                break;

            case R.string.terms_and_conditions:
                start(TermsAndConditionsFragment.newInstance());

                break;

            case R.string.about_us:
                start(AboutUsFragment.newInstance());

                break;

            case R.string.contact_us:
                start(ContactUsFragment.newInstance());
                break;

            case R.string.virtual_tour:
                start(VirtualTourFragment.newInstance());

                break;

            case R.string.imu_qr_code:
//                QRReaderFragment qrReaderFragment = findFragment(QRReaderFragment.class);
//                if (qrReaderFragment == null) {
//                    popTo(MainFragment.class, false, new Runnable() {
//                        @Override
//                        public void run() {
//                            start(QRReaderFragment.newInstance());
//                        }
//                    });
//                } else {
//                    start(qrReaderFragment, SupportFragment.SINGLETASK);
//                }
                start(QRReaderFragment.newInstance());
                break;

            case R.string.my_timetable:
                EventMainFragment timetableFragment = findFragment(EventMainFragment.class);
                if (timetableFragment == null) {
                    popTo(MainFragment.class, false, new Runnable() {
                        @Override
                        public void run() {
                            start(EventMainFragment.newInstance(1));
                        }
                    });
                } else {
                    // 如果已经在栈内,则以SingleTask模式start,也可以用popTo
                    start(timetableFragment, SupportFragment.SINGLETASK);
                }

                break;

            case R.string.settings:
                start(SettingsFragment.newInstance());

                break;

        }

        selectionId = null;
    }


    //    @Override
    public boolean onNavigationItemSelected(int selectedIda) {
        selectionId = selectedIda;

        //highlighting

        for (NavigationRowObject navigationRowObject:generateMenuDataList(MainActivity.this)){
            if (selectedIda == navigationRowObject.getId() && navigationRowObject.getTYPE() == NavigationRowObject.TYPE_WITH_ICON){
                selectedId = selectedIda;
                generateNavUI();
            }
        }


        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        }else{
            selectedMenuAction();
        }

        return true;
    }


    @Subscribe
    public void onAction(MainActivityAction mainActivityAction) {

        switch (mainActivityAction.getAction()) {
            case 0: {
                initView();
                break;
            }
        }
    }

    @Subscribe
    public void onGeneralPayload(GeneralPayload generalPayload) {
        Log.d(this.getClass().getSimpleName(), "Entering event bus");

        progressBar.hide();
        QuickUtils.runToast(MainActivity.this, generalPayload.getStatusMessage());
    }

    @Override
    protected void onDestroy() {
        runThread = false;
        super.onDestroy();
        GlobalConfig.getInstance().getEventBus().unregister(this);

        bubblesManager.recycle();
    }


    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver();
        initializeBubblesManager();


    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();

        bubblesManager.recycle();
    }


    public static List<NavigationRowObject> generateMenuDataList(Context context) {
        List<NavigationRowObject> navigationRowObjectList = new ArrayList<>();
        navigationRowObjectList.add(new NavigationRowObject(R.string.home, context.getString(R.string.home), R.drawable.home_white_icon, true, NavigationRowObject.TYPE_WITH_ICON));
        navigationRowObjectList.add(new NavigationRowObject(R.string.my_timetable, context.getString(R.string.my_timetable), R.drawable.event_icn_01, true, NavigationRowObject.TYPE_WITH_ICON));
        navigationRowObjectList.add(new NavigationRowObject(R.string.events, context.getString(R.string.events), R.drawable.event_icn_01, true, NavigationRowObject.TYPE_WITH_ICON));
        navigationRowObjectList.add(new NavigationRowObject(R.string.imu_news, context.getString(R.string.imu_news), R.drawable.news_icn_01, true, NavigationRowObject.TYPE_WITH_ICON));
        navigationRowObjectList.add(new NavigationRowObject(R.string.imu_qr_code, context.getString(R.string.imu_qr_code), R.drawable.qrcode_white_icon, true, NavigationRowObject.TYPE_WITH_ICON));

        navigationRowObjectList.add(new NavigationRowObject(0, "", 0, true, NavigationRowObject.TYPE_DIVIDER));

//        navigationRowObjectList.add(new NavigationRowObject(R.string.emergency_contact, getString(R.string.emergency_contact), 0, true, NavigationRowObject.TYPE_TITLE));
        navigationRowObjectList.add(new NavigationRowObject(R.string.contact_us, context.getString(R.string.contact_us), 0, true, NavigationRowObject.TYPE_TITLE));
        navigationRowObjectList.add(new NavigationRowObject(R.string.about_us, context.getString(R.string.about_us), 0, true, NavigationRowObject.TYPE_TITLE));
        navigationRowObjectList.add(new NavigationRowObject(R.string.virtual_tour, context.getString(R.string.virtual_tour), 0, true, NavigationRowObject.TYPE_TITLE));
        navigationRowObjectList.add(new NavigationRowObject(R.string.privacy_policy, context.getString(R.string.privacy_policy), 0, true, NavigationRowObject.TYPE_TITLE));
//        navigationRowObjectList.add(new NavigationRowObject(R.string.terms_and_conditions, context.getString(R.string.terms_and_conditions), 0, true, NavigationRowObject.TYPE_TITLE));
        navigationRowObjectList.add(new NavigationRowObject(R.string.settings, context.getString(R.string.settings), 0, true, NavigationRowObject.TYPE_TITLE));
        navigationRowObjectList.add(new NavigationRowObject(R.string.upload_log, context.getString(R.string.upload_log), 0, true, NavigationRowObject.TYPE_TITLE));

        return navigationRowObjectList;
    }

    private NavigationRowObject generateHeaderData() {
        NavigationRowObject headerData = new NavigationRowObject(0, new TinyDB(MainActivity.this).getString(UserConst.USERNAME), 0, true, NavigationRowObject.TYPE_HEADER);
        LoginObj loginObj = (LoginObj) new TinyDB(MainActivity.this).getObject(UserConst.USER, LoginObj.class);
        if (loginObj != null) {
            headerData.setImgURL(loginObj.getPersonalPhotoURL());
        }

        return headerData;
    }

    private void generateNavUI() {
        if (navAdapter == null) {

            selectedId = R.string.home;
            navAdapter = new NavigationListAdapter(MainActivity.this);
            navAdapter.setData(generateMenuDataList(MainActivity.this), generateHeaderData());
            navAdapter.setSelectedId(selectedId);
            navigationListView.setAdapter(navAdapter);
        } else {
            navAdapter.setData(generateMenuDataList(MainActivity.this), generateHeaderData());
            navAdapter.setSelectedId(selectedId);
            navAdapter.notifyDataSetChanged();
        }

        navigationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onNavigationItemSelected((int) l);
            }
        });

        navigationListView.setOnHeaderClickListener(new StickyListHeadersListView.OnHeaderClickListener() {
            @Override
            public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {

                if (mDrawer.isDrawerOpen(GravityCompat.START)) {
                    mDrawer.closeDrawer(GravityCompat.START);
                }

                mDrawer.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        start(ProfileFragment.newInstance(0));

                    }
                }, DRAWER_TIMER);
            }
        });

    }


    private void initBubble() {
        bubbleView = (BubbleLayout) LayoutInflater.from(MainActivity.this).inflate(R.layout.bubble_layout, null);
        bubbleView.setOnBubbleRemoveListener(new BubbleLayout.OnBubbleRemoveListener() {
            @Override
            public void onBubbleRemoved(BubbleLayout bubble) { }
        });
        bubbleView.setOnBubbleClickListener(new BubbleLayout.OnBubbleClickListener() {

            @Override
            public void onBubbleClick(BubbleLayout bubble) {
                MainFragment mainFragment = findFragment(MainFragment.class);
                mainFragment.start(QRReaderFragment.newInstance());
            }
        });

        bubbleView.setShouldStickToWall(true);

    }

    private void initializeBubblesManager() {
        bubblesManager = new BubblesManager.Builder(MainActivity.this)
                .setInitializationCallback(new OnInitializedCallback() {
                    @Override
                    public void onInitialized() {

                    }
                })
                .build();
        bubblesManager.initialize();

    }


    public void showQRBubbleButton(){
        Fragment topFragment = getTopFragment();


        if (topFragment instanceof EventMainFragment
                ){
            if (bubbleView == null ){
                initBubble();
                bubblesManager.addBubble(bubbleView, containerLayout.getWidth()-280 ,containerLayout.getHeight()-280);
            }


        }else {

            if (bubbleView != null){
                bubblesManager.removeBubble(bubbleView);
                bubbleView = null;
            }

        }

    }

    private Fragment getCurrentFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
        Fragment currentFragment = fragmentManager.findFragmentByTag(fragmentTag);
        return currentFragment;
    }

    private void runUICheckThread(){
        new Thread(new Runnable() {
            public void run() {
                while (runThread) {


                    try {
                        showQRBubbleButton();


                        Thread.sleep(250);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public void onFilterApply(String tab){
        // Get Fragment B
        Log.d(tab, tab);

//        start(EventAllFragment.newInstance("ETT1", true));
//        EventAllFragment eventAllFragment = findFragment(EventAllFragment.class);
//        eventAllFragment.onFilterApply("jjj");
//        EventAllFragment frag = (EventAllFragment)
//                getSupportFragmentManager().findFragmentById(R.id.eve);
//        frag.updateText(text);
    }

}
