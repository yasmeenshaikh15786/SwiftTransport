package com.nebula.connect.internetTask;

import android.content.Context;
import android.os.AsyncTask;

import com.nebula.connect.Commons;
import com.nebula.connect.Constants;
import com.nebula.connect.Settings;
import com.nebula.connect.entities.SaleMetadataEntity;
import com.nebula.connect.logreports.Logger;
import com.nebula.connect.queries.InsertQueries;
import com.nebula.connect.queries.SelectQueries;
import com.nebula.connect.queries.UpdateQueries;
import com.nebula.connect.utilities.MultipartUtility;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by sagar on 29/7/16.
 */
public abstract class SendSalesMetadataTask extends AsyncTask<Void, Void, Boolean> {

    private Context context;
    private int planId;
    private String response,ldr="";
    private String errCode = "",errMsg = "Error sending photos";
    private static final String TAG=SendSalesMetadataTask.class.getSimpleName();

    public SendSalesMetadataTask(Context context, int planId,String ldr) {
        this.context = context;
        this.planId = planId;
        this.ldr = ldr;


    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        boolean retVal=false;

        long id = System.currentTimeMillis()/ 1000L;
        InsertQueries.setSetting(context,Settings.PHOTOS_ID,id+"");

        ArrayList<SaleMetadataEntity> entities= SelectQueries.getSaleMetadataElements(context,planId,0);
        String charset = "UTF-8";
        String requestURL = Constants.SALES_METADATA_URL;
        String deviceId = SelectQueries.getSetting(context, Settings.DEVICE_ID);
        Logger.d(TAG, "deviceid="+deviceId + " requesturl=" + requestURL+" planid="+planId);

        for(SaleMetadataEntity entity:entities) {

            Logger.d(TAG,"Sale meta data="+entity.toString());
                if(entity != null && !(Constants.CLOSED).equals(entity.status)) {
                    try {
                        if(!(id+"").equals(SelectQueries.getSetting(context,Settings.PHOTOS_ID))){
                            return true;
                        }
                        File uploadFile = new File(entity.filePath);
                        MultipartUtility multipart = new MultipartUtility(context, requestURL, charset);
                        if(uploadFile.exists()){
                            multipart.addFilePart("image", uploadFile);
                        } else {
                            multipart.addFormField("image",Commons.getBase64Encoded("File Not Found"));
                        }
                        multipart.addFormField("image_capture_time", Commons.getBase64Encoded(entity.created + ""));
                        multipart.addFormField("meeting_id", Commons.getBase64Encoded(entity.meeting_id+""));
                        multipart.addFormField("latitude", Commons.getBase64Encoded(entity.latitude + ""));
                        multipart.addFormField("longitude", Commons.getBase64Encoded(entity.longitude+""));
                        multipart.addFormField("accuracy", Commons.getBase64Encoded(entity.accuracy + ""));
                        multipart.addFormField("device_id", Commons.getBase64Encoded(deviceId));
                        multipart.addFormField("tag", Commons.getBase64Encoded(entity.tag+""));
                        multipart.addFormField("metadata", Commons.getBase64Encoded(ldr+""));

                        response = multipart.finish();
                        Logger.d(TAG,"response="+response.toString());

                        if (response.contains("Error from server")) {
                            retVal = false;
                            break;
                        } else {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            String status = jsonObject.getString("status");
                            if ("success".equals(status)) {
                                //update
                                retVal = true;
                                UpdateQueries.updateMetadataStatus(context, entity.m_id, Constants.CLOSED);
                            } else {
                                int code = jsonObject.optInt("code",0);
                                if(code != 0){
                                    errMsg = Commons.getWSErrors(code);
                                    errCode = code+"";
                                }
                                retVal = false;
                                break;
                            }
                        }

                    } catch (IOException ex) {
                        Logger.e(TAG,ex);
                        ex.printStackTrace();
                        retVal = false;
                        break;
                    } catch (JSONException e) {
                        Logger.e(TAG,e);
                        e.printStackTrace();
                        retVal = false;
                        break;
                    }
                }else{
                    retVal = true;
                }
        }

        return retVal;
    }

    protected abstract void afterExecution(int planId);
    protected abstract void notUploaded(String code, String msg);

    @Override
    protected void onPostExecute(Boolean result) {
        Logger.d(TAG,"onpostResult="+response);
        if(result){
            afterExecution(planId);
        }else {
            Logger.d(TAG,"errCode="+errCode+" errMsg="+errMsg);
            notUploaded(errCode,errMsg);
        }
    }

}
