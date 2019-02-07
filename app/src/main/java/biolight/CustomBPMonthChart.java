package biolight;

import android.content.*;
import android.graphics.*;
import android.support.v4.content.*;
import android.text.*;
import android.util.*;
import android.view.*;

import java.text.*;
import java.util.*;

import logger.*;
import test.bpl.com.bplscreens.*;

public class CustomBPMonthChart extends View {

    int pixels_per_unit=0;
    final String TAG= CustomBPDayChart.class.getSimpleName();


    Paint mPaint;
    Paint mTextPaint;



    int startX=0;
    int startY=0;

    int stopX=0;
    int stopY=0;



    int nStartX=0;
    int nStartY=0;



    float mLeft=0;
    float mTop=0;

    float mRight=0;
    float mBottom=0;



    int mX=0;
    float maxY=0;


    List<String> daysOfMonth;
    private List<String> systolicList;
    private List<String> diabolicList;

    int maximum;
    int minimum;

    Paint dashpaint;
    Path mPath;
    BpListner bpListner;

    public CustomBPMonthChart(Context context) {
        super(context);
        init();
        bpListner= (BpListner) context;
    }

    public CustomBPMonthChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomBPMonthChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }



    private void init()
    {
        int density=getResources().getDisplayMetrics().densityDpi;
        pixels_per_unit= (int) (density/5f);
        Logger.log(Level.DEBUG,TAG,"pixel per unit="+pixels_per_unit);

    }


    float heighRatio;

    boolean mDraw;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        if(mPaint==null){
            mPaint=new Paint();
        }


        if(mTextPaint==null){
            mTextPaint=new TextPaint();
        }

        if(dashpaint==null){
            dashpaint=new Paint();
            dashpaint.setStyle(Paint.Style.STROKE);
            dashpaint.setPathEffect(new DashPathEffect(new float[] { 2,10 }, 0));
        }


        if(mColor_==null){
            mColor_=new ArrayList<>();

        }


        if(mPath==null){
            mPath=new Path();
        }
        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(1);


        stopY=getHeight();


        // draw vertical grids
      /*  for(int i=0;i<getWidth();i+=pixels_per_unit){

            canvas.drawLine(startX,startY,stopX,stopY,mPaint);
            startX=startX+pixels_per_unit;
            stopX=startX;

        }*/



        // draw horizontal grids




        stopX=getWidth();
        startX=pixels_per_unit;
        startY=0;
        stopY=0;
        for(int i=0;i<getHeight();i+=pixels_per_unit){

            canvas.drawLine(startX,startY,stopX,stopY,mPaint);
            startY=startY+pixels_per_unit;
            stopY=stopY+pixels_per_unit;

        }



        nStartY=getHeight()-pixels_per_unit;
        nStartX=pixels_per_unit;


        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(25f);


        // draw text for horizontal labels
        for(int i=0;i<daysOfMonth.size();i++)
        {
            canvas.drawText(daysOfMonth.get(i).substring(0,5),
                    nStartX-10f,nStartY+20f,mTextPaint);
            nStartX=nStartX+2*pixels_per_unit;
        }

        nStartX=10;
        nStartY=0;

        int polar=pixels_per_unit/2;
        for(int i=0;i<6;i++) {

            mPath.moveTo(pixels_per_unit,polar);
            mPath.lineTo(getWidth(),polar);
            canvas.drawPath(mPath,dashpaint);
            // canvas.drawLine(pixels_per_unit,polar,getWidth(),polar, dashpaint);

            polar=polar+pixels_per_unit;
        }

        nStartX=0;
        nStartY=pixels_per_unit;


        maxY=160;

        if(maximum>140 && maximum <160){
            maxY=160;
            heighRatio=120;
        }else if(maximum>=160 && maximum <210){
            maxY=120;
            heighRatio=170;
        }

        else if(maximum>=130 && maximum<=140 ){

            maxY=140;
            heighRatio=120;

        }

        else {
            maxY=130;
            heighRatio=120;
        }

        // scaling y axis
        final float height_scale=((float)getHeight()-pixels_per_unit)/maxY;

        // plot the bar

        mX=pixels_per_unit;

        for(int i=0;i<daysOfMonth.size();i++){


            mLeft=mX;
            mTop=(maxY-Integer.parseInt(systolicList.get(i)))*height_scale;

            mRight=mX;
            mBottom=(maxY-Integer.parseInt(diabolicList.get(i)))*height_scale;


            mTextPaint.setStrokeWidth(40);

            checkSysytolicPressure(i,canvas);
            canvas.drawLine(mLeft,mTop,mRight,mBottom,mTextPaint);


            mX=mX+2*pixels_per_unit;

            // draw bar
        }


        mDraw=true;
    }

    private void checkSysytolicPressure(int i,Canvas canvas)
    {
        if(systolicList.get(i).equals("0") || diabolicList.get(i).equals("0"))
        {
            mColor_.add("0");
            return;
        }

        if(Integer.parseInt(systolicList.get(i))<=90)
        {
            if(Integer.parseInt(diabolicList.get(i))<=60)
            {
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.low));
                mColor="blue";

            }else if(Integer.parseInt(diabolicList.get(i))>60 && Integer.parseInt(diabolicList.get(i))<80)
            {
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.text_color));
                mColor="green";

            }else if(Integer.parseInt(diabolicList.get(i)) >=80 && Integer.parseInt(diabolicList.get(i))<85)
            {
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.text_color));
                mColor="green";
            }
            else if(Integer.parseInt(diabolicList.get(i)) >=85 && Integer.parseInt(diabolicList.get(i)) <90)
            {
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.high_normal));
                mColor="green";
            }
            else if(Integer.parseInt(diabolicList.get(i)) >=90 && Integer.parseInt(diabolicList.get(i)) <100)
            {
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.severe_hypertension_color));
                mColor="red";
            }

            else if(Integer.parseInt(diabolicList.get(i)) >=100 && Integer.parseInt(diabolicList.get(i)) <110)
            {
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.severe_hypertension_color));
                mColor="red";
            }
            else{
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.severe_hypertension_color));
                mColor="red";
            }
        }
        else if(Integer.parseInt(systolicList.get(i))>90 && Integer.parseInt(systolicList.get(i))<120)
        {
            if(Integer.parseInt(diabolicList.get(i))<=60)
            {
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.text_color));
                mColor="green";

            }else if(Integer.parseInt(diabolicList.get(i))>60 && Integer.parseInt(diabolicList.get(i)) <80)
            {
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.text_color));
                mColor="green";

            }else if(Integer.parseInt(diabolicList.get(i)) >=80 && Integer.parseInt(diabolicList.get(i)) <85)
            {
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.text_color));
                mColor="green";

            }
            else if(Integer.parseInt(diabolicList.get(i)) >=85 && Integer.parseInt(diabolicList.get(i))<90)
            {
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.high_normal));
                mColor="yellow";

            }
            else if(Integer.parseInt(diabolicList.get(i)) >=90 && Integer.parseInt(diabolicList.get(i)) <100)
            {
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.severe_hypertension_color));
                mColor="red";
            }

            else if(Integer.parseInt(diabolicList.get(i)) >=100 && Integer.parseInt(diabolicList.get(i)) <110)
            {
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.severe_hypertension_color));
                mColor="red";
            }
            else{
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.severe_hypertension_color));
                mColor="red";
            }

        }

        else if(Integer.parseInt(systolicList.get(i))>=120 && Integer.parseInt(systolicList.get(i))<=129){
            if(Integer.parseInt(diabolicList.get(i)) <=60)
            {
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.text_color));
                mColor="green";

            }else if(Integer.parseInt(diabolicList.get(i)) >60 && Integer.parseInt(diabolicList.get(i))  <80)
            {
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.text_color));
                mColor="green";

            }else if(Integer.parseInt(diabolicList.get(i))  >=80 && Integer.parseInt(diabolicList.get(i))  <85)
            {
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.text_color));
                mColor="green";

            }

            else if(Integer.parseInt(diabolicList.get(i))  >=85 && Integer.parseInt(diabolicList.get(i))  <90)
            {
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.high_normal));
                mColor="yellow";

            }

            else if(Integer.parseInt(diabolicList.get(i))  >=90 && Integer.parseInt(diabolicList.get(i))  <100)
            {
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.severe_hypertension_color));
                mColor="red";
            }

            else if(Integer.parseInt(diabolicList.get(i))  >=100 && Integer.parseInt(diabolicList.get(i))  <110)
            {
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.severe_hypertension_color));
                mColor="red";
            }
            else{
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.severe_hypertension_color));
                mColor="red";
            }
        }
        else if(Integer.parseInt(systolicList.get(i))>=130 && Integer.parseInt(systolicList.get(i))<=139)
        {
            if(Integer.parseInt(diabolicList.get(i))<=60)
            {
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.high_normal));
                mColor="yellow";


            }else if(Integer.parseInt(diabolicList.get(i))>60 && Integer.parseInt(diabolicList.get(i)) <80)
            {
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.high_normal));
                mColor="yellow";

            }else if(Integer.parseInt(diabolicList.get(i)) >=80 && Integer.parseInt(diabolicList.get(i)) <85)
            {
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.high_normal));
                mColor="yellow";
            }
            else if(Integer.parseInt(diabolicList.get(i)) >=85 && Integer.parseInt(diabolicList.get(i)) <90)
            {
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.high_normal));
                mColor="yellow";

            }
            else if(Integer.parseInt(diabolicList.get(i)) >=90 && Integer.parseInt(diabolicList.get(i)) <100)
            {
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.severe_hypertension_color));
                mColor="red";
            }

            else if(Integer.parseInt(diabolicList.get(i)) >=100 && Integer.parseInt(diabolicList.get(i)) <110)
            {
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.severe_hypertension_color));
                mColor="red";
            }
            else{
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.severe_hypertension_color));
                mColor="red";
            }
        }else if(Integer.parseInt(systolicList.get(i))>=140 && Integer.parseInt(systolicList.get(i))<159)
        {
            mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.severe_hypertension_color));
            mColor="red";
        }
        else if(Integer.parseInt(systolicList.get(i))>=160&& Integer.parseInt(systolicList.get(i))<180)
        {
            mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.severe_hypertension_color));
            mColor="red";
        }

        else{
            mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.severe_hypertension_color));
            mColor="red";
        }


        String sys= String.valueOf(Integer.parseInt(systolicList.get(i)));

        String sysX,diaX;
        sysX = new DecimalFormat("0.####").format(Double.parseDouble(sys));
        String dia=String.valueOf(diabolicList.get(i));
        diaX=new DecimalFormat("0.####").format(Double.parseDouble(dia));



        mColor_.add(mColor);

        // canvas.save();
        //canvas.rotate(-10,mLeft,mTop);
        mPaint.setTextSize(25);
        mPaint.setStrokeWidth(10);

        if(!sysX.equalsIgnoreCase("0") && !diaX.equalsIgnoreCase("0")){
            if(isValues){
                // canvas.drawText(sysX+"/"+diaX,mLeft,mTop,mPaint);
                canvas.drawText(sysX+"/"+diaX,mLeft-35,mTop-2,mPaint);


            }

        }
        //canvas.restore();


    }
    List<String> mColor_;
    String mColor="";
    public List<String> setSystolicPlotPoints(List<String> points){

        systolicList=points;
        maximum=largest(systolicList);
        Logger.log(Level.DEBUG,TAG,"**Systolic record size**="+points.size());
        return systolicList;
    }

    public List<String> setDiabolicPlotPoints(List<String> points){

        diabolicList=points;
        minimum= Smallest(diabolicList);
        Logger.log(Level.DEBUG,TAG,"**Diabolic record size**="+points.size());
        return diabolicList;
    }






    public List<String> setHorizontalLabels(List<String> daysOfMonth_){

        daysOfMonth=daysOfMonth_;
        Logger.log(Level.DEBUG,TAG,"**Day record size**="+daysOfMonth.size());
        return daysOfMonth;
    }





    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(daysOfMonth.size()<7)
            setMeasuredDimension(14*pixels_per_unit,7*pixels_per_unit);
        else
            setMeasuredDimension(2*(daysOfMonth.size()*pixels_per_unit),7*pixels_per_unit);
    }



    private int largest(List<String> sysoliclist)
    {
        int max= Integer.parseInt(sysoliclist.get(0));
        for(String b: sysoliclist)
        {
            if(Integer.parseInt(b)>max)
            {
                max= Integer.parseInt(b);
            }
        }

        return max;
    }


    public void showValues(boolean val)
    {
        isValues=val;
        invalidate();

    }
    boolean isValues;

    private int Smallest(List<String> diabolicList)
    {

        int min= Integer.parseInt(diabolicList.get(0));
        for(String b: diabolicList)
        {
            if(Integer.parseInt(b)<min)
            {
                min= Integer.parseInt(b);
            }
        }

        return min;
    }


    public List<String>  getColor() {

        return mColor_;
    }
}
