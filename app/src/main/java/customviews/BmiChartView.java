package customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

import model.RecordDetailWeighMachine;
import test.bpl.com.bplscreens.WeighMachineRecordActivity;



public class BmiChartView extends View {

    Paint paint;
    int pixels_per_unit=0;
    Paint p;
    Paint mPaint;

    List<RecordDetailWeighMachine> listBmi;

    List<String> bmiList;
    List<String> datesList;


    public BmiChartView(Context context) {
        super(context);
        init();
    }

    public BmiChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init()
    {
        int density=getResources().getDisplayMetrics().densityDpi;
        pixels_per_unit= (int) (density/5f);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(paint==null){
            paint=new Paint();
        }


        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);



        if(p==null){
            p=new Paint();

        }
        p.setColor(Color.BLACK);
        p.setTextSize(21);


        float mx=0; int my=0;


        canvas.drawRect(0,0,getWidth(),getHeight(),paint);




        if(mPaint==null){
            mPaint=new Paint();

        }
        mPaint.setTextSize(24);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(3);



        canvas.drawText("10",0,getHeight()-30,mPaint);
        canvas.drawText("20",0,3*(getHeight()-30)/4,mPaint);
        canvas.drawText("30",0,(getHeight()-30)/2,mPaint);
        canvas.drawText("40",0,(getHeight()-20)/4,mPaint);
        canvas.drawText("50",0,20,mPaint);





        int height=getHeight()-30;
        int width=getWidth()-30;







        // horizontal  lines
        canvas.drawLine(0,0,width,0,p);
        canvas.drawLine(0,height/4,width,height/4,p);
        canvas.drawLine(0,height/2,width,height/2,p);
        canvas.drawLine(0,3*height/4,width,3*height/4,p);
        canvas.drawLine(0,height,width,height,p);


        int startx=pixels_per_unit;
        int starty=height;

        // vertical lines
        for(int i=0;i<listBmi.size();i++)
        {
            canvas.drawLine(startx,0,startx,height,p);
            startx=startx+(2*pixels_per_unit);
        }




        // draw text
        int startx_=pixels_per_unit;
        p.setColor(Color.GRAY);
        for(int i=0;i<datesList.size();i++)
        {
            canvas.save();
            canvas.rotate(-45,startx_,height+10);
            String [] dateTime=datesList.get(i).split(" ");
            canvas.drawText(dateTime[0],startx_-15,height-20,p);
            canvas.drawText(dateTime[1],startx_-15,height,p);

            startx_=startx_+(2*pixels_per_unit);
            canvas.restore();
        }





        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        paint.setTextSize(50);





        // Plotting the point

        // scaling the axis
        final float height_scale=(float)height/50;
        final float width_scale=pixels_per_unit;



        // first point
        float p1x1=width_scale;
        float p1y1=0;
        if(bmiList.size()>0)
        {
             p1y1=(50-(Float.parseFloat(bmiList.get(0))))*height_scale;
        }




        // circle the points


        p.setColor(Color.parseColor("#FF4081"));

        // draw circle points
        float mpX=width_scale;
        for(int i = 0; i<bmiList.size(); i++)
        {
            float y_points=(50-(Float.parseFloat(bmiList.get(i))));
            float px_y=y_points*height_scale;// current y pointts

            canvas.drawCircle(mpX,px_y,7,p);
            mpX=mpX+(2*width_scale);
        }



        // draw line connecting to circle
        p.setStrokeWidth(3);
        p.setColor(Color.GRAY);
        p.setAntiAlias(true);

        mx=width_scale;
        for(int i =0; i<bmiList.size(); i++)
        {
            float y_points=(50-(Float.parseFloat(bmiList.get(i))));
            float px_y=y_points*height_scale;// current y points

            canvas.drawLine(p1x1,p1y1,mx,px_y,p);
            p1x1=mx;
            p1y1=px_y;
            mx=mx+(2*width_scale);
        }
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension((2*listBmi.size()*pixels_per_unit)+pixels_per_unit,
                6*pixels_per_unit);
    }



    public void set_XY_points(List<RecordDetailWeighMachine> listX)
    {
        listBmi=listX;
    }




    public void setList(List<String> dates,List<String> bmi){
        datesList=dates;
        bmiList=bmi;
    }


}
