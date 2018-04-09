package com.nebula.connect.tables;

/**
 * Created by sagar on 29/3/17.
 */

public class MEETING_TBL {

    public static String TABLE_NAME = "MEETING_TBL";
    public static String PLANNING_ID="PLANNING_ID";
    public static String VID = "VID";
    public static String HOTEL_NAME = "HOTEL_NAME";
    public static String PAINTER_ATTENDANCE="PAINTER_ATTENDANCE";
    public static String NON_PARTICIPANTS="NON_PARTICIPANTS";
    public static String MEETING_DETAIL_START="MEETING_DETAIL_START";
    public static String TOTAL_GIFTS_GIVEN="TOTAL_GIFTS_GIVEN";
    public static String DETAIL_START_TIME="DETAIL_START_TIME";
    public static String STATUS="STATUS";
    public static String MEETING_FIELD1="MEETING_FIELD1";
    public static String MEETING_FIELD2="MEETING_FIELD2";
    public static String MEETING_FIELD3="MEETING_FIELD3";
    public static String MEETING_FIELD4="MEETING_FIELD4";
    public static String MEETING_FIELD5="MEETING_FIELD5";

    private static final String MEETING_TBL_CREATE =
            (new StringBuffer()).append("create table " ).append( TABLE_NAME ).append(" ( " ).append(
                    PLANNING_ID ).append( " integer primary key , " ).append(VID).append(" text, ")
                    .append(HOTEL_NAME).append(" text, ").append(PAINTER_ATTENDANCE).append(" text,")
                    .append(NON_PARTICIPANTS).append(" text,").append(MEETING_DETAIL_START).append(" text, ")
                    .append(TOTAL_GIFTS_GIVEN).append(" text, ").append(DETAIL_START_TIME).append(" text, ")
                    .append(STATUS).append(" text, ").append(MEETING_FIELD1).append(" text, ")
                    .append(MEETING_FIELD2).append(" text, ").append(MEETING_FIELD3).append(" text, ")
                    .append(MEETING_FIELD4)
                    .append(" text, ").append(MEETING_FIELD5).append( " text);").toString();

    private static final String MEETING_TBL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static String createTable(){
        return MEETING_TBL_CREATE;
    }

    public static String dropTable(){
        return MEETING_TBL_DROP;
    }

}
