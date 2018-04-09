package com.nebula.connect.queries;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;


import com.nebula.connect.Constants;
import com.nebula.connect.Settings;
import com.nebula.connect.db.DBAdapter;
import com.nebula.connect.entities.PlanningEntity;
import com.nebula.connect.entities.DealerEntity;
import com.nebula.connect.entities.MeetingEntity;
import com.nebula.connect.entities.PainterEntity;
import com.nebula.connect.entities.RoutePlanEntity;
import com.nebula.connect.entities.SaleMetadataEntity;
import com.nebula.connect.entities.StartDayEntity;
import com.nebula.connect.logreports.Logger;
import com.nebula.connect.tables.DEALER_TBL;
import com.nebula.connect.tables.MEETING_TBL;
import com.nebula.connect.tables.PAINTER_TBL;
import com.nebula.connect.tables.PLANNING_TBL;
import com.nebula.connect.tables.ROUTE_PLAN_TBL;
import com.nebula.connect.tables.SALE_METADATA_TBL;
import com.nebula.connect.tables.SETTINGS_TBL;
import com.nebula.connect.tables.STARTDAY_TBL;

import java.util.ArrayList;

/**
 * Created by siddhesh on 7/22/16.
 */
public class SelectQueries {

    private SelectQueries(){};

    private static final String TAG=SelectQueries.class.getSimpleName();

    public static synchronized String getSetting(Context context, Settings type) {
        DBAdapter adapter = DBAdapter.getInstance(context);
        adapter.open();
        Cursor cursor = adapter.getDB()
                .query(SETTINGS_TBL.TABLE_NAME,
                        new String[]{SETTINGS_TBL.STB_STNGS_VAL},
                        new StringBuffer().append(SETTINGS_TBL.STB_STNGS_NM)
                                .append(" = '").append(type).append("' ").toString(), null,
                        null, null, null);
        String retVal = null;

        if (cursor.getCount() == 0) {

            ContentValues initialValues = new ContentValues();
            initialValues.put(SETTINGS_TBL.STB_STNGS_NM, type.toString());
            initialValues.put(SETTINGS_TBL.STB_STNGS_VAL, "");
            adapter.getDB().insert(SETTINGS_TBL.TABLE_NAME, null, initialValues);

            cursor.close();
            cursor = adapter.getDB().query(
                    SETTINGS_TBL.TABLE_NAME,
                    new String[] { SETTINGS_TBL.STB_STNGS_VAL },
                    new StringBuffer().append(SETTINGS_TBL.STB_STNGS_NM).append(
                            " = '").append(type).append("' ").toString(), null, null, null,
                    null);
        }
        cursor.moveToFirst();
        retVal = cursor.getString(0);
        cursor.close();
        Logger.d(TAG, "retval=" + retVal+" cursor count="+cursor.getCount());
        //adapter.close();
        return retVal;
    }


