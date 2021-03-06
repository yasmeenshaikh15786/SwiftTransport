package com.nebula.connect.queries;

import android.content.ContentValues;
import android.content.Context;

import com.nebula.connect.Constants;
import com.nebula.connect.Settings;
import com.nebula.connect.db.DBAdapter;
import com.nebula.connect.entities.DealerEntity;
import com.nebula.connect.entities.MeetingEntity;
import com.nebula.connect.entities.PainterEntity;
import com.nebula.connect.entities.PlanningEntity;
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
import com.nebula.connect.tables.STARTDAY_TBL;

/**
 * Created by siddhesh on 7/22/16.
 */
public class InsertQueries {

    private InsertQueries(){
        super();
    }

    private static String TAG = InsertQueries.class.getSimpleName();
    public synchronized static long setSetting(Context context, Settings name, String value){
        DBAdapter adapter = DBAdapter.getInstance(context);
        adapter.open();
        long retVal = -1;
        //adapter.close();

        SelectQueries.getSetting(context, name);
        retVal = UpdateQueries.updateSetting(context, name, value);
        Logger.d(TAG, "retval=" + retVal);
        return retVal;
    }

    public static synchronized long insertRoutePlan(Context context, RoutePlanEntity entity){

        DBAdapter adapter = DBAdapter.getInstance(context);
        adapter.open();
        long retVal =-1;
        try {
            ContentValues initialValues = new ContentValues();
            initialValues.put(ROUTE_PLAN_TBL.R_ID, entity.r_id);
            initialValues.put(ROUTE_PLAN_TBL.VID, entity.vid);
            initialValues.put(ROUTE_PLAN_TBL.UID, entity.uid);
            initialValues.put(ROUTE_PLAN_TBL.DATE, entity.date);
            initialValues.put(ROUTE_PLAN_TBL.VILLAGEID_1, entity.villageid_1);
            initialValues.put(ROUTE_PLAN_TBL.VILLAGEID_2, entity.villageid_2);
            initialValues.put(ROUTE_PLAN_TBL.VILLAGEID_3, entity.villageid_3);
            initialValues.put(ROUTE_PLAN_TBL.VILLAGEID_4, entity.villageid_4);
            initialValues.put(ROUTE_PLAN_TBL.VILLAGEID_5, entity.villageid_5);
            initialValues.put(ROUTE_PLAN_TBL.VILLAGENAME_1, entity.villagename_1);
            initialValues.put(ROUTE_PLAN_TBL.VILLAGENAME_2, entity.villagename_2);
            initialValues.put(ROUTE_PLAN_TBL.VILLAGENAME_3, entity.villagename_3);
            initialValues.put(ROUTE_PLAN_TBL.VILLAGENAME_4, entity.villagename_4);
            initialValues.put(ROUTE_PLAN_TBL.VILLAGENAME_5, entity.villagename_5);
            initialValues.put(ROUTE_PLAN_TBL.CREATED, entity.created);
            initialValues.put(ROUTE_PLAN_TBL.CREATED_BY, entity.created_by);
            initialValues.put(ROUTE_PLAN_TBL.RP_FIELD1, entity.rp_field1);
            initialValues.put(ROUTE_PLAN_TBL.RP_FIELD2, entity.rp_field2);
            initialValues.put(ROUTE_PLAN_TBL.RP_FIELD3, entity.rp_field3);
            initialValues.put(ROUTE_PLAN_TBL.RP_FIELD4, entity.rp_field4);
            initialValues.put(ROUTE_PLAN_TBL.RP_FIELD5, entity.rp_field5);
            initialValues.put(ROUTE_PLAN_TBL.RP_FIELD6, entity.rp_field6);
            initialValues.put(ROUTE_PLAN_TBL.RP_FIELD7, entity.rp_field7);
            initialValues.put(ROUTE_PLAN_TBL.RP_FIELD8, entity.rp_field8);
            initialValues.put(ROUTE_PLAN_TBL.RP_FIELD9, entity.rp_field9);
            initialValues.put(ROUTE_PLAN_TBL.RP_FIELD10, entity.rp_field10);
            retVal = adapter.getDB().insert(ROUTE_PLAN_TBL.TABLE_NAME, null, initialValues);
        } catch (Exception e) {
            Logger.e(TAG,e);
            e.printStackTrace();
        }
        Logger.d(TAG, "retval="+retVal);
        //adapter.close();
        return retVal;
    }

