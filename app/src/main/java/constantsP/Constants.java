package constantsP;


import android.annotation.*;

import java.util.HashMap;

public class Constants {

    // constant for picking up of image

    public static final int ALRM_TONE=300;
    public static final int TIMER_DATA=400;
    public static final int TIMER_DATA_TEXT=450;
    public static final int BLINK=445;

    public static final int CAMERA_CODE=999;
    public static final int PLETHY_1000=1000;

    public static final int SELECT_PICTURE=111;
    public static final int TIMER_FIRST_TWENTY_SEC=200;
    public static final int TIMER_AFTER_TWENTY_SEC=220;

    public static final int CONTINUE_PLOTTING=333;

    public static final int DEVICE_DISCONNECTED=190;

    public static final int PERMISSION_REQUEST_COARSE_LOCATION =112 ;

    public static boolean mIsloggedin=false;


    public static final String USER_NAME="USER_NAME";
    public static final String AGE="AGE";
    public static final String SEX="SEX";
    public static final String HEIGHT="HEIGHT";

    public static final String WEIGHT="WEIGHT";
    public static final String BMI="BMI";

    public static final String PROFILE_IMAGE="PROFILE_IMAGE";

    public static final String PASSWORD="PASSWORD";

    public static final String MOBILE_NO="MOBILE_NO";

    public static final String PREFERENCE_FILE_KEY="preference_file_key";
    public static final String PREFERENCE_PROFILE_IMAGE="preference_profile_iamge";

    public static final String PROFILE_FLAG="PROFILE_FLAG";
    public static final String USE_TYPE="USE_TYPE";
    public static final String PROFILE_ADD="PROFILE_ADD";
    public static final String PROFILE_EDIT="PROFILE_EDIT";

    public static final String MONTH="month";
    public static final String YEAR="year";

    public static final int REQUEST_CODE=100;

    public static final int CAMERA_REQUEST_CODE=221;

    public static final int BLUETOOTH_REQUEST_CODE=111;

    public static final String MAC_ADDRESS="MAC_ADDRESS";


    public static final String SPO2_HIGH="SPO2_HIGH";
    public static final String SPO2_LOW="SPO2_LOW";

    public static  final String HEART_RATE_HIGH="HEART_RATE_HIGH";
    public static final String HEART_RATE_LOW="HEART_RATE_LOW";


    public static final String PI_HIGH="PI_HIGH";
    public static final String PI_LOW="PI_LOW";


    public static final String TOGLE_SPO2_HIGH="TOGLE_SPO2_HIGH";
    public static final String TOGLE_SPO2_LOW="TOGLE_SPO2_LOW";

    public static final String TOGLE_HR_HIGH="TOGLE_HR_HIGH";
    public static final String TOGLE_HR_LOW="TOGLE_HR_LOW";

    public static final String TOGLE_PI_HIGH="TOGLE_PI_HIGH";
    public static final String TOGLE_PI_LOW="TOGLE_PI_LOW";

    public static final String BIOLIGHT_PREF="BIOLIGHT_PREF";
    public static final String BIOLIGHT_PREF_VOICE="BIO_LIGHT_PREF_VOICE";
    public static final String MM_UNIT_MEASUREMENT_KEY="MM_UNIT_MEASUREMENT_KEY";

    public static final String MM_HG="MM_HG";
    public static final String KPA="KPA";

    public static final String BIO_LIGHT_VOICE_STATUS="BIO_LIGHT_VOICE_STATUS";

    public static final String SWITCH_ON="ON";
    public static final String SWITCH_OFF="OFF";

    public static String ACTION_CONNECTION_STATUS="";
    public static final String RECORDS_DETAIL="RECORDS_DETAIL";

    public static final String RECORDS_WEIGH_MACHINE1="RECORDS_WEIGH_MACHINE1";
    public static final String RECORDS_WEIGH_MACHINE2="RECORDS_WEIGH_MACHINE2";
    public static final String RECORDS_WEIGH_MACHINE="RECORDS_WEIGH_MACHINE";

    // -----


    public static final String RECORDS_BPMACHINE_BIOLIGHT="RECORDS_BP_MACHINE_BIO_LIGHT";
    public static final String RECORDS_BPMACHINE_BPUMP="RECORDS_BP_MACHINE_B_PUMP";
    public static final String RECORDS_BP_MACHINE="RECORDS_BP_MACHINE";


    public static final String systolic="SYSTOLIC";
    public static final String diabolic ="DIABOLIC";
    public static final String pulse="PULSE";
    public static final String testing_time="TESTING_TIME";
    public static final String comment="COMMENT";


    public static final String DEVICE_PARAMETER_IOXY="bpl_ioxy";
    public static final String DEVICE_PARAMETER_IWEIGH="bpl_iweigh";
    public static final String DEVICE_PARAMETER_IPRESSURE="bpl_ipressure";







    public static  String LOGGED_User_ID="";

