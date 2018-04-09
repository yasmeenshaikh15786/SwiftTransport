package com.nebula.connect.tables;

/**
 * Created by sagar on 29/3/17.
 */

public class PAINTER_TBL {

    public static String TABLE_NAME = "PAINTER_TBL";
    public static String PAINTER_ID="PAINTER_ID";
    public static String PLANNING_ID="PLANNING_ID";
    public static String VID="VID";
    public static String VILLAGE_ID="VILLAGE_ID";
    public static String PAINTER_NAME="PAINTER_NAME";
    public static String NPP_CODE="NPP_CODE";
    public static String DEALER_NAME="DEALER_NAME";
    public static String DEALER_ID="DEALER_ID";
    public static String CONTACT="CONTACT";
    public static String DETAIL_START_TIME="DETAIL_START_TIME";
    public static String STATUS="STATUS";
    public static String BUSINESS_STARTED_YEAR="BUSINESS_STARTED_YEAR";
    public static String QUALIFICATION="QUALIFICATION";
    public static String TEAM_SIZE="TEAM_SIZE";
    public static String DEALER_CODE="DEALER_CODE";
    public static String DEALER_CONTACT="DEALER_CONTACT";
    public static String ORDER_BOOKING="ORDER_BOOKING";
    public static String REMARK1="REMARK1";
    public static String REMARK2="REMARK2";
    public static String PAINTER_FIELD1="PAINTER_FIELD1";
    public static String PAINTER_FIELD2="PAINTER_FIELD2";
    public static String PAINTER_FIELD3="PAINTER_FIELD3";
    public static String PAINTER_FIELD4="PAINTER_FIELD4";
    public static String PAINTER_FIELD5="PAINTER_FIELD5";

    private static final String PAINTER_TBL_CREATE =
            (new StringBuffer()).append("create table " ).append( TABLE_NAME ).append(" ( " ).append(
                    PAINTER_ID ).append( " integer primary key autoincrement, " ).append(PLANNING_ID).append(" text, ")
                    .append(VID).append(" text, ")
                    .append(PAINTER_NAME).append(" text,").append(VILLAGE_ID).append(" text, ")
                    .append(DEALER_ID).append(" text, ")
                    .append(NPP_CODE).append(" text,").append(DEALER_NAME).append(" text, ")
                    .append(CONTACT).append(" text, ").append(DETAIL_START_TIME).append(" text, ")
                    .append(STATUS).append(" text, ").append(BUSINESS_STARTED_YEAR).append(" text, ")
                    .append(QUALIFICATION).append(" text, ").append(TEAM_SIZE).append(" text, ")
                    .append(DEALER_CODE).append(" text, ").append(DEALER_CONTACT).append(" text, ")
                    .append(ORDER_BOOKING).append(" text, ").append(REMARK1).append(" text, ")
                    .append(REMARK2).append(" text, ").append(PAINTER_FIELD1).append(" text, ")
                    .append(PAINTER_FIELD2).append(" text, ").append(PAINTER_FIELD3).append(" text, ")
                    .append(PAINTER_FIELD4)
                    .append(" text, ").append(PAINTER_FIELD5).append( " text);").toString();

    private static final String PAINTER_TBL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static String createTable(){
        return PAINTER_TBL_CREATE;
    }

    public static String dropTable(){
        return PAINTER_TBL_DROP;
    }

}
