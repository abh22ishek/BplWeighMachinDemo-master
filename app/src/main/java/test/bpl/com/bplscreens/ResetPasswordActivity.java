package test.bpl.com.bplscreens;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import constantsP.Constants;
import database.DatabaseManager;
import logger.Level;
import logger.Logger;



public class ResetPasswordActivity extends Activity {

    Button verify;
    EditText password2,password1;
    String username="";

    private String TAG=ResetPasswordActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);



        verify= findViewById(R.id.verify);
        password1=  findViewById(R.id.registered_mobile_no);
        password2=  findViewById(R.id.verification_code);

        password1.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        password1.setHint("Enter new Password");
        password2.setHint("Confirm Password");
        password1.addTextChangedListener(watcher);

        verify.setText(getString(R.string.reset_pwd));


        if(getIntent().getExtras().getString(Constants.USER_NAME)!=null)
        {
            username=getIntent().getExtras().getString(Constants.USER_NAME);
        }



        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Logger.log(Level.INFO, TAG,"Username ="+username);

                if(password1.getText().toString().trim().equals(password2.getText().toString().trim()))
                {
                    // Here update the password for current user name int the database
                    DatabaseManager.getInstance().update_password(username,password1.getText().toString().trim());

                    Toast.makeText(ResetPasswordActivity.this,"Password has been reset successfully",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(ResetPasswordActivity.this,OximeterMainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(ResetPasswordActivity.this,"Password didn't match",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }



    TextWatcher watcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {


        }

        @Override
        public void afterTextChanged(Editable s) {


            if(s.length()<6)
            {
                password1.setError("Minimum length of password is 6");
                password1.requestFocus();
            }else{
                password1.setError("ok");
              //  password1.clearFocus();
            }
        }
    };
}
