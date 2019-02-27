package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import logger.Level;
import logger.Logger;



public class BplOximterdbHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    private static final String TAG="BplOXimeterdbHelper";

    // Database Name
    public static final String DATABASE_NAME = "UserDatabase.db";

    // Table Names
    public static final String TABLE_NAME = "Users";

    public static final String TABLE_NAME_PROFILE = "Profile";
    public static final String TABLE_NAME_RECORDS = "Records";
    public static final String TABLE_NAME_WEIGHT = "Measured_Weight";


    public static final String TABLE_NAME_BIO_LIGHT = "Bio_Light";

    public static final String TABLE_NAME_WEIGHT_MACHINE_B = "Measured_Weight_Machine_B";

    public static final String TABLE_NAME_USER_PROFILE = "User_Profile";
    public static final String TABLE_NAME_HOME_PROFILE = "Home_Profile";

    public static final String TABLE_NAME_LAST_ACTIVITY_USERS= "Last_Activity";




    // Common column names
    public static final String KEY_ID = "id";
    public static final String KEY_ID_RECORDS = "records_no";
    public static final String USER_NAME ="user_name";
    public static final String PASSWORD="password";

    public static final String SECURITY_Q_1="security_question_1";
    public static final String SECURITY_Q_2="security_question_2";
    public static final String USE_TYPE="use_type";



    public static final  String NAME="name";
    public static  final  String AGE="age";
    public static  final String SEX="sex";
    public static  final String WEIGHT="weight";
    public static  final String HEIGHT="height";



    // profile home
    public static final  String NAME_HOME_USER="name_home";
    public static final  String CITY_HOME_USER="address_home";
    public static  final  String AGE_HOME_USER="age_home";
    public static  final String SEX_HOME_USER="sex_home";
    public static  final String WEIGHT_HOME_USER="weight_home";
    public static  final String HEIGHT_HOME_USER="height_home";
    public static  final String PHONE_HOME_USER="phone_home";


    public static final  String USER_NAME_="user_names";
    public static final  String USER_EMAIL_="user_email";

    public static final String DEVICE_PARAMETER="device_parameter";
    public static  final  String USER_AGE_="user_age";

    public static  final  String USER_ADDRESS_="user_address";
    public static  final String USER_SEX_="user_sex";
    public static  final  String USER_PHONE_="user_phone";
    public static  final String USER_WEIGHT_="user_weight";
    public static  final String USER_HEIGHT_="user_height";


    public static final String SPO2="spo2";
    public static final String PULSE_RATE="pulse_rate";
    public static final String PERFUSION_INDEX="perfusion_index";
    public static final String TESTING_TIME="testing_time";
    public static final String DURATION="duration";
    public static final String DEVICE_MACID="device_macid";

    public static final String LAST_ACTIVITY_USERS="last_activity";

    public static  final String MEASURED_WEIGHT="measured_weight";
    public static  final String MEASURED_WEIGHT_DATE="measured_weight_date_time";
    public static  final String MEASURED_BMI="measured_weight_bmi";



    // bio light parameters

    public static final String SYS="systolic_pressure";
    public static final String DIA="diabolic_pressure";
    public static final String PUL="pulse_per_min";
    public static final String TESTING_TIME_BIO_LIGHT="testing_time";
    public static final String COMMENT="comment";
    public static final String TYPE_BP="type_bp";

    public static final String BIOLIGHT_MACID="biolight_macid";
    public static final String BIOLIGHT_SERIAL_N0="biolight_Serial_no";

    public static final String BIOLIGHT_USE_TYPE="use_type";

    public static final String CREATE_TABLE="CREATE TABLE " +TABLE_NAME+"("+KEY_ID+" integer primary key autoincrement"+","
            +USER_NAME+" "+"text"+","+PASSWORD+" "+"text"+","+SECURITY_Q_1+" "+"text"+","
    +SECURITY_Q_2+" "+"text"+","+USE_TYPE+" "+"text"+")";


    public static final String CREATE_TABLE_PROFILE="CREATE TABLE " +TABLE_NAME_PROFILE+"("+KEY_ID+" integer primary key autoincrement"+","
            +USER_NAME+" "+"text"+","+
            NAME+" "+"text"+","+AGE+" "+"text"+","+SEX+" "+"text"+","+WEIGHT+" "+"text"+","+
            HEIGHT+" "+"text"+")";


// ---user


    public static final String CREATE_TABLE_USER_PROFILE="CREATE TABLE " +TABLE_NAME_USER_PROFILE+"("+KEY_ID+" integer primary key autoincrement"+","
            +USER_NAME+" "+"text"+","+
            USER_NAME_+" "+"text"+","+USER_PHONE_+" "+"text"+","+USER_SEX_+" "+"text"+","+USER_ADDRESS_+" "+"text"+","
            +USER_HEIGHT_+" "+"text"+","+
            USER_WEIGHT_+" "+"text"+","+
            USER_EMAIL_+" "+"text"



            +")";



