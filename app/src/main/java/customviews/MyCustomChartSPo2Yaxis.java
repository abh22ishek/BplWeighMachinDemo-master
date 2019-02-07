package customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import logger.Level;
import logger.Logger;
import test.bpl.com.bplscreens.UserTestReportGraphActivity;

import static android.content.ContentValues.TAG;

/**
 * Created by abhishek.raj on 28-02-2018.
 */

public class MyCustomChartSPo2Yaxis extends View {

    int pixels_per_5unit=0;
    Paint mPaint;
    public MyCustomChartSPo2Yaxis(Context context) {
        super(context);
        init();
    }

    public MyCustomChartSPo2Yaxis(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(4);

        canvas.drawText("80",0,getHeight(),mPaint);
        canvas.drawText("100",0,35f,mPaint);
        canvas.drawText("90",0,getHeight()/2+12f,mPaint);
        canvas.drawText("95",0,getHeight()/4+25f,mPaint);
        canvas.drawText("85",0,3*getHeight()/4,mPaint);



    }

    private void init()
    {
        int density=getResources().getDisplayMetrics().densityDpi;
        pixels_per_5unit= (int) (density/5f);// 5 units per box used only for y axis
        mPaint=new Paint();
        mPaint.setTextSize(24);
    }






    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(70,5*(int)pixels_per_5unit);
    }


}
