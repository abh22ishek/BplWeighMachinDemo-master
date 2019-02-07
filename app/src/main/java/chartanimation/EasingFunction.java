package chartanimation;


import android.animation.TimeInterpolator;

public interface EasingFunction extends TimeInterpolator {
    @Override
    float getInterpolation(float v);
}
