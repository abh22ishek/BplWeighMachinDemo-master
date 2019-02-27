package test.bpl.com.bplscreens;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Keep;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import bluetooth.BLEService;
import bluetooth.BLEServiceHelper;
import bluetooth.BLEdevicelistActivity;
import constantsP.Constants;
import constantsP.DateTime;
import constantsP.GlobalClass;
import constantsP.Utility;
import customviews.HorizontalProgressDrawable;
import customviews.ProgressDrawable;
import database.BplOximterdbHelper;
import database.DatabaseManager;
import jjoe64.graphview.GraphView;
import jjoe64.graphview.GridLabelRenderer;
import jjoe64.graphview.series.DataPoint;
import jjoe64.graphview.series.LineGraphSeries;
import logger.Level;
import logger.Logger;


@Keep
public class HomeScreenActivity extends FragmentActivity {

    public ImageView img_settings, img_records, img_help, img_speaker, img_bluetooth;
    private boolean exit = false;

    private TextView tx1, txt2, txt3, txt4;
    private static String TAG = HomeScreenActivity.class.getSimpleName();
    private int x = 20;
    private int count1 = 5;
    private int count2 = 10;
    private int count3 = 15;
    private int count4 = 20;


    private ProgressBar horizontal_progressbar;
    private ProgressBar vprogressbar;

    private boolean mStoptimer;
    private boolean mEqulaiser;
    private boolean mPlethysysmograph;

    int counter = 0;
    public TextView spo2, heart_rate, perfusion_index;

    // GATT Serveice
    protected BLEService mBluetoothLeService;

    private BleBroadCastReceiver myBleRecever;

    private boolean isBindServise;

    private boolean isBleseviceRegister = false;
    private boolean mStophr = false;

    ImageView heart_icon;

    int seconds_counter = 0;


    private TextView spo2_limit, pulserate_limit, pi_limit;




    public MediaPlayer mPlayer;
    public static boolean mSpaeaker = false;

    private long start_time;
    private long end_time;
    private long difference;

    private Vector<String> spo2_vector;
    private Vector<String> pr_vector;
    private Vector<String> pi_vector;
    public static List<String> mPlethysymograph_vector;


    private String spo2_str = "";
    private String pr_str = "";
    private String pi_str = "";

    SQLiteDatabase mDatabase;
    GlobalClass globalVariable;
    private TextView spo2_tolerance_status, pr_tolerance_status, pi_toloerance_status;

    private String spo2_low_limit = "85";
    private String spo2_high_limit = "100";


    private String pr_low_limit = "50";
    private String pr_high_limit = "150";

    private String pi_low_limit = "0";
    private String pi_high_limit = "20";

    private boolean mAlert;
    SoundPool mSoundpool;
    int mSound_id;


    boolean c_Val_spo2;
    boolean c_Val_plethysymograph;

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    int c_count = 1;

    ProgressDialog pDialog;
    String device_macid = "";
    static AlertDialog alert;

    public static double d = 0;
    GraphView grapView;

    private LineGraphSeries<DataPoint> mSeries2;
    private double graph2LastXValue = 0;

    int count_px = 0;
    int mPixelsPerCm;

    int aq_count = 0;
    boolean mUpdateGraph;


    boolean mstart_recording;
    String recording_TAG = "START";
    Button btn_start_recording;

    private String recording_time="";
    RelativeLayout relativeLayout2;

    String mUserName="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_hdpi);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);


