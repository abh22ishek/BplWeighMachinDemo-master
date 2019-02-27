package biolight;

import android.*;
import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.net.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v4.app.*;
import android.support.v4.content.*;
import android.support.v7.widget.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.widget.PopupMenu;


import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import constantsP.*;
import database.*;
import logger.*;
import test.bpl.com.bplscreens.*;
import test.bpl.com.bplscreens.R;

public class BioLightRecordActivity extends Activity implements BioLightListner{



    TextView headerText;
    RecyclerView recyclerView;
    List<BPMeasurementModel> mRecordDetailList;

    List<String> dateTimeList;

    private final String TAG= BioLightRecordActivity.class.getSimpleName();
    HomeActivityListner homeActivityListner;
    GlobalClass globalVariable;
    private List<String> dates_list;

    @Override
    protected void onResume() {
        super.onResume();


    }
    String mUserName,mAge,mGender;
    private PopupMenu popup;
    boolean popUpTag;
    private ImageView optionsSettings;

    String currentTime="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.biolight_record);
        globalVariable= (GlobalClass) getApplicationContext();
        headerText=findViewById(R.id.base_header_title);


        recyclerView= findViewById(R.id.recycler_view);

        optionsSettings=findViewById(R.id.optionSettings);
        optionsSettings.setVisibility(View.VISIBLE);


        optionsSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup = new PopupMenu(BioLightRecordActivity.this,view);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        if(mRecordDetailList.size()==0)
                        {
                            Toast.makeText(BioLightRecordActivity.this,"No Records Found. Please Take " +
                                    "Records with BPL iPressure BT02",Toast.LENGTH_LONG).show();
                        }else{

                            if(!isStoragePermissionGranted())
                            {
                                Toast.makeText(BioLightRecordActivity.this,
                                        "Storage Permission is necessary to generate .txt File ",Toast.LENGTH_SHORT).show();

                            }

                        }
                        return true;
                    }
                });


                popup.inflate(R.menu.export_pdf);
                popup.show();
            }
        });


        final ImageView backKey=findViewById(R.id.imgBackKey);
        backKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mUserName=getIntent().getExtras().getString(Constants.USER_NAME);
        mAge= getIntent().getExtras().getString("age");
        mGender= getIntent().getExtras().getString("gender");
        // Fetch record data
        int density =this.getResources().getDisplayMetrics().densityDpi;
        if(DisplayMetrics.DENSITY_XXHIGH==density)
        {
            headerText.setTextSize(22);
        }else{
            headerText.setTextSize(22);
        }
        if(mUserName.length()>12)
        {
           String mx= mUserName.substring(0,10)+"..";
            headerText.setText(new StringBuilder().append(mx).append(" 's ").append("BP").append(" ").
                    append(getString(R.string.record)).toString());

        }else{
            headerText.setText(new StringBuilder().append(mUserName).append(" 's ").append("BP").append(" ").
                    append(getString(R.string.record)).toString());

        }

        DatabaseManager.getInstance().openDatabase();

        mRecordDetailList=new ArrayList<>();
        dateTimeList=new ArrayList<>();

        if(mUserName!=null)
        {
            mRecordDetailList= DatabaseManager.getInstance().getBioLightBPRecords(mUserName,globalVariable.getUserType());

        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(BioLightRecordActivity.this,
                LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);

        BioLightRecyclerViewAdapter recyclerViewAdapter=new
                BioLightRecyclerViewAdapter(BioLightRecordActivity.this,BioLightRecordActivity.this,
                mRecordDetailList,homeActivityListner,mUserName,mAge,mGender);



        // get list of dateTime

        if(mRecordDetailList!=null){

            for(BPMeasurementModel m:mRecordDetailList){

                dateTimeList.add(m.getMeasurementTime());
            }
        }

        Logger.log(Level.DEBUG,TAG,"// get Measurement Time List // "+dateTimeList);

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerViewAdapter);

        }


    private static void  createUsersTextFile(String fileName, String loginFileName, StringBuilder data) {
        String fileNameDir =Constants.BPL_FOLDER;

        File file = new File(Environment.getExternalStorageDirectory(), fileNameDir);
        if (!file.exists()) {
            file.mkdir();

        }

        File loginFile = new File(file, loginFileName);

        if (!loginFile.exists()) {
            loginFile.mkdir();
        }


        File listOfUsersFile = new File(loginFile, fileName);


        try {
            FileWriter filewriter = new FileWriter(listOfUsersFile, false);
            Logger.log(Level.DEBUG,"TAG",
                    "-SavingFile Path-"+listOfUsersFile.toString()+"-&&-"+listOfUsersFile.getAbsolutePath());

            filewriter.append(data);
            filewriter.flush();
            filewriter.close();
            Log.i("FilePath", "saving data into file");

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }





        // create and show the alert dialog
    public void onCreateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(BioLightRecordActivity.this);
        builder.setTitle(".txt File Created. Choose Any option");
        // add a radio button list
        final String[] colors = {"View .txt File", "Share .txt File"};
        final int checkedItem = -1; // cow

        builder.setSingleChoiceItems(colors, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user checked an item
                if (which != -1) {
                    // Write code for share
                    if (which == 0) {
                        // PDF ViewerDaysCha

                        File   mFile = new File(Environment.getExternalStorageDirectory() +
                                "/"+Constants.BPL_FOLDER+"/"+globalVariable.getUsername()+"/" +mUserName+"_bp_report_"+currentTime+".txt");


                        Intent target = new Intent(Intent.ACTION_VIEW);

                        target.setDataAndType(Uri.fromFile(mFile), "text/plain");
                        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                        Intent intent = Intent.createChooser(target, "Open File");
                        try {
                            if(Build.VERSION.SDK_INT>=24){
                                try{
                                    Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                                    m.invoke(null);
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                            }
                            startActivity(intent);
                        } catch (Exception e) {
                            // Instruct the user to install a PDF reader here, or something
                            e.printStackTrace();
                            Logger.log(Level.DEBUG,"---","Install any PDF viewer");
                        }
                    } else if (which == 1) {
                        // Share


                     File   mFile = new File(Environment.getExternalStorageDirectory() +
                                "/"+Constants.BPL_FOLDER+"/"+globalVariable.getUsername()+"/" +mUserName+"_bp_report_"+currentTime+".txt");

                     Logger.log(Level.DEBUG,TAG,"-File Path-"+mFile.toString()+"--"+mFile.getAbsolutePath());

                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        Uri contentUri = FileProvider.getUriForFile(BioLightRecordActivity.this,
                                BioLightRecordActivity.this.getPackageName() + ".provider", mFile);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        shareIntent.setType("text/plain");
                        startActivity(Intent.createChooser(shareIntent, "Share Via"));
                    } else {

                    }


                    // dialog.dismiss();
                }
            }
        });




        // add OK and Cancel buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user clicked OK
                dialog.dismiss();
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void biolightItemRecived(Object isItem) {

    }



    private void ExportBPRecords(List<BPMeasurementModel> mRecordDetailList)
    {
        StringBuilder sb=new StringBuilder();

        try {
            for(int i=0;i<mRecordDetailList.size();i++){
                sb.append(i+1).append(". ").append(" Name :").append(mUserName).
                        append(" SBP/DBP (mmHg) : ").append(mRecordDetailList.get(i).getSysPressure()).
                        append("/").append(mRecordDetailList.get(i).getDiabolicPressure()).append(" HR(bpm) :").
                        append(mRecordDetailList.get(i).getPulsePerMin()).append(" Time :").append(mRecordDetailList.get(i).getMeasurementTime()).
                        append(" BP Status :").append(mRecordDetailList.get(i).getTypeBP()).append(" Comments :").append(mRecordDetailList.get(i).getComments());
                sb.append("\n\n");

            }
            createUsersTextFile(mUserName+
                    DateTime.getDateTimeinMinutes()+"_BPL_iPressure_BT02"+".txt", globalVariable.getUsername(),sb);
            currentTime=DateTime.getDateTimeinMinutes();
            onCreateDialog();

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        Toast.makeText(BioLightRecordActivity.this,
                "Users BP Report Exported Successfully",Toast.LENGTH_SHORT).show();
    }

    public  boolean isStoragePermissionGranted() {

        if (Build.VERSION.SDK_INT >= 23) {

            if (BioLightRecordActivity.this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Logger.log(Level.DEBUG,TAG,"Permission is granted");
                ExportBPRecords(mRecordDetailList);
                return true;
            }
            else {

                Logger.log(Level.DEBUG,TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(BioLightRecordActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else {
            Logger.log(Level.DEBUG,TAG,"Permission is granted");
            ExportBPRecords(mRecordDetailList);
            return true;
        }
    }





    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Logger.log(Level.DEBUG,TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            Logger.log(Level.DEBUG,TAG,"##Capturing Screen Processed###");
            ExportBPRecords(mRecordDetailList);

        }
    }
}
