package com.nebula.connect;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.Toast;

import com.bizonesoft.metadatainfo.LDRCaptureTask;
import com.nebula.connect.entities.PlanningEntity;
import com.nebula.connect.entities.SaleMetadataEntity;
import com.nebula.connect.entities.StartDayEntity;
import com.nebula.connect.internetTask.SendMeetingDataTask;
import com.nebula.connect.location.LocationCaptureTask;
import com.nebula.connect.logreports.Logger;
import com.nebula.connect.queries.InsertQueries;
import com.nebula.connect.queries.SelectQueries;

import java.io.File;

/**
 * Created by Sonam on 22/2/17.
 */
public class StartDayActivity extends AppCompatActivity {

    private static final String TAG = StartDayActivity.class.getSimpleName();
    private Uri outputFileUri = null;
    private ImageView shop, addImg;
    private static final int SHOP_CAMERA = 2;
    private boolean isLocationCaptured = false;
    private static final int MY_CAMERA = 1;
    private TextClock clock;
    private EditText remarks,mobEdit;
    private Button startDisplayBtn,startBtn;
    private StartDayEntity startDayEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_day);
        Logger.d(TAG, "inside onCreate");
        shop = (ImageView) findViewById(R.id.photo);
        clock = (TextClock) findViewById(R.id.ct_time);
        remarks = (EditText) findViewById(R.id.training_center);
        mobEdit = (EditText) findViewById(R.id.mobEdit);
        startDisplayBtn = (Button) findViewById(R.id.start);
        startBtn = (Button) findViewById(R.id.stardt);
        addImg = (ImageView) findViewById(R.id.add_photo);
        // started=(TextView)findViewById(R.id.started);
        int planId = getIntent().getIntExtra("plan_id", 0);
        PlanningEntity planningEntity = SelectQueries.getPlanningById(this,planId);
        startDayEntity = new StartDayEntity();
        startDayEntity.imagePath = null;
      //  remarks.setText(planningEntity.trainingCenterName);



        StartDayEntity entity = SelectQueries.getStartDayElementsByStartId(this, planId);
        if (entity.startId > 0) {
            remarks.setText(entity.remarks);
            remarks.setEnabled(false);
            mobEdit.setText(entity.mobile+"");
            mobEdit.setEnabled(false);
            startDisplayBtn.setVisibility(View.VISIBLE);
            startBtn.setVisibility(View.GONE);
            clock.setVisibility(View.GONE);
            startDisplayBtn.setBackgroundResource(R.color.status_closed_bg);
            startDisplayBtn.setTextColor(getResources().getColor(R.color.list_txt));
            startDisplayBtn.setText("Meeting started at\n " + SelectQueries.getSetting(StartDayActivity.this, Settings.START_TIME_DISPLAY));
            Bitmap b = BitmapFactory.decodeFile(entity.imagePath);
            shop.setImageBitmap(Bitmap.createScaledBitmap(b, 150, 150, false));
            addImg.setEnabled(false);
        } else {
            remarks.setEnabled(true);
            mobEdit.setEnabled(true);
            startBtn.setVisibility(View.VISIBLE);
            startDisplayBtn.setVisibility(View.GONE);
            clock.setVisibility(View.VISIBLE);
            startBtn.setText("Start Meeting");
            addImg.setEnabled(true);
            mobEdit.setText("");
            remarks.setText(planningEntity.trainingCenterName);
        }

        startDisplayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textBtn = startDisplayBtn.getText().toString();
                if (textBtn.startsWith("Meeting started at")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(StartDayActivity.this);
                    builder.setMessage(textBtn)
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });


        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        getSupportActionBar().setTitle("Meeting Start");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.upload_menu, menu);
        return true;
    }


    public void captureLocation() {
        if (!isLocationCaptured) {
            LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps();
            } else {
                new LocationCaptureTask(this) {
                    @Override
                    protected void afterExecution(String lat, String lon, String accuracy) {
                        Logger.d(TAG, "Lat =" + lat + " Lon =" + lon);
                        isLocationCaptured = true;
                        startDayEntity.latitude = lat;
                        startDayEntity.longitude = lon;
                        startDayEntity.accuracy = accuracy;

                        afterLocationCapture();
                    }
                }.execute();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            Intent intent = new Intent();
            intent.putExtra("photoArray", startDayEntity);
            setResult(Activity.RESULT_OK, intent);
            finish();
        } else if (menuItem.getItemId() == R.id.upload) {
            String textBtn = startDisplayBtn.getText().toString();
            if (textBtn.startsWith("Meeting started at")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(StartDayActivity.this);
                builder.setMessage(textBtn)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            } else if(startBtn.getText().toString().startsWith("Start Meeting")){
                validate();
            }
        }
            return super.onOptionsItemSelected(menuItem);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == Activity.RESULT_OK && requestCode == SHOP_CAMERA) {
                String imgPath = "", tag = "";
                imgPath = outputFileUri.getPath();
                Logger.d(TAG, "imgPath=" + imgPath);
                Commons.decodeFile(imgPath, Constants.IMG_WIDTH, Constants.IMG_HEIGHT);
                startDayEntity.imagePath = imgPath;
                startDayEntity.startDate = (int) (System.currentTimeMillis() / 1000L);
                //   startDayEntity.created=System.currentTimeMillis();
                // saleMetadataEntities.get(0).tag="shop";
                Bitmap b = BitmapFactory.decodeFile(imgPath);
                shop.setImageBitmap(Bitmap.createScaledBitmap(b, 150, 150, false));
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
            e.printStackTrace();
        }
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
                        //startActivity(elemAct);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onBackPressed() {
        if (null != (startDayEntity.imagePath)) {
            new android.app.AlertDialog.Builder(StartDayActivity.this)
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

    public void validate() {
        startDayEntity.remarks = remarks.getText().toString();
        startDayEntity.startId = getIntent().getIntExtra("plan_id", 0);
        startDayEntity.mobile = mobEdit.getText().toString();

        if (null == (startDayEntity.imagePath)) {
            Toast.makeText(this, R.string.capture_photo, Toast.LENGTH_SHORT).show();
        } else if ((startDayEntity.remarks).equals("")) {
            Toast.makeText(this, R.string.add_training_center, Toast.LENGTH_SHORT).show();
        } else if ((startDayEntity.mobile).equals("")) {
            Toast.makeText(this, R.string.add_contact, Toast.LENGTH_SHORT).show();
        }else if ((startDayEntity.mobile).length() != 10) {
            Toast.makeText(this, R.string.proper_contact, Toast.LENGTH_SHORT).show();
        } else {
            captureLocation();
        }

    }

    public void afterLocationCapture() {

        SaleMetadataEntity saleMetadataEntity = new SaleMetadataEntity();
        saleMetadataEntity.meeting_id = getIntent().getIntExtra("plan_id", 0);
        saleMetadataEntity.tag = Constants.PHOTO_START;
        saleMetadataEntity.filePath = startDayEntity.imagePath;
        saleMetadataEntity.latitude = startDayEntity.latitude;
        saleMetadataEntity.longitude = startDayEntity.longitude;
        saleMetadataEntity.accuracy = startDayEntity.accuracy;
        saleMetadataEntity.created = startDayEntity.startDate;
        InsertQueries.insertStartDayData(this, startDayEntity);
        InsertQueries.insertSaleMetadata(this,saleMetadataEntity);
        InsertQueries.setSetting(StartDayActivity.this, Settings.START_TIME, System.currentTimeMillis() + "");
        InsertQueries.setSetting(StartDayActivity.this, Settings.START_TIME_DISPLAY, clock.getText().toString());

        if (!ContextCommons.isOnline(this)) {
            Toast.makeText(this, R.string.enable_internet, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        new LDRCaptureTask(getBaseContext()) {
            @Override
            public void afterExecution(String ldr) {

                new SendMeetingDataTask(StartDayActivity.this, getIntent().getIntExtra("plan_id", 0), 0,1,ldr) {

                    @Override
                    protected void afterExecution(boolean result, String msg, int errCode, int saleId) {

                        if (result) {
                            finish();
                            Toast.makeText(StartDayActivity.this, R.string.data_uploaded, Toast.LENGTH_LONG).show();
                        } else if (errCode == 403) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(StartDayActivity.this);
                            builder.setMessage("Session expired. Please login again.")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent intent = new Intent(StartDayActivity.this, LoginActivity.class);
                                            InsertQueries.setSetting(StartDayActivity.this, Settings.PASSWORD, "");
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        } else {
                            Toast.makeText(StartDayActivity.this, msg, Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                }.execute();
            }
        }.execute();

    }

    public void onClickImg(View v) {
        Logger.d(TAG, "inside onClick");
        if (v.getId() == R.id.add_photo) {
            if (ContextCompat.checkSelfPermission(StartDayActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(StartDayActivity.this, new String[]{Manifest.permission.CAMERA}, MY_CAMERA);
            } else {
                if (ContextCompat.checkSelfPermission(StartDayActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    takePictureShop();
                } else {
                    Toast.makeText(StartDayActivity.this, "Please give storage permission to proceed further.", Toast.LENGTH_LONG).show();
                }
            }

        } else if (v.getId() == R.id.photo) {
            if (!(null == (startDayEntity.imagePath))) {
                Intent i = new Intent(StartDayActivity.this, ImageActivity.class);
                i.putExtra("path", startDayEntity.imagePath);
                startActivity(i);
            }
        }
    }

    public void takePictureShop() {
        File file = Commons.getOutputMediaFile();
        outputFileUri = Uri.fromFile(file);
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(cameraIntent, SHOP_CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_CAMERA) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // We can now safely use the API we requested access to
                takePictureShop();
            } else {
                AlertDialog alertDialog = new AlertDialog.Builder(StartDayActivity.this).create();
                alertDialog.setMessage("For Nebula Connect to work properly you require Camera permission");
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
}











