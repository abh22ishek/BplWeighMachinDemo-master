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

class CustomBPDayChart extends View {
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

    List<BPMeasurementModel> mRecords;

    int maximum,minimum;
    Path mPath;
    float heighRatio;
    float[] intervals = new float[]{50.0f, 20.0f};
    Paint dashpaint;
    Paint paint;
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Logger.log(Level.DEBUG,TAG,"on AttachToWindow() gets called");

    }

    public CustomBPDayChart(Context context) {
        super(context);
        init();
    }

    public CustomBPDayChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomBPDayChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    Bitmap mBitmap;

    private void init()
    {
        int density=getResources().getDisplayMetrics().densityDpi;
        pixels_per_unit= (int) (density/5f);
        Logger.log(Level.DEBUG,TAG,"pixel per unit="+pixels_per_unit);
        mBitmap= BitmapFactory.decodeResource(getResources(),R.drawable.comment);
        mPath=new Path();
        paint=new Paint();
    }





    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        if(mPaint==null){
            mPaint=new Paint();
        }


        if(dashpaint==null){
            dashpaint=new Paint();
            dashpaint.setStyle(Paint.Style.STROKE);
            dashpaint.setPathEffect(new DashPathEffect(new float[] { 2,10 }, 0));
        }

        dashpaint.setColor(Color.BLACK);
        dashpaint.setAntiAlias(true);
        dashpaint.setStrokeWidth(1);


        if(mTextPaint==null){
            mTextPaint=new TextPaint();
        }

        if(mColor_==null){
            mColor_=new ArrayList<>();
        }
        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(1);




        stopY=getHeight();





        stopX=getWidth();
        startX=pixels_per_unit;
        startY=0;
        stopY=0;
        //  float phase = 0;

        for(int i=0;i<getHeight();i+=pixels_per_unit){

            canvas.drawLine(startX,startY,stopX,stopY,mPaint);


            startY=startY+pixels_per_unit;
            stopY=stopY+pixels_per_unit;



        }


        int polar=pixels_per_unit/2;

        for(int i=0;i<6;i++) {

            mPath.moveTo(pixels_per_unit,polar);
            mPath.lineTo(getWidth(),polar);
            canvas.drawPath(mPath,dashpaint);
            // canvas.drawLine(pixels_per_unit,polar,getWidth(),polar, dashpaint);

            polar=polar+pixels_per_unit;
        }

        nStartY=getHeight()-pixels_per_unit;
        nStartX=pixels_per_unit;


        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setAntiAlias(true);


        mTextPaint.setColor(Color.WHITE);
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
        final float height_scale=((float)getHeight()-pixels_per_unit)/heighRatio;



        // plot the bar

        mX=pixels_per_unit;
        mPaint.setTextSize(25);
        mPaint.setStrokeWidth(10);
        for(int i=0;i<mRecords.size();i++){


            mLeft=mX;
            mTop=(maxY-mRecords.get(i).getSysPressure())*height_scale;

            mRight=mX;
            mBottom=(maxY-mRecords.get(i).getDiabolicPressure())*height_scale;
            mTextPaint.setStrokeWidth(40);

            checkSysytolicPressure(i,canvas);



            canvas.drawLine(mLeft,mTop,mRight,mBottom,mTextPaint);
            mX=mX+2*pixels_per_unit;

            // draw bar

        }

    }


    private void checkSysytolicPressure(int i,Canvas canvas)
    {


        if(mRecords.get(i).getSysPressure()<=90)
        {
            if(mRecords.get(i).getDiabolicPressure()<=60)
            {
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.low));
                mColor="blue";

            }else if(mRecords.get(i).getDiabolicPressure()>60 && mRecords.get(i).getDiabolicPressure() <80)
            {
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.text_color));
                mColor="green";


            }else if(mRecords.get(i).getDiabolicPressure() >=80 && mRecords.get(i).getDiabolicPressure() <85)
            {
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.text_color));
                mColor="green";

            }
            else if(mRecords.get(i).getDiabolicPressure() >=85 && mRecords.get(i).getDiabolicPressure() <90)
            {
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.high_normal));
                mColor="yellow";

            }
            else if(mRecords.get(i).getDiabolicPressure() >=90 && mRecords.get(i).getDiabolicPressure() <100)
            {
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.severe_hypertension_color));
                mColor="red";

            }

            else if(mRecords.get(i).getDiabolicPressure() >=100 && mRecords.get(i).getDiabolicPressure() <110)
            {
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.severe_hypertension_color));
                mColor="red";

            }
            else{
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.severe_hypertension_color));
                mColor="red";

            }
        }
        else if(mRecords.get(i).getSysPressure()>90 && mRecords.get(i).getSysPressure()<=120)
        {
            if(mRecords.get(i).getDiabolicPressure()<=60)
            {
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.text_color));
                mColor="green";


            }else if(mRecords.get(i).getDiabolicPressure()>60 && mRecords.get(i).getDiabolicPressure() <80)
            {
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.text_color));
                mColor="green";


            }else if(mRecords.get(i).getDiabolicPressure() >=80 && mRecords.get(i).getDiabolicPressure() <85)
            {
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.text_color));
                mColor="green";


            }
            else if(mRecords.get(i).getDiabolicPressure() >=85 && mRecords.get(i).getDiabolicPressure() <90)
            {
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.high_normal));
                mColor="yellow";


            }
            else if(mRecords.get(i).getDiabolicPressure() >=90 && mRecords.get(i).getDiabolicPressure() <100)
            {
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.severe_hypertension_color));
                mColor="red";


            }

            else if(mRecords.get(i).getDiabolicPressure() >=100 && mRecords.get(i).getDiabolicPressure() <110)
            {
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.severe_hypertension_color));
                mColor="red";

            }
            else{
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.severe_hypertension_color));
                mColor="red";

            }

        }

        else if(mRecords.get(i).getSysPressure() >120 && mRecords.get(i).getSysPressure() <=129)
        {

            if( mRecords.get(i).getDiabolicPressure()<=60)
            {
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.text_color));
                mColor="green";

            }else if( mRecords.get(i).getDiabolicPressure()>60 &&  mRecords.get(i).getDiabolicPressure() <80)
            {
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.text_color));
                mColor="green";

            }else if( mRecords.get(i).getDiabolicPressure() >=80 &&  mRecords.get(i).getDiabolicPressure() <85)
            {
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.text_color));
                mColor="green";

            }

            else if( mRecords.get(i).getDiabolicPressure() >=85 &&  mRecords.get(i).getDiabolicPressure() <90)
            {
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.high_normal));
                mColor="yellow";

            }

            else if( mRecords.get(i).getDiabolicPressure() >=90 &&  mRecords.get(i).getDiabolicPressure() <100)
            {
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.severe_hypertension_color));
                mColor="red";

            }

            else if( mRecords.get(i).getDiabolicPressure() >=100 &&  mRecords.get(i).getDiabolicPressure() <110)
            {
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.severe_hypertension_color));
                mColor="red";

            }
            else{
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.severe_hypertension_color));
                mColor="red";

            }
        }
        else if(mRecords.get(i).getSysPressure()>=130 && mRecords.get(i).getSysPressure()<=139)
        {

            if( mRecords.get(i).getDiabolicPressure()<=60)
            {
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.high_normal));
                mColor="yellow";


            }else if( mRecords.get(i).getDiabolicPressure()>60 &&  mRecords.get(i).getDiabolicPressure() <80)
            {
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.high_normal));
                mColor="yellow";


            }else if( mRecords.get(i).getDiabolicPressure() >=80 &&  mRecords.get(i).getDiabolicPressure() <85)
            {
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.high_normal));
                mColor="yellow";


            }

            else if( mRecords.get(i).getDiabolicPressure() >=85 &&  mRecords.get(i).getDiabolicPressure() <90)
            {
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.high_normal));
                mColor="yellow";


            }

            else if( mRecords.get(i).getDiabolicPressure() >=90 &&  mRecords.get(i).getDiabolicPressure() <100)
            {
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.severe_hypertension_color));
                mColor="red";


            }

            else if( mRecords.get(i).getDiabolicPressure() >=100 &&  mRecords.get(i).getDiabolicPressure() <110)
            {
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.severe_hypertension_color));
                mColor="red";

            }
            else{
                mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.severe_hypertension_color));
                mColor="red";

            }
        }else if(mRecords.get(i).getSysPressure()>=140 && mRecords.get(i).getSysPressure()<159)
        {
            mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.severe_hypertension_color));
            mColor="red";

        }
        else if(mRecords.get(i).getSysPressure()>=160&& mRecords.get(i).getSysPressure()<180)
        {
            mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.severe_hypertension_color));
            mColor="red";

        }

        else{
            mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.severe_hypertension_color));
            mColor="red";

        }


        String sys= String.valueOf(mRecords.get(i).getSysPressure());

        String sysX,diaX;
        sysX = new DecimalFormat("0.####").format(Double.parseDouble(sys));
        String dia=String.valueOf(mRecords.get(i).getDiabolicPressure());
        diaX=new DecimalFormat("0.####").format(Double.parseDouble(dia));

        mColor_.add(mColor);
        // canvas.save();
        //canvas.rotate(-10,mLeft,mTop);
        if(isValues){

            paint.setColor(Color.WHITE);

            //  RectF r=new RectF(mLeft,mTop,120,70);
            //   canvas.drawRect(r,paint);


            //// mPaint.setColor(Color.BLACK);
            canvas.drawText(sysX+"/"+diaX,mLeft-35,mTop-2,mPaint);
            //  canvas.drawBitmap(mBitmap,mLeft-40,mTop,mPaint);


        }
        //canvas.restore();


    }

    int radius =10;
    public List<BPMeasurementModel> setHorizontalLabels(List<BPMeasurementModel> dateTime){

        mRecords=dateTime;
        maximum=largest(mRecords);
        minimum=Smallest(mRecords);
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




    private int largest(List<BPMeasurementModel> mRecords)
    {
        int max= (int) mRecords.get(0).getSysPressure();
        for(BPMeasurementModel b: mRecords)
        {
            if(b.getSysPressure()>max)
            {
                max= (int) b.getSysPressure();
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
    private int Smallest(List<BPMeasurementModel> mRecords)
    {

        int min= (int) mRecords.get(0).getDiabolicPressure();
        for(BPMeasurementModel b: mRecords)
        {
            if(b.getDiabolicPressure()<min)
            {
                min= (int) b.getSysPressure();
            }
        }

        return min;
    }


    List<String> mColor_;
    String mColor="";

    public List<String>  getColor() {

        return mColor_;
    }
}
