package com.nebula.connect;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.bizonesoft.metadatainfo.LDRCaptureTask;
import com.nebula.connect.entities.MeetingEntity;
import com.nebula.connect.entities.PainterEntity;
import com.nebula.connect.entities.PlanningEntity;
import com.nebula.connect.entities.SaleMetadataEntity;
import com.nebula.connect.entities.StartDayEntity;
import com.nebula.connect.entities.UploadPendingEntity;
import com.nebula.connect.internetTask.SendMeetingDataTask;
import com.nebula.connect.internetTask.SendSalesMetadataTask;
import com.nebula.connect.logreports.Logger;
import com.nebula.connect.queries.InsertQueries;
import com.nebula.connect.queries.SelectQueries;
import com.nebula.connect.queries.UpdateQueries;
import com.nebula.connect.utilities.SelectAllCheckbox;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by siddhesh on 8/3/16.
 */
public class UploadPendingActivity extends AppCompatActivity {
    private static final String TAG=UploadPendingActivity.class.getSimpleName();

    private ListView list;
    private ArrayList<UploadPendingEntity> uploadPendingEntities;
    private ProgressDialog dialog;
    private ArrayList<MeetingEntity> meetingEntities;
    private ArrayList<PainterEntity> painterEntities;
    private ArrayList<StartDayEntity> startEntities;
    private ArrayList<SaleMetadataEntity> saleMetadataEntities;
    private ArrayList<PlanningEntity> planningEntities;
    private ArrayList<String> meetIdArray;
    private int count = 0, metaData = 0,checkCount = 0,currentCount = 0;
    private TextView noRecord;
    private Button upload;
    private CustomPendingList adapter;
    private LinearLayout checkLayout;
    private SelectAllCheckbox checkAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.d(TAG, "inside onCreate");
        setContentView(R.layout.upload_sales);

        noRecord = (TextView) findViewById(R.id.norecord);
        upload = (Button) findViewById(R.id.upload);
        checkLayout = (LinearLayout) findViewById(R.id.checkll);
        checkAll = (SelectAllCheckbox) findViewById(R.id.checkall);
        adapter = new CustomPendingList();
        meetIdArray = new ArrayList<>();
        uploadPendingEntities=new ArrayList<UploadPendingEntity>();
        meetingEntities = new ArrayList<MeetingEntity>();
        painterEntities = new ArrayList<PainterEntity>();
        saleMetadataEntities = new ArrayList<SaleMetadataEntity>();
        planningEntities = new ArrayList<PlanningEntity>();