    public static final String SPO2_CONSTANTS="SPO2_CONSTANTS";
    public static final String PI_CONSTATNTS="PI_CONSTANTS";
    public static final String PR_CONSTANTS="PR_CONSTANTS";
    public static final String TESTING_TIME_CONSTANTS="TT_CONSTANTS";
    public static final String DURATION_CONSTANTS="DURATION_CONSTANTS";
    public static final String DEVICE_MACID="DEVICE_MACID";

    public static final String SP02_TEXT="spo2";
    public static final String PR_TEXT="pr";


    public static String SELECTED_USER_SNOW="";

    public static String SHOW_VALUES="FALSE";

    @SuppressLint("UseSparseArrays")
    public static HashMap<Integer,Integer> ROTATION_DEGREES=new HashMap<>();

    static {
        ROTATION_DEGREES.put(4,-90);
        ROTATION_DEGREES.put(5,-90);
        ROTATION_DEGREES.put(6,-90);
        ROTATION_DEGREES.put(7,-88);

        ROTATION_DEGREES.put(8,-88);
        ROTATION_DEGREES.put(9,-88);


        ROTATION_DEGREES.put(10,-87);
        ROTATION_DEGREES.put(11,-87);

        ROTATION_DEGREES.put(12,-85);
        ROTATION_DEGREES.put(13,-83);
        ROTATION_DEGREES.put(14,-82);

        ROTATION_DEGREES.put(15,-81);
        ROTATION_DEGREES.put(16,-80);

        ROTATION_DEGREES.put(17,-60);
        ROTATION_DEGREES.put(18,-30);
        ROTATION_DEGREES.put(19,-20);
        ROTATION_DEGREES.put(20,0);
        ROTATION_DEGREES.put(21,10);
        ROTATION_DEGREES.put(22,20);
        ROTATION_DEGREES.put(23,30);
        ROTATION_DEGREES.put(24,35);

        ROTATION_DEGREES.put(25,40);
        ROTATION_DEGREES.put(26,45);
        ROTATION_DEGREES.put(27,50);
        ROTATION_DEGREES.put(28,52);


        ROTATION_DEGREES.put(29,56);
        ROTATION_DEGREES.put(30,58);
        ROTATION_DEGREES.put(31,62);
        ROTATION_DEGREES.put(32,68);
        ROTATION_DEGREES.put(33,70);
        ROTATION_DEGREES.put(34,70);
        ROTATION_DEGREES.put(35,73);
        ROTATION_DEGREES.put(36,75);
        ROTATION_DEGREES.put(37,78);
        ROTATION_DEGREES.put(38,80);
        ROTATION_DEGREES.put(39,83);
        ROTATION_DEGREES.put(40,86);

        ROTATION_DEGREES.put(42,86);
        ROTATION_DEGREES.put(44,90);


    }



    public static String LOGGED_USER_NAME="default123@bpl.in";

    public static final String VOICE_ENABLED="VOICE_LOCALE";


    public static final int TEST_RESULT = 1000;
    public static final int CURRENT_DATA = 1001;
    public static final int WORK_MODE = 1002;
    public static final int WORK_STEP = 1003;
    public static final int ERROR_CODE = 1004;
    public static final int CURRENT_PRESSURE_VALUE = 1005;
    public static final int DEVICE_TIME = 1006;
    public static final int CURRENT_USER = 1007;
    public static final int REQUEST_CODE_SELECT_BLUETOOTH = 1008;


    public static final String ONGOING_BP="ONGOING_BP";
    public static final String MEASURED_BP="MEASURED_BP";


    public static final String NORMAL="NORMAL";
    public static final String DESIRABLE="DESIRABLE";
    public static final String LOW_="LOW";
    public static final String PRE_HYPERTENSION="PRE HYPERTENSION";
    public static final String HYPERTENSION="HYPERTENSION";
    public static final String MILD_HYPERTENSION="MILD HYPERTENSION";
    public static final String MODERATE_HYPERTENSION="MODERATE HYPERTENSION";
    public static final String SEVERE_HYPERTENSION="SEVERE HYPERTENSION";


    public static final String SWITCH_STATE_PREFERENCE_FILE="SWITCH_STATE_PREFERENCE";
    public static final String TOGGLE_STATE_PREFERENCE_FILE="TOGGLE_STATE_PREFERENCE";
    public static final String SWITCH_STATE_KEY="SWITCH_STATE_KEY";



    public static final String BPL_FOLDER="BPLBeWell";
    public static final String CHART="CHART";
    public static final String REPORT="REPORT";
    public static final String DATE="DATE";

    public static  String GENDER="AGE";

    public static  String SELECTED_USER_TYPE="Home";

    public static String SELECTED_MAC_ID_BT02="";
    public static String SELECTED_SERIAL_NO_ID_BT02="";


    public static final String USE_TYPE_HOME="Home";
    public static final String USE_TYPE_CLINIC="Clinic";

}
