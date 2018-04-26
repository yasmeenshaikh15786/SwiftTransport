package com.nebula.connect;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nebula.connect.entities.SaleMetadataEntity;
import com.nebula.connect.logreports.Logger;
import com.nebula.connect.queries.SelectQueries;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Sonam on 24/3/17.
 */
public class ReportCardFragment  extends Fragment {

    private static final String TAG=ReportCardFragment.class.getSimpleName();
    public ImageView frontCam,backCam,front,back;
    private TextView frontStatus,backStatus;
    private Uri outputFileUri = null;
    private ArrayList<SaleMetadataEntity> saleMetadataEntities ;
    private static final int FRONT_CARD = 1;
    private static final int BACK_CARD = 2;
    private static final int FRONT_IMAGE = 1;
    private static final int BACK_IMAGE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_report_card, container, false);
        front = (ImageView) rootView.findViewById(R.id.front_img);
        back = (ImageView)rootView.findViewById(R.id.back_img);
        frontCam = (ImageView)rootView.findViewById(R.id.add_front);
        backCam = (ImageView)rootView.findViewById(R.id.add_back);
        frontStatus = (TextView)rootView.findViewById(R.id.status);
        backStatus = (TextView)rootView.findViewById(R.id.statusback);

        saleMetadataEntities= SelectQueries.getSaleMetadataElementsByTag(getActivity(),getActivity().getIntent().getIntExtra("plan_id",0),Constants.PHOTO_REPORT);

        if (saleMetadataEntities.size() == 0) {
            SaleMetadataEntity sale1 = new SaleMetadataEntity();
            sale1.filePath = null;
            sale1.tag = Constants.PHOTO_REPORT_FRONT;
            sale1.meeting_id = getActivity().getIntent().getIntExtra("plan_id", 0);
            sale1.status = Constants.OPEN;
            SaleMetadataEntity sale2 = new SaleMetadataEntity();
            sale2.filePath = null;
            sale2.tag = Constants.PHOTO_REPORT_BACK;
            sale2.meeting_id = getActivity().getIntent().getIntExtra("plan_id", 0);
            sale2.status = Constants.OPEN;
            saleMetadataEntities.add(sale1);
            saleMetadataEntities.add(sale2);
        } else if(saleMetadataEntities.size() == 1){
            Commons.decodeFile(saleMetadataEntities.get(0).filePath, Constants.IMG_WIDTH, Constants.IMG_HEIGHT);
            Bitmap b = BitmapFactory.decodeFile(saleMetadataEntities.get(0).filePath);

            if(saleMetadataEntities.get(0).tag.equals(Constants.PHOTO_REPORT_FRONT)) {
                SaleMetadataEntity sale2 = new SaleMetadataEntity();
                sale2.filePath = null;
                sale2.tag = Constants.PHOTO_REPORT_BACK;
                sale2.meeting_id = getActivity().getIntent().getIntExtra("plan_id", 0);
                sale2.status = Constants.OPEN;
                saleMetadataEntities.add(sale2);
                front.setImageBitmap(Bitmap.createScaledBitmap(b, 150, 150, false));
            }else if(saleMetadataEntities.get(0).tag.equals(Constants.PHOTO_REPORT_BACK)) {
                SaleMetadataEntity saleMetadataEntity = saleMetadataEntities.get(0);
                saleMetadataEntities.clear();
                SaleMetadataEntity sale1 = new SaleMetadataEntity();
                sale1.filePath = null;
                sale1.tag = Constants.PHOTO_REPORT_FRONT;
                sale1.meeting_id = getActivity().getIntent().getIntExtra("plan_id", 0);
                sale1.status = Constants.OPEN;
                saleMetadataEntities.add(sale1);
                saleMetadataEntities.add(saleMetadataEntity);
                back.setImageBitmap(Bitmap.createScaledBitmap(b, 150, 150, false));
            }

        } else {
            if (saleMetadataEntities.get(0).tag.equals(Constants.PHOTO_REPORT_FRONT)){
                Commons.decodeFile(saleMetadataEntities.get(0).filePath, Constants.IMG_WIDTH, Constants.IMG_HEIGHT);
                Bitmap b = BitmapFactory.decodeFile(saleMetadataEntities.get(0).filePath);
                Commons.decodeFile(saleMetadataEntities.get(1).filePath, Constants.IMG_WIDTH, Constants.IMG_HEIGHT);
                Bitmap b1 = BitmapFactory.decodeFile(saleMetadataEntities.get(1).filePath);

                front.setImageBitmap(Bitmap.createScaledBitmap(b, 150, 150, false));
                back.setImageBitmap(Bitmap.createScaledBitmap(b1, 150, 150, false));
            }else {
                SaleMetadataEntity entity1 = saleMetadataEntities.get(0);
                SaleMetadataEntity entity2 = saleMetadataEntities.get(1);
                saleMetadataEntities.clear();
                saleMetadataEntities.add(entity2);
                saleMetadataEntities.add(entity1);
                Commons.decodeFile(saleMetadataEntities.get(0).filePath, Constants.IMG_WIDTH, Constants.IMG_HEIGHT);
                Bitmap b = BitmapFactory.decodeFile(saleMetadataEntities.get(0).filePath);
                Commons.decodeFile(saleMetadataEntities.get(1).filePath, Constants.IMG_WIDTH, Constants.IMG_HEIGHT);
                Bitmap b1 = BitmapFactory.decodeFile(saleMetadataEntities.get(1).filePath);

                front.setImageBitmap(Bitmap.createScaledBitmap(b, 150, 150, false));
                back.setImageBitmap(Bitmap.createScaledBitmap(b1, 150, 150, false));
            }

        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(Constants.INPROGRESS.equals(saleMetadataEntities.get(0).status) &&
                saleMetadataEntities.get(0).tag.equals(Constants.PHOTO_REPORT_FRONT)) {
            frontStatus.setText("PENDING");
            frontStatus.setTextColor(getResources().getColor(R.color.status_inprogress));
        }else if(Constants.CLOSED.equals(saleMetadataEntities.get(0).status)&&
                saleMetadataEntities.get(0).tag.equals(Constants.PHOTO_REPORT_FRONT)){
            frontStatus.setText("UPLOADED");
            frontStatus.setTextColor(getResources().getColor(R.color.status_closed));
        }else if(saleMetadataEntities.get(0).status.equals(Constants.OPEN) &&
                saleMetadataEntities.get(0).tag.equals(Constants.PHOTO_REPORT_FRONT) ){
            frontStatus.setText("OPEN");
            frontStatus.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
        if(Constants.INPROGRESS.equals(saleMetadataEntities.get(1).status) &&
                saleMetadataEntities.get(1).tag.equals(Constants.PHOTO_REPORT_BACK)) {
            backStatus.setText("PENDING");
            backStatus.setTextColor(getResources().getColor(R.color.status_inprogress));
        }else if(Constants.CLOSED.equals(saleMetadataEntities.get(1).status)&&
                saleMetadataEntities.get(1).tag.equals(Constants.PHOTO_REPORT_BACK)){
            backStatus.setText("UPLOADED");
            backStatus.setTextColor(getResources().getColor(R.color.status_closed));
        }else if(saleMetadataEntities.get(1).tag.equals(Constants.PHOTO_REPORT_BACK)
                && saleMetadataEntities.get(1).status.equals(Constants.OPEN)){
            backStatus.setText("OPEN");
            backStatus.setTextColor(getResources().getColor(R.color.colorPrimary));
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == Activity.RESULT_OK && requestCode == FRONT_CARD) {
                String imgPath = "", tag = "";
                imgPath = outputFileUri.getPath();
                Logger.d(TAG, "imgPath=" + imgPath);
                Commons.decodeFile(imgPath, Constants.IMG_WIDTH, Constants.IMG_HEIGHT);
                saleMetadataEntities.get(0).filePath = imgPath;
                saleMetadataEntities.get(0).created = (int) (System.currentTimeMillis() / 1000L);
                saleMetadataEntities.get(0).tag = Constants.PHOTO_REPORT_FRONT;
                Bitmap b = BitmapFactory.decodeFile(imgPath);
                front.setImageBitmap(Bitmap.createScaledBitmap(b, 150, 150, false));
            } else if (resultCode == Activity.RESULT_OK && requestCode == BACK_CARD) {
                String imgPath = "", tag = "";
                imgPath = outputFileUri.getPath();
                Logger.d(TAG, "imgPath=" + imgPath);
                Commons.decodeFile(imgPath, Constants.IMG_WIDTH, Constants.IMG_HEIGHT);
                saleMetadataEntities.get(1).filePath = imgPath;
                saleMetadataEntities.get(1).created = (int) (System.currentTimeMillis() / 1000L);
                saleMetadataEntities.get(1).tag = Constants.PHOTO_REPORT_BACK;
                Bitmap b = BitmapFactory.decodeFile(imgPath);
                back.setImageBitmap(Bitmap.createScaledBitmap(b, 150, 150, false));
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
            e.printStackTrace();
        }
    }

    public ArrayList<SaleMetadataEntity> getSaleMetadataEntities(){
        return saleMetadataEntities;
    }



    public void takeFrontCardPic() {

        if(Build.VERSION.SDK_INT>=24)
        {
            File file = Commons.getOutputMediaFile();
            Uri uri = FileProvider.getUriForFile(getActivity(),   getActivity().getApplicationContext().getPackageName()+".fileprovider",file);
            outputFileUri= Uri.fromFile(file.getAbsoluteFile());
            String path=outputFileUri.getPath();
            String filepath=file.getAbsolutePath();
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(cameraIntent, FRONT_CARD);

        }else {

            File file = Commons.getOutputMediaFile();
            outputFileUri = Uri.fromFile(file);
            Log.d("outputFileUri", String.valueOf(outputFileUri));
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            startActivityForResult(cameraIntent, FRONT_CARD);

           /* outputFileUri=Uri.fromFile(file);
            outputFileUri = FileProvider.getUriForFile(StartDayActivity.this, BuildConfig.APPLICATION_ID + ".provider",file);
            outputFileUri = Uri.fromFile(file);*/

        }

        StrictMode.VmPolicy.Builder newbuilder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(newbuilder.build());



      /*  File file = Commons.getOutputMediaFile();
        outputFileUri = Uri.fromFile(file);
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(cameraIntent, FRONT_CARD);*/
    }

    public void takeBackCardPic() {


        if(Build.VERSION.SDK_INT>=24)
        {
            File file = Commons.getOutputMediaFile();
            Uri uri = FileProvider.getUriForFile(getActivity(),   getActivity().getApplicationContext().getPackageName()+".fileprovider",file);
            outputFileUri= Uri.fromFile(file.getAbsoluteFile());
            String path=outputFileUri.getPath();
            String filepath=file.getAbsolutePath();
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(cameraIntent, BACK_CARD);

        }else {

            File file = Commons.getOutputMediaFile();
            outputFileUri = Uri.fromFile(file);
            Log.d("outputFileUri", String.valueOf(outputFileUri));
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            startActivityForResult(cameraIntent, BACK_CARD);

           /* outputFileUri=Uri.fromFile(file);
            outputFileUri = FileProvider.getUriForFile(StartDayActivity.this, BuildConfig.APPLICATION_ID + ".provider",file);
            outputFileUri = Uri.fromFile(file);*/

        }

        StrictMode.VmPolicy.Builder newbuilder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(newbuilder.build());
/*
        File file = Commons.getOutputMediaFile();
        outputFileUri = Uri.fromFile(file);
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(cameraIntent, BACK_CARD);*/
    }

    public void onClickImg(View v) {
        Logger.d(TAG, "inside onClick");
        if (v.getId() == R.id.add_front) {
            if(!Constants.CLOSED.equals(saleMetadataEntities.get(0).status)) {
                takeFrontCardPic();
            }else {
                Toast.makeText(getActivity(),R.string.cannot_edit_photo, Toast.LENGTH_LONG).show();
            }
        } else if(v.getId() == R.id.front_img){
            if(!(null==(saleMetadataEntities.get(0).filePath)) && saleMetadataEntities.get(0).tag.equals(Constants.PHOTO_REPORT_FRONT)){
                Intent i = new Intent(getContext(), ImageActivity.class);
                i.putExtra("path",saleMetadataEntities.get(0).filePath);
                startActivity(i);
            }
        } else if (v.getId() == R.id.add_back) {
            if(!Constants.CLOSED.equals(saleMetadataEntities.get(1).status)) {
                takeBackCardPic();
            }else {
                Toast.makeText(getActivity(),R.string.cannot_edit_photo, Toast.LENGTH_LONG).show();
            }
        } else if(v.getId() == R.id.back_img){
            if(!(null==(saleMetadataEntities.get(1).filePath)) && saleMetadataEntities.get(1).tag.equals(Constants.PHOTO_REPORT_BACK)){
                Intent i = new Intent(getActivity(), ImageActivity.class);
                i.putExtra("path",saleMetadataEntities.get(1).filePath);
                startActivity(i);
            }
        }
    }


}