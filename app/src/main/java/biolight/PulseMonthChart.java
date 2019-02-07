package biolight;


import android.annotation.*;
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

public class PulseMonthChart extends View{

    int pixels_per_unit=0;


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

    int mX=0;
    float maxY=0;

    List<String> mPoints;

    List<String> daysOfMonth;
    final String TAG=PulseMonthChart.class.getSimpleName();


    Paint dashpaint;
    Path mPath;

    public PulseMonthChart(Context context) {
        super(context);
        init();
    }

    public PulseMonthChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init()
    {
        int density=getResources().getDisplayMetrics().densityDpi;
        pixels_per_unit= (int) (density/5f);
        Logger.log(Level.DEBUG,TAG,"pixel per unit="+pixels_per_unit);
        mPath=new Path();
        dashpaint=new Paint();
        dashpaint.setPathEffect(new DashPathEffect(new float[] { 2,10 }, 0));
    }


    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        if(mPaint==null){
            mPaint=new Paint();
        }


        if(mTextPaint==null){
            mTextPaint=new TextPaint();
        }

        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);

        mPaint.setStrokeWidth(1);

        stopY=getHeight();

        dashpaint.setStyle(Paint.Style.STROKE);
        dashpaint.setAntiAlias(true);


        int polar=pixels_per_unit/2;
        for(int i=0;i<6;i++) {

            mPath.moveTo(pixels_per_unit,polar);
            mPath.lineTo(getWidth(),polar);
            canvas.drawPath(mPath,dashpaint);
            // canvas.drawLine(pixels_per_unit,polar,getWidth(),polar, dashpaint);

            polar=polar+pixels_per_unit;
        }



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

        nStartX=0;
        nStartY=pixels_per_unit;


        maxY=120;
        // scaling y axis
        final float height_scale=((float)getHeight()-pixels_per_unit)/maxY;



        // plot the bar

        mX=pixels_per_unit;
        for(int i=0;i<mPoints.size();i++){

            mLeft=mX;
            mTop=(maxY-Integer.parseInt(mPoints.get(i)))*height_scale;

            mTextPaint.setStrokeWidth(15f);
            mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.white));
            if(!mPoints.get(i).equalsIgnoreCase("0")){
                canvas.drawPoint(mX,mTop,mTextPaint);
                String pulse= String.valueOf(mPoints.get(i));


                    pulseX = new DecimalFormat("0.####").format(Double.parseDouble(pulse));




                //    canvas.rotate(-45,mLeft,mTop);
                mPaint.setStrokeWidth(10);
                mPaint.setTextSize(25);

                if(isValues){
                    canvas.drawText("  [ "+ pulseX+" ]",mLeft,mTop,mPaint);

                }
            }
            mX=mX+2*pixels_per_unit;

        }

    }
    String pulseX=null;

    public List<String> setPulsePlotPoints(List<String> points){

        mPoints=points;
        Logger.log(Level.DEBUG,TAG,"**Day record size**="+points.size());
        return mPoints;
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


    public void showValues(boolean val)
    {
        isValues=val;
        invalidate();

    }
    boolean isValues;

}

