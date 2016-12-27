package com.template.project.constants;

import android.content.Context;

import com.template.project.BuildConfig;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.message.BasicHeader;

/**
 * Created by 00020443 on 13/10/2016.
 */

public class HeaderConst {
    public final static String REQUEST_DATETIME = "REQUEST_DATETIME";
    public final static String SESSION_KEY = "SESSION_KEY";
    public final static String SYNC_DATE = "syncdate";
    public final static String APP_TYPE = "APP_TYPE";
    public final static String APP_TYPE2 = "appType";
    public final static String APP_VERSION = "AppVersion";
    public final static String CONTENT_TYPE = "Content-Type";
    public final static String RESPONSE_TIME = "RESPOND_TIME";
    public final static String PlatformCode = "PlatformCode";

    public static Header[] getHeader(Context context, String lastSyncDateConst){
        Header[] headers = {
                new BasicHeader(APP_VERSION, BuildConfig.VERSION_NAME.replace("v", "")),
//                new BasicHeader(APP_TYPE, "android"),
                new BasicHeader(CONTENT_TYPE, "application/json; charset=utf-8"),
                new BasicHeader(APP_TYPE, ApplicationConst.APP_TYPE),
                new BasicHeader(APP_TYPE2, ApplicationConst.APP_TYPE),
                new BasicHeader(PlatformCode, ApplicationConst.PLATFORM_CODE)
//                new BasicHeader(SYNC_DATE , new TinyDB(context).getString(lastSyncDateConst)),
//                new BasicHeader(SESSION_KEY , new TinyDB(context).getString(UtilConst.SESSION_KEY))
        };

        return headers;
    }

    public static Header[] getHeaderMultiPartData(Context context, String lastSyncDateConst){
        Header[] headers = {
                new BasicHeader(APP_VERSION, BuildConfig.VERSION_NAME.replace("v", "")),
//                new BasicHeader(APP_TYPE, "android"),
//                new BasicHeader(CONTENT_TYPE, "multipart/form-data; boundary="),
                new BasicHeader(APP_TYPE, ApplicationConst.APP_TYPE)
//                new BasicHeader(SYNC_DATE , new TinyDB(context).getString(lastSyncDateConst)),
//                new BasicHeader(SESSION_KEY , new TinyDB(context).getString(UtilConst.SESSION_KEY))
        };

        return headers;
    }

}
