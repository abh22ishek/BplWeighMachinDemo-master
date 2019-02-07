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


public class PulseDayChart extends View {


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
    Path mPath;
    Paint dashpaint;

    List<BPMeasurementModel> mRecords;
    final String TAG= CustomBPDayChart.class.getSimpleName();

    public PulseDayChart(Context context) {
        super(context);
        init();
    }

    public PulseDayChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PulseDayChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }



    private void init()
    {
        int density=getResources().getDisplayMetrics().densityDpi;
        pixels_per_unit= (int) (density/5f);
        Logger.log(Level.DEBUG,TAG,"pixel per unit="+pixels_per_unit);
        mPath=new Path();
        dashpaint=new Paint();
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
        dashpaint.setPathEffect(new DashPathEffect(new float[] { 2,10 }, 0));

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
        for(int i=0;i<mRecords.size();i++)
        {
            canvas.drawText(mRecords.get(i).getMeasurementTime().substring(11,16),
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


        maxY=120;
        // scaling y axis
        final float height_scale=((float)getHeight()-pixels_per_unit)/maxY;



        // plot the points

        mX=pixels_per_unit;
        mTextPaint.setStrokeWidth(15f);
        mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.white));
        mPaint.setTextSize(25);
        mPaint.setStrokeWidth(10);

        for(int i=0;i<mRecords.size();i++){
           // canvas.save();
            mLeft=mX;
            mTop=(maxY-mRecords.get(i).getPulsePerMin())*height_scale;

            canvas.drawPoint(mX,mTop,mTextPaint);
            String pulse= String.valueOf(mRecords.get(i).getPulsePerMin());

            String pulseX;
            pulseX = new DecimalFormat("0.####").format(Double.parseDouble(pulse));


        //    canvas.rotate(-45,mLeft,mTop);
            if(isValues){
                canvas.drawText("  [ "+ pulseX+" ]",mLeft,mTop,mPaint);

            }

            mX=mX+2*pixels_per_unit;
          //  canvas.restore();


        }


    }



    public List<BPMeasurementModel> setHorizontalLabel(List<BPMeasurementModel> dateTime){

        mRecords=dateTime;
        Logger.log(Level.DEBUG,TAG,"**Day record size**="+mRecords.size());
        return mRecords;
    }




    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(mRecords.size()<7)
            setMeasuredDimension(14*pixels_per_unit,7*pixels_per_unit);
        else
            setMeasuredDimension(2*(mRecords.size()*pixels_per_unit),7*pixels_per_unit);
    }

    public void showValues(boolean val)
    {
        isValues=val;
        invalidate();

    }
    boolean isValues;

}
