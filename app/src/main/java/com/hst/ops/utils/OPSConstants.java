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


    //INTRO VIDEO URL
    public static final String GET_INTRO_VIDEO = "intro_video/";

    //NEWSFEED URL
    public static final String GET_NEWSFEED = "newsfeeds_categoryid/";

    //NEWSFEED DETAILS URL
    public static final String GET_NEWSFEED_DETAIL = "newsfeed_details/";

    //SEARCH URL
    public static final String SEARCH_NEWSFEED = "newsfeed_search/";

    //GALLERY URL
    public static final String GET_GALLERY = "gallery/";

    //ALL IMAGES URL
    public static final String GET_ALL_IMAGES = "images_all/";

    //ALL VIDEOS URL
    public static final String GET_ALL_VIDEOS = "videos_all/";

    //ALL VIDEOS URL
    public static final String GET_LIVE_EVENT_LIST = "live_events/";

    //PROFILE DETAILS
    public static final String GET_PROFILE_DETAILS = "profile_details/";

    //PROFILE UPDATE URL
    public static final String GET_PROFILE_UPDATE = "profile_update/";

    //PROFILE PIC UPDATE
    public static final String PROFILE_PIC_UPDATE = "profilepic_update/";

    //BIOGRAPHY URL
    public static final String GET_BIOGRAPHY = "ops_biogrphy/";

    //ACHIEVEMENTS URL
    public static final String GET_ACHIEVEMENTS = "ops_achievements/";

    //ABOUT PARTY URL
    public static final String ABOUT_PARTY = "about_party/";

    //PARTY STATE LIST
    public static final String PARTY_STATES = "party_states/";

    //ELECTION URL
    public static final String ELECTION_LIST = "party_elections/";


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


    //    Newsfeed Params
    public static final String NEWSFEED_CATEGORY = "nf_category_id";
    public static final String NEWSFEED_ID = "newsfeed_id";
    public static final String KEY_OFFSET = "offset";
    public static final String KEY_ROWCOUNT = "rowcount";
    public static final String KEY_SEARCH_KEYWORD = "search_text";


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
    public static String SEARCH_STATUS = "search_status";


    //    USER DETAILS

    public static final String KEY_USER_IMAGE = "profile_pic";
    public static final String KEY_PHONE_NO = "phone_number";
    public static final String KEY_USER_GENDER = "gender";
    public static final String KEY_USERNAME = "full_name";
    public static final String KEY_USER_BIRTHDAY = "dob";
    public static final String KEY_USER_EMAIL_ID = "email_id";
}