    public static synchronized long insertDealer(Context context, DealerEntity entity){

        DBAdapter adapter = DBAdapter.getInstance(context);
        adapter.open();
        long retVal =-1;
        try {
            ContentValues initialValues = new ContentValues();
            initialValues.put(DEALER_TBL.DEALER_ID, entity.dealerId);
            initialValues.put(DEALER_TBL.MEETING_ID, entity.meetingId);
            initialValues.put(DEALER_TBL.NAME, entity.name);
            initialValues.put(DEALER_TBL.CODE, entity.code);
            initialValues.put(DEALER_TBL.CONTACT1, entity.contact1);
            initialValues.put(DEALER_TBL.CONTACT2, entity.contact2);
            initialValues.put(DEALER_TBL.STATE_ID, entity.stateId);
            initialValues.put(DEALER_TBL.VILLAGE_ID, entity.villageId);
            retVal = adapter.getDB().insert(DEALER_TBL.TABLE_NAME, null, initialValues);
        } catch (Exception e) {
            Logger.e(TAG,e);
            e.printStackTrace();
        }
        Logger.d(TAG, "retval="+retVal);
        //adapter.close();
        return retVal;
    }

    public static synchronized long insertPlanning(Context context, PlanningEntity entity){

        DBAdapter adapter = DBAdapter.getInstance(context);
        adapter.open();
        long retVal =-1;
        try {
            ContentValues initialValues = new ContentValues();
            initialValues.put(PLANNING_TBL.PLANNING_ID, entity.planningId);
            initialValues.put(PLANNING_TBL.VID, entity.vId);
            initialValues.put(PLANNING_TBL.DIVISION, entity.division);
            initialValues.put(PLANNING_TBL.DEPO, entity.depo);
            initialValues.put(PLANNING_TBL.DEPO_CODE, entity.depoCode);
            initialValues.put(PLANNING_TBL.MEETING_TYPE, entity.meetingType);
            initialValues.put(PLANNING_TBL.MEETING_CODE, entity.meetingCode);
            initialValues.put(PLANNING_TBL.DATE, entity.date);
            initialValues.put(PLANNING_TBL.TRAINING_CENTER_NAME, entity.trainingCenterName);
            initialValues.put(PLANNING_TBL.START_TIME, entity.startTime);
            initialValues.put(PLANNING_TBL.STATE_ID, entity.statedId);
            initialValues.put(PLANNING_TBL.VILLAGE_ID, entity.villageId);
            initialValues.put(PLANNING_TBL.LOCATION, entity.location);
            initialValues.put(PLANNING_TBL.ESTIMATE_ATTENDANCE, entity.estimateAttendance);
            initialValues.put(PLANNING_TBL.NEROLAC_TSI, entity.nerolacTsi);
            initialValues.put(PLANNING_TBL.NEROLAC_TSI_CONTACT, entity.nerolacTsiContact);
            initialValues.put(PLANNING_TBL.BUDGET_PER_MEET, entity.budgetPerMeet);
            initialValues.put(PLANNING_TBL.ASM_NAME, entity.asmName);
            initialValues.put(PLANNING_TBL.ASM_CONTACT, entity.asmContact);
            initialValues.put(PLANNING_TBL.DSM_NAME, entity.dsmName);
            initialValues.put(PLANNING_TBL.DSM_CONTACT, entity.dsmContact);
            initialValues.put(PLANNING_TBL.PLANNING_FIELD1, entity.remarks);
            initialValues.put(PLANNING_TBL.PLANNING_FIELD4, entity.planning_field4);
            initialValues.put(PLANNING_TBL.PLANNING_FIELD5, entity.planning_field5);
            initialValues.put(PLANNING_TBL.STATUS, entity.status);
            retVal = adapter.getDB().insert(PLANNING_TBL.TABLE_NAME, null, initialValues);
        } catch (Exception e) {
            Logger.e(TAG,e);
            e.printStackTrace();
        }
        Logger.d(TAG, "retval="+retVal);
        //adapter.close();
        return retVal;
    }

