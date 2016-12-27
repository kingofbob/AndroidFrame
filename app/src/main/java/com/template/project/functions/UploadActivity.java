package com.template.project.functions;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.template.project.connections.UploadActivityAPI;
import com.template.project.constants.UserConst;
import com.template.project.utils.QuickUtils;
import com.template.project.utils.TinyDB;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by 00020443 on 22/11/2016.
 */

public class UploadActivity {
    public static void uploadLogs(final Context context, final AVLoadingIndicatorView progressBar) {


        new AsyncTask<Void,Void,String>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar.show();
            }

            @Override
            protected String doInBackground(Void... voids) {

                BufferedReader bufferedReader = null;
                BufferedWriter writer = null;
                String filename = "logfile";
                try {
                    Process process = Runtime.getRuntime().exec("logcat -d");
                    bufferedReader = new BufferedReader(
                            new InputStreamReader(process.getInputStream()));

                    File removeFile = new File(context.getCacheDir() +"/" + filename + ".txt");
                    if (removeFile.exists()){
                        Log.d(UploadActivity.class.getSimpleName(), "logfile.txt exist, deleting...");
                        removeFile.delete();
                    }
                    removeFile.createNewFile();

                    FileWriter fw = new FileWriter(removeFile.getAbsoluteFile());


                    writer = new BufferedWriter(fw);

                    StringBuilder log = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        writer.write(line + "\n");
                    }

                    File zippedFile = QuickUtils.zipFile(removeFile, filename + ".zip");


                   return zippedFile.getPath();

                } catch (IOException e) {
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException eio) {
                            e.printStackTrace();
                        }
                    }

                    if (writer != null) {
                        try {
                            writer.close();
                        } catch (IOException eio) {
                            e.printStackTrace();
                        }
                    }
                }

                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (s != null){
                    String username =  new TinyDB(context).getString(UserConst.USERNAME);
                    username = username.length()>0?username:"guest";
                    new UploadActivityAPI(context).execute(s, username);
                }else{
                    progressBar.hide();
                }

            }
        }.execute();


    }
}
