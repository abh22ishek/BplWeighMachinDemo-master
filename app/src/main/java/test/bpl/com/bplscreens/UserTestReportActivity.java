package test.bpl.com.bplscreens;

import android.Manifest;
import android.content.*;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.*;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.internal.NavigationMenu;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.*;
import android.util.*;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import biolight.*;
import constantsP.*;
import customviews.MyCustomSPO2Graph;
import database.DatabaseManager;
import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;
import jjoe64.graphview.DefaultLabelFormatter;
import jjoe64.graphview.GraphView;
import jjoe64.graphview.GridLabelRenderer;
import jjoe64.graphview.Viewport;
import jjoe64.graphview.series.DataPoint;
import jjoe64.graphview.series.LineGraphSeries;
import logger.Level;
import logger.Logger;
import model.UserModel;


public class UserTestReportActivity extends FragmentActivity {

    private ImageView mBackKey;
    private TextView header_title;
    private LinearLayout root_layout;

    private TextView name, age, gender, testingtime, duration, height, weight;
    private TextView event9990, event8980, event7970, event6960, event5950, event4940, event3930;
    private TextView spo2_baseline, spo2_duartion, spo2_event, spo2_minimum, spo2_lowoxyavg;
    private TextView spo2_pulserate, pulserate_avergaebpm, pulserate_minimum;


    private String TAG = UserTestReportActivity.class.getSimpleName();

    private String spo2_str = "";
    private String pr_str = "";
    private String pi_str = "";
    private String testing_time_str = "";
    public static    String duration_str = "";
    private String[] spo2_arr;

    private int counter_99_90, counter_89_80, counter_79_70, counter_69_60, counter_59_50, counter_49_40, counter_39_30 = 0;




    GraphView graphview_spo2, graphview_pr;
    public static List<String> mspo2_list, mPR_list;


    private TextView device_macid,txt_app_version;
    String device_id="";



    GlobalClass globalClass;

    FabSpeedDial fabSpeedDial;
    ScrollView iv_scroll;
    RelativeLayout relative_scroll;

    MyCustomSPO2Graph spo2_graph;