    public static synchronized long insertPainter(Context context, PainterEntity entity){

        DBAdapter adapter = DBAdapter.getInstance(context);
        adapter.open();
        long retVal =-1;
        try {
            ContentValues initialValues = new ContentValues();
            initialValues.put(PAINTER_TBL.PLANNING_ID, entity.planningId);
            initialValues.put(PAINTER_TBL.VID, entity.vId);
            initialValues.put(PAINTER_TBL.VILLAGE_ID, entity.villageId);
            initialValues.put(PAINTER_TBL.PAINTER_NAME, entity.painterName);
            initialValues.put(PAINTER_TBL.NPP_CODE, entity.nppCode);
            initialValues.put(PAINTER_TBL.DEALER_NAME, entity.dealerName);
            initialValues.put(PAINTER_TBL.DEALER_ID, entity.dealerId);
            initialValues.put(PAINTER_TBL.CONTACT, entity.contact);
            initialValues.put(PAINTER_TBL.DETAIL_START_TIME, entity.detailStartTime);
            initialValues.put(PAINTER_TBL.STATUS, Constants.INPROGRESS);
            initialValues.put(PAINTER_TBL.BUSINESS_STARTED_YEAR, entity.business_started_year);
            initialValues.put(PAINTER_TBL.QUALIFICATION, entity.qualification);
            initialValues.put(PAINTER_TBL.TEAM_SIZE, entity.team_size);
            initialValues.put(PAINTER_TBL.DEALER_CODE, entity.dealer_code);
            initialValues.put(PAINTER_TBL.DEALER_CONTACT, entity.dealer_contact);
            initialValues.put(PAINTER_TBL.ORDER_BOOKING, entity.order_booking);
            initialValues.put(PAINTER_TBL.REMARK1, entity.remark1);
            initialValues.put(PAINTER_TBL.REMARK2, entity.remark2);
            initialValues.put(PAINTER_TBL.PAINTER_FIELD1, entity.painter_field1);
            initialValues.put(PAINTER_TBL.PAINTER_FIELD2, entity.painter_field2);
            initialValues.put(PAINTER_TBL.PAINTER_FIELD3, entity.painter_field3);
            initialValues.put(PAINTER_TBL.PAINTER_FIELD4, entity.painter_field4);
            initialValues.put(PAINTER_TBL.PAINTER_FIELD5, entity.painter_field5);
            retVal = adapter.getDB().insert(PAINTER_TBL.TABLE_NAME, null, initialValues);
        } catch (Exception e) {
            Logger.e(TAG,e);
            e.printStackTrace();
        }
        Logger.d(TAG, "retval="+retVal);
        //adapter.close();
        return retVal;
    }

