package com.nebula.connect.queries;

import android.content.Context;

import com.nebula.connect.Constants;
import com.nebula.connect.db.DBAdapter;
import com.nebula.connect.logreports.Logger;
import com.nebula.connect.tables.DEALER_TBL;
import com.nebula.connect.tables.ROUTE_PLAN_TBL;
import com.nebula.connect.tables.SALE_METADATA_TBL;

/**
 * Created by sagar on 2/8/16.
 */
public class DeleteQueries {

    private static final String TAG = DeleteQueries.class.getSimpleName();
    private DeleteQueries(){
        super();
    }

    public static synchronized long deleteSaleMetadataRecord(Context context, int planId) {

        DBAdapter adapter = DBAdapter.getInstance(context);
        adapter.open();
        long retVal = adapter.getDB().delete(
                SALE_METADATA_TBL.TABLE_NAME,
                new StringBuffer().append(SALE_METADATA_TBL.MEETING_ID).append(" = ").append(planId)
                        .append(" AND ").append(SALE_METADATA_TBL.STATUS).append(" = '").append(Constants.INPROGRESS)
                        .append("'").append(" AND ").append(SALE_METADATA_TBL.TAG).append(" != '").append(Constants.PHOTO_START)
                        .append("'").toString(), null);

        Logger.d(TAG, "retval="+retVal);
        //adapter.close();
        return retVal;
    }

    public static synchronized long deleteDealer(Context context, int planId) {

        DBAdapter adapter = DBAdapter.getInstance(context);
        adapter.open();
        long retVal = adapter.getDB().delete(
                DEALER_TBL.TABLE_NAME,
                new StringBuffer().append(DEALER_TBL.MEETING_ID).append(" = ").append(planId).toString(), null);

        Logger.d(TAG, "retval="+retVal);
        //adapter.close();
        return retVal;
    }


    public static synchronized long deleteRoutePlanRecord(Context context, int rId) {

        DBAdapter adapter = DBAdapter.getInstance(context);
        adapter.open();
        long retVal = adapter.getDB().delete(
                ROUTE_PLAN_TBL.TABLE_NAME,
                new StringBuffer().append(ROUTE_PLAN_TBL.R_ID).append(" = ").append(rId).toString(), null);

        Logger.d(TAG, "retval="+retVal);
        //adapter.close();
        return retVal;
    }

}
