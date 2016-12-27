package com.template.project.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.template.project.R;
import com.template.project.constants.UserConst;
import com.template.project.customui.RoundCornersDrawable;
import com.template.project.database.entities.GeneralMasterData;
import com.template.project.objects.SweetAlertOptions;
import com.wang.avi.AVLoadingIndicatorView;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cz.msebera.android.httpclient.Header;

/**
 * Created by 00020443 on 15/9/2016.
 */
public class QuickUtils {
    private static AVLoadingIndicatorView progressBar;

    public static String getSqlDateTime(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return simpleDateFormat.format(date);
    }

    public static Date getDateFromSQL(String datetime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date date = null;
        try {
            date = simpleDateFormat.parse(datetime);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return date;
    }

    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public static void runToast(final Activity context, final String message) {
        context.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void longLogcat(String Tag, String veryLongString) {
        int maxLogSize = 1000;
        for (int i = 0; i <= veryLongString.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i + 1) * maxLogSize;
            end = end > veryLongString.length() ? veryLongString.length() : end;
            Log.i(Tag, veryLongString.substring(start, end));
        }
    }

    public static String getHeaderValue(Header[] headers, String key) {
        for (int t = 0; t < headers.length; t++) {
            Log.d(QuickUtils.class.getSimpleName(), "Header keys: " + headers[t].getName().toString() + " : " + headers[t].getValue().toString());
            if (headers[t].getName().equalsIgnoreCase(key)) {
                return headers[t].getValue();
            }
        }

        return null;
    }

    public static void imageRoundedCorners(Context context, ImageView imageView, CardView cardView, int drawable) {
        Bitmap mBitmap = BitmapFactory.decodeResource(context.getResources(), drawable);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //Default
            imageView.setBackgroundResource(drawable);
        } else {
            //RoundCorners
            RoundCornersDrawable round = new RoundCornersDrawable(mBitmap,
                    context.getResources().getDimension(R.dimen.card_corner_radius), 0); //or your custom radius


            cardView.setPreventCornerOverlap(false); //it is very important

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                imageView.setBackground(round);
            else
                imageView.setBackgroundDrawable(round);
        }
    }

    public static void alertDialog(Context context, String message, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, listener)
                .show();
    }

    public static void underlineText(TextView textview) {
        SpannableString content = new SpannableString(textview.getText().toString());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        textview.setText(content);
    }

    public static File zipFile(File file, String filename) {
        try {

            String filepath = file.getParent()  +"/" +  filename;
            ZipFile zipFile = new ZipFile(filepath);
            Log.d(QuickUtils.class.getSimpleName(), " zipping file name: " + filepath);
            // Folder to add


            // Initiate Zip Parameters which define various properties such
            // as compression method, etc.
            ZipParameters parameters = new ZipParameters();

            // set compression method to store compression
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);

            // Set the compression level
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

            // Add folder to the zip file
            zipFile.addFile(file, parameters);

            return zipFile.getFile();

        } catch (Exception e) {
            Log.e(QuickUtils.class.getSimpleName(), "error", e);
        }

