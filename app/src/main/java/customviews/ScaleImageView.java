package customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.util.Arrays;

import logger.Level;
import logger.Logger;


public class ScaleImageView extends ImageView {

    private final String TAG = ScaleImageView.class.getSimpleName();

    private Context mContext;

    // These matrices will be used to move and zoom image
    private Matrix mMatrix = new Matrix();
    private Matrix mSavedMatrix = new Matrix();

    // We can be in one of these 3 states
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private static final float MAX_ZOOM = 5.0f;
    private static final float MIN_ZOOM = 0.15f;
    private int mode = NONE;

    // Remember some things for zooming
    private PointF mStart = new PointF();
    private PointF mMid = new PointF();
    private float mOldDist = 1f;

    private float[] mLastEvent = null;
    private float mPrevRotation = 0f;
    private float mNewRotation = 0f;

    private float[] initialBounds = new float[8];
    private float[] resultingBounds = new float[8];

    private PointF initialMidPoint;

    private float initialTranslateX;
    private float initialTranslateY;
    private float circleRadius;

    enum Dimension {
        DIMEN_WIDTH,
        DIMEN_HEIGHT
    }

    private float maxDistance;

    private Dimension longestDimension;

    public ScaleImageView(Context context) {
        super(context);
        initializeImageMatrix();
    }

