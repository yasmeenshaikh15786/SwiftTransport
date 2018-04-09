package com.nebula.connect;

import android.content.Context;
import android.os.AsyncTask;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.nebula.connect.logreports.Logger;
import com.nebula.connect.queries.InsertQueries;
import com.nebula.connect.queries.SelectQueries;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Sonam on 11/1/17.
 */
public class MyFIrebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FCMInitializationService";

    @Override
    public void onTokenRefresh() {
        String fcmToken = FirebaseInstanceId.getInstance().getToken();
        InsertQueries.setSetting(getApplicationContext(), Settings.FCM_TOKEN,fcmToken);
           new SendFcmTokenTask(getApplicationContext()).execute();
    }

    public class SendFcmTokenTask extends AsyncTask<Void, Void, Boolean> {
        private  final String TAG=SendFcmTokenTask.class.getSimpleName();
        private Context context;
        JSONObject json;
        String name;
        String errMsg = "Error sending token";

        public SendFcmTokenTask(Context context) {
            Logger.d(TAG,"inside GetShopTask");
            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            Logger.d(TAG,"inside onPreExecute");
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Logger.d(TAG,"inside doInBackground");

            boolean retVal=false;

            try {
                URL url=new URL(Constants.FCM_TOKEN_URL);
                Logger.d(TAG,"url="+url.toString());
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setDoInput (true);
                urlConnection.setDoOutput (true);
                urlConnection.setUseCaches (false);
                urlConnection.setRequestMethod( "POST" );
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("X-CSRF-Token", SelectQueries.getSetting(context, Settings.TOKEN));
                urlConnection.setRequestProperty("Cookie", SelectQueries.getSetting(context, Settings.SESS_NAME_ID));

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("fcm_token", SelectQueries.getSetting(getApplicationContext(),Settings.FCM_TOKEN));
                jsonParam.put("device_id", SelectQueries.getSetting(getApplicationContext(),Settings.DEVICE_ID));

                DataOutputStream printout = new DataOutputStream(urlConnection.getOutputStream ());
                printout.writeBytes(jsonParam.toString());
                printout.flush();
                printout.close();

                StringBuffer jsonString=new StringBuffer();
                int statusCode = urlConnection.getResponseCode();
                Logger.d(TAG, "statusCode =" + statusCode);
                     /* 200 represents HTTP OK */
                if (statusCode == 200) {
                    BufferedReader inputStream = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"utf-8"));
                    String line;
                    while ((line = inputStream.readLine()) != null) {
                        jsonString.append(line);
                    }
                    inputStream.close();
                    Logger.d(TAG, "response =" + jsonString);
                    json=new JSONObject(jsonString.toString());
                    String status="error";
                    status=json.optString("status");
                    if("success".equals(status)){


                    }else {
                        int code = json.optInt("status_code",0);
                        if(code != 0){
                            String msg = json.optString("msg","");
                            if(msg.equals("Session Expired. Please login again.")){
                                errMsg="403";
                            }else {
                                errMsg = json.optString("msg","Error sending token");
                            }
                        }
                        Logger.d(TAG,"error ="+errMsg);
                    }
                }else if(statusCode == 403){
                    errMsg = "403";
                    retVal = false;
                }else {
                    String resMsg;
                    resMsg = urlConnection.getResponseMessage();
                    Logger.d(TAG,"resMsg=" + resMsg);
                    if("".equals(resMsg)) {
                        errMsg = Commons.getWSErrors(statusCode);
                    }else {
                        errMsg = resMsg;
                    }
                    retVal = false;
                }
                urlConnection.disconnect();
            }catch (Exception e){
                Logger.e(TAG,e);
                retVal=false;
                errMsg = "Error sending token";
                e.printStackTrace();
            }

            return retVal;
        }


        @Override
        protected void onPostExecute(Boolean result) {
            Logger.d(TAG,"inside onPostExecute="+result);

        }
    }

}