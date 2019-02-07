package biolight;

import android.content.*;
import android.graphics.*;
import android.support.annotation.*;
import android.util.*;
import android.view.*;

import java.util.*;

public class CustomBPDayChartYaxis extends View {

    int pixels_per_unit=0;
    Paint mTextPaint;

    int nStartX=0;
    int nStartY=0;
    List<BPMeasurementModel> mRecords;

    int maximum;
    int minimum;

    public CustomBPDayChartYaxis(Context context) {
        super(context);
        init();
    }

    public CustomBPDayChartYaxis(Context context, @Nullable AttributeSet attrs) {
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

        if(maximum>140 && maximum <160){
            canvas.drawText("160",nStartX,nStartY+17,mTextPaint);
            nStartY=pixels_per_unit+2;
            canvas.drawText("140",nStartX,nStartY+5,mTextPaint);
            canvas.drawText("120",nStartX,2*nStartY,mTextPaint);
            canvas.drawText("100",nStartX,3*nStartY,mTextPaint);
            canvas.drawText("80",nStartX,4*nStartY,mTextPaint);
            canvas.drawText("60",nStartX,5*nStartY,mTextPaint);
            canvas.drawText("40",nStartX,6*nStartY,mTextPaint);
        }else if(maximum>=160 && maximum <210){
            canvas.drawText("210",nStartX,nStartY+17,mTextPaint);
            nStartY=pixels_per_unit+2;
            canvas.drawText("180",nStartX,nStartY+5,mTextPaint);
            canvas.drawText("150",nStartX,2*nStartY,mTextPaint);
            canvas.drawText("130",nStartX,3*nStartY,mTextPaint);
            canvas.drawText("100",nStartX,4*nStartY,mTextPaint);
            canvas.drawText("70",nStartX,5*nStartY,mTextPaint);
            canvas.drawText("40",nStartX,6*nStartY,mTextPaint);
        }

        else if(maximum>=130 && maximum<=140 ){

            canvas.drawText("140",nStartX,nStartY+17,mTextPaint);
            nStartY=pixels_per_unit+2;
            canvas.drawText("120",nStartX,nStartY+5,mTextPaint);
            canvas.drawText("100",nStartX,2*nStartY,mTextPaint);
            canvas.drawText("80",nStartX,3*nStartY,mTextPaint);
            canvas.drawText("60",nStartX,4*nStartY,mTextPaint);
            canvas.drawText("40",nStartX,5*nStartY,mTextPaint);
            canvas.drawText("20",nStartX,6*nStartY,mTextPaint);

        }

        else {
            canvas.drawText("130",nStartX,nStartY+17,mTextPaint);
            nStartY=pixels_per_unit+2;
            canvas.drawText("110",nStartX,nStartY+5,mTextPaint);
            canvas.drawText("90",nStartX,2*nStartY,mTextPaint);
            canvas.drawText("70",nStartX,3*nStartY,mTextPaint);
            canvas.drawText("50",nStartX,4*nStartY,mTextPaint);
            canvas.drawText("30",nStartX,5*nStartY,mTextPaint);
            canvas.drawText("10",nStartX,6*nStartY,mTextPaint);
        }

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(60,7*pixels_per_unit);
    }




    public void getRecords(List<BPMeasurementModel> mrec)
    {
        mRecords=mrec;
        maximum= largest(mRecords);
        minimum= Smallest(mRecords);
    }


    private int largest(List<BPMeasurementModel> mRecords)
    {

        int max= (int) mRecords.get(0).getSysPressure();

        for(BPMeasurementModel b: mRecords)
        {
            if(b.getSysPressure()>max)
            {
                max= (int) b.getSysPressure();
            }
        }

        return max;
    }




    private int Smallest(List<BPMeasurementModel> mRecords)
    {

        int min= (int) mRecords.get(0).getDiabolicPressure();


        for(BPMeasurementModel b: mRecords)
        {
            if(b.getDiabolicPressure()<min)
            {
                min= (int) b.getDiabolicPressure();
            }
        }

        return min;
    }
}
