package canny;

import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;

import java.util.*;

import model.*;

public class IweighBodyFatView extends View {

    Paint paint,dashpaint;
    int pixels_per_unit=0;

    Paint mPaint;

    List<RecordDetailWeighMachine> listBdyFat;

    List<String> bodyFatList;
    List<String> datesList;

    int startX=0;
    int startY=0;

    int stopX=0;
    int stopY=0;

    int heightScaleRatio;

    Path path ;


    public IweighBodyFatView(Context context) {
        super(context);
        init();
    }

    public IweighBodyFatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init()
    {
        int density=getResources().getDisplayMetrics().densityDpi;
        pixels_per_unit= (int) (density/5f);
        // startx_=pixels_per_unit;
        heightScaleRatio=60;

        path=new Path();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(paint==null){
            paint=new Paint();
        }


        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);




        if(mPaint==null){
            mPaint=new Paint();

        }


        mPaint.setTextSize(24);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(3);




        if(dashpaint==null) {
            dashpaint = new Paint();
            dashpaint.setStyle(Paint.Style.FILL);
            dashpaint.setColor(Color.CYAN);
            dashpaint.setStrokeWidth(30);


        }

        stopX=getWidth();
        startX=0;
        startY=0;
        stopY=0;
        //  float phase = 0;

        // horizontal  lines
        paint.setStrokeWidth(1.1f);
        for(int i=0;i<getHeight();i+=pixels_per_unit){

            canvas.drawLine(startX,startY,stopX,stopY,paint);


            startY=startY+pixels_per_unit;
            stopY=stopY+pixels_per_unit;

        }




        // draw horizontal labels
        int startx_=pixels_per_unit;
        paint.setTextSize(25);
        for(int i=0;i<datesList.size();i++)
        {
            canvas.save();
            //  canvas.rotate(-45,startx_,getHeight());
            String [] dateTime=datesList.get(i).split(" ");
            // canvas.drawText(dateTime[0],startx_-15,height-20,paint);
            canvas.drawText(dateTime[1].substring(0,5),startx_,getHeight()-pixels_per_unit+20,paint);

            startx_=startx_+(2*pixels_per_unit);
            canvas.restore();
        }






        // Plotting the point

        // scaling the axis
        final float height_scale=((float)getHeight()-pixels_per_unit)/heightScaleRatio;
        final float width_scale=pixels_per_unit;



        // first point
        float p1x1=width_scale;
        float p1y1=0;
        if(bodyFatList.size()>0)
        {
            p1y1=(heightScaleRatio-(Float.parseFloat(bodyFatList.get(0))))*height_scale;
        }




        // circle the points


        paint.setColor(Color.parseColor("#FF4081"));

        // draw circle points
        float mpX=width_scale;
        for(int i = 0; i<bodyFatList.size(); i++)
        {
            float y_points=(heightScaleRatio-(Float.parseFloat(bodyFatList.get(i))));
            float px_y=y_points*height_scale;// current y pointts

            canvas.drawCircle(mpX,px_y,7,paint);
            canvas.drawText("( "+bodyFatList.get(i)+" )",mpX,px_y,mPaint);
            mpX=mpX+(2*width_scale);
        }



        // draw line connecting to circle
        mPaint.setStrokeWidth(3);
        mPaint.setColor(Color.GRAY);

        mPaint.setStrokeWidth(5);




        float mx=0;


        mx=width_scale;
        for(int i =0; i<bodyFatList.size(); i++)
        {
            float y_points=(heightScaleRatio-(Float.parseFloat(bodyFatList.get(i))));

            float px_y= y_points*height_scale;// current y points

            canvas.drawLine(p1x1,p1y1,mx,px_y,mPaint);


            p1x1=mx;
            p1y1=px_y;
            mx=mx+(2*width_scale);
        }

    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if(listBdyFat.size()<=3){
            setMeasuredDimension((6*listBdyFat.size()*pixels_per_unit)+pixels_per_unit,
                    7*pixels_per_unit);
        }else{
            setMeasuredDimension((2*listBdyFat.size()*pixels_per_unit)+pixels_per_unit,
                    7*pixels_per_unit);
        }

    }



    public void set_XY_points(List<RecordDetailWeighMachine> listX)
    {
        listBdyFat=listX;
    }


    public void setList(List<String> dates,List<String> bmi){
        datesList=dates;
        bodyFatList=bmi;
    }
}
