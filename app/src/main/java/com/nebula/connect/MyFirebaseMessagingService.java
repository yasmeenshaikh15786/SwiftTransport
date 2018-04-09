package com.nebula.connect;

import android.content.Intent;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.nebula.connect.logreports.Logger;
import com.nebula.connect.queries.UpdateQueries;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

/**
 * Created by Sonam on 11/1/17.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCMCallbackService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
       String from = remoteMessage.getFrom();
        JSONObject json = new JSONObject(remoteMessage.getData());
        Logger.d(TAG,"json = "+json.toString()+" from = "+from);
        String message = null;
        try{
            message = json.getString("message");
        } catch (JSONException e){
            e.printStackTrace();
        }

        if(Constants.TECH_REQ.equals(message)){
            startService(new Intent(this, SendLogService.class));
        } else if(Constants.SYNC.equals(message)){
            startService(new Intent(this,SyncService.class));
        } else if(message.startsWith(Constants.PHOTOS)){

            String[] arrr = message.split(Pattern.quote(Constants.SALEMETA_SEPARATOR));
            if(arrr[1].equals("1")){
                UpdateQueries.updateAllMetadataStatus(this,Constants.INPROGRESS);
            }else if(arrr[1].equals("2")){
                for (int i=2;i<arrr.length;i++){
                    UpdateQueries.updateMetadataStatus(this,Integer.parseInt(arrr[i]),Constants.INPROGRESS);
                }
            }

        }
    }

}

