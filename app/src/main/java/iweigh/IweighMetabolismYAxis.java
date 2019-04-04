package iweigh;

import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;

public class IweighMetabolismYAxis extends View {

    Paint mTextPaint;

    int nStartX=0;
    int nStartY=0;


    int pixels_per_unit=0;

    public IweighMetabolismYAxis(Context context) {
        super(context);
        init();
    }

    public IweighMetabolismYAxis(Context context, AttributeSet attrs) {
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

        canvas.drawText("2000",nStartX,nStartY+17,mTextPaint);
        nStartY=pixels_per_unit+2;
        canvas.drawText("1900",nStartX,nStartY+5,mTextPaint);
        canvas.drawText("1800",nStartX,2*nStartY,mTextPaint);
        canvas.drawText("1700",nStartX,3*nStartY,mTextPaint);
        canvas.drawText("1600",nStartX,4*nStartY,mTextPaint);
        canvas.drawText("1500",nStartX,5*nStartY,mTextPaint);
        canvas.drawText("1400",nStartX,6*nStartY,mTextPaint);
        canvas.drawText("1300",nStartX,7*nStartY,mTextPaint);
        canvas.drawText("1200",nStartX,8*nStartY,mTextPaint);

        canvas.drawText("1100",nStartX,9*nStartY,mTextPaint);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(60,10*pixels_per_unit);
    }



}
