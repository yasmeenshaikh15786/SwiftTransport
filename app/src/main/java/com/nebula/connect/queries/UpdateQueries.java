package com.nebula.connect.queries;

import android.content.ContentValues;
import android.content.Context;

import com.nebula.connect.Constants;
import com.nebula.connect.Settings;
import com.nebula.connect.db.DBAdapter;
import com.nebula.connect.entities.MeetingEntity;
import com.nebula.connect.entities.PainterEntity;
import com.nebula.connect.entities.PlanningEntity;
import com.nebula.connect.entities.RoutePlanEntity;
import com.nebula.connect.logreports.Logger;
import com.nebula.connect.tables.MEETING_TBL;
import com.nebula.connect.tables.PAINTER_TBL;
import com.nebula.connect.tables.PLANNING_TBL;
import com.nebula.connect.tables.ROUTE_PLAN_TBL;
import com.nebula.connect.tables.SALE_METADATA_TBL;
import com.nebula.connect.tables.SETTINGS_TBL;
import com.nebula.connect.tables.STARTDAY_TBL;

/**
 * Created by siddhesh on 7/22/16.
 */
public class UpdateQueries {

    private static final String TAG = UpdateQueries.class.getSimpleName();
    private UpdateQueries(){
        super();
    }

    public synchronized static long updateSetting(Context context, Settings name, String value){

        DBAdapter adapter = DBAdapter.getInstance(context);
        adapter.open();
        ContentValues initialValues = new ContentValues();
        initialValues.put(SETTINGS_TBL.STB_STNGS_VAL, value);
        long retVal = adapter.getDB().update(
                SETTINGS_TBL.TABLE_NAME,
                initialValues,
                new StringBuffer().append(SETTINGS_TBL.STB_STNGS_NM).append(" = '").append(name).append("'").toString(), null);

        Logger.d(TAG, "retVal=" + retVal);

        //adapter.close();
        return retVal;
    }

    public static synchronized long updateMetadataStatus(Context context, int mId, String status) {
        DBAdapter adapter = DBAdapter.getInstance(context);
        adapter.open();
        ContentValues initialValues = new ContentValues();
        initialValues.put(SALE_METADATA_TBL.STATUS, status);
        long retVal = adapter.getDB().update(
                SALE_METADATA_TBL.TABLE_NAME,
                initialValues,
                new StringBuffer().append(SALE_METADATA_TBL.SALE_M_ID).append(" = ").append(mId)
                        .toString(), null);
        Logger.d(TAG, "retVal=" + retVal);
        //adapter.close();

        return retVal;
    }

    public static synchronized long updateAllMetadataStatus(Context context, String status) {
        DBAdapter adapter = DBAdapter.getInstance(context);
        adapter.open();
        ContentValues initialValues = new ContentValues();
        initialValues.put(SALE_METADATA_TBL.STATUS, status);
        long retVal = adapter.getDB().update(
                SALE_METADATA_TBL.TABLE_NAME,
                initialValues,
               null, null);
        Logger.d(TAG, "retVal=" + retVal);
        //adapter.close();

        return retVal;
    }

    public static synchronized long updateStartDayStatus(Context context, int startId, String status) {
        DBAdapter adapter = DBAdapter.getInstance(context);
        adapter.open();
        ContentValues initialValues = new ContentValues();
        initialValues.put(STARTDAY_TBL.STATUS, status);
        long retVal = adapter.getDB().update(
                STARTDAY_TBL.TABLE_NAME,
                initialValues,
                new StringBuffer().append(STARTDAY_TBL.START_ID).append(" = ").append(startId)
                        .toString(), null);
        Logger.d(TAG, "retVal=" + retVal);
        //adapter.close();

        return retVal;
    }

    public static synchronized long updatePlanningStatus(Context context, int planID, String status) {
        DBAdapter adapter = DBAdapter.getInstance(context);
        adapter.open();
        ContentValues initialValues = new ContentValues();
        initialValues.put(PLANNING_TBL.STATUS, status);
        long retVal = adapter.getDB().update(
                PLANNING_TBL.TABLE_NAME,
                initialValues,
                new StringBuffer().append(PLANNING_TBL.PLANNING_ID).append(" = ").append(planID)
                        .toString(), null);

        Logger.d(TAG, "retVal=" + retVal);
        //adapter.close();

        return retVal;
    }

