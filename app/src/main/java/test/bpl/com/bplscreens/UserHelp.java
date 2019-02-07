package test.bpl.com.bplscreens;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import constantsP.Constants;
import logger.Level;
import logger.Logger;


public class UserHelp extends Activity {

    ImageView mBackKey;
    TextView headertitle;

    private Button user_document,app_update,logOut,user_guide;

    private String TAG=UserHelp.class.getSimpleName();
    AlertDialog alert;
    PackageInfo pInfo = null;
     //RippleView rippleView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);

        mBackKey= findViewById(R.id.imgBackKey);
        mBackKey.setOnClickListener(mListner);


       // rippleView=(RippleView) findViewById(R.id.more);
       /* rippleView.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {

            @Override
            public void onComplete(RippleView rippleView) {

                Intent intent;
                intent=new Intent(UserHelp.this,UsersProfileActivity.class);
                intent.putExtra(ConstantsP.PROFILE_FLAG,ConstantsP.PROFILE_EDIT);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

        });*/
        headertitle=  findViewById(R.id.base_header_title);
        headertitle.setText(getString(R.string.help));

        user_document=  findViewById(R.id.userdocument);
        app_update=  findViewById(R.id.appupdate);
        logOut=  findViewById(R.id.logout);
        user_guide=  findViewById(R.id.userguide);


        user_document.setOnClickListener(mListner);
       app_update.setVisibility(View.VISIBLE);
        app_update.setOnClickListener(mListner);
        logOut.setOnClickListener(mListner);
        user_guide.setOnClickListener(mListner);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               // app_update.setAlpha(3);
            }
        }, 1000);


    }


    View.OnClickListener mListner= new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent;

            switch (v.getId())
            {
                case R.id.appupdate:
                    try {
                        pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(UserHelp.this);
                    builder.setMessage(getResources().getString(R.string.app_version_str)+" "+"("+pInfo.versionName+")")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    alert.dismiss();



                                }
                            });


                    alert = builder.create();
                    alert.setCanceledOnTouchOutside(false);
                    Logger.log(Level.DEBUG, TAG, "Alert dialog box gets called");
                    alert.show();
                    break;


                case R.id.userdocument:
                    intent=new Intent(UserHelp.this,UsersProfileActivity.class);
                    intent.putExtra(Constants.PROFILE_FLAG,Constants.PROFILE_EDIT);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                    break;

                case R.id.logout:
                    break;

                case R.id.userguide:

                    intent=new Intent(UserHelp.this,UserGuideContent.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;

                case R.id.imgBackKey:

                    finish();
                    break;
            }




        }
    };







}
