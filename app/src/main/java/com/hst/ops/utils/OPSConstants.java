package com.hst.ops.utils;

public class OPSConstants {


    //URL'S
    //BASE URL
    private static final String BASE_URL = "https://happysanz.in/";

    //Development Mode
    //development
//    public static final String JOINT_URL = "development/";
    //uat
//    public static final String JOINT_URL = "uat/";
    //live
    public static final String JOINT_URL = "opsweb/";
    //
//    //BUILD URL
    public static final String BUILD_URL = BASE_URL + JOINT_URL + "apiandroid/";


    //CONSTITUENCY URL
    public static final String GET_INTRO_VIDEO = "intro_video/";



    //////    Service Params    ///////


    public static String PARAM_MESSAGE = "msg";
    public static String PARAM_MESSAGE_ENG = "msg_en";
    public static String PARAM_MESSAGE_TAMIL = "msg_ta";

    //     Shared preferences file name
    public static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    //    Shared FCM ID
    public static final String KEY_FCM_ID = "fcm_id";

    //    Shared IMEI No
    public static final String KEY_IMEI = "imei_code";

    //    Shared Phone No
    public static final String KEY_MOBILE_NUMBER = "mobile_no";
    public static final String KEY_ADMIN_MOBILE_NUMBER = "mobile_no";

    //    Shared Lang
    public static final String KEY_LANGUAGE = "language";



    // Alert Dialog Constants
    public static String ALERT_DIALOG_TITLE = "alertDialogTitle";
    public static String ALERT_DIALOG_MESSAGE = "alertDialogMessage";
    public static String ALERT_DIALOG_TAG = "alertDialogTag";
    public static String ALERT_DIALOG_POS_BUTTON = "alert_dialog_pos_button";
    public static String ALERT_DIALOG_NEG_BUTTON = "alert_dialog_neg_button";


    //    USER DATA

    public static String KEY_USER_ID = "user_id";
    public static String KEY_VERSION = "version_code";
    public static String VERSION_VALUE = "1";



}
