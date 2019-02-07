package constantsP;


import android.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import logger.Level;
import logger.Logger;


public class DateTime {

    @SuppressLint("SimpleDateFormat")


// convert time from milli sec to hh mm ss
    public static String secondsTominutes(long timeInMillis){

        int hours = (int) ((timeInMillis / (1000 * 60 * 60)));
        int minutes = (int) ((timeInMillis / (1000 * 60)) % 60);
        int  seconds = (int) ((timeInMillis / 1000) % 60);

        if(hours==0)
        {
            return minutes+"m"+seconds+"s";
        }
        return hours+"h"+minutes+"m"+seconds+"s";
    }

    // convert time from milli sec to hh:mm:ss


    public static String secondsTominutesHours(long timeInMillis){

        int hours = (int) ((timeInMillis / (1000 * 60 * 60)));
        int minutes = (int) ((timeInMillis / (1000 * 60)) % 60);
        int  seconds = (int) ((timeInMillis / 1000) % 60);

        if(hours==0)
        {
            return minutes+":"+seconds;
        }
        return hours+":"+minutes+":"+seconds;
    }

    // get current date and time
    @SuppressLint("SimpleDateFormat")

    public  static String getDateTime()
    {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();
        return  df.format(date);
    }



    // get Current Date

    public  static String getCurrentDate()
    {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return  df.format(date);
    }

    // convert sec to hhmmss

    public  static double seconds_to_minutes_hours(double total_secs)
    {
        double time_str;
        int totalSecs= (int) (total_secs);
       int  hours = totalSecs / 3600;
        int minutes = (totalSecs % 3600) / 60;
       int  seconds = totalSecs % 60;

        String temp = String.format("%02d%02d%02d",hours, minutes, seconds);

        time_str=Double.parseDouble(temp);
        Logger.log(Level.INFO,"DATETIME","get double time str="+time_str);
        return  time_str;
    }


    public static double time_to_seconds(String time)
    {
        String[] units = time.split(":"); //will break the string up into an array
        int minutes = Integer.parseInt(units[0]); //first element
        int seconds = Integer.parseInt(units[1]); //second element
        int hours=Integer.parseInt(units[2]);
        int duration =60*60*hours +60*minutes + seconds; //add up our values

        return Double.parseDouble(String.valueOf(duration));

    }

    @SuppressLint("SimpleDateFormat")

    public static String digit_to_time(String output_string)
    {
        SimpleDateFormat formatFrom = new SimpleDateFormat("HHmmss");
        SimpleDateFormat formatTo = new SimpleDateFormat("HH:mm:ss");
       // SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

        String newDateString = null;
        Date d=null;
        try {
            String formattedString = String.format("%06d", Integer.parseInt(output_string));
            Date date = formatFrom.parse(formattedString);
            newDateString = formatTo.format(date);
           // d=formatter.parse(newDateString);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        Logger.log(Level.DEBUG,"DATE TIME","date"+newDateString);
        return newDateString;
    }

    @SuppressLint("SimpleDateFormat")

    public static  Date time_format( int secs)
    {
        Date d=null;
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        int hours = secs / 3600;
        int secondsLeft = secs - hours * 3600;
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft - minutes * 60;

        String formattedTime = "";
        if (hours < 10)
            formattedTime += "0";
        formattedTime += hours + ":";

        if (minutes < 10)
            formattedTime += "0";
        formattedTime += minutes + ":";

        if (seconds < 10)
            formattedTime += "0";
        formattedTime += seconds ;
        Logger.log(Level.DEBUG,"DATE TIME","formatted time="+formattedTime);
        try {
            d=formatter.parse(formattedTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Logger.log(Level.DEBUG,"DATE TIME","date="+d);
        return d;

    }

    //yyyy-mm-dd

    // get month from date
    @SuppressLint("SimpleDateFormat")

    public static String get_month(String input_date)
    {
        String month="";
        Date date=null;

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf_output=new SimpleDateFormat("MM");
        try {
            date = sdf.parse(input_date);
            month = sdf_output.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        Logger.log(Level.DEBUG,"DATE TIME","Return Month="+month);

        return month;
    }



    // Convert String to date
    @SuppressLint("SimpleDateFormat")

    public static Date convert_string_to_date(String input_date)
    {
        String month="";
        Date date=null;

        SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdf_output=new SimpleDateFormat("dd-MM-yyyy");
        try {
            date = sdf.parse(input_date);
           month = sdf_output.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        Logger.log(Level.DEBUG,"DATE TIME","Return date="+date);

        return date;
    }
}
