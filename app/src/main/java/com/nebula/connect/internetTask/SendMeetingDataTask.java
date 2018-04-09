package com.nebula.connect.internetTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.KeyEvent;

import com.nebula.connect.Commons;
import com.nebula.connect.Constants;
import com.nebula.connect.entities.MeetingEntity;
import com.nebula.connect.entities.PainterEntity;
import com.nebula.connect.entities.StartDayEntity;
import com.nebula.connect.logreports.Logger;
import com.nebula.connect.Settings;
import com.nebula.connect.queries.SelectQueries;
import com.nebula.connect.queries.UpdateQueries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by sagar on 28/7/16.
 */
public abstract class SendMeetingDataTask extends AsyncTask<Void, Void, Boolean> {
    private static final String TAG=SendMeetingDataTask.class.getSimpleName();
    private Context context;
    private ProgressDialog mProgressDialog;
    JSONObject json;
    String ldr="";
    String errMsg = "Error sending Meeting Data";
    private int errCode,meetStatus,isShowDialog;
    int planningId;

    public SendMeetingDataTask(Context context, int planningId, int meetStatus, int isShowDialog,String ldr) {
        this.context = context;
        this.planningId = planningId;
        this.meetStatus = meetStatus;
        this.isShowDialog = isShowDialog;
        this.ldr = ldr;
    }

    @Override
    protected void onPreExecute() {
        Logger.d(TAG,"inside onPreExecute");
        if(1 == isShowDialog){
            showProgressDialog();
        }

    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Logger.d(TAG,"inside doInBackground");
        boolean retVal=false;

        try {
            URL url=new URL(Constants.APP_TRANSACTION_URL);
            Logger.d(TAG,"url="+url.toString());
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoInput (true);
            urlConnection.setDoOutput (true);
            urlConnection.setUseCaches (false);
            urlConnection.setRequestMethod( "POST" );
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("X-CSRF-Token", SelectQueries.getSetting(context, Settings.TOKEN));
            urlConnection.setRequestProperty("Cookie", SelectQueries.getSetting(context, Settings.SESS_NAME_ID));

            //Create JSONObject here
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("meeting_start", getMeetingStartJson());
            jsonParam.put("meeting_details", getMeetingDetailJson());
            jsonParam.put("painter_details", getPainterArray());
            jsonParam.put("painter_photo_count", Commons.getBase64Encoded(SelectQueries.getSaleMetaCountByTag(context,planningId,Constants.PHOTO_PAINTER)+""));
            jsonParam.put("meeting_photo_count", Commons.getBase64Encoded(SelectQueries.getSaleMetaCountByTag(context,planningId,Constants.PHOTO_MEETING)+""));
            jsonParam.put("status",Commons.getBase64Encoded(meetStatus+""));
           // jsonParam.put("metadata",Commons.getBase64Encoded(ldr+""));

            Logger.d(TAG,"jsonParams="+jsonParam.toString());
            DataOutputStream printout = new DataOutputStream(urlConnection.getOutputStream());
            printout.writeBytes(jsonParam.toString());
            printout.flush ();
            printout.close ();
            int statusCode = urlConnection.getResponseCode();

            StringBuffer jsonString=new StringBuffer();
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
                    retVal=addToDb();
                }else {
                    errMsg = json.optString("msg","Error sending Data");
                    Logger.d(TAG,"error= "+errMsg);
                }
            }else {
                errCode = statusCode;
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
            errMsg = "Error sending data";
            e.printStackTrace();
        }

