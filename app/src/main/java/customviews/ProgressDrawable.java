package customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

import constantsP.Utility;
import test.bpl.com.bplscreens.R;



public class ProgressDrawable extends Drawable {


    private Paint mPaint = new Paint();

    private float density;
    private Context context;

    public ProgressDrawable(float density,Context context) {
        this.density = density;
        this.context=context;
    }



    @Override
    public void draw(@NonNull Canvas canvas) {
        // TODO Auto-generated method stub
        int level = getLevel();

        mPaint.setAntiAlias(true);
        //int pixelpercm=density/1/
        Rect b = getBounds();

        float height = b.height();
        float width=b.width();
        if(density== DisplayMetrics.DENSITY_MEDIUM) {
            int NUM_RECTS = 15;
            draw_circle(15, 15, 10, NUM_RECTS, level, canvas, width, height, 20, 30, mPaint);
           /* float x=15;
            float y=15;

            for (int i =0; i<NUM_RECTS; i++) {

                if((i+1)*10000/NUM_RECTS>level)
                {
                    mPaint.setColor(Color.GRAY);
                }else{
                    mPaint.setColor(Color.GREEN);
                }
                //canvas.drawRect(0,height-x,width,height-y, mPaint);
                canvas.drawCircle(width/2, height-y,10, mPaint);

                x=x+20;
                y=y+30;
            }*/

        }else if(density==DisplayMetrics.DENSITY_XHIGH){
            int NUM_RECTS =9;
            draw_circle(30,30,20,NUM_RECTS,level,canvas,width,height,35,50,mPaint);
        }
        else if(density==300)
        {
            int NUM_RECTS =12;
            draw_circle(30,30,20,NUM_RECTS,level,canvas,width,height,35,48,mPaint);
        }

        else if(density==DisplayMetrics.DENSITY_XXXHIGH)
        {
            int NUM_RECTS = 12;
            draw_circle(50,50,40,NUM_RECTS,level,canvas,width,height,60,80,mPaint);
        }
        else if(density==DisplayMetrics.DENSITY_XXHIGH) {
            int NUM_RECTS = 12;
            draw_circle(40,40,30,NUM_RECTS,level,canvas,width,height,50,70,mPaint);
        }else if(density>200  && density<280)
        {
            int NUM_RECTS =8;
            draw_circle(30,30,15,NUM_RECTS,level,canvas,width,height,35,35,mPaint);
        }
        else {

            int NUM_RECTS = 10;
            draw_circle(40, 40, 30, NUM_RECTS, level, canvas, width, height, 50, 70, mPaint);

           /* float x=40;
            float y=40;

            for (int i =0; i<NUM_RECTS; i++) {

                if((i+1)*10000/NUM_RECTS>level)
                {
                    mPaint.setColor(Color.GRAY);
                }else{
                    mPaint.setColor(Color.GREEN);
                }
                //canvas.drawRect(0,height-x,width,height-y, mPaint);
                canvas.drawCircle(width/2, height-y,30, mPaint);

                x=x+50;
                y=y+70;
            }*/


        }




    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }


    @Override
    protected boolean onLevelChange(int level) {
        invalidateSelf();
        return true;
    }



    private void draw_circle(float x,float y,float radius,int NUM_RECTS,int level,Canvas canvas,float width,
                            float height,float increment_x,float increment_y,Paint mPaint)
    {


        for (int i =0; i<NUM_RECTS; i++) {

            if((i+1)*10000/NUM_RECTS>level)
            {
                mPaint.setColor(Color.GRAY);
            }else{
                if((i>0) &&(i<3)){
                    mPaint.setColor(Utility.getColorWrapper(context, R.color.sig_strength_hi));
                }
               else if((i > 2) && (i < 6))
                    mPaint.setColor(Utility.getColorWrapper(context, R.color.sig_strength_lo));
                else if ((i > 5) && (i < 8))
                    mPaint.setColor(Utility.getColorWrapper(context, R.color.sig_strength_mid));

                else if(i>7 &&i<NUM_RECTS)
                    mPaint.setColor(Utility.getColorWrapper(context, R.color.sig_strength_top));
                else //if ((i > 6) && (i < 11))
                    mPaint.setColor(Utility.getColorWrapper(context, R.color.top_strength_color));

            }
            //canvas.drawRect(0,height-x,width,height-y, mPaint);
            canvas.drawCircle(width/2, height-y,radius, mPaint);

            x=x+increment_x;
            y=y+increment_y;
        }

    }
}
