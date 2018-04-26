package com.nebula.connect;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.nebula.connect.entities.DealerEntity;
import com.nebula.connect.entities.PainterEntity;
import com.nebula.connect.entities.PlanningEntity;
import com.nebula.connect.queries.InsertQueries;
import com.nebula.connect.queries.SelectQueries;
import com.nebula.connect.queries.UpdateQueries;

import java.util.ArrayList;

/**
 * Created by Sonam on 29/3/17.
 */
public class PainterDataActivity extends AppCompatActivity {

    private static final String TAG=PainterDataActivity.class.getSimpleName();
    private EditText nameEdit,nppEdit,mobileEdit,bussiness_started,qualification,team_size,dealer_code,dealer_name
            ,dealer_contact,order_booking,remark1,remark2;
    private Spinner dealerSpin;
    private ArrayList<DealerEntity> arrayDealer;
    private boolean retVal = false;
    private ArrayList<PainterEntity> painterEntities ;
    PainterEntity painterEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_painterdata);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        getSupportActionBar().setTitle("Attendee Detail");
        nameEdit = (EditText) findViewById(R.id.name);
        nppEdit = (EditText) findViewById(R.id.npp);
        mobileEdit = (EditText) findViewById(R.id.mobEdit);
        bussiness_started = (EditText) findViewById(R.id.business_started_year);
        qualification = (EditText) findViewById(R.id.qualification);
        team_size = (EditText) findViewById(R.id.team_size);
        dealer_code = (EditText) findViewById(R.id.dealer_code);
        dealer_name = (EditText) findViewById(R.id.dealer_name);
        dealer_contact = (EditText) findViewById(R.id.dealer_contact);
        order_booking = (EditText) findViewById(R.id.order_booking);
        remark1 = (EditText) findViewById(R.id.remark1);
        remark2 = (EditText) findViewById(R.id.remark2);
        int planningId = getIntent().getIntExtra("painterId", 0);

        dealerSpin = (Spinner) findViewById(R.id.dealer);

        painterEntity = SelectQueries.getPainterData(this, planningId);

        String painterName = painterEntity.painterName;
        String mobile = painterEntity.contact;
        String businessStarted = painterEntity.business_started_year;
        String qualification_str = painterEntity.qualification;
        String teamsize_str = painterEntity.team_size;
        String dealerCode = painterEntity.dealer_code;
        String dealerName = painterEntity.dealerName;
        String dealerContact = painterEntity.dealer_contact;
        String npp = painterEntity.nppCode;
        String orderBooking = painterEntity.order_booking;
        String remark1_str = painterEntity.remark1;
        String remark2_str = painterEntity.remark2;


        if (painterEntity.painterId > 0) {

            nameEdit.setText(painterName);
            mobileEdit.setText(mobile);
            bussiness_started.setText(businessStarted);
            qualification.setText(qualification_str);
            team_size.setText(teamsize_str);
            dealer_code.setText(dealerCode);
            dealer_name.setText(dealerName);
            dealer_contact.setText(dealerContact);
            nppEdit.setText(npp);
            order_booking.setText(orderBooking);
            remark1.setText(remark1_str);
            remark2.setText(remark2_str);


        } else {

            nameEdit.setText("");
            mobileEdit.setText("");
            bussiness_started.setText("");
            qualification.setText("");
            team_size.setText("");
            dealer_code.setText("");
            dealer_name.setText("");
            dealer_contact.setText("");
            nppEdit.setText("");
            order_booking.setText("");
            remark1.setText("");
            remark2.setText("");

        }
    }


        //painterEntities = SelectQueries.getPainterByPlanningId(this,getIntent().getIntExtra("plan_id",0),0);

        //String paintername = painterEntities.add()

        //String dealerName = getIntent().getStringExtra("dealerName");
