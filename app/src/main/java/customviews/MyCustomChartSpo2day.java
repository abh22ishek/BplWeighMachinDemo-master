package customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import logger.Level;
import logger.Logger;
import test.bpl.com.bplscreens.UserTestReportGraphActivity;
import testreport.TestReportDayFragment;

import static android.content.ContentValues.TAG;
import static testreport.TestReportDayFragment.day_time;



public class MyCustomChartSpo2day extends View {


    float pixels_per_5unit=0;
    float pixels_per_5unit_xaxis=0;
    float pixels_per_unit=0;
    Paint mPaint,p;

    int no_labels_x_axis=0;

    String str_pax1,str_pax2,str_pax3;


    public MyCustomChartSpo2day(Context context) {
        super(context);
        init();
    }

    public MyCustomChartSpo2day(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }




    private void init()
    {
        int density=getResources().getDisplayMetrics().densityDpi;
        pixels_per_5unit= (int) (density/5f);// 5 units per box used only for y axis
        pixels_per_5unit_xaxis=2*pixels_per_5unit;
        pixels_per_unit= (int) (pixels_per_5unit_xaxis/5f); // used for unit of x axis
        int max_x_axis=Integer.parseInt(UserTestReportGraphActivity.total_time);
        no_labels_x_axis=max_x_axis/5;

        Logger.log(Level.DEBUG,TAG,"no label x axis="+no_labels_x_axis);



    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Logger.log(Level.DEBUG,MyCustomChartHRDay.class.getSimpleName(),"**OnDraw() gets called **");

        if(p==null){
            p=new Paint();

        }
        p.setColor(Color.TRANSPARENT);
        p.setTextSize(21);

        int height=getHeight()-30;
        int width=getWidth();


        final float height_scale=(float)height/20;
        final float width_scale= ((float)width/Float.parseFloat(UserTestReportGraphActivity.total_time));

        String time[]=day_time.split(" ");
        String time_str=time[1];// hh:mm:ss

        String pxTime=time_str.replace(":","");


        int tx= Integer.parseInt(pxTime);



        int max_x_axis=Integer.parseInt(UserTestReportGraphActivity.total_time);

        if(pxTime.charAt(0)=='0' && pxTime.charAt(1)!='0')
        {
            str_pax1=String.valueOf(tx).substring(0,1);
            str_pax2=String.valueOf(tx).substring(1,3);
            str_pax3=String.valueOf(tx).substring(3,5);

        }else if(pxTime.charAt(0)=='0' && pxTime.charAt(1)=='0'){

            str_pax1="00";
            str_pax2=String.valueOf(tx).substring(0,2);
            str_pax3=String.valueOf(tx).substring(2,4);
        }

        else{
            str_pax1=String.valueOf(tx).substring(0,2);
            str_pax2=String.valueOf(tx).substring(2,4);
            str_pax3=String.valueOf(tx).substring(4,6);

        }
        Logger.log(Level.DEBUG, TestReportDayFragment.class.getSimpleName(), "str_pax1=" +str_pax1);
        Logger.log(Level.DEBUG,TestReportDayFragment.class.getSimpleName(), "str_pax2=" +str_pax2);
        Logger.log(Level.DEBUG,TestReportDayFragment.class.getSimpleName(), "str_pax3=" +str_pax3);


        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(str_pax1));
        calendar.set(Calendar.MINUTE,Integer.parseInt(str_pax2));
        calendar.set(Calendar.SECOND, Integer.parseInt(str_pax3));


        List<String> mTime=new ArrayList<>();
        for(int i=0;i<max_x_axis;i++)
        {

            Logger.log(Level.DEBUG, MyCustomChartHRDay.class.getSimpleName(), "tx=" +tx);
            String time_label=new SimpleDateFormat("HH:mm:ss").format(calendar.getTime());
            mTime.add(time_label);

            calendar.add(Calendar.SECOND, 1);
            Logger.log(Level.DEBUG,MyCustomChartHRDay.class.getSimpleName(),"**calendar counter**"+new SimpleDateFormat("HH:mm:ss").format(calendar.getTime()));
            canvas.drawText(time_label,width_scale,height_scale,p);
            tx=tx+1;
        }


        Paint paint=null;
        if(paint==null){
            paint=new Paint();

        }
        paint.setColor(Color.TRANSPARENT);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);




        p.setColor(Color.BLACK);
        p.setTextSize(21);



        float mx=0; int my=0;


        canvas.drawRect(0,0,getWidth(),getHeight(),paint);


        p.setStrokeWidth(3);
        p.setColor(Color.CYAN);


        if(mPaint==null){
            mPaint=new Paint();

        }
        mPaint.setTextSize(24);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(4);
        paint.setStyle(Paint.Style.STROKE);





       /* canvas.drawText("80",0,height,mPaint);
        canvas.drawText("100",0,20,mPaint);
        canvas.drawText("90",0,getHeight()/2+7,mPaint);
        canvas.drawText("95",0,getHeight()/4+20,mPaint);
        canvas.drawText("85",0,3*getHeight()/4,mPaint);*/


        // vertical lines
        canvas.drawLine(0,0,0,height,p);

        canvas.drawLine(0,0,width,0,p);



        canvas.drawLine(0,height/4,width,height/4,p);
        canvas.drawLine(0,height/2,width,height/2,p);
        canvas.drawLine(0,3*height/4,width,3*height/4,p);
        canvas.drawLine(0,height,width,height,p);


        float mCx=pixels_per_5unit_xaxis;

        // dynamic vertical grids
        for(int i=0;i<no_labels_x_axis;i++)
        {
            canvas.drawLine(mCx,0,mCx,height,p);
            mCx=mCx+pixels_per_5unit_xaxis;
        }

        float mCxy=0;
        p.setTextSize(21);
        p.setColor(Color.WHITE);


        // draw text

        p.setTextSize(22);
        p.setAntiAlias(true);
        p.setStrokeWidth(4);

        for(int i=0;i< Integer.parseInt(UserTestReportGraphActivity.total_time);i=i+5)
        {

            if(i==0)
            {
                canvas.drawText(mTime.get(i),mCxy,getHeight(),p);
            }else{
                canvas.drawText(mTime.get(i),mCxy-30,getHeight(),p);
            }

            mCxy=mCxy+pixels_per_5unit_xaxis;
        }








        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(4);


        p.setColor(Color.parseColor("#FF4081"));

        // draw circle points
        float mpX=0;
        for(int i=0;i<Integer.parseInt(UserTestReportGraphActivity.total_time);i++)
        {
            float y_points=(100-(Integer.parseInt(TestReportDayFragment.mspo2_list[i])));
            float px_y=y_points*height_scale;// current y pointts

            canvas.drawCircle(mpX,px_y,7,p);
            mpX=mpX+width_scale;
        }


        // draw line connecting to circle
        p.setStrokeWidth(3);
        // first point
        float p1x1=0;
        float p1y1=(100-(Integer.parseInt(TestReportDayFragment.mspo2_list[0])))*height_scale;

        mx=width_scale;
        for(int i=1;i<Integer.parseInt(UserTestReportGraphActivity.total_time);i++)
        {
            float y_points=(100-(Integer.parseInt(TestReportDayFragment.mspo2_list[i])));
            float px_y=y_points*height_scale;// current y pointts
            canvas.drawLine(p1x1,p1y1,mx,px_y,p);
            p1x1=mx;
            p1y1=px_y;
            mx=mx+width_scale;
        }


    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(Integer.parseInt(UserTestReportGraphActivity.total_time)*(int)pixels_per_unit,5*(int)pixels_per_5unit);
    }


}
