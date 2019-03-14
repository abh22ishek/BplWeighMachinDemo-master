package cannyscale;

public class CannyAlgorithms {



    public static String bodyFatMale(float bmi, int age , int imp){

        String bodyFat;


        double fat=(0.000295*imp+1.54)*bmi+0.075219*age-24.8;

        bodyFat=String.valueOf(fat);

        return  bodyFat;

    }


    public static String bodyFatFeMale(float bmi, int age , int imp){

        String bodyFat;


        double fat=(0.000295*imp+1.54)*bmi-0.015*age-8;

        bodyFat=String.valueOf(fat);

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

        return  visceralFat;

    }




    public static String bodyWaterMale(float  bodyFat, int age){    // TBW

        double fat=(100-bodyFat)*(0.82-0.002*age)-8.3;
        String  bodyWater=String.valueOf(fat);

        return  bodyWater;

    }




    public static String bodyWaterFeMale(float  bodyFat, int age){    // TBW

        double water=(100-bodyFat)*(0.77-0.002*age)-5.3;
        String  bodyWater=String.valueOf(water);

        return  bodyWater;

    }



    public static String muscleMassMale(int height,int imp,  float weight, int age){    // TBW

        double muscle=(0.2333*height*height/imp+6.142)/weight*100-0.15*age+13.15;

        String  muscleMass=String.valueOf(muscle);

        return  muscleMass;

    }



    public static String muscleMassFeMale(int height,int imp,  float weight, int age){    // TBW

        double muscle=(0.2300*height*height/imp+1.66)/weight*100-0.15*age+9.2;
        String  muscleMass=String.valueOf(muscle);

        return  muscleMass;

    }




    public static String boneMassMale(float fat,  float weight){    // TBW

        double muscle=((1-fat/100)*0.93*weight+0.3 )*(0.052-0.357);
        String  boneMass=String.valueOf(muscle);

        return  boneMass;

    }


    public static String boneMassFeMale(float fat,  float weight){    // TBW

        double muscle=((1-fat/100)*0.93*weight+0.3 )*(0.0972-1.83);
        String  boneMass=String.valueOf(muscle);

        return  boneMass;

    }
}