/*
        nameEdit.setText(getIntent().getStringExtra("painterName"));
        nppEdit.setText(getIntent().getStringExtra("nppCode"));
        mobileEdit.setText(getIntent().getStringExtra("mobile"));

        bussiness_started.setText(getIntent().getStringExtra("bussinessStarted"));
        qualification.setText(getIntent().getStringExtra("qualification"));
        team_size.setText(getIntent().getStringExtra("teamSize"));
        dealer_code.setText(getIntent().getStringExtra("dealerCode"));
        dealer_name.setText(getIntent().getStringExtra("dealerName"));
        dealer_contact.setText(getIntent().getStringExtra("dealerContact"));
        order_booking.setText(getIntent().getStringExtra("orderBooking"));
        remark1.setText(getIntent().getStringExtra("remark1"));
        remark2.setText(getIntent().getStringExtra("remark2"));*/



     /*   arrayDealer = SelectQueries.getDealerByMeetingId(this, getIntent().getIntExtra("plan_id",0));

        ArrayList<String> arraySpinner = new ArrayList<>();
        for (int i=0;i<arrayDealer.size()+1;i++){
            if(i==0){
                arraySpinner.add("Select Dealer");
            }else {
                arraySpinner.add(arrayDealer.get(i-1).name);
            }
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, arraySpinner);
        dealerSpin.setAdapter(adapter);

        if(!"".equals(dealerName)) {
            for (int i = 0; i<arraySpinner.size(); i++){
                if(arraySpinner.get(i).equalsIgnoreCase(dealerName)){
                    dealerSpin.setSelection(i);
                    break;
                }
            }
        }
*/


    public void onSave(View v){
        if (v.getId() == R.id.save_info) {
            if(validate()) {
                addToDb();
            }
        }
    }

    public void addToDb(){

            PainterEntity entity = new PainterEntity();
            entity.painterName = nameEdit.getText().toString();
            entity.nppCode = nppEdit.getText().toString();
            entity.contact = mobileEdit.getText().toString();
            //entity.dealerName = dealerSpin.getSelectedItem().toString();
            entity.detailStartTime = System.currentTimeMillis() / 1000L + "";
           // entity.dealerId = arrayDealer.get(dealerSpin.getSelectedItemPosition()-1).dealerId+"";
            entity.planningId = getIntent().getIntExtra("plan_id",0)+"";
            entity.business_started_year = bussiness_started.getText().toString();
            entity.qualification = qualification.getText().toString();
            entity.team_size = team_size.getText().toString();
            entity.dealer_code = dealer_code.getText().toString();
            entity.dealerName = dealer_name.getText().toString();
            entity.dealer_contact = dealer_contact.getText().toString();
            entity.order_booking = order_booking.getText().toString();
            entity.remark1 = remark1.getText().toString();
            entity.remark2 = remark2.getText().toString();




        if(getIntent().getIntExtra("painterId",0) == 0) {
                InsertQueries.insertPainter(this, entity);
            }else {
                UpdateQueries.updatePaintByPaintId(this,entity,getIntent().getIntExtra("painterId",0));
            }
            finish();

    }

    public boolean isDuplicateMobile(){
        boolean retValue = false;
        ArrayList<String> arrayList =  SelectQueries.getPainterMobileNo(PainterDataActivity.this,getIntent().getIntExtra("plan_id",0));
        String mobileValue = mobileEdit.getText().toString().trim();
        if(arrayList.size() > 0){
            if(arrayList.contains(mobileValue)){
                new android.app.AlertDialog.Builder(PainterDataActivity.this)
                        .setMessage(R.string.duplicate)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                dialog.dismiss();
                            }
                        }).show();
                retValue = true;
            }else{

                retValue = false;
            }

        }

        return retValue;
    }

    public boolean validate() {
        String nameValue, mobileValue, dealerValue, dealerContact;
        boolean cancel = false;
        View focusView = null;

        nameEdit.setError(null);
        mobileEdit.setError(null);

        nameValue = nameEdit.getText().toString().trim();
        mobileValue = mobileEdit.getText().toString().trim();
        dealerContact = dealer_contact.getText().toString().trim();
        //dealerValue = dealerSpin.getSelectedItem().toString().trim();


        if (TextUtils.isEmpty(nameValue) && !cancel) {
            nameEdit.setError("Name is required");
            focusView = nameEdit;
            cancel = true;
        } else if (TextUtils.isEmpty(mobileValue) && !cancel) {
            mobileEdit.setError("Mobile Number is required");
            focusView = mobileEdit;
            cancel = true;

        } else if (isCodeValidate(nameValue) && !cancel) {
            nameEdit.setError(getString(R.string.error_more_3));
            focusView = nameEdit;
            cancel = true;
        }

        if (!TextUtils.isEmpty(mobileValue) && !isContactValid(mobileValue) && !cancel) {
            mobileEdit.setError("Please enter valid mobile no.");
            focusView = mobileEdit;
            cancel = true;
        } else if (!TextUtils.isEmpty(mobileValue) && isDuplicateMobile() && !cancel) {
            focusView = mobileEdit;
            cancel = true;
        } /*else if (!TextUtils.isEmpty(dealerContact) && !isContactValid(dealerContact) && !cancel) {
            dealer_contact.setError("Please enter valid mobile no.");
            focusView = dealer_contact;
            cancel = true;
        }*/


     /*   if("Select Dealer".equals(dealerValue) && !cancel){
            Toast.makeText(PainterDataActivity.this,"Please select Dealer to proceed",Toast.LENGTH_LONG).show();
            focusView = mobileEdit;
            cancel = true;
        }*/

            if (cancel) {
                if (focusView != null) {
                    focusView.requestFocus();
                }
                return false;
            } else if (TextUtils.isEmpty(mobileValue)) {
                new android.app.AlertDialog.Builder(PainterDataActivity.this)
                        .setMessage(" Do you really want to keep mobile number empty. Continue?")
                        .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                dialog.dismiss();
                                addToDb();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                retVal = false;
                                dialog.dismiss();
                            }
                        }).show();
                return false;

            } else {
                return true;
            }
        }


    private boolean isCodeValidate(String code) {
        return code.length() < 3;
    }

    private boolean isNppValid(String code) {
        return code.length() > 0;
    }

    private boolean isContactValid(String password) {
        return password.length() == 10;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();

        }
        return true;
    }

    }