        return retVal;
    }

    private boolean addToDb() {
        Logger.d(TAG,"inside addToDb");
        boolean retVal=false;
        int trans_id = json.optInt("transaction_id", 1);
        if(!"error".equals(trans_id)){
            try {
                UpdateQueries.updateStartDayStatus(context, planningId, Constants.CLOSED);
                UpdateQueries.updatePainterStatus(context, planningId, Constants.CLOSED);
                UpdateQueries.updateMeetingStatus(context, planningId, Constants.CLOSED);
                retVal = true;
            }catch (Exception e) {
                Logger.e(TAG,e);
                e.printStackTrace();
            }
        }
        return retVal;
    }

    protected abstract void afterExecution(boolean result,String msg,int errCode, int saleId);

    @Override
    protected void onPostExecute(Boolean result) {
        Logger.d(TAG,"inside onPostExecute="+result);
        if(1 == isShowDialog){
            if(mProgressDialog != null){
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }
        }

        afterExecution(result, errMsg, errCode,planningId);
    }

    private void showProgressDialog(){
        Logger.d(TAG,"inside showProgressDialog");
        String message;

        message = "Please Wait...";

        if(mProgressDialog == null){
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage(message);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    // Act as if all keys are processed
                    return true;
                }
            });
        }
        mProgressDialog.show();
    }

    private JSONObject getMeetingStartJson() {
        Logger.d(TAG,"inside getMeetingStartJson");
        StartDayEntity entity = SelectQueries.getStartDayElementsByStartId(context, planningId);
        Logger.d(TAG,"startEntity="+entity);
        JSONObject jObj = new JSONObject();
        try {
            jObj.put("meeting_id", Commons.getBase64Encoded(entity.startId+""));
            jObj.put("start_date", Commons.getBase64Encoded(entity.startDate+""));
            jObj.put("remarks", Commons.getBase64Encoded(entity.remarks+""));
            jObj.put("latitude", Commons.getBase64Encoded(entity.latitude+""));
            jObj.put("longitude", Commons.getBase64Encoded(entity.longitude+""));
            jObj.put("accuracy", Commons.getBase64Encoded(entity.accuracy+""));
            jObj.put("contact",Commons.getBase64Encoded(entity.mobile+""));
        }catch (JSONException e){
            Logger.e(TAG,e);
            e.printStackTrace();
        }
        return jObj;
    }

    private JSONObject getMeetingDetailJson() {
        Logger.d(TAG,"inside getMeetingJson");
        MeetingEntity entity = SelectQueries.getMeetingByPlanningId(context, planningId);
        Logger.d(TAG,"meetingEntity="+entity);
        JSONObject jObj = new JSONObject();
        try {
            jObj.put("meeting_id", Commons.getBase64Encoded(entity.planningId+""));
            jObj.put("hotel_name", Commons.getBase64Encoded(entity.hotelName+""));
            jObj.put("painter_attendance", Commons.getBase64Encoded(entity.painterAttendance+""));
            jObj.put("non_participants", Commons.getBase64Encoded(entity.nonParticipants+""));
            jObj.put("meeting_detail_start", Commons.getBase64Encoded(entity.meetiingDetailsStart+""));
            jObj.put("total_gifts_given", Commons.getBase64Encoded(entity.totalGiftsGiven+""));
            jObj.put("detail_start_time", Commons.getBase64Encoded(entity.detailStartTime+""));
            jObj.put("meeting_type", Commons.getBase64Encoded(entity.meeting_field1+""));
            jObj.put("meeting_remark", Commons.getBase64Encoded(entity.meeting_field2+""));
            jObj.put("remark_1", Commons.getBase64Encoded(entity.meeting_field4+""));
            jObj.put("remark_2", Commons.getBase64Encoded(entity.meeting_field5+""));
        }catch (JSONException e){
            Logger.e(TAG,e);
            e.printStackTrace();
        }
        return jObj;
    }

    private JSONArray getPainterArray() {
        Logger.d(TAG,"inside getPainterArray");
        ArrayList<PainterEntity> entities = SelectQueries.getPainterByPlanningId(context,planningId,1);
        JSONArray jArray = null;
        for (PainterEntity entity:entities){
            Logger.d(TAG,"painter="+entity);
            JSONObject jObj = new JSONObject();
            if(jArray == null) {
                jArray = new JSONArray();
            }
            try{
                jObj.put("painter_id", Commons.getBase64Encoded(entity.painterId+""));
                jObj.put("meeting_id", Commons.getBase64Encoded(entity.planningId+""));
                jObj.put("village_id", Commons.getBase64Encoded(entity.planningId+""));
                jObj.put("painter_name", Commons.getBase64Encoded(entity.painterName+""));
                jObj.put("npp_code", Commons.getBase64Encoded(entity.nppCode));
                jObj.put("dealer_name", Commons.getBase64Encoded(entity.dealerName));
                jObj.put("dealer_id", Commons.getBase64Encoded(entity.dealerId));
                jObj.put("contact", Commons.getBase64Encoded(entity.contact));
                jObj.put("detail_start_time", Commons.getBase64Encoded(entity.detailStartTime));
                jObj.put("business_started_year", Commons.getBase64Encoded(entity.business_started_year));
                jObj.put("qualification", Commons.getBase64Encoded(entity.qualification));
                jObj.put("team_size", Commons.getBase64Encoded(entity.team_size));
                jObj.put("dealer_code", Commons.getBase64Encoded(entity.dealer_code));
                jObj.put("dealer_contact", Commons.getBase64Encoded(entity.dealer_contact));
                jObj.put("order_booking", Commons.getBase64Encoded(entity.order_booking));
                jObj.put("remark_1", Commons.getBase64Encoded(entity.remark1));
                jObj.put("remark_2", Commons.getBase64Encoded(entity.remark2));


                jArray.put(jObj);
            }catch (JSONException e){
                Logger.e(TAG,e);
                e.printStackTrace();
            }
        }
        return jArray;
    }
}