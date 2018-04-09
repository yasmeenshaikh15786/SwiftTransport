package com.nebula.connect;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bizonesoft.metadatainfo.LDRCaptureTask;
import com.nebula.connect.entities.MeetingEntity;
import com.nebula.connect.entities.PlanningEntity;
import com.nebula.connect.internetTask.SendMeetingDataTask;
import com.nebula.connect.queries.InsertQueries;
import com.nebula.connect.queries.SelectQueries;
import com.nebula.connect.queries.UpdateQueries;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by Sonam on 29/3/17.
 */
public class MeetingDetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private EditText painterAttend, totalGifts, nonParticipants, newMeetingType;
    private Spinner typeSpin;
    private ArrayList<MeetingEntity> meetingEntityArrayList;
    private String planId;
    private LinearLayout otherLayout;
    String[] meetItems;
    private  String painAttend,totGifts,nonParticipant;
    private EditText remark1,remark2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        getSupportActionBar().setTitle("Meeting Details");
        ((TextView)findViewById(R.id.meeting_id)).setText(getIntent().getStringExtra("meet_code"));
        ((TextView)findViewById(R.id.estimated_attend)).setText(getIntent().getStringExtra("estimate_attend"));
        ((TextView)findViewById(R.id.meet_type)).setText(getIntent().getStringExtra("meet_type"));
        planId = getIntent().getStringExtra("meet_id");
        painterAttend = (EditText) findViewById(R.id.painter_attend);
        totalGifts = (EditText) findViewById(R.id.total_gifts);
        nonParticipants = (EditText) findViewById(R.id.non_participants);
        newMeetingType = (EditText) findViewById(R.id.new_meeting_type);
        remark1 = (EditText) findViewById(R.id.remark1);
        remark2 = (EditText) findViewById(R.id.remark2);
        otherLayout = (LinearLayout) findViewById(R.id.new_type);
        typeSpin= (Spinner) findViewById(R.id.type);
        typeSpin.setEnabled(false);
        typeSpin.setFocusable(false);
        initializeSpinner(typeSpin);

    }


    public void initializeSpinner(Spinner spinner){
     //   Logger.d(TAG,"inside initializeSpinner");
        String meetTy = SelectQueries.getSetting(this,Settings.MEETING_TYPES);
        meetTy = meetTy+Constants.MSG_SEPARATER+"Others";
        meetItems = meetTy.split(Pattern.quote(Constants.MSG_SEPARATER));
        final int lastPosition = meetItems.length - 1;
        typeSpin.setOnItemSelectedListener(MeetingDetailsActivity.this);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter <String>(this, android.R.layout.simple_spinner_item, meetItems);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        MeetingEntity entity = SelectQueries.getMeetingByPlanningId(this,Integer.parseInt(planId));

        typeSpin.setAdapter(dataAdapter);
        if(entity.planningId > 0){
            painterAttend.setText(entity.painterAttendance);
            totalGifts.setText(entity.totalGiftsGiven);
            nonParticipants.setText(entity.nonParticipants);

            selectSpinnerValue(typeSpin,entity.meeting_field1, lastPosition);
            newMeetingType.setText(entity.meeting_field2);

        }else {
            painterAttend.setText("");
            totalGifts.setText("");
            nonParticipants.setText("");
            String meetType = getIntent().getStringExtra("meet_type");
            newMeetingType.setText(entity.meeting_field2);
            selectSpinnerValue(typeSpin,meetType, lastPosition);

            PlanningEntity planningEntity = SelectQueries.getPlanningById(MeetingDetailsActivity.this,Integer.parseInt(planId));
            if ("Others".equals(spinner.getSelectedItem().toString())) {
                newMeetingType.setText(planningEntity.remarks);
            }

        }

        typeSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == lastPosition){
                    otherLayout.setVisibility(View.VISIBLE);
                }else {
                    otherLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
    private void selectSpinnerValue(Spinner spinner, String myString, int lastPosition)
    {
        boolean hasGot = false;
        for(int i = 0; i < spinner.getCount(); i++){
            if(spinner.getItemAtPosition(i).toString().equals(myString)){
                spinner.setSelection(i);
                hasGot = true;
                break;
            }
        }

        if(!hasGot){
            spinner.setSelection(lastPosition);
            otherLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.upload_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            painAttend = painterAttend.getText().toString();
            totGifts = totalGifts.getText().toString();
            nonParticipant = nonParticipants.getText().toString();
            if (!"".equals(painAttend) || !"".equals(totGifts) || !"".equals(nonParticipant)) {
                new android.app.AlertDialog.Builder(MeetingDetailsActivity.this)
                        .setMessage("Your current data will be lost. Do you wish to continue?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                dialog.dismiss();
                            }
                        }).show();
            } else {
                finish();
            }
        } else if (menuItem.getItemId() == R.id.upload) {

            if(!validate()){
                return true;
            }
            addToDb();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void addToDb(){
        MeetingEntity entity = new MeetingEntity();
        entity.detailStartTime = System.currentTimeMillis()/ 1000L+"";
        entity.painterAttendance = painterAttend.getText().toString();
        entity.totalGiftsGiven = totalGifts.getText().toString();
        entity.nonParticipants = nonParticipants.getText().toString();
        entity.meeting_field1 = typeSpin.getSelectedItem().toString();
        entity.meeting_field4 = remark1.getText().toString();
        entity.meeting_field5 = remark2.getText().toString();
        if(otherLayout.getVisibility() == View.VISIBLE){
            entity.meeting_field2 = newMeetingType.getText().toString();
        }else {
            entity.meeting_field2 = "";
        }
        entity.planningId = getIntent().getIntExtra("plan_id",0);

        long id = UpdateQueries.updateMeetByPlanId(this, entity);

        if(id == 0) {
            InsertQueries.insertMeeting(this, entity);
        }

        if (!ContextCommons.isOnline(this)) {
            Toast.makeText(this, R.string.enable_internet, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        new LDRCaptureTask(getBaseContext())
        {

            @Override
            public void afterExecution(String ldr) {

                new SendMeetingDataTask(MeetingDetailsActivity.this,getIntent().getIntExtra("plan_id",0), 0,1,ldr){

                    @Override
                    protected void afterExecution(boolean result, String msg,int errCode, int saleId) {

                        if(result){
                            finish();
                            Toast.makeText(MeetingDetailsActivity.this, R.string.data_uploaded, Toast.LENGTH_LONG).show();
                        }else if(errCode == 403){
                            AlertDialog.Builder builder = new AlertDialog.Builder(MeetingDetailsActivity.this);
                            builder.setMessage("Session expired. Please login again.")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent intent = new Intent(MeetingDetailsActivity.this, LoginActivity.class);
                                            InsertQueries.setSetting(MeetingDetailsActivity.this, Settings.PASSWORD, "");
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }else {
                            Toast.makeText(MeetingDetailsActivity.this, msg, Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                }.execute();

            }
        }.execute();



    }

    private boolean validate() {

        boolean retVal = true;
        int msg = 0;

        painAttend = painterAttend.getText().toString();
        totGifts = totalGifts.getText().toString();
        nonParticipant = nonParticipants.getText().toString();

        if(otherLayout.getVisibility() == View.VISIBLE){
            if (newMeetingType.getText().toString().length() == 0){
                retVal = false;
                msg = R.string.valid_other_meeting;
                Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
            }
        }

        if(painAttend.length() == 0 && retVal){
            retVal = false;
            msg = R.string.valid_painter_attnd;
            Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
        }

        if(totGifts.length() == 0 && retVal){
            retVal = false;
            msg = R.string.valid_total_gifts;
            Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
        }

        if(nonParticipant.length() == 0 && retVal){
            retVal = false;
            msg = R.string.valid_non_participants;
            Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
        }

        if(totGifts.length()>0 && painAttend.length()>0){
            if(Integer.parseInt(totGifts)>Integer.parseInt(painAttend)){
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MeetingDetailsActivity.this);
                builder.setMessage(R.string.painvsgifts)
                        .setCancelable(false)
                        .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                          addToDb();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                    }
                }).show();

                retVal = false;
            }

        }


        return retVal;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        painAttend = painterAttend.getText().toString();
        totGifts = totalGifts.getText().toString();
        nonParticipant = nonParticipants.getText().toString();
        if (!"".equals(painAttend) || !"".equals(totGifts) || !"".equals(nonParticipant)) {
            new android.app.AlertDialog.Builder(MeetingDetailsActivity.this)
                    .setMessage("Your current data will be lost. Do you wish to continue?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            dialog.dismiss();
                        }
                    }).show();
        } else {
            finish();
        }
    }
}
