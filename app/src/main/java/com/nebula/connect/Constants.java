package com.nebula.connect;

import android.os.Environment;

import com.nebula.connect.logreports.Logger;

import java.io.File;
import java.io.IOException;

/**
 * Created by sagar on 22/7/16.
 */
public class Constants {
    private static final String TAG=Constants.class.getSimpleName();

   // public final static String BASE_URL="http://nerolac.nebuladigital.in/";
    public final static String BASE_URL=" http://connect.nebuladigital.in/";
   /* public final static String BASE_URL="http://37.187.19.42/";*/
    public final static String LOGIN_URL=BASE_URL+"device/app/login";
    public final static String APP_TRANSACTION_URL = BASE_URL+"app/update-meeting";
    public final static String SALES_METADATA_URL = BASE_URL+"sales/metadata";
    public final static String LOGIN_AUDIT_URL = BASE_URL+"sales/loginaudit";
    public final static String ROUTE_PLAN_URL = BASE_URL+"app/routeplan";
    public final static String MEETINGS_URL = BASE_URL+"app/meetings";
    public final static String SETTINGS_URL = BASE_URL+"app/settings";
    public final static String LOGIN_HISTORY_URL = BASE_URL+"my-login-activity";
    public static final String OPEN = "OPEN";
    public static final String INPROGRESS = "INPROGRESS";
    public static final String CLOSED = "CLOSED";
    public static final String PHOTO_MEETING = "PHOTO_MEETING";
    public static final String PHOTO_PAINTER = "PHOTO_PAINTER";
    public static final String PHOTO_REPORT = "PHOTO_REPORT";
    public static final String PHOTO_REPORT_FRONT = "PHOTO_REPORT_FRONT";
    public static final String PHOTO_REPORT_BACK = "PHOTO_REPORT_BACK";
    public static final String PHOTO_START = "PHOTO_START";
    public static final String FIELD_USER_HISTORY_URL = BASE_URL+"field-user-meetings-report";
    public final static String PURCHASE_LIST_URL=BASE_URL+"field-user-purchase-list";
    public static final String fldr = Environment.getExternalStorageDirectory()+"/.NEBULA_CONNECT/";
    public static final String logFileName = fldr+".logfile.txt";
    public static final String TECH_REQ = "TECH_REQ";
    public static final String SYNC = "SYNC";
    public static final String PHOTOS = "PHOTOS";
    public static final String SALEMETA_SEPARATOR="@^";
    public final static String SEND_LOG = BASE_URL+"sales/technical";
    public final static String FCM_TOKEN_URL = BASE_URL+"device/fcm_token";
    public static final String MSG_SEPARATER = "$^";
    public final static String USER_GUIDE_APP_URL = BASE_URL+"app-guide";

    static {
        File dir = new File(fldr);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File logFile = new File(logFileName);
        if(!logFile.exists()){
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                Logger.e(TAG,e);
                e.printStackTrace();
            }
        }
    }


    public static final int IMG_WIDTH=640;
    public static final int IMG_HEIGHT=480;

}
