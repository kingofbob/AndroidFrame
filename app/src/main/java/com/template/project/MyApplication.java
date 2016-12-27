package com.template.project;

import android.support.multidex.MultiDexApplication;

import com.activeandroid.ActiveAndroid;
import com.mobsandgeeks.saripaar.Validator;
import com.template.project.validators.CustomEmailPhoneValidator;

/**
 * Created by 00020443 on 14/10/2016.
 */

public class MyApplication extends MultiDexApplication{
    @Override
    public void onCreate() {
        super.onCreate();
//        SugarContext.init(getApplicationContext());
        ActiveAndroid.initialize(this);
        Validator.registerAnnotation(CustomEmailPhoneValidator.class);

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
//        SugarContext.terminate();

    }


}