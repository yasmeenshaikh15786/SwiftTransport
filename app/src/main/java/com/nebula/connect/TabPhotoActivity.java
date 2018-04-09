package com.nebula.connect;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bizonesoft.metadatainfo.LDRCaptureTask;
import com.nebula.connect.entities.SaleMetadataEntity;
import com.nebula.connect.internetTask.SendSalesMetadataTask;
import com.nebula.connect.location.LocationCaptureTask;
import com.nebula.connect.logreports.Logger;
import com.nebula.connect.queries.DeleteQueries;
import com.nebula.connect.queries.InsertQueries;
import com.nebula.connect.queries.SelectQueries;
import com.nebula.connect.utilities.SlidingTabLayout;

import java.util.ArrayList;


/**
 * Created by Sonam on 24/3/17.
 */
public class TabPhotoActivity extends AppCompatActivity {

    private static final String TAG=TabPhotoActivity.class.getSimpleName();
    private ViewPager viewPager;
    private TabsPagerAdapter adapter;
    private SlidingTabLayout tabs;
    private String lattitude, longitude, accuracy;
    private boolean isLocationCaptured = false;
    Toolbar toolbar;
    private static final int MY_CAMERA_PERMI = 1;

    // Tab titles

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_photo_main);

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter = new TabsPagerAdapter(getSupportFragmentManager());

        // Assigning ViewPager View and setting the adapter
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);
        // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        tabs.setEnabled(false);

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorPrimary);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(viewPager);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Meeting Photos");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_CAMERA_PERMI) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // We can now safely use the API we requested access to
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, R.string.stor_permission, Toast.LENGTH_LONG).show();
                }
            } else {
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setMessage(R.string.camera_permission+"");
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //ActivityCompat.requestPermissions(ActivityStart.this, new String[]{Manifest.permission.READ_PHONE_STATE}, MY_READ_PHONE_STATE);
                        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }

                });

                alertDialog.show();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isLocationCaptured) {
            LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps();
            } else {
                new LocationCaptureTask(this) {
                    @Override
                    protected void afterExecution(String lat, String lon, String accu) {
                        // Logger.d(TAG, "Lat =" + lat + " Lon =" + lon);
                        isLocationCaptured = true;
                        lattitude = lat;
                        longitude = lon;
                        accuracy = accu;
                    }
                }.execute();
            }
        }
    }

    public ArrayList<String> getLocation() {
        ArrayList<String> location = new ArrayList<String>();
        location.add(lattitude);
        location.add(longitude);
        location.add(accuracy);
        return location;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, please enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                        //  finish();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void addMeetShop(View v) {
        if (checkForPhotoPermission()) {
            adapter.getMeetingPhotoFragment().addShop(v);
        }
    }

    private boolean checkForPhotoPermission() {
        boolean isGiven = false;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMI);
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                isGiven = true;
            } else {
                Toast.makeText(this,R.string.stor_permission, Toast.LENGTH_LONG).show();
            }
        }

        return isGiven;
    }

    public void addPainterShop(View v) {
        if (checkForPhotoPermission()) {
            adapter.getPainterFragment().addShop(v);
        }
    }

    public void onClickImg(View v) {
        if (checkForPhotoPermission()) {
            adapter.getReportCardFragment().onClickImg(v);
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
           addToDb();
            finish();

        } else if (menuItem.getItemId() == R.id.upload) {
             int count = addToDb();

            if (!ContextCommons.isOnline(this)) {
                Toast.makeText(this, R.string.enable_internet, Toast.LENGTH_LONG).show();
                finish();
                return true;
            } else {
                if(count > 0) {
                    new android.app.AlertDialog.Builder(this)
                            .setMessage(R.string.sure_photo_meeting)
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
                }else {
                    new android.app.AlertDialog.Builder(this)
                            .setMessage(R.string.cap_photo)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                    dialog.dismiss();
                                }
                            }).show();
                    return true;
                }
            }

            new LDRCaptureTask(getBaseContext()) {
                @Override
                public void afterExecution(String ldr) {

                    new SendSalesMetadataTask(TabPhotoActivity.this, getIntent().getIntExtra("plan_id", 0),ldr) {

                        @Override
                        protected void afterExecution(int planIdR) {

                        }

                        @Override
                        protected void notUploaded(String code, String msg) {
                            if ("403".equals(code)) {
                                // expire
                                AlertDialog.Builder builder = new AlertDialog.Builder(TabPhotoActivity.this);
                                builder.setMessage("Session expired. Please login again.")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Intent intent = new Intent(TabPhotoActivity.this, LoginActivity.class);
                                                InsertQueries.setSetting(TabPhotoActivity.this, Settings.PASSWORD, "");
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

            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public int addToDb(){

        ArrayList<SaleMetadataEntity> totalMeta = new ArrayList<>();
        totalMeta.addAll(adapter.getPainterFragment().getSaleMetadataEntities());
        totalMeta.addAll(adapter.getMeetingPhotoFragment().getSaleMetadataEntities());
        totalMeta.addAll(adapter.getReportCardFragment().getSaleMetadataEntities());

        DeleteQueries.deleteSaleMetadataRecord(TabPhotoActivity.this, getIntent().getIntExtra("plan_id", 0));
        Logger.d(TAG,"Delete record");
        ArrayList<String> loc = getLocation();
        for (SaleMetadataEntity entity : totalMeta) {
            entity.latitude = loc.get(0);
            entity.longitude = loc.get(1);
            entity.accuracy = loc.get(2);
        }

        int count = 0;
        Logger.d(TAG,"totalMeta = "+totalMeta);
        for (SaleMetadataEntity entity : totalMeta) {
            if (entity.filePath != null && !Constants.CLOSED.equals(entity.status)) {
                Logger.d(TAG,"insert entity = "+entity);
                boolean ifRecordExists = SelectQueries.isSaleMetaExist(this,entity);
                if(!ifRecordExists){
                    InsertQueries.insertSaleMetadata(this, entity);
                    count ++;
                }


            }
        }
       return count;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        addToDb();
        finish();
    }
}