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

public class MyCustomChartHRDayYaxis extends View {

    float pixels_per_5unit=0;
    Paint mPaint,p;



    public MyCustomChartHRDayYaxis(Context context) {
        super(context);
        init();
    }

    public MyCustomChartHRDayYaxis(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init()
    {
        mPaint=new Paint();
        mPaint.setTextSize(24);

        int density=getResources().getDisplayMetrics().densityDpi;
        pixels_per_5unit= (int) (density/5f);// 5 units per box used only for y axis


    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(4);


        canvas.drawText("0",0,getHeight()-15f,mPaint);
        canvas.drawText("200",0,20,mPaint);
        canvas.drawText("100",0,getHeight()/2+7,mPaint);
        canvas.drawText("150",0,getHeight()/4+10f,mPaint);
        canvas.drawText("50",0,3*getHeight()/4-5f,mPaint);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(70,5*(int)pixels_per_5unit);
    }
}
