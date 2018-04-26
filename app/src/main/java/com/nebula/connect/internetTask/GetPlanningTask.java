package com.nebula.connect.internetTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;

import com.nebula.connect.Commons;
import com.nebula.connect.Constants;
import com.nebula.connect.Settings;
import com.nebula.connect.entities.DealerEntity;
import com.nebula.connect.entities.PlanningEntity;
import com.nebula.connect.logreports.Logger;
import com.nebula.connect.queries.DeleteQueries;
import com.nebula.connect.queries.InsertQueries;
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

/**
 * Created by sagar on 29/3/17.
 */

public abstract class GetPlanningTask extends AsyncTask<Void, Void, Boolean> {
    private static final String TAG=GetPlanningTask.class.getSimpleName();
    private Context context;
    private JSONObject json;
    private String errMsg = "Error fetching Planning";

    public GetPlanningTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Logger.d(TAG,"inside doInBackground: GetPlanningTask");
        boolean retVal=false;

        try {
            URL url=new URL(Constants.MEETINGS_URL);
            Logger.d(TAG,"url="+url.toString());
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput (true);
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("X-CSRF-Token", SelectQueries.getSetting(context, Settings.TOKEN));
            urlConnection.setRequestProperty("Cookie", SelectQueries.getSetting(context, Settings.SESS_NAME_ID));

            int vid = SelectQueries.getPlanningVid(context);
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("vid", Base64.encodeToString((vid+"").getBytes("UTF-8"), Base64.DEFAULT));

            DataOutputStream printout = new DataOutputStream(urlConnection.getOutputStream ());
            printout.writeBytes(jsonParam.toString());
            printout.flush();
            printout.close();

            int statusCode = urlConnection.getResponseCode();
            Logger.d(TAG,"inside doInBackground: GetPlanningTask: statusCode="+statusCode);

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
                    errMsg = json.optString("msg","Error fetching Planning");
                }
            }else if (statusCode == 403){
                errMsg ="403";
                retVal=false;
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
            errMsg = "Error fetching Planning";
            e.printStackTrace();
        }
        Logger.d(TAG,"inside doInBackground: GetPlanningTask: errMsg="+errMsg+" : retval="+retVal);

        return retVal;
    }

    private boolean addToDb() {
        boolean retVal=false;
        try {
            JSONArray branchArray=json.getJSONArray("meetings");
            if(branchArray.length()==0){
                retVal=true;
            }
            for(int i=0;i<branchArray.length();i++){
                JSONObject routeJson=branchArray.getJSONObject(i);
                PlanningEntity entity = new PlanningEntity();
                entity.vId = routeJson.getString("vid");
                entity.planningId = routeJson.getInt("meeting_id");
                entity.division = routeJson.getString("division");
                entity.depo = routeJson.getString("depo");
                entity.depoCode = routeJson.getString("depo_code");
                entity.meetingType = routeJson.optString("meeting_type");
                entity.remarks = routeJson.optString("field1");
                entity.meetingCode = routeJson.optString("meeting_code");
                entity.date = routeJson.optString("date");
                entity.trainingCenterName = routeJson.optString("training_center_name");
                entity.startTime = routeJson.optString("start_time");
                entity.statedId = routeJson.optString("state_id", null);
                entity.villageId = routeJson.optString("village_id",null);
                entity.location = routeJson.optString("location",null);
                entity.estimateAttendance = routeJson.optString("estimate_attendance",null);
                entity.nerolacTsi = routeJson.optString("nerolac_tsi",null);
                entity.nerolacTsiContact = routeJson.getString("nerolac_tsi_contact");
                entity.budgetPerMeet = routeJson.getString("budget_per_meet");
                entity.asmName = routeJson.getString("asm_name");
                entity.asmContact = routeJson.getString("asm_contact");
                entity.dsmName = routeJson.getString("dsm_name");
                entity.dsmContact = routeJson.getString("dsm_contact");
                entity.planning_field4 = routeJson.getString("remark_1");
                entity.planning_field5 = routeJson.getString("remark_2");
                String status = routeJson.getString("meeting_status");

                if("2".equals(status) ||"0".equals(status)){
                    entity.status = Constants.OPEN;
                }else if("1".equals(status)){
                    entity.status = Constants.CLOSED;
                }
                int id = UpdateQueries.updatePlanningRecord(context,entity);
                if(id==0) {
                    InsertQueries.insertPlanning(context, entity);
                }
                retVal=true;
            }
        }catch (JSONException e){

            Logger.e(TAG,e);
            e.printStackTrace();
            Logger.d(TAG,"error= "+e);
        }

        try {
            JSONArray dealerArray=json.getJSONArray("dealers");
            if(dealerArray.length()==0){
                retVal=true;
            }
            for (int i=0;i<dealerArray.length();i++){
                JSONObject dealerJson=dealerArray.getJSONObject(i);
                DeleteQueries.deleteDealer(context,dealerJson.getInt("meeting_id"));
            }

            for(int i=0;i<dealerArray.length();i++){
                JSONObject dealerJson=dealerArray.getJSONObject(i);
                DealerEntity entity = new DealerEntity();
                entity.meetingId = dealerJson.getString("meeting_id");
                entity.dealerId = dealerJson.getInt("dealer_id");
                entity.name = dealerJson.getString("name");
                entity.code = dealerJson.getString("code");
                entity.contact1 = dealerJson.optString("contact1");
                entity.contact2 = dealerJson.optString("contact2");
                entity.villageId = dealerJson.optString("village_id");
                entity.stateId = dealerJson.optString("state_id");
                InsertQueries.insertDealer(context, entity);
                retVal=true;
            }
        }catch (JSONException e){

            Logger.e(TAG,e);
            e.printStackTrace();
            Logger.d(TAG,"error= "+e);
        }

        return retVal;
    }

    protected abstract void afterExecution(boolean result,String msg);

    @Override
    protected void onPostExecute(Boolean result) {
        Logger.d(TAG,"onPostExecute="+result);
        afterExecution(result, errMsg);
    }
}