package com.nebula.connect.tables;

/**
 * Created by sagar on 29/3/17.
 */

public class DEALER_TBL {

    public static String TABLE_NAME = "DEALER_TBL";
    public static String DEALER_ID="DEALER_ID";
    public static String MEETING_ID="MEETING_ID";
    public static String VID="VID";
    public static String NAME="NAME";
    public static String CODE="CODE";
    public static String CONTACT1="CONTACT1";
    public static String CONTACT2="CONTACT2";
    public static String STATE_ID="STATE_ID";
    public static String VILLAGE_ID="VILLAGE_ID";
    public static String DEALER_FIELD1="DEALER_FIELD1";
    public static String DEALER_FIELD2="DEALER_FIELD2";
    public static String DEALER_FIELD3="DEALER_FIELD3";
    public static String DEALER_FIELD4="DEALER_FIELD4";
    public static String DEALER_FIELD5="DEALER_FIELD5";

    private static final String DEALER_TBL_CREATE =
            (new StringBuffer()).append("create table " ).append( TABLE_NAME ).append(" ( " ).append(
                    DEALER_ID ).append( " integer , " ).append(MEETING_ID).append(" text,").append(VID).append(" text, ")
                    .append(NAME).append(" text,").append(CODE).append(" text, ")
                    .append(CONTACT1).append(" text, ")
                    .append(CONTACT2).append(" text,").append(STATE_ID).append(" text, ").append(VILLAGE_ID).append(" text, ").append(DEALER_FIELD1).append(" text, ")
                    .append(DEALER_FIELD2).append(" text, ").append(DEALER_FIELD3).append(" text, ")
                    .append(DEALER_FIELD4)
                    .append(" text, ").append(DEALER_FIELD5).append( " text);").toString();

    private static final String DEALER_TBL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static String createTable(){
        return DEALER_TBL_CREATE;
    }

    public static String dropTable(){
        return DEALER_TBL_DROP;
    }

}