    String userName="";
    String loginName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_report);

        header_title =  findViewById(R.id.base_header_title);



        mBackKey =  findViewById(R.id.imgBackKey);
        mBackKey.setOnClickListener(listner);

        root_layout =  findViewById(R.id.root_layout);


        globalClass= (GlobalClass) getApplicationContext();

        populate_view(UserTestReportActivity.this);
        // mTestReport.setImageBitmap(convertTobitmap(UserTestReportActivity.this));
        userName=getIntent().getExtras().getString(Constants.USER_NAME);
        int density =this.getResources().getDisplayMetrics().densityDpi;

        if(DisplayMetrics.DENSITY_XXHIGH==density)
        {
            header_title.setTextSize(22);
        }else{
            header_title.setTextSize(21);
        }
        if(userName.length()>12)
        {
            String mx= userName.substring(0,9)+"..";
            header_title.setText(new StringBuilder().append(mx).append(" 's ").append("Sp02 ").
                    append(getString(R.string.report)).toString());

        }else{
            header_title.setText(new StringBuilder().append(userName).append(" 's ").
                    append("Sp02 ").append(getString(R.string.report)).toString());

        }
        init(userName);


        if (getIntent().getExtras() != null) {
            spo2_str = getIntent().getExtras().getString(Constants.SPO2_CONSTANTS, "");
            pr_str = getIntent().getExtras().getString(Constants.PR_CONSTANTS, "");
            pi_str = getIntent().getExtras().getString(Constants.PI_CONSTATNTS, "");

            testing_time_str = getIntent().getExtras().getString(Constants.TESTING_TIME_CONSTANTS, "");
            duration_str = getIntent().getExtras().getString(Constants.DURATION_CONSTANTS, "");
            device_id = getIntent().getExtras().getString(Constants.DEVICE_MACID,"");


            Logger.log(Level.INFO, TAG, "Get spo2 string Intent from" +
                    " Recycler View of Records list=" + spo2_str);

            Logger.log(Level.INFO, TAG, "Get PR string Intent" +
                    " from Recycler View of Records list=" + pr_str);


            Logger.log(Level.INFO, TAG, "Get PI string Intent from " +
                    "Recycler View of Records list=" + pi_str);



            if (spo2_str != "") {
                spo2_arr = spo2_str.split(",");
                Logger.log(Level.DEBUG, TAG, "Spo2 array=" + spo2_arr.length);
                validate_events(spo2_arr);


            }

            mspo2_list = new ArrayList<>(Arrays.asList(spo2_str.split(",")));
            mPR_list = new ArrayList<>(Arrays.asList(pr_str.split(",")));
            Logger.log(Level.DEBUG, TAG, "Spo2 list size=" + mspo2_list.size() + "---" + mspo2_list);



        }


        display_text();

        plot_Spo2_graph(mspo2_list, UserTestReportActivity.this);
        plot_PR_graph(mPR_list, UserTestReportActivity.this);


        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                //TODO: Start some activity
                if(menuItem.getItemId()==R.id.action_png_)
                {

                    if(!isStoragePermissionGranted("Save"))
                    {
                       Toast.makeText(UserTestReportActivity.this,"Permission are necessary " +
                               "in order to save " +
                               "this file",Toast.LENGTH_SHORT).show();

                    }


                }else if(menuItem.getItemId()==R.id.share_png)
                {
                    if(screenShotFile!=null) {

                        sharePNG();
                    }else{
                        if(!isStoragePermissionGranted("Save"))
                        {
                            Toast.makeText(UserTestReportActivity.this,"Permission are necessary " +
                                    "in order to save " +
                                    "this file",Toast.LENGTH_SHORT).show();
                        }else{
                            sharePNG();
                        }

                    }
                }
                return true;
            }
        });
    }


    public void init(String userName) throws ClassCastException {

        device_macid =  findViewById(R.id.txt_device_macid);
        txt_app_version=  findViewById(R.id.txt_app_version);


        fabSpeedDial=  findViewById(R.id.fabSpeedDial);

        iv_scroll=  findViewById(R.id.vScroll);
        relative_scroll=  findViewById(R.id.relative_scroll);

        name =  findViewById(R.id.txtName);
        age =  findViewById(R.id.txtAge);
        gender =  findViewById(R.id.txtGender);
        testingtime =  findViewById(R.id.txttestingtime);
        duration =  findViewById(R.id.txtduration);
        height =  findViewById(R.id.txtHeight);
        weight =  findViewById(R.id.txtWeight);


        event9990 =  findViewById(R.id.txt9990);
        event8980 =  findViewById(R.id.txt8980);
        event7970 =  findViewById(R.id.txt7970);
        event6960 =  findViewById(R.id.txt6960);
        event5950 =  findViewById(R.id.txt5950);
        event4940 =  findViewById(R.id.txt4940);
        event3930 =  findViewById(R.id.txt3930);

        spo2_baseline =  findViewById(R.id.baseline);
        spo2_duartion =  findViewById(R.id.duration);

        spo2_event =  findViewById(R.id.event);

        spo2_minimum =  findViewById(R.id.minimum);
        spo2_lowoxyavg =  findViewById(R.id.lowoxyavg);
        spo2_pulserate =  findViewById(R.id.pulseRate);
        pulserate_avergaebpm =  findViewById(R.id.avgbpm);
        pulserate_minimum =  findViewById(R.id.minimumbpm);


        graphview_spo2 = findViewById(R.id.graph_spo2);
        graphview_pr = findViewById(R.id.graph_pr);
        List<UserModel>UserModellist;
        if (Constants.LOGGED_User_ID != null || !Constants.LOGGED_User_ID.equals("")) {
            DatabaseManager.getInstance().openDatabase();

            if(globalClass.getUserType()!=null && globalClass.getUserType().
                    equalsIgnoreCase("home")){
              UserModellist = new ArrayList<>(DatabaseManager.getInstance().
                        getAllMemberProfilecontent(userName,""));

            }else{
                 UserModellist = new ArrayList<>(DatabaseManager.getInstance().
                        getAllUserprofilecontent(userName,""));

            }

            if (UserModellist.size() > 0) {
               loginName=UserModellist.get(0).getName();
               if(globalClass.getUserType().equalsIgnoreCase("home")){
                   age.setText(UserModellist.get(0).getAge());
               }else{
                   age.setText(UserModellist.get(0).getAddress());
               }

                weight.setText(UserModellist.get(0).getWeight());
                height.setText(UserModellist.get(0).getHeight());
                Logger.log(Level.DEBUG, TAG, "GENDER=" + UserModellist.get(0).getSex());
                gender.setText(UserModellist.get(0).getSex());
            }

        }
    }


    public void display_text() {

        device_macid.setText(new StringBuilder().append("Device Id:  ").append(device_id).toString());
        name.setText(userName);
        PackageInfo pInfo=null;

        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        txt_app_version.setText(String.format("App Version: %s", pInfo.versionName));



        event9990.setText(new StringBuilder().append(counter_99_90).toString());
        event8980.setText(new StringBuilder().append(counter_89_80).toString());

        event7970.setText(new StringBuilder().append(counter_79_70).toString());
        event6960.setText(new StringBuilder().append(counter_69_60).toString());
        event5950.setText(new StringBuilder().append(counter_59_50).toString());
        event4940.setText(new StringBuilder().append(counter_49_40).toString());
        event3930.setText(new StringBuilder().append(counter_39_30).toString());


        testingtime.setText(new StringBuilder().append(testing_time_str).toString());
        duration.setText(new StringBuilder().append(duration_str).append("s").toString());


        spo2_baseline.setText(spo2_str.split(",")[0]);
        spo2_duartion.setText(new StringBuilder().append(0).toString());
        spo2_event.setText(new StringBuilder().append(0).toString());

        if (spo2_str != "") {
            List<String> spo2_integer = new ArrayList<>(Arrays.asList(spo2_arr));
            Collections.sort(spo2_integer);
            Logger.log(Level.INFO, TAG, "spo2 integer=" + spo2_integer);
            spo2_minimum.setText(spo2_integer.get(0));
        }


        if (pr_str != "") {
            int x = 0;
            List<String> pr_list = new ArrayList<String>(Arrays.asList(pr_str.split(",")));

            for (String s : pr_list) {
                x = x + Integer.parseInt(s);
            }
            pulserate_avergaebpm.setText(new StringBuilder().append(x / pr_list.size()).toString());
            Collections.sort(pr_list);
            Logger.log(Level.INFO, TAG, " pr_list=" + pr_list);
            pulserate_minimum.setText(pr_list.get(0));
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }


    View.OnClickListener listner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (v == mBackKey) {
                finish();

            }

        }
    };


    protected View populate_view(Context context) {


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.testreport, root_layout);
        spo2_graph=  findViewById(R.id.spo2_custom_graph);
        Logger.log(Level.INFO, TAG, "view returned=" + v);
        return v;


    }


    private void validate_events(String[] spo2_arr) {
        for (String s : spo2_arr) {
            if (Integer.parseInt(s) >= 90 && Integer.parseInt(s) <= 99) {
                counter_99_90++;
            } else if (Integer.parseInt(s) >= 80 && Integer.parseInt(s) <= 89) {
                counter_89_80++;
            } else if (Integer.parseInt(s) >= 70 && Integer.parseInt(s) <= 79) {
                counter_79_70++;
            } else if (Integer.parseInt(s) >= 60 && Integer.parseInt(s) <= 69) {
                counter_69_60++;
            } else if (Integer.parseInt(s) >= 50 && Integer.parseInt(s) <= 59) {
                counter_59_50++;
            } else if (Integer.parseInt(s) >= 40 && Integer.parseInt(s) <= 49) {
                counter_49_40++;
            } else if (Integer.parseInt(s) >= 30 && Integer.parseInt(s) <= 39) {
                counter_39_30++;
            }

        }

    }


    private void plot_Spo2_graph(List<String> spo2_list, Context context) {


        DataPoint[] dataPoints = new DataPoint[spo2_list.size()];

        for (int i = 0; i < dataPoints.length; i++) {
            dataPoints[i] = new DataPoint(i, Double.parseDouble(spo2_list.get(i)));
        }

        LineGraphSeries<DataPoint> mSeries = new LineGraphSeries<>(dataPoints);
        Viewport viewport = graphview_spo2.getViewport();
        viewport.setBackgroundColor(getResources().getColor(R.color.separator));
        mSeries.setThickness(3);
        graphview_spo2.setTitle("Spo2");
        graphview_spo2.setTitleColor(Color.BLACK);
        graphview_spo2.setTitleTextSize(22f);
        graphview_spo2.getGridLabelRenderer().setTextSize(24f);
        mSeries.setColor(Color.BLACK);
        //  mSeries.setDrawBackground(true);
        // mSeries.setBackgroundColor(Color.WHITE);

        graphview_spo2.getGridLabelRenderer().setHorizontalLabelsVisible(true);// remove horizontal x labels and line
        graphview_spo2.getGridLabelRenderer().setVerticalLabelsVisible(true);
        // graphview_spo2.getGridLabelRenderer().set
        graphview_spo2.getGridLabelRenderer().setHorizontalLabelsColor(Color.BLACK);
        graphview_spo2.getGridLabelRenderer().setVerticalLabelsColor(Color.BLACK);


        graphview_spo2.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.BOTH);
        graphview_spo2.getGridLabelRenderer().setHighlightZeroLines(true);
        graphview_spo2.setBackgroundColor(Color.TRANSPARENT);
        graphview_spo2.getGridLabelRenderer().setGridColor(Utility.getColorWrapper(context, R.color.separator));
        graphview_spo2.getGridLabelRenderer().reloadStyles();

        graphview_spo2.addSeries(mSeries);
        graphview_spo2.getViewport().setXAxisBoundsManual(true);
        // graphview_spo2.getGridLabelRenderer().setNumHorizontalLabels(9);
        graphview_spo2.getGridLabelRenderer().setNumVerticalLabels(5);

        //graph2.getViewport().setBackgroundColor();
        graphview_spo2.getViewport().setMinX(0);
        graphview_spo2.getViewport().setMaxX(spo2_list.size());


        graphview_spo2.getViewport().setYAxisBoundsManual(true);
        graphview_spo2.getViewport().setMinY(80);  // set the min value of the viewport of y axis
        graphview_spo2.getViewport().setMaxY(100);
        graphview_pr.getViewport().setScalable(true);
        graphview_spo2.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // show normal x values
                    value = Math.round(value);
                    return super.formatLabel(value, isValueX) + "s";
                } else {
                    // show currency for y values
                    return super.formatLabel(value, isValueX);
                }
            }
        });


    }


    private void plot_PR_graph(List<String> pr_list, Context context) {


        DataPoint[] dataPoints = new DataPoint[pr_list.size()];

        for (int i = 0; i < dataPoints.length; i++) {
            dataPoints[i] = new DataPoint(i, Double.parseDouble(pr_list.get(i)));
        }

        LineGraphSeries<DataPoint> mSeries = new LineGraphSeries<DataPoint>(dataPoints);
        Viewport viewport = graphview_spo2.getViewport();
        viewport.setBackgroundColor(getResources().getColor(R.color.separator));
        mSeries.setThickness(3);
        graphview_pr.setTitle("PR");
        graphview_pr.setTitleColor(Color.BLACK);
        graphview_pr.setTitleTextSize(22f);
        graphview_pr.getGridLabelRenderer().setTextSize(24f);
       // graph.getGridLabelRenderer().reloadStyles();
        mSeries.setColor(Color.BLACK);

        mSeries.setColor(Color.BLACK);
        // mSeries.setDrawBackground(true);
        // mSeries.setBackgroundColor(Color.BLACK);

        graphview_pr.getGridLabelRenderer().setHorizontalLabelsVisible(true);// remove horizontal x labels and line
        graphview_pr.getGridLabelRenderer().setVerticalLabelsVisible(true);
        // graphview_spo2.getGridLabelRenderer().set
        graphview_pr.getGridLabelRenderer().setHorizontalLabelsColor(Color.BLACK);
        //  graphview_pr.getGridLabelRenderer().setTextSize(25f);
        graphview_pr.getGridLabelRenderer().setVerticalLabelsColor(Color.BLACK);
        graphview_pr.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.BOTH);
        graphview_pr.getGridLabelRenderer().setHighlightZeroLines(false);
        graphview_pr.setBackgroundColor(Color.TRANSPARENT);
        graphview_pr.getGridLabelRenderer().setGridColor(getResources().getColor(R.color.separator));
        graphview_pr.getGridLabelRenderer().reloadStyles();

        graphview_pr.addSeries(mSeries);
        graphview_pr.getViewport().setXAxisBoundsManual(true);


        //graph2.getViewport().setBackgroundColor();
        graphview_pr.getViewport().setMinX(0);
        graphview_pr.getViewport().setMaxX(pr_list.size());


        graphview_pr.getViewport().setYAxisBoundsManual(true);
        graphview_pr.getViewport().setMinY(40);  // set the min value of the viewport of y axis
        graphview_pr.getViewport().setMaxY(240);

        // graphview_pr.getGridLabelRenderer().setNumHorizontalLabels(9);
        graphview_pr.getGridLabelRenderer().setNumVerticalLabels(6);

        graphview_pr.getViewport().setScalable(true);

        graphview_pr.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // show normal x values
                    //value = Math.round(value);

                    return super.formatLabel(value,isValueX) + "s";


                } else {
                    // show currency for y values
                    return super.formatLabel(value,isValueX);
                }
            }
        });

    }


    private void captureScreen(String loginName,String userName,String Tag) {

        //iv_scroll.getChildAt(0).measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Bitmap bmp = Bitmap.createBitmap(iv_scroll.getChildAt(0).getMeasuredWidth(), iv_scroll.getChildAt(0).getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        //iv_scroll.getChildAt(0).layout(0, 0, iv_scroll.getChildAt(0).getMeasuredWidth(), iv_scroll.getChildAt(0).getMeasuredHeight());
        Drawable bgDrawable =iv_scroll.getChildAt(0).getBackground();
        if (bgDrawable!=null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);

        iv_scroll.getChildAt(0).draw(canvas);

        String name=userName+"_"+DateTime.getDateTimeinMinutes()+"_BPL_iOxy" + ".PNG";

        try {
            FileOutputStream fos = new FileOutputStream(saveScreenshot(name,loginName,userName));
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            if(Tag.equalsIgnoreCase("save")){
                Toast.makeText(UserTestReportActivity.this, "BPL iOxy Report is saved successfully in a folder " +
                        "Bpl Be Well", Toast.LENGTH_SHORT).show();
            }

            fos.flush();
            fos.close();
          
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();


    }



   /* private void captureScreen(int resId) {
        View v= findViewById(resId).getRootView();
        Logger.log(Level.INFO,TAG,"Get Root view="+v.getId());
        if(v!=null)
        {
            v.setDrawingCacheEnabled(true);
            Bitmap bmp = Bitmap.createBitmap(v.getDrawingCache());
            v.setDrawingCacheEnabled(false);
            v.buildDrawingCache(true);

            try {
                FileOutputStream fos = new FileOutputStream(new File(Environment
                        .getExternalStorageDirectory().toString(), "Spo2_Test"
                        + System.currentTimeMillis() + ".PNG"));
                bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                Toast.makeText(UserTestReportActivity.this, "Screen Saved", Toast.LENGTH_SHORT).show();
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/


    public  boolean isStoragePermissionGranted(String Tag) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Logger.log(Level.DEBUG,TAG,"Permission is granted");
                captureScreen(loginName,userName,Tag);
                return true;
            } else {

                Logger.log(Level.DEBUG,TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
                return false;
            }
        }
        else {
            Logger.log(Level.DEBUG,TAG,"Permission is granted");
            captureScreen(loginName,userName,Tag);
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Logger.log(Level.DEBUG,TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            Logger.log(Level.DEBUG,TAG,"##Snapshot Screen Processed###");
            captureScreen(loginName,userName,"save");
        }
    }


    private void sharePNG()
    {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        Uri contentUri = FileProvider.getUriForFile(UserTestReportActivity.this,
                UserTestReportActivity.this.getPackageName() + ".provider", screenShotFile);
        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);

        shareIntent.setType("image/png");
        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.share_via)));
    }


    File screenShotFile;
    private  File saveScreenshot(String fileName,String loginFileName,String userDirName)
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
}
