package canny;

import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;

public class IweighChartYaxis extends View {

    Paint mTextPaint;

    int nStartX=0;
    int nStartY=0;


    int pixels_per_unit=0;


    public IweighChartYaxis(Context context) {
        super(context);
        init();
    }

    public IweighChartYaxis(Context context,  AttributeSet attrs) {
        super(context, attrs);
        init();
    }



    private void init() {
        int density = getResources().getDisplayMetrics().densityDpi;
        pixels_per_unit = (int) (density / 5f);
        if(mTextPaint==null){
            mTextPaint=new Paint();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(24f);
        mTextPaint.setAntiAlias(true);

        nStartX=10;
        nStartY=0;

        // draw txt for vertical labels

            canvas.drawText("180",nStartX,nStartY+17,mTextPaint);
            nStartY=pixels_per_unit+2;
            canvas.drawText("160",nStartX,nStartY+5,mTextPaint);
            canvas.drawText("140",nStartX,2*nStartY,mTextPaint);
            canvas.drawText("120",nStartX,3*nStartY,mTextPaint);
            canvas.drawText("100",nStartX,4*nStartY,mTextPaint);
            canvas.drawText("80",nStartX,5*nStartY,mTextPaint);
            canvas.drawText("60",nStartX,6*nStartY,mTextPaint);
        canvas.drawText("40",nStartX,7*nStartY,mTextPaint);
        canvas.drawText("20",nStartX,8*nStartY,mTextPaint);

        canvas.drawText("0",nStartX,9*nStartY,mTextPaint);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(60,10*pixels_per_unit);
    }







}
