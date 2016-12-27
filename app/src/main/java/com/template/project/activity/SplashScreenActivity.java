package com.template.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.template.project.R;
import com.template.project.connections.MasterDataAPI;
import com.template.project.constants.ErrorCodes;
import com.template.project.constants.UserConst;
import com.template.project.objects.MasterDataPayload;
import com.template.project.utils.GlobalConfig;
import com.template.project.utils.QuickUtils;
import com.template.project.utils.TinyDB;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.services.concurrency.AsyncTask;
import me.yokeyword.fragmentation.SupportActivity;

public class SplashScreenActivity extends SupportActivity {

    @Bind(R.id.progress)
    AVLoadingIndicatorView progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalConfig.getInstance().getEventBus().register(this);

        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);
        QuickUtils.setStatusBarTranslucent(SplashScreenActivity.this, true);
        new MasterDataAPI(this).execute();

        QuickUtils.setStatusBarColor(this, R.color.primary_darker);

        progressBar.show();
    }


    @Subscribe
    public void onEvent(MasterDataPayload masterDataPayload) {

        Log.d(this.getClass().getSimpleName(), "Entering event bus");

        if (masterDataPayload.getStatusCode() == ErrorCodes.SUCCESS_CODE) {
            new AsyncTask<MasterDataPayload, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected Void doInBackground(MasterDataPayload... masterDataPayloads) {


                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                }
            }.execute(masterDataPayload);
        } else {
            QuickUtils.runToast(SplashScreenActivity.this, masterDataPayload.getStatusMessage());
        }


        //go to login screen or to main


      finish();
      if (new TinyDB(SplashScreenActivity.this).getString(UserConst.USERNAME).length()<=0){
          Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
          startActivity(intent);
      }else{
          Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
          startActivity(intent);
      }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GlobalConfig.getInstance().getEventBus().unregister(this);
    }


}
