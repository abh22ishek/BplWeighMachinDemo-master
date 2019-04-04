package canny;

import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;

public class IweighBodyFatYaxis extends View {

    Paint mTextPaint;

    int nStartX=0;
    int nStartY=0;
    int pixels_per_unit=0;


    public IweighBodyFatYaxis(Context context) {
        super(context);
        init();
    }

    public IweighBodyFatYaxis(Context context, AttributeSet attrs) {
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
        canvas.drawText("60",nStartX,10+nStartY,mTextPaint);
        nStartY=pixels_per_unit+2;

        canvas.drawText("50",nStartX,1*nStartY,mTextPaint);
        canvas.drawText("40",nStartX,2*nStartY,mTextPaint);
        canvas.drawText("30",nStartX,3*nStartY,mTextPaint);
        canvas.drawText("20",nStartX,4*nStartY,mTextPaint);
        canvas.drawText("10",nStartX,5*nStartY,mTextPaint);
        canvas.drawText("0",nStartX,6*nStartY,mTextPaint);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(60,7*pixels_per_unit);
    }

}