// HOME PROFILE
public static final String CREATE_TABLE_HOME_PROFILE="CREATE TABLE " +TABLE_NAME_HOME_PROFILE+"("+KEY_ID+" integer primary key autoincrement"+","
        +USER_NAME+" "+"text"+","+
        NAME_HOME_USER+" "+"text"+","+AGE_HOME_USER+" "+"text"+","+SEX_HOME_USER+" "+"text"+","
        +PHONE_HOME_USER+" "+"text"+","
        +CITY_HOME_USER+" "+"text"+","
        +WEIGHT_HOME_USER+" "+"text"+","+
        HEIGHT_HOME_USER+" "+"text"+")";



    //-------
    public static final String CREATE_TABLE_RECORDS="CREATE TABLE " +TABLE_NAME_RECORDS+"("+KEY_ID+" integer primary key autoincrement"+","
            +USER_NAME+" "+"text"+","+USER_NAME_+" "+"text"+","+
            SPO2+" "+"text"+","+PULSE_RATE+" "+"text"+","+PERFUSION_INDEX+" "+"text"+","+TESTING_TIME+" "+"text"+","+
            DURATION+" "+"text"+","+DEVICE_MACID+" "+"text"+ ","+USE_TYPE+" "+"text"+")";


    public static final String CREATE_TABLE_WEIGHT="CREATE TABLE " +TABLE_NAME_WEIGHT+"("+KEY_ID+" integer primary key autoincrement"+","
            +USER_NAME+" "+"text"+","+USER_NAME_+" "+"text"+","
            +MEASURED_WEIGHT_DATE+" "+"text"+","
            +MEASURED_BMI+" "+"text"+","
            +MEASURED_WEIGHT+" "+"text"+")";



    public static final String CREATE_TABLE_WEIGHT_MACHINE_B="CREATE TABLE " +TABLE_NAME_WEIGHT_MACHINE_B+"("+KEY_ID+" integer primary key autoincrement"+","
            +USER_NAME+" "+"text"+","+USER_NAME_+" "+"text"+","
            +MEASURED_WEIGHT_DATE+" "+"text"+","
            +MEASURED_BMI+" "+"text"+","
            +MEASURED_WEIGHT+" "+"text"+")";





    public static final String CREATE_TABLE_BIO_LIGHT="CREATE TABLE " +TABLE_NAME_BIO_LIGHT+"("+KEY_ID+" " +
            "integer primary key autoincrement"+","
            +USER_NAME+" "+"REAL"+","+USER_NAME_+" "+"text"+","+SYS+" "+"REAL"+","
            +DIA+" "+"REAL"+","+PUL+" "+"REAL"+","+TESTING_TIME+" "+"TEXT"+","+TYPE_BP+" "+"TEXT"+","+
            COMMENT+" "+"TEXT"+","+ BIOLIGHT_MACID+" "+"TEXT"+","+ BIOLIGHT_SERIAL_N0+" "+"TEXT" +","+
            BIOLIGHT_USE_TYPE+" "+"TEXT"+ ")";




    public static final String CREATE_TABLE_LAST_ACTIVITY_OF_USERS=
            "CREATE TABLE " +TABLE_NAME_LAST_ACTIVITY_USERS+"("+KEY_ID+" integer primary key autoincrement"+","
                    +USER_NAME+" "+"text"+","+
                    USER_NAME_+" "+"text"+","+
                    DEVICE_PARAMETER+" "+"text"+","+
                    LAST_ACTIVITY_USERS+" "+"text"+","+USE_TYPE+" "+"text"
                    +")";





    public BplOximterdbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }





    private BplOximterdbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE_PROFILE);
        db.execSQL(CREATE_TABLE_USER_PROFILE);

        db.execSQL(CREATE_TABLE_RECORDS);
        db.execSQL(CREATE_TABLE_WEIGHT);
        db.execSQL(CREATE_TABLE_WEIGHT_MACHINE_B);

        db.execSQL(CREATE_TABLE_BIO_LIGHT);

        db.execSQL(CREATE_TABLE_LAST_ACTIVITY_OF_USERS);
        db.execSQL(CREATE_TABLE_HOME_PROFILE);

        Logger.log(Level.INFO, TAG, CREATE_TABLE);
        Logger.log(Level.INFO,TAG,CREATE_TABLE_PROFILE);

        Logger.log(Level.DEBUG, TAG, "Table" +TABLE_NAME +TABLE_NAME_PROFILE+TABLE_NAME_PROFILE+","
                        +TABLE_NAME_WEIGHT+","+TABLE_NAME_WEIGHT_MACHINE_B+" "+TABLE_NAME_HOME_PROFILE
                +"AND"+TABLE_NAME_BIO_LIGHT+"AND" +"of"+DATABASE_NAME+ "got created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Logger.log(Level.DEBUG,TAG,"On upgrade () is called with new Version="+newVersion +"and Old version="+oldVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PROFILE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_USER_PROFILE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_RECORDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_WEIGHT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_WEIGHT_MACHINE_B);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_BIO_LIGHT);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_LAST_ACTIVITY_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_HOME_PROFILE);
        onCreate(db);
     /*   if(oldVersion<1)
        {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_WEIGHT_MACHINE_B);
        }*/

    }


    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
        Logger.log(Level.DEBUG,TAG,"On Downgrade () is called with new Version="+newVersion +"and Old version="+oldVersion);
    }
}
