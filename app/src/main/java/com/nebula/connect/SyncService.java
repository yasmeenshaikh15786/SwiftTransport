package com.nebula.connect;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.bizonesoft.metadatainfo.LDRCaptureTask;
import com.nebula.connect.entities.MeetingEntity;
import com.nebula.connect.entities.PainterEntity;
import com.nebula.connect.entities.PlanningEntity;
import com.nebula.connect.entities.SaleMetadataEntity;
import com.nebula.connect.entities.StartDayEntity;
import com.nebula.connect.entities.UploadPendingEntity;
import com.nebula.connect.internetTask.GetRoutePlanTask;
import com.nebula.connect.internetTask.SendMeetingDataTask;
import com.nebula.connect.internetTask.SendSalesMetadataTask;
import com.nebula.connect.logreports.Logger;
import com.nebula.connect.queries.SelectQueries;
import com.nebula.connect.queries.UpdateQueries;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by sagar on 26/7/16.
 */
public class SyncService extends Service {

    private static final String TAG = SyncService.class.getSimpleName();
    private int metaData = 0,count = 0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Logger.removeLines(TAG,"removing lines from async");
            }
        });

        Logger.d(TAG, "inside start command");

        if(ContextCommons.isOnline(this)) {
            startRoutePlan();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void startRoutePlan() {
        Logger.d(TAG, "inside startRoutePlan");
        int vid = SelectQueries.getRoutePlanVid(this);
        new GetRoutePlanTask(this,vid){
            @Override
            protected void afterExecution(boolean result, String msg) {
                if(result) {
                    startIncompleteUpload();
                }else {
                    stopSelf();
                }
            }
        }.execute();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void startIncompleteUpload() {

        Logger.d(TAG, "inside startIncompleteUpload");
        final ArrayList<UploadPendingEntity> uploadPendingEntities=new ArrayList<UploadPendingEntity>();
        ArrayList<MeetingEntity> meetingEntities = SelectQueries.getMeetingByStatus(this);
        ArrayList<PainterEntity> painterEntities = SelectQueries.getPainterByStatus(this);
        ArrayList<SaleMetadataEntity> saleMetadataEntities = SelectQueries.getSaleMetadataByStatus(this);
        ArrayList<StartDayEntity> startEntities = SelectQueries.getStartDayElementsByStatus(this);
        ArrayList<PlanningEntity> planningEntities = SelectQueries.getPlanningByStatus(this);
        ArrayList<String> meetIdArray = new ArrayList<>();

        for (int i=0;i<startEntities.size();i++){
            meetIdArray.add(startEntities.get(i).startId+"");
        }
        for (int i=0;i<meetingEntities.size();i++){
            meetIdArray.add(meetingEntities.get(i).planningId+"");
        }
        for (int i=0;i<painterEntities.size();i++){
            meetIdArray.add(painterEntities.get(i).planningId+"");
        }
        for (int i=0;i<saleMetadataEntities.size();i++){
            meetIdArray.add(saleMetadataEntities.get(i).meeting_id+"");
        }

        for (int i=0;i<planningEntities.size();i++){
            meetIdArray.add(planningEntities.get(i).planningId+"");
        }
        Set<String> hs = new HashSet<>();
        hs.addAll(meetIdArray);
        meetIdArray.clear();
        meetIdArray.addAll(hs);

        for(int i = 0;i<meetIdArray.size();i++){
            PlanningEntity entity =  SelectQueries.getPlanningById(this,Integer.parseInt(meetIdArray.get(i)+""));
            UploadPendingEntity entity1 = new UploadPendingEntity();
            entity1.meetingCode = entity.meetingCode;
            entity1.meetingId = entity.planningId;
            entity1.meetingName = entity.meetingType;
            entity1.location = entity.location;
            entity1.date = entity.date;
            entity1.isChecked = true;
            uploadPendingEntities.add(entity1);
        }

        for(int i=0; i<uploadPendingEntities.size(); i++) {
            if(uploadPendingEntities.get(i).isChecked) {
                int complete = 0;
                PlanningEntity entity = SelectQueries.getPlanningById(this,uploadPendingEntities.get(i).meetingId);
                if(Constants.INPROGRESS.equals(entity.status)){
                    complete = 1;
                }
                final int finalI = i;
                final int finalComplete = complete;
                new LDRCaptureTask(getBaseContext()) {
                    @Override
                    public void afterExecution(String ldr) {

                        new SendMeetingDataTask(SyncService.this,uploadPendingEntities.get(finalI).meetingId, finalComplete,0,ldr){

                            @Override
                            protected void afterExecution(boolean result, String msg,int errCode, int planningId) {

                                if(result){
                                    ArrayList<SaleMetadataEntity> entities = SelectQueries.getSaleMetadataElements(SyncService.this,planningId,0);
                                    if(entities.size() == 0){
                                        PlanningEntity entity1 =  SelectQueries.getPlanningById(SyncService.this,planningId);
                                        if(Constants.INPROGRESS.equals(entity1.status)){
                                            UpdateQueries.updatePlanningStatus(SyncService.this, planningId, Constants.CLOSED);
                                        }
                                    }else {
                                        sendMetaImages(planningId);
                                    }
                                }else {
                                    stopSelf();
                                }
                            }
                        }.execute();
                    }
                }.execute();


            }
        }
        Logger.d(TAG, "exiting startIncompleteUpload");
    }

    private void sendMetaImages(final int meetingId) {
        Logger.d(TAG, "inside sendMetaImages");
        new LDRCaptureTask(getBaseContext()) {
            @Override
            public void afterExecution(String ldr) {

                new SendSalesMetadataTask(SyncService.this,meetingId,ldr){

                    @Override
                    protected void afterExecution(int planId) {
                        PlanningEntity entity = SelectQueries.getPlanningById(SyncService.this,planId);
                        if(Constants.INPROGRESS.equals(entity.status)){
                            UpdateQueries.updatePlanningStatus(SyncService.this, meetingId, Constants.CLOSED);

                        }
                    }

                    @Override
                    protected void notUploaded(String code, String msg) {
                        stopSelf();
                    }
                }.execute();

                Logger.d(TAG, "exiting sendMetaImages");
            }
        }.execute();



    }

}