        relativeLayout2=  findViewById(R.id.relative_layout2);


        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            PERMISSION_REQUEST_COARSE_LOCATION);
                }
            }
        }


       mUserName= getIntent().getExtras().getString(Constants.USER_NAME);

        myBleRecever = new BleBroadCastReceiver();
        registerReceiver(myBleRecever, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        globalVariable = (GlobalClass) getApplicationContext();
        Constants.LOGGED_User_ID = globalVariable.getUsername();
        mStophr = true;

        mSpaeaker = true;

        mSoundpool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        mSound_id = mSoundpool.load(this, R.raw.hospital_beep, 1);

        Logger.log(Level.INFO, TAG, "on Create() called");
        mPlethysymograph_vector = new ArrayList<String>();
        init();
        final float density = getResources().getDisplayMetrics().densityDpi;
        mPixelsPerCm = (int) (density / 100f);
        mUpdateGraph = true;
        if (device_macid == "") {
            show_dialog(HomeScreenActivity.this);
        }


    }


    public void init_BLE_connetcion() {
        registerReceiver(mGattUpdateReceiver, BLEServiceHelper.makeGattUpdateIntentFilter());
        isBleseviceRegister = true;
    }

    protected void startService() {
        Intent gattServiceIntent = new Intent(HomeScreenActivity.this, BLEService.class);
        isBindServise = bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    // Binding service to activity
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            // ??services??

            mBluetoothLeService = ((BLEService.LocalBinder) service).getService();
            mBluetoothLeService.connect_to_device(device_macid);
            pDialog.setMessage("Connecting to device..");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    private void init() throws ClassCastException {


        grapView = (GraphView) findViewById(R.id.graph2);
        btn_start_recording = (Button) findViewById(R.id.btn_start_recording);
        btn_start_recording.setOnClickListener(listner);
        // graphView1= (GraphView) findViewById(R.id.graph1);


        img_settings = (ImageView) findViewById(R.id.img_settings);
        img_records = (ImageView) findViewById(R.id.img_records);
        img_help = (ImageView) findViewById(R.id.img_help);
        img_speaker = (ImageView) findViewById(R.id.img_speaker);
        img_bluetooth = (ImageView) findViewById(R.id.imgBluetooth);

        spo2_tolerance_status = (TextView) findViewById(R.id.spo2_tolerance_status);
        pr_tolerance_status = (TextView) findViewById(R.id.pr_tolerance_status);
        pi_toloerance_status = (TextView) findViewById(R.id.pi_tolerance_status);

        spo2_tolerance_status.setVisibility(View.GONE);
        pr_tolerance_status.setVisibility(View.GONE);
        pi_toloerance_status.setVisibility(View.GONE);


        img_settings.setOnClickListener(listner);
        img_help.setOnClickListener(listner);
        img_records.setOnClickListener(listner);
        img_speaker.setOnClickListener(listner);
        img_bluetooth.setOnClickListener(listner);
        img_bluetooth.setAlpha(0.2f);

        horizontal_progressbar = (ProgressBar) findViewById(R.id.progressDownload);
        HorizontalProgressDrawable h = new HorizontalProgressDrawable();
        horizontal_progressbar.setProgressDrawable(h);

        vprogressbar = (ProgressBar) findViewById(R.id.vprogressbar);
        final float density = getResources().getDisplayMetrics().densityDpi;
        ProgressDrawable drawable = new ProgressDrawable(density, HomeScreenActivity.this);
        vprogressbar.setProgressDrawable(drawable);

        spo2 = (TextView) findViewById(R.id.spo2);
        heart_rate = (TextView) findViewById(R.id.heart_rate);
        perfusion_index = (TextView) findViewById(R.id.perfusion_index);


        tx1 = (TextView) findViewById(R.id.textView1);
        txt2 = (TextView) findViewById(R.id.textView2);
        txt3 = (TextView) findViewById(R.id.textView3);
        txt4 = (TextView) findViewById(R.id.textView4);

        tx1.setText("");
        txt2.setText("");
        txt3.setText("");
        txt4.setText("");


        spo2_limit = (TextView) findViewById(R.id.limit1);
        pulserate_limit = (TextView) findViewById(R.id.limit2);
        pi_limit = (TextView) findViewById(R.id.limit3);


        spo2_limit.setText("85/100");
        pulserate_limit.setText("50/150");
        pi_limit.setText("5/20");


        heart_icon = (ImageView) findViewById(R.id.imageViewheart);
        heart_icon.setOnClickListener(listner);
        mStoptimer = true;

        plot_graph();
        // plot_second();

       /* FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        spo2_frag=new Spo2GraphFragment();

        transaction.add(R.id.relative_layout_realtimechart, spo2_frag);
        transaction.addToBackStack(null);
        transaction.commit();*/


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Logger.log(Level.INFO, TAG, "Data coming from settings screen" +
                        "" + data.getExtras().getString(Constants.SPO2_LOW, "0"));
                spo2_limit.setText(data.getExtras().getString(Constants.SPO2_LOW, "85") + "/" +
                        data.getExtras().getString(Constants.SPO2_HIGH, "100"));

                pulserate_limit.setText(data.getExtras().getString(Constants.HEART_RATE_LOW, "50") + "/" +
                        data.getExtras().getString(Constants.HEART_RATE_HIGH, "150"));

                pi_limit.setText(data.getExtras().getString(Constants.PI_LOW, "5") + "/" +
                        data.getExtras().getString(Constants.PI_HIGH, "20"));
            }

        }

        if (requestCode == Constants.BLUETOOTH_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            device_macid = data.getStringExtra(Constants.MAC_ADDRESS);
            if ((device_macid != "") || (device_macid != null)) {
                pDialog = new ProgressDialog(HomeScreenActivity.this);
                pDialog.setMessage("Please wait....");
                pDialog.setIndeterminate(true);
                pDialog.setCanceledOnTouchOutside(false);
                pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pDialog.show();
                mStoptimer = true;
                startService();
                init_BLE_connetcion();
            }
        } else if (requestCode == Constants.BLUETOOTH_REQUEST_CODE && resultCode == RESULT_OK && data == null) {
            isBleseviceRegister = false;
            isBindServise = false;
        }
    }


    View.OnClickListener listner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent;
            switch (v.getId()) {
                case R.id.img_settings:

                    intent = new Intent(HomeScreenActivity.this, SettingsActivity.class);
                    startActivityForResult(intent, Constants.REQUEST_CODE);
                    break;

                case R.id.img_help:
                    intent = new Intent(HomeScreenActivity.this,UserGuideContent.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    break;

                case R.id.img_speaker:

                    if (mSpaeaker) {
                        img_speaker.setImageResource(R.mipmap.speaker_mute);
                        mSpaeaker = false;

                    } else {
                        img_speaker.setImageResource(R.mipmap.speaker_on);
                        mSpaeaker = true;

                    }

                    break;


                case R.id.img_records:
                    intent = new Intent(HomeScreenActivity.this, UsersRecordActivity.class);
                    intent.putExtra(Constants.USER_NAME,mUserName);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    break;

                case R.id.imgBluetooth:
                    break;

                case R.id.imageViewheart:
                    break;

                case R.id.btn_start_recording:
                    if (device_macid == "") {
                        show_dialog(HomeScreenActivity.this);
                      //  Toast.makeText(HomeScreenActivity.this, R.string.connect_device, Toast.LENGTH_LONG).show();
                    } else {

                        if (recording_TAG.equalsIgnoreCase("START")) {
                            recording_TAG = "STOP";
                            start_recording();
                            btn_start_recording.setText(R.string.stop_rec);
                        } else {
                            recording_TAG = "START";
                            stop_recording();
                            btn_start_recording.setText(R.string.start_rec);

                        }

                    }
                    break;


            }

        }
    };


    private void start_timer() {

        TimerThread t = new TimerThread();
        t.start();
        Logger.log(Level.DEBUG, TAG, "Calling handler++ =timer thread gets called");


    }



    class TimerThread extends Thread {


        @Override
        public synchronized void run() {
            super.run();

            while (mStoptimer) {
                Logger.log(Level.DEBUG, TAG, "mStoptimer value=" + mStoptimer + "*Count_px=*" + count_px);

                if (count_px == 0) {
                    pxHandler.sendEmptyMessage(Constants.TIMER_FIRST_TWENTY_SEC);

                    count_px = 1;// for first 20 sec
                    while (counter <= 100) {
                        horizontal_progressbar.setProgress(counter);
                        counter += 5;

                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // break the loop if user stop before 20 sec
                        if (!mStoptimer) {
                            horizontal_progressbar.setProgress(0);
                            counter = 0;
                            break;
                        }
                    }


                } else {
                    counter = 0;
                    count1 = count1 + x;
                    count2 = count2 + x;
                    count3 = count3 + x;
                    count4 = count4 + x;
                    pxHandler.sendEmptyMessage(Constants.TIMER_AFTER_TWENTY_SEC);


                    Logger.log(Level.INFO, TAG, "Get count1 value=" + count1);
                    Logger.log(Level.INFO, TAG, "Get count2 value=" + count2);
                    Logger.log(Level.INFO, TAG, "Get count3 value=" + count3);
                    Logger.log(Level.INFO, TAG, "Get count4 value=" + count4);

                    Logger.log(Level.INFO, TAG, "Get counter value=" + counter);

                    while (counter < 100) {
                        counter += 5;
                        Logger.log(Level.INFO, TAG, "Get counter<100 value++=" + counter);
                        horizontal_progressbar.setProgress(counter);
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                        if (!mStoptimer)
                            break;
                    }

                    pxHandler.sendEmptyMessage(Constants.TIMER_DATA_TEXT);
                    counter = 0;

                }


            }
        }


    }


    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            int i = msg.what;

            if (i == Constants.TIMER_DATA) {
                tx1.setText(DateTime.secondsTominutes(count1 * 1000));
                txt2.setText(DateTime.secondsTominutes(count2 * 1000));
                txt3.setText(DateTime.secondsTominutes(count3 * 1000));
                txt4.setText(DateTime.secondsTominutes(count4 * 1000));
            }
        }
    };


    class VprogressBar extends Thread {

        int percentage;


        public VprogressBar() {
            super();
        }


        @Override
        public synchronized void run() {
            // TODO Auto-generated method stub
            super.run();


            while (mEqulaiser) {

                while (!spo2.getText().toString().equals("--") && mEqulaiser) {

                    if (Double.parseDouble(spo2.getText().toString()) < 100 && Double.parseDouble(spo2.getText().toString()) > 95) {
                        percentage = 10;
                    } else {
                        percentage = Integer.parseInt(spo2.getText().toString()) / 10;
                    }


                    Logger.log(Level.INFO, TAG, "Percentage=" + percentage);

                    for (int i = 1; i <= percentage; i++) {
                        vprogressbar.setProgress(10 * i);


                        try {
                            sleep(100);
                        } catch (Exception e) {
                            // TODO: handle exception
                            e.printStackTrace();
                        }

                        if (i == 10) {
                            vprogressbar.setProgress(0);
                            break;
                        }


                    }

                }


            }
        }

    }


    // for BLE Connection

    /*
   * Listener class,to receive the data at the real time
   */
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {

        @SuppressWarnings("unused")
        @Override
        public void onReceive(Context context, final Intent intent) {
            final String action = intent.getAction();
            Logger.log(Level.INFO, TAG, "mGattUpdateReceiver Action=" + action);

            if (BLEService.ACTION_GATT_SERVICES_DISCOVERED
                    .equals(action)) {
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
                Logger.log(Level.INFO, TAG, "The device is ready");
                tx1.setText(count1 + "sec");
                txt2.setText(count2 + "sec");
                txt3.setText(count3 + "sec");
                txt4.setText(count4 + "sec");

                c_count = 0;

            } else if (BLEService.ACTION_DATA_AVAILABLE.equals(action)) {
                Logger.log(Level.INFO, TAG, "The data has been read");


                byte[] data = intent.getExtras().getByteArray("data");
                if (data[0] == -128) {
                    Logger.log(Level.DEBUG, TAG, "data[1]=" + data[1]);
                    Logger.log(Level.DEBUG, TAG, "data[2]=" + data[2]);
                    Logger.log(Level.DEBUG, TAG, "data[3]=" + data[3]);

                    Logger.log(Level.DEBUG, TAG, "data[4]=" + data[4]);
                    Logger.log(Level.DEBUG, TAG, "data[5]=" + data[5]);
                    Logger.log(Level.DEBUG, TAG, "data[6]=" + data[6]);

                    Logger.log(Level.DEBUG, TAG, "data[7]=" + data[7]);
                    Logger.log(Level.DEBUG, TAG, "data[8]=" + data[8]);
                    Logger.log(Level.DEBUG, TAG, "data[9]=" + data[9]);
                    Logger.log(Level.DEBUG, TAG, "data[10]=" + data[10]);


                    for (int x = 1; x < data.length; x++) {
                        mPlethysymograph_vector.add("" + data[x]);

                        if (mPlethysymograph_vector.size() > 9) {

                            Logger.log(Level.DEBUG, TAG, "***acquisition count***=" + aq_count);
                            Logger.log(Level.DEBUG, TAG, "synchronized list=" + mPlethysymograph_vector);
                            pxHandler.obtainMessage(Constants.PLETHY_1000, mPlethysymograph_vector.toString()).sendToTarget();
                            mPlethysymograph_vector.clear();


                        }

                    }


                    if (!c_Val_plethysymograph) {
                        start_time = System.currentTimeMillis();
                        c_Val_plethysymograph = true;
                    }


                } else if (data[0] == -127) {
                    Logger.log(Level.DEBUG, TAG, "data[0]=-127 gets called");
                    // get_plethysemovector();

                    int pulse = (byte) data[1] & 0xFF;
                    int oxygen = (byte) data[2] & 0xFF;
                    int pi = (byte) data[3] & 0xFF;


                    if (pulse != 255) {
                        seconds_counter++;
                    }

                    if (oxygen == 127) {
                        spo2.setText("--");

                        spo2_tolerance_status.setVisibility(View.GONE);
                        spo2.setTextColor(Utility.getColorWrapper(HomeScreenActivity.this, R.color.white));


                    } else {

                        if (!c_Val_spo2) {
                            Logger.log(Level.DEBUG, TAG, "c_Val_spo2=true");
                            c_Val_spo2 = true;
                            mEqulaiser = true;
                            new VprogressBar().start();
                        }


                        if (mstart_recording) {
                            spo2_vector.add("" + oxygen);
                        }

                        if (oxygen > Integer.parseInt(spo2_high_limit)) {
                            spo2_tolerance_status.setVisibility(View.VISIBLE);
                            spo2_tolerance_status.setText("High");
                            mAlert = true;
                            pxHandler.sendEmptyMessage(Constants.ALRM_TONE);
                            spo2_tolerance_status.setTextColor(Utility.getColorWrapper(HomeScreenActivity.this, R.color.button_text_color));
                            spo2.setTextColor(Utility.getColorWrapper(HomeScreenActivity.this, R.color.button_text_color));
                        } else if (oxygen < Integer.parseInt(spo2_low_limit)) {
                            spo2_tolerance_status.setVisibility(View.VISIBLE);
                            spo2_tolerance_status.setText("Low");
                            mAlert = true;
                            pxHandler.sendEmptyMessage(Constants.ALRM_TONE);
                            spo2_tolerance_status.setTextColor(Utility.getColorWrapper(HomeScreenActivity.this, R.color.button_text_color));
                            spo2.setTextColor(Utility.getColorWrapper(HomeScreenActivity.this, R.color.button_text_color));
                        } else {

                            spo2_tolerance_status.setVisibility(View.GONE);
                            mAlert = false;
                            spo2.setTextColor(Utility.getColorWrapper(HomeScreenActivity.this, R.color.white));
                        }
                        spo2.setText("" + oxygen);


                    }

                    if (pulse == 255) {
                        heart_rate.setText("--");
                        pr_tolerance_status.setVisibility(View.GONE);
                        heart_rate.setTextColor(Utility.getColorWrapper(HomeScreenActivity.this, R.color.white));


                    } else {
                        if (mstart_recording) {
                            pr_vector.add(String.valueOf(pulse));
                        }

                        if (pulse > Integer.parseInt(pr_high_limit)) {
                            pr_tolerance_status.setVisibility(View.VISIBLE);
                            pr_tolerance_status.setText("High");
                            mAlert = true;
                            pxHandler.sendEmptyMessage(Constants.ALRM_TONE);
                            pr_tolerance_status.setTextColor(Utility.getColorWrapper(HomeScreenActivity.this, R.color.button_text_color));
                            heart_rate.setTextColor(Utility.getColorWrapper(HomeScreenActivity.this, R.color.button_text_color));
                        } else if (oxygen < Integer.parseInt(pr_low_limit)) {
                            pr_tolerance_status.setVisibility(View.VISIBLE);
                            pr_tolerance_status.setText("Low");
                            mAlert = true;
                            pxHandler.sendEmptyMessage(Constants.ALRM_TONE);
                            pr_tolerance_status.setTextColor(Utility.getColorWrapper(HomeScreenActivity.this, R.color.button_text_color));
                            heart_rate.setTextColor(Utility.getColorWrapper(HomeScreenActivity.this, R.color.button_text_color));
                        } else {
                            mAlert = false;
                            pr_tolerance_status.setVisibility(View.GONE);
                            heart_rate.setTextColor(Utility.getColorWrapper(HomeScreenActivity.this, R.color.white));
                        }
                        heart_rate.setText("" + pulse);

                    }

                    if (pi == 0) {
                        perfusion_index.setText("--");
                        pi_toloerance_status.setVisibility(View.GONE);
                        perfusion_index.setTextColor(Utility.getColorWrapper(HomeScreenActivity.this, R.color.white));
                    } else {
                        if (mstart_recording) {
                            Logger.log(Level.INFO, TAG, "received perfusion index via BlE=" + pi);
                            pi_vector.add(String.valueOf((double) pi * 10 / 100));
                        }

                        if ((double) pi / 10 > Double.parseDouble(pi_high_limit)) {
                            pi_toloerance_status.setVisibility(View.VISIBLE);
                            pi_toloerance_status.setText("High");
                            mAlert = true;
                            pxHandler.sendEmptyMessage(Constants.ALRM_TONE);
                            pi_toloerance_status.setTextColor(Utility.getColorWrapper(HomeScreenActivity.this, R.color.button_text_color));
                            perfusion_index.setTextColor(Utility.getColorWrapper(HomeScreenActivity.this, R.color.button_text_color));
                        } else if ((double) pi / 10 < Double.parseDouble(pi_low_limit)) {
                            pi_toloerance_status.setVisibility(View.VISIBLE);
                            pi_toloerance_status.setText("Low");
                            mAlert = true;
                            pxHandler.sendEmptyMessage(Constants.ALRM_TONE);
                            pi_toloerance_status.setTextColor(Utility.getColorWrapper(HomeScreenActivity.this, R.color.button_text_color));
                            perfusion_index.setTextColor(Utility.getColorWrapper(HomeScreenActivity.this, R.color.button_text_color));
                        } else {
                            mAlert = false;
                            pi_toloerance_status.setVisibility(View.GONE);
                            perfusion_index.setTextColor(Utility.getColorWrapper(HomeScreenActivity.this, R.color.white));
                        }
                        perfusion_index.setText("" + (double) pi * 10 / 100);
                    }


                } else if (data[0] == -126) {
                    //  int high_spo2=(byte)data[1];
                    //  Logger.log(Level.INFO,TAG,"Get spo2 high="+high_spo2);
                    // txtcount.setText(""+high_spo2);

                }


            } else if (BLEService.ACTION_GATT_DISCONNECTED.equals(action)) {
                device_macid = "";
                mPlethysysmograph = false;


                if(recording_TAG.equalsIgnoreCase("STOP")|| recording_TAG=="STOP")
                {
                    btn_start_recording.setText(R.string.start_rec);
                    recording_TAG="START";

                }



                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                Logger.log(Level.INFO, TAG, "****The device is Disconnected*****");

                if (mstart_recording) {
                    stop_recording();
                }

                img_bluetooth.setAlpha(0.2f);
                Logger.log(Level.INFO, TAG, "c_count==" + c_count);

                if (mEqulaiser) {

                    mEqulaiser = false;
                    vprogressbar.setProgress(0);
                }

                spo2.setText("--");
                heart_rate.setText("--");
                perfusion_index.setText("--");
                if (c_count == 0) {

                    show_dialog(HomeScreenActivity.this);

                    c_Val_spo2 = false;
                    vprogressbar.setProgress(0);
                    spo2.setTextColor(Utility.getColorWrapper(HomeScreenActivity.this, R.color.white));

                /*
                    if(spo2_vector!=null && pr_vector!=null && pi_vector!=null)
                    {
                        Logger.log(Level.DEBUG,TAG,"SPo2 vector="+spo2_vector+"spo2 vector size="+spo2_vector.size());
                        Logger.log(Level.DEBUG,TAG,"PR vector="+pr_vector+"pr_vector size="+pr_vector.size());
                        Logger.log(Level.DEBUG,TAG,"PI vector="+pi_vector+"pi vector size="+pi_vector.size());

                        Logger.log(Level.DEBUG,TAG,"Plethysymograph vector="+mPlethysymograph_vector+
                                "pi vector size="+mPlethysymograph_vector.size());

                        if(spo2_vector.size()>40) {

                            // get spo2 vector
                            for (int x = 0; x < spo2_vector.size(); x++) {
                                if (x == spo2_vector.size() - 1)
                                    spo2_str = spo2_str + spo2_vector.get(x);
                                else
                                    spo2_str = spo2_str + spo2_vector.get(x) + ",";


                            }

                            // get PR vector
                            for (int x = 0; x < pr_vector.size(); x++) {
                                if (x == pr_vector.size() - 1)
                                    pr_str = pr_str + pr_vector.get(x);
                                else
                                    pr_str = pr_str + pr_vector.get(x) + ",";


                            }


                            // get PI vector

                            for (int x = 0; x < pi_vector.size(); x++) {
                                if (x == pi_vector.size() - 1)
                                    pi_str = pi_str + pi_vector.get(x);
                                else
                                    pi_str = pi_str + pi_vector.get(x) + ",";

                            }

                            if (globalVariable.getUsername() != null) {
                                mDatabase = DatabaseManager.getInstance().openDatabase();
                                mDatabase.insert(BplOximterdbHelper.TABLE_NAME_RECORDS, null,
                                        insert_pulsemeter_readings(globalVariable.getUsername(), spo2_str, pr_str, pi_str, DateTime.getDateTime(),String.valueOf(spo2_vector.size())));
                                Logger.log(Level.DEBUG, TAG, "New Records are inserted into the database");

                            }


                        }


                    }//*/
                }


                c_count++;
                Logger.log(Level.DEBUG, TAG, " c_count value changed=" + " " + c_count);


            } else if (BLEService.ACTION_GATT_CONNECTED.equals(action)) {
                //  start_time = System.currentTimeMillis();
                pDialog.setMessage("Receiving the data...");
                Logger.log(Level.INFO, TAG, "**PDialog is dismissed***");
                pDialog.dismiss();
                Logger.log(Level.INFO, TAG, "****The device is connected****");
                mPlethysysmograph = true;
                img_bluetooth.setAlpha(1f);


            }
        }
    };

    //  setCharacteristicNotification to obtain data
    @SuppressLint("NewApi")
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null)
            return;
        for (BluetoothGattService gattService : gattServices) {
            String uuid = gattService.getUuid().toString();
            List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
            if (uuid.equals("cdeacb80-5235-4c07-8846-93a37ee6b86d")) {
                for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                    String uuid1 = gattCharacteristic.getUuid().toString();
                    if (uuid1.equals("cdeacb81-5235-4c07-8846-93a37ee6b86d")) {
                        mBluetoothLeService.setCharacteristicNotification(gattCharacteristic, true);
                    }
                }
            }
        }
    }


    private class BleBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String stateExtra = BluetoothAdapter.EXTRA_STATE;
            int state = intent.getIntExtra(stateExtra, -1);
            switch (state) {
                case BluetoothAdapter.STATE_TURNING_ON:
                    Logger.log(Level.INFO, TAG, "Bluetooth is Turning ON....");
                    break;
                case BluetoothAdapter.STATE_ON:
                    Logger.log(Level.INFO, TAG, "local Bluetooth adapter is on, and ready for use..");
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    Logger.log(Level.INFO, TAG, "local Bluetooth  is turning of ..");
                    break;
                case BluetoothAdapter.STATE_OFF:
                    Logger.log(Level.INFO, TAG, "local Bluetooth  is already off ..");
                    break;

            }
        }
    }


    public void get_limits_value() {
        SharedPreferences prefs;
        prefs = HomeScreenActivity.this.getSharedPreferences(Utility.LIMIT_KEY, Context.MODE_PRIVATE);


        spo2_limit.setText(prefs.getString(Constants.SPO2_LOW, "85")
                + "/" + prefs.getString(Constants.SPO2_HIGH, "100"));


        spo2_low_limit = prefs.getString(Constants.SPO2_LOW, "85");
        spo2_high_limit = prefs.getString(Constants.SPO2_HIGH, "100");


        pulserate_limit.setText(prefs.getString(Constants.HEART_RATE_LOW, "50") + "/" +
                prefs.getString(Constants.HEART_RATE_HIGH, "150"));

        pr_low_limit = prefs.getString(Constants.HEART_RATE_LOW, "50");
        pr_high_limit = prefs.getString(Constants.HEART_RATE_HIGH, "150");

        pi_limit.setText(prefs.getString(Constants.PI_LOW, "0") + "/" +
                prefs.getString(Constants.PI_HIGH, "20"));

        pi_low_limit = prefs.getString(Constants.PI_LOW, "0");
        pi_high_limit = prefs.getString(Constants.PI_HIGH, "20");

        Logger.log(Level.DEBUG, TAG, "get value stored in a shared preference s file *" + prefs);

    }


    private void stopMediaPlayer() {
        if (mPlayer != null) {
            mPlayer.stop();
            // mPlayer.release();
            // mPlayer=null;
        }
    }


    private void startMediaPlayer() {
        if (mPlayer != null) {
            //  mPlayer=MediaPlayer.create(HomeScreenActivity.this,R.raw.alert);
            mPlayer.start();
        }
    }

    MediaPlayer.OnPreparedListener plistner = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {

            mp.start();
        }
    };


    @Override
    public void onBackPressed() {

        cancel_spo2(HomeScreenActivity.this);


    }


    public ContentValues insert_pulsemeter_readings(String admin_email, String user,String spo2, String pr, String pi, String testing_time, String duration,String mac_id) {
        ContentValues values = new ContentValues();


        values.put(BplOximterdbHelper.USER_NAME, admin_email);
        values.put(BplOximterdbHelper.USER_NAME_, user);
        values.put(BplOximterdbHelper.SPO2, spo2);
        values.put(BplOximterdbHelper.PULSE_RATE, pr);
        values.put(BplOximterdbHelper.PERFUSION_INDEX, pi);
        values.put(BplOximterdbHelper.TESTING_TIME, testing_time);
        values.put(BplOximterdbHelper.DURATION, duration);
        values.put(BplOximterdbHelper.DEVICE_MACID,mac_id);
        return values;


    }



    public void play_sound() {
        Logger.log(Level.DEBUG, TAG, "mSoundpool called+++");
        mSoundpool.play(mSound_id, 1, 1, 0, 0, 1);
    }


    public void stop_sound() {

        mSoundpool.stop(mSound_id);
    }


    /**
     * The Class MyHandler.
     */


    private void release_soundpool() {
        if (mSoundpool != null) {
            mSoundpool.release();
        }
    }


    /*private void show_Ble_dialog_frag()
    {
        FragmentManager fm = getSupportFragmentManager();
        BLEDialogFragment dialogFragment = new BLEDialogFragment();
        dialogFragment.show(fm, "BLE Dialog");
    }*/


   /* public void calling_handler() {
        mHandler.postDelayed(new Runnable() {

            @Override
            public synchronized void run() {

               if (counter <= 100) {
                    mHandler.postDelayed(this, 1000L);
                    horizontal_progressbar.setProgress(counter);
                    counter += 5;
                } else {
                    mHandler.removeCallbacks(this);
                    mStoptimer = true;
                    textdispaly();
                }



            }


        },0);




    }
*/


    private void show_dialog(final Context context) {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.connect_device)
                .setPositiveButton(R.string.tap_connect, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(context, BLEdevicelistActivity.class);
                        if (isBleseviceRegister) {
                            unregisterReceiver(mGattUpdateReceiver);
                            isBleseviceRegister = false;
                        }
                        if (isBindServise) {
                            unbindService(mServiceConnection);
                            isBindServise = false;
                        }


                        alert.dismiss();
                        startActivityForResult(intent, Constants.BLUETOOTH_REQUEST_CODE);
                    }
                });


        alert = builder.create();
        //  alert.setCancelable(false);
        alert.setCanceledOnTouchOutside(false);
        Logger.log(Level.DEBUG, TAG, "Alert dialog box gets called");
        alert.show();

    }


    @Override
    protected void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.

        Logger.log(Level.DEBUG, TAG, "**On Start() gets called**");
        get_limits_value();


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

    }

    @Override
    protected void onResume() {
        super.onResume();
        globalVariable.activityResumed();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlethysysmograph) {
            mPlethysysmograph = false;
        }

        if (mSpaeaker) {
            mSpaeaker = false;
        }

        if (mStoptimer) {
            mStoptimer = false;
        }
        if (mEqulaiser) {
            mEqulaiser = false;
        }
        Logger.log(Level.DEBUG, TAG, "On destoy called");
        release_soundpool();
        if (isBleseviceRegister) {
            unregisterReceiver(mGattUpdateReceiver);
        }
        if (isBindServise) {
            this.unbindService(mServiceConnection);
        }

        if (mstart_recording) {
            stop_recording();
        }


        unregisterReceiver(myBleRecever);


    }


    public void plot_graph() {
        mSeries2 = new LineGraphSeries<DataPoint>();

        mSeries2.setColor(Color.WHITE);
        mSeries2.setThickness(2);
        //graph2.setTitle("Spo2 chart");
        grapView.addSeries(mSeries2);
        grapView.getViewport().setXAxisBoundsManual(true);


        mSeries2.setDrawBackground(true);
        // mSeries2.setBackgroundColor(R.color.white);

        //  grapView.getViewport().setBackgroundColor(hite));
        grapView.getViewport().setMinX(0);
        grapView.getViewport().setMaxX(350);


        grapView.getViewport().setYAxisBoundsManual(true);
        grapView.getViewport().setMinY(0);  // set the min value of the viewport of y axis
        grapView.getViewport().setMaxY(100);    // set the max value of the viewport of y-axis

        grapView.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        grapView.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        grapView.getGridLabelRenderer().setVerticalLabelsVisible(false);
        //  graph2.getGridLabelRenderer().setNumHorizontalLabels(300);
        //grapView.getGridLabelRenderer().setNumVerticalLabels(2);
        grapView.getViewport().setScrollable(true);

    }


    final Handler plethy_Handler = new Handler(Looper.getMainLooper());
    final MyHandler pxHandler = new MyHandler(HomeScreenActivity.this);
    List<DataPoint> mDatapointlist = new ArrayList<DataPoint>();

    int count_reset = 0;
    DataPoint[] myArr = new DataPoint[380];
    double Xview = 10;
    double xstep = 0.01d;

    class MyHandler extends Handler {

        /**
         * The m activity.
         */
        private final WeakReference<HomeScreenActivity> mActivity;

        /**
         * Constructor for MyHandler.
         */
        private MyHandler(HomeScreenActivity activity) {
            mActivity = new WeakReference<HomeScreenActivity>(activity);
        }

        /**
         * Method handleMessage.
         *
         * @param msg android.os.Message
         */
        public void handleMessage(Message msg) {
            final HomeScreenActivity parent = mActivity.get();
            Logger.log(Level.DEBUG, "HomeScreenActivity", "msg.what=" + msg.what);

            if (msg.what == Constants.TIMER_DATA) {

                mStoptimer = false;
                count_px = 0;
                horizontal_progressbar.setProgress(0);
                count1 = 5;
                count2 = 10;
                count3 = 15;
                count4 = 20;


            } else if (msg.what == Constants.TIMER_DATA_TEXT) {
                tx1.setText(DateTime.secondsTominutes(count1 * 1000));
                txt2.setText(DateTime.secondsTominutes(count2 * 1000));
                txt3.setText(DateTime.secondsTominutes(count3 * 1000));
                txt4.setText(DateTime.secondsTominutes(count4 * 1000));
            } else if (msg.what == Constants.PLETHY_1000) {

                String str = (String) msg.obj;

                final String[] arr = str.replace("[", "").replace("]", "").split(",");

                //Do something here


                synchronized (this) {
                    for (int i = 0; i < arr.length; i++) {
                        mSeries2.appendData(new DataPoint(graph2LastXValue, Double.parseDouble(arr[i])), true, 350);
                        graph2LastXValue++;


                    }
                }


            } else if (msg.what == Constants.ALRM_TONE) {

                if (mSpaeaker) {
                    play_sound();
                }
            } else if (msg.what == Constants.TIMER_FIRST_TWENTY_SEC) {


                tx1.setText(DateTime.secondsTominutes(count1 * 1000));
                txt2.setText(DateTime.secondsTominutes(count2 * 1000));
                txt3.setText(DateTime.secondsTominutes(count3 * 1000));
                txt4.setText(DateTime.secondsTominutes(count4 * 1000));


            } else if (msg.what == Constants.TIMER_AFTER_TWENTY_SEC) {
                tx1.setText(DateTime.secondsTominutes(count1 * 1000));
                txt2.setText(DateTime.secondsTominutes(count2 * 1000));
                txt3.setText(DateTime.secondsTominutes(count3 * 1000));
                txt4.setText(DateTime.secondsTominutes(count4 * 1000));
            } else if (msg.what == Constants.CONTINUE_PLOTTING) {

                mStoptimer = false;
                mEqulaiser = false;
                count_px = 0;
                vprogressbar.setProgress(0);
                horizontal_progressbar.setProgress(0);
                count1 = 5;
                count2 = 10;
                count3 = 15;
                count4 = 20;


                AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreenActivity.this);
                builder.setMessage("Do you want to continue Recording?")
                        .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                alert.dismiss();
                                mBluetoothLeService.disconnect_BLE();
                                mBluetoothLeService.close_BLE();
                                finish();

                            }
                        });


                alert = builder.create();
                alert.setCanceledOnTouchOutside(false);
                Logger.log(Level.DEBUG, TAG, "Alert dialog box gets called");
                alert.show();
            }
        }

    }

    ;


    @Override
    protected void onPause() {
        super.onPause();
        Logger.log(Level.DEBUG, TAG, "**On pause() method gets called**");
        if(globalVariable!=null)
        {
            globalVariable.activityPaused();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Logger.log(Level.DEBUG, TAG, "**On Stop() method gets called**");


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Logger.log(Level.DEBUG, TAG, "**On Restart()**");
        if (device_macid == "") {
            if (!alert.isShowing()) {
                vprogressbar.setProgress(0);
                grapView.removeSeries(mSeries2);
                plot_graph();
                show_dialog(HomeScreenActivity.this);
            }

        }
    }


    private boolean start_recording() {
        Logger.log(Level.DEBUG, TAG, "**Recording started**");
        spo2_vector = new Vector<String>();
        pr_vector = new Vector<String>();
        pi_vector = new Vector<String>();
        mstart_recording = true;
        mStoptimer = true;
        start_timer();
        recording_time=DateTime.getDateTime();
        return mstart_recording;
    }


    private void stop_recording() {
        Logger.log(Level.DEBUG, TAG, "**Recording stopped**");
        mstart_recording = false;
        mStoptimer = false;

        pxHandler.sendEmptyMessage(Constants.TIMER_DATA);
        if (horizontal_progressbar.isIndeterminate()) {
            horizontal_progressbar.setProgress(0);
        }



        if (spo2_vector != null && pr_vector != null && pi_vector != null) {
            Logger.log(Level.DEBUG, TAG, "SPo2 vector=" + spo2_vector + "spo2 vector size=" + spo2_vector.size());
            Logger.log(Level.DEBUG, TAG, "PR vector=" + pr_vector + "pr_vector size=" + pr_vector.size());
            Logger.log(Level.DEBUG, TAG, "PI vector=" + pi_vector + "pi vector size=" + pi_vector.size());

            Logger.log(Level.DEBUG, TAG, "Plethysymograph vector=" + mPlethysymograph_vector +
                    "pi vector size=" + mPlethysymograph_vector.size());

            end_time = System.currentTimeMillis();
            difference = end_time - start_time;


            Logger.log(Level.INFO, TAG, "**Total time elapse for reading data in seconds**=" + difference / 1000 + "seconds");
            Logger.log(Level.INFO, TAG, "toatal no. of times Pulse Rate display=" + seconds_counter);


            if (spo2_vector.size() > 40) {

                // get spo2 vector
                for (int x = 0; x < spo2_vector.size(); x++) {
                    if (x == spo2_vector.size() - 1)
                        spo2_str = spo2_str + spo2_vector.get(x);
                    else
                        spo2_str = spo2_str + spo2_vector.get(x) + ",";


                }

                // get PR vector
                for (int x = 0; x < pr_vector.size(); x++) {
                    if (x == pr_vector.size() - 1)
                        pr_str = pr_str + pr_vector.get(x);
                    else
                        pr_str = pr_str + pr_vector.get(x) + ",";


                }


                // get PI vector

                for (int x = 0; x < pi_vector.size(); x++) {
                    if (x == pi_vector.size() - 1)
                        pi_str = pi_str + pi_vector.get(x);
                    else
                        pi_str = pi_str + pi_vector.get(x) + ",";

                }

                if (globalVariable.getUsername() != null) {
                    mDatabase = DatabaseManager.getInstance().openDatabase();
                    mDatabase.insert(BplOximterdbHelper.TABLE_NAME_RECORDS, null,
                            insert_pulsemeter_readings(globalVariable.getUsername(),mUserName, spo2_str, pr_str, pi_str,recording_time,String.valueOf(spo2_vector.size()),device_macid));
                    Logger.log(Level.DEBUG,TAG,"**Recording Time**="+recording_time);

                    Logger.log(Level.DEBUG, TAG, "New Records are inserted into the database");
                    Toast.makeText(HomeScreenActivity.this, R.string.rec_saved, Toast.LENGTH_LONG).show();

                    try{

                        mDatabase.insert(BplOximterdbHelper.TABLE_NAME_LAST_ACTIVITY_USERS,null,Utility.lastActivityUsers(globalVariable.getUsername(),
                                mUserName,Constants.DEVICE_PARAMETER_IOXY,DateTime.getCurrentDate()));

                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }


            }else{
                Toast.makeText(HomeScreenActivity.this,R.string.record_not_saved, Toast.LENGTH_LONG).show();
            }


            Logger.log(Level.DEBUG, TAG, "spo2 vector converted to string=" + " " + spo2_str);
            Logger.log(Level.DEBUG, TAG, "pr vector converted to string=" + " " + pr_str);
            Logger.log(Level.DEBUG, TAG, "pi vector converted to string=" + " " + pi_str);

            spo2_str = "";
            pr_str = "";
            pi_str = "";
        }

    }


    private void change_layout()
    {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int density= metrics.densityDpi;

        if(density<300 || density==300)
        {
            int value_in_pixels= (int) getResources().getDimension(R.dimen.relative_layout2_height);
            relativeLayout2.setMinimumHeight(value_in_pixels+120);
        }
    }


    private void cancel_spo2(Context context)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.disconnect_device)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        finish();
                        alert.dismiss();
                    }
                });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                alert.dismiss();
            }
        });


        alert = builder.create();
        alert.setCanceledOnTouchOutside(false);
        Logger.log(Level.DEBUG, TAG, "Alert dialog box gets called");
        alert.show();
    }

}
