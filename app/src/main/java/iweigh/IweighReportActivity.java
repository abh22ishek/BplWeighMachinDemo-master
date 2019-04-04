package iweigh;

import android.*;
import android.content.*;
import android.content.pm.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.net.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v4.app.*;
import android.support.v4.content.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import java.io.*;
import java.util.*;

import biolight.*;
import constantsP.*;
import database.*;
import io.github.yavski.fabspeeddial.*;
import logger.*;
import model.*;
import test.bpl.com.bplscreens.R;

public class IweighReportActivity extends FragmentActivity {


    RelativeLayout bmiRelativeLayout,metabolismRelativeLayout,visceralFatR;
    RelativeLayout muscleMassR,bodyFatR,bodyAgeR,proteinR,boneMassR,bodyWaterR,lbmR;
    LinearLayout menu_bar;
    String headerBar="";

    private final String TAG=IweighHomeScreenActivityl.class.getSimpleName();
    ImageView settings,record;





    String mUri;

    private TextView readingWeight,bmiTxt;
    private Button btn_save;

    TextView date;

    private GlobalClass globalVariable;

    private TextView height,age,bodyAge,visceralFat,bodyWater,bodyFat,boneMass,muscleMass,obesity,LBM,protein,metabolism;



     String mUserName;

    private TextView base_header_title;
    private ArrayList<RecordDetailWeighMachine> UserMeasuredWeightList;

