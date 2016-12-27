package com.template.project.fragments;


import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.poliveira.parallaxrecyclerview.HeaderLayoutManagerFixed;
import com.template.project.R;
import com.template.project.adapters.ProfileAdapter;
import com.template.project.base.BaseMainFragment;
import com.template.project.connections.GetUserProfileAPI;
import com.template.project.connections.UploadPhotoAPI;
import com.template.project.constants.ErrorCodes;
import com.template.project.constants.UserConst;
import com.template.project.constants.UtilConst;
import com.template.project.database.entities.GeneralMasterData;
import com.template.project.listobjects.CommonStickyListObject;
import com.template.project.objects.LoginObj;
import com.template.project.objects.StudentDetails;
import com.template.project.objects.SweetAlertOptions;
import com.template.project.objects.UploadPhotoPayload;
import com.template.project.objects.UserProfilePayload;
import com.template.project.utils.GlobalConfig;
import com.template.project.utils.ImageLoader;
import com.template.project.utils.QuickUtils;
import com.template.project.utils.TinyDB;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class ProfileFragment extends BaseMainFragment {
    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_PICTURE = 1;
    private static final String ARG_PARAM1 = "param1";
    private static ImageView profileImage;
    private static String selectedImagePath = "";
    private int mParam1;
    private int mTotalScrolled = 0;
    private Boolean isInitCamera = false;
    private ProfileAdapter adapter;
    private SweetAlertDialog progressDialog;
    StudentDetails studentDetailsData = new StudentDetails();

    @Bind(R.id.recycler_view)RecyclerView recyclerView;
    @Bind(R.id.toolbar)Toolbar mToolbar;
    @Bind(R.id.toolbar_frame)FrameLayout toolbarFrame;
    @Bind(R.id.layout_edit_profile)FrameLayout layoutEditProfile;
    @Bind(R.id.layout_cancel_profile)FrameLayout layoutCancelProfile;
    @Bind(R.id.button_edit_save)TextView buttonEditSave;

    public static ProfileFragment newInstance(int param1) {

        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
        }

        String currentUsername = new TinyDB(getActivity()).getString(UserConst.USERNAME);
        GlobalConfig.getInstance().getEventBus().register(this);

        new GetUserProfileAPI(getActivity()).execute(currentUsername);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initToolbarNav(mToolbar);
        QuickUtils.addStatusBarGab(getActivity(), toolbarFrame);

        StudentDetails studentDetails = new StudentDetails();
        Object objStudentDetails =  new TinyDB(getActivity()).getObject(UserConst.USER_OBJECT_CACHE, StudentDetails.class);
        if(objStudentDetails != null && objStudentDetails != ""){
            studentDetails = (StudentDetails)objStudentDetails;
            studentDetails.setPhotoURL(null);
        }

        initView(studentDetails);

        initToolbarAction();
    }

    private void resetToolbar(){
        selectedImagePath = "";
        buttonEditSave.setText(getString(R.string.edit));
        layoutCancelProfile.setVisibility(View.GONE);
        mToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        ImageView buttonCamera = (ImageView) getActivity().findViewById(R.id.button_camera);
        buttonCamera.setVisibility(View.GONE);
    }

    private void initToolbarAction(){
        // edit or save user profile photo
        layoutEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentState = buttonEditSave.getText().toString();
                if(currentState.equals(getString(R.string.edit))){

                    mToolbar.setNavigationIcon(null);
                    buttonEditSave.setText(getString(R.string.save));
                    layoutCancelProfile.setVisibility(View.VISIBLE);
                    ImageView buttonCamera = (ImageView) getActivity().findViewById(R.id.button_camera);
                    profileImage = (ImageView) getView().findViewById(R.id.profile_image);
                    buttonCamera.setVisibility(View.VISIBLE);

                    buttonCamera.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            uploadPhotoDialog();
                        }
                    });
                }else{
                    // upload profile photo
                    if(selectedImagePath != null && selectedImagePath.length() > 0){
                        SweetAlertOptions sweetAlertOptions = new SweetAlertOptions();
                        sweetAlertOptions.setAlertType(SweetAlertDialog.PROGRESS_TYPE);
                        sweetAlertOptions.setTittle("Loading");
                        sweetAlertOptions.setIsCancelable(false);
                        sweetAlertOptions.setBarColor(R.color.primary_orange);
                        progressDialog = QuickUtils.showSweetAlertDialog(getActivity(), sweetAlertOptions);

                        new UploadPhotoAPI(getActivity()).execute(selectedImagePath, QuickUtils.getCurrentUsername(getContext()));
                    }else{
                        resetToolbar();
                    }

                }
            }
        });

        // cancel edit profile
        layoutCancelProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetToolbar();
                initView(studentDetailsData);
            }
        });
    }

    private void uploadPhotoDialog() {
        AlertDialog.Builder photoOptionsDialog = new AlertDialog.Builder(getActivity());
        photoOptionsDialog.setTitle(getString(R.string.photo_dialog_title));
        photoOptionsDialog.setMessage(getString(R.string.photo_dialog_message));

        photoOptionsDialog.setPositiveButton("Gallery",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    Intent pictureActionIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pictureActionIntent, GALLERY_PICTURE);

                }
            });

        photoOptionsDialog.setNegativeButton("Camera",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA_REQUEST);
                }
            });

        photoOptionsDialog.show();
    }

    private void updateNavigationProfilePhoto(String photoUrl){
        LoginObj loginObj = (LoginObj) new TinyDB(getActivity()).getObject(UserConst.USER, LoginObj.class);

        if(photoUrl != null && photoUrl != "" && loginObj.getPersonalPhotoURL() != photoUrl){
            if (loginObj != null) {
                loginObj.setPersonalPhotoURL(photoUrl);
                new TinyDB(getContext()).putObject(UserConst.USER, loginObj);
            }

            ImageView profileImage = (ImageView) getActivity().findViewById(R.id.profile_image_navigation);
            new ImageLoader.DownloadImageTask(profileImage)
                    .execute(photoUrl);
        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getActivity());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath() + "/profile.jpg";
    }

    // camera or gallery photo selected callback
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        if(resultCode == RESULT_OK){
            switch (requestCode)
            {
                case CAMERA_REQUEST:
                case GALLERY_PICTURE:

                    Uri selectedImage = imageReturnedIntent.getData();
                    Bitmap bitmap;
                    String[] filePath = { MediaStore.Images.Media.DATA };
                    Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    selectedImagePath = c.getString(columnIndex);
                    c.close();

                    float rotation = 0;

                    try{
                        ExifInterface ei = new ExifInterface(selectedImagePath);
                        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                ExifInterface.ORIENTATION_UNDEFINED);

                        switch(orientation) {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                rotation = 90;
                                break;

                            case ExifInterface.ORIENTATION_ROTATE_180:
                                rotation = 180;
                                break;

                            case ExifInterface.ORIENTATION_ROTATE_270:
                                rotation = 270;
                                break;

                            case ExifInterface.ORIENTATION_NORMAL:

                            default:
                                break;
                        }
                    }catch (Exception ex){
                        Log.d(this.getClass().getName(), ex.toString());
                    }

                    bitmap = QuickUtils.decodeImage(selectedImagePath, UtilConst.MEDIUM_DECODE_SIZE, true);
                    bitmap = QuickUtils.rotateImage(bitmap, rotation);

                    String tempImageFile = saveToInternalStorage(bitmap);
                    selectedImagePath =  tempImageFile;

                    profileImage.setImageBitmap(bitmap);

                    break;

            }
        }
    }

    // get user profile api callback
    @Subscribe
    public void UserProfilePayload(UserProfilePayload dataPayload) {

        Log.d(this.getClass().getSimpleName(), "Entering event bus");

        if (dataPayload.getStatusCode().intValue() == ErrorCodes.SUCCESS_CODE) {
            studentDetailsData = dataPayload.getStudentDetails().get(0);
            updateNavigationProfilePhoto(studentDetailsData.getPhotoURL());
            initView(studentDetailsData);
            new TinyDB(getActivity()).putObject(UserConst.USER_OBJECT_CACHE, dataPayload.getStudentDetails().get(0));
        }
    }

    // upload photo api callback
    @Subscribe
    public void UploadPhotoPayload(UploadPhotoPayload dataPayload) {

        progressDialog.dismissWithAnimation();
        Log.d(this.getClass().getSimpleName(), "Entering event bus");

        if (dataPayload.getStatusCode() == ErrorCodes.SUCCESS_CODE) {
            Log.d("Upload Photo","Upload photo success.");

            updateNavigationProfilePhoto(dataPayload.getPhotoUrl());

            resetToolbar();
        } else {
            QuickUtils.runToast(getActivity(), dataPayload.getStatusMessage());
        }
    }

    public void initView(StudentDetails studentDetails) {
        List<CommonStickyListObject> datas = new ArrayList<>();

        String gender = null;
        String nationality = null;
        String city = null;
        String state = null;
        String country = null;
        String documentType = null;
        String dob = null;

        try {
            GeneralMasterData genderMasterData = new Select().from(GeneralMasterData.class).where("Code=?", studentDetails.getGender()).executeSingle();
            if (genderMasterData != null) {
                gender = genderMasterData.getValue();
            }

            GeneralMasterData nationalityMasterData = new Select().from(GeneralMasterData.class).where("Code=?", studentDetails.getNationality()).executeSingle();
            if (nationalityMasterData != null) {
                nationality = nationalityMasterData.getValue();
            }

            GeneralMasterData cityMasterData = new Select().from(GeneralMasterData.class).where("Code=?", studentDetails.getCity()).executeSingle();
            if (cityMasterData != null) {
                city = cityMasterData.getValue();
            }

            GeneralMasterData stateMasterData = new Select().from(GeneralMasterData.class).where("Code=?", studentDetails.getState()).executeSingle();
            if (stateMasterData != null) {
                state = stateMasterData.getValue();
            }

            GeneralMasterData countryMasterData = new Select().from(GeneralMasterData.class).where("Code=?", studentDetails.getCountry()).executeSingle();
            if (countryMasterData != null) {
                country = countryMasterData.getValue();
            }
            GeneralMasterData documentTypeMasterData = new Select().from(GeneralMasterData.class).where("Code=?", studentDetails.getDocumentType()).executeSingle();
            if (documentTypeMasterData != null) {
                documentType = documentTypeMasterData.getValue();
            }

            DateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
            DateFormat toFormat = new SimpleDateFormat("dd MMM yyyy");
            Date date = fromFormat.parse(studentDetails.getDOB());
            dob = toFormat.format(date);
        } catch (Exception ex) {

        }

        datas.add(new CommonStickyListObject(0, "PROFILE PIC", "", "", null, true, true, 0));

        // public CommonStickyListObject(long id, String title, String subTitle, ImageView image, boolean isHeader, boolean isVisible, int sectionFirstPosition) {
        datas.add(new CommonStickyListObject(1, "Gender", gender, "Required", null, false, true, 0));
        datas.add(new CommonStickyListObject(2, "Date Of Birth", dob, "Required", null, false, true, 0));
        datas.add(new CommonStickyListObject(3, "Nationality", nationality, "Required", null, false, true, 0));
        datas.add(new CommonStickyListObject(4, "Email", studentDetails.getEmail(), "Required", null, false, true, 0));
        datas.add(new CommonStickyListObject(5, "Contact Number", studentDetails.getPhoneNumber(), "Required", null, false, true, 0));

        datas.add(new CommonStickyListObject(6, "Emergency Contact", "", "", null, true, true, 1));
        datas.add(new CommonStickyListObject(7, "Contact Person", studentDetails.getEmergencyContactName(), "", null, false, true, 1));
        datas.add(new CommonStickyListObject(8, "Mobile Number", studentDetails.getEmergencyContactNumber(), "", null, false, true, 1));

        datas.add(new CommonStickyListObject(9, "Address", "", "", null, true, true, 1));
        datas.add(new CommonStickyListObject(10, "Address 1", studentDetails.getAddress1(), "Optional", null, false, true, 2));
        datas.add(new CommonStickyListObject(11, "Address 2", studentDetails.getAddress2(), "Optional", null, false, true, 2));
        datas.add(new CommonStickyListObject(12, "City", city, "Optional", null, false, true, 2));
        datas.add(new CommonStickyListObject(13, "State", state, "Optional", null, false, true, 2));
        datas.add(new CommonStickyListObject(14, "Country", country, "Optional", null, false, true, 2));
        datas.add(new CommonStickyListObject(15, "Postcode", studentDetails.getPostCode(), "Optional", null, false, true, 2));

//        datas.add(new CommonStickyListObject(16, "Corresponding Address", "", null, true, true, 3));
//        datas.add(new CommonStickyListObject(17, "Address 1", studentDetails.getAddress1(), null, false, true, 3));
//        datas.add(new CommonStickyListObject(18, "Address 2", studentDetails.getAddress2(), null, false, true, 3));
//        datas.add(new CommonStickyListObject(19, "City", city, null, false, true, 3));
//        datas.add(new CommonStickyListObject(20, "State", state, null, false, true,3));
//        datas.add(new CommonStickyListObject(21, "Country", country, null, false, true, 3));
//        datas.add(new CommonStickyListObject(22, "Postcode", studentDetails.getPostCode(), null, false, true, 3));

        datas.add(new CommonStickyListObject(23, "Document Type", "", "", null, true, true, 3));
        datas.add(new CommonStickyListObject(24, documentType, studentDetails.getDocNumber(), "", null, false, true, 3));

        if (studentDetails.getApplicantTypeCode() != null) {
            if (studentDetails.getApplicantTypeCode().equals(UserConst.USER_TYPE_CODE)){
                // student details
                datas.add(new CommonStickyListObject(25, "Student Info", "", "", null, true, true, 4));
                datas.add(new CommonStickyListObject(26, "Admit Type", studentDetails.getAdmitType(), "Optional", null, false, true, 4));
                datas.add(new CommonStickyListObject(27, "Program Code", studentDetails.getProgramCode(), "Optional", null, false, true, 4));
//                datas.add(new CommonStickyListObject(28, "Program Description", studentDetails.getDesignation(), "Optional", null, false, true, 4));
                datas.add(new CommonStickyListObject(29, "Cohort", studentDetails.getCohort(), "Optional", null, false, true, 4));
                datas.add(new CommonStickyListObject(30, "Semester", studentDetails.getCurrentSemester(), "Optional", null, false, true, 4));
                datas.add(new CommonStickyListObject(31, "Year", studentDetails.getCurrentYear(), "Optional", null, false, true, 4));
                datas.add(new CommonStickyListObject(32, "Term", studentDetails.getCurrentTerm(), "Optional", null, false, true, 4));
                datas.add(new CommonStickyListObject(33, "Term Begin", studentDetails.getCurrentActivatedTermBeginDate(), "Optional", null, false, true, 4));
                datas.add(new CommonStickyListObject(34, "Term End", studentDetails.getCurrentActivatedTermEndDate(), "Optional", null, false, true, 4));
                datas.add(new CommonStickyListObject(35, "Campus", studentDetails.getCampus(), "Optional", null, false, true, 4));
                datas.add(new CommonStickyListObject(36, "Career", studentDetails.getCareer(),"Optional",  null, false, true, 4));
                datas.add(new CommonStickyListObject(37, "Sponsor Code", studentDetails.getSponsorCode(), "Optional", null, false, true, 4));
            } else {
                // staff details
                datas.add(new CommonStickyListObject(38, "Staff Info", "", "Optional", null, true, true, 5));
                datas.add(new CommonStickyListObject(39, "Employment Type", studentDetails.getEmploymentType(), "Optional", null, false, true, 5));
                datas.add(new CommonStickyListObject(40, "Staff Type", studentDetails.getStaffType(), "Optional", null, false, true, 5));
                datas.add(new CommonStickyListObject(41, "Designation", studentDetails.getDesignation(), "Optional", null, false, true, 5));
                datas.add(new CommonStickyListObject(42, "School", studentDetails.getSchool(), "Optional", null, false, true, 5));
                datas.add(new CommonStickyListObject(42, "Department", studentDetails.getDepartment(), "Optional", null, false, true, 5));
                datas.add(new CommonStickyListObject(43, "Division", studentDetails.getDivision(), "Optional", null, false, true, 5));
            }
        }


        adapter = new ProfileAdapter(getActivity(), datas, studentDetails, mToolbar);
//
        HeaderLayoutManagerFixed manager = new HeaderLayoutManagerFixed(getActivity());


//        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
//        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);



//        adapter.setParallaxHeader(getActivity().getLayoutInflater().inflate(R.layout.header_picture_profile, recyclerView, false), recyclerView);
//        adapter.setOnParallaxScroll(new ParallaxRecyclerAdapter.OnParallaxScroll() {
//            @Override
//            public void onParallaxScroll(float percentage, float offset, View parallax) {
//                Drawable c = mToolbar.getBackground();
//                Log.d("ProfileAdapter", "toolbar backgound: " + percentage + " : " + offset);
//                c.setAlpha(Math.round(percentage * 255));
//                mToolbar.setBackground(c);
//            }
//        });
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mTotalScrolled += dy;

                //calculate percentage
                float scrolledPercentage = ((float)mTotalScrolled/(float)(adapter.getProfilePictureHeader().getHeight()*4));

                if (scrolledPercentage > 0.25f){
                    scrolledPercentage = 0.25f;
                }
                int scrolledInt = Math.round(scrolledPercentage * 255);
                Log.d("ProfileFragment", "scrolled percentage: " +scrolledInt );

                Drawable c = mToolbar.getBackground();
                c.setAlpha(scrolledInt);
                mToolbar.setBackground(c);
            }
        });

    }


}
