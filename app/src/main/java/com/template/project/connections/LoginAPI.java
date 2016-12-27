package com.template.project.connections;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.activeandroid.query.Select;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.template.project.BuildConfig;
import com.template.project.R;
import com.template.project.constants.ErrorCodes;
import com.template.project.constants.HeaderConst;
import com.template.project.constants.SyncDateConst;
import com.template.project.constants.SystemConfigConst;
import com.template.project.constants.URLConst;
import com.template.project.constants.UserConst;
import com.template.project.constants.UtilConst;
import com.template.project.database.entities.SystemConfig;
import com.template.project.objects.LoginResponsePayload;
import com.template.project.utils.AESBase64;
import com.template.project.utils.GlobalConfig;
import com.template.project.utils.QuickUtils;
import com.template.project.utils.TinyDB;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;


/**
 * Created by 00020443 on 26/1/2016.
 */

public class LoginAPI {

    private Context context;
    private AsyncHttpClient client;
    private String url = URLConst.LoginAPI;

    /**
     * Instantiates a new Dprm connection.
     *
     * @param context the context
     */
    public LoginAPI(Context context) {
        this.context = context;
        client = new AsyncHttpClient();

        client.setTimeout(UtilConst.HTTP_TIMEOUT);
        client.setMaxRetriesAndTimeout(UtilConst.HTTP_RETRIES, UtilConst.HTTP_TIMEOUT);

    }

    /**
     * Exec connection.
     */
    public void execute(String username, String password) {
        // url is the URL to download.

        //check internet
        if(!QuickUtils.isNetworkConnected(context)){
            exeFailed(0, context.getResources().getString(R.string.error_no_internet));
            return;
        }

        Log.i(LoginAPI.class.getSimpleName(), "Running WebService: " + url);
        try {
            JSONObject payload = new JSONObject();

            payload.put("username", username);
            SystemConfig saltKey = new Select().from(SystemConfig.class).where("Config_Code=?", SystemConfigConst.PASSWORD_SALT_KEY).executeSingle();
            SystemConfig publicKey = new Select().from(SystemConfig.class).where("Config_Code=?", SystemConfigConst.PASSWORD_PUBLIC_KEY).executeSingle();

//            Log.d(LoginAPI.class.getSimpleName(), "SaltKey: " + saltKey.getConfig_Value());
//            Log.d(LoginAPI.class.getSimpleName(), "publicKey: " + publicKey.getConfig_Value());
            String encryptedPassword = AESBase64.encrypt(saltKey.getConfig_Value(),publicKey.getConfig_Value(), password );
//            Log.d(LoginAPI.class.getSimpleName(), "encryptedPassword: " + encryptedPassword);
            payload.put("password", encryptedPassword);

            StringEntity entity = new StringEntity(payload.toString());

            Log.d(LoginAPI.class.getSimpleName(), "Payload: " + payload.toString() + " version: " +  BuildConfig.VERSION_NAME);


            client.post(context, url, HeaderConst.getHeader(context,""), entity, "application/json; charset=utf-8", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    try {

                        String str = new String(responseBody, "UTF-8"); // for UTF-8 encoding
                        Log.d(LoginAPI.class.getSimpleName(), "Payload response: " + str);
                        Log.d(LoginAPI.class.getSimpleName(), "Header response time: " + QuickUtils.getHeaderValue(headers, HeaderConst.RESPONSE_TIME));
                        exeSuccess(str, QuickUtils.getHeaderValue(headers, HeaderConst.RESPONSE_TIME));


                    } catch (Exception e) {
                        Log.e(LoginAPI.class.getSimpleName(), "execute", e);

                    }


                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    if (error != null){
                        exeFailed(0, error.getMessage());
                    }else{
                        exeFailed(0, context.getResources().getString(R.string.error_connection_problem));
                    }

                }

                @Override
                public void onStart() {
                    super.onStart();

                }

                @Override
                public void onFinish() {
                    super.onFinish();

                }

                @Override
                public void onRetry(int retryNo) {
                    super.onRetry(retryNo);

                }

                @Override
                public void onCancel() {
                    super.onCancel();

                }
            });

        }  catch (Exception e) {
            Log.e(LoginAPI.class.getSimpleName(), "execute", e);
            exeFailed(0, e.getMessage());

        }
    }


    private void exeSuccess(String response, String syncDate){
        new AsyncTask<String,Void,LoginResponsePayload>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected LoginResponsePayload doInBackground(String... params) {
                LoginResponsePayload loginResponsePayload = null;
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    QuickUtils.longLogcat(LoginAPI.class.getSimpleName(), "Data to map: " + params[0]);
                    loginResponsePayload = mapper.readValue(params[0], LoginResponsePayload.class);
                    Log.i(LoginAPI.class.getSimpleName(), "Data to mapped: " +loginResponsePayload.toString());

                    if (loginResponsePayload.getStatusCode() != ErrorCodes.SUCCESS_CODE){
                        return loginResponsePayload;
                    }

                    new TinyDB(context).putString(UserConst.USERNAME, loginResponsePayload.getLoginObj().getUsername().toString());

                    new TinyDB(context).putObject(UserConst.USER, loginResponsePayload.getLoginObj());
                    new TinyDB(context).putObject(UserConst.USER_DETAILS, loginResponsePayload.getUserDetailsObj());
                    new TinyDB(context).putString(SyncDateConst.LOGIN_DATA, params[1]==null?"":params[1]);

                }catch (Exception e){
                    Log.e(LoginAPI.class.getSimpleName(), "execute", e);
                    loginResponsePayload = new LoginResponsePayload();
                    loginResponsePayload.setStatusCode(0);
                    loginResponsePayload.setStatusMessage(context.getResources().getString(R.string.error_parsing_data));
                }


                return loginResponsePayload;
            }

            @Override
            protected void onPostExecute(LoginResponsePayload loginResponsePayload) {
                super.onPostExecute(loginResponsePayload);

                if (loginResponsePayload.getStatusCode() == ErrorCodes.SUCCESS_CODE){
                    GlobalConfig.getInstance().getEventBus().post(loginResponsePayload);
                }else{
                    exeFailed(loginResponsePayload.getStatusCode(),loginResponsePayload.getStatusMessage() );
                }
            }
        }.execute(response, syncDate);
    }

    private void exeFailed(int errorCode, String errorMessage){
        if (errorCode == 0 && errorMessage == null){
            errorMessage = context.getString(R.string.too_long_respond);
        }
        Log.i(LoginAPI.class.getSimpleName(), "Failed with error code = " + errorCode + " errorMessage = " + errorMessage );
        LoginResponsePayload loginResponsePayload = new LoginResponsePayload();
        loginResponsePayload.setStatusCode(errorCode);
        loginResponsePayload.setStatusMessage(errorMessage);
        GlobalConfig.getInstance().getEventBus().post(loginResponsePayload);
    }



}