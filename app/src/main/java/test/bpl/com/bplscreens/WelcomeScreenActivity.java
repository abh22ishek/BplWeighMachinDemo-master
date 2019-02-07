package test.bpl.com.bplscreens;

import android.annotation.*;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import constantsP.Constants;
import customviews.MyBounceInterpolator;
import database.DatabaseManager;
import localstorage.StoreCredentials;
import logger.Level;
import logger.Logger;


public class WelcomeScreenActivity extends FragmentActivity {

  ImageView profile_pic;
    TextView username;

    Handler mHandler;

    String mUsername="";
    TextView sign_different_user;
    ImageView proceed;

    private final String TAG=WelcomeScreenActivity.class.getSimpleName();
    Intent intent;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.welcome_user);

        profile_pic=  findViewById(R.id.profile_pic);
        username= findViewById(R.id.user_name);
        mUsername=getIntent().getStringExtra(Constants.USER_NAME);
        username.setText(new StringBuilder().append("Welcome back  ").append(mUsername).toString());
        sign_different_user= findViewById(R.id.sign_different_user);
        proceed=  findViewById(R.id.proceed);

        mHandler=new Handler(getMainLooper());





        sign_different_user.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction()==MotionEvent.ACTION_UP)
                {
                    StoreCredentials.loggedout(WelcomeScreenActivity.this);
                    intent=new Intent(WelcomeScreenActivity.this,OximeterMainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                return true;
            }
        });


        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseManager.getInstance().openDatabase();
                if(DatabaseManager.getInstance().IsProfileExists())
                    startActivity(new Intent(WelcomeScreenActivity.this,
                            SelectParameterActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                 //   startActivity(new Intent(WelcomeScreenActivity.this, MenuPageIndicatorActivity.class));

                else
               startActivity(new Intent(WelcomeScreenActivity.this, UsersProfileActivity.class).
                        putExtra(Constants.PROFILE_FLAG,Constants.PROFILE_ADD));




            }
        });

        display_image(WelcomeScreenActivity.this);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                final Animation myAnim = AnimationUtils.loadAnimation(WelcomeScreenActivity.this, R.anim.scale);
                myAnim.setInterpolator(interpolator);
                proceed.startAnimation(myAnim);
                profile_pic.startAnimation(myAnim);
            }
        },400);


    }


    private void display_image(Context context)
    {
        if(get_profile_image(mUsername)!=""|| !get_profile_image(mUsername).equals(""))
        {
         //    byte[] imageAsBytes = Base64.decode(get_profile_image(mUsername).getBytes(), Base64.DEFAULT);
           //  Bitmap bitmap= BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
           //   profile_pic.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 150, 150, false));

            Uri uri=Uri.parse(get_profile_image(mUsername));
                Logger.log(Level.DEBUG, TAG, "Uri=" + uri);
            if(uri!=null)
            {
                Glide
                        .with(WelcomeScreenActivity.this)
                        .load(uri)
                        .override(200, 200)
                                            //resizes the image to these dimensions (in pixel). does not respect aspect ratio
                        .into(new GlideDrawableImageViewTarget(profile_pic) {
                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                                super.onResourceReady(resource, animation);

                                Logger.log(Level.DEBUG,TAG,"Glide loaded the image successfully");
                            }

                            @Override
                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                super.onLoadFailed(e, errorDrawable);

                                Logger.log(Level.ERROR,TAG,e.toString());
                            }
                        });
            }


        }else
            profile_pic.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.user_icon));

    }

    private String get_profile_image(String key_username)
    {
        SharedPreferences profile_image;
        profile_image = WelcomeScreenActivity.this.getSharedPreferences(Constants.PREFERENCE_PROFILE_IMAGE, Context.MODE_PRIVATE);

        String imagee_str=profile_image.getString(key_username,"");
        Logger.log(Level.INFO, TAG, "get profile image from shared pref=" + imagee_str);
        return imagee_str;

    }

    @Override
    protected void onResume() {
        super.onResume();

        }
}
