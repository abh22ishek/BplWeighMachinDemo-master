package test.bpl.com.bplscreens;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.*;

import constantsP.Constants;
import database.DatabaseManager;
import logger.*;


public class ForgotPasswordActivity extends Activity {

    Button verify;
    private EditText security_question1,security_question2,security_question3;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





        setContentView(R.layout.forgot_password);
        verify= findViewById(R.id.verify);

        security_question1=  findViewById(R.id.securityques1);
        security_question2= findViewById(R.id.securityques2);
        security_question3=  findViewById(R.id.securityques3);

        final int density = getResources().getDisplayMetrics().densityDpi;

        if(density== DisplayMetrics.DENSITY_XHIGH)
            security_question3.setHint(getString(R.string.security_question_3_));
        else
            security_question3.setHint(getString(R.string.security_question_3));

        if(getIntent().getExtras().get(Constants.USER_NAME)!=null)
        {
           username= (String) getIntent().getExtras().get(Constants.USER_NAME);
        }



        verify.setText(getResources().getString(R.string.verify));

        verify.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if(security_question1.getText().toString().trim().equals(""))
               {
                   security_question1.setError(getResources().getString(R.string.field_empty));
                   return;
               }

                if(security_question2.getText().toString().trim().equals(""))
                {
                    security_question2.setError(getResources().getString(R.string.field_empty));
                    return;
                }

              HashMap<Boolean,String> hashMap=  DatabaseManager.getInstance().IssecurityQvalid(username,security_question1.getText().toString().
                        toLowerCase().trim(),security_question2.getText().toString().toLowerCase().trim());

                if(hashMap.size()>0)
                {

                    startActivity(new Intent(ForgotPasswordActivity.this, ResetPasswordActivity.class)
                            .putExtra(Constants.USER_NAME,username));
                    String useType=hashMap.get(true);
                    Logger.log(Level.DEBUG,"ForGotPass Activity()","Use Type is "+useType);

                }else{

                    Toast.makeText(ForgotPasswordActivity.this,"Sorry,wrong answer",Toast.LENGTH_LONG).show();
                }







            }
        });




    }


    /*private void send_verification_code()
    {
        String phoneNo = security_question1.getText().toString();
        String msg ="2222";
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),
                    ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }*/
}
