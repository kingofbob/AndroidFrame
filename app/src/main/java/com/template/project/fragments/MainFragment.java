package com.template.project.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;
import com.gigamole.infinitecycleviewpager.OnInfiniteCyclePageTransformListener;
import com.template.project.R;
import com.template.project.activity.MainActivity;
import com.template.project.adapters.HorizontalPagerAdapter;
import com.template.project.adapters.MainAdapter;
import com.template.project.base.BaseMainFragment;
import com.template.project.connections.GetRecentPublicEventAPI;
import com.template.project.connections.NotificationHistoryCountAPI;
import com.template.project.constants.ErrorCodes;
import com.template.project.constants.NotificationConst;
import com.template.project.constants.UserConst;
import com.template.project.database.dao.LandingPageDAO;
import com.template.project.database.entities.EventList;
import com.template.project.objects.GetRecentPublicEventPayload;
import com.template.project.objects.LoginObj;
import com.template.project.objects.MainActivityAction;
import com.template.project.objects.NotificationHistoryCountPayload;
import com.template.project.utils.GlobalConfig;
import com.template.project.utils.QuickUtils;
import com.template.project.utils.TinyDB;

import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.support.v4.content.ContextCompat.getColor;


public class MainFragment extends BaseMainFragment {


    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.notificationCountText)
    TextView notificationCountText;
    @Bind(R.id.hicvp)
    HorizontalInfiniteCycleViewPager horizontalInfiniteCycleViewPager;
//    @Bind(com.daimajia.slider.library.R.id.description_layout)
//    View descriptionLayout;


    @Bind(R.id.notification_icon)
    FrameLayout notificationLayout;
    @Bind(R.id.slider)
    SliderLayout sliderLayout;
    @Bind(R.id.custom_indicator)
    PagerIndicator pagerIndicator;

    private MainAdapter adapter;
    private HorizontalPagerAdapter horizontalPagerAdapter;
    private HashMap<String, String> url_maps = new HashMap<String, String>();

    public static MainFragment newInstance() {

        Bundle args = new Bundle();

        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        GlobalConfig.getInstance().getEventBus().register(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        GlobalConfig.getInstance().getEventBus().register(this);
        initView();
        notificationCountText.setText(new TinyDB(getActivity()).getInt(NotificationConst.COUNT) + "");
        new NotificationHistoryCountAPI(getActivity()).execute(new TinyDB(getActivity()).getString(UserConst.USERNAME));
    }

    private void initView() {

        String username = new TinyDB(getActivity()).getString(UserConst.USERNAME);
        username = username.length() > 0 ?
                ((LoginObj) new TinyDB(getActivity()).getObject(UserConst.USER, LoginObj.class)).getFullName()
                : getActivity().getString(R.string.guest);

//        mToolbar.setTitle(getActivity().getString(R.string.welcome) + " " + username);
        initToolbarNav(mToolbar, true);
        mToolbar.inflateMenu(R.menu.home);


//        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));


//        initGridView();
        initBottomTiles();

        tempHeaderData();
        initTopView();

        notificationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start(NotificationFragment.newInstance());
            }
        });

        new GetRecentPublicEventAPI(getActivity()).execute();

    }


    @Override
    protected void onNewBundle(Bundle args) {
        super.onNewBundle(args);

//        Toast.makeText(_mActivity, args.getString("from"), Toast.LENGTH_SHORT).show();
    }

//    class MyHeaderViewHolder extends RecyclerView.ViewHolder {
//
//        SliderLayout sliderLayout;
//        PagerIndicator pagerIndicator;
//
//        public MyHeaderViewHolder(View itemView) {
//            super(itemView);
//
//            sliderLayout = (SliderLayout) itemView.findViewById(R.id.slider);
//            pagerIndicator = (PagerIndicator) itemView.findViewById(R.id.custom_indicator);
//
//            View descriptionLayout = itemView.findViewById(com.daimajia.slider.library.R.id.description_layout);
//            if(descriptionLayout!=null){
//                itemView.findViewById(com.daimajia.slider.library.R.id.description_layout).setVisibility(View.GONE);
//            }
//        }
//    }

    private void initTopView() {
//        if (descriptionLayout != null) {
//            descriptionLayout.setVisibility(View.GONE);
//        }


        List<EventList> topEventsList = LandingPageDAO.getLandingPage();
        sliderLayout.removeAllSliders();
        for (EventList eventList: topEventsList){
            DefaultSliderView textSliderView = new DefaultSliderView(getActivity());
            textSliderView
                    .description(eventList.getEventName())
                    .image("http://imunews.imu.edu.my/wp-content/uploads/2013/04/1-670x400.jpg")
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(BaseSliderView slider) {

                        }
                    });

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", eventList.getEventName());

            sliderLayout.addSlider(textSliderView);
        }


        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Default);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
