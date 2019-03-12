package customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import test.bpl.com.bplscreens.UserTestReportActivity;
import testreport.*;


public class MyCustomSPO2Graph extends View {


    Paint paint;

    int pixels_per_unit=0;
    Paint p;
    Paint mPaint;

    String mTag;
    int max_x_axis;
    public MyCustomSPO2Graph(Context context) {
        super(context);

        init();
    }

    public MyCustomSPO2Graph(Context context, AttributeSet attrs) {
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





        //canvas.drawText("0"+"s",0,getHeight(),p);

        if(mTag.equalsIgnoreCase("share")){
             max_x_axis=Integer.parseInt(ShareReportSpo2Activity.duration_str);
        }else{
             max_x_axis=Integer.parseInt(UserTestReportActivity.duration_str);
        }

        int min_x_axis=0;
        int mid_x_axis=(max_x_axis+min_x_axis)/2;

        int bw_min_mid=(mid_x_axis+min_x_axis)/2;
        int bw_max_mid=(mid_x_axis+max_x_axis)/2;


        if(mPaint==null){
            mPaint=new Paint();

        }
        mPaint.setTextSize(24);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(3);



        canvas.drawText("80",0,getHeight()-30,mPaint);
        canvas.drawText("100",0,20,mPaint);
        canvas.drawText("90",0,(getHeight()-30)/2,mPaint);
        canvas.drawText("95",0,(getHeight()-20)/4,mPaint);
        canvas.drawText("85",0,3*(getHeight()-30)/4,mPaint);


        canvas.drawText(min_x_axis+"s",0,getHeight(),mPaint);
        canvas.drawText(mid_x_axis+"s",getWidth()/2-25,getHeight(),mPaint);// for middle x axis
        canvas.drawText(bw_min_mid+"s",getWidth()/4-30,getHeight(),mPaint);// for 2st middle axis
        canvas.drawText(bw_max_mid+"s",3*getWidth()/4-35,getHeight(),mPaint);
        canvas.drawText(max_x_axis+"s",getWidth()-50,getHeight(),mPaint);

        int height=getHeight()-30;
        int width=getWidth()-30;


            // horizontal lines
        canvas.drawLine(0,0,0,height,p);
        canvas.drawLine(width/4,0,width/4,height,p);
        canvas.drawLine(width/2,0,width/2,height,p);
        canvas.drawLine(width,0,width,height,p);
        canvas.drawLine(3*width/4,0,3*width/4,height,p);
        canvas.drawLine(0,0,width,0,p);

        // vertical lines

        canvas.drawLine(0,height/4,width,height/4,p);
        canvas.drawLine(0,height/2,width,height/2,p);
        canvas.drawLine(0,3*height/4,width,3*height/4,p);
        canvas.drawLine(0,height,width,height,p);



        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        paint.setTextSize(50);



        canvas.drawText("SPo2",getWidth()/2-30,getHeight()/2,paint);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(4);


        final float height_scale=(float)height/20;
        if(mTag.equalsIgnoreCase("share")){
            final float width_scale= ((float)width/Float.parseFloat(ShareReportSpo2Activity.duration_str));

            // first point
            float p1x1=width_scale;
            float p1y1=(100-(Integer.parseInt(ShareReportSpo2Activity.mspo2_list.get(0))))*height_scale;

            mx=2*width_scale;
            for(int i = 1; i< ShareReportSpo2Activity.mspo2_list.size(); i++)
            {
                float y_points=(100-(Integer.parseInt(ShareReportSpo2Activity.mspo2_list.get(i))));
                float px_y=y_points*height_scale;// current y pointts


                //canvas.drawCircle(mx,px_y,2f,paint);
                canvas.drawLine(p1x1,p1y1,mx,px_y,paint);
                p1x1=mx;
                p1y1=px_y;
                mx=mx+width_scale;




            }
        }else{
            final float width_scale= ((float)width/Float.parseFloat(UserTestReportActivity.duration_str));

            // first point
            float p1x1=width_scale;
            float p1y1=(100-(Integer.parseInt(UserTestReportActivity.mspo2_list.get(0))))*height_scale;

            mx=2*width_scale;
            for(int i = 1; i< UserTestReportActivity.mspo2_list.size(); i++)
            {
                float y_points=(100-(Integer.parseInt(UserTestReportActivity.mspo2_list.get(i))));
                float px_y=y_points*height_scale;// current y pointts


                //canvas.drawCircle(mx,px_y,2f,paint);
                canvas.drawLine(p1x1,p1y1,mx,px_y,paint);
                p1x1=mx;
                p1y1=px_y;
                mx=mx+width_scale;




            }
        }
       // mx=width_scale;




    }

   @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(10*pixels_per_unit,5*pixels_per_unit);
    }


    public void setTag(String Tag)
    {

        mTag=Tag;
    }
}
