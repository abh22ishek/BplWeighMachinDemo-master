package test.bpl.com.bplscreens;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import constantsP.Constants;
import constantsP.GlobalClass;
import database.BplOximterdbHelper;
import database.DatabaseManager;
import localstorage.StoreCredentials;
import logger.Level;
import logger.Logger;
import model.UserModel;


public class UsersProfileActivity extends Activity {

    private ImageView img_photo, mBackkey;
    private Button btn_Submit, btn_Update_delete;

    private TextView headertitle;
    private RadioButton male, female;
    private RadioGroup sex;
    private String gender = "Male";
    List<UserModel> UserModellist;
    EditText name, age, height, weight;
    GlobalClass globalVariable;

    String profile_image = "";
    String TAG_USER = "Edit";
    final String TAG = UsersProfileActivity.class.getSimpleName();
    File file;

    private  TextView userEmail;
    Button manageUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_details);


        globalVariable = (GlobalClass) getApplicationContext();


        // StatusBarWindow.set_status_bar_color(UsersProfileActivity.this);
        sex =  findViewById(R.id.sex);


        manageUser=findViewById(R.id.btnManageUser);
        manageUser.setVisibility(View.GONE);

        male =  findViewById(R.id.radio_btn_Male);
        female =  findViewById(R.id.radio_btn_Female);

        name =  findViewById(R.id.name); // Address
        age =  findViewById(R.id.age); // Phone
        height =  findViewById(R.id.height);
        weight =  findViewById(R.id.weight);

        mBackkey =  findViewById(R.id.imgBackKey);
        mBackkey.setOnClickListener(listner);

        img_photo =  findViewById(R.id.img_photo);
        btn_Submit =  findViewById(R.id.btnSubmit);

        btn_Update_delete =  findViewById(R.id.deleteUpdate);

        btn_Update_delete.setOnClickListener(listner);

        btn_Submit.setOnClickListener(listner);
        img_photo.setOnClickListener(listner);

        userEmail=findViewById(R.id.userEmail);
        final LinearLayout   layoutHeight=findViewById(R.id.layoutHeight);
        layoutHeight.setVisibility(View.GONE);

        final LinearLayout   layoutWeight=findViewById(R.id.layoutWeight);
        layoutWeight.setVisibility(View.GONE);

        final LinearLayout   layoutGender=findViewById(R.id.layoutGender);
        layoutGender.setVisibility(View.GONE);

        headertitle =  findViewById(R.id.base_header_title);


        if (globalVariable.getUsername() != null) {
            userEmail.setText(globalVariable.getUsername());
            userEmail.setEnabled(false);
        }



            headertitle.setText(R.string.add_clinic_prof);





        Logger.log(Level.DEBUG,TAG,"userType="+globalVariable.getUserType());

        manageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder(); StrictMode.setVmPolicy(builder.build());

        if (getIntent().getStringExtra(Constants.PROFILE_FLAG).equals(Constants.PROFILE_EDIT)) {

            name.setEnabled(false);
            age.setEnabled(false);
            height.setEnabled(false);
            weight.setEnabled(false);
            sex.setEnabled(false);
            male.setEnabled(false);
            female.setEnabled(false);
            manageUser.setVisibility(View.VISIBLE);


            img_photo.setEnabled(false);
            img_photo.setImageDrawable(ContextCompat.getDrawable(
                    UsersProfileActivity.this, R.drawable.edit_profile));


            TAG_USER = "Edit";
            btn_Update_delete.setText(R.string.edit);
            hide_soft_keypad(UsersProfileActivity.this);


            headertitle.setText(R.string.edit_prof);
            btn_Update_delete.setVisibility(View.VISIBLE);

            btn_Submit.setVisibility(View.GONE);
            if (globalVariable.getUsername() != null) {
                DatabaseManager.getInstance().openDatabase();
                UserModellist = new ArrayList<>(DatabaseManager.getInstance().
                        getprofilecontent(globalVariable.getUsername()));

                if (UserModellist.size() > 0) {
                    name.setText(UserModellist.get(0).getName());
                    age.setText(UserModellist.get(0).getAge());
                    weight.setText(UserModellist.get(0).getWeight());
                    height.setText(UserModellist.get(0).getHeight());


                    if (UserModellist.get(0).getSex().equalsIgnoreCase("Male"))
                        male.setChecked(true);
                    else
                        female.setChecked(true);

                }


                if (globalVariable.getUsername() != null) {
                    String img_profile = get_profile_image(globalVariable.getUsername());
                    if (!img_profile.equals("") || img_profile != "") {
                        profile_image = img_profile;

                        Uri uri = Uri.parse(profile_image);


                        Glide
                                .with(UsersProfileActivity.this)
                                .load(uri)
                                .override(200,200) // resizes the image to these dimensions (in pixel). does not respect aspect ratio
                                .into(new GlideDrawableImageViewTarget(img_photo) {
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
                        img_photo.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.user_icon));

                }


            }

        } else

        {
            manageUser.setVisibility(View.GONE);
            TAG_USER = "Update";
            img_photo.setImageDrawable(ContextCompat.getDrawable(UsersProfileActivity.this, R.drawable.add_profile));

            if(globalVariable.getUserType()!=null && globalVariable.getUserType().equalsIgnoreCase("Clinic"))
            {

                headertitle.setText(R.string.add_clinic_prof);
            }else{
                headertitle.setText(getString(R.string.add_prof));

            }

            btn_Update_delete.setVisibility(View.GONE);
            btn_Submit.setVisibility(View.VISIBLE);
        }


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


    }


    View.OnClickListener listner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (v == mBackkey) {
                if (headertitle.getText().toString().equalsIgnoreCase("ADD PROFILE"))
                    Toast.makeText(UsersProfileActivity.this, "Please add your Profile", Toast.LENGTH_SHORT).show();
                else
                    finish();

            } else if (v == img_photo) {


                choose_option(UsersProfileActivity.this);

            } else if (v == btn_Submit) {
                if (validate_profile()) {




                        if (globalVariable.getUsername() != null) {
                            // BplOximeterCommentsSource db=new BplOximeterCommentsSource(UsersProfileActivity.this);

                            // db.addContentProfiles(globalVariable.getUsername(), name.getText().toString(),
                            // age.getText().toString(),gender,height.getText().toString(),weight.getText().toString());
                            // db.close();


                            //----------





                            SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
                            database.insert(BplOximterdbHelper.TABLE_NAME_PROFILE, null, addContentProfiles(globalVariable.getUsername(), name.getText().toString(),
                                    age.getText().toString())

                            );
                            // database.close();
                            // DatabaseManager.getInstance().closeDatabase(); // correct way
                            StoreCredentials.
                                    store_profile_image(UsersProfileActivity.this, profile_image, "Clinic", globalVariable.getUsername());
                            startActivity(new Intent(UsersProfileActivity.this,SelectParameterActivity.class));
                        }




            }

            } else if (v == btn_Update_delete) {


                if (TAG_USER == "Edit") {
                    name.setEnabled(true);
                    age.setEnabled(true);
                    height.setEnabled(true);
                    weight.setEnabled(true);
                    sex.setEnabled(true);
                    male.setEnabled(true);
                    female.setEnabled(true);
                    img_photo.setEnabled(true);
                    btn_Update_delete.setText(getString(R.string.update));
                    TAG_USER = "Update";
                    img_photo.setImageDrawable(ContextCompat.getDrawable(UsersProfileActivity.this, R.drawable.add_profile));

                } else {

                    if(validate_profile())
                    {

                            if (globalVariable.getUsername() != null) {
                                SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
                                database.update(BplOximterdbHelper.TABLE_NAME_PROFILE,
                                        addContentProfiles(globalVariable.getUsername(), name.getText().toString(),
                                                age.getText().toString()
                                        ), BplOximterdbHelper.USER_NAME + "=?", new String[]{globalVariable.getUsername()});
                                StoreCredentials.store_profile_image(UsersProfileActivity.this, profile_image,"Clinic", globalVariable.getUsername());
                                Toast.makeText(UsersProfileActivity.this, "Profile Successfully updated", Toast.LENGTH_SHORT).show();

                                finish();
                            }


                    }

                }
                // update the database


            }
        }
    };





    private boolean validate_profile() {
        boolean b = false;

        if (name.getText().toString().equals("")) {
            name.setError("Name cannot be empty");
            return b;
        }

        if (age.getText().toString().equals("")) {
            age.setError("Age cannot be empty");
            return b;
        }


        b = true;
        return b;

    }


    private boolean validateFields()
    {
        boolean b=false;




        if(Integer.parseInt(height.getText().toString())>50 && Integer.parseInt(height.getText().toString())<250)
        {
            b=true;
        }else{
            Toast.makeText(UsersProfileActivity.this,"Please enter a valid height",Toast.LENGTH_SHORT).show();
            b=false;
            return b;
        }


        if(Integer.parseInt(weight.getText().toString())>10 && Integer.parseInt(weight.getText().toString())<200)
        {
            b=true;
        }else
        {
            Toast.makeText(UsersProfileActivity.this,"Please enter a valid weight",Toast.LENGTH_SHORT).show();
            b=false;
            return b;
        }

        return b;
    }


    public ContentValues addContentProfiles(String username, String address, String phone) {
        ContentValues values = new ContentValues();

        values.put(BplOximterdbHelper.USER_NAME, username);
        values.put(BplOximterdbHelper.NAME, address);
        values.put(BplOximterdbHelper.AGE, phone);
        values.put(BplOximterdbHelper.SEX, "NA");
        values.put(BplOximterdbHelper.HEIGHT,"NA");
        values.put(BplOximterdbHelper.WEIGHT, "NA");


        return values;


    }


    // to dispaly existing profile conetnts


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }


    private String get_profile_image(String key_username) {
        SharedPreferences profile_image_prefs;
        profile_image_prefs = UsersProfileActivity.this.getSharedPreferences(Constants.PREFERENCE_PROFILE_IMAGE, Context.MODE_PRIVATE);

        String image_str = profile_image_prefs.getString(key_username, "");
        Logger.log(Level.INFO, TAG, "get profile image from shared pref=" + image_str);
        return image_str;

    }








    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.SELECT_PICTURE && resultCode == RESULT_OK && null != data) {
            Uri uri = data.getData();

            Glide
                    .with(UsersProfileActivity.this)
                    .load(uri)
                    .override(200, 200) // resizes the image to these dimensions (in pixel). does not respect aspect ratio
                    .into(new GlideDrawableImageViewTarget(img_photo) {
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


            hide_soft_keypad(UsersProfileActivity.this);
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
                        .with(UsersProfileActivity.this)
                        .load(uri)
                        .override(200, 200)
                        .centerCrop()// resizes the image to these dimensions (in pixel). does not respect aspect ratio
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(new GlideDrawableImageViewTarget(img_photo) {
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
                img_photo.setImageDrawable(this.getResources().getDrawable(R.drawable.user_icon));
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

    public static Bitmap cropCenter(Bitmap bmp) {
        int dimension = Math.min(bmp.getWidth(), bmp.getHeight());
        return ThumbnailUtils.extractThumbnail(bmp, dimension, dimension);
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
                if(ActivityCompat.shouldShowRequestPermissionRationale(UsersProfileActivity.this,Manifest.permission.CAMERA)
                        || ActivityCompat.shouldShowRequestPermissionRationale(UsersProfileActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE))
                {
                    Toast.makeText(UsersProfileActivity.this,"Permission are necessary",Toast.LENGTH_SHORT).show();
                    showDialogOK("Camera and Write External Storage Permission required for this app",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE:
                                            ActivityCompat.requestPermissions(UsersProfileActivity.this,
                                                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                    Constants.CAMERA_REQUEST_CODE);
                                            break;
                                        case DialogInterface.BUTTON_NEGATIVE:
                                            img_photo.setImageDrawable(ContextCompat.getDrawable(UsersProfileActivity.this, R.drawable.user_icon));
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




    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }


    @Override
    protected void onResume() {
        super.onResume();

        Logger.log(Level.DEBUG,TAG,"On Resume() called");

    }


    Dialog dialog;
    List<String> options;


    private void choose_option(final Context context)
    {

        /*MyCustomDialogFragment dialogFragment=new MyCustomDialogFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        dialogFragment.show(fragmentManager,"fragment");*/

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
        header.setText(getResources().getString(R.string.select_option));
        content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(adapterView.getItemAtPosition(i)=="Default")
                {
                    img_photo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.user_icon));
                     profile_image="";

                }else if(adapterView.getItemAtPosition(i)=="Camera")
                {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        ActivityCompat.requestPermissions(UsersProfileActivity.this,
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
}