return null;
    }


    public static String removeLastChar(String str) {
        if (str.length()<=0)
            return "";
        return str.substring(0,str.length()-1);
    }


    public static void addStatusBarGab(Context context, View v) {
        if (v != null) {

//            TypedValue tv = new TypedValue();
//            context.getTheme().resolveAttribute(android.support.v7.appcompat.R.attr.actionBarSize, tv, true);
//            paddingTop += TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
            int paddingTop = 0;
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
              paddingTop = getStatusBarHeight(context);
          }

            v.setPadding(0,  paddingTop , 0, 0);
        }

    }

    // A method to find height of the status bar
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    public static void setStatusBarTranslucent(Context context, boolean makeTranslucent) {
        if (makeTranslucent) {
            ((Activity) context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            ((Activity) context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public static String objToJson(Object object){
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writeValueAsString(object);
        }catch (Exception e){
            Log.e(QuickUtils.class.getSimpleName(),"objToJson", e );
        }

        return "";
    }


    public static  <T> T  jsonToObj(String json, Class<T> valueType){
        ObjectMapper mapper = new ObjectMapper();

        try {
            return  mapper.readValue(json, valueType);
        }catch (Exception e){
            Log.e(QuickUtils.class.getSimpleName(),"objToJson", e );
        }

        return null;
    }

    public static void setProgressBar(AVLoadingIndicatorView progressBar) {
        QuickUtils.progressBar = progressBar;
    }

    public static AVLoadingIndicatorView getProgressBar() {
        return QuickUtils.progressBar;
    }

    public static void showProgressBar() {
        if (QuickUtils.progressBar != null){
            QuickUtils.progressBar.setVisibility(View.VISIBLE);
        }
    }

    public static void hideProgressBar() {
        if (QuickUtils.progressBar != null){
            QuickUtils.progressBar.setVisibility(View.GONE);
        }
    }

    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size = 1024;
        try
        {
            byte[] bytes = new byte[buffer_size];
            for(;;)
            {
                int count = is.read(bytes, 0, buffer_size);
                if(count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }

    public static String DateDuration(String startDate, String endDate){
        String dateDuration = "";

        try{
            String startDMY = DateFormats.convertDateStringToString(DateFormats.dtFormat, "dd MMMM yyyy", startDate);
            String endDMY = DateFormats.convertDateStringToString(DateFormats.dtFormat, "dd MMMM yyyy", endDate);

            String startD = DateFormats.convertDateStringToString(DateFormats.dtFormat, "dd", startDate);
            String startM = DateFormats.convertDateStringToString(DateFormats.dtFormat, "MMMM", startDate);
            String startY = DateFormats.convertDateStringToString(DateFormats.dtFormat, "yyyy", startDate);
            String endD = DateFormats.convertDateStringToString(DateFormats.dtFormat, "dd", endDate);
            String endM = DateFormats.convertDateStringToString(DateFormats.dtFormat, "MMMM", endDate);
            String endY = DateFormats.convertDateStringToString(DateFormats.dtFormat, "yyyy", endDate);

            if(startDMY.equals(endDMY)){
                dateDuration = startDMY;
            }else if(!startY.equals(endY)){
                dateDuration = startDMY + " - " + endDMY;
            }else if(!startM.equals(endM)){
                dateDuration = startD + " " + startM + " - " + endDMY;
            }else if(!startD.equals(endD)){
                dateDuration = startD + " - " + endDMY;
            }else{
                dateDuration = startDMY + " - " + endDMY;
            }
        }catch (Exception ex){
            Log.d("DateDuration", ex.toString());
        }

        Log.d("DateDuration", dateDuration);

        return dateDuration;
    }

    public static SweetAlertDialog showSweetAlertDialog(final Activity activity, final SweetAlertOptions sweetAlertOptions){
        SweetAlertDialog dialog = new SweetAlertDialog(activity, sweetAlertOptions.getAlertType());

        String title = sweetAlertOptions.getTittle();
        String content = sweetAlertOptions.getContent();
        Boolean isCancellable = sweetAlertOptions.getIsCancelable();
        Integer barColor = sweetAlertOptions.getBarColor();

        if(sweetAlertOptions.getIsDefault()){

            if(isCancellable == null)
                isCancellable = false;

            switch (sweetAlertOptions.getAlertType())
            {
                case SweetAlertDialog.NORMAL_TYPE:
                    break;

                case SweetAlertDialog.ERROR_TYPE:
                    if(title == null)
                        title = "Error";
                    break;

                case SweetAlertDialog.SUCCESS_TYPE:
                    if(title == null)
                        title = "Success";
                    break;

                case SweetAlertDialog.WARNING_TYPE:
                    if(title == null)
                        title = "Warning";
                    break;

                case SweetAlertDialog.CUSTOM_IMAGE_TYPE:
                    break;

                case SweetAlertDialog.PROGRESS_TYPE:
                    if(title == null)
                        title = "Loading";

                    if(barColor == null)
                        barColor = R.color.primary_orange;
                    break;
            }

        }

        if(title != null)
            dialog.setTitleText(title);

        if(content != null)
            dialog.setContentText(content);

        if(isCancellable != null)
            dialog.setCancelable(isCancellable);

        if(barColor != null)
            dialog.getProgressHelper().setBarColor(barColor);

        if(sweetAlertOptions.getOnDismiss() != null)
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    try {
                        sweetAlertOptions.getOnDismiss().call();
                    } catch(Exception ex){

                    }
                }
            });

        dialog.show();

        return dialog;
    }

    public static String getCurrentUsername(Context context){
        return new TinyDB(context).getString(UserConst.USERNAME);
    }

    //decodes image and scales it to reduce memory consumption
    public static Bitmap decodeImage(String path, int decodeSize, Boolean isLocalFile){
        try {
            //decode image size
            BitmapFactory.Options options = new BitmapFactory.Options();
            InputStream in;
            options.inJustDecodeBounds = true;

            if(isLocalFile){
                in = new FileInputStream(path);
            }else{
                in = new java.net.URL(path).openStream();
            }

            BitmapFactory.decodeStream(in, null, options);

            //Find the correct scale value. It should be the power of 2.
            int width_tmp = options.outWidth, height_tmp = options.outHeight;
            int scale = 1;
            while(true){
                if(width_tmp/2 < decodeSize || height_tmp/2 < decodeSize)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }

            //decode with inSampleSize
            BitmapFactory.Options optionsFinal = new BitmapFactory.Options();
            optionsFinal.inSampleSize = scale;

            if(isLocalFile){
                in = new FileInputStream(path);
            }else{
                in = new java.net.URL(path).openStream();
            }

            return BitmapFactory.decodeStream(in, null, optionsFinal);

        } catch (Exception e) {
            Log.d("Error", e.toString());
        }
        return null;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public static void setStatusBarColor(Activity activity, int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(activity, color));
        }
    }

    public static GeneralMasterData getGeneralMasterDataByCode(String code){
        GeneralMasterData masterData = new Select().from(GeneralMasterData.class).where("Code=?", code).executeSingle();
        return masterData;
    }

    public static String convertObjectToString(Object object){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        }catch (JsonMappingException e){
            Log.w(QuickUtils.class.getSimpleName(),"convertObjectToString", e );
        }catch (JsonProcessingException e){
            Log.w(QuickUtils.class.getSimpleName(),"convertObjectToString", e );
        }

        return "";
    }
}