        meetingEntities = SelectQueries.getMeetingByStatus(this);
        painterEntities = SelectQueries.getPainterByStatus(this);
        saleMetadataEntities = SelectQueries.getSaleMetadataByStatus(this);
        startEntities = SelectQueries.getStartDayElementsByStatus(this);
        planningEntities = SelectQueries.getPlanningByStatus(this);
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
            entity1.date = entity.date;
            entity1.location = entity.location;
            entity1.isChecked = true;
            uploadPendingEntities.add(entity1);
            checkCount++;
        }

        list=(ListView)findViewById(R.id.list_view_sales);
        Logger.d(TAG,"setting adapter");
        list.setAdapter(adapter);

        checkAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkAll.isSelfUncheckFlag()) {
                    checkAll.setSelfUncheckFlag(false);
                } else if (isChecked) {
                    for (int i = 0; i < uploadPendingEntities.size(); i++) {
                        uploadPendingEntities.get(i).isChecked = true;

                    }
                } else {
                    for (int i = 0; i < uploadPendingEntities.size(); i++) {
                        uploadPendingEntities.get(i).isChecked = false;
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });


        Logger.d(TAG,"setActionBar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        if(uploadPendingEntities.size() > 0) {
            noRecord.setVisibility(View.GONE);
            checkAll.setChecked(true);
            checkLayout.setVisibility(View.VISIBLE);
            upload.setVisibility(View.VISIBLE);
            list.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        closeDialog();
        super.onDestroy();
    }

    public void onUpload(View v){

        currentCount = 0;
        checkCount = 0;
        for (int i = 0; i < uploadPendingEntities.size(); i++) {
            if (uploadPendingEntities.get(i).isChecked) {
                checkCount++;
            }
        }

        if (checkCount == 0) {
            Toast.makeText(this, "Please select one or more record to upload", Toast.LENGTH_LONG).show();
            return;
        }

        if(!ContextCommons.isOnline(this)){
            Toast.makeText(this, R.string.enable_internet, Toast.LENGTH_LONG).show();
            return;
        }

        count = 0;
        metaData = 0;
        for(UploadPendingEntity uploadPendingEntity:uploadPendingEntities) {
            if(uploadPendingEntity.isChecked) {
                count++;
            }
        }

        if(count > 0){
            dialog = new ProgressDialog(this);
            dialog.setMessage("Please wait....");
            dialog.setCancelable(false);
            dialog.show();
        }else {
            return;
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

                        new SendMeetingDataTask(UploadPendingActivity.this,uploadPendingEntities.get(finalI).meetingId, finalComplete,0,ldr){

                            @Override
                            protected void afterExecution(boolean result, String msg,int errCode, int planningId) {

                                if(result){
                                    metaData++;
                                    if(metaData == count) {
                                        if (UploadPendingActivity.this.isFinishing()) {
                                            return;
                                        }
                                        closeDialog();
                                        Toast.makeText(UploadPendingActivity.this,R.string.data_uploaded,Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                    ArrayList<SaleMetadataEntity> entities = SelectQueries.getSaleMetadataElements(UploadPendingActivity.this,planningId,0);
                                    if(entities.size() == 0){
                                        PlanningEntity entity1 =  SelectQueries.getPlanningById(UploadPendingActivity.this,planningId);
                                        if(Constants.INPROGRESS.equals(entity1.status)){
                                            UpdateQueries.updatePlanningStatus(UploadPendingActivity.this, planningId, Constants.CLOSED);
                                        }
                                    }else {
                                        sendMetaImages(planningId);
                                    }
                                }else if(errCode == 403){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(UploadPendingActivity.this);
                                    builder.setMessage("Session expired. Please login again.")
                                            .setCancelable(false)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    Intent intent = new Intent(UploadPendingActivity.this, LoginActivity.class);
                                                    InsertQueries.setSetting(UploadPendingActivity.this, Settings.PASSWORD, "");
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }else {
                                    Toast.makeText(UploadPendingActivity.this, msg, Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            }
                        }.execute();

                    }
                }.execute();



            }
        }
    }

    private void sendMetaImages(final int meetingId) {

        new LDRCaptureTask(getBaseContext()) {
            @Override
            public void afterExecution(String ldr) {


                new SendSalesMetadataTask(UploadPendingActivity.this,meetingId,ldr){

                    @Override
                    protected void afterExecution(int planId) {
                        PlanningEntity entity = SelectQueries.getPlanningById(UploadPendingActivity.this,planId);
                        if(Constants.INPROGRESS.equals(entity.status)){
                            UpdateQueries.updatePlanningStatus(UploadPendingActivity.this, meetingId, Constants.CLOSED);

                        }

                    }

                    @Override
                    protected void notUploaded(String code, String msg) {
                        if ("403".equals(code)) {
                            // expire
                            AlertDialog.Builder builder = new AlertDialog.Builder(UploadPendingActivity.this);
                            builder.setMessage("Session expired. Please login again.")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent intent = new Intent(UploadPendingActivity.this, LoginActivity.class);
                                            InsertQueries.setSetting(UploadPendingActivity.this, Settings.PASSWORD, "");
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

            }
        }.execute();

            }





    private void goToMainActivity() {

        Intent intent = new Intent(UploadPendingActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }

    private void closeDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }


    public class CustomPendingList extends BaseAdapter {

        @Override
        public int getCount() {
            return uploadPendingEntities.size();
        }

        @Override
        public Object getItem(int position) {
            return uploadPendingEntities.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder{
            TextView meet_id,meet_name,date,location;
            CheckBox ischeck;

        }
        @Override
        public View getView(final int pos, View convertView, ViewGroup arg2) {
            Logger.d(TAG,"inside getView");


            ViewHolder holder = null;

            if(convertView == null) {
                convertView =getLayoutInflater().inflate(R.layout.upload_sales_lits_item, null);

                holder=new ViewHolder();
                holder.meet_id = (TextView) convertView.findViewById(R.id.meet_id);
                holder.meet_name = (TextView) convertView.findViewById(R.id.meeting_name);
                holder.date = (TextView) convertView.findViewById(R.id.date);
                holder.ischeck = (CheckBox) convertView.findViewById(R.id.check);
                holder.location = (TextView) convertView.findViewById(R.id.meeting_loc);
                holder.ischeck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            uploadPendingEntities.get(pos).isChecked = true;
                            checkCount++;
                        } else {
                            uploadPendingEntities.get(pos).isChecked = false;
                            checkCount--;
                            if (checkAll.isChecked()) {
                                checkAll.setSelfUncheckFlag(true);
                                checkAll.setChecked(false);
                            }
                        }
                    }
                });
                convertView.setTag(holder);

            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            UploadPendingEntity entity=uploadPendingEntities.get(pos);
            Logger.d(TAG, "UploadPendingEntity="+entity.toString());

            holder.meet_id.setText(entity.meetingCode+"");
            holder.meet_name.setText(entity.meetingName+"");
            holder.date.setText((Commons.milliToDateWithYear(Long.parseLong(entity.date + ""))));
            holder.location.setText(entity.location);
            holder.ischeck.setChecked(entity.isChecked);

            return convertView;
        }


    }


}
