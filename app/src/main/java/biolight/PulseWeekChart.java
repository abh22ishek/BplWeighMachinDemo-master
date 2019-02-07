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


public class PulseWeekChart extends View {


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

    List<String> dates;
    List<String> mPoints;

    Paint dashpaint;
    Path mPath;
    final String TAG=PulseWeekChart.class.getSimpleName();


    public PulseWeekChart(Context context) {
        super(context);
        init();
    }

    public PulseWeekChart(Context context, AttributeSet attrs) {
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
        mPaint.setStrokeWidth(1);
        mPaint.setAntiAlias(true);


        stopY=getHeight();

        dashpaint.setStyle(Paint.Style.STROKE);
        dashpaint.setAntiAlias(true);
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

        int polar=pixels_per_unit/2;
        for(int i=0;i<6;i++) {

            mPath.moveTo(pixels_per_unit,polar);
            mPath.lineTo(getWidth(),polar);
            canvas.drawPath(mPath,dashpaint);
            // canvas.drawLine(pixels_per_unit,polar,getWidth(),polar, dashpaint);

            polar=polar+pixels_per_unit;
        }

        // draw text for horizontal labels
       for(int i=0;i<dates.size();i++)
        {
            canvas.drawText(dates.get(i).substring(0,5), nStartX-10f,nStartY+20f,mTextPaint);
            nStartX=nStartX+2*pixels_per_unit;
        }
        nStartX=10;
        nStartY=0;


        nStartX=0;
        nStartY=pixels_per_unit;


        maxY=120;
        // scaling y axis
        final float height_scale=((float)getHeight()-pixels_per_unit)/maxY;

        // plot the points

        mX=pixels_per_unit;
        mPaint.setStrokeWidth(10);
        mPaint.setTextSize(25);
        for(int i=0;i<mPoints.size();i++){


            mLeft=mX;
            mTop=(maxY-Integer.parseInt(mPoints.get(i)))*height_scale;
            mTextPaint.setStrokeWidth(15f);
            mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.white));
            if(!mPoints.get(i).equalsIgnoreCase("0"))
            {
                canvas.drawPoint(mX,mTop,mTextPaint);
                String pulse= String.valueOf(mPoints.get(i));


                    pulseX = new DecimalFormat("0.####").format(Double.parseDouble(pulse));




                //    canvas.rotate(-45,mLeft,mTop);
                if(isValues){
                    canvas.drawText("  [ "+ pulseX+" ]",mLeft,mTop,mPaint);

                }
            }

            mX=mX+2*pixels_per_unit;

        }

    }
    String pulseX=null;

    public List<String> setPlotPoints(List<String> points){
        mPoints=points;
        Logger.log(Level.DEBUG,TAG,"**Day record size**="+mPoints.size());

        return mPoints;
    }

    public List<String> setHorizontalLabel(List<String> WeekDates){
        dates=WeekDates;
        Logger.log(Level.DEBUG,TAG,"**Day record size**="+dates.size());
        return dates;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(14 * pixels_per_unit, 7 * pixels_per_unit);

    }
    public void showValues(boolean val)
    {
        isValues=val;
        invalidate();

    }
    boolean isValues;

}
