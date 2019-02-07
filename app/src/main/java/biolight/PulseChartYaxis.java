package biolight;

import android.content.*;
import android.graphics.*;
import android.support.annotation.*;
import android.util.*;
import android.view.*;

/**
 * Created by abhishek.raj on 06-03-2018.
 */

public class PulseChartYaxis extends View {


    int pixels_per_unit=0;
    Paint mPaint;
    int nStartX,nStartY=0;



    public PulseChartYaxis(Context context) {
        super(context);
        init();
    }

    public PulseChartYaxis(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        int density = getResources().getDisplayMetrics().densityDpi;
        pixels_per_unit = (int) (density / 5f);

        if(mPaint==null){
            mPaint=new Paint();
        }


    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(24f);
        mPaint.setAntiAlias(true);


        nStartX=10;
        nStartY=0;


        // draw txt for vertical labels
        canvas.drawText("120",nStartX,nStartY+17,mPaint);
        nStartY=pixels_per_unit+2;
        canvas.drawText("100",nStartX,nStartY+5,mPaint);
        canvas.drawText("80",nStartX,2*nStartY,mPaint);
        canvas.drawText("60",nStartX,3*nStartY,mPaint);
        canvas.drawText("40",nStartX,4*nStartY,mPaint);
        canvas.drawText("20",nStartX,5*nStartY,mPaint);
        canvas.drawText("0",nStartX,6*nStartY,mPaint);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(60,7*pixels_per_unit);


    }
}