    public ScaleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeImageMatrix();
    }

    public ScaleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeImageMatrix();
    }
    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        initializeImageMatrix();
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        initializeImageMatrix();
    }

    private void initializeImageMatrix() {
        this.setScaleType(ScaleType.MATRIX);
        this.mMatrix = new Matrix();
        Drawable d = getDrawable();
        if (d != null) {
            // Getting all the dimension in float to calculate the scale correctly without typecasting.
            float imageWidth = d.getIntrinsicWidth();
            float imageHeight = d.getIntrinsicHeight();

            float viewWidth = getMeasuredWidth();
            float viewHeight = getMeasuredHeight();

            circleRadius = viewWidth/2; // Or viewHeight/2 as height and width same

            float xScale = viewWidth/imageWidth;
            float yScale = viewHeight/imageHeight;

            float minScale = Math.min(xScale, yScale);

            mMatrix.postScale(minScale, minScale);

            int scaledImageWidth = (int)(imageWidth * minScale);

            int scaledImageHeight = (int)(imageHeight * minScale);

            if(scaledImageWidth < viewWidth){
                initialTranslateX = (float)(viewWidth - scaledImageWidth) / 2;
                mMatrix.postTranslate(initialTranslateX, 0);
            }
            if(scaledImageHeight < viewHeight){
                initialTranslateY = (int)(viewHeight - scaledImageHeight) / 2;
                mMatrix.postTranslate(0, initialTranslateY);
            }

            setImageMatrix(mMatrix);

            initialBounds[0] = 0;
            initialBounds[1] = 0;
            initialBounds[2] = imageWidth;
            initialBounds[3] = 0;
            initialBounds[4] = imageWidth;
            initialBounds[5] = imageHeight;
            initialBounds[6] = 0;
            initialBounds[7] = imageHeight;

            Logger.log(Level.DEBUG,TAG,"Initial Bounds:" + Arrays.toString(initialBounds));
            mMatrix.mapPoints(resultingBounds, initialBounds);

            Logger.log(Level.DEBUG, TAG, "Initial Resulting Bounds:" + Arrays.toString(resultingBounds));
            PointF point1 = new PointF(resultingBounds[0],resultingBounds[1]);
            PointF point2 = new PointF(resultingBounds[2],resultingBounds[3]);
            PointF point3 = new PointF(resultingBounds[4], resultingBounds[5]);

            initialMidPoint = new PointF();
            midPoint(initialMidPoint, point1, point3);

            Logger.log(Level.DEBUG, TAG,"Initial Mid Points1: ( " + initialMidPoint.x + ", " + initialMidPoint.y + " )");


            float hDistance = distance(point1, point2);
            float vDistance = distance(point2, point3);

            if(hDistance > vDistance){
                maxDistance = scaledImageWidth;
                longestDimension = Dimension.DIMEN_WIDTH;
            } else{
                maxDistance = scaledImageHeight;
                longestDimension = Dimension.DIMEN_HEIGHT;
            }
        }

    }

    public boolean onTouchEvent(MotionEvent event) {

        // Handle touch events here...
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mSavedMatrix.set(mMatrix);
                mStart.set(event.getX(), event.getY());
                mode = DRAG;
                mLastEvent = null;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mOldDist = spacing(event);
                if (mOldDist > 10f) {
                    mSavedMatrix.set(mMatrix);
                    midPoint(mMid, event);
                    mode = ZOOM;
                }
                mLastEvent = new float[4];
                mLastEvent[0] = event.getX(0);
                mLastEvent[1] = event.getX(1);
                mLastEvent[2] = event.getY(0);
                mLastEvent[3] = event.getY(1);
                mPrevRotation = rotation(event);
                break;
            case MotionEvent.ACTION_UP:
                adjustResultingBounds();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                mLastEvent = null;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    // ...
                    mMatrix.set(mSavedMatrix);
                    mMatrix.postTranslate(event.getX() - mStart.x, event.getY()
                            - mStart.y);
                } else if (mode == ZOOM && event.getPointerCount() == 2) {
                    float newDist = spacing(event);
                    mMatrix.set(mSavedMatrix);
                    if (newDist > 10f) {
                        float scale = newDist / mOldDist;
                        mMatrix.postScale(scale, scale, mMid.x, mMid.y);
                    }
                    if (mLastEvent != null) {
                        mNewRotation = rotation(event);
                        float r = mNewRotation - mPrevRotation;
                        mMatrix.postRotate(r, (float)getMeasuredWidth() / 2,
                                (float)getMeasuredHeight() / 2);
                    }
                }
                break;
            default:
                break;
        }

        setImageMatrix(mMatrix);

        return true;
    }

    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);

        return (float) Math.toDegrees(radians);
    }

    /** Determine the space between the first two fingers */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);

        return (float) Math.sqrt(x * x + y * y);
    }

    /** Calculate the mMid point of the first two fingers */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    private float distance(PointF point1, PointF point2){
        float x = point1.x - point2.x;
        float y = point1.y - point2.y;
        return (float) Math.sqrt(x * x + y * y);
    }

    /** Calculate the mMid point of the first two fingers */
    private void midPoint(PointF pointResult, PointF point1, PointF point2) {
        float x = point1.x + point2.x;
        float y =  point1.y + point2.y;;
        pointResult.set(x / 2, y / 2);
    }

    /**
     * This will check if the image has been scaled down to a dimension below the desired
     * boundary circle. It also checks if the image is translated beyond the desired boundary.
     * In such case the image will be repositioned with the desired scaling and translation.
     */
    private void adjustResultingBounds(){
        mMatrix.mapPoints(resultingBounds, initialBounds);
        Logger.log(Level.DEBUG,TAG, "Resulting Bounds:" + Arrays.toString(resultingBounds));

        PointF point1 = new PointF(resultingBounds[0],resultingBounds[1]);
        PointF point2 = new PointF(resultingBounds[2],resultingBounds[3]);
        PointF point3 = new PointF(resultingBounds[4], resultingBounds[5]);
        PointF point4 = new PointF(resultingBounds[6], resultingBounds[7]);

        float currentWidth = 0;
        if(longestDimension == Dimension.DIMEN_WIDTH){
            currentWidth = distance(point1, point2);
        } else if(longestDimension == Dimension.DIMEN_HEIGHT){
            currentWidth = distance(point2, point3);
        }

        if(currentWidth < maxDistance){

            // This block is for verifying the scale down below the desired dimension

            mMatrix.mapPoints(resultingBounds, initialBounds);

            float scale = maxDistance / currentWidth;
            mMatrix.postScale(scale, scale);
            mMatrix.mapPoints(resultingBounds, initialBounds);

            point1 = new PointF(resultingBounds[0],resultingBounds[1]);
            point3 = new PointF(resultingBounds[4], resultingBounds[5]);

            PointF currentMidPoint = new PointF();
            midPoint(currentMidPoint, point1, point3);

            float xTranslate = initialMidPoint.x - currentMidPoint.x;
            float yTranslate = initialMidPoint.y - currentMidPoint.y;

            mMatrix.postTranslate(xTranslate, yTranslate);
            mMatrix.mapPoints(resultingBounds, initialBounds);

            Logger.log(Level.DEBUG, TAG, "Resulting Mid Points1: ( " + currentMidPoint.x + ", " + currentMidPoint.y + " )");

            setImageMatrix(mMatrix);
        } else {

            // This block is for verifying the translation beyond the desired boundary.

            boolean matrixUpdated = false;
            float distanceSide1 = distanceFromPerimeter(point1, point2);
            float widthSide1 = distance(point1, point4);
            if(distanceSide1 > 0 || (distanceSide1 + widthSide1) < 2*circleRadius){
                float desiredDistance = circleRadius - widthSide1/2;
                float desiredChange = desiredDistance - distanceSide1;

                double angle = getAngle(point1, point2);
                float xTranslate = (float)(desiredChange * Math.cos(angle));
                float yTranslate = (float)(desiredChange * Math.sin(angle));
                mMatrix.postTranslate(xTranslate, yTranslate);
                matrixUpdated = true;
            }

            float distanceSide2 = distanceFromPerimeter(point2, point3);
            float widthSide2 = distance(point1, point2);
            if(distanceSide2 > 0 || (distanceSide2 + widthSide2) < 2*circleRadius){
                float desiredDistance = circleRadius - widthSide2/2;
                float desiredChange = desiredDistance - distanceSide2;

                double angle = getAngle(point2, point3);
                float xTranslate = (float)(desiredChange * Math.cos(angle));
                float yTranslate = (float)(desiredChange * Math.sin(angle));
                mMatrix.postTranslate(xTranslate, yTranslate);
                matrixUpdated = true;
            }

            if(matrixUpdated){
                setImageMatrix(mMatrix);
            }
        }
    }

    private float distanceFromPerimeter(PointF linePoint1, PointF linePoint2){
        float distanceFromCenter = getDistance(linePoint1, linePoint2, initialMidPoint);
        return circleRadius + distanceFromCenter;
    }

    private float getDistance(PointF linePoint1, PointF linePoint2, PointF fromPoint){
        // The formula for calculating distance between a line with two points (x1,y1) and (x2,y2)
        // from another point (x0,y0) is
        //((y2-y1)*x1 - (x2-x1)*y1 + x2*y1 - y2*x1)/Math.sqrt(Math.pow(x2 - x1), 2) + Math.sqrt(Math.pow(y2 - y1), 2)

        float numerator =
                (linePoint2.y - linePoint1.y)*fromPoint.x
                        - (linePoint2.x - linePoint1.x)*fromPoint.y
                        + (linePoint2.x * linePoint1.y)
                        - (linePoint2.y*linePoint1.x);

        float denominator = (float)Math.sqrt(
                Math.pow((linePoint2.x - linePoint1.x), 2)
                        + Math.pow((linePoint2.y - linePoint1.y), 2));

        if(denominator != 0){
            return numerator/denominator;
        }
        return 0;
    }

    public double getAngle(PointF point1, PointF point2) {
        float delta_x = point1.x  - point2.x;
        float delta_y = point1.y - point2.y;
        double theta_radians = Math.atan2(delta_y, delta_x);
        double perpAngle = theta_radians - Math.PI/2;
        return perpAngle;
    }
    public Bitmap getCropBitmap(){
        if(getDrawable() == null){
            return null;
        }

        Bitmap sourceBitmap = ((BitmapDrawable)getDrawable()).getBitmap();
        Bitmap croppedBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(croppedBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawBitmap(sourceBitmap, mMatrix, paint);

        return croppedBitmap;
    }
}
