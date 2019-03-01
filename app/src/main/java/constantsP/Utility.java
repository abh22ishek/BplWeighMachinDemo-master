package constantsP;

import android.annotation.*;
import android.app.*;
import android.content.*;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.*;
import java.text.*;
import java.util.*;

import database.*;
import logger.Level;
import logger.Logger;
import model.RecordsDetail;
import test.bpl.com.bplscreens.*;
import testreport.*;


public class Utility {

    public static List<RecordsDetail> mRecord_detail_list;

    public static String SPO02_LIMIT_LOW="43";
    public static String SPO22_LIMIIT_HIGH="12";

    public static long mRAM_SIZE=0;


    public static String PULSERATE_LIMIT_LOW="23";
    public static String PULSERATE_LIMIT_HIGH="32";

    public static String PI_LIMIT_LOW="23";
    public static String PI_LIMIT_HIGH="32";

    public static final SharedPreferences limitprefs=null;
    public static final String LIMIT_KEY="Limit_key_prefs";


    public static void  writeDatatoFile(String filename,String data)
    {

        String filenamedir="Bpl_be_Well";


        File file =new File(Environment.getExternalStorageDirectory(),filenamedir);
        if(!file.exists())
        {
            file.mkdir();
        }

        File ecgfile=new File(file,filename);


        try {
            FileWriter filewriter=new FileWriter(ecgfile,true);
            filewriter.append(data);
            filewriter.flush();
            filewriter.close();
            Log.i("FilePath", "saving data into file");

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }


    // to save image from camera into sd card

    public static Uri save_camera_pics()
    {
        String filenamedir="Bpl_be_Well_photos";

        File file =new File(Environment.getExternalStorageDirectory(),filenamedir);
            if(!file.exists())
            {
                file.mkdir();
            }

        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HHmmss").format(new Date());
        File mediaFile;
        mediaFile = new File(file.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");


        Uri uri = Uri.fromFile(mediaFile);
        Logger.log(Level.DEBUG,"Utility",uri.toString());
            return uri;
    }




    public static int getColorWrapper(Context context, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(id);
        } else {
            //noinspection deprecation
            return context.getResources().getColor(id);
        }
    }



    public static ContentValues lastActivityUsers(String admin_email, String user, String device_parameter, String date,String useType)
    {

        ContentValues values = new ContentValues();


        values.put(BplOximterdbHelper.USER_NAME, admin_email);
        values.put(BplOximterdbHelper.USER_NAME_, user);
        values.put(BplOximterdbHelper.DEVICE_PARAMETER,device_parameter);
        values.put(BplOximterdbHelper.LAST_ACTIVITY_USERS, date);

        values.put(BplOximterdbHelper.USE_TYPE, useType);
        return values;


    }


    public static String validatePressureForVoice(int systolic,int diabolic,Context context)
    {

        String bpCategory = null;

        if(systolic <=90){
            if(diabolic<=60)
            {
                bpCategory=context.getString(R.string.sound_low);
            }else if(diabolic>60 && diabolic <80)
            {

                bpCategory=context.getString(R.string.sound_desirable);
            }else if(diabolic >=80 && diabolic <85)
            {
                bpCategory=context.getString(R.string.sound_normal);

            }
            else if(diabolic >=85 && diabolic <90)
            {
                bpCategory=context.getString(R.string.sound_pre_hyper);

            }
            else if(diabolic >=90 && diabolic <100)
            {
                bpCategory=context.getString(R.string.sound_mild_hyper);

            }

            else if(diabolic >=100 && diabolic <110)
            {
                bpCategory=context.getString(R.string.sound_moderate_hyper);

            }
            else{
                bpCategory=context.getString(R.string.sound_severe_hypertension);

            }
        }
        else if(systolic <120 && systolic >90)
        {
            if(diabolic<=60)
            {
                bpCategory=context.getString(R.string.sound_desirable);
            }else if(diabolic>60 && diabolic <80)
            {

                bpCategory=context.getString(R.string.sound_desirable);
            }else if(diabolic >=80 && diabolic <85)
            {
                bpCategory=context.getString(R.string.sound_normal);

            }
            else if(diabolic >=85 && diabolic <90)
            {
                bpCategory=context.getString(R.string.sound_pre_hyper);

            }
            else if(diabolic >=90 && diabolic <100)
            {
                bpCategory=context.getString(R.string.sound_mild_hyper);

            }

            else if(diabolic >=100 && diabolic <110)
            {
                bpCategory=context.getString(R.string.sound_moderate_hyper);

            }
            else{
                bpCategory=context.getString(R.string.sound_severe_hypertension);

            }

        }

        else if(systolic >=120 && systolic <=129)
        {
            if(diabolic<=60)
            {
                bpCategory=context.getString(R.string.sound_normal);
            }else if(diabolic>60 && diabolic <80)
            {

                bpCategory=context.getString(R.string.sound_normal);
            }else if(diabolic >=80 && diabolic <85)
            {
                bpCategory=context.getString(R.string.sound_normal);

            }
            else if(diabolic >=85 && diabolic <90)
            {
                bpCategory=context.getString(R.string.sound_pre_hyper);

            }
            else if(diabolic >=90 && diabolic <100)
            {
                bpCategory=context.getString(R.string.sound_mild_hyper);

            }

            else if(diabolic >=100 && diabolic <110)
            {
                bpCategory=context.getString(R.string.sound_moderate_hyper);

            }
            else{
                bpCategory=context.getString(R.string.sound_severe_hypertension);

            }

        }


        else if(systolic >=130 && systolic <=139)
        {
            if(diabolic<=60)
            {
                bpCategory=context.getString(R.string.sound_pre_hyper);
            }else if(diabolic>60 && diabolic <80)
            {

                bpCategory=context.getString(R.string.sound_pre_hyper);
            }else if(diabolic >=80 && diabolic <85)
            {
                bpCategory=context.getString(R.string.sound_pre_hyper);

            }
            else if(diabolic >=85 && diabolic <90)
            {
                bpCategory=context.getString(R.string.sound_pre_hyper);

            }
            else if(diabolic >=90 && diabolic <100)
            {
                bpCategory=context.getString(R.string.sound_mild_hyper);

            }

            else if(diabolic >=100 && diabolic <110)
            {
                bpCategory=context.getString(R.string.sound_moderate_hyper);

            }
            else{
                bpCategory=context.getString(R.string.sound_severe_hypertension);

            }

        }


        else if(systolic >=140 && systolic <=159)
        {
            if(diabolic<=60)
            {
                bpCategory=context.getString(R.string.sound_mild_hyper);
            }else if(diabolic>60 && diabolic <80)
            {

                bpCategory=context.getString(R.string.sound_mild_hyper);
            }else if(diabolic >=80 && diabolic <85)
            {
                bpCategory=context.getString(R.string.sound_mild_hyper);

            }
            else if(diabolic >=85 && diabolic <90)
            {
                bpCategory=context.getString(R.string.sound_mild_hyper);

            }
            else if(diabolic >=90 && diabolic <100)
            {
                bpCategory=context.getString(R.string.sound_mild_hyper);

            }

            else if(diabolic >=100 && diabolic <110)
            {
                bpCategory=context.getString(R.string.sound_moderate_hyper);

            }
            else{
                bpCategory=context.getString(R.string.sound_severe_hypertension);

            }
        }


        else if(systolic >=160 && systolic <=179)
        {
            if(diabolic<=60)
            {
                bpCategory=context.getString(R.string.sound_moderate_hyper);
            }else if(diabolic>60 && diabolic <80)
            {

                bpCategory=context.getString(R.string.sound_moderate_hyper);
            }else if(diabolic >=80 && diabolic <85)
            {
                bpCategory=context.getString(R.string.sound_moderate_hyper);

            }
            else if(diabolic >=85 && diabolic <90)
            {
                bpCategory=context.getString(R.string.sound_moderate_hyper);

            }
            else if(diabolic >=90 && diabolic <100)
            {
                bpCategory=context.getString(R.string.sound_moderate_hyper);

            }

            else if(diabolic >=100 && diabolic <110)
            {
                bpCategory=context.getString(R.string.sound_moderate_hyper);

            }
            else{
                bpCategory=context.getString(R.string.sound_severe_hypertension);

            }
        }

        else if(systolic >=180)
        {
            bpCategory=context.getString(R.string.sound_severe_hypertension);
        }

        return  bpCategory;
    }



    public static String validateTypeBP(String systolicX,String diabolicX)
    {

        String typeBP="";
        int systolic;
        int diabolic;
        if(systolicX.contains("."))
        {
            systolic= KpaToMmhg(Float.parseFloat(systolicX));
        }else{
            systolic=Integer.parseInt(systolicX);
        }



        if(diabolicX.contains(".")){
            diabolic=KpaToMmhg(Float.parseFloat(diabolicX));
        }else{
            diabolic=Integer.parseInt(diabolicX);
        }
        if(systolic<90)
        {
            if(diabolic<=60)
            {
                typeBP=Constants.LOW_;
            }else if(diabolic>60 && diabolic <80)
            {
                typeBP=Constants.DESIRABLE;
            }else if(diabolic >=80 && diabolic <85)
            {
                typeBP=Constants.NORMAL;
            }
            else if(diabolic >=85 && diabolic <90)
            {
                typeBP=Constants.PRE_HYPERTENSION;
            }
            else if(diabolic >=90 && diabolic <100)
            {
                typeBP=Constants.MILD_HYPERTENSION;
            }

            else if(diabolic >=100 && diabolic <110)
            {
                typeBP=Constants.MODERATE_HYPERTENSION;
            }
            else{
                typeBP=Constants.SEVERE_HYPERTENSION;
            }
        }
        else if(systolic <=120 && systolic>=90)
        {
            typeBP= Constants.DESIRABLE;

            if(diabolic<=60)
            {
                typeBP=Constants.DESIRABLE;
            }else if(diabolic>60 && diabolic <80)
            {
                typeBP=Constants.DESIRABLE;
            }else if(diabolic >=80 && diabolic <85)
            {
                typeBP=Constants.NORMAL;
            }

            else if(diabolic >=85 && diabolic <90)
            {
                typeBP=Constants.PRE_HYPERTENSION;
            }

            else if(diabolic >=90 && diabolic <100)
            {
                typeBP=Constants.PRE_HYPERTENSION;
            }

            else if(diabolic >=100 && diabolic <110)
            {
                typeBP=Constants.MODERATE_HYPERTENSION;
            }
            else{
                typeBP=Constants.SEVERE_HYPERTENSION;
            }
        }else if(systolic >120 && systolic <=129)
        {
            typeBP= Constants.NORMAL;
            if(diabolic<=60)
            {
                typeBP=Constants.NORMAL;
            }else if(diabolic>60 && diabolic <80)
            {
                typeBP=Constants.NORMAL;
            }else if(diabolic >=80 && diabolic <85)
            {
                typeBP=Constants.NORMAL;
            }

            else if(diabolic >=85 && diabolic <90)
            {
                typeBP=Constants.PRE_HYPERTENSION;
            }

            else if(diabolic >=90 && diabolic <100)
            {
                typeBP=Constants.MILD_HYPERTENSION;
            }

            else if(diabolic >=100 && diabolic <110)
            {
                typeBP=Constants.MODERATE_HYPERTENSION;
            }
            else{
                typeBP=Constants.SEVERE_HYPERTENSION;
            }

        }else if(systolic >=130 && systolic <=139)
        {
            typeBP= Constants.PRE_HYPERTENSION;
            if(diabolic<=60)
            {
                typeBP=Constants.PRE_HYPERTENSION;
            }else if(diabolic>60 && diabolic <80)
            {
                typeBP=Constants.PRE_HYPERTENSION;
            }else if(diabolic >=80 && diabolic <85)
            {
                typeBP=Constants.PRE_HYPERTENSION;
            }

            else if(diabolic >=85 && diabolic <90)
            {
                typeBP=Constants.PRE_HYPERTENSION;
            }

            else if(diabolic >=90 && diabolic <100)
            {
                typeBP=Constants.MILD_HYPERTENSION;
            }

            else if(diabolic >=100 && diabolic <110)
            {
                typeBP=Constants.MODERATE_HYPERTENSION;
            }
            else{
                typeBP=Constants.SEVERE_HYPERTENSION;
            }
        }else if(systolic >=140 && systolic <=159)
        {
            typeBP= Constants.MILD_HYPERTENSION;

            if(diabolic<=60)
            {
                typeBP=Constants.MILD_HYPERTENSION;
            }else if(diabolic>60 && diabolic <80)
            {
                typeBP=Constants.MILD_HYPERTENSION;
            }else if(diabolic >=80 && diabolic <85)
            {
                typeBP=Constants.MILD_HYPERTENSION;
            }

            else if(diabolic >=85 && diabolic <90)
            {
                typeBP=Constants.MILD_HYPERTENSION;
            }

            else if(diabolic >=90 && diabolic <100)
            {
                typeBP=Constants.MILD_HYPERTENSION;
            }

            else if(diabolic >=100 && diabolic <110)
            {
                typeBP=Constants.MODERATE_HYPERTENSION;
            }
            else{
                typeBP=Constants.SEVERE_HYPERTENSION;
            }
        }else if(systolic >=160 && systolic <=179)
        {
            typeBP= Constants.HYPERTENSION;
            if(diabolic<=60)
            {
                typeBP=Constants.MODERATE_HYPERTENSION;
            }else if(diabolic>60 && diabolic <80)
            {
                typeBP=Constants.MODERATE_HYPERTENSION;
            }else if(diabolic >=80 && diabolic <85)
            {
                typeBP=Constants.MODERATE_HYPERTENSION;
            }

            else if(diabolic >=85 && diabolic <90)
            {
                typeBP=Constants.MODERATE_HYPERTENSION;
            }

            else if(diabolic >=90 && diabolic <100)
            {
                typeBP=Constants.MODERATE_HYPERTENSION;
            }

            else if(diabolic >=100 && diabolic <110)
            {
                typeBP=Constants.MODERATE_HYPERTENSION;
            }
            else{
                typeBP=Constants.SEVERE_HYPERTENSION;
            }
        }else if(systolic >=180)
        {

            typeBP= Constants.SEVERE_HYPERTENSION;

        }

        return  typeBP;
    }


    public static int KpaToMmhg(float kpa) {
        float tempUnit = 7.5006168f;
        float tempmmhg = tempUnit * kpa;
        BigDecimal b = new BigDecimal(tempmmhg);
        int mmhg = b.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
        return mmhg;
    }


    @SuppressLint("SimpleDateFormat")
    public static List<String> getDaysBetweenDates(String  start_date, String end_date)
    {

        List<String> dates_list_=new ArrayList<>();
        Date startdate=null;
        Date enddate=null;

        SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");

        try {
            startdate=sdf.parse(start_date);
            enddate=sdf.parse(end_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Calendar start = Calendar.getInstance();
        start.setTime(startdate);
        Calendar end = Calendar.getInstance();
        end.setTime(enddate);
        end.add(Calendar.DAY_OF_YEAR, 1); //Add 1 day to endDate to make sure endDate is included into the final list
        DateFormat df=new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat dfx=new SimpleDateFormat("dd-MM-yyyy");
        while (start.before(end)) {
            String str=df.format(start.getTime());
            dates_list_.add(str);
            Date d=null;
            logger.Logger.log(logger.Level.DEBUG, TestReportMonthFragment.class.getSimpleName(),"str="+str);
            try {
                d=dfx.parse(str);
                start.add(Calendar.DAY_OF_YEAR, 1);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        logger.Logger.log(logger.Level.DEBUG,TestReportDayFragment.class.getSimpleName(),"List of dates="+dates_list_);
        return dates_list_;
    }


    public static  ContentValues content_values_biolight_bp_monitor(String username,String user_name_ ,String systolicPressure, String diastolicPressure,
                                                            String pulsePerMin, String testingTime,
                                                                    String typeBP, String comment,String macid,
                                                                    String sno,String useType) {

        ContentValues values = new ContentValues();

        values.put(BplOximterdbHelper.USER_NAME, username);
        values.put(BplOximterdbHelper.USER_NAME_, user_name_);
        values.put(BplOximterdbHelper.SYS,systolicPressure);
        values.put(BplOximterdbHelper.DIA,diastolicPressure);
        values.put(BplOximterdbHelper.PUL,pulsePerMin);
        values.put(BplOximterdbHelper.TESTING_TIME_BIO_LIGHT,testingTime);
        values.put(BplOximterdbHelper.TYPE_BP,typeBP);
        values.put(BplOximterdbHelper.COMMENT,comment);
        values.put(BplOximterdbHelper.BIOLIGHT_MACID,macid);
        values.put(BplOximterdbHelper.BIOLIGHT_SERIAL_N0,sno);

        values.put(BplOximterdbHelper.BIOLIGHT_USE_TYPE,useType);

        return values;
    }



    public static void navActivity(Context context1, Class cl)
    {

        ActivityOptions options;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            options = ActivityOptions.makeSceneTransitionAnimation((Activity) context1);
            Intent intent=new Intent(context1,cl);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context1.startActivity(intent,options.toBundle());

        } else {
            Intent intent=new Intent(context1,cl);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context1.startActivity(intent);
        }
    }
}
