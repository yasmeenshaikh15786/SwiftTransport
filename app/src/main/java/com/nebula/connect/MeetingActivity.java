package com.nebula.connect;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bizonesoft.metadatainfo.LDRCaptureTask;
import com.nebula.connect.entities.MeetingEntity;
import com.nebula.connect.entities.PainterEntity;
import com.nebula.connect.entities.PlanningEntity;
import com.nebula.connect.entities.SaleMetadataEntity;
import com.nebula.connect.entities.StartDayEntity;
import com.nebula.connect.internetTask.SendMeetingDataTask;
import com.nebula.connect.internetTask.SendSalesMetadataTask;
import com.nebula.connect.logreports.Logger;
import com.nebula.connect.queries.InsertQueries;
import com.nebula.connect.queries.SelectQueries;
import com.nebula.connect.queries.UpdateQueries;

import java.util.ArrayList;

/**
 * Created by Sonam on 27/3/17.
 */
public class MeetingActivity extends AppCompatActivity {
    private static final String TAG = MeetingActivity.class.getSimpleName();
    private int planningId;
    private PlanningEntity entity;
    private ImageView meetingStart, meetingDetail, meetingPhoto, painterDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);
        Logger.d(TAG, "inside onCreate");
        planningId = getIntent().getIntExtra("plan_id",0);
        entity = SelectQueries.getPlanningById(this, planningId);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        meetingStart = (ImageView) findViewById(R.id.ms_img);
        meetingDetail = (ImageView) findViewById(R.id.md_img);
        meetingPhoto = (ImageView) findViewById(R.id.mp_img);
        painterDetail = (ImageView) findViewById(R.id.pd_img);
        ((TextView)findViewById(R.id.meeting_id)).setText(entity.meetingCode+"");
        ((TextView)findViewById(R.id.estimated_attend)).setText(entity.estimateAttendance+"");
        ((TextView)findViewById(R.id.meet_type)).setText(entity.meetingType);
        ((TextView)findViewById(R.id.location)).setText(entity.location);
        Logger.d(TAG, "exiting onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.d(TAG, "inside onResume");
        StartDayEntity startDayEntity = SelectQueries.getStartDayElementsByStartId(this,planningId);

        if(startDayEntity.startId > 0 && Constants.CLOSED.equals(startDayEntity.status)){
            meetingStart.setVisibility(View.VISIBLE);
            meetingStart.setImageDrawable(getResources().getDrawable(R.mipmap.verified));
        }else if(startDayEntity.startId > 0 && Constants.INPROGRESS.equals(startDayEntity.status)){
            meetingStart.setVisibility(View.VISIBLE);
            meetingStart.setImageDrawable(getResources().getDrawable(R.mipmap.pending_white));

        }

        MeetingEntity meetingEntity = SelectQueries.getMeetingByPlanningId(this,planningId);

        if(meetingEntity.planningId > 0 && Constants.CLOSED.equals(meetingEntity.status)){
            meetingDetail.setVisibility(View.VISIBLE);
            meetingDetail.setImageDrawable(getResources().getDrawable(R.mipmap.verified));
        }else if(meetingEntity.planningId > 0 && Constants.INPROGRESS.equals(meetingEntity.status)) {
            meetingDetail.setVisibility(View.VISIBLE);
            meetingDetail.setImageDrawable(getResources().getDrawable(R.mipmap.pending_white));
        }

            ArrayList<SaleMetadataEntity> saleMetadataEntities = SelectQueries.getSaleMetadataElements(this,planningId,1);

        boolean isPhotoClose = true;
        for (SaleMetadataEntity entity:saleMetadataEntities){
            if(Constants.INPROGRESS.equals(entity.status)){
                meetingPhoto.setVisibility(View.VISIBLE);
                meetingPhoto.setImageDrawable(getResources().getDrawable(R.mipmap.pending_white));

                isPhotoClose = false;
                break;
            }
        }

        if(isPhotoClose && saleMetadataEntities.size()>0){
            meetingPhoto.setVisibility(View.VISIBLE);
            meetingPhoto.setImageDrawable(getResources().getDrawable(R.mipmap.verified));
        }

        ArrayList<PainterEntity> painterEntities = SelectQueries.getPainterByPlanningId(this,planningId,0);

        isPhotoClose = true;
        for (PainterEntity entity:painterEntities){
            if(Constants.INPROGRESS.equals(entity.status)){
                painterDetail.setVisibility(View.VISIBLE);
                painterDetail.setImageDrawable(getResources().getDrawable(R.mipmap.pending_white));

                isPhotoClose = false;
                break;
            }
        }

        if(isPhotoClose && painterEntities.size()>0){
            painterDetail.setVisibility(View.VISIBLE);
            painterDetail.setImageDrawable(getResources().getDrawable(R.mipmap.verified));
        }
        Logger.d(TAG, "exiting onResume");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.upload_complete_menu, menu);
        return true;
    }

    private void sendMetaImages(final int meetingId) {
        Logger.d(TAG, "inside sendMetaImages");

        new LDRCaptureTask(getBaseContext()) {
            @Override
            public void afterExecution(String ldr) {

                new SendSalesMetadataTask(MeetingActivity.this,meetingId,ldr){


                    @Override
                    protected void afterExecution(int planId) {
                        UpdateQueries.updatePlanningStatus(MeetingActivity.this, planningId, Constants.CLOSED);
                    }

                    @Override
                    protected void notUploaded(String code, String msg) {
                        if ("403".equals(code)) {
                            // expire
                            AlertDialog.Builder builder = new AlertDialog.Builder(MeetingActivity.this);
                            builder.setMessage("Session expired. Please login again.")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent intent = new Intent(MeetingActivity.this, LoginActivity.class);
                                            InsertQueries.setSetting(MeetingActivity.this, Settings.PASSWORD, "");
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    }
                }.execute();
                Logger.d(TAG, "exiting sendMetaImages");
            }
        }.execute();





    }

    private boolean validateEntries(){
        Logger.d(TAG, "inside validateEntries");
        boolean retVal = true;
        String msg = "";
        String title = "";
        Intent i = null;

        StartDayEntity entity = SelectQueries.getStartDayElementsByStartId(this, planningId);
        if (entity.startId == 0) {
            retVal = false;
            title = getString(R.string.meet_not_start);
            msg = getString(R.string.meet_not_start_body);
            i = new Intent(this,StartDayActivity.class);
        }

        ArrayList<SaleMetadataEntity> meetingEntities = SelectQueries.getSaleMetadataElementsByTag(this,planningId,Constants.PHOTO_MEETING);
        if(meetingEntities.size() < Integer.parseInt(SelectQueries.getSetting(this,Settings.MIN_MEETING_COUNT))  && retVal){
            retVal = false;
            title =getString( R.string.meet_photo_req);
            msg = getString(R.string.meet_photo_req_body)+" "+SelectQueries.getSetting(this,Settings.MIN_MEETING_COUNT)+" "+getString(R.string.meet_photo_req_body_two);
            i = new Intent(MeetingActivity.this, TabPhotoActivity.class);
        }

        ArrayList<SaleMetadataEntity> painterEntities = SelectQueries.getSaleMetadataElementsByTag(this,planningId,Constants.PHOTO_PAINTER);
        if(painterEntities.size() < Integer.parseInt(SelectQueries.getSetting(this,Settings.MIN_PAINTER_COUNT)) && retVal){
            retVal = false;
            title = getString(R.string.db_photo_req);
            msg = getString(R.string.db_photo_req_body)+" "+SelectQueries.getSetting(this,Settings.MIN_PAINTER_COUNT)+" "+getString(R.string.db_photo_req_body_two);
            i = new Intent(MeetingActivity.this, TabPhotoActivity.class);
        }

        ArrayList<SaleMetadataEntity> reportEntities= SelectQueries.getSaleMetadataElementsByTag(this,planningId,Constants.PHOTO_REPORT);
        if(reportEntities.size() < 2 && retVal){
            retVal = false;
            title = getString(R.string.card_photo_req);
            msg = getString(R.string.card_photo_req_body);
            i = new Intent(MeetingActivity.this, TabPhotoActivity.class);
        }

        MeetingEntity meetEntity = SelectQueries.getMeetingByPlanningId(this,planningId);
        if(meetEntity.planningId == 0 && retVal){
            retVal = false;
            title = getString(R.string.meet_detail_req);
            msg = getString(R.string.meet_detail_req_body);
            i = new Intent(MeetingActivity.this, MeetingDetailsActivity.class);

            i.putExtra("meet_code",this.entity.meetingCode+"");
            i.putExtra("meet_id",this.entity.planningId+"");
            i.putExtra("estimate_attend",this.entity.estimateAttendance+"");
            i.putExtra("meet_type",this.entity.meetingType+"");
        }

        if(!retVal){
            i.putExtra("plan_id",planningId);
            showCustomAlert(title,msg,i);
         //   showAlertDialog(msg,i,"Meeting Closure");
        }
        Logger.d(TAG, "exiting validateEntries retval = "+retVal);

        return retVal;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();

        } else if (menuItem.getItemId() == R.id.upload) {


            if(validateEntries()) {
                ArrayList<PainterEntity> painterEntities = SelectQueries.getPainterByPlanningId(MeetingActivity.this,planningId,0);
                MeetingEntity meetingEntity = SelectQueries.getMeetingByPlanningId(this,planningId);
                if(painterEntities.size() < Integer.parseInt(meetingEntity.painterAttendance)){

                    showCustomAlertDialog(getString(R.string.check_painter),getString(R.string.check_painter_body));
                }else {
                    showCustomAlertDialog(getString(R.string.meet_closure),getString(R.string.meet_closure_body));

                }
            }
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void sendMeeting(){

        UpdateQueries.updatePlanningStatus(MeetingActivity.this, planningId, Constants.INPROGRESS);
        if (!ContextCommons.isOnline(this)) {
            Toast.makeText(this, R.string.enable_internet, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        new LDRCaptureTask(getBaseContext()) {
            @Override
            public void afterExecution(String ldr) {

                new SendMeetingDataTask(MeetingActivity.this,planningId,1,1,ldr){

                    @Override
                    protected void afterExecution(boolean result, String msg,int errCode, int planningId) {

                        if(result){
                            ArrayList<SaleMetadataEntity> entities = SelectQueries.getSaleMetadataElements(MeetingActivity.this,planningId,0);
                            if(entities.size() == 0){
                                PlanningEntity entity1 =  SelectQueries.getPlanningById(MeetingActivity.this,planningId);
                                if(Constants.INPROGRESS.equals(entity1.status)){
                                    UpdateQueries.updatePlanningStatus(MeetingActivity.this, planningId, Constants.CLOSED);
                                }
                            }else {
                                sendMetaImages(planningId);
                            }
                            finish();
                        }else if(errCode == 403){
                            AlertDialog.Builder builder = new AlertDialog.Builder(MeetingActivity.this);
                            builder.setMessage("Session expired. Please login again.")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent intent = new Intent(MeetingActivity.this, LoginActivity.class);
                                            InsertQueries.setSetting(MeetingActivity.this, Settings.PASSWORD, "");
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }else {
                            Toast.makeText(MeetingActivity.this, msg, Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                }.execute();
            }
        }.execute();

    }

    public void onClick(View v) {
        Intent i = null;
        StartDayEntity startDayEntity = SelectQueries.getStartDayElementsByStartId(this, planningId);

        if (v.getId() == R.id.meet_start_txt) {
            if (ActivityCompat.checkSelfPermission(MeetingActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MeetingActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                Toast.makeText(MeetingActivity.this,R.string.loca_permission,Toast.LENGTH_LONG).show();
            }
           i=new Intent(this,StartDayActivity.class);
        } else if(startDayEntity.startId == 0) {
            i = new Intent(this,StartDayActivity.class);
            i.putExtra("plan_id",planningId);
            showCustomAlert(getString(R.string.meet_not_start),getString(R.string.meet_not_start_body),i);
       //     showAlertDialog(R.string.start_day_save, i,"Meeting Start");
            return;
        } else if (v.getId() == R.id.meet_detail_txt) {
            i = new Intent(MeetingActivity.this, MeetingDetailsActivity.class);
            i.putExtra("meet_id",entity.planningId+"");
            i.putExtra("estimate_attend",entity.estimateAttendance+"");
            i.putExtra("meet_type",entity.meetingType+"");
            i.putExtra("meet_code",entity.meetingCode+"");
            i.putExtra("remark1",entity.planning_field4+"");
            i.putExtra("remark2",entity.planning_field5+"");

        } else if (v.getId() == R.id.meet_photo_txt) {
            i = new Intent(MeetingActivity.this, TabPhotoActivity.class);
        }else if (v.getId() == R.id.painter_data_txt) {
            i = new Intent(MeetingActivity.this, PainterDataListActivity.class);
        }

        i.putExtra("plan_id",planningId);
        startActivity(i);
    }


    public void showCustomAlert(String title,String msg,final Intent intent){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_complete_meeting_dialog);

        Button no = (Button)dialog.findViewById(R.id.no);
        Button yes = (Button)dialog.findViewById(R.id.yes);
        TextView titletxt = (TextView)dialog.findViewById(R.id.title);
        TextView msgtxt = (TextView)dialog.findViewById(R.id.textMsg);
        titletxt.setText(title);
        msgtxt.setText(msg);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
                dialog.dismiss();
                dialog.cancel();
            }
        });

        dialog.show();

    }

    public void showCustomAlertDialog(String title,String msg){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_complete_meeting_dialog);

        Button no = (Button)dialog.findViewById(R.id.no);
        Button yes = (Button)dialog.findViewById(R.id.yes);
        TextView titletxt = (TextView)dialog.findViewById(R.id.title);
        TextView msgtxt = (TextView)dialog.findViewById(R.id.textMsg);
        titletxt.setText(title);
        msgtxt.setText(msg);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMeeting();
                dialog.dismiss();
                dialog.cancel();
            }
        });

        dialog.show();

    }


}
