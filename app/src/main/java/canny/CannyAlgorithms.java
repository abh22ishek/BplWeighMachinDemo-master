package canny;

import java.util.*;

import logger.*;

public class CannyAlgorithms {




    public static String bodyFatMale(float bmi, int age , float imp){

        String bodyFat;

        double fat=((0.000295*imp+1.54)*bmi+(0.075219*age))-24.8;

        bodyFat=String.valueOf(fat);

        if(bodyFat.length()>4){
            bodyFat=bodyFat.substring(0,4);
        }

        Logger.log(Level.DEBUG,"Body Fat Male is ",bodyFat);
        return  bodyFat;
    }


    public static String bodyFatFeMale(float bmi, int age , float imp){

        String bodyFat;


        double fat=(0.000295*imp+1.54)*bmi-0.015*age-8;

        bodyFat=String.valueOf(fat);

        if(bodyFat.length()>4){
            bodyFat=bodyFat.substring(0,4);
        }
        Logger.log(Level.DEBUG,"Body Fat FEMALE is ",bodyFat);
        return  bodyFat;

    }




    public static String visceralFat(float bmi,int age){

        String visceralFat;

        float k1=0,k2=0;

        if(bmi >0 && bmi< 18.8f){
            k1=0.1f;
            k2=1;
        }else if(bmi >18.8 && bmi <25.9){

            k1=0.1f;
            k2=3.2f;
        }
        else if(bmi >25.9 && bmi <33){

            k1=0.1f;
            k2=7.2f;
        }
        else if(bmi >33){
            k1=0.18f;
            k2=6f;
        }


        visceralFat= String.valueOf(k1*age+k2);
        Logger.log(Level.DEBUG,"Visceral Fat  is ",visceralFat);

        Locale locale=  Locale.getDefault();
      // String vis = String.format(locale,"%.1f",visceralFat);
        return  visceralFat;

    }




    public static String bodyWaterMale(float  bodyFat, int age){    // TBW

        double fat=(100-bodyFat)*(0.82-0.002*age)-8.3;
        String  bodyWater=String.valueOf(fat);
        String mTBW;
        if(bodyWater.length()>4){
            mTBW= new StringBuilder().append(bodyWater.substring(0, 4)).toString();

        }else{
            mTBW= new StringBuilder().append(bodyWater).toString();

        }
        return  mTBW;

    }




    public static String bodyWaterFeMale(float  bodyFat, int age){    // TBW

        double water=(100-bodyFat)*(0.77-0.002*age)-5.3;
        String mTBW;
        String  bodyWater=String.valueOf(water);
        if(bodyWater.length()>4){
             mTBW= new StringBuilder().append(bodyWater.substring(0, 4)).toString();

        }else{
             mTBW= new StringBuilder().append(bodyWater).toString();

        }

        return  mTBW;

    }



    public static String muscleMassMale(int height,int imp,  float weight, int age){    // TBW


        double muscle=(0.2333*height*height/imp+6.142)/weight*100-0.15*age+13.15;

        Logger.log(Level.DEBUG,"Muscle mass imp , wt ,age =",imp +" "+age+" "+height);
        String  muscleMass=String.valueOf(muscle);

        return  muscleMass;

    }



    public static String muscleMassFeMale(int height,int imp,  float weight, int age){    // TBW

        double muscle=(0.2333*height*height/imp+1.66)/weight*100-0.15*age+9.2;
        String  muscleMass=String.valueOf(muscle);

        return  muscleMass;

    }


    public static String boneMassMale(float fat,  float weight){    // TBW

        Logger.log(Level.DEBUG,"body Fat and Weight =",fat+"  "+weight);
        double muscle=((1-fat/100)*0.93*weight+0.3 )*0.052-0.357;
        String  boneMass=String.valueOf(muscle);

        if(boneMass.length()>4){
            boneMass=boneMass.substring(0,4);
        }
        Logger.log(Level.DEBUG,"Bone mass male is =",boneMass);
        return  boneMass;

    }


    public static String boneMassFeMale(float fat,  float weight){    // TBW

        double muscle=((1-fat/100)*0.93*weight+0.3 )*0.0972-1.83;
        String  boneMass=String.valueOf(muscle);
        Logger.log(Level.DEBUG,"Bone mass female is =",boneMass);
        if(boneMass.length()>4){
            boneMass=boneMass.substring(0,4);
        }

        return  boneMass;

    }




    public static String calculate_bmi(float weight, float height) {

        Logger.log(Level.DEBUG,"Get weight and height value =",weight+" "+height);

        float  heightS=((height/100)*(height/100));
        Logger.log(Level.DEBUG,"Get HeightS ="," "+heightS);
        float bmi=weight/heightS;
        Logger.log(Level.DEBUG,"BMI Algorithm=",""+bmi);
        String s="";


        try{
          Locale locale=  Locale.getDefault();
             s = String.format(locale,"%.1f",bmi);

        }catch (Exception e)
        {
            e.printStackTrace();
        }


        Logger.log(Level.DEBUG,"Get BMI value =",s);
        return s;
    }


    public static String MeatabolismMale(float weight, int age)
    {
        float k=0;
        if(age <30){

            k=24;
        }else{
            k=22.3f;
        }
        float kcalMetabolism = weight *k;

        return String.valueOf(kcalMetabolism);
    }

    public static String MeatabolismFeMale(float weight, int age)
    {
        float k=0;
        if(age <30){

            k=23.6f;
        }else{
            k=21.7f;
        }
        float kcalMetabolism = weight *k;

        return String.valueOf(kcalMetabolism);
    }
}
