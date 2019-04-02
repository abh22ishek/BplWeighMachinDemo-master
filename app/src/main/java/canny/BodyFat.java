package canny;

public class BodyFat {

    static {
        System.loadLibrary("BodyFat"); //libBodyFat.so
    }
    public native static String getVersion();
    public native static String getAuthToken();
    /*
    1.sex:  if female,sex=ture else sex=flase
    2.imp:  the impedance of body, get the impedance from the device
    3. althlevel:  let althlevel=0, keep default value!
    4,age: the user age
    5,cm: the user height in cm
    6,wtkg: the user weight in kg
     */
    /*public native static float getFat(boolean sex,float age,float cm,float wtkg,float imp,int althlevel);
    public native static float getTbw(boolean sex,float age,float cm,float wtkg,float imp,int althlevel);
    public native static float getMus(boolean sex,float age,float cm,float wtkg,float imp,int althlevel);
    public native static float getBone(boolean sex,float age,float cm,float wtkg,float imp,int althlevel);
    //basal metabolism rate BMR=Kcal
    public native static float getKcal(boolean sex,float age,float cm,float wtkg,float imp,int althlevel);
    //visceral fat
    public native static float getVfat(boolean sex,float age,float cm,float wtkg,float imp,int althlevel);
    //body age
    public native static float getBage(boolean sex,float age,float cm,float wtkg,float imp,int althlevel);
    public native static float getBMI(boolean sex,float age,float cm,float wtkg,float imp,int althlevel);
    public native static float getProtein(boolean sex,float age,float cm,float wtkg,float imp,int althlevel);

    public native static float getWeightWithoutFat(boolean sex,float age,float cm,float wtkg,float imp,int althlevel);*/
    public native static float Java_cannyscale_BodyFat_Java_1com_1canny_1xue_1bialib_1BodyFat_1getBestWeight(boolean sex,float age,float cm,float wtkg,float imp,int althlevel);

    //evaluate the obese rate
    // obeserate=(actual weight - best weight)/best weight
    /*
    obeserate <10.0 healthy
    obeserate<20.0 over weight
    obeserate<30.0 mildly
    obeserate< 50.0 moderate
    obeserate>50 severe
    */
    public native static float getObeseRate(boolean sex,float age,float cm,float wtkg,float imp,int althlevel);
    //evaluate body score, total score is 100
    public native static float getScore(boolean sex,float age,float cm,float wtkg,float imp,int althlevel);
    //evaluate the body shape, 5 type body shape:
    /*
    <0.5 Lean type
    0.5<= x <1.5 Normal Type
    1.5<= x <2.5 over weight type
    2.5<= x <3.5 over fat
    3.5<= x      Obese
     */
    public native static float getBodyShape(boolean sex,float age,float cm,float wtkg,float imp,int althlevel);
}
