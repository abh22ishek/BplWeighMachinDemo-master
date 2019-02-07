package biolight;

import android.content.*;
import android.graphics.*;
import android.support.v4.content.*;
import android.util.*;
import android.view.*;

import test.bpl.com.bplscreens.*;

public class CustomBPChartBioLight extends View {

    Paint mPaint;
    int pixels_per_unit=0;
    int marginX=0;
    int marginY=0;
    Paint paint; // for coloring

    int innerHeight=0;
    int innerWidth=0;

    RectF rectBG;
    Bitmap mBitmap;
    Bitmap mSysBitmap;
    Bitmap mDiaBitmap;

    RectF rectF;
    Path mPath;
    RectF rectangleF;

    float finalPointX=0;
    float finalPointY=0;

    Paint whiteborder;

    int density=0;
    Paint vPaint;

    Paint textPaint;

    public CustomBPChartBioLight(Context context) {
        super(context);
        init();
    }

    public CustomBPChartBioLight(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init()
    {

        if(mPaint==null)
        {
            mPaint=new Paint();
        }


        if(paint==null)
        {
            paint=new Paint();
        }

        if(whiteborder==null){
            whiteborder=new Paint();
        }

        vPaint=new Paint();

        textPaint=new Paint();
        density=getResources().getDisplayMetrics().densityDpi;
        pixels_per_unit= (int)(density/5f);
        marginX=pixels_per_unit;
        marginY=pixels_per_unit;

        rectF=new RectF();
        rectBG=new RectF();

        mBitmap= BitmapFactory.decodeResource(getResources(),R.mipmap.person_pin);
        mSysBitmap=BitmapFactory.decodeResource(getResources(),R.mipmap.sys_bp_text);
        mDiaBitmap=BitmapFactory.decodeResource(getResources(),R.mipmap.dia_bp_text);
        mPath=new Path();

    }




    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        rectF.set(0,0,getWidth(),getHeight());
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.white));
        mPaint.setStyle(Paint.Style.STROKE);
        // canvas.drawRect(rectF,mPaint);
        // set paint color to white
        paint.setColor(Color.WHITE);

        innerHeight=getHeight()-2*marginY;
        innerWidth=getWidth()-2*marginX;


        final float height_scale=(float)innerHeight/130;
        final float width_scale= (float)innerWidth/80;

        // x axis name


        textPaint.setColor(Color.WHITE);
        textPaint.setStrokeWidth(10);
        textPaint.setTextSize(25);
        textPaint.setAntiAlias(true);

        vPaint.setColor(Color.WHITE);
        vPaint.setStrokeWidth(15);
        vPaint.setTextSize(30);
        vPaint.setAntiAlias(true);


        if(rectangleF==null)
        {
            rectangleF=new RectF(0,0,marginX,getHeight());
        }


        // mPath.addOval(rectangleF,Path.Direction.CW);
        // paint.setTextSize(25f);
        // canvas.drawTextOnPath("Diastolic BP ",mPath,0,50,paint);


        rectF.set(marginX,marginY,getWidth()-marginX,getHeight()-marginY);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.white));
        canvas.drawRect(rectF,mPaint);

        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.moderate_hypertension_color));
        // Vertical Y axis labelling

        canvas.drawCircle(marginX,getHeight()-marginY,5f,paint);// 70
        canvas.drawCircle(marginX,marginY,5f,paint);// 190
        //canvas.drawCircle(marginX,3*getHeight()/4,5f,paint);// 180




        mPaint.setTextSize(24f);


        if(density== DisplayMetrics.DENSITY_XXHIGH){
            mPaint.setTextSize(30f);
            textPaint.setTextSize(32);
        }else if(density==DisplayMetrics.DENSITY_XXXHIGH){
            mPaint.setTextSize(40f);
            textPaint.setTextSize(40);

        }
        mPaint.setColor(Color.WHITE);



        if(density== DisplayMetrics.DENSITY_XXHIGH){
            canvas.drawText("70",marginX-38,getHeight()-marginY,vPaint);


            canvas.drawText("90",marginX-42,marginY+(190-90)*height_scale,vPaint);
            canvas.drawCircle(marginX,marginY+(190-90)*height_scale,5f,paint);// 90

            canvas.drawText("120",marginX-50,marginY+(190-120)*height_scale,vPaint);
            canvas.drawCircle(marginX,marginY+(190-120)*height_scale,5f,paint);// 120


            canvas.drawText("140",marginX-55,marginY+(190-140)*height_scale,vPaint);
            canvas.drawCircle(marginX,marginY+(190-140)*height_scale,5f,paint);// 140


            canvas.drawText("160",marginX-55,marginY+(190-160)*height_scale,vPaint);
            canvas.drawCircle(marginX,marginY+(190-160)*height_scale,5f,paint);// 160

            canvas.drawText("190",marginX-60,marginY+20f,vPaint);
        }
        else{
            vPaint.setTextSize(23f);
            canvas.drawText("70",marginX-32,getHeight()-marginY,vPaint);


            canvas.drawText("90",marginX-32,marginY+(190-90)*height_scale,vPaint);
            canvas.drawCircle(marginX,marginY+(190-90)*height_scale,5f,paint);// 90

            canvas.drawText("120",marginX-41,marginY+(190-120)*height_scale,vPaint);
            canvas.drawCircle(marginX,marginY+(190-120)*height_scale,5f,paint);// 120


            canvas.drawText("140",marginX-41,marginY+(190-140)*height_scale,vPaint);
            canvas.drawCircle(marginX,marginY+(190-140)*height_scale,5f,paint);// 140


            canvas.drawText("160",marginX-40,marginY+(190-160)*height_scale,vPaint);
            canvas.drawCircle(marginX,marginY+(190-160)*height_scale,5f,paint);// 160

            canvas.drawText("190",marginX-45,marginY+20f,vPaint);
        }




        // Horizontal x axis labelling



        canvas.drawText("40",marginX,getHeight()-marginY+30,vPaint);

        canvas.drawCircle(getWidth()-marginX,getHeight()-marginY,5f,paint);// 130
        canvas.drawText("120",getWidth()-marginX-10,getHeight()-marginY+30,vPaint);


        canvas.drawCircle(getWidth()/2,getHeight()-marginY,5f,paint);// 80
        canvas.drawText("80",getWidth()/2,getHeight()-marginY+30,vPaint);

        canvas.drawCircle((marginX+getWidth()/2)/2,getHeight()-marginY,5f,paint);// 60
        canvas.drawText("60",(marginX+getWidth()/2)/2,getHeight()-marginY+30,vPaint);

        canvas.drawCircle((getWidth()/2+getWidth()-marginX)/2,getHeight()-marginY,5f,paint);// 100
        canvas.drawText("100",(getWidth()/2+getWidth()-marginX)/2,getHeight()-marginY+30,vPaint);

        canvas.drawCircle(marginX+((110-40)*width_scale),getHeight()-marginY,5f,paint);// 110
        canvas.drawText("110",marginX+((110-40)*width_scale),getHeight()-marginY+30,vPaint);

        canvas.drawCircle( marginX+((90-40)*width_scale),getHeight()-marginY,5f,paint);// 90
        canvas.drawText("90", marginX+((90-40)*width_scale),getHeight()-marginY+30,vPaint);


        // Severe Hypertension draw BG

        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        // severe
        rectBG.set(marginX,marginY,getWidth()-marginX,getHeight()-marginY);
        whiteborder.setColor(Color.WHITE);
        whiteborder.setStrokeWidth( 1f );
        whiteborder.setStyle( Paint.Style.STROKE );


        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.severe_hypertension_color));
        canvas.drawRect(rectBG,mPaint);
        canvas.drawRect(rectBG,whiteborder);


        // moderate 180 y axis and 110 x axis
        rectBG.set(marginX,marginY+(10*height_scale),marginX+((110-40)*width_scale),getHeight()-marginY);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.moderate_hypertension_color));
        canvas.drawRect(rectBG,mPaint);
        canvas.drawRect(rectBG,whiteborder);



        // mild 160 y axis  and 100 x axis
        rectBG.set(marginX,marginY+((190-160)*height_scale),marginX+((100-40)*width_scale),getHeight()-marginY);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.mild_hypertension_color));
        canvas.drawRect(rectBG,mPaint);
        canvas.drawRect(rectBG,whiteborder);



        // high normal 140 y axis 90 x axis

        rectBG.set(marginX,marginY+(190-140)*height_scale,
                marginX+((90-40)*width_scale),getHeight()-marginY);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.high_normal));
        canvas.drawRect(rectBG,mPaint);
        canvas.drawRect(rectBG,whiteborder);


        //Normal 130 y axis 85 x axis

        rectBG.set(marginX,marginY+(190-130)*height_scale,
                marginX+((85-40)*width_scale),getHeight()-marginY);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.normal));
        canvas.drawRect(rectBG,mPaint);
        canvas.drawRect(rectBG,whiteborder);


        // optimal 120 y axis 80 x axis

        rectBG.set(marginX,marginY+(190-120)*height_scale,
                marginX+((80-40)*width_scale),getHeight()-marginY);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.optimal));
        canvas.drawRect(rectBG,mPaint);
        canvas.drawRect(rectBG,whiteborder);


        // Low 90 y axis 60 x axis
        rectBG.set(marginX,marginY+(190-90)*height_scale,
                marginX+((60-40)*width_scale),getHeight()-marginY);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.low));
        canvas.drawRect(rectBG,mPaint);
        canvas.drawRect(rectBG,whiteborder);


        // draw label text

        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(2f);


        canvas.drawText(getContext().getString(R.string.severe_hypertension),
                marginX+30,marginY+25,textPaint);

        canvas.drawText(getContext().getString(R.string.moderate_hypertension),
                marginX+30,marginY+(10*height_scale)+35,textPaint);

        textPaint.setColor(Color.BLACK);
        //mPaint.setStrokeWidth(1.8f);

        canvas.drawText(getContext().getString(R.string.mild_hypertension),marginX+30,marginY+((30)*height_scale)+40,textPaint);

        canvas.drawText(getContext().getString(R.string.high_normal),
                marginX+30,marginY+(190-140)*height_scale+30,textPaint);

        canvas.drawText(getContext().getString(R.string.normal_),marginX+30,marginY+(190-130)*height_scale+30,textPaint);

        canvas.drawText(getContext().getString(R.string.optimal),marginX+30,marginY+(190-120)*height_scale+60,textPaint);

        canvas.drawText(getContext().getString(R.string.low),marginX+30,marginY+(190-90)*height_scale+50,textPaint);


        // x label

        // canvas.drawText("Systolic Blood Pressure(mm Hg)",getWidth()/2-150f,getHeight()-10f,paint);

        if(density==DisplayMetrics.DENSITY_XXHIGH){
            canvas.drawBitmap(mSysBitmap,getWidth()/2-160f, getHeight()-34f,paint);
        }
        else if(density==DisplayMetrics.DENSITY_XXXHIGH){
            canvas.drawBitmap(mSysBitmap,getWidth()/2-160f, getHeight()-40f,paint);
        }
        else{
            canvas.drawBitmap(mSysBitmap,getWidth()/2-160f, getHeight()-27f,paint);
        }



        canvas.drawBitmap(mDiaBitmap,0f, 100f,paint);
        // plot the points x and y (140,100)


        // canvas.drawCircle((100-40)*width_scale+marginX,(190-140)*height_scale+marginY,20f,paint);

        canvas.drawBitmap(mBitmap,(finalPointX-40)*width_scale+marginX-mBitmap.getWidth()/2,
                (190-finalPointY)*height_scale+marginY-mBitmap.getHeight()+10,paint);

        if(finalPointX!=0 && finalPointY!=0){
            vPaint.setTextSize(25);
            int px= (int) finalPointX;
            int py= (int) finalPointY;

            if(density==DisplayMetrics.DENSITY_XXHIGH){
                textPaint.setTextSize(34);
            }else{
                textPaint.setTextSize(25);
            }
            textPaint.setColor(Color.WHITE);
            canvas.drawText("("+py+"/"+px+")",(finalPointX-40)*width_scale+marginX-mBitmap.getWidth()/2,
                    ((190-finalPointY)*height_scale+marginY-mBitmap.getHeight())+mBitmap.getHeight()+20,textPaint);

        }

    }



    public void set_XY_points(float x,float y)
    {
        finalPointX=x;
        finalPointY=y;
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(11*pixels_per_unit,10*pixels_per_unit);

    }

}
