package canny;

import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;

import java.util.*;

import model.*;

public class IWeighChartView extends View {

    Paint paint,mPaint,dashpaint;
    int pixels_per_unit=0;


    List<RecordDetailWeighMachine> listWeight;
    List<String> weightList;
    List<String> datesList;



    int startX=0;
    int startY=0;

    int stopX=0;
    int stopY=0;


    int goalWeight=68;


    int startx_=0;
    int heightScaleRatio;
    private Path mPath;


    public IWeighChartView(Context context) {
        super(context);
        init();
    }

    public IWeighChartView(Context context, AttributeSet attrs) {
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
            String [] dateTime=datesList.get(i).split(" ");

           // canvas.drawText(dateTime[0],startx_-30,getHeight()-15,paint);
            canvas.drawText(dateTime[1].substring(0,5),startx_-30,getHeight()-pixels_per_unit+20,paint);
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

            dashpaint.setPathEffect(new DashPathEffect(new float[] { 5,5 }, 0));
        }

        dashpaint.setColor(Color.parseColor("#7CBC50"));
        dashpaint.setAntiAlias(true);
        dashpaint.setStrokeWidth(8);

        mPath.moveTo(0,(heightScaleRatio-goalWeight)*height_scale);
        mPath.lineTo(getWidth(),(heightScaleRatio-goalWeight)* height_scale);
        canvas.drawPath(mPath,dashpaint);



        // circle the points





        float mx=0;

        mx=width_scale;
        // first point
        float p1x1=width_scale;
        float p1y1=0;
        if(weightList.size()>0)
        {
            p1y1=(heightScaleRatio-(Float.parseFloat(weightList.get(0))))*height_scale;
        }

        mPaint.setStrokeWidth(3);
        mPaint.setColor(Color.GRAY);
        for(int i =0; i<weightList.size(); i++)
        {
            float y_points=(heightScaleRatio-(Float.parseFloat(weightList.get(i))));

            float px_y= y_points*height_scale;// current y points

            canvas.drawLine(p1x1,p1y1,mx,px_y,mPaint);


            p1x1=mx;
            p1y1=px_y;
            mx=mx+(2*width_scale);
        }


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

        mPaint.setTextSize(50);
        mPaint.setStrokeWidth(30);
        mPaint.setAntiAlias(true);
        Typeface custom_font = Typeface.createFromAsset(getContext().getAssets(),  "fonts/CentraleSans-Book.otf");
        mPaint.setTypeface(custom_font);


        mPaint.setColor(Color.parseColor("#7CBC50"));
        canvas.drawText("Weight Goal 68 KG",4*pixels_per_unit,getHeight()-35,mPaint);

    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if(listWeight.size()<=3){
            setMeasuredDimension((5*listWeight.size()*pixels_per_unit)+pixels_per_unit,
                    10*pixels_per_unit);
        }else{
            setMeasuredDimension((2*listWeight.size()*pixels_per_unit)+pixels_per_unit,
                    10*pixels_per_unit);
        }

    }


    public void set_XY_points(List<RecordDetailWeighMachine> listX)
    {
        listWeight=listX;
        datesList=new ArrayList<>();
        weightList=new ArrayList<>();
        for(int i=0;i<listWeight.size();i++){
            datesList.add(listWeight.get(i).getDate());
            weightList.add(String.valueOf(listWeight.get(i).getWeight()));
        }
    }


    public void setList(List<String> dates,List<String> weight){
      //  datesList=dates;
       // weightList=weight;
    }
}
