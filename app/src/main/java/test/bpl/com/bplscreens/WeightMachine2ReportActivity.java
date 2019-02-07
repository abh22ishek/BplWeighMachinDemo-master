package test.bpl.com.bplscreens;

import android.*;
import android.annotation.*;
import android.content.pm.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import android.support.annotation.*;
import android.support.design.internal.*;
import android.support.v4.app.*;
import android.util.*;
import android.view.*;
import android.view.animation.*;
import android.widget.*;

import java.io.*;
import java.util.*;

import bluetooth.*;
import constantsP.*;
import customviews.*;
import database.*;
import io.github.yavski.fabspeeddial.*;
import logger.*;
import model.*;


public class WeightMachine2ReportActivity extends FragmentActivity implements BmiListner{


    private final String TAG=WeightMachine2ReportActivity.class.getSimpleName();


    GlobalClass globalVariable;
    String userName;

    ImageView back;
    TextView dateTime,weight,bmi, age , height;
    ImageView needle;

    CurvePathView curvePathView;

    BmiListner mListner;

    ScrollView iv_scroll;
    private FabSpeedDial fabSpeedDial;

    RelativeLayout relative_scroll;

    Handler mHandler;

    TextView userNameText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.weigh_report);

      final TextView base_header_title=findViewById(R.id.base_header_title);

      needle=findViewById(R.id.needle);
      dateTime=findViewById(R.id.status);

      mListner=this;
      weight=findViewById(R.id.weight);

      bmi=findViewById(R.id.bmi_value);

      globalVariable= (GlobalClass) getApplicationContext();

      back=findViewById(R.id.imgBackKey);

      age=findViewById(R.id.txtage_);
        curvePathView= findViewById(R.id.curvePathView);
      height=findViewById(R.id.txtheight_);

      fabSpeedDial=findViewById(R.id.fabSpeedDial);

        iv_scroll=findViewById(R.id.vScroll);

        relative_scroll=  findViewById(R.id.relative_scroll);

        userNameText=findViewById(R.id.name);
        int density =this.getResources().getDisplayMetrics().densityDpi;
        if(DisplayMetrics.DENSITY_XXHIGH==density)
        {
            base_header_title.setTextSize(25);
        }else{
            base_header_title.setTextSize(22);
        }

      back.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              finish();
          }
      });

      if(getIntent().getExtras().getString(Constants.TESTING_TIME_CONSTANTS)!=null){
          dateTime.setText(getIntent().getExtras().getString(Constants.TESTING_TIME_CONSTANTS));
      }



        if(getIntent().getExtras().getString(Constants.BMI)!=null){
            bmi.setText(getIntent().getExtras().getString(Constants.BMI));
        }


        if(getIntent().getExtras().getString(Constants.USER_NAME)!=null)
        {
             userName=getIntent().getExtras().getString(Constants.USER_NAME);


            if(userName.length()>12)
            {
                String mx= userName.substring(0,10)+"..";
                base_header_title.setText(new StringBuilder().append(mx).append(" 's ").
                        append(getString(R.string.report)).toString());

            }else{
                base_header_title.setText(new StringBuilder().append(userName)
                        .append(" 's").append(getString(R.string.report)).toString());

            }



            List<UserModel> userModelList= DatabaseManager.getInstance().getAllUserprofilecontent(userName,"");

         if(userModelList!=null){
             age.setText(userModelList.get(0).getAddress());
             height.setText(userModelList.get(0).getHeight());
             }

        }







        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                return true;
            }
        });


        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                //TODO: Start some activity
                if(menuItem.getItemId()==R.id.action_png_)
                {


                    if(!isStoragePermissionGranted())
                    {
                        Toast.makeText(WeightMachine2ReportActivity.this,"Permission are necessary in order to save " +
                                "this file",Toast.LENGTH_SHORT).show();
                    }


                }
                return true;
            }
        });

    }

    int needleHeight=0;
    @Override
    protected void onResume() {
        super.onResume();
        mHandler=new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                needleHeight=needle.getHeight();
                if(getIntent().getExtras().getString(Constants.WEIGHT)!=null){

                    weight.setText(getIntent().getExtras().getString(Constants.WEIGHT));
                    setWeighDataText(getIntent().getExtras().getString(Constants.WEIGHT));
                }
                Logger.log(Level.DEBUG,"On Resume()","Pivot Y value="+needle.getHeight());
            }
        }, 1000);




    }

    private void setWeighDataText(String weight) {


            mListner.calculate_bmi(Float.parseFloat(weight),Float.parseFloat(height.getText().toString()));
            curvePathView.invalidate();
    }





    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Logger.log(Level.DEBUG,TAG,"Permission is granted");

                if(globalVariable.getUsername()!=null){
                    captureScreen(globalVariable.getUsername(),userName);

                }
                return true;
            } else {

                Logger.log(Level.DEBUG,TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else {
            Logger.log(Level.DEBUG,TAG,"Permission is granted");
            captureScreen(globalVariable.getUsername(),userName);
            return true;
        }
    }





    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Logger.log(Level.DEBUG,TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            Logger.log(Level.DEBUG,TAG,"##Capturing Screen Processed###");
            captureScreen(globalVariable.getUsername(),userName);
        }
    }


    private void captureScreen(String loginName, String userName) {

        // need to cj


        //iv_scroll.getChildAt(0).measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int width=iv_scroll.getChildAt(0).getWidth();
        int height=iv_scroll.getChildAt(0).getHeight();


        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        //iv_scroll.getChildAt(0).layout(0, 0, iv_scroll.getChildAt(0).getMeasuredWidth(), iv_scroll.getChildAt(0).getMeasuredHeight());
        Drawable bgDrawable =iv_scroll.getChildAt(0).getBackground();
        if (bgDrawable!=null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);

        iv_scroll.getChildAt(0).draw(canvas);

        String name=userName+"_"+System.currentTimeMillis()+"_iWeigh" + ".PNG";

        try {
            FileOutputStream fos = new FileOutputStream(saveScreenshot(name,loginName,userName));
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            Toast.makeText(WeightMachine2ReportActivity.this, "Screenshot Captured", Toast.LENGTH_SHORT).show();
            fos.flush();
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private File saveScreenshot(String fileName, String loginFileName, String userDirName)
    {
        String fileNameDir="BplBeWell";

        File file =new File(Environment.getExternalStorageDirectory(),fileNameDir);
        if(!file.exists())
        {
            file.mkdir();

        }

        File loginFile=new File(file,loginFileName);

        if(!loginFile.exists()){

            loginFile.mkdir();
        }

        File userDir=new File(loginFile,userDirName);

        if(!userDir.exists()){
            userDir.mkdir();
        }


        File screenShotFile=new File(userDir,fileName);

        try {
            FileWriter filewriter=new FileWriter(screenShotFile,false);
            filewriter.flush();
            Logger.log(Level.DEBUG,TAG,"Saving Screenshot into Bpl Be Well Folder");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return screenShotFile;
    }

    float bmi_index=0;
    @Override
    public void calculate_bmi(float weight, float height) {
        bmi_index=(weight)/((height/100)*(height/100));



        try{
            @SuppressLint("DefaultLocale") String s = String.format("%.1f",bmi_index);
            bmi.setText(s);
            if(Float.parseFloat(s)>=25f){
                bmi.setTextColor(Color.RED);
            }else if(Float.parseFloat(s)>18.5f && Float.parseFloat(s)<25){
                bmi.setTextColor(Color.parseColor("#7CBC50"));
            }else{
                bmi.setTextColor(Color.WHITE);
            }

            int rotationValue =Math.round(Float.parseFloat(s)); // 3
            if(rotationValue<5)
                rotate_needle(-90);
            else if(rotationValue>40)
                rotate_needle(90);
            else
                rotate_needle(Constants.ROTATION_DEGREES.get(rotationValue));


        }catch (Exception e)
        {
            e.printStackTrace();
        }


    }




    float from_degree=0f;




    @Override
    public void rotate_needle(float to_degree) {

        RotateAnimation anim = new RotateAnimation(from_degree,to_degree,0f,needle.getHeight());
        Logger.log(Level.DEBUG,TAG,"Pivot Y value="+needle.getHeight());
        anim.setInterpolator(new LinearInterpolator());
        anim.setDuration(500);
        anim.setFillAfter(true);
        needle.startAnimation(anim);
        from_degree=to_degree;
    }

}
