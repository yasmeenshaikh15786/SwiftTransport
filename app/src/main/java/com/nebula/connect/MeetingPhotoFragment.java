package com.nebula.connect;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nebula.connect.entities.SaleMetadataEntity;
import com.nebula.connect.queries.SelectQueries;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Sonam on 24/3/17.
 */
public class MeetingPhotoFragment  extends Fragment {
    private ListView list;
    private Uri outputFileUri = null;
    private ArrayList<SaleMetadataEntity> saleMetadataEntities = new ArrayList<SaleMetadataEntity>();
    private CustomList adapter;
    private static final int MY_CAMERA_PERMISSION = 1;
    private static final int PHOTO_SAVED = 2;
    private int currentEntityPosition = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_meeting_photo, container, false);
        adapter = new CustomList();

        // Add dummy entries... TODO:After get from database
        saleMetadataEntities = SelectQueries.getSaleMetadataElementsByTag(getActivity(),getActivity().getIntent().getIntExtra("plan_id",0),Constants.PHOTO_MEETING);

        list = (ListView)rootView.findViewById(R.id.list_view);
        //Logger.d(TAG,"setting adapter");
        list.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if(resultCode == Activity.RESULT_OK && requestCode == PHOTO_SAVED) {
                String imgPath = "", tag = "";
                imgPath = outputFileUri.getPath();
                //Logger.d(TAG, "imgPath=" + imgPath);
                Commons.decodeFile(imgPath, Constants.IMG_WIDTH, Constants.IMG_HEIGHT);
                if(currentEntityPosition == -1) {
                    SaleMetadataEntity entity = new SaleMetadataEntity();
                    entity.filePath = imgPath;
                    entity.created = (int) (System.currentTimeMillis() / 1000L);
                    entity.tag = Constants.PHOTO_MEETING;
                    entity.status = Constants.INPROGRESS;
                    entity.meeting_id = getActivity().getIntent().getIntExtra("plan_id",0);
                    saleMetadataEntities.add(entity);
                }else {
                    saleMetadataEntities.get(currentEntityPosition).filePath = imgPath;
                    saleMetadataEntities.get(currentEntityPosition).created = (int) (System.currentTimeMillis() / 1000L);
                    saleMetadataEntities.get(currentEntityPosition).tag = Constants.PHOTO_MEETING;
                }
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<SaleMetadataEntity> getSaleMetadataEntities(){
        return saleMetadataEntities;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            // finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void addShop(View v){
        //Logger.d(TAG,"inside addShop");
        if(v.getId()==R.id.addBtn){
           String maxPhotoLimit = SelectQueries.getSetting(getContext(),Settings.MAX_MEETING_COUNT);
           int max = Integer.parseInt(maxPhotoLimit);

            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                if(saleMetadataEntities.size() < max){
                    imageEdit(-1);
                }else if(saleMetadataEntities.size()==max){
                    Toast.makeText(getContext(),  "You cannot add more than "+ maxPhotoLimit +" photos", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void capturePhoto(){

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
            startActivityForResult(cameraIntent, PHOTO_SAVED);

        }else {

            File file = Commons.getOutputMediaFile();
            outputFileUri = Uri.fromFile(file);
            Log.d("outputFileUri", String.valueOf(outputFileUri));
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            startActivityForResult(cameraIntent, PHOTO_SAVED);

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
        startActivityForResult(cameraIntent, PHOTO_SAVED);*/
    }


   private void imageClick(int position){
        if(saleMetadataEntities.get(position).filePath != null){
            // display image
            Intent i = new Intent(getActivity(), ImageActivity.class);
            i.putExtra("path",saleMetadataEntities.get(position).filePath);
            startActivity(i);
        }
    }

    private void imageEdit(int position){
        currentEntityPosition = position;
        capturePhoto();

    }

    private void imageDelete(int position){
        saleMetadataEntities.remove(position);
        adapter.notifyDataSetChanged();
    }

    public class CustomList extends BaseAdapter {

        @Override
        public int getCount() {
            return saleMetadataEntities.size();
        }

        @Override
        public Object getItem(int position) {
            return saleMetadataEntities.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder{
            TextView status;
            ImageView image;
            ImageView edit;
            ImageView delete;

        }
        @Override
        public View getView(final int pos, View convertView, ViewGroup arg2) {
            //Logger.d(TAG, "inside getView");


            ViewHolder holder = null;

            if(convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_row, null);

                holder=new ViewHolder();
                holder.status = (TextView) convertView.findViewById(R.id.status);
                holder.image = (ImageView) convertView.findViewById(R.id.image);
                holder.edit = (ImageView) convertView.findViewById(R.id.edit);
                holder.delete = (ImageView) convertView.findViewById(R.id.delete);
                convertView.setTag(holder);

            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            final SaleMetadataEntity entity = saleMetadataEntities.get(pos);


            if(Constants.INPROGRESS.equals(entity.status)){
                holder.status.setText("PENDING");
                holder.status.setTextColor(getResources().getColor(R.color.status_inprogress));
            }else if(Constants.CLOSED.equals(entity.status)){
                holder.status.setText("UPLOADED");
                holder.status.setTextColor(getResources().getColor(R.color.status_closed));
            }

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imageClick(pos);
                }
            });

            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!Constants.CLOSED.equals(entity.status)) {
                        imageEdit(pos);
                    }else {
                        Toast.makeText(getActivity(),R.string.cannot_edit_photo, Toast.LENGTH_LONG).show();
                    }
                }
            });

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!Constants.CLOSED.equals(entity.status)) {
                        new android.app.AlertDialog.Builder(getActivity())
                                .setMessage(R.string.delete_photo)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub
                                        imageDelete(pos);
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
                        Toast.makeText(getActivity(),R.string.cannot_delete_photo, Toast.LENGTH_LONG).show();
                    }
                }
            });

            if(entity.filePath != null){
                Bitmap b = BitmapFactory.decodeFile(entity.filePath);
                holder.image.setImageBitmap(Bitmap.createScaledBitmap(b, 150, 150, false));
            }else {
                Bitmap icon = BitmapFactory.decodeResource(getResources(),R.mipmap.empty_image);
                holder.image.setImageBitmap(icon);
            }

            return convertView;
        }
    }
}