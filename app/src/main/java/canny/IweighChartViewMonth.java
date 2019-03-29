package canny;

import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;

import java.util.*;

import logger.*;
import model.*;

public class IweighChartViewMonth extends View {

    Paint paint,mPaint,dashpaint;
    Path mPath;
    int pixels_per_unit=0;


    List<RecordDetailWeighMachine> listWeight;
    List<String> weightList;
    List<String> datesList;


    int goalWeight=68;

    int startX=0;
    int startY=0;

    int stopX=0;
    int stopY=0;




    int startx_=0;
    int heightScaleRatio;



    public IweighChartViewMonth(Context context) {
        super(context);
        init();
    }

    public IweighChartViewMonth(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init()
    {
        int density=getResources().getDisplayMetrics().densityDpi;
        pixels_per_unit= (int) (density/5f);
        startx_=pixels_per_unit;
        heightScaleRatio=180;
        mPath=new Path();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (paint == null) {
            paint = new Paint();

        }
        if (mPaint == null) {
            mPaint = new Paint();

        }

        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(1.1f);



        // horizontal  lines


        stopX=getWidth();
        startX=0;
        startY=0;
        stopY=0;


        //  float phase = 0;

        for(int i=0;i<getHeight();i+=pixels_per_unit){

            canvas.drawLine(startX,startY,stopX,stopY,paint);

            startY=startY+pixels_per_unit;
            stopY=stopY+pixels_per_unit;

        }


        // draw dates and time text
        paint.setTextSize(18f);

        for(int i=0;i<datesList.size();i++)
        {
            canvas.save();
            //canvas.rotate(-45,startx_,height+20);

            // canvas.drawText(dateTime[0],startx_-30,getHeight()-15,paint);
            canvas.drawText(datesList.get(i).substring(0,5),startx_-30,getHeight()-pixels_per_unit+20,paint);
            startx_=startx_+(2*pixels_per_unit);
            canvas.restore();
        }

        // plot the points


        // scaling the axis
        final float height_scale=((float)getHeight()-pixels_per_unit)/heightScaleRatio;
        final float width_scale=pixels_per_unit;

        if(dashpaint==null){
            dashpaint=new Paint();
            dashpaint.setStyle(Paint.Style.STROKE);
            dashpaint.setPathEffect(new DashPathEffect(new float[] { 5,15 }, 0));
        }

        dashpaint.setColor(Color.parseColor("#7CBC50"));
        dashpaint.setAntiAlias(true);
        dashpaint.setStrokeWidth(20);


        mPath.moveTo(0,(heightScaleRatio-goalWeight)*height_scale);
        mPath.lineTo(getWidth(),(heightScaleRatio-goalWeight)* height_scale);
        canvas.drawPath(mPath,dashpaint);

        // circle the points
        paint.setColor(Color.parseColor("#FF4081"));
        paint.setStrokeWidth(40);
        paint.setTextSize(30);

        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(30);
        mPaint.setStrokeWidth(5);

        // draw circle points
        float mpX=width_scale;


        for(int i = 0; i<weightList.size(); i++)
        {
            float y_points=(heightScaleRatio-(Float.parseFloat(weightList.get(i))));
            float px_y=y_points*height_scale;// current y pointts

            //canvas.drawCircle(mpX,px_y,7,p);
            //  canvas.drawRect(mpX-25,px_y,mpX+25,getHeight()-pixels_per_unit,paint);
            canvas.drawCircle(mpX,px_y,10,paint);
            canvas.drawText("( " +weightList.get(i)+ " )",mpX,px_y,mPaint);
            mpX=mpX+(2*width_scale);
        }
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension((2*30*pixels_per_unit)+pixels_per_unit,
                10*pixels_per_unit);
    }


    public void set_XY_points(List<RecordDetailWeighMachine> listX)
    {
        listWeight=listX;
    }




    public List<String> setHorizontalLabel(List<String> WeekDates){
        datesList=WeekDates;
        Logger.log(Level.DEBUG,"Custom View ","**Day record size**="+datesList.size());
        return datesList;
    }

    public List<String> setPlotPoints(List<String> points){
        weightList=points;
        Logger.log(Level.DEBUG,"Custom View ","**Day record size**="+weightList.size());
        return weightList;
    }
}