    public static synchronized int getRoutePlanVid(Context context){

        DBAdapter adapter = DBAdapter.getInstance(context);
        adapter.open();
        Cursor cursor = adapter.getDB().query(ROUTE_PLAN_TBL.TABLE_NAME, new String[]{"MAX(" + ROUTE_PLAN_TBL.VID + ")"}, null, null, null, null, null);
        int vid = 0;
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            vid = cursor.getInt(0);
        }
        Logger.d(TAG, "vid=" + vid);
        return vid;
    }

    public static synchronized int getPlanningVid(Context context){

        DBAdapter adapter = DBAdapter.getInstance(context);
        adapter.open();
        Cursor cursor = adapter.getDB().query(PLANNING_TBL.TABLE_NAME, new String[]{"MAX(" + PLANNING_TBL.VID + ")"}, null, null, null, null, null);
        int vid = 0;
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            vid = cursor.getInt(0);
        }
        Logger.d(TAG, "vid=" + vid);
        return vid;
    }

    public static synchronized int getDealerVid(Context context){

        DBAdapter adapter = DBAdapter.getInstance(context);
        adapter.open();
        Cursor cursor = adapter.getDB().query(DEALER_TBL.TABLE_NAME, new String[]{"MAX(" + DEALER_TBL.VID + ")"}, null, null, null, null, null);
        int vid = 0;
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            vid = cursor.getInt(0);
        }
        Logger.d(TAG, "vid=" + vid);
        return vid;
    }

    public static synchronized ArrayList<RoutePlanEntity> getRoutePlanElements(Context context) {

        DBAdapter adapter = DBAdapter.getInstance(context);
        adapter.open();
        Cursor cursor = adapter.getDB()	.query(	ROUTE_PLAN_TBL.TABLE_NAME,new String[] {ROUTE_PLAN_TBL.R_ID,ROUTE_PLAN_TBL.VID
                        ,ROUTE_PLAN_TBL.UID,ROUTE_PLAN_TBL.DATE,ROUTE_PLAN_TBL.VILLAGEID_1,ROUTE_PLAN_TBL.VILLAGEID_2,
                        ROUTE_PLAN_TBL.VILLAGEID_3, ROUTE_PLAN_TBL.VILLAGEID_4,
                        ROUTE_PLAN_TBL.VILLAGEID_5,ROUTE_PLAN_TBL.VILLAGENAME_1,ROUTE_PLAN_TBL.VILLAGENAME_2,ROUTE_PLAN_TBL.VILLAGENAME_3,
                        ROUTE_PLAN_TBL.VILLAGENAME_4,ROUTE_PLAN_TBL.VILLAGENAME_5,
                        ROUTE_PLAN_TBL.CREATED,ROUTE_PLAN_TBL.CREATED_BY,ROUTE_PLAN_TBL.RP_FIELD1,ROUTE_PLAN_TBL.RP_FIELD2,
                        ROUTE_PLAN_TBL.RP_FIELD3,ROUTE_PLAN_TBL.RP_FIELD4,ROUTE_PLAN_TBL.RP_FIELD5,ROUTE_PLAN_TBL.RP_FIELD6,ROUTE_PLAN_TBL.RP_FIELD7
                        ,ROUTE_PLAN_TBL.RP_FIELD8,ROUTE_PLAN_TBL.RP_FIELD9,ROUTE_PLAN_TBL.RP_FIELD10},
                null, null,null, null, null);
        int count=cursor.getCount();
        ArrayList<RoutePlanEntity> entities=new ArrayList<RoutePlanEntity>();
        if(count > 0){
            cursor.moveToFirst();
            for(int i=0; i<count; i++){
                RoutePlanEntity entity=new RoutePlanEntity();
                entity.r_id=cursor.getInt(0);
                entity.vid=cursor.getInt(1);
                entity.uid=cursor.getInt(2);
                entity.date=cursor.getString(3);
                entity.villageid_1=cursor.getInt(4);
                entity.villageid_2=cursor.getInt(5);
                entity.villageid_3=cursor.getInt(6);
                entity.villageid_4=cursor.getInt(7);
                entity.villageid_5=cursor.getInt(8);
                entity.villagename_1=cursor.getString(9);
                entity.villagename_2=cursor.getString(10);
                entity.villagename_3=cursor.getString(11);
                entity.villagename_4=cursor.getString(12);
                entity.villagename_5=cursor.getString(13);
                entity.created=cursor.getInt(14);
                entity.created_by=cursor.getInt(15);
                entity.rp_field1=cursor.getString(16);
                entity.rp_field2=cursor.getString(17);
                entity.rp_field3=cursor.getString(18);
                entity.rp_field4=cursor.getString(19);
                entity.rp_field5=cursor.getString(10);
                entity.rp_field6=cursor.getString(21);
                entity.rp_field7=cursor.getString(22);
                entity.rp_field8=cursor.getString(23);
                entity.rp_field9=cursor.getString(24);
                entity.rp_field10=cursor.getString(25);
                entities.add(entity);
                cursor.moveToNext();
            }
        }
        cursor.close();
        Logger.d(TAG, "cursor count=" + cursor.getCount());
        //adapter.close();
        return entities;
    }


    public static synchronized MeetingEntity getMeetingByPlanningId(Context context, int planningId) {

        DBAdapter adapter = DBAdapter.getInstance(context);
        adapter.open();
        Cursor cursor = adapter.getDB()	.query(	MEETING_TBL.TABLE_NAME,new String[] {MEETING_TBL.PLANNING_ID,MEETING_TBL.VID
                        ,MEETING_TBL.HOTEL_NAME,MEETING_TBL.PAINTER_ATTENDANCE,MEETING_TBL.NON_PARTICIPANTS,MEETING_TBL.MEETING_DETAIL_START,
                        MEETING_TBL.TOTAL_GIFTS_GIVEN,MEETING_TBL.DETAIL_START_TIME,MEETING_TBL.STATUS,MEETING_TBL.MEETING_FIELD1,MEETING_TBL.MEETING_FIELD2,MEETING_TBL.MEETING_FIELD3
                        ,MEETING_TBL.MEETING_FIELD4,MEETING_TBL.MEETING_FIELD5},
                new StringBuffer().append(MEETING_TBL.PLANNING_ID).append(" = ").append(planningId).toString(), null,null, null, null);
        int count=cursor.getCount();
        MeetingEntity entity = new MeetingEntity();
        if(count > 0){
            cursor.moveToFirst();
            entity.planningId=cursor.getInt(0);
            entity.vId=cursor.getString(1);
            entity.hotelName=cursor.getString(2);
            entity.painterAttendance=cursor.getString(3);
            entity.nonParticipants=cursor.getString(4);
            entity.meetiingDetailsStart=cursor.getString(5);
            entity.totalGiftsGiven=cursor.getString(6);
            entity.detailStartTime=cursor.getString(7);
            entity.status=cursor.getString(8);
            entity.meeting_field1=cursor.getString(9);
            entity.meeting_field2=cursor.getString(10);
            entity.meeting_field3=cursor.getString(11);
            entity.meeting_field4=cursor.getString(12);
            entity.meeting_field5=cursor.getString(13);
            cursor.moveToNext();
        }
        cursor.close();
        //adapter.close();
        return entity;
    }

    public static synchronized ArrayList<DealerEntity> getDealerByMeetingId(Context context, int meetingId) {

        DBAdapter adapter = DBAdapter.getInstance(context);
        adapter.open();
        Cursor cursor = adapter.getDB()	.query(	DEALER_TBL.TABLE_NAME,new String[] {DEALER_TBL.DEALER_ID,DEALER_TBL.NAME,DEALER_TBL.MEETING_ID},
                new StringBuffer().append(DEALER_TBL.MEETING_ID).append(" = '").append(meetingId).append("'").toString(), null,null, null, null);
        int count=cursor.getCount();
        ArrayList<DealerEntity> dealerList=new ArrayList<DealerEntity>();
        if(count > 0){
            cursor.moveToFirst();
            for(int i=0; i<count; i++) {
                DealerEntity entity=new DealerEntity();
                entity.dealerId=cursor.getInt(0);
                entity.name = cursor.getString(1);
                entity.meetingId = cursor.getString(2);
                dealerList.add(entity);
                cursor.moveToNext();
            }
        }
        cursor.close();
        Logger.d(TAG, "cursor count=" + cursor.getCount());
        //adapter.close();
        return dealerList;
    }

    public static synchronized ArrayList<PainterEntity> getPainterByPlanningId(Context context, int planningId,int useWhere) {

        DBAdapter adapter = DBAdapter.getInstance(context);
        adapter.open();
        String whereClause="";
        if(useWhere == 1){
            whereClause =  new StringBuffer().append(PAINTER_TBL.PLANNING_ID).append(" = ").append(planningId) .append(" AND ").append(PAINTER_TBL.STATUS).append(" = '").append(Constants.INPROGRESS).append("'")
                    .toString();
        }else if(useWhere == 0){
            whereClause = new StringBuffer().append(PAINTER_TBL.PLANNING_ID).append(" = ").append(planningId).toString();
        }
        Cursor cursor = adapter.getDB()	.query(PAINTER_TBL.TABLE_NAME, new String[]{PAINTER_TBL.PAINTER_ID
                        , PAINTER_TBL.PLANNING_ID, PAINTER_TBL.VID,  PAINTER_TBL.PAINTER_NAME,PAINTER_TBL.VILLAGE_ID,PAINTER_TBL.DEALER_ID,
                        PAINTER_TBL.NPP_CODE, PAINTER_TBL.DEALER_NAME, PAINTER_TBL.CONTACT, PAINTER_TBL.DETAIL_START_TIME,
                        PAINTER_TBL.STATUS, PAINTER_TBL.BUSINESS_STARTED_YEAR, PAINTER_TBL.QUALIFICATION, PAINTER_TBL.TEAM_SIZE,
                        PAINTER_TBL.DEALER_CODE, PAINTER_TBL.DEALER_CONTACT, PAINTER_TBL.ORDER_BOOKING, PAINTER_TBL.REMARK1, PAINTER_TBL.REMARK2, PAINTER_TBL.PAINTER_FIELD1, PAINTER_TBL.PAINTER_FIELD2,PAINTER_TBL.PAINTER_FIELD3
                        , PAINTER_TBL.PAINTER_FIELD4,PAINTER_TBL.PAINTER_FIELD5},
             whereClause, null, null, null, null);
        int count=cursor.getCount();
        ArrayList<PainterEntity> entities=new ArrayList<PainterEntity>();
        if(count > 0){
            cursor.moveToFirst();
            for(int i=0; i<count; i++){
                PainterEntity entity=new PainterEntity();
                entity.painterId=cursor.getInt(0);
                entity.planningId=cursor.getString(1);
                entity.vId=cursor.getString(2);
                entity.painterName=cursor.getString(3);
                entity.villageId=cursor.getString(4);
                entity.dealerId=cursor.getString(5);
                entity.nppCode=cursor.getString(6);
                entity.dealerName=cursor.getString(7);
                entity.contact=cursor.getString(8);
                entity.detailStartTime=cursor.getString(9);
                entity.status=cursor.getString(10);
                entity.business_started_year=cursor.getString(11);
                entity.qualification=cursor.getString(12);
                entity.team_size=cursor.getString(13);
                entity.dealer_code=cursor.getString(14);
                entity.dealer_contact=cursor.getString(15);
                entity.order_booking=cursor.getString(16);
                entity.remark1=cursor.getString(17);
                entity.remark2=cursor.getString(18);
                entity.painter_field1=cursor.getString(19);
                entity.painter_field2=cursor.getString(20);
                entity.painter_field3=cursor.getString(21);
                entity.painter_field4=cursor.getString(22);
                entity.painter_field5=cursor.getString(23);
                entities.add(entity);
                cursor.moveToNext();
            }
        }
        cursor.close();
        Logger.d(TAG, "cursor count=" + cursor.getCount());
        //adapter.close();
        return entities;
    }

    public static synchronized ArrayList<PainterEntity> getPainterByFilter(Context context, int planningId, String filter) {

        DBAdapter adapter = DBAdapter.getInstance(context);
        adapter.open();
        Cursor cursor = adapter.getDB()	.query(PAINTER_TBL.TABLE_NAME, new String[]{PAINTER_TBL.PAINTER_ID
                        , PAINTER_TBL.PLANNING_ID, PAINTER_TBL.VID, PAINTER_TBL.VILLAGE_ID, PAINTER_TBL.PAINTER_NAME,
                        PAINTER_TBL.NPP_CODE, PAINTER_TBL.DEALER_NAME,PAINTER_TBL.DEALER_ID, PAINTER_TBL.CONTACT,PAINTER_TBL.DETAIL_START_TIME,
                        PAINTER_TBL.STATUS,PAINTER_TBL.PAINTER_FIELD1, PAINTER_TBL.PAINTER_FIELD2,PAINTER_TBL.PAINTER_FIELD3
                        , PAINTER_TBL.PAINTER_FIELD4,PAINTER_TBL.PAINTER_FIELD5},
                new StringBuffer().append(PAINTER_TBL.PLANNING_ID).append(" = ").append(planningId).append(" AND ")
                        .append(PAINTER_TBL.PAINTER_NAME).append(" LIKE ?").toString(),
                new String[] {"%"+ filter+ "%" }, null, null, null, null);
        int count=cursor.getCount();
        ArrayList<PainterEntity> entities=new ArrayList<PainterEntity>();
        if(count > 0){
            cursor.moveToFirst();
            for(int i=0; i<count; i++){
                PainterEntity entity=new PainterEntity();
                entity.painterId=cursor.getInt(0);
                entity.planningId=cursor.getString(1);
                entity.vId=cursor.getString(2);
                entity.villageId=cursor.getString(3);
                entity.painterName=cursor.getString(4);
                entity.nppCode=cursor.getString(5);
                entity.dealerName=cursor.getString(6);
                entity.dealerId=cursor.getString(7);
                entity.contact=cursor.getString(8);
                entity.detailStartTime=cursor.getString(9);
                entity.status=cursor.getString(10);
                entity.painter_field1=cursor.getString(11);
                entity.painter_field2=cursor.getString(12);
                entity.painter_field3=cursor.getString(13);
                entity.painter_field4=cursor.getString(14);
                entity.painter_field5=cursor.getString(15);
                entities.add(entity);
                cursor.moveToNext();
            }
        }
        cursor.close();
        Logger.d(TAG, "cursor count=" + cursor.getCount());
        //adapter.close();
        return entities;
    }


    public static synchronized PlanningEntity getPlanningById(Context context, int meetingId) {

        DBAdapter adapter = DBAdapter.getInstance(context);
        adapter.open();
        Cursor cursor = adapter.getDB()	.query(	PLANNING_TBL.TABLE_NAME,new String[] {PLANNING_TBL.PLANNING_ID,PLANNING_TBL.VID
                        ,PLANNING_TBL.DIVISION,PLANNING_TBL.DEPO,PLANNING_TBL.DEPO_CODE,PLANNING_TBL.MEETING_TYPE
                        ,PLANNING_TBL.MEETING_CODE,PLANNING_TBL.DATE,PLANNING_TBL.TRAINING_CENTER_NAME,PLANNING_TBL.START_TIME
                        ,PLANNING_TBL.STATE_ID,PLANNING_TBL.VILLAGE_ID,PLANNING_TBL.LOCATION,PLANNING_TBL.ESTIMATE_ATTENDANCE
                        ,PLANNING_TBL.NEROLAC_TSI,PLANNING_TBL.NEROLAC_TSI_CONTACT,PLANNING_TBL.BUDGET_PER_MEET,PLANNING_TBL.ASM_NAME
                        ,PLANNING_TBL.ASM_CONTACT,PLANNING_TBL.DSM_NAME,PLANNING_TBL.DSM_CONTACT,PLANNING_TBL.MONTH,PLANNING_TBL.STATUS,PLANNING_TBL.PLANNING_FIELD1},
                new StringBuffer().append(PLANNING_TBL.PLANNING_ID).append(" = ").append(meetingId).toString(), null,null, null, null);
        int count=cursor.getCount();
        PlanningEntity entity = new PlanningEntity();
        if(count > 0){
            cursor.moveToFirst();
            entity.planningId=cursor.getInt(0);
            entity.vId=cursor.getString(1);
            entity.division=cursor.getString(2);
            entity.depo=cursor.getString(3);
            entity.depoCode=cursor.getString(4);
            entity.meetingType=cursor.getString(5);
            entity.meetingCode=cursor.getString(6);
            entity.date=cursor.getString(7);
            entity.trainingCenterName=cursor.getString(8);
            entity.startTime=cursor.getString(9);
            entity.statedId=cursor.getString(10);
            entity.villageId=cursor.getString(11);
            entity.location=cursor.getString(12);
            entity.estimateAttendance=cursor.getString(13);
            entity.nerolacTsi=cursor.getString(14);
            entity.nerolacTsiContact=cursor.getString(15);
            entity.budgetPerMeet=cursor.getString(16);
            entity.asmName=cursor.getString(17);
            entity.asmContact=cursor.getString(18);
            entity.dsmName=cursor.getString(19);
            entity.dsmContact=cursor.getString(20);
            entity.month=cursor.getString(21);
            entity.status=cursor.getString(22);
            entity.remarks = cursor.getString(23);
            cursor.moveToNext();
        }
        cursor.close();
        //adapter.close();
        return entity;
    }


    public static synchronized boolean isSaleMetaExist(Context context, SaleMetadataEntity entity){
        DBAdapter adapter = DBAdapter.getInstance(context);
        adapter.open();
        Cursor cursor = adapter.getDB()	.query(SALE_METADATA_TBL.TABLE_NAME, new String[]{SALE_METADATA_TBL.SALE_M_ID
                        , SALE_METADATA_TBL.TRANSACTION_ID, SALE_METADATA_TBL.MEETING_ID, SALE_METADATA_TBL.FID, SALE_METADATA_TBL.CREATED,
                        SALE_METADATA_TBL.STATUS, SALE_METADATA_TBL.FILE_PATH,SALE_METADATA_TBL.TAG,
                        SALE_METADATA_TBL.LATITUDE, SALE_METADATA_TBL.LONGITUDE,SALE_METADATA_TBL.ACCURACY},
                new StringBuffer().append(SALE_METADATA_TBL.FILE_PATH).append(" = '").append(entity.filePath)
                        .append("'").toString()
                , null, null, null, null);
        int count=cursor.getCount();
        boolean isExist = false;
        if(count > 0) {
            isExist = true;
        }
        cursor.close();
        //adapter.close();
        return isExist;
    }


    public static synchronized ArrayList<SaleMetadataEntity> getSaleMetadataElements(Context context,int meetingId,int useWhere) {

        DBAdapter adapter = DBAdapter.getInstance(context);
        adapter.open();
        String whereClause="";
        if(useWhere == 1){
            whereClause =  new StringBuffer().append(SALE_METADATA_TBL.MEETING_ID).append(" = ").append(meetingId)
                    .append(" AND ").append(SALE_METADATA_TBL.TAG).append(" != '").append(Constants.PHOTO_START).append("'")
                   /* .append(" AND ").append(SALE_METADATA_TBL.STATUS).append(" = '").append(Constants.INPROGRESS).append("'")*/.toString();
        }else if(useWhere == 0){
            whereClause = new StringBuffer().append(SALE_METADATA_TBL.MEETING_ID).append(" = ").append(meetingId)
            .append(" AND ").append(SALE_METADATA_TBL.STATUS).append(" = '").append(Constants.INPROGRESS).append("'").toString();
        }else if(useWhere == 2){
          whereClause = null;
        }
        Cursor cursor = adapter.getDB()	.query(SALE_METADATA_TBL.TABLE_NAME, new String[]{SALE_METADATA_TBL.SALE_M_ID
                        , SALE_METADATA_TBL.TRANSACTION_ID, SALE_METADATA_TBL.MEETING_ID, SALE_METADATA_TBL.FID, SALE_METADATA_TBL.CREATED,
                SALE_METADATA_TBL.STATUS, SALE_METADATA_TBL.FILE_PATH,SALE_METADATA_TBL.TAG,
                        SALE_METADATA_TBL.LATITUDE, SALE_METADATA_TBL.LONGITUDE,SALE_METADATA_TBL.ACCURACY},
                whereClause, null, null, null, null);
        int count=cursor.getCount();
        ArrayList<SaleMetadataEntity> entities=new ArrayList<SaleMetadataEntity>();
        if(count > 0){
            cursor.moveToFirst();
            for(int i=0; i<count; i++){
                SaleMetadataEntity entity=new SaleMetadataEntity();
                entity.m_id=cursor.getInt(0);
                entity.transaction_id=cursor.getInt(1);
                entity.meeting_id=cursor.getInt(2);
                entity.fid=cursor.getInt(3);
                entity.created=cursor.getInt(4);
                entity.status=cursor.getString(5);
                entity.filePath=cursor.getString(6);
                entity.tag=cursor.getString(7);
                entity.latitude=cursor.getString(8);
                entity.longitude=cursor.getString(9);
                entity.accuracy=cursor.getString(10);
                entities.add(entity);
                cursor.moveToNext();
            }
        }
        cursor.close();
        Logger.d(TAG, "cursor count=" + cursor.getCount());
        //adapter.close();
        return entities;
    }

    public static synchronized ArrayList<SaleMetadataEntity> getSaleMetadataElementsByTag(Context context,int meetingId, String tag) {

        DBAdapter adapter = DBAdapter.getInstance(context);
        adapter.open();
        String whereClause = "";
        if(tag.equals(Constants.PHOTO_REPORT)){
           whereClause = new StringBuffer().append(SALE_METADATA_TBL.MEETING_ID).append(" = ").append(meetingId).append(" AND (")
                    .append(SALE_METADATA_TBL.TAG).append(" = '").append(Constants.PHOTO_REPORT_FRONT).append("'")
                    .append(" OR ").append(SALE_METADATA_TBL.TAG).append(" = '").append(Constants.PHOTO_REPORT_BACK).append("')").toString();
        }else {
            whereClause = new StringBuffer().append(SALE_METADATA_TBL.MEETING_ID).append(" = ").append(meetingId).append(" AND ")
                    .append(SALE_METADATA_TBL.TAG).append(" = '").append(tag).append("'").toString();

        }
        Cursor cursor = adapter.getDB()	.query(SALE_METADATA_TBL.TABLE_NAME, new String[]{SALE_METADATA_TBL.SALE_M_ID
                        , SALE_METADATA_TBL.TRANSACTION_ID, SALE_METADATA_TBL.MEETING_ID, SALE_METADATA_TBL.FID, SALE_METADATA_TBL.CREATED,
                SALE_METADATA_TBL.STATUS, SALE_METADATA_TBL.FILE_PATH,SALE_METADATA_TBL.TAG,
                        SALE_METADATA_TBL.LATITUDE, SALE_METADATA_TBL.LONGITUDE,SALE_METADATA_TBL.ACCURACY},
                whereClause, null, null, null, null);
        int count=cursor.getCount();
        ArrayList<SaleMetadataEntity> entities=new ArrayList<SaleMetadataEntity>();
        if(count > 0){
            cursor.moveToFirst();
            for(int i=0; i<count; i++){
                SaleMetadataEntity entity=new SaleMetadataEntity();
                entity.m_id=cursor.getInt(0);
                entity.transaction_id=cursor.getInt(1);
                entity.meeting_id=cursor.getInt(2);
                entity.fid=cursor.getInt(3);
                entity.created=cursor.getInt(4);
                entity.status=cursor.getString(5);
                entity.filePath=cursor.getString(6);
                entity.tag=cursor.getString(7);
                entity.latitude=cursor.getString(8);
                entity.longitude=cursor.getString(9);
                entity.accuracy=cursor.getString(10);
                entities.add(entity);
                cursor.moveToNext();
            }
        }
        cursor.close();
        Logger.d(TAG, "cursor count=" + cursor.getCount());
        //adapter.close();
        return entities;
    }

    public static synchronized int getSaleMetaCountByTag(Context context,int meetingId,String tag) {

        DBAdapter adapter = DBAdapter.getInstance(context);
        adapter.open();
        Cursor cursor = adapter.getDB()	.query(SALE_METADATA_TBL.TABLE_NAME, new String[]{SALE_METADATA_TBL.SALE_M_ID
                        , SALE_METADATA_TBL.TRANSACTION_ID, SALE_METADATA_TBL.MEETING_ID, SALE_METADATA_TBL.FID, SALE_METADATA_TBL.CREATED,
                        SALE_METADATA_TBL.STATUS, SALE_METADATA_TBL.FILE_PATH,SALE_METADATA_TBL.TAG},
                new StringBuffer().append(SALE_METADATA_TBL.MEETING_ID).append(" = ").append(meetingId)
                        .append(" AND ").append(SALE_METADATA_TBL.TAG).append(" = '").append(tag).append("'").toString(), null, null, null, null);
        int count=cursor.getCount();

        cursor.close();
        Logger.d(TAG, "cursor count=" + cursor.getCount());
        //adapter.close();
        return count;
    }

    public static synchronized StartDayEntity getStartDayElements(Context context) {

        DBAdapter adapter = DBAdapter.getInstance(context);
        adapter.open();
        Cursor cursor = adapter.getDB()	.query(STARTDAY_TBL.TABLE_NAME, new String[]{STARTDAY_TBL.START_ID
                        , STARTDAY_TBL.IMAGE_PATH, STARTDAY_TBL.START_DATE, STARTDAY_TBL.REMARKS
                        , STARTDAY_TBL.LATITUDE, STARTDAY_TBL.LONGITUDE, STARTDAY_TBL.ACCURACY, STARTDAY_TBL.STATUS,STARTDAY_TBL.MOBILE},
                null, null, null, null, null);
        int count=cursor.getCount();
        StartDayEntity entity = null;
        if(count > 0){
            cursor.moveToLast();
            for(int i=0; i<count; i++){
                entity=new StartDayEntity();
                entity.startId=cursor.getInt(0);
                entity.imagePath=cursor.getString(1);
                entity.startDate=cursor.getInt(2);
                entity.remarks=cursor.getString(3);
                entity.latitude=cursor.getString(4);
                entity.longitude=cursor.getString(5);
                entity.accuracy=cursor.getString(6);
                entity.status=cursor.getString(7);
                entity.mobile=cursor.getString(8);
            }
        }
        cursor.close();
        Logger.d(TAG, "cursor count=" + cursor.getCount());
        //adapter.close();
        return entity;
    }

    public static synchronized StartDayEntity getStartDayElementsByStartId(Context context,int stardId) {

        DBAdapter adapter = DBAdapter.getInstance(context);
        adapter.open();
        Cursor cursor = adapter.getDB()	.query(STARTDAY_TBL.TABLE_NAME, new String[]{STARTDAY_TBL.START_ID
                        , STARTDAY_TBL.IMAGE_PATH, STARTDAY_TBL.START_DATE, STARTDAY_TBL.REMARKS
                        , STARTDAY_TBL.LATITUDE, STARTDAY_TBL.LONGITUDE, STARTDAY_TBL.ACCURACY, STARTDAY_TBL.STATUS,STARTDAY_TBL.MOBILE},
                new StringBuffer().append(STARTDAY_TBL.START_ID).append(" = ").append(stardId).toString(), null, null, null, null);
        int count=cursor.getCount();
        StartDayEntity entity = new StartDayEntity();
        if(count > 0){
            cursor.moveToLast();
            for(int i=0; i<count; i++){
                entity.startId=cursor.getInt(0);
                entity.imagePath=cursor.getString(1);
                entity.startDate=cursor.getInt(2);
                entity.remarks=cursor.getString(3);
                entity.latitude=cursor.getString(4);
                entity.longitude=cursor.getString(5);
                entity.accuracy=cursor.getString(6);
                entity.status=cursor.getString(7);
                entity.mobile=cursor.getString(8);
            }
        }
        cursor.close();
        Logger.d(TAG, "cursor count=" + cursor.getCount());
        //adapter.close();
        return entity;
    }

    public static synchronized ArrayList<PainterEntity> getPainterByStatus(Context context) {

        DBAdapter adapter = DBAdapter.getInstance(context);
        adapter.open();
        Cursor cursor = adapter.getDB()	.query(PAINTER_TBL.TABLE_NAME, new String[]{PAINTER_TBL.PAINTER_ID
                        , PAINTER_TBL.PLANNING_ID, PAINTER_TBL.VID, PAINTER_TBL.VILLAGE_ID, PAINTER_TBL.PAINTER_NAME,
                        PAINTER_TBL.NPP_CODE, PAINTER_TBL.DEALER_NAME,PAINTER_TBL.DEALER_ID, PAINTER_TBL.CONTACT,PAINTER_TBL.DETAIL_START_TIME,
                        PAINTER_TBL.STATUS,PAINTER_TBL.PAINTER_FIELD1, PAINTER_TBL.PAINTER_FIELD2,PAINTER_TBL.PAINTER_FIELD3
                        , PAINTER_TBL.PAINTER_FIELD4,PAINTER_TBL.PAINTER_FIELD5},
                new StringBuffer().append(PAINTER_TBL.STATUS).append(" = '")
                        .append(Constants.INPROGRESS).append("'").toString(), null, null, null, null);
        int count=cursor.getCount();
        ArrayList<PainterEntity> entities=new ArrayList<PainterEntity>();
        if(count > 0){
            cursor.moveToFirst();
            for(int i=0; i<count; i++){
                PainterEntity entity=new PainterEntity();
                entity.painterId=cursor.getInt(0);
                entity.planningId=cursor.getString(1);
                entity.vId=cursor.getString(2);
                entity.villageId=cursor.getString(3);
                entity.painterName=cursor.getString(4);
                entity.nppCode=cursor.getString(5);
                entity.dealerName=cursor.getString(6);
                entity.dealerId=cursor.getString(7);
                entity.contact=cursor.getString(8);
                entity.detailStartTime=cursor.getString(9);
                entity.status=cursor.getString(10);
                entity.painter_field1=cursor.getString(11);
                entity.painter_field2=cursor.getString(12);
                entity.painter_field3=cursor.getString(13);
                entity.painter_field4=cursor.getString(14);
                entity.painter_field5=cursor.getString(15);
                entities.add(entity);
                cursor.moveToNext();
            }
        }
        cursor.close();
        Logger.d(TAG, "cursor count=" + cursor.getCount());
        //adapter.close();
        return entities;
    }

    public static synchronized ArrayList<String> getPainterMobileNo(Context context,int meetId) {

        DBAdapter adapter = DBAdapter.getInstance(context);
        adapter.open();
        Cursor cursor = adapter.getDB()	.query(PAINTER_TBL.TABLE_NAME, new String[]{ PAINTER_TBL.CONTACT},
                new StringBuffer().append(PAINTER_TBL.PLANNING_ID).append(" = ").append(meetId).toString(), null, null, null, null);
        int count=cursor.getCount();
        ArrayList<String> entities=new ArrayList<String>();
        if(count > 0){
            cursor.moveToFirst();
            for(int i=0; i<count; i++){
                entities.add(cursor.getString(0));
                cursor.moveToNext();
            }
        }
        cursor.close();
        Logger.d(TAG, "cursor count=" + cursor.getCount());
        //adapter.close();
        return entities;
    }

    public static synchronized ArrayList<PlanningEntity> getPlanningByStatus(Context context) {

        DBAdapter adapter = DBAdapter.getInstance(context);
        adapter.open();
        Cursor cursor = adapter.getDB()	.query(	PLANNING_TBL.TABLE_NAME,new String[] {PLANNING_TBL.PLANNING_ID,PLANNING_TBL.VID
                        ,PLANNING_TBL.DIVISION,PLANNING_TBL.DEPO,PLANNING_TBL.DEPO_CODE,PLANNING_TBL.MEETING_TYPE
                        ,PLANNING_TBL.MEETING_CODE,PLANNING_TBL.DATE,PLANNING_TBL.TRAINING_CENTER_NAME,PLANNING_TBL.START_TIME
                        ,PLANNING_TBL.STATE_ID,PLANNING_TBL.VILLAGE_ID,PLANNING_TBL.LOCATION,PLANNING_TBL.ESTIMATE_ATTENDANCE
                        ,PLANNING_TBL.NEROLAC_TSI,PLANNING_TBL.NEROLAC_TSI_CONTACT,PLANNING_TBL.BUDGET_PER_MEET,PLANNING_TBL.ASM_NAME
                        ,PLANNING_TBL.ASM_CONTACT,PLANNING_TBL.DSM_NAME,PLANNING_TBL.DSM_CONTACT,PLANNING_TBL.MONTH,PLANNING_TBL.STATUS},
                new StringBuffer().append(PLANNING_TBL.STATUS).append(" = '").append(Constants.INPROGRESS).append("'").toString(), null,null, null, null);
        int count=cursor.getCount();

        ArrayList<PlanningEntity> entities=new ArrayList<PlanningEntity>();
        if(count > 0){
            cursor.moveToFirst();
            for(int i=0; i<count; i++){
                PlanningEntity entity = new PlanningEntity();
                entity.planningId=cursor.getInt(0);
                entity.vId=cursor.getString(1);
                entity.division=cursor.getString(2);
                entity.depo=cursor.getString(3);
                entity.depoCode=cursor.getString(4);
                entity.meetingType=cursor.getString(5);
                entity.meetingCode=cursor.getString(6);
                entity.date=cursor.getString(7);
                entity.trainingCenterName=cursor.getString(8);
                entity.startTime=cursor.getString(9);
                entity.statedId=cursor.getString(10);
                entity.villageId=cursor.getString(11);
                entity.location=cursor.getString(12);
                entity.estimateAttendance=cursor.getString(13);
                entity.nerolacTsi=cursor.getString(14);
                entity.nerolacTsiContact=cursor.getString(15);
                entity.budgetPerMeet=cursor.getString(16);
                entity.asmName=cursor.getString(17);
                entity.asmContact=cursor.getString(18);
                entity.dsmName=cursor.getString(19);
                entity.dsmContact=cursor.getString(20);
                entity.month=cursor.getString(21);
                entity.status=cursor.getString(22);
                entities.add(entity);
                cursor.moveToNext();
            }
        }
        cursor.close();
        Logger.d(TAG, "cursor count=" + cursor.getCount());
        //adapter.close();
        return entities;
    }


    public static synchronized ArrayList<MeetingEntity> getMeetingByStatus(Context context) {

        DBAdapter adapter = DBAdapter.getInstance(context);
        adapter.open();
        Cursor cursor = adapter.getDB()	.query(	MEETING_TBL.TABLE_NAME,new String[] {MEETING_TBL.PLANNING_ID,MEETING_TBL.VID
                        ,MEETING_TBL.HOTEL_NAME,MEETING_TBL.PAINTER_ATTENDANCE,MEETING_TBL.NON_PARTICIPANTS,MEETING_TBL.MEETING_DETAIL_START,
                        MEETING_TBL.TOTAL_GIFTS_GIVEN,MEETING_TBL.DETAIL_START_TIME,MEETING_TBL.STATUS,MEETING_TBL.MEETING_FIELD1,MEETING_TBL.MEETING_FIELD2,MEETING_TBL.MEETING_FIELD3
                        ,MEETING_TBL.MEETING_FIELD4,MEETING_TBL.MEETING_FIELD5},
                new StringBuffer().append(MEETING_TBL.STATUS).append(" = '").append(Constants.INPROGRESS).append("'").toString(), null, null, null, null);
        int count=cursor.getCount();
        ArrayList<MeetingEntity> entities=new ArrayList<MeetingEntity>();
        if(count > 0){
            cursor.moveToFirst();
            MeetingEntity entity=new MeetingEntity();
            entity.planningId=cursor.getInt(0);
            entity.vId=cursor.getString(1);
            entity.hotelName=cursor.getString(2);
            entity.painterAttendance=cursor.getString(3);
            entity.nonParticipants=cursor.getString(4);
            entity.meetiingDetailsStart=cursor.getString(5);
            entity.totalGiftsGiven=cursor.getString(6);
            entity.detailStartTime=cursor.getString(7);
            entity.status=cursor.getString(8);
            entity.meeting_field1=cursor.getString(9);
            entity.meeting_field2=cursor.getString(10);
            entity.meeting_field3=cursor.getString(11);
            entity.meeting_field4=cursor.getString(12);
            entity.meeting_field5=cursor.getString(13);
            entities.add(entity);
            cursor.moveToNext();
        }
        cursor.close();
        //adapter.close();
        return entities;
    }

    public static synchronized ArrayList<StartDayEntity> getStartDayElementsByStatus(Context context) {

        DBAdapter adapter = DBAdapter.getInstance(context);
        adapter.open();
        Cursor cursor = adapter.getDB()	.query(STARTDAY_TBL.TABLE_NAME, new String[]{STARTDAY_TBL.START_ID
                        , STARTDAY_TBL.IMAGE_PATH, STARTDAY_TBL.START_DATE, STARTDAY_TBL.REMARKS
                        , STARTDAY_TBL.LATITUDE, STARTDAY_TBL.LONGITUDE, STARTDAY_TBL.ACCURACY, STARTDAY_TBL.STATUS,STARTDAY_TBL.MOBILE},
                new StringBuffer().append(STARTDAY_TBL.STATUS).append(" = '").append(Constants.INPROGRESS).append("'").toString(), null, null, null, null);
        int count=cursor.getCount();
        ArrayList<StartDayEntity> entities = new ArrayList<>();
        if(count > 0){
            cursor.moveToFirst();
            for(int i=0; i<count; i++){
                StartDayEntity entity=new StartDayEntity();
                entity.startId=cursor.getInt(0);
                entity.imagePath=cursor.getString(1);
                entity.startDate=cursor.getInt(2);
                entity.remarks=cursor.getString(3);
                entity.latitude=cursor.getString(4);
                entity.longitude=cursor.getString(5);
                entity.accuracy=cursor.getString(6);
                entity.status=cursor.getString(7);
                entity.mobile=cursor.getString(8);
                entities.add(entity);
                cursor.moveToNext();
            }
        }
        cursor.close();
        Logger.d(TAG, "cursor count=" + cursor.getCount());
        //adapter.close();
        return entities;
    }

    public static synchronized ArrayList<SaleMetadataEntity> getSaleMetadataByStatus(Context context) {

        DBAdapter adapter = DBAdapter.getInstance(context);
        adapter.open();
        Cursor cursor = adapter.getDB()	.query(SALE_METADATA_TBL.TABLE_NAME, new String[]{SALE_METADATA_TBL.SALE_M_ID
                        , SALE_METADATA_TBL.TRANSACTION_ID, SALE_METADATA_TBL.MEETING_ID, SALE_METADATA_TBL.FID, SALE_METADATA_TBL.CREATED,
                        SALE_METADATA_TBL.STATUS, SALE_METADATA_TBL.FILE_PATH,SALE_METADATA_TBL.TAG,
                        SALE_METADATA_TBL.LATITUDE, SALE_METADATA_TBL.LONGITUDE,SALE_METADATA_TBL.ACCURACY },
                new StringBuffer().append(SALE_METADATA_TBL.STATUS).append(" = '").append(Constants.INPROGRESS).append("'").toString(), null, null, null, null);
        int count=cursor.getCount();
        ArrayList<SaleMetadataEntity> entities=new ArrayList<SaleMetadataEntity>();
        if(count > 0){
            cursor.moveToFirst();
            for(int i=0; i<count; i++){
                SaleMetadataEntity entity=new SaleMetadataEntity();
                entity.m_id=cursor.getInt(0);
                entity.transaction_id=cursor.getInt(1);
                entity.meeting_id=cursor.getInt(2);
                entity.fid=cursor.getInt(3);
                entity.created=cursor.getInt(4);
                entity.status=cursor.getString(5);
                entity.filePath=cursor.getString(6);
                entity.tag=cursor.getString(7);
                entity.latitude=cursor.getString(8);
                entity.longitude=cursor.getString(9);
                entity.accuracy=cursor.getString(10);
                entities.add(entity);
                cursor.moveToNext();
            }
        }
        cursor.close();
        Logger.d(TAG, "cursor count=" + cursor.getCount());
        //adapter.close();
        return entities;
    }


}
