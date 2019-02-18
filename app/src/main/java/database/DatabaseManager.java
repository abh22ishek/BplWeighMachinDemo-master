package database;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;


import java.util.*;

import biolight.*;
import constantsP.*;
import logger.*;
import model.*;

import static database.BplOximterdbHelper.*;


// Singelton class to manage database connection
public class DatabaseManager {

private final String TAG="DatabaseManager";


    private int mOpenCounter;
    private static DatabaseManager instance;
    private static SQLiteOpenHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;



    private DatabaseManager()
    {

    }

    public static synchronized void initializeInstance(SQLiteOpenHelper helper) {
        if (instance == null) {
            instance = new DatabaseManager();
            mDatabaseHelper = helper;
        }
    }


    // lazy intialization of singelton
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException(DatabaseManager.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }

        return instance;
    }

    public synchronized SQLiteDatabase openDatabase() {
        mOpenCounter++;
        if(mOpenCounter == 1) {
            // Opening new database
            mDatabase = mDatabaseHelper.getWritableDatabase();
          //  mDatabaseHelper.onUpgrade(mDatabase,1,2); // upgrade database
           // mDatabaseHelper.onDowngrade(mDatabase,1,2);
        }

        return mDatabase;
    }

    /*public synchronized void closeDatabase() {
        mOpenCounter--;
        if(mOpenCounter == 0) {
            // Closing database
            mDatabase.close();

        }
    }*/


    // to login

    public boolean Login(String username,String password) throws SQLException
    {
      //  Cursor mCursor = mDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE user_name=? AND password=?", new String[]{USER_NAME,PASSWORD});

        Logger.log(Level.INFO, "DatabaseManager", "username " +username);
        Logger.log(Level.INFO, "DatabaseManager", "password " +password);




        Cursor cursor=mDatabase.query(TABLE_NAME,
                new String[]{USER_NAME,PASSWORD},
                USER_NAME + "=?"+" "+"AND"+" "+PASSWORD+"=?" , new String[]{username,password}, null, null, null);

        Logger.log(Level.INFO, "DatabaseManager", "Get  username and password count=" + cursor.getCount());
        //Logger.log(Level.INFO, "DatabaseManager", "Get  username vlaue=" + cursor.getString(cursor.getColumnIndex("user_name")));

        if(cursor.moveToNext())
        {
            Logger.log(Level.INFO, "DatabaseManager", "Get  username value="
                    + cursor.getString(cursor.getColumnIndex("user_name")));
        }

        if(cursor.getCount() > 0)
            {
                return true;
            }

            cursor.close();
        return false;
    }



    // to check if username is already taken or not


    public boolean IsFirstTimeLogin()
    {
        boolean b;

        long count=DatabaseUtils.queryNumEntries(mDatabaseHelper.getReadableDatabase(),TABLE_NAME);

        if(count>=1)
            b=true;
        else
        b=false;

        Logger.log(Level.INFO, "Get count of username exists=", "" +count);


        return b;

    }


    public boolean IsProfileExists(String useType)
    {
        boolean b;

        long count;
        if(useType.equalsIgnoreCase("clinic")){
            count=DatabaseUtils.queryNumEntries(mDatabaseHelper.getReadableDatabase(), TABLE_NAME_PROFILE);
        }else{
             count=DatabaseUtils.queryNumEntries(mDatabaseHelper.getReadableDatabase(), TABLE_NAME_HOME_PROFILE);
        }


        if(count>=1)
            b=true;
        else
            b=false;

        Logger.log(Level.INFO, "Get Profile   exists=", "" +count);


        return b;

    }


    public boolean IsUsernameexists(String username)
    {
        boolean b=false;


        Cursor cursor=mDatabase.query(TABLE_NAME,
                new String[]{USER_NAME,PASSWORD,SECURITY_Q_1,SECURITY_Q_2,USE_TYPE},
                USER_NAME+"=?", new String[]{username}, null, null, null);




        Logger.log(Level.INFO, "Get count of username exists=", "" +  cursor.getCount());
        if(cursor.getCount()>=1)
        {
            b=true;
            Logger.log(Level.INFO,TAG,"Username already exists insie table");
            return b;


        }
        Logger.log(Level.INFO,TAG,"Username is new  inside table");
        cursor.close();
        return b;

    }

    public boolean IsUsernameexistsInLoginProfile(String loginName,String userName)
    {
        boolean b=false;


        Cursor cursor=mDatabase.query(TABLE_NAME_USER_PROFILE,null,
                USER_NAME+"=?" + " " + "AND" +" "+USER_NAME_+"=?",
                new String[]{loginName,userName}, null, null, null);




        Logger.log(Level.INFO, "Get count of username exists=", "" +  cursor.getCount());
        if(cursor.getCount()>=1)
        {
            b=true;
            Logger.log(Level.INFO,TAG,"Username already exists inside User LOGIN PROFILE");
            return b;


        }
        Logger.log(Level.INFO,TAG,"Username is new  inside  LOGIN PROFILE");
        cursor.close();
        return b;

    }

    // to check the security question entered by user is correct or not


    public HashMap<Boolean,String> IssecurityQvalid(String username,String sq1,String sq2)
    {
        boolean b=false;

        List<String> list=new ArrayList<>();
        HashMap<Boolean,String> hashMap=new HashMap<>();

        Cursor cursor=mDatabase.query(TABLE_NAME,
                new String[]{SECURITY_Q_1,SECURITY_Q_2,USE_TYPE},USER_NAME+"=?",new String[] {username},null,null,null);

        Logger.log(Level.INFO,TAG, "Get count of username exists for all 3 security questions= "+ cursor.getCount());
        if(cursor.moveToNext())
        {
            list.add(cursor.getString(cursor.getColumnIndex(SECURITY_Q_1)));
            list.add(cursor.getString(cursor.getColumnIndex(SECURITY_Q_2)));
            list.add(cursor.getString(cursor.getColumnIndex(USE_TYPE)));

            Logger.log(Level.INFO, "DataBaseManager get security q1", cursor.getString(cursor.getColumnIndex(SECURITY_Q_1)));
            Logger.log(Level.INFO,"DataBaseManager get security q2",cursor.getString(cursor.getColumnIndex(SECURITY_Q_2)));
            Logger.log(Level.INFO,"DataBaseManager get security q3",cursor.getString(cursor.getColumnIndex(USE_TYPE)));

        }

        cursor.close();

        if(list.get(0).equals(sq1) && list.get(1).equals(sq2))
        {
            b=true;
            hashMap.put(b,list.get(2));
            return hashMap;
        }

        return hashMap;


    }


    // to retreive  profile

    public List<UserModel> getprofilecontent(String username)
    {

        List<UserModel> user_Modelslist=new ArrayList<>();
        Cursor cursor=mDatabase.query(TABLE_NAME_PROFILE, null,USER_NAME+"=?",new String[]{username},null,null,null);

        UserModel m=new UserModel();
        Logger.log(Level.INFO, TAG, TABLE_NAME_PROFILE+"Get profile count=" + cursor.getCount());

        if(cursor.moveToNext())
        {
            m.setName(cursor.getString(cursor.getColumnIndex(NAME)));
            m.setAge(cursor.getString(cursor.getColumnIndex(AGE)));
            m.setSex(cursor.getString(cursor.getColumnIndex(SEX)));
            m.setHeight(cursor.getString(cursor.getColumnIndex(HEIGHT)));
            m.setWeight(cursor.getString(cursor.getColumnIndex(WEIGHT)));
            user_Modelslist.add(m);
        }


        Logger.log(Level.INFO, TAG, "user_Models_list length=" + user_Modelslist.size());
        cursor.close();
      return user_Modelslist;

    }



    // get memeber profile content

    public List<UserModel> getMemberprofilecontent(String memberName)
    {

        List<UserModel> user_Modelslist=new ArrayList<>();
        Cursor cursor=mDatabase.query(TABLE_NAME_HOME_PROFILE, null,
                NAME_HOME_USER+"=?",new String[]{memberName},null,null,null);

        UserModel m=new UserModel();
        Logger.log(Level.INFO, TAG, TABLE_NAME_PROFILE+"Get profile count=" + cursor.getCount());

        if(cursor.moveToNext())
        {
            m.setName(cursor.getString(cursor.getColumnIndex(NAME_HOME_USER)));
            m.setAge(cursor.getString(cursor.getColumnIndex(AGE_HOME_USER)));
            m.setSex(cursor.getString(cursor.getColumnIndex(SEX_HOME_USER)));
            m.setHeight(cursor.getString(cursor.getColumnIndex(HEIGHT_HOME_USER)));
            m.setAddress(cursor.getString(cursor.getColumnIndex(CITY_HOME_USER)));
            m.setWeight(cursor.getString(cursor.getColumnIndex(WEIGHT_HOME_USER)));
            m.setPhone(cursor.getString(cursor.getColumnIndex(PHONE_HOME_USER)));
            user_Modelslist.add(m);
        }



        Logger.log(Level.INFO, TAG, "user_Models_list length=" + user_Modelslist.size());

        cursor.close();
        return user_Modelslist;

    }

    // to retrieve user profile

    public List<UserModel> getAllUserprofilecontent(String username,String TAG)
    {

        List<UserModel> user_Model_List=new ArrayList<>();

        Cursor cursor;
        if(TAG.equals(Constants.USER_NAME)){
            cursor=mDatabase.query(TABLE_NAME_USER_PROFILE,
                    null,USER_NAME+"=?",new String[]{username},null,null,null);

        }else
        {
            cursor=mDatabase.query(TABLE_NAME_USER_PROFILE,
                    null,USER_NAME_+"=?",new String[]{username},null,null,null);

        }


        Logger.log(Level.DEBUG, TAG, TABLE_NAME_USER_PROFILE+"(((Get profile count=)))" + cursor.getCount()+"--column count" +
                +cursor.getColumnCount()+"");


        while (cursor.moveToNext())
        {
            UserModel m=new UserModel();
            m.setName(cursor.getString(cursor.getColumnIndex(USER_NAME)));
            m.setUserName(cursor.getString(cursor.getColumnIndex(USER_NAME_)));
            m.setPhone(cursor.getString(cursor.getColumnIndex(USER_PHONE_)));
            m.setSex(cursor.getString(cursor.getColumnIndex(USER_SEX_)));
            m.setAddress(cursor.getString(cursor.getColumnIndex(USER_ADDRESS_)));
            m.setHeight(cursor.getString(cursor.getColumnIndex(USER_HEIGHT_)));
            m.setWeight(cursor.getString(cursor.getColumnIndex(USER_WEIGHT_)));
            m.setUserEmail(cursor.getString(cursor.getColumnIndex(USER_EMAIL_)));
            user_Model_List.add(m);
        }

        cursor.close();


        Logger.log(Level.DEBUG, TAG, "user_Models_list length=" + user_Model_List.size());

        return user_Model_List;

    }



        // Home based Users



    public List<UserModel> getAllMemberProfilecontent(String username,String TAG)
    {

        List<UserModel> user_Model_List=new ArrayList<>();

        Cursor cursor;

        if(TAG.equals(Constants.USER_NAME)){
            cursor=mDatabase.query(TABLE_NAME_HOME_PROFILE,
                    null,USER_NAME+"=?",new String[]{username},null,null,null);

        }

        else
        {
            cursor=mDatabase.query(TABLE_NAME_HOME_PROFILE,
                    null,NAME_HOME_USER+"=?",new String[]{username},
                    null,null,null);

        }





        Logger.log(Level.DEBUG, TAG, TABLE_NAME_HOME_PROFILE+"(((Get profile count=)))" + cursor.getCount()+"--column count" +
                +cursor.getColumnCount()+"");


        while (cursor.moveToNext())
        {
            UserModel m=new UserModel();
            m.setName(cursor.getString(cursor.getColumnIndex(USER_NAME)));
            m.setUserName(cursor.getString(cursor.getColumnIndex(NAME_HOME_USER)));
            m.setPhone(cursor.getString(cursor.getColumnIndex(PHONE_HOME_USER)));
            m.setSex(cursor.getString(cursor.getColumnIndex(SEX_HOME_USER)));
            m.setAddress(cursor.getString(cursor.getColumnIndex(CITY_HOME_USER)));
            m.setHeight(cursor.getString(cursor.getColumnIndex(HEIGHT_HOME_USER)));
            m.setWeight(cursor.getString(cursor.getColumnIndex(WEIGHT_HOME_USER)));
            m.setAge(cursor.getString(cursor.getColumnIndex(AGE_HOME_USER)));
         //   m.setUserEmail(cursor.getString(cursor.getColumnIndex(USER_EMAIL_)));
            user_Model_List.add(m);
        }

        cursor.close();


        Logger.log(Level.DEBUG, TAG, "user_Models_list length=" + user_Model_List.size());

        return user_Model_List;

    }



    public  List<RecordDetailWeighMachine> get_measuredWeight(String username)
    {
        List<RecordDetailWeighMachine> user_weight_list=new ArrayList<>();
        Cursor cursor;

             cursor=mDatabase.query(TABLE_NAME_WEIGHT, null,
                    USER_NAME+"=?",new String[]{username},null,null,null);




        Logger.log(Level.INFO, TAG, TABLE_NAME_WEIGHT+"Get weight count=" + cursor.getCount());

        while(cursor.moveToNext())
        {
            RecordDetailWeighMachine m=new RecordDetailWeighMachine();
            m.setDate(cursor.getString(cursor.getColumnIndex(MEASURED_WEIGHT_DATE)));
            m.setBmi(Float.parseFloat(cursor.getString(cursor.getColumnIndex(MEASURED_BMI))));
            m.setWeight(Float.parseFloat(cursor.getString(cursor.getColumnIndex(MEASURED_WEIGHT))));

            user_weight_list.add(m);
        }
        cursor.close();
        return user_weight_list;
    }



    // get list of records of weigh machine b


    public  List<RecordDetailWeighMachine> get_measuredWeightMachineB(String username_)
    {
        final  List<RecordDetailWeighMachine> user_weight_list=new ArrayList<>();
        Cursor cursor=mDatabase.query(TABLE_NAME_WEIGHT_MACHINE_B, null,
                USER_NAME_+"=?",new String[]{username_},null,null,null);
        Logger.log(Level.INFO, TAG, TABLE_NAME_WEIGHT_MACHINE_B+"Get weight count=" + cursor.getCount());

        while(cursor.moveToNext())
        {
            RecordDetailWeighMachine m=new RecordDetailWeighMachine();
            m.setDate(cursor.getString(cursor.getColumnIndex(MEASURED_WEIGHT_DATE)));
            m.setBmi(Float.parseFloat(cursor.getString(cursor.getColumnIndex(MEASURED_BMI))));
            m.setWeight(Float.parseFloat(cursor.getString(cursor.getColumnIndex(MEASURED_WEIGHT))));

            user_weight_list.add(m);
        }
        cursor.close();
        return user_weight_list;
    }





    // to update password into the database

    public void update_password(String username,String new_password)
    {
        ContentValues cv=new ContentValues();
        cv.put(PASSWORD, new_password);
        mDatabase.update(TABLE_NAME, cv, USER_NAME + "=?", new String[]{username});
    }


    // retreive list of readings submitted by userId
    public List<RecordsDetail> get_Records_list(String username_)
    {

        List<RecordsDetail> recordsDetailList=new ArrayList<RecordsDetail>();


        Cursor cursor=mDatabase.query(TABLE_NAME_RECORDS, null,
                USER_NAME_+"=?",new String[]{username_},null,null, TESTING_TIME+" "+"DESC");




        Logger.log(Level.INFO, TAG,TABLE_NAME_RECORDS+ " records count=" + cursor.getCount());

        int i=0;
        while(cursor.moveToNext())
        {
            i++;
            Logger.log(Level.INFO, TAG, "Iteration c**" + i);
           recordsDetailList.add(new RecordsDetail(cursor.getString(cursor.getColumnIndex(BplOximterdbHelper.SPO2)),
                    cursor.getString(cursor.getColumnIndex(BplOximterdbHelper.PULSE_RATE)),
                    cursor.getString(cursor.getColumnIndex(BplOximterdbHelper.PERFUSION_INDEX)),
                    cursor.getString(cursor.getColumnIndex(TESTING_TIME)),
                    cursor.getString(cursor.getColumnIndex(BplOximterdbHelper.DURATION)),
                   cursor.getString(cursor.getColumnIndex(BplOximterdbHelper.DEVICE_MACID))));
        }

        Logger.log(Level.INFO, TAG, "Records detail length=" + recordsDetailList.size());
        cursor.close();

        return recordsDetailList;
    }


    public void delete_records(String username,String testing_time)
    {
        mDatabase.delete(TABLE_NAME_RECORDS, USER_NAME + "=?" + " " + "AND"
                + " " + TESTING_TIME + "=?", new String[]{username, testing_time});
    }


    public void delete_weighing_machine_records(String username,String measured_weight_date)
    {
        mDatabase.delete(TABLE_NAME_WEIGHT, USER_NAME + "=?" + " " + "AND" + " " + MEASURED_WEIGHT_DATE + "=?", new String[]{username,measured_weight_date});
    }


    // delete weigh_machine_b rec


    public void delete_weighing_machine_records_b(String username,String measured_weight_date)
    {
        mDatabase.delete(TABLE_NAME_WEIGHT_MACHINE_B, USER_NAME + "=?" + " " + "AND"
                + " " + MEASURED_WEIGHT_DATE + "=?", new String[]{username,measured_weight_date});
    }


    public List<RecordsDetail> get_Records_Test_Report(String username,String testing_time)
    {
        List<RecordsDetail> recordsDetailList=new ArrayList<RecordsDetail>();

        Cursor cursor=mDatabase.query(TABLE_NAME_RECORDS, null,
                USER_NAME + "=?" + " " + "AND" + " " + TESTING_TIME + "=?",new String[]{username,testing_time},null,null,null);

        Logger.log(Level.INFO, TAG,TABLE_NAME_RECORDS+ " records count=" + cursor.getCount());

        if(cursor.moveToNext())
        {

            Logger.log(Level.INFO, TAG, "Iteration c**" );
            recordsDetailList.add(new RecordsDetail(cursor.getString(cursor.getColumnIndex(BplOximterdbHelper.SPO2)),
                    cursor.getString(cursor.getColumnIndex(BplOximterdbHelper.PULSE_RATE)),
                    cursor.getString(cursor.getColumnIndex(BplOximterdbHelper.PERFUSION_INDEX)),
                    cursor.getString(cursor.getColumnIndex(TESTING_TIME)),
                    cursor.getString(cursor.getColumnIndex(BplOximterdbHelper.DURATION)),
                    cursor.getString(cursor.getColumnIndex(BplOximterdbHelper.DEVICE_MACID))));
        }

        Logger.log(Level.INFO, TAG, "Records detail length=" + recordsDetailList.size());
        cursor.close();

        // its size will always be 1
        return  recordsDetailList;
    }


    public void order_by_date()
    {
       //Cursor c= mDatabase.query(TABLE_NAME_RECORDS,null,null,null,null,null, TESTING_TIME+" DESC");

    }






    // --BioLight Database query

    // retreive list of BP Records submitted by userId

    public List<biolight.BPMeasurementModel> getBioLightBPRecords(String username_)
    {

        List<BPMeasurementModel> recordsDetailList=new ArrayList<>();


        Cursor cursor=mDatabase.query(BplOximterdbHelper.TABLE_NAME_BIO_LIGHT, null,
                USER_NAME_+"=?",new String[]{username_},null,null,
                BplOximterdbHelper.TESTING_TIME_BIO_LIGHT+" "+"DESC"+" "+"LIMIT 100");


        Logger.log(Level.INFO, TAG, BplOximterdbHelper.TABLE_NAME_BIO_LIGHT+
                " records count=" + cursor.getCount());


        while(cursor.moveToNext())
        {
            BPMeasurementModel m=new BPMeasurementModel();
            m.setSysPressure(cursor.getFloat(cursor.getColumnIndex(BplOximterdbHelper.SYS)));
            m.setDiabolicPressure(cursor.getFloat(cursor.getColumnIndex(BplOximterdbHelper.DIA)));
            m.setPulsePerMin(cursor.getFloat(cursor.getColumnIndex(BplOximterdbHelper.PUL)));
            m.setMeasurementTime(cursor.getString(cursor.getColumnIndex(BplOximterdbHelper.TESTING_TIME_BIO_LIGHT)));
            m.setTypeBP(cursor.getString(cursor.getColumnIndex(BplOximterdbHelper.TYPE_BP)));
            m.setComments(cursor.getString(cursor.getColumnIndex(BplOximterdbHelper.COMMENT)));
            recordsDetailList.add(m);

        }

        Logger.log(Level.INFO, TAG, "Records detail length=" + recordsDetailList.size());
        cursor.close();
        return recordsDetailList;
    }







    public void deleteBioLightBPRecords(String username,String testing_time)
    {
        mDatabase.delete(BplOximterdbHelper.TABLE_NAME_BIO_LIGHT, BplOximterdbHelper.USER_NAME + "=?" + " " + "AND"
                + " " + BplOximterdbHelper.TESTING_TIME_BIO_LIGHT + "=?", new String[]{username, testing_time});
    }





    public void deleteUser(String username)
    {
        mDatabase.delete(BplOximterdbHelper.TABLE_NAME_USER_PROFILE, BplOximterdbHelper.USER_NAME_ + "=?", new String[]{username});
    }


    public void deleteFamilyMembers(String username)
    {
        mDatabase.delete(BplOximterdbHelper.TABLE_NAME_HOME_PROFILE,
                BplOximterdbHelper.NAME_HOME_USER + "=?", new String[]{username});
    }



    // get activity for last users


    public List<LastActivityModel> getUserNamesFromLastActivity(String userName)
    {
        List<LastActivityModel> lastActivityModelList=new ArrayList<>();


        Cursor cursor=mDatabase.query(TABLE_NAME_LAST_ACTIVITY_USERS, null,
                USER_NAME+"=?",new String[]{userName},null,null, null);



        int i=0;

        Logger.log(Level.INFO, TAG,TABLE_NAME_LAST_ACTIVITY_USERS+ " records count=" + cursor.getCount());
        while(cursor.moveToNext())
        {
          i++;
            Logger.log(Level.INFO, TAG, "Iteration c**" + i);
            LastActivityModel m=new LastActivityModel();

            m.setUserNames_(cursor.getString(cursor.getColumnIndex(USER_NAME_)));
            m.setDeviceParameter(cursor.getString(cursor.getColumnIndex(DEVICE_PARAMETER)));

          int noOfdays=  Utility.getDaysBetweenDates(cursor.
                  getString(cursor.getColumnIndex(LAST_ACTIVITY_USERS)),DateTime.getCurrentDate()).size();
            m.setLastActivity(String.valueOf(noOfdays));
            lastActivityModelList.add(m);

        }

        if(!cursor.isClosed()){
            cursor.close();
        }
        Logger.log(Level.INFO, TAG, "lastActivityModelList.Size" + lastActivityModelList.size());
        return lastActivityModelList;
    }



}