    public static synchronized long insertMeeting(Context context, MeetingEntity entity){

        DBAdapter adapter = DBAdapter.getInstance(context);
        adapter.open();
        long retVal =-1;
        try {
            ContentValues initialValues = new ContentValues();
            initialValues.put(MEETING_TBL.PLANNING_ID, entity.planningId);
            initialValues.put(MEETING_TBL.VID, entity.vId);
            initialValues.put(MEETING_TBL.HOTEL_NAME, entity.hotelName);
            initialValues.put(MEETING_TBL.PAINTER_ATTENDANCE, entity.painterAttendance);
            initialValues.put(MEETING_TBL.NON_PARTICIPANTS, entity.nonParticipants);
            initialValues.put(MEETING_TBL.MEETING_DETAIL_START, entity.meetiingDetailsStart);
            initialValues.put(MEETING_TBL.TOTAL_GIFTS_GIVEN, entity.totalGiftsGiven);
            initialValues.put(MEETING_TBL.DETAIL_START_TIME, entity.detailStartTime);
            initialValues.put(MEETING_TBL.STATUS, Constants.INPROGRESS);
            initialValues.put(MEETING_TBL.MEETING_FIELD1, entity.meeting_field1);
            initialValues.put(MEETING_TBL.MEETING_FIELD2, entity.meeting_field2);
            initialValues.put(MEETING_TBL.MEETING_FIELD3, entity.meeting_field3);
            initialValues.put(MEETING_TBL.MEETING_FIELD4, entity.meeting_field4);
            initialValues.put(MEETING_TBL.MEETING_FIELD5, entity.meeting_field5);
            retVal = adapter.getDB().insert(MEETING_TBL.TABLE_NAME, null, initialValues);
        } catch (Exception e) {
            Logger.e(TAG,e);
            e.printStackTrace();
        }
        Logger.d(TAG, "retval="+retVal);
        //adapter.close();
        return retVal;
    }


    public static synchronized long insertSaleMetadata(Context context, SaleMetadataEntity entity){

        DBAdapter adapter = DBAdapter.getInstance(context);
        adapter.open();
        long retVal =-1;
        try {
            ContentValues initialValues = new ContentValues();
            initialValues.put(SALE_METADATA_TBL.TRANSACTION_ID, entity.transaction_id);
            initialValues.put(SALE_METADATA_TBL.MEETING_ID, entity.meeting_id);
            initialValues.put(SALE_METADATA_TBL.FID, entity.fid);
            initialValues.put(SALE_METADATA_TBL.FILE_PATH, entity.filePath);
            initialValues.put(SALE_METADATA_TBL.CREATED, entity.created);
            initialValues.put(SALE_METADATA_TBL.STATUS, Constants.INPROGRESS);
            initialValues.put(SALE_METADATA_TBL.TAG, entity.tag);
            initialValues.put(SALE_METADATA_TBL.LATITUDE, entity.latitude);
            initialValues.put(SALE_METADATA_TBL.LONGITUDE, entity.longitude);
            initialValues.put(SALE_METADATA_TBL.ACCURACY, entity.accuracy);

            retVal = adapter.getDB().insert(SALE_METADATA_TBL.TABLE_NAME, null, initialValues);
        } catch (Exception e) {
            Logger.e(TAG,e);
            e.printStackTrace();
        }
        Logger.d(TAG, "retval="+retVal);
        //adapter.close();
        return retVal;
    }

    public static synchronized long insertStartDayData(Context context, StartDayEntity entity){

        DBAdapter adapter = DBAdapter.getInstance(context);
        adapter.open();
        long retVal =-1;
        try {
            ContentValues initialValues = new ContentValues();
            initialValues.put(STARTDAY_TBL.START_ID, entity.startId);
            initialValues.put(STARTDAY_TBL.IMAGE_PATH, entity.imagePath);
            initialValues.put(STARTDAY_TBL.START_DATE, entity.startDate);
            initialValues.put(STARTDAY_TBL.REMARKS, entity.remarks);
            initialValues.put(STARTDAY_TBL.LATITUDE, entity.latitude);
            initialValues.put(STARTDAY_TBL.LONGITUDE, entity.longitude);
            initialValues.put(STARTDAY_TBL.ACCURACY, entity.accuracy);
            initialValues.put(STARTDAY_TBL.STATUS, Constants.INPROGRESS);
            initialValues.put(STARTDAY_TBL.MOBILE, entity.mobile);
            retVal = adapter.getDB().insert(STARTDAY_TBL.TABLE_NAME, null, initialValues);
        } catch (Exception e) {
            Logger.e(TAG,e);
            e.printStackTrace();
        }
        Logger.d(TAG, "retval="+retVal);
        //adapter.close();
        return retVal;
    }
}