    String mDate;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.iweigh_report);

        bmiRelativeLayout=findViewById(R.id.bmiRelativeLayout);

        muscleMassR=findViewById(R.id.muscleMassR);
        bodyFatR=findViewById(R.id.bodyFatR);
        bodyAgeR=findViewById(R.id.bodyAgeR);
        proteinR=findViewById(R.id.proteinR);
        boneMassR=findViewById(R.id.boneMassR);
        bodyWaterR=findViewById(R.id.bodyWaterR);
        lbmR=findViewById(R.id.LBMR);

        height=findViewById(R.id.txtheight_);
        age=findViewById(R.id.txtage_);
        settings=findViewById(R.id.img_settings);
        record=findViewById(R.id.img_records);

        date=findViewById(R.id.date);
        btn_save=findViewById(R.id.btn_save);
        btn_save.setText(getString(R.string.scan));

        btn_save.setVisibility(View.GONE);
        bmiTxt=findViewById(R.id.bmiTxt);
        bodyAge=findViewById(R.id.bodyAge);
        base_header_title = findViewById(R.id.base_header_title);
        readingWeight=findViewById(R.id.readingWeight);
        visceralFat=findViewById(R.id.visceralFat);
        bodyWater=findViewById(R.id.bodyWater);
        bodyFat=findViewById(R.id.bodyFat);
        boneMass=findViewById(R.id.boneMass);

        obesity=findViewById(R.id.obesity);
        protein=findViewById(R.id.protein);

        iv_scroll=findViewById(R.id.iv_scroll);

        muscleMass=findViewById(R.id.muscleMass);
        fabSpeedDial=  findViewById(R.id.fabSpeedDial);
        LBM=findViewById(R.id.lbm);

        metabolism=findViewById(R.id.metabolismT);


         mUserName=getIntent().getExtras().getString(Constants.USER_NAME);
        // Fetch record data


        int density =this.getResources().getDisplayMetrics().densityDpi;
        if(DisplayMetrics.DENSITY_XXHIGH==density)
        {
            base_header_title.setTextSize(22);
        }else{
            base_header_title.setTextSize(22);
        }
        if(mUserName.length()>12)
        {
            String mx= mUserName.substring(0,10)+"..";
            base_header_title.setText(new StringBuilder().append(mx).append(" 's ").append("Weight").append(" ")
                    .append(getString(R.string.report)).toString());

        }else{
            base_header_title.setText(new StringBuilder().append(mUserName).append(" 's ").append("Weight")
                    .append(" ").append(getString(R.string.report)).toString());

        }
        Logger.log(Level.DEBUG,TAG,"Get m UserName--"+mUserName);



        if (getIntent().getExtras().getString(Constants.DATE) != null) {
            mDate = getIntent().getExtras().getString(Constants.DATE);
            date.setText(mDate);
        }




        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                //TODO: Start some activity
                if(menuItem.getItemId()==R.id.action_png_)
                {

                    if(!isStoragePermissionGranted("save"))
                    {
                        Toast.makeText(IweighReportActivity.this,
                                "Permission are necessary in order to save " +
                                        "this file",Toast.LENGTH_SHORT).show();
                    }



                }else if(menuItem.getItemId()==R.id.share_png){

                    if(screenShotFile!=null) {

                        sharePNG();
                    }else{
                        if(!isStoragePermissionGranted("save"))
                        {
                            Toast.makeText(IweighReportActivity.this,
                                    "Permission are necessary in order to save " +
                                            "this file",Toast.LENGTH_SHORT).show();
                        }else{
                            sharePNG();
                        }
                    }
                }
                return true;
            }
        });


        globalVariable = (GlobalClass) getApplicationContext();




        final ImageView  mBackKey=  findViewById(R.id.imgBackKey);
        mBackKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });





        visceralFatR=findViewById(R.id.visceralFatR);

        visceralFatR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                VisceralScreenFragment visceralScreenFragment=new VisceralScreenFragment();
                callFragments(visceralScreenFragment,"visceral fat");
            }
        });

        lbmR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LBMScreenFragment lbmScreenFragment=new LBMScreenFragment();
                callFragments(lbmScreenFragment,"lbm");
            }
        });
        muscleMassR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MuscleMassScreenFragment muscleMassScreenFragment=new MuscleMassScreenFragment();
                callFragments(muscleMassScreenFragment,"muscle mass");
            }
        });


        bodyFatR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BodyFatScreenFragment bodyFatScreenFragment=new BodyFatScreenFragment();
                callFragments(bodyFatScreenFragment,"body fat");
            }
        });



        bodyAgeR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BodyAgeScreenFragment bodyAgeScreenFragment=new BodyAgeScreenFragment();
                callFragments(bodyAgeScreenFragment,"body age");

            }


        });


        proteinR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProteinScreenFragments proteinScreenFragments=new ProteinScreenFragments();
                callFragments(proteinScreenFragments,"protein");

            }
        });


        boneMassR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BoneMassScreenFragment boneMassScreenFragment=new BoneMassScreenFragment();
                callFragments(boneMassScreenFragment,"bone mass");

            }
        });




        bmiRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BMIScreenFragment bmiScreenFragment=new BMIScreenFragment();
                callFragments(bmiScreenFragment,"bmi");

            }
        });


        bodyWaterR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BodyWaterScreenFragment bodyWaterScreenFragment=new BodyWaterScreenFragment();
                callFragments(bodyWaterScreenFragment,"body water");
            }
        });

        metabolismRelativeLayout=findViewById(R.id.metabolism);
        metabolismRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MetabolismScreenFragment metabolismScreenFragment=new MetabolismScreenFragment();
                callFragments(metabolismScreenFragment,"metabolism");
            }
        });

        if(getIntent().getExtras().getString("data")!=null ) {

            if (!getIntent().getExtras().getString("data").equalsIgnoreCase("share")) {
                callRec();
            }else{
                callRec();
            }
        }
    }


    private void callRec()
    {
        if(globalVariable.getUserType().equalsIgnoreCase(Constants.USE_TYPE_HOME)) {
            DatabaseManager.getInstance().openDatabase();

            UserMeasuredWeightList = new ArrayList<>(DatabaseManager.getInstance().get_measuredWeightMachineBWithSelectedDate(mUserName,mDate));

            if (UserMeasuredWeightList.size() > 0) {
                readingWeight.setText(String.valueOf(UserMeasuredWeightList.get(0).getWeight()));
                bmiTxt.setText(String.valueOf(UserMeasuredWeightList.get(0).getBmi()));
                bodyFat.setText(String.valueOf(UserMeasuredWeightList.get(0).getBodyFat()));
                bodyWater.setText(String.valueOf(UserMeasuredWeightList.get(0).getBodyWater()));
                boneMass.setText(String.valueOf(UserMeasuredWeightList.get(0).getBoneMass()));
                muscleMass.setText(String.valueOf(UserMeasuredWeightList.get(0).getMuscleMass()));
                protein.setText(String.valueOf(UserMeasuredWeightList.get(0).getProtein()));
                visceralFat.setText(String.valueOf(UserMeasuredWeightList.get(0).getVisceralFat()));
                bodyAge.setText(String.valueOf(UserMeasuredWeightList.get(0).getBodyAge()));
                LBM.setText(String.valueOf(UserMeasuredWeightList.get(0).getLBM()));
                obesity.setText(String.valueOf(UserMeasuredWeightList.get(0).getObesity()));
                metabolism.setText(String.valueOf(UserMeasuredWeightList.get(0).getMetabolism()));



            }
        }else {
            UserMeasuredWeightList = new ArrayList<>(DatabaseManager.getInstance().get_measuredWeightMachineBWithSelectedDate(mUserName,mDate));

            if (UserMeasuredWeightList.size() > 0) {

                readingWeight.setText(String.valueOf(UserMeasuredWeightList.get(0).getWeight()));
                bmiTxt.setText(String.valueOf(UserMeasuredWeightList.get(0).getBmi()));
                readingWeight.setText(String.valueOf(UserMeasuredWeightList.get(0).getWeight()));
                bmiTxt.setText(String.valueOf(UserMeasuredWeightList.get(0).getBmi()));
                bodyFat.setText(String.valueOf(UserMeasuredWeightList.get(0).getBodyFat()));
                bodyWater.setText(String.valueOf(UserMeasuredWeightList.get(0).getBodyWater()));
                boneMass.setText(String.valueOf(UserMeasuredWeightList.get(0).getBoneMass()));
                muscleMass.setText(String.valueOf(UserMeasuredWeightList.get(0).getMuscleMass()));
                protein.setText(String.valueOf(UserMeasuredWeightList.get(0).getProtein()));
                visceralFat.setText(String.valueOf(UserMeasuredWeightList.get(0).getVisceralFat()));
                bodyAge.setText(String.valueOf(UserMeasuredWeightList.get(0).getBodyAge()));
                LBM.setText(String.valueOf(UserMeasuredWeightList.get(0).getLBM()));
                obesity.setText(String.valueOf(UserMeasuredWeightList.get(0).getObesity()));
                metabolism.setText(String.valueOf(UserMeasuredWeightList.get(0).getMetabolism()));
            }
        }
    }
    int mCountResume=0;

    Handler mHandler;
    @Override
    protected void onResume() {
        super.onResume();
        Logger.log(Level.DEBUG,"On Resume()","called");
        mHandler=new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(getIntent().getExtras().getString("data")!=null )
                {

                    if(!getIntent().getExtras().getString("data").equals(""))
                    {
                        if(mCountResume>=1){
                            return;
                        }


                        mCountResume++;

                        if(!isStoragePermissionGranted("share"))
                        {
                            Toast.makeText(IweighReportActivity.this,"Permission " +
                                    "are necessary in order to save " +
                                    "this file",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if(screenShotFile!=null) {

                            sharePNG();

                        }

                    }
                }
            }
        }, 50);



    }


    public  boolean isStoragePermissionGranted(String tag) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Logger.log(Level.DEBUG,TAG,"Permission is granted");

                if(globalVariable.getUsername()!=null){
                    captureScreen(globalVariable.getUsername(),mUserName,tag);

                }
                return true;
            } else {

                Logger.log(Level.DEBUG,TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else {
            Logger.log(Level.DEBUG,TAG,"Permission is granted");
            captureScreen(globalVariable.getUsername(),mUserName,tag);
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Logger.log(Level.DEBUG,TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            Logger.log(Level.DEBUG,TAG,"##Capturing Screen Processed###");
            captureScreen(globalVariable.getUsername(),mUserName,"save");
        }
    }


    FrameLayout iv_scroll;

    private void captureScreen(String loginName,String userName,String tag) {

        fabSpeedDial.setVisibility(View.GONE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        // create bitmap screen capture
        View v1 = getWindow().getDecorView().getRootView();
        v1.setDrawingCacheEnabled(true);
        Bitmap bmp = Bitmap.createBitmap(v1.getDrawingCache());
        v1.setDrawingCacheEnabled(false);

        //iv_scroll.getChildAt(0).measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
       /* int width=iv_scroll.getChildAt(0).getWidth()+iv_scroll.getChildAt(1).getWidth();
        int height=iv_scroll.getChildAt(0).getHeight()+iv_scroll.getChildAt(1).getHeight();


        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        //iv_scroll.getChildAt(0).layout(0, 0, iv_scroll.getChildAt(0).getMeasuredWidth(), iv_scroll.getChildAt(0).getMeasuredHeight());
        Drawable bgDrawable =iv_scroll.getChildAt(0).getBackground();
        if (bgDrawable!=null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);

        iv_scroll.getChildAt(0).draw(canvas);*/

        String name=userName+"_"+DateTime.getDateTimeinMinutes()+"_BPL_iWeigh" + ".png";
        mUri=name;

        try {
            FileOutputStream fos = new FileOutputStream(saveScreenshot(name,loginName,userName));
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);

            if(tag.equalsIgnoreCase("save")){
                Toast.makeText(IweighReportActivity.this, "BP Report is saved successfully " +
                        "in a Folder "+Constants.BPL_FOLDER, Toast.LENGTH_SHORT).show();
            }

            fos.flush();
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        finally {
            fabSpeedDial.setVisibility(View.VISIBLE);
        }
    }



    File screenShotFile;
    private File saveScreenshot(String fileName, String loginFileName, String userDirName)
    {
        String fileNameDir=Constants.BPL_FOLDER;

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
        screenShotFile=new File(userDir,fileName);


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




    @Override
    protected void onRestart() {
        super.onRestart();
        Logger.log(Level.DEBUG,"On Restart()","called");

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }




    @Override
    public void onBackPressed() {


        FragmentManager fragmentManager = getSupportFragmentManager();
        int count = fragmentManager.getBackStackEntryCount();
        Logger.log(Level.DEBUG, "I-weigh", "((--count no. fo Fragments-----))=" + count);


    }




    public void callFragments(Fragment fragment,String Tag)
    {
        android.support.v4.app.FragmentManager fragmentManager;
        android.support.v4.app.FragmentTransaction fragmentTransaction;
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.conatiner, fragment,Tag);
        fragmentTransaction.addToBackStack(Tag);
        fragmentTransaction.commit();
    }


    private void sharePNG()
    {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        Uri contentUri = FileProvider.getUriForFile(IweighReportActivity.this,
                IweighReportActivity.this.getPackageName() + ".provider", screenShotFile);
        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);

        shareIntent.setType("image/png");
        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.share_via)));
    }

    private FabSpeedDial fabSpeedDial;
}
























