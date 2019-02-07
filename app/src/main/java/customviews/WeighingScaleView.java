package customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;



public class WeighingScaleView extends View {

    Paint mPaint;

    public WeighingScaleView(Context context) {
        super(context);
        init();
    }

    public WeighingScaleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }





    private void init()
    {
        mPaint=new Paint();
        mPaint.setStrokeWidth(1.5f);
        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);


    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


       /* RectF r=new RectF(0,0,getWidth(),getHeight()/2);

        canvas.drawArc(r,0,-180,false,mPaint);

        RectF r1=new RectF(40,40,getWidth()-40,getHeight()/2-40);
        mPaint.setColor(Color.CYAN);
        canvas.drawArc(r1,0,-180,false,mPaint);*/


        float size = Math.min(getWidth(),getHeight());
        mPaint.setStrokeWidth(80);
        int [] colors={Color.BLUE,Color.GREEN,Color.YELLOW,Color.RED};
        float [] position={0,1,2,3};
        //mPaint.setShader(new LinearGradient(0, 0, 0, getHeight(),colors,null,Shader.TileMode.MIRROR));
        mPaint.setStyle(Paint.Style.STROKE);
        final RectF oval = new RectF(40,40,getWidth()-40,getHeight()/2-40);
        //oval.inset(size/4,size/4);

        mPaint.setColor(Color.WHITE);
        Path redPath = new Path();
        redPath.addArc(oval,180,180);
        canvas.drawPath(redPath, mPaint);

        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE );
        mPaint.setStrokeWidth(2);
        mPaint.setTextSize(25f);
        canvas.drawTextOnPath("Hello ",redPath,10,5,mPaint);

        /*mPaint.setColor(Color.GREEN);
        Path greenPath = new Path();
        greenPath.arcTo(oval, 120, 120, true);
        canvas.drawPath(greenPath, mPaint);

        mPaint.setColor(Color.BLUE);
        Path bluePath = new Path();
        bluePath.arcTo(oval, 240, 120, true);
        canvas.drawPath(bluePath, mPaint);

        mPaint.setStrokeWidth(2);
        mPaint.setColor(0xff000000);
        canvas.save();*/
       /* for(int i=0;i<360;i+=40){
            canvas.rotate(40,size/2,size/2);
            canvas.drawLine(size*3/4,size/2,size,size/2,mPaint);
        }
        canvas.restore();

        final RectF ovalOuter = new RectF(0, 0, getWidth(), getHeight());
        ovalOuter.inset(1,1);
        canvas.drawOval(ovalOuter,mPaint);

        final RectF ovalInner = new RectF(size/4, size/4, size*3/4,size*3/4);
        canvas.drawOval(ovalInner,mPaint);*/


    }
}
