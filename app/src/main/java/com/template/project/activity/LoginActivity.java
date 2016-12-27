package com.template.project.activity;

import android.os.Bundle;

import com.crashlytics.android.Crashlytics;
import com.template.project.R;
import com.template.project.fragments.LoginFragment;
import com.template.project.utils.QuickUtils;

import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;
import me.yokeyword.fragmentation.SupportActivity;

public class LoginActivity extends SupportActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        QuickUtils.setStatusBarTranslucent(LoginActivity.this, true);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


        if (savedInstanceState == null) {
            loadRootFragment(R.id.container, LoginFragment.newInstance(0));
        }



    }


}
