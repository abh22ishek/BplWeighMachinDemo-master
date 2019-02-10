package test.bpl.com.bplscreens;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import constantsP.Constants;
import constantsP.GlobalClass;
import database.DatabaseManager;
import logger.Level;
import logger.Logger;

public class SplashScreenActivity extends Activity {

    Handler mHandler;

    private final  String TAG=SplashScreenActivity.class.getSimpleName();

    private static final int splash_timeout=5000;


    GlobalClass globalVariable;
    String mUsername;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

        }else{
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }

        setContentView(R.layout.splashscreen);
        globalVariable = (GlobalClass) getApplicationContext();



    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.log(Level.DEBUG,TAG,"On Pause()");
        mHandler.removeCallbacksAndMessages(null);

    }

    @Override
    protected void onResume() {
        super.onResume();
       ;

        if(mHandler==null){
            mHandler=new Handler();
        }

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Logger.log(Level.DEBUG,TAG,"***On Resume() Called***");

                if(IsloggedIn())
                {
                    Intent intent = new Intent(SplashScreenActivity.this,WelcomeScreenActivity.class);
                    intent.putExtra(Constants.USER_NAME,mUsername);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }else{

                    Intent intent;
                    DatabaseManager.getInstance().openDatabase();
                    if(DatabaseManager.getInstance().IsFirstTimeLogin())
                     intent = new Intent(SplashScreenActivity.this, OximeterMainActivity.class);
                    else
                     intent = new Intent(SplashScreenActivity.this,SignUpActivity.class);

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                    finish();
            }
        }, splash_timeout);


    }

    // To check whetere the user has logged in or not

    // get username value of shared pref
    private boolean IsloggedIn()
    {
        boolean b=false;

        SharedPreferences login_credentials;
        login_credentials = SplashScreenActivity.this.getSharedPreferences(Constants.PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);

        mUsername=login_credentials.getString(Constants.USER_NAME, null);
        String mPwd= login_credentials.getString(Constants.PASSWORD, null);

        String mUserType=login_credentials.getString(Constants.USE_TYPE,null);


        if(mUsername!=null)
            b=true;
        else
            b=false;

        Logger.log(Level.DEBUG, TAG, "get value stored in a shared preference s file **User ID**" + mUsername);

        if(globalVariable.getUsername()==null)
        {
            globalVariable.setUsername(mUsername);
        }


        if(globalVariable.getUserType()==null){
            globalVariable.setUserType(mUserType);
        }
        return b;

    }

}
