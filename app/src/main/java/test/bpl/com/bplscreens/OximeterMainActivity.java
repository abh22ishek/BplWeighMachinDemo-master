package test.bpl.com.bplscreens;

import android.annotation.*;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import biolight.*;
import constantsP.Constants;
import constantsP.GlobalClass;
import database.DatabaseManager;
import localstorage.StoreCredentials;


public class OximeterMainActivity extends Activity {

    private final String TAG=OximeterMainActivity.class.getSimpleName();

    private TextView mSignUp,mForgotPassword,mUserGuide;
    private EditText emailid,password;

    GlobalClass globalVariable;



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_signup);

        mSignUp= findViewById(R.id.SignUp);
        mForgotPassword=  findViewById(R.id.forgotPassword);
        mUserGuide= findViewById(R.id.user_guide);
        Button btnlogin = findViewById(R.id.btnLoginSignUp);



        globalVariable = (GlobalClass) getApplicationContext();

        emailid=  findViewById(R.id.emailid);
        password=  findViewById(R.id.password);





        mSignUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    startActivity( new Intent(OximeterMainActivity.this,SignUpActivity.class));
                }

                return true;
            }
        });


        mUserGuide.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    choose_option(OximeterMainActivity.this);
                }
                return true;
            }
        });


        mForgotPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    if(emailid.getText().toString().trim().equals(""))
                        Toast.makeText(OximeterMainActivity.this,
                                "Please enter your registered email id",Toast.LENGTH_SHORT).show();


                    else
                    {

                        DatabaseManager.getInstance().openDatabase();
                        if(DatabaseManager.getInstance().IsUsernameexists(emailid.getText().toString().trim()))
                        {
                            startActivity( new Intent(OximeterMainActivity.this,ForgotPasswordActivity.class)
                                    .putExtra(Constants.USER_NAME,emailid.getText().toString()));
                        }else
                            emailid.setError("Email id not registered");
                        }


                }
                return true;
            }
        });








        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (emailid.getText().toString().trim().equals("")) {
                    Toast.makeText(OximeterMainActivity.this, "Email id cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.getText().toString().trim().equals("")) {
                    Toast.makeText(OximeterMainActivity.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (emailid.getText().toString() != null && password.getText().toString() != null) {


                    // call the database query
                    DatabaseManager.getInstance().openDatabase();
                    if (DatabaseManager.getInstance().Login(emailid.getText().toString().trim(), password.getText().toString().trim())) {
                        globalVariable.setUsername(emailid.getText().toString());
                        StoreCredentials.save_signupcredentials(OximeterMainActivity.this, emailid.getText().toString().trim(), TAG,"");
                       // Toast.makeText(OximeterMainActivity.this, "User id value stored in Shared Pref file" + " " + " ", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(OximeterMainActivity.this, SelectParameterActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(OximeterMainActivity.this, "Invalid Password and UserId", Toast.LENGTH_SHORT).show();
                    }




                }
            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        OximeterMainActivity.this.finishAffinity();
    }


    ArrayList<String> options;
    Dialog dialog;
    private void choose_option(final Context context)
    {
        if(options==null)
        {
            options=new ArrayList<>();
            options.add(getString(R.string.ioxy));
            options.add(getString(R.string.weigh_mc));
            options.add(getString(R.string.bp));

        }

        final ArrayAdapter<String> adapter=new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,options);
        if(dialog==null)
        {
            dialog = new Dialog(context);
        }


        dialog.getWindow().getAttributes().windowAnimations =R.style.DialogBoxAnimation;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.custom_dialog_items);
        final ListView content= dialog.findViewById(R.id.list_content);
        final TextView header= dialog.findViewById(R.id.header);


        content.setAdapter(adapter);
        header.setText(getResources().getString(R.string.selection));
        content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(adapterView.getItemAtPosition(i).equals(getString(R.string.weigh_mc)))
                {

                    Toast.makeText(OximeterMainActivity.this,"Coming Soon",Toast.LENGTH_SHORT).show();

                }else if(adapterView.getItemAtPosition(i).equals(getString(R.string.ioxy)))
                {

                    startActivity(new Intent(OximeterMainActivity.this,UserGuideContent.class).
                            setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

                }

                else if(adapterView.getItemAtPosition(i).equals(getString(R.string.bp))){
                    startActivity(new Intent(OximeterMainActivity.this,
                            BioLightUserHelpActivity.class).
                            setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }

                dialog.dismiss();
            }

        });

        dialog.show();
    }


    // another weigh machine

    private ArrayList<String> optionsList;
    private  Dialog mDialog;
    /*private void selectOptionWeighMachine(final Context context)
    {
        if(optionsList==null)
        {
            optionsList=new ArrayList<>();
            optionsList.add("I-Weigh Machine 1");
            optionsList.add("I-Weigh Machine 2");
        }



        ArrayAdapter<String> adapter=new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,optionsList);
        if(mDialog==null)
        {
            mDialog = new Dialog(context);
        }


        mDialog.getWindow().getAttributes().windowAnimations =R.style.DialogBoxAnimation;
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setContentView(R.layout.custom_dialog_items);
        final ListView content=  mDialog.findViewById(R.id.list_content);
        final TextView header=  mDialog.findViewById(R.id.header);


        content.setAdapter(adapter);
        header.setText(getResources().getString(R.string.selection));
        content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(adapterView.getItemAtPosition(i).equals("I-Weigh Machine 1"))  // weigh machine 1
                {

                     startActivity(new Intent(OximeterMainActivity.this,WeighingMachineUsersguide.class).
                          setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));


                }else  // weigh machine 2
                {

                }

                mDialog.dismiss();
            }

        });

        mDialog.show();
    }

*/

}
