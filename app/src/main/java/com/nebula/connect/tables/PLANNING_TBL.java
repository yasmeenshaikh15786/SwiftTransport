package com.nebula.connect.tables;

/**
 * Created by dipesh on 30/12/15.
 */
public class PLANNING_TBL {

    private PLANNING_TBL(){

    };
    public static String TABLE_NAME="PLANNING_TBL";
    public static String PLANNING_ID="PLANNING_ID";
    public static String VID = "VID";
    public static String DIVISION="DIVISION";
    public static String DEPO="DEPO";
    public static String DEPO_CODE="DEPO_CODE";
    public static String MEETING_TYPE="MEETING_TYPE";
    public static String MEETING_CODE="MEETING_CODE";
    public static String DATE="DATE";
    public static String TRAINING_CENTER_NAME="TRAINING_CENTER_NAME";
    public static String START_TIME="START_TIME";
    public static String STATE_ID="STATE_ID";
    public static String VILLAGE_ID="VILLAGE_ID";
    public static String LOCATION="LOCATION";
    public static String ESTIMATE_ATTENDANCE="ESTIMATE_ATTENDANCE";
    public static String NEROLAC_TSI="NEROLAC_TSI";
    public static String NEROLAC_TSI_CONTACT="NEROLAC_TSI_CONTACT";
    public static String BUDGET_PER_MEET="BUDGET_PER_MEET";
    public static String ASM_NAME="ASM_NAME";
    public static String ASM_CONTACT="ASM_CONTACT";
    public static String DSM_NAME="DSM_NAME";
    public static String DSM_CONTACT="DSM_CONTACT";
    public static String MONTH = "MONTH";
    public static String STATUS = "STATUS";
    public static String PLANNING_FIELD1="PLANNING_FIELD1";
    public static String PLANNING_FIELD2="PLANNING_FIELD2";
    public static String PLANNING_FIELD3="PLANNING_FIELD3";
    public static String PLANNING_FIELD4="PLANNING_FIELD4";
    public static String PLANNING_FIELD5="PLANNING_FIELD5";

    private static final String PLANNING_TBL_CREATE =
            (new StringBuffer()).append("create table " ).append( TABLE_NAME ).append(" ( " ).append(
                    PLANNING_ID ).append( " integer primary key , " ).append(VID).append(" text, ").append(DIVISION).append(" text,")
                    .append(DEPO).append(" text,").append(DEPO_CODE).append(" text, ")
                    .append(MEETING_TYPE).append(" text, ").append(MEETING_CODE).append(" text, ")
                    .append(DATE).append(" text, ").append(TRAINING_CENTER_NAME).append(" text, ")
                    .append(START_TIME).append(" text, ").append(STATE_ID).append(" text, ")
                    .append(VILLAGE_ID).append(" text, ").append(LOCATION).append(" text, ")
                    .append(ESTIMATE_ATTENDANCE).append(" text, ")
                    .append(NEROLAC_TSI).append(" text, ")
                    .append(NEROLAC_TSI_CONTACT).append(" text, ")
                    .append(BUDGET_PER_MEET).append(" text, ")
                    .append(ASM_NAME).append(" text, ")
                    .append(ASM_CONTACT).append(" text, ")
                    .append(DSM_CONTACT).append(" text, ")
                    .append(DSM_NAME).append(" text, ")
                    .append(MONTH).append(" text, ")
                    .append(STATUS).append(" text, ")
                    .append(PLANNING_FIELD1).append(" text , ")
                    .append(PLANNING_FIELD2).append(" text, ")
                    .append(PLANNING_FIELD3).append(" text, ").append(PLANNING_FIELD4)
                    .append(" text, ").append(PLANNING_FIELD5).append( " text);").toString();

    private static final String PLANNING_TBL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static String createTable(){
        return PLANNING_TBL_CREATE;
    }

    public static String dropTable(){
        return PLANNING_TBL_DROP;
    }

}
