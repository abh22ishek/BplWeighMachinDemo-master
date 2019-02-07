package test.bpl.com.bplscreens;

import android.annotation.*;
import android.app.*;
import android.content.*;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.*;

import java.util.*;

import constantsP.Constants;
import constantsP.GlobalClass;
import database.BplOximterdbHelper;
import database.DatabaseManager;
import localstorage.StoreCredentials;



public class SignUpActivity extends Activity{


    private EditText email_id,password;
    private Button btnLoginSignUp;

    private EditText security_question1,security_question2,security_question3;


    private String TAG=SignUpActivity.class.getSimpleName();
    GlobalClass globalVariable;

    SQLiteDatabase database;
    private TextView LogIn;

    private boolean isLoginProfileExists;
    final String[] colors = {"Red", "Green", "Blue", "Yellow", "Orange"};
    final String[] useTypeArr = {"Home","Clinic"};
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        globalVariable = (GlobalClass) getApplicationContext();

        email_id=  findViewById(R.id.email);
        password= findViewById(R.id.password);


        security_question1=  findViewById(R.id.securityques1);
        security_question2=  findViewById(R.id.securityques2);
        security_question3=  findViewById(R.id.securityques3);
        security_question1.setCursorVisible(false);
        security_question2.setCursorVisible(false);
        security_question3.setCursorVisible(true);


        btnLoginSignUp=  findViewById(R.id.btnLoginSignUp);
        btnLoginSignUp.setOnClickListener(listner);

        final int density = getResources().getDisplayMetrics().densityDpi;

        if(density== DisplayMetrics.DENSITY_XHIGH || density==DisplayMetrics.DENSITY_XXHIGH)
            security_question3.setHint(getString(R.string.security_question_3_));
            else
        security_question3.setHint(getString(R.string.security_question_3));


            security_question1.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if(MotionEvent.ACTION_DOWN==motionEvent.getAction())
                    {
                     // Dialog d=new Dialog(SignUpActivity.this);
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
                        datePicker();
                    }
                    return true;
                }
            });


            security_question3.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if(MotionEvent.ACTION_DOWN==motionEvent.getAction())
                    {
                        onCreateDialog(useTypeArr,"3");
                    }
                    return true;
                }
            });
        email_id.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(!hasFocus)
                {
                    // open  the database
                    database = DatabaseManager.getInstance().openDatabase();
                    if(DatabaseManager.getInstance().IsUsernameexists(email_id.getText().toString()))
                    {
                        email_id.setError("Email id has already taken");
                        isLoginProfileExists=false;

                    }else{
                        isLoginProfileExists=true;
                    }
                }
            }
        });


    }


    View.OnClickListener listner= new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(!isLoginProfileExists)
            {
                return;
            }

            if(email_id.getText().toString().trim().equals("")||password.getText().toString().trim().equals(""))
            {
                Toast.makeText(SignUpActivity.this,"Please enter the fields",Toast.LENGTH_SHORT).show();
                return;

            }else if (!isValidEmail(email_id.getText().toString().trim()))
            {
                Toast.makeText(SignUpActivity.this,"Email id format is not correct",Toast.LENGTH_SHORT).show();
                return;

            }


            else if(password.getText().toString().trim().length()<6)
            {
                password.setError("Password should be minimum 6 characters");
                return;
            }
            else if(security_question1.getText().toString().trim().equals(""))
            {
                security_question1.setError("Security Question 1 cannot be empty");
                return;
            }

            else if(security_question2.getText().toString().trim().equals(""))
            {
                security_question2.setError("Security Question 2 cannot be empty");
                return;
            }

            else if(security_question3.getText().toString().trim().equals(""))
            {
                security_question3.setError("Security Question 3 cannot be empty");
                return;
            }




            else{

                DatabaseManager.getInstance().openDatabase();
              if(!DatabaseManager.getInstance().IsUsernameexists(email_id.getText().toString().trim()))
                {

                    database.insert(BplOximterdbHelper.TABLE_NAME, null,
                            addUsers(email_id.getText().toString().trim(), password.getText().toString().trim(),
                                    security_question1.getText().toString().toLowerCase().trim()
                                    , security_question2.getText().toString().toLowerCase().trim(),
                                    security_question3.getText().toString().toLowerCase().trim()));
                    // database.close(); Don't close it directly!

                    StoreCredentials.save_signupcredentials(SignUpActivity.this,email_id.getText().toString().trim(),TAG);
                    globalVariable.setUsername(email_id.getText().toString().trim());

                    startActivity(new Intent(SignUpActivity.this, UsersProfileActivity.class).
                            putExtra(Constants.PROFILE_FLAG,Constants.PROFILE_ADD)
                    .putExtra(Constants.USE_TYPE,security_question3.getText().toString().trim()));
                    Toast.makeText(SignUpActivity.this,"Successful Registration", Toast.LENGTH_SHORT).show();
                    finish();

                }


            }


        }
    };


    public final  boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }









    private ContentValues addUsers(String username,String password,String security_ques1,String security_ques2,String security_ques3)
    {
        ContentValues values = new ContentValues();
        values.put(BplOximterdbHelper.USER_NAME, username);
        values.put(BplOximterdbHelper.PASSWORD, password);
        values.put(BplOximterdbHelper.SECURITY_Q_1, security_ques1);
        values.put(BplOximterdbHelper.SECURITY_Q_2, security_ques2);
        values.put(BplOximterdbHelper.USE_TYPE, security_ques3);

        return values;

    }

    private int mYear, mMonth, mDay, mHour, mMinute;
    public void datePicker(){


        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
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

    int index=0;

    public void onCreateDialog(final String [] arrayC, final String params) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
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
