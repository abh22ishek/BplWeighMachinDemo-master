package customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;


public class CurvePathView extends View {

    Paint paint;
    int marginX_outer=0;
    int marginY_outer=0;

    int marginX_inner=0;
    int marginY_inner=0;
    private float density;

    Paint p1;


    public CurvePathView(Context context) {
        super(context);
        init();
    }

    public CurvePathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init()
    {
        paint=new Paint();
         p1=new Paint();
        // paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        density = getResources().getDisplayMetrics().densityDpi;

        if(density==DisplayMetrics.DENSITY_HIGH)
        {
            marginX_outer=18;
            marginY_outer=30;
            marginX_inner=45;
            marginY_inner=60;

        }else {
            marginX_outer=30;
            marginY_outer=40;
            marginX_inner=70;
            marginY_inner=80;

        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);




        Path path=new Path();
        RectF rectF=new RectF(marginX_outer,marginY_outer,getWidth()-marginX_outer,getHeight()+marginY_outer);
        path.addArc(rectF,180f,180f);

        if(density== DisplayMetrics.DENSITY_HIGH)
        {
            paint.setStrokeWidth(30f);
            p1.setStrokeWidth(30f);
        }else{
            paint.setStrokeWidth(50f);
            p1.setStrokeWidth(50f);
        }

        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        // path.addArc();

        //  int [] colors={Color.RED,Color.GREEN,Color.YELLOW,Color.RED};

        int [] colors={Color.RED,Color.parseColor("#628BAB"),Color.parseColor("#628CAB"),Color.parseColor("#669999")
                ,Color.parseColor("#7CBC50"),Color.YELLOW,Color.RED};
        float [] position={0.25f,0.25f,1f};
        SweepGradient sweepGradient=new SweepGradient(getWidth()/2,getHeight()/2,colors,null);
        paint.setShader(sweepGradient);

        canvas.drawPath(path,paint);



        Paint p=new Paint();

        p.setColor(Color.BLACK);

        Path innerpath=new Path();
        RectF r2= new RectF(marginX_inner,marginY_inner,getWidth()-marginX_inner,getHeight());
        innerpath.addArc(r2,180f,180f);

        p1.setColor(Color.WHITE);

        p1.setAntiAlias(true);
        p1.setStyle(Paint.Style.STROKE);
        canvas.drawPath(innerpath,p1);



        if(density== DisplayMetrics.DENSITY_XHIGH)
        {
            p.setTextSize(22f);
            canvas.drawTextOnPath("U N  D E R W E I G H T",path,2*marginX_outer,5f,p);
            canvas.drawTextOnPath("N O R M A L",path,(getWidth()+5*marginX_outer)/2,5f,p);
            canvas.drawTextOnPath("O V E R W E I G H T",path,getWidth()+2*marginX_outer,5f,p);

            p.setColor(Color.WHITE);
            p.setTextSize(30);
            canvas.drawTextOnPath("16",innerpath,10f,50f,p);
            canvas.drawTextOnPath("25",innerpath,getWidth()-marginX_inner-marginX_outer+20f,50f,p);
            canvas.drawTextOnPath("18.5",innerpath,3*marginX_inner,50f,p);
            canvas.drawTextOnPath("40",innerpath,getWidth()+2*marginX_inner-20,50f,p);

            p.setTextSize(22f);
            p.setTextSize(60);
            p.setColor(Color.GRAY);
            p.setAntiAlias(true);
          //  canvas.drawTextOnPath("|",innerpath,17f,20f,p);
           // canvas.drawTextOnPath("|",innerpath,getWidth()-marginX_inner-marginX_outer+20f,20f,p);
           // canvas.drawTextOnPath("|",innerpath,3*marginX_inner+10f,20f,p);
           // canvas.drawTextOnPath("|",innerpath,getWidth()+2*marginX_inner-10,20f,p);


        }else if(density==DisplayMetrics.DENSITY_XXHIGH)
        {
            p.setTextSize(25f);
            canvas.drawTextOnPath("U N  D E R W E I G H T",path,2*marginX_outer,5f,p);
            canvas.drawTextOnPath("N O R M A L",path,(getWidth()+10*marginX_outer)/2,5f,p);
            canvas.drawTextOnPath("O V E R W E I G H T",path,getWidth()+6*marginX_outer,5f,p);

            p.setColor(Color.WHITE);
            p.setTextSize(50);
            canvas.drawTextOnPath("16",innerpath,5f,70f,p);
            canvas.drawTextOnPath("18.5",innerpath,5*marginX_inner,70f,p);
            canvas.drawTextOnPath("25",innerpath,getWidth()-marginX_inner-2*marginX_outer,70f,p);
            canvas.drawTextOnPath("40",innerpath,getWidth()+3*marginX_inner+50f,70f,p);

            p.setTextSize(22f);
            p.setTextSize(60);
            p.setColor(Color.GRAY);
            p.setAntiAlias(true);

            //canvas.drawTextOnPath("|",innerpath,20f,20f,p);

           // canvas.drawTextOnPath("|",innerpath,5*marginX_inner+25f,20f,p);
           // canvas.drawTextOnPath("|",innerpath,getWidth()-marginX_inner-marginX_outer,20f,p);
           // canvas.drawTextOnPath("|",innerpath,getWidth()+4*marginX_inner-20f,20f,p);
        }

        else if(density==DisplayMetrics.DENSITY_HIGH)
        {
            p.setTextSize(16f);
            canvas.drawTextOnPath("U N  D E R W E I G H T",path,2*marginX_outer,5f,p);
            canvas.drawTextOnPath("N O R M A L",path,(getWidth()+7*marginX_outer)/2,5f,p);
            canvas.drawTextOnPath("O V E R W E I G H T",path,getWidth()+5*marginX_outer,5f,p);

            p.setColor(Color.WHITE);
            p.setTextSize(25);
            canvas.drawTextOnPath("16",innerpath,10f,50f,p);
            canvas.drawTextOnPath("25",innerpath,getWidth()-marginX_inner-marginX_outer-10,50f,p);
            canvas.drawTextOnPath("18.5",innerpath,3*marginX_inner,50f,p);
            canvas.drawTextOnPath("40",innerpath,getWidth()+2*marginX_inner-20,50f,p);

            p.setTextSize(20f);
            p.setTextSize(40);
            p.setColor(Color.GRAY);
            p.setAntiAlias(true);
           // canvas.drawTextOnPath("|",innerpath,10f,20f,p);
           // canvas.drawTextOnPath("|",innerpath,getWidth()-marginX_inner-marginX_outer+10f,20f,p);
           // canvas.drawTextOnPath("|",innerpath,3*marginX_inner+10f,20f,p);
           // canvas.drawTextOnPath("|",innerpath,getWidth()+2*marginX_inner-10,20f,p);

        }

        int x_offset=0;
        p.setColor(Color.BLACK);
        p.setTextSize(23);
        PathMeasure pathMeasure=new PathMeasure(innerpath,false);
        int length= (int)pathMeasure.getLength();
        for(int i=0;i<length;i++)
        {
            canvas.drawTextOnPath("|",innerpath,x_offset,18f,p);
            x_offset=x_offset+30;
        }
        p.setStrokeWidth(5f);
        p.setTextSize(20f);



    }


    @Override
    public void setOnTouchListener(OnTouchListener l) {
        super.setOnTouchListener(l);

    }




}
