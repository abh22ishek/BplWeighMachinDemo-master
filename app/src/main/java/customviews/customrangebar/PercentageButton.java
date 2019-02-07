package customviews.customrangebar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by admin on 28-03-2016.
 */
public class PercentageButton extends View {


    private Paint paintDefault;
    private Paint paintFilled;
    private RectF rectDefault;
    private RectF rectFilled;
    private int mWidth;
    private int mHeight;
    private float precentValue=20;



    public PercentageButton(Context context) {
        super(context);
    }

    public PercentageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    private void init() {

        paintDefault = new Paint();
        paintDefault.setColor(Color.GRAY);

        paintFilled = new Paint();
        paintFilled.setColor(Color.GREEN);


        rectDefault = new RectF();
        rectFilled = new RectF();


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mWidth = getWidth();
        mHeight = getHeight();


        rectDefault.set(10, 10, 200, 100);
        rectFilled.set(10, 10, precentValue, 100);

        canvas.drawRect(rectDefault, paintDefault);
        canvas.drawRect(rectFilled, paintFilled);


    }

    public void setPrecentValue(int precentValue) {
        this.precentValue = precentValue;
        invalidate();
    }
}