//            ((MyHeaderViewHolder) holder).sliderLayout.setCustomAnimation(new DescriptionAnimation());

        sliderLayout.setDuration(10000);
        sliderLayout.setCustomIndicator(pagerIndicator);
        sliderLayout.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void tempHeaderData() {
        url_maps.put("IMU Hall", "http://imunews.imu.edu.my/wp-content/uploads/2013/04/1-670x400.jpg");
        url_maps.put("IMU New Building", "http://imu.edu.my/imu/wp-content/uploads/accomodation_1.jpg");
        url_maps.put("IMU Front", "https://upload.wikimedia.org/wikipedia/en/6/62/IMU_Front.jpg");
        url_maps.put("The Pulse of IMU", "http://imunews.imu.edu.my/wp-content/uploads/2015/05/Biocamp1.jpg");
    }

//    private void initGridView() {
//        List<String> datas = new ArrayList<>();
//        datas.add("Header");
//        datas.add("Events");
//        datas.add("News");
//
//
////        HashMap<String, String> url_maps = new HashMap<String, String>();
////        url_maps.put("IMU Hall", "http://imunews.imu.edu.my/wp-content/uploads/2013/04/1-670x400.jpg");
////        url_maps.put("IMU New Building", "http://imu.edu.my/imu/wp-content/uploads/accomodation_1.jpg");
////        url_maps.put("IMU Front", "https://upload.wikimedia.org/wikipedia/en/6/62/IMU_Front.jpg");
////        url_maps.put("The Pulse of IMU", "http://imunews.imu.edu.my/wp-content/uploads/2015/05/Biocamp1.jpg");
//
//        HashMap<String, Integer> url_maps = new HashMap<String, Integer>();
//        url_maps.put("IMU Hall", R.drawable.img_home_1);
//        url_maps.put("IMU New Building", R.drawable.img_home_1);
//        url_maps.put("IMU Front", R.drawable.img_home_1);
//        url_maps.put("The Pulse of IMU", R.drawable.img_home_1);
//        adapter = new MainAdapter(getActivity());
//
//        adapter.setDatas(datas, url_maps);
//
//        final GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
//        recyclerView.setLayoutManager(manager);
//        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                return adapter.isHeader(position) ? manager.getSpanCount() : 1;
//            }
//        });
//
//        recyclerView.setAdapter(adapter);
//    }

    private void initBottomTiles() {
        if (horizontalPagerAdapter == null) {
            horizontalPagerAdapter = new HorizontalPagerAdapter(getActivity());
            horizontalPagerAdapter.setDatas(MainActivity.generateMenuDataList(getActivity()));
            horizontalInfiniteCycleViewPager.setAdapter(horizontalPagerAdapter);

            horizontalInfiniteCycleViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            horizontalInfiniteCycleViewPager.setOnInfiniteCyclePageTransformListener(new OnInfiniteCyclePageTransformListener() {
                @Override
                public void onPreTransform(View page, float position) {
                    Log.d(MainActivity.class.getSimpleName(), "onPreTransform: " + ((TextView) page.findViewById(R.id.title)).getText().toString());


                }

                @Override
                public void onPostTransform(View page, float position) {
                    Log.d(MainActivity.class.getSimpleName(), "onPostTransform: " + position + " : " + ((TextView) page.findViewById(R.id.title)).getText().toString());


                    if (position == 0) {
                        ((CardView) page.findViewById(R.id.cardview)).setCardBackgroundColor(getColor(getActivity(), R.color.primary_orange));
                    } else if (position == 1) {
                        ((CardView) page.findViewById(R.id.cardview)).setCardBackgroundColor(getColor(getActivity(), R.color.primary));
                    } else if (position == -1) {
                        ((CardView) page.findViewById(R.id.cardview)).setCardBackgroundColor(getColor(getActivity(), R.color.primary));
                    }

                }
            });


        } else {
            horizontalPagerAdapter.setDatas(MainActivity.generateMenuDataList(getActivity()));
            horizontalPagerAdapter.notifyDataSetChanged();
        }

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        GlobalConfig.getInstance().getEventBus().unregister(this);
    }


    @Subscribe
    public void onNotificationHistoryCountPayload(final NotificationHistoryCountPayload dataPayload) {

        Log.d(this.getClass().getSimpleName(), "Entering event bus");

        if (dataPayload.getStatusCode() == ErrorCodes.SUCCESS_CODE) {
            new AsyncTask<NotificationHistoryCountPayload, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected Void doInBackground(NotificationHistoryCountPayload... dataPayload) {


                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);

                    notificationCountText.setText(new TinyDB(getActivity()).getInt(NotificationConst.COUNT) + "");
//                    getDatas();
//                    swipeRefreshLayout.setRefreshing(false);
                }
            }.execute(dataPayload);
        } else {
            QuickUtils.runToast(getActivity(), dataPayload.getStatusMessage());
//            swipeRefreshLayout.setRefreshing(false);
        }


    }

    @Subscribe
    public void onGetRecentPublicEventPayload(final GetRecentPublicEventPayload dataPayload) {

        Log.d(this.getClass().getSimpleName(), "Entering event bus");

        if (dataPayload.getStatusCode() == ErrorCodes.SUCCESS_CODE) {
            new AsyncTask<GetRecentPublicEventPayload, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected Void doInBackground(GetRecentPublicEventPayload... dataPayload) {


                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);

                    initTopView();
                }
            }.execute(dataPayload);
        } else {
            QuickUtils.runToast(getActivity(), dataPayload.getResponseMessage());
        }


    }
}
