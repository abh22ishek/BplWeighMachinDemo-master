package biolight;

import android.*;
import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.net.*;
import android.nfc.*;
import android.os.*;
import android.support.annotation.*;
import android.support.design.internal.*;
import android.support.v4.app.*;
import android.support.v4.content.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import java.io.*;
import java.util.*;

import constantsP.*;
import database.*;
import io.github.yavski.fabspeeddial.*;
import logger.*;
import logger.Level;
import logger.Logger;
import model.*;
import test.bpl.com.bplscreens.R;

public class BioLightReportActivity extends Activity {
    String sysPressure,diaPressure,pulseRate;
    TextView pulseRateText,comment;

    private CustomBPChartBioLight customBPChart;

    private final String TAG=BioLightReportActivity.class.getSimpleName();
    private FabSpeedDial fabSpeedDial;

    GlobalClass globalVariable;
    String userName="";

    ScrollView iv_scroll;

    TextView bp,name,age;
    TextView testTime;

    RelativeLayout  relative_scroll;
    String mUri;
    Handler mHandler;
    List<UserModel> userModelList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.biolight_report_wh);


        bp=findViewById(R.id.txtBP);
        name=findViewById(R.id.txtName);
        age=findViewById(R.id.txtAge);
        pulseRateText= findViewById(R.id.txtPulseRate);
        comment= findViewById(R.id.txtComment);
        customBPChart= findViewById(R.id.customBPView);

        testTime=findViewById(R.id.txtTestingTime);

        iv_scroll=findViewById(R.id.vScroll);
        relative_scroll=  findViewById(R.id.relative_scroll);
        final  TextView base_header_title=findViewById(R.id.base_header_title);

        globalVariable = (GlobalClass) getApplicationContext();
        fabSpeedDial=  findViewById(R.id.fabSpeedDial);

        final ImageView imgBackKey=findViewById(R.id.imgBackKey);
        imgBackKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


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

                    if(!isStoragePermissionGranted("save"))
                    {
                        Toast.makeText(BioLightReportActivity.this,
                                "Permission are necessary in order to save " +
                                "this file",Toast.LENGTH_SHORT).show();
                        }




                }else if(menuItem.getItemId()==R.id.share_png){

                    if(screenShotFile!=null) {

                      sharePNG();
                    }else{
                        if(!isStoragePermissionGranted("save"))
                        {
                            Toast.makeText(BioLightReportActivity.this,
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




        Logger.log(Level.DEBUG,TAG,"Systolic Pressure is ="+getIntent().
                getExtras().getString(Constants.systolic));

        if(getIntent().getExtras().getString(Constants.USER_NAME)!=null){
           userName=  getIntent().getExtras().getString(Constants.USER_NAME);

            final String mUserName=getIntent().getExtras().getString(Constants.USER_NAME);
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
                base_header_title.setText(new StringBuilder().append(mx).append(" 's ").append("BP").append(" ")
                        .append(getString(R.string.report)).toString());

            }else{
                base_header_title.setText(new StringBuilder().append(mUserName).append(" 's ").append("BP")
                        .append(" ").append(getString(R.string.report)).toString());

            }
        }




        if(globalVariable.getUserType().equalsIgnoreCase("clinic")){
             userModelList=DatabaseManager.getInstance().getAllUserprofilecontent(userName,"");
            age.setText(new StringBuilder().append(getString(R.string.age)).append(":").
                    append(userModelList.get(0).getAddress()).append(" , ").append(" ").
                    append(getString(R.string.weight)).append(": ").append(userModelList.get(0).getWeight()).append(", ").
                    append(getString(R.string.height)).append(": ").append(userModelList.get(0).getHeight()).toString());

        }else{
            userModelList=DatabaseManager.getInstance().getAllMemberProfilecontent(userName,"");
            age.setText(new StringBuilder().append(getString(R.string.age)).append(":").
                    append(userModelList.get(0).getAge()).append(" , ").append(" ").
                    append(getString(R.string.weight)).append(": ").append(userModelList.get(0).getWeight()).append(", ").
                    append(getString(R.string.height)).append(": ").append(userModelList.get(0).getHeight()).toString());

        }

        if(userModelList!=null && userModelList.size()>0){

            name.setText(new StringBuilder().append(getString(R.string.name)).append(" :").append(" ").
                    append(userName).append(",").append(" ").append(userModelList.get(0).getSex()).toString());

        }

        if(getIntent().getExtras().getString(Constants.systolic)!=null){
            String systolic=getIntent().getExtras().getString(Constants.systolic);
            if(systolic.contains(".")){

                sysPressure= systolic.substring(0,systolic.length()-2);
                }else{
                sysPressure=systolic;

            }
        }

        if(getIntent().getExtras().getString(Constants.diabolic)!=null){

            String diabolic=getIntent().getExtras().getString(Constants.diabolic);
            if(diabolic.contains(".")){
                diaPressure=diabolic.substring(0,diabolic.length()-2);
            }else{
                diaPressure=diabolic;

            }


        }



        bp.setText(new StringBuilder().append("BLOOD PRESSURE (SYS/DIA) : ").
                append(sysPressure).append("/").append(diaPressure).append(" mmHg").toString());

        if(getIntent().getExtras().getString(Constants.pulse)!=null){
            String pulse=getIntent().getExtras().getString(Constants.pulse);


            if(pulse.contains(".")){

                pulseRate= pulse.substring(0,pulse.length()-2);
            }else{
                pulseRate=pulse;

                }

                pulseRateText.setText(new StringBuilder().append(getString(R.string.pulse_rate)).
                        append("(BPM)").append(" :").append(" ").append(pulseRate).toString());
        }

        if(getIntent().getExtras().getString(Constants.comment)!=null){
            comment.setText(new StringBuilder().append("Comment : ").
                    append(getIntent().getExtras().getString(Constants.comment)).toString());
        }


        if(getIntent().getExtras().getString(Constants.TESTING_TIME_CONSTANTS)!=null){
            testTime.setText(new StringBuilder().append("Testing Time : ").
                    append(getIntent().getExtras().getString(Constants.TESTING_TIME_CONSTANTS)).toString());
        }

        float sysF = Float.parseFloat(getIntent().getExtras().getString(Constants.systolic));
        final int pointX=(int) sysF;

        float diaF = Float.parseFloat(getIntent().getExtras().getString(Constants.diabolic));
        final int pointY=(int) diaF;


        customBPChart.set_XY_points(pointY,pointX);
        customBPChart.invalidate();







    }


    private void sharePNG()
    {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        Uri contentUri = FileProvider.getUriForFile(BioLightReportActivity.this,
                BioLightReportActivity.this.getPackageName() + ".provider", screenShotFile);
        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);

        shareIntent.setType("image/png");
        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.share_via)));
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Logger.log(Level.INFO,"OnRestart ()", "gets called");


            if(getIntent().getExtras().getString("data")!=null )
            {

                if(!getIntent().getExtras().getString("data").equals("")) {

                    finish();

                }
            }
    }

    int mCountResume=0;
    @Override
    protected void onResume() {
        super.onResume();

        Logger.log(Level.INFO,"OnResume ()", "gets called");
        mHandler=new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(getIntent().getExtras().getString("data")!=null )
                {

                    if(!getIntent().getExtras().getString("data").equals(""))
                    {

                        fabSpeedDial.setVisibility(View.INVISIBLE);
                        if(mCountResume>=1){
                            return;
                        }


                        mCountResume++;

                        if(!isStoragePermissionGranted("share"))
                        {
                            Toast.makeText(BioLightReportActivity.this,"Permission " +
                                    "are necessary in order to save " +
                                    "this file",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if(screenShotFile!=null) {

                            sharePNG();

                        }

                    }else{
                        fabSpeedDial.setVisibility(View.VISIBLE);
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
                    captureScreen(globalVariable.getUsername(),userName,tag);

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
            captureScreen(globalVariable.getUsername(),userName,tag);
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Logger.log(Level.DEBUG,TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            Logger.log(Level.DEBUG,TAG,"##Capturing Screen Processed###");
            captureScreen(globalVariable.getUsername(),userName,"save");
        }
    }


    private void captureScreen(String loginName,String userName,String tag) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

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

        String name=userName+"_"+DateTime.getDateTimeinMinutes()+"_BPL_iPressure_BT02" + ".png";
        mUri=name;

        try {
            FileOutputStream fos = new FileOutputStream(saveScreenshot(name,loginName,userName));
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);

            if(tag.equalsIgnoreCase("save")){
                Toast.makeText(BioLightReportActivity.this, "BP Report is saved successfully " +
                        "in a Folder "+Constants.BPL_FOLDER, Toast.LENGTH_SHORT).show();
            }

            fos.flush();
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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

      //  mSavePath=screenShotFile.getAbsolutePath();

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



}
