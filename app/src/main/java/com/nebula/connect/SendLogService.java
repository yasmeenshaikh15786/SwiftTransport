package com.nebula.connect;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;


import com.nebula.connect.db.DBAdapter;
import com.nebula.connect.logreports.Logger;
import com.nebula.connect.queries.InsertQueries;
import com.nebula.connect.queries.SelectQueries;
import com.nebula.connect.utilities.MultipartUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by Sonam on 11/1/17.
 */
public class SendLogService extends Service {
    private String zipFilePath;

    private static final String TAG=SendLogService.class.getSimpleName();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);
        if (ContextCommons.isOnline(this)) {
            new SendLog(getApplicationContext()) {
                @Override
                protected void afterExecution(boolean result, String msg) {
                    if (result) {

                    } else if ("403".equals(msg)) {
                        // expire
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getApplicationContext());
                        builder.setMessage("Session expired. Please login again.")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                        InsertQueries.setSetting(getApplicationContext(), Settings.PASSWORD, "");
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                });
                        android.support.v7.app.AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    }
                }
            }.execute();
        } else {
            Toast.makeText(getApplicationContext(), R.string.enable_internet, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }


    public abstract class SendLog extends AsyncTask<Void, Void, Boolean> {
        private final String TAG = SendLog.class.getSimpleName();
        private Context context;
        JSONObject json;
        private String response;
        String errMsg = "Error sending log";

        public SendLog(Context context) {
            Logger.d(TAG, "inside SendLog");
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            Logger.d(TAG, "inside onPreExecute");
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Logger.d(TAG, "inside doInBackground");

            boolean retVal = false;

            try {String charset = "UTF-8";
                String requestURL = Constants.SEND_LOG;
                File dir = new File(Constants.fldr);
                for (File file : dir.listFiles()) {
                    if (file.getName().endsWith((".zip"))) {
                        file.delete();
                    }
                }

                zipFilePath=Constants.fldr+System.currentTimeMillis()+".zip";
                generateZipFile();
                MultipartUtility multipart = new MultipartUtility(context, requestURL, charset);
                String deviceId = SelectQueries.getSetting(context,Settings.DEVICE_ID);
                if (zipFilePath != null) {
                    File signage = new File(zipFilePath);
                    multipart.addFilePart("image", signage);
                }
                multipart.addFormField("device_id", deviceId);
                response = multipart.finish();
                Logger.d(TAG, "response=" + response.toString());

                if (response.contains("Error from server")) {
                    retVal = false;
                } else {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    String status = jsonObject.getString("status");
                    if ("success".equals(status)) {


                        retVal = true;
                    } else {
                        errMsg = jsonObject.optString("msg", "");
                        retVal = false;
                    }
                }

            } catch (IOException ex) {
                Logger.e(TAG, ex);
                ex.printStackTrace();
                retVal = false;
            } catch (JSONException e) {
                Logger.e(TAG, e);
                e.printStackTrace();
                retVal = false;
            }
            return retVal;
        }

        protected abstract void afterExecution(boolean result, String msg);

        @Override
        protected void onPostExecute(Boolean result) {
            Logger.d(TAG, "inside onPostExecute=" + result);
            afterExecution(result, errMsg);

        }
    }

    private void generateZipFile() {
        Logger.d(TAG,"inside generateZipFile");
        String backupDBPath = Constants.fldr+ "/"+ System.currentTimeMillis() +".db";
        String currentDB = this.getDatabasePath(DBAdapter.DB_NAME).getAbsolutePath();
        Commons.copyTextFile(currentDB, backupDBPath);

        // declare an array for storing the files i.e the path
        // of your source files
        String[] s = new String[2];

        // Type the path of the files in here
        s[0] = backupDBPath;
        s[1] = Constants.logFileName;

        zip(s, zipFilePath);
        File dbFile = new File(backupDBPath);
        if(dbFile.exists()){
            dbFile.delete();
        }
    }

    private void zip(String[] _files, String zipFileName) {
        Logger.d(TAG,"inside zip");
        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(zipFileName);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            byte data[] = new byte[1024];
            Logger.d(TAG,"byte 1024");
            for (int i = 0; i < _files.length; i++) {
                Log.d(TAG,"file length"+_files.length);
                Log.d(TAG,"i = "+i);
                Log.v("Compress", "Adding: " + _files[i]);
                Logger.d("Compress", "Adding: ");
                FileInputStream fi = new FileInputStream(_files[i]);
                origin = new BufferedInputStream(fi, 1024);

                String fileName = "";
                if(i==0){
                    fileName = _files[i].substring(_files[i].lastIndexOf("/") + 1);
                }else {
                    fileName = _files[i].substring(_files[i].lastIndexOf("/") + 2);
                }
                ZipEntry entry = new ZipEntry(fileName);
                out.putNextEntry(entry);
                int count;

                while ((count = origin.read(data, 0, 1024)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }

            out.close();
        } catch (Exception e) {
            Logger.e(TAG,e);
            e.printStackTrace();
        }
    }


}
