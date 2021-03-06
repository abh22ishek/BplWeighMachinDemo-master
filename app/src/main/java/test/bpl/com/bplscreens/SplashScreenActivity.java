package test.bpl.com.bplscreens;

import android.content.*;
import android.os.*;
import android.support.v4.app.*;
import android.view.*;

import constantsP.*;
import database.*;
import logger.*;

public class SplashScreenActivity extends FragmentActivity {

    Handler mHandler;

    private final  String TAG=SplashScreenActivity.class.getSimpleName();

    private static final int splash_timeout=5000;


    GlobalClass globalVariable;
    String mUsername;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


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

        String mUserType=login_credentials.getString(Constants.USE_TYPE,"Home");


        if(mUsername!=null)
            b=true;
        else
            b=false;

        Logger.log(Level.DEBUG, TAG, "get value stored in a shared preference s file **User ID**" + mUsername);


            globalVariable.setUsername(mUsername);
            globalVariable.setUserType(mUserType);



        return b;

    }

}
