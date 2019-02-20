package test.bpl.com.bplscreens;

import android.*;
import android.annotation.*;
import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.database.sqlite.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.net.*;
import android.os.*;
import android.provider.*;
import android.support.annotation.*;
import android.support.v4.app.*;
import android.support.v4.content.*;
import android.util.*;
import android.view.*;
import android.view.inputmethod.*;
import android.widget.*;

import com.bumptech.glide.*;
import com.bumptech.glide.load.engine.*;
import com.bumptech.glide.load.resource.drawable.*;
import com.bumptech.glide.request.animation.*;
import com.bumptech.glide.request.target.*;

import java.io.*;
import java.util.*;

import constantsP.*;
import database.*;
import localstorage.*;
import logger.*;
import model.*;

public class PatientProfileActivity extends Activity {

     ImageView userIcon;

     final String TAG=PatientProfileActivity.class.getSimpleName();

     private EditText height,weight, Age,userName,userName_;
     private GlobalClass globalVariable;

        Button submit,update;
        String TAG_USER = "Edit";
        List<UserModel> UserModellist;
        String mUserName;

    String gender="Male";
    RadioGroup sex;
    private RadioButton male, female;
    SQLiteDatabase database;
    boolean isUserExists;

    EditText userEmail,userPhone;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pat_profile);


        userName=findViewById(R.id.userName);
        height=findViewById(R.id.height);

        weight=findViewById(R.id.weight);
        userIcon=findViewById(R.id.img_photo);

        Age=findViewById(R.id.address);

        userName_=findViewById(R.id.userName_);
         submit=findViewById(R.id.btnSubmit);

        male =  findViewById(R.id.radio_btn_Male);
        female =  findViewById(R.id.radio_btn_Female);

        sex=findViewById(R.id.sex);

        userEmail=findViewById(R.id.userEmail);
        userPhone=findViewById(R.id.userPhone);

        final TextView headertitle= findViewById(R.id.base_header_title);
        headertitle.setText(getString(R.string.add_user));

        globalVariable = (GlobalClass) getApplicationContext();
        if (globalVariable.getUsername() != null) {
            String mName=globalVariable.getUsername();
            userEmail.setText(mName);
        }
        userEmail.setEnabled(false);
        final ImageView backKey=findViewById(R.id.imgBackKey);

        backKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


     //   populatedata();
        sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.radio_btn_Male) {
                    gender = "Male";
                } else {
                    gender = "Female";
                }
            }
        });


        userName_.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(!hasFocus)
                {

                    // open  the database
                    database = DatabaseManager.getInstance().openDatabase();
                    if(DatabaseManager.getInstance().IsUsernameexistsInLoginProfile(globalVariable.getUsername(),
                            userName_.getText().toString()))
                    {
                        if (!getIntent().getStringExtra(Constants.PROFILE_FLAG).equals(Constants.PROFILE_EDIT)){
                            userName_.setError("User Name has already taken");
                            isUserExists=false;
                        }


                    }else{
                        isUserExists=true;
                    }
                }
            }
        });

        update=findViewById(R.id.deleteUpdate);
        update.setVisibility(View.GONE);


        if (getIntent().getStringExtra(Constants.PROFILE_FLAG).equals(Constants.PROFILE_EDIT)

                && !getIntent().getExtras().getString("user").equals("")) {

            mUserName= getIntent().getExtras().getString("user");
            userName_.setEnabled(false);
            Age.setEnabled(false);
            height.setEnabled(false);
            weight.setEnabled(false);
            userIcon.setEnabled(false);

            userEmail.setEnabled(false);
            userPhone.setEnabled(false);
            sex.setEnabled(false);
            male.setEnabled(false);
            female.setEnabled(false);

            userIcon.setImageDrawable(ContextCompat.getDrawable(PatientProfileActivity.this,
                    R.drawable.edit_profile));


            TAG_USER = "Edit";


            update.setVisibility(View.VISIBLE);
            update.setText("Edit");


            hide_soft_keypad(PatientProfileActivity.this);
            headertitle.setText(getString(R.string.edit_user));



            submit.setVisibility(View.GONE);
            if (globalVariable.getUsername() != null) {

                DatabaseManager.getInstance().openDatabase();
                UserModellist= DatabaseManager.getInstance().
                        getAllUserprofilecontent(mUserName,Constants.SELECTED_USER_SNOW);

                if (UserModellist.size() > 0) {
                    userName.setText(UserModellist.get(0).getName());
                    userName_.setText(UserModellist.get(0).getUserName());
                    Age.setText(UserModellist.get(0).getAddress());
                    weight.setText(UserModellist.get(0).getWeight());
                    height.setText(UserModellist.get(0).getHeight());

                    userEmail.setText(UserModellist.get(0).getUserEmail());
                    userPhone.setText(UserModellist.get(0).getPhone());

                    if (UserModellist.get(0).getSex().equalsIgnoreCase("Male"))
                        male.setChecked(true);
                    else
                        female.setChecked(true);


                }


                if (globalVariable.getUsername() != null) {


                    String img_profile = get_profile_image(userName_.getText().toString()+"_"+"clinic");
                    if (!img_profile.equals("") || img_profile != "") {
                        profile_image = img_profile;

                        Uri uri = Uri.parse(profile_image);

                        Glide
                                .with(PatientProfileActivity.this)
                                .load(uri)
                                .override(200,200) // resizes the image to these dimensions (in pixel). does not respect aspect ratio
                                .into(new GlideDrawableImageViewTarget(userIcon) {
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


                    }else
                        userIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.user_icon));

                }


            }

        } else

        {
            TAG_USER = "Update";
            userIcon.setImageDrawable
                    (ContextCompat.getDrawable(PatientProfileActivity.this, R.drawable.add_profile));

            headertitle.setText(getString(R.string.add_user));
            update.setVisibility(View.GONE);
            submit.setVisibility(View.VISIBLE);
        }


        update.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {


                if (TAG_USER == "Edit") {

                    Age.setEnabled(true);
                    sex.setEnabled(true);
                    male.setEnabled(true);
                    female.setEnabled(true);
                    userPhone.setEnabled(true);
                    userEmail.setEnabled(true);
                    height.setEnabled(true);
                    weight.setEnabled(true);
                    userIcon.setEnabled(true);
                    update.setText("Update");

                    TAG_USER = "Update";
                    userIcon.setImageDrawable
                            (ContextCompat.getDrawable(PatientProfileActivity.this, R.drawable.add_profile));

                } else {

                    if(validate_profile())
                    {
                        if(validateFields()){
                            if (globalVariable.getUsername() != null) {


                                 database = DatabaseManager.getInstance().openDatabase();

                                try{
                                    database.update(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,
                                            addContentProfiles(globalVariable.getUsername(), userName_.getText().toString(),
                                                  userPhone.getText().toString().trim(),  gender,Age.getText().toString(), height.getText().toString(),
                                                    weight.getText().toString(),userEmail.getText().toString().toLowerCase().trim()), BplOximterdbHelper.USER_NAME_ + "=?", new String[]{mUserName});
                                    StoreCredentials.store_profile_image(PatientProfileActivity.this,
                                            profile_image, TAG, userName_.getText().toString()+"_"+"clinic");

                                    Toast.makeText(PatientProfileActivity.this,
                                            "User Profile Successfully updated", Toast.LENGTH_SHORT).show();

                                }catch (Exception e)
                                {
                                    e.printStackTrace();
                                }


                                finally {
                                    finish();

                                }

                            }

                        }
                    }

                }
                // update the database



            }
        });



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(validate_profile() && isUserExists)
                {
                    if(validateFields())
                    {
                         database = DatabaseManager.getInstance().openDatabase();

                        try{
                            database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE, null,
                                    addContentProfiles(globalVariable.getUsername(),userName_.getText().toString().trim(),
                                            userPhone.getText().toString().trim(),gender,
                                    Age.getText().toString(),  height.getText().toString().trim(),
                                            "NA",
                                            userEmail.getText().toString().toLowerCase().trim()));



                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }


                        String patientUri=userName_.getText().toString()+"_"+"clinic";
                        // pending Tasks to do

                            StoreCredentials.store_profile_image(PatientProfileActivity.this, profile_image, TAG, patientUri);
                            Toast.makeText(PatientProfileActivity.this, "User Successfully Added", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }



            }
        });



        userIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choose_option(PatientProfileActivity.this);
            }
        });

    }



    List<String> options;
    Dialog dialog;
    String profile_image="";
    File file;
    private void choose_option(final Context context)
    {


        if(options==null)
        {
            options=new ArrayList<>();
            options.add("Default");
            options.add("Camera");
            options.add("Gallery");

        }



        ArrayAdapter<String> adapter=new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,options);
        if(dialog==null)
        {
            dialog = new Dialog(context);
        }


        dialog.getWindow().getAttributes().windowAnimations =R.style.DialogBoxAnimation;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.custom_dialog_items);
        final ListView content= dialog.findViewById(R.id.list_content);
        final TextView header=  dialog.findViewById(R.id.header);


        content.setAdapter(adapter);
        header.setText(getResources().getString(R.string.selection));
        content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(adapterView.getItemAtPosition(i)=="Default")
                {
                    userIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.user_icon));
                    profile_image="";

                }else if(adapterView.getItemAtPosition(i)=="Camera")
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        ActivityCompat.requestPermissions(PatientProfileActivity.this,
                                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
                                Constants.CAMERA_REQUEST_CODE);


                    }
                    else
                    {
                        file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"test.jpg"+System.currentTimeMillis());
                        Uri tempuri=Uri.fromFile(file);
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,tempuri);
                        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1);
                        startActivityForResult(intent,Constants.CAMERA_CODE);
                    }

                }else{
                    Intent intent;
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                        intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                    }else{
                        intent = new Intent(Intent.ACTION_GET_CONTENT);
                    }
                    intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), Constants.SELECT_PICTURE);

                }
                dialog.dismiss();
            }

        });

        dialog.show();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.SELECT_PICTURE && resultCode == RESULT_OK && null != data) {
            Uri uri = data.getData();




            Glide
                    .with(PatientProfileActivity.this)
                    .load(uri)
                    .override(200, 200) // resizes the image to these dimensions (in pixel). does not respect aspect ratio
                    .into(new GlideDrawableImageViewTarget(userIcon) {
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


            hide_soft_keypad(PatientProfileActivity.this);
            profile_image=uri.toString();






        }
        else if(requestCode==Constants.CAMERA_CODE && resultCode==RESULT_OK)
        {
            String uri;
            if(file.exists())
            {
                Log.i("**Absolute path**=", "" + file.getAbsolutePath());
                uri=file.getAbsolutePath();

                profile_image="file://"+uri;
               /* Picasso
                        .with(UsersProfileActivity.this)
                        .load("file://"+Uri.parse(uri))
                        .resize(200,200) // resizes the image to these dimensions (in pixel)
                        .centerCrop() // this cropping technique scales the image so that it fills the requested bounds and then crops the extra.
                        .into(img_photo);*/

                Glide
                        .with(PatientProfileActivity.this)
                        .load(uri)
                        .override(200, 200)
                        .centerCrop()// resizes the image to these dimensions (in pixel). does not respect aspect ratio
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(new GlideDrawableImageViewTarget(userIcon) {
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


            }else{
                userIcon.setImageDrawable(this.getResources().getDrawable(R.drawable.user_icon));
            }


        }
    }
    private void hide_soft_keypad(Context context) {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }



    private boolean validate_profile() {
        boolean b = false;

        if (userName_.getText().toString().equals("")) {
            userName_.setError("Name cannot be empty");
            return b;
        }

        if (userEmail.getText().toString().equals("")) {
            userEmail.setError("Weight cannot be empty");
            return b;
        }

        if (userPhone.getText().toString().equals("")) {
            userPhone.setError("Phone cannot be empty");
            return b;
        }


        if (Age.getText().toString().equals("")) {
            Age.setError("Age cannot be empty");
            return b;
        }


        if (height.getText().toString().equals("")) {
            height.setError("Height cannot be empty");
            return b;
        }




        b = true;
        return b;

    }
    public final  boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private boolean validateFields()
    {
        boolean b=false;


        if(!isValidEmail(userEmail.getText().toString()))
        {
            userEmail.setError("Email Id not valid");
            return b;
        }


        if(userPhone.getText().toString().trim().length()<10){
            userPhone.setError("Phone not valid");
        }

        if(Integer.parseInt(height.getText().toString())>50 && Integer.parseInt(height.getText().toString())<250)
        {

            b=true;
        }else{
            Toast.makeText(PatientProfileActivity.this,"Please enter a valid height",Toast.LENGTH_SHORT).show();
            b=false;
          return b;
        }



        return b;
    }



    public ContentValues addContentProfiles(String admin_email, String username, String phone,String gender,
                                            String age, String height, String weight,String userEmail) {
        ContentValues values = new ContentValues();

        values.put(BplOximterdbHelper.USER_NAME, admin_email);
        values.put(BplOximterdbHelper.USER_NAME_, username);
        values.put(BplOximterdbHelper.USER_PHONE_, phone);
        values.put(BplOximterdbHelper.USER_SEX_, gender);
        values.put(BplOximterdbHelper.USER_ADDRESS_,age);
        values.put(BplOximterdbHelper.USER_HEIGHT_, height);
        values.put(BplOximterdbHelper.USER_WEIGHT_, weight);
        values.put(BplOximterdbHelper.USER_EMAIL_, userEmail);

        return values;


    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        // Log.i("TAG", "**Grant Results**=" + grantResults[0]+" "+grantResults[1]+" "+grantRes + "request code=" + requestCode);

        for(int k=0;k<grantResults.length;k++)
        {
            Logger.log(Level.DEBUG,TAG,"grant results[]="+grantResults[k]);

        }
        if(requestCode==Constants.CAMERA_REQUEST_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED
                    ) {
                file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"test.jpg"+System.currentTimeMillis());
                Uri tempuri=Uri.fromFile(file);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,tempuri);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                startActivityForResult(intent, Constants.CAMERA_CODE);
            }

            else if(grantResults[0]==PackageManager.PERMISSION_DENIED ||grantResults[1]==PackageManager.PERMISSION_DENIED)

            {
                if(ActivityCompat.shouldShowRequestPermissionRationale(PatientProfileActivity.this,Manifest.permission.CAMERA)
                        || ActivityCompat.shouldShowRequestPermissionRationale(PatientProfileActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE))
                {
                    Toast.makeText(PatientProfileActivity.this,"Permission are necessary",Toast.LENGTH_SHORT).show();
                    showDialogOK("Camera and Write External Storage Permission required for this app",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE:
                                            ActivityCompat.requestPermissions(PatientProfileActivity.this,
                                                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                    Constants.CAMERA_REQUEST_CODE);
                                            break;
                                        case DialogInterface.BUTTON_NEGATIVE:
                                            userIcon.setImageDrawable(ContextCompat.getDrawable(PatientProfileActivity.this, R.drawable.user_icon));
                                            break;
                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(this, "Go to settings And enable permissions", Toast.LENGTH_LONG)
                            .show();

                }



            }


        }
    }



    private String get_profile_image(String key_username) {
        SharedPreferences profile_image_prefs;
        profile_image_prefs = PatientProfileActivity.this.
                getSharedPreferences(Constants.PREFERENCE_PROFILE_IMAGE, Context.MODE_PRIVATE);

        String image_str = profile_image_prefs.getString(key_username, "");
        Logger.log(Level.INFO, TAG, "get profile image from shared pref=" + image_str);
        return image_str;

    }



    private void populatedata()
    {
        database = DatabaseManager.getInstance().openDatabase();
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"AamirKhan0001","9876543001","Male","18","180","50","Aamir@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"AbrahamLincoln0001","9876543002","Female","19","179","51","Abrah@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"AdolfHitler0001","9876543003","Male","20","181","52","Adolf@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"AishwaryaRai0001","9876543004","Female","21","180","53","Aishw@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"AlbertEinstein0001","9876543005","Male","22","182","54","Alber@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"Amitabhbachan0001","9876543006","Female","23","181","55","Amita@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"AngelaMerkel0001","9876543007","Male","24","183","56","Angel@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"AngelinaJolie0001","9876543008","Female","25","182","57","Angel@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"ArnoldSchwazegger0001","9876543009","Male","26","184","58","Arnol@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"BarackObama0001","9876543010","Female","27","183","59","Barac@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"Beyonce0001","9876543011","Male","28","185","60","Beyon@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"BhagatSingh0001","9876543012","Female","29","184","61","Bhaga@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"BillClinton0001","9876543013","Male","30","186","62","BillC@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"BillGates0001","9876543014","Female","31","185","63","BillG@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"BradPitt0001","9876543015","Male","32","187","64","BradP@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"BritneySpears0001","9876543016","Female","33","186","65","Britn@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"BruceLee0001","9876543017","Male","34","188","66","Bruce@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"BruceWillis0001","9876543018","Female","35","187","67","Bruce@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"CharlieChaplin0001","9876543019","Male","36","189","68","Charl@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"CheGuevara0001","9876543020","Female","37","188","69","CheGu@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"CurtisJamesJacksonIII0001","9876543021","Male","38","190","70","Curti@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"DalaiLama0001","9876543022","Female","39","189","71","Dalai@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"DanielCraig0001","9876543023","Male","40","191","72","Danie@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"DanielRadicliffe0001","9876543024","Female","41","190","73","Danie@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"DavidBeckham0001","9876543025","Male","42","192","74","David@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"DwayneJohnson0001","9876543026","Female","43","191","75","Dwayn@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"ElvisPresley0001","9876543027","Male","44","193","76","Elvis@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"EmmaWatson0001","9876543028","Female","45","192","77","EmmaW@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"fnames.txt000","9876543029","Male","46","194","78","fname@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"GeorgeClooney0001","9876543030","Female","47","193","79","Georg@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"HillaryClinton0001","9876543031","Male","48","195","80","Hilla@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"HughJackman0001","9876543032","Female","49","194","81","HughJ@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"IndiraGandhi0001","9876543033","Male","50","196","82","Indir@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"JackieChan0001","9876543034","Female","51","195","83","Jacki@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"JawaharlalNehru0001","9876543035","Male","52","197","84","Jawah@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"Jay-Z0001","9876543036","Female","53","196","85","Jay-Z@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"JimCarrey0001","9876543037","Male","54","198","86","JimCa@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"JKRowling0001","9876543038","Female","55","197","87","JKRow@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"JohnFKennedy0001","9876543039","Male","56","199","88","JohnF@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"JohnLennon0001","9876543040","Female","57","198","89","JohnL@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"JohnnyDepp0001","9876543041","Male","58","200","90","Johnn@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"JustinBieber0001","9876543042","Female","59","199","91","Justi@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"KateMiddleton0001","9876543043","Male","60","201","92","KateM@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"KatrinaKaif0001","9876543044","Female","61","200","93","Katri@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"KimJongUn0001","9876543045","Male","62","202","94","KimJo@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"LaluPrasadYadav0001","9876543046","Female","63","201","95","LaluP@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"LanceArmstrong0001","9876543047","Male","64","203","96","Lance@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"LataMangeshkar0001","9876543048","Female","65","202","97","LataM@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"LeonardoDiCaprio0001","9876543049","Male","66","204","98","Leona@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"LionelMessi0001","9876543050","Female","67","203","99","Lione@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"LouisArmstrong0001","9876543051","Male","68","205","100","Louis@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"LucianoPavarotti0001","9876543052","Female","69","204","101","Lucia@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"LucilleBall0001","9876543053","Male","70","206","102","Lucil@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"MahatmaGandhi0001","9876543054","Female","71","205","103","Mahat@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"MalcolmX0001","9876543055","Male","72","207","104","Malco@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"ManmohanSingh0001","9876543056","Female","73","206","105","Manmo@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"MargaretThatcher0001","9876543057","Male","74","208","106","Marga@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"MarilynMonroe0001","9876543058","Female","75","207","107","Maril@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"MarkZuckerberg0001","9876543059","Male","76","209","108","MarkZ@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"MartinLutherKing0001","9876543060","Female","77","208","109","Marti@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"MattDamon0001","9876543061","Male","78","210","110","MattD@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"MeganFox0001","9876543062","Female","79","209","111","Megan@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"MichaelJackson0001","9876543063","Male","80","211","112","Micha@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"MichaelJordan0001","9876543064","Female","81","210","113","Micha@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"MichaelPhelps0001","9876543065","Male","82","212","114","Micha@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"MorganFreeman0001","9876543066","Female","83","211","115","Morga@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"MotherTeresa0001","9876543067","Male","84","213","116","Mothe@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"MuhammadAli0001","9876543068","Female","85","212","117","Muham@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"NarendraModi0001","9876543069","Male","86","214","118","Naren@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"NelsonMandela0001","9876543070","Female","87","213","119","Nelso@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"NicoleKidman0001","9876543071","Male","88","215","120","Nicol@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"OprahWinfrey0001","9876543072","Female","89","214","121","Oprah@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"PabloPicasso0001","9876543073","Male","90","216","122","Pablo@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"PaulMcCartney0001","9876543074","Female","91","215","123","PaulM@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"Pele0001","9876543075","Male","92","217","124","Pele@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"PeterJackson0001","9876543076","Female","93","216","125","Peter@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"PeterSellers0001","9876543077","Male","94","218","126","Peter@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"PopeJohnPaulII0001","9876543078","Female","95","217","127","PopeJ@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"PrinceCharles0001","9876543079","Male","96","219","128","Princ@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"PrincessDiana0001","9876543080","Female","97","218","129","Princ@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"QueenElizabethII0001","9876543081","Male","98","220","130","Queen@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"QuentinTarantino0001","9876543082","Female","99","219","131","Quent@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"RafaelNadal0001","9876543083","Male","100","221","132","Rafae@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"Rajinikanth0001","9876543084","Female","101","220","133","Rajin@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"RobertDowneyJr0001","9876543085","Male","102","222","134","Rober@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"RobynRihannaFenty0001","9876543086","Female","103","221","135","Robyn@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"RowanAtkinson0001","9876543087","Male","104","223","136","Rowan@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"RussellCrowe0001.jpg","9876543088","Female","105","222","137","Russe@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"SachinTendulkar0001","9876543089","Male","106","224","138","Sachi@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"ScarlettJohansson0001","9876543090","Female","107","223","139","Scarl@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"SelenaGomez0001","9876543091","Male","108","225","140","Selen@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"Shakira0001","9876543092","Female","109","224","141","Shaki@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"StephenHawking0001","9876543093","Male","110","226","142","Steph@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"SteveJobs0001","9876543094","Female","111","225","143","Steve@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"SylvesterStallone0001","9876543095","Male","112","227","144","Sylve@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"TaylorSwift0001","9876543096","Female","113","226","145","Taylo@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"TigerWoods0001","9876543097","Male","114","228","146","Tiger@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"TomCruise0001","9876543098","Female","115","227","147","TomCr@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"UsainBolt0001","9876543099","Male","116","229","148","Usain@gmail.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"VladimirPutin0001","9876543100","Female","117","228","149","Vladi@yahoo.com"));
        database.insert(BplOximterdbHelper.TABLE_NAME_USER_PROFILE,null,addContentProfiles(globalVariable.getUsername(),"WinstonChurchill0001","9876543101","Male","118","230","150","Winst@gmail.com"));

    }
}
