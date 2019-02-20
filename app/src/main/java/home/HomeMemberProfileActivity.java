package home;

import android.*;
import android.annotation.*;
import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.database.sqlite.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.os.*;
import android.provider.*;
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
import test.bpl.com.bplscreens.R;
import test.bpl.com.bplscreens.*;

public class HomeMemberProfileActivity extends FragmentActivity {

    private ImageView img_photo, mBackkey;
    private Button btn_Submit, btn_Update_delete;

    private TextView headertitle, city, member_name;
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

    private TextView userEmail,phoneNumber;
    Button manageUser;

    String mMemeberName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_profile);


        globalVariable = (GlobalClass) getApplicationContext();

        sex = findViewById(R.id.sex);
        member_name = findViewById(R.id.memberName);

        manageUser = findViewById(R.id.btnManageUser);
        manageUser.setVisibility(View.GONE);

        male = findViewById(R.id.radio_btn_Male);
        female = findViewById(R.id.radio_btn_Female);

        city = findViewById(R.id.address); // city
        age = findViewById(R.id.age); //
        height = findViewById(R.id.height);
        phoneNumber=findViewById(R.id.phoneNumber);

        mBackkey = findViewById(R.id.imgBackKey);
        mBackkey.setOnClickListener(listner);

        img_photo = findViewById(R.id.img_photo);
        btn_Submit = findViewById(R.id.btnSubmit);

        btn_Update_delete = findViewById(R.id.deleteUpdate);

        btn_Update_delete.setOnClickListener(listner);

        btn_Submit.setOnClickListener(listner);
        img_photo.setOnClickListener(listner);

        userEmail = findViewById(R.id.userEmail);


        final LinearLayout layoutWeight = findViewById(R.id.layoutWeight);
        layoutWeight.setVisibility(View.GONE);


        headertitle = findViewById(R.id.base_header_title);


        if (globalVariable.getUsername() != null) {
            userEmail.setText(globalVariable.getUsername());
            userEmail.setEnabled(false);
        }


        headertitle.setText(R.string.add_fam_mem);

        Logger.log(Level.DEBUG, TAG, "userType=" + globalVariable.getUserType());

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
        manageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        if (getIntent().getStringExtra(Constants.PROFILE_FLAG).equals(Constants.PROFILE_EDIT)) {
            mMemeberName= getIntent().getExtras().getString("user");

            member_name.setEnabled(false);
            age.setEnabled(false);
            height.setEnabled(false);
//            weight.setEnabled(false);
            sex.setEnabled(false);
            male.setEnabled(false);
            female.setEnabled(false);
            btn_Update_delete.setVisibility(View.VISIBLE);
            btn_Submit.setVisibility(View.GONE);
            headertitle.setText(R.string.edt_fam_mem);
            img_photo.setEnabled(false);
            img_photo.setImageDrawable(ContextCompat.getDrawable(HomeMemberProfileActivity.this,
                    R.drawable.edit_profile));


            TAG_USER = "Edit";
            btn_Update_delete.setText(R.string.edit);
            hide_soft_keypad(HomeMemberProfileActivity.this);



            btn_Update_delete.setVisibility(View.VISIBLE);

            btn_Submit.setVisibility(View.GONE);
            if (globalVariable.getUsername() != null) {
                DatabaseManager.getInstance().openDatabase();
                UserModellist = new ArrayList<>(DatabaseManager.getInstance().
                        getMemberprofilecontent(mMemeberName));

                if (UserModellist.size() > 0) {
                    member_name.setText(UserModellist.get(0).getName());
                    mMemeberName=UserModellist.get(0).getName();
                    age.setText(UserModellist.get(0).getAge());
                 //   weight.setText(UserModellist.get(0).getWeight());
                    height.setText(UserModellist.get(0).getHeight());

                    phoneNumber.setText(UserModellist.get(0).getPhone());

                    if (UserModellist.get(0).getSex().equalsIgnoreCase("Male"))
                        male.setChecked(true);
                    else
                        female.setChecked(true);

                }


                if (globalVariable.getUsername() != null) {
                    String img_profile = get_profile_image(member_name.getText().toString().trim()+"_"+"home");
                    if (!img_profile.equals("") || img_profile != "") {
                        profile_image = img_profile;

                        Uri uri = Uri.parse(profile_image);

                        Glide
                                .with(HomeMemberProfileActivity.this)
                                .load(uri)
                                .override(200, 200) // resizes the image to these dimensions (in pixel). does not respect aspect ratio
                                .into(new GlideDrawableImageViewTarget(img_photo) {
                                    @Override
                                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                                        super.onResourceReady(resource, animation);

                                        Logger.log(Level.DEBUG, TAG, "Glide loaded the image successfully");
                                    }

                                    @Override
                                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                        super.onLoadFailed(e, errorDrawable);

                                        Logger.log(Level.ERROR, TAG, e.toString());
                                    }
                                });


                    } else
                        img_photo.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.user_icon));

                }


            }

        } else

        {
            manageUser.setVisibility(View.GONE);
            TAG_USER = "Update";
            img_photo.setImageDrawable(ContextCompat.getDrawable(HomeMemberProfileActivity.this,
                    R.drawable.add_profile));
            btn_Update_delete.setVisibility(View.GONE);
            btn_Submit.setVisibility(View.VISIBLE);


        }


    }








    View.OnClickListener listner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (v == mBackkey) {
                if (headertitle.getText().toString().equalsIgnoreCase("ADD PROFILE"))
                    Toast.makeText(HomeMemberProfileActivity.this, "Please add your Profile", Toast.LENGTH_SHORT).show();
                else
                    finish();

            } else if (v == img_photo) {

                choose_option(HomeMemberProfileActivity.this);

            } else if (v == btn_Submit) {
                if (validate_profile()) {

                    if(!validateFields())
                        return;

                    if (globalVariable.getUsername() != null) {


                        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
                        database.insert(BplOximterdbHelper.TABLE_NAME_HOME_PROFILE,
                                null, addContentProfiles(globalVariable.getUsername(),
                                member_name.getText().toString().trim(),
                                age.getText().toString().trim(),
                                        gender,phoneNumber.getText().toString().trim(),
                                        city.getText().toString().trim(),
                                height.getText().toString())

                        );

                        String mMemberUri=member_name.getText().toString().trim()+"_"+"home";

                        StoreCredentials.store_profile_image(HomeMemberProfileActivity.this, profile_image, "Home",mMemberUri);
                        startActivity(new Intent(HomeMemberProfileActivity.this,SelectParameterActivity.class));

                    }




                }

            } else if (v == btn_Update_delete) {


                if (TAG_USER == "Edit") {
                    member_name.setEnabled(true);
                    age.setEnabled(true);
                    height.setEnabled(true);
                   // weight.setEnabled(true);
                    sex.setEnabled(true);
                    male.setEnabled(true);
                    female.setEnabled(true);
                    img_photo.setEnabled(true);
                    btn_Update_delete.setText(getString(R.string.update));
                    TAG_USER = "Update";
                    img_photo.setImageDrawable(ContextCompat.getDrawable(HomeMemberProfileActivity.this, R.drawable.add_profile));

                } else {

                    if(validate_profile())
                    {

                        if(!validateFields())
                            return;


                        if (globalVariable.getUsername() != null) {
                            SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
                            database.update(BplOximterdbHelper.TABLE_NAME_HOME_PROFILE,
                                    addContentProfiles(globalVariable.getUsername(), member_name.getText().
                                                    toString().trim(),
                                            age.getText().toString().trim(),gender,phoneNumber.getText().toString().trim(),city.getText().toString().trim()
                                            ,height.getText().toString().trim()
                                    ),
                                    BplOximterdbHelper.NAME_HOME_USER + "=?",
                                    new String[]{mMemeberName});
                            StoreCredentials.store_profile_image(HomeMemberProfileActivity.this, profile_image, "Home", member_name.getText().toString().trim()+"_"+"home");
                            Toast.makeText(HomeMemberProfileActivity.this, "Profile Successfully updated", Toast.LENGTH_SHORT).show();


                            Intent intent = new Intent();
                            intent.putExtra("IsUserEdited",true);
                            setResult(800, intent);
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

        if (member_name.getText().toString().equals("")) {
            member_name.setError("Name cannot be empty");
            return b;
        }


        if (phoneNumber.getText().toString().equals("")) {
            phoneNumber.setError("Phone cannot be empty");
            return b;
        }

        if (age.getText().toString().equals("")) {
            age.setError("Age cannot be empty");
            return b;
        }




        if (height.getText().toString().equals("")) {
            height.setError("Height cannot be empty");
            return b;
        }
        b = true;
        return b;

    }


    private boolean validateFields()
    {
        boolean b=false;



        if(phoneNumber.getText().toString().trim().length()<10){
            Toast.makeText(HomeMemberProfileActivity.this,"Phone Number Not Valid",
                    Toast.LENGTH_SHORT).show();
            return  b;
        }


        if(Integer.parseInt(age.getText().toString().trim())>120){
            Toast.makeText(HomeMemberProfileActivity.this,"Age is not Valid",
                    Toast.LENGTH_SHORT).show();
            return  b;
        }
        if(!(Integer.parseInt(height.getText().toString())>50 && Integer.parseInt(height.getText().toString())<250))
        {
            Toast.makeText(HomeMemberProfileActivity.this,"Please enter a valid height",Toast.LENGTH_SHORT).show();
            return b;
        }


        b=true;

        return b;
    }


    public ContentValues addContentProfiles(String username, String family_mem_name,String age,String sex,
                                            String phone,String city,String height) {
        ContentValues values = new ContentValues();

        values.put(BplOximterdbHelper.USER_NAME, username);
        values.put(BplOximterdbHelper.NAME_HOME_USER, family_mem_name);
        values.put(BplOximterdbHelper.AGE_HOME_USER, age);
        values.put(BplOximterdbHelper.SEX_HOME_USER, sex);
        values.put(BplOximterdbHelper.PHONE_HOME_USER,phone);
        values.put(BplOximterdbHelper.CITY_HOME_USER, city);
        values.put(BplOximterdbHelper.WEIGHT_HOME_USER, "NA");
        values.put(BplOximterdbHelper.HEIGHT_HOME_USER,height );
        return values;


    }


    // to dispaly existing profile conetnts


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }


    private String get_profile_image(String key_username) {
        SharedPreferences profile_image_prefs;
        profile_image_prefs = HomeMemberProfileActivity.this.
                getSharedPreferences(Constants.PREFERENCE_PROFILE_IMAGE, Context.MODE_PRIVATE);

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
                    .with(HomeMemberProfileActivity.this)
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


            hide_soft_keypad(HomeMemberProfileActivity.this);
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

                Glide
                        .with(HomeMemberProfileActivity.this)
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
                if(ActivityCompat.shouldShowRequestPermissionRationale(HomeMemberProfileActivity.this,Manifest.permission.CAMERA)
                        || ActivityCompat.shouldShowRequestPermissionRationale(HomeMemberProfileActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE))
                {
                    Toast.makeText(HomeMemberProfileActivity.this,"Permission are necessary",Toast.LENGTH_SHORT).show();
                    showDialogOK("Camera and Write External Storage Permission required for this app",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE:
                                            ActivityCompat.requestPermissions(HomeMemberProfileActivity.this,
                                                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                    Constants.CAMERA_REQUEST_CODE);
                                            break;
                                        case DialogInterface.BUTTON_NEGATIVE:
                                            img_photo.setImageDrawable(ContextCompat.getDrawable(HomeMemberProfileActivity.this, R.drawable.user_icon));
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



        ArrayAdapter<String> adapter=new ArrayAdapter<>(context,android.R.layout.simple_list_item_1,options);
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
                    img_photo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.user_icon));
                    profile_image="";

                }else if(adapterView.getItemAtPosition(i)=="Camera")
                {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        ActivityCompat.requestPermissions(HomeMemberProfileActivity.this,
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
                    startActivityForResult(Intent
                            .createChooser(intent, "Select Picture"), Constants.SELECT_PICTURE);

                }
                dialog.dismiss();
            }

        });

        dialog.show();
    }
}
