package test.bpl.com.bplscreens;
import android.annotation.*;
import android.app.*;
import android.content.*;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.*;
import android.view.inputmethod.*;
import android.widget.*;

import java.util.*;

import constantsP.Constants;
import database.DatabaseManager;
import logger.*;


public class ForgotPasswordActivity extends Activity {

    Button verify;
    private EditText security_question1,security_question2,security_question3;
    String username;
    final String[] colors = {"Red", "Green", "Blue", "Yellow", "Orange"};
    @SuppressLint("ClickableViewAccessibility")
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



        security_question1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(MotionEvent.ACTION_DOWN==motionEvent.getAction())
                {
                    hide_soft_keypad(ForgotPasswordActivity.this);

                    onCreateDialog(colors,"1");
                }
                return true;
            }
        });
        security_question2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(MotionEvent.ACTION_DOWN==motionEvent.getAction())
                {
                    hide_soft_keypad(ForgotPasswordActivity.this);
                    datePicker();
                }
                return true;
            }
        });

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

    private int mYear, mMonth, mDay, mHour, mMinute;
    public void datePicker(){


        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,R.style.datepicker,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        security_question2.setText(new StringBuilder().append(dayOfMonth).
                                append("-").append(monthOfYear + 1).append("-").append(year).toString());

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
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

    private void hide_soft_keypad(Context context) {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    int index=0;

    public void onCreateDialog(final String [] arrayC, final String params) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPasswordActivity.this,R.style.AlertDialogCustom);
        builder.setTitle(getString(R.string.pick_col));

// add a radio button list

        final int checkedItem = 0; // cow

        if(params.equals("1"))
            security_question1.setText(arrayC[checkedItem]);
        else
            security_question3.setText(arrayC[checkedItem]);
        builder.setSingleChoiceItems(arrayC, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user checked an item
                if(which!=-1){
                    if(params.equals("1"))
                        security_question1.setText(arrayC[which]);
                    else
                        security_question3.setText(arrayC[which]);

                    dialog.dismiss();
                }
            }
        });

        // add OK and Cancel buttons



        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
