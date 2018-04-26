package com.nebula.connect;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bizonesoft.metadatainfo.LDRCaptureTask;
import com.nebula.connect.entities.PainterEntity;
import com.nebula.connect.logreports.Logger;
import com.nebula.connect.queries.SelectQueries;

import java.util.ArrayList;

import com.nebula.connect.internetTask.SendMeetingDataTask;
import com.nebula.connect.queries.InsertQueries;

/**
 * Created by Sonam on 29/3/17.
 */
public class PainterDataListActivity extends AppCompatActivity {

    private static final String TAG=PainterDataListActivity.class.getSimpleName();
    private EditText actv;
    private ListView list;
    private ArrayList<PainterEntity> painterEntities ;
    private TextView noRecord;
    private LinearLayout checkLayout;
    private LinearLayout linearSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_painterdata_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        getSupportActionBar().setTitle("Attendee Data");
        linearSearch = (LinearLayout)findViewById(R.id.linear_search);
        list=(ListView)findViewById(R.id.list_view);
        actv = (EditText) findViewById(R.id.search_shop);
        noRecord = (TextView) findViewById(R.id.norecord);
        checkLayout = (LinearLayout) findViewById(R.id.checkll);
    }



    public void addPainter(View v){
        if (v.getId() == R.id.add_painter) {
            Intent i = new Intent(PainterDataListActivity.this, PainterDataActivity.class);
            i.putExtra("painterId",0);
            i.putExtra("painterName","");
            i.putExtra("nppCode","");
            i.putExtra("dealerName","");
            i.putExtra("mobile","");
            i.putExtra("plan_id",getIntent().getIntExtra("plan_id",0));
            startActivity(i);
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
            finish();

        } else if (menuItem.getItemId() == R.id.upload) {

            if (!ContextCommons.isOnline(this)) {
                Toast.makeText(this, R.string.enable_internet, Toast.LENGTH_LONG).show();
                finish();
                return true;
            }
            ArrayList<PainterEntity> painterEntityArrayList = SelectQueries.getPainterByPlanningId(this,getIntent().getIntExtra("plan_id",0),1);
            if(painterEntityArrayList.size() < 1){
                new android.app.AlertDialog.Builder(this)
                        .setMessage(R.string.add_painter)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                dialog.dismiss();
                            }
                        }).show();
                return true;
            }

            new LDRCaptureTask(getBaseContext()) {
                @Override
                public void afterExecution(String ldr) {

                    new SendMeetingDataTask(PainterDataListActivity.this,getIntent().getIntExtra("plan_id",0),0,1,ldr){

                        @Override
                        protected void afterExecution(boolean result, String msg,int errCode, int saleId) {

                            if(result){
                                finish();
                                Toast.makeText(PainterDataListActivity.this, R.string.data_uploaded, Toast.LENGTH_LONG).show();
                            }else if(errCode == 403){
                                AlertDialog.Builder builder = new AlertDialog.Builder(PainterDataListActivity.this);
                                builder.setMessage("Session expired. Please login again.")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Intent intent = new Intent(PainterDataListActivity.this, LoginActivity.class);
                                                InsertQueries.setSetting(PainterDataListActivity.this, Settings.PASSWORD, "");
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }else {
                                Toast.makeText(PainterDataListActivity.this, msg, Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }
                    }.execute();

                }
            }.execute();

            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public class CustomList extends BaseAdapter {

        @Override
        public int getCount() {
            return painterEntities.size();
        }

        @Override
        public Object getItem(int position) {
            return painterEntities.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder{
            TextView painterName,nppCode,dealerName,mobile;
        }
        @Override
        public View getView(int pos, View convertView, ViewGroup arg2) {
            Logger.d(TAG, "inside getView");


            ViewHolder holder = null;

            if(convertView == null) {
                convertView =getLayoutInflater().inflate(R.layout.painter_list_item, null);

                holder=new ViewHolder();
                holder.painterName = (TextView) convertView.findViewById(R.id.painter_name);
                holder.nppCode = (TextView) convertView.findViewById(R.id.nppcode);
                holder.dealerName = (TextView) convertView.findViewById(R.id.dealerName);
                holder.mobile = (TextView) convertView.findViewById(R.id.mobile);
                convertView.setTag(holder);

            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            PainterEntity entity = painterEntities.get(pos);

            if (entity.status.equals(Constants.CLOSED)) {
                convertView.setBackgroundResource(R.color.status_closed_bg);
            }else {
                convertView.setBackgroundResource(R.color.colorLight);

            }

            holder.painterName.setText(entity.painterName);
            if("".equals(entity.nppCode)){
                holder.nppCode.setText("Not Specified");
            }else {
                holder.nppCode.setText(entity.nppCode);
            }

            holder.dealerName.setText(entity.dealerName);
            if("".equals(entity.contact)){
                holder.mobile.setText("Not Specified");
            }else {
                holder.mobile.setText(entity.contact);
            }
            return convertView;
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        final CustomList adapter = new CustomList();
        painterEntities = SelectQueries.getPainterByPlanningId(this,getIntent().getIntExtra("plan_id",0),0);

        if(painterEntities.size() > 0) {
            noRecord.setVisibility(View.GONE);
            checkLayout.setVisibility(View.VISIBLE);
            list.setVisibility(View.VISIBLE);
        }

        if(painterEntities.size()>5){
            linearSearch.setVisibility(View.VISIBLE);
        }else{
            linearSearch.setVisibility(View.GONE);
        }

        actv.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                    painterEntities = SelectQueries.getPainterByFilter(PainterDataListActivity.this,getIntent().getIntExtra("plan_id",0),cs.toString());
             //   if(painterEntities != null && painterEntities.size()>0) {
                    adapter.notifyDataSetChanged();
              //  }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });



        Logger.d(TAG,"setting adapter");
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent i = new Intent(PainterDataListActivity.this,PainterDataActivity.class);
                i.putExtra("painterId",painterEntities.get(position).painterId);
                i.putExtra("painterName",painterEntities.get(position).painterName);
                i.putExtra("nppCode",painterEntities.get(position).nppCode);
                i.putExtra("dealerName",painterEntities.get(position).dealerName);
                i.putExtra("mobile",painterEntities.get(position).contact);
                i.putExtra("plan_id",getIntent().getIntExtra("plan_id",0));

                i.putExtra("bussinessStarted",painterEntities.get(position).business_started_year);
                i.putExtra("qualification",painterEntities.get(position).qualification);
                i.putExtra("teamSize",painterEntities.get(position).team_size);
                i.putExtra("dealerCode",painterEntities.get(position).dealer_code);
               // i.putExtra("dealerName",painterEntities.get(position).dealername);
                i.putExtra("dealerContact",painterEntities.get(position).dealer_contact);
                i.putExtra("orderBooking",painterEntities.get(position).order_booking);
                i.putExtra("remark1",painterEntities.get(position).remark1);
                i.putExtra("remark2",painterEntities.get(position).remark2);


                startActivity(i);
            }
        });




    }

}