    public static synchronized int updateRoutePlanRecord(Context context, RoutePlanEntity entity, boolean byId) {
        DBAdapter adapter = DBAdapter.getInstance(context);
        adapter.open();
        ContentValues initialValues = new ContentValues();
        initialValues.put(ROUTE_PLAN_TBL.VID, entity.vid);
        initialValues.put(ROUTE_PLAN_TBL.UID, entity.uid);
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

        String whereClause;
        if(byId){
            initialValues.put(ROUTE_PLAN_TBL.DATE, entity.date);
            whereClause = new StringBuffer().append(ROUTE_PLAN_TBL.R_ID).append(" = ").append(entity.r_id).toString();
        }else {
            initialValues.put(ROUTE_PLAN_TBL.R_ID, entity.r_id);
            whereClause = new StringBuffer().append(ROUTE_PLAN_TBL.DATE).append(" = '").append(entity.date).append("'")
                    .toString();
        }
        long retVal = adapter.getDB().update(
                ROUTE_PLAN_TBL.TABLE_NAME,
                initialValues, whereClause, null);
        Logger.d(TAG, "retVal=" + retVal);
        //adapter.close();

        return (int)retVal;
    }

    public static synchronized int updatePlanningRecord(Context context, PlanningEntity entity) {
        DBAdapter adapter = DBAdapter.getInstance(context);
        adapter.open();
        ContentValues initialValues = new ContentValues();
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
        initialValues.put(PLANNING_TBL.STATUS, entity.status);

        String whereClause = new StringBuffer().append(PLANNING_TBL.PLANNING_ID).append(" = ").append(entity.planningId).toString();
        long retVal = adapter.getDB().update(
                PLANNING_TBL.TABLE_NAME,
                initialValues, whereClause, null);
        Logger.d(TAG, "retVal=" + retVal);
        //adapter.close();

        return (int)retVal;
    }

    public static synchronized long updateMeetingStatus(Context context, int planningId, String status) {
        DBAdapter adapter = DBAdapter.getInstance(context);
        adapter.open();
        ContentValues initialValues = new ContentValues();
        initialValues.put(MEETING_TBL.STATUS, status);
        long retVal = adapter.getDB().update(
                MEETING_TBL.TABLE_NAME,
                initialValues,
                new StringBuffer().append(MEETING_TBL.PLANNING_ID).append(" = ").append(planningId)
                        .toString(), null);

        Logger.d(TAG, "retVal=" + retVal);
        //adapter.close();

        return retVal;
    }

    public static synchronized long updatePainterStatus(Context context, int planningId, String status) {
        DBAdapter adapter = DBAdapter.getInstance(context);
        adapter.open();
        ContentValues initialValues = new ContentValues();
        initialValues.put(PAINTER_TBL.STATUS, status);
        long retVal = adapter.getDB().update(
                PAINTER_TBL.TABLE_NAME,
                initialValues,
                new StringBuffer().append(PAINTER_TBL.PLANNING_ID).append(" = ").append(planningId)
                        .toString(), null);

        Logger.d(TAG, "retVal=" + retVal);
        //adapter.close();

        return retVal;
    }

    public static synchronized long updateMeetByPlanId(Context context, MeetingEntity entity) {
        DBAdapter adapter = DBAdapter.getInstance(context);
        adapter.open();
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

        long retVal = adapter.getDB().update(
                MEETING_TBL.TABLE_NAME,
                initialValues,
                new StringBuffer().append(MEETING_TBL.PLANNING_ID).append(" = ").append(entity.planningId)
                        .toString(), null);

        Logger.d(TAG, "retVal=" + retVal);
        //adapter.close();

        return retVal;
    }

    public static synchronized long updatePaintByPaintId(Context context, PainterEntity entity, int paintId) {
        DBAdapter adapter = DBAdapter.getInstance(context);
        adapter.open();
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
        initialValues.put(PAINTER_TBL.PAINTER_FIELD1, entity.painter_field1);
        initialValues.put(PAINTER_TBL.PAINTER_FIELD2, entity.painter_field2);
        initialValues.put(PAINTER_TBL.PAINTER_FIELD3, entity.painter_field3);
        initialValues.put(PAINTER_TBL.PAINTER_FIELD4, entity.painter_field4);
        initialValues.put(PAINTER_TBL.PAINTER_FIELD5, entity.painter_field5);

        long retVal = adapter.getDB().update(
                PAINTER_TBL.TABLE_NAME,
                initialValues,
                new StringBuffer().append(PAINTER_TBL.PAINTER_ID).append(" = ").append(paintId)
                        .toString(), null);

        Logger.d(TAG, "retVal=" + retVal);
        //adapter.close();

        return retVal;
    }


}
