package com.example.bluetoothlibrary.Impl;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.example.bluetoothlibrary.BluetoothLeClass;
import com.example.bluetoothlibrary.HomeUtil;
import com.example.bluetoothlibrary.Interface.Wbpdata;
import com.example.bluetoothlibrary.MyDateUtil;
import com.example.bluetoothlibrary.R;
import com.example.bluetoothlibrary.SettingUtil;
import com.example.bluetoothlibrary.StringUtill;
import com.example.bluetoothlibrary.entity.BleData;
import com.example.bluetoothlibrary.entity.SampleGattAttributes;
import com.example.bluetoothlibrary.entity.SycnBp;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.bluetoothlibrary.SettingUtil.isGusetmode;
import static com.example.bluetoothlibrary.StringUtill.hexString2binaryString;

/**
 * Created by laiyiwen on 2017/4/28.
 */

public class ResolveWbp implements Wbpdata {
    public static int WBPMODE = -1;

//    private ConnectBleServiceInfo connectServiceInfo;
    private List<Integer> bpDataList = new ArrayList<Integer>();
    private int currentCount = 0;
    private long beforeErrorCount = 0;
    private Timer timerResendData;
    private Timer realDataTimer;
    public static boolean iserror = false;
    private int stateCount = 0;
    private int i = 0;
    public static int DROPMEASURE = -1;
    public Object obStr = "0";
    public int mesurebp,endsys,enddia,endhr;
    public int battey,user_mode;
    public String blestate;
    public String ver;
    public String time,devstate;

    public boolean guest;

    public ArrayList<SycnBp> bps=new ArrayList<>();
    public OnWBPDataListener onWBPDataListener;



    public interface OnWBPDataListener
    {
        void onMeasurementBp(int temp);
       void onMeasurementfin(int SYS, int DIA, int HR, Boolean isguestmode);
        void onErroer(Object obj);
        void onState(int btbattey, String bleState, String version, String devState);
        void onSycnBp(ArrayList<SycnBp> sycnBps);
        void onTime(String wbp_time);
        void onUser(int user);
    }


    @Override
    public void setOnWBPDataListener(OnWBPDataListener listener) {

        onWBPDataListener=listener;
    }
    @Override
    public synchronized void resolveBPData2(byte[] datas, final BluetoothLeClass mBLE, Activity activity) {
        try {
            for (int i = 0; i < datas.length; i++) {
                if (datas[i] < 0)
                    bpDataList.add(datas[i] + 256);
                else
                    bpDataList.add((int) datas[i]);
            }
            int length = bpDataList.size();// ????????
            String tstr = "";
            for (int j = 0; j < length; j++) {
                tstr = tstr + "," + bpDataList.get(j) + "";
                if (bpDataList.get(j) == 170 && j < length - 1 && bpDataList.get(j + 1) != 170) {
                    int n = bpDataList.get(j + 1);
                    int sum = n;

                    if (j + 1 + n > length) {

                        break;
                    }

                    for (int k = 0; k < n - 1; k++) {
                        // ???????????????????????
                        if (j + 1 + n > length) {
                            break;
                        }

                        if (bpDataList.get(j + 1 + k + 1) == 170) {
                            if (bpDataList.get(j + 1 + k + 2) == 170) {
                                k++;
                                n++;
                            } else {
                                // ??????----
                                Log.d("test", "exception,the data have no 0xAA");
                                // bpDataList.clear();
                                return;
                            }
                        }
                        int add = bpDataList.get(j + 1 + k + 1);

                        Log.d("muk", "received add" + add);
                        Log.d("muk", "received sum" + sum);
                        sum = sum + add;
                        Log.d("muk", "received sum2" + sum);
                    }

                    if (j + 1 + n > length) {
                        return;

                    }
                    List<Integer> bytesTwo = new ArrayList<Integer>();
                    for (int m = 0; m < n - 1; m++) {

                        bytesTwo.add(bpDataList.get(j + 1 + m + 1));
                        if (bpDataList.get(j + 1 + m + 1) == 170 && bpDataList.get(j + 1 + m + 2) == 170) {
                            m++;
                        }
                    }
                    int pID = bytesTwo.get(0);
                    if (pID == 43) {
                        Log.d("nice",
                                "--------------------now is ,synchronization-----------------------");
                        if (SettingUtil.resendData == true) {
                            Log.d("nice",
                                    "--------------------synchronization-----resenddata--------------");
                            SettingUtil.isHaveData = true;
                        } else {
                            currentCount++;
                        }

                    }
                    // ???У??????
                    int checksum = bpDataList.get(j + 1 + n); // ?????n??????????0xAA????????
                    // У??
                    if (checksum != sum % 256 && checksum + sum % 256 != 256) {
                        // У????????????
                        Log.d("muk", "check failed??data exception?");
                        if (pID == 43) {
                            Log.d("nice",
                                    "--------------------now is ,synchronization----------------------");
                            if (SettingUtil.resendData == true) {// ???????г?????????????????
                                // ????????????
                                finishToResendData(mBLE);
                                return;
                            } else {

                                Log.d("nice",
                                        "--------------------now is ,synchronization..add--------------------------");
                                beforeErrorCount++;
                                BleData mydata = new BleData();
                                mydata.setByte1(bytesTwo.get(1));
                                mydata.setByte2(bytesTwo.get(2));
                                mydata.setByte3(bytesTwo.get(3));
                                mydata.setByte4(bytesTwo.get(4));
                                SampleGattAttributes.listdata.add(mydata);
                            }

                        }
                        continue;
                    }

                    Log.d("muk", "received pid????????????????????" + pID);
                    Log.d("muk", bytesTwo.size()
                            + "-<------------------------first-----pID===============????" + pID);
                    switch (pID) {
                        case 40: // ??????????
//                        if (config.getBPMeasurementFragment() != null) {
//                            MyHandlerUtil.sendMsg(Macro.MACRO_CODE_302, config.getBPMeasurementFragment(),
//                                    null);
//                        }
                            String byte3 = byteToBit(bytesTwo.get(3));
                            Log.d("muk", "length-----------------" + bytesTwo.size());
                            String mode = byte3.charAt(6) + "" + byte3.charAt(7);
                            if (mode.equals("01")) {// ??????????
                                Log.d("mye", "is 01");
                                return;
                            } else if (mode.equals("00")) {
                                byte tempCuff = (byte) (BitToByte(byte3.charAt(0) + "") + bytesTwo.get(1));

                                int tempL = tempCuff;
                                if (tempL < 0) {
                                    tempL += 256;
                                }
                                Log.d("mye", "?? 00");
                                Log.d("mye", "Cuff pressure??????-----------------------=" + tempL);
//                            if (BTState != 1) {
//                                BTState = 5;
//                            }
                                //??????????????
                                mesurebp=tempL;

//                                if (SettingUtil.bleState.equals("1")) {
//                                    SettingUtil.isNowTestBp = false;
//                                    SettingUtil.isNowTestBpFinish = false;
//                                if (config.getBPMeasurementFragment() != null) {
//                                    MyHandlerUtil.sendMsg(Macro.MACRO_CODE_3,
//                                            config.getBPMeasurementFragment(), tempL);
//                                }
//                                }

                            }

                            SettingUtil.isSyncFinish = true;
                            break;
                        case 41: // ???????
                            Log.d("muk", "PID is-----------------41");
                            String byteResult = byteToBit(bytesTwo.get(1));
                            SettingUtil.isNowTestBpFinish = true;
                            Log.d("muk", "the test result--------------------------byteResult------????" + byteResult);
                            String tempR = byteResult.charAt(3) + "";
                            Log.d("muk", "the test result--------------------------tempR------????" + tempR);
                            if (tempR.equals("0")) {
                                Log.d("muk", "the test result-----------------normal---------------");
                                iserror = false;
                                int SYS = bytesTwo.get(3);
                                if (SYS < 0) {
                                    SYS += 256;
                                }
                                int dp = bytesTwo.get(4);
                                int hr = bytesTwo.get(6);
                                if (dp < 0) {
                                    dp += 256;
                                }
                                if (hr < 0) {
                                    hr += 256;
                                }
//
                                endsys=SYS;
                                enddia=dp;
                                endhr=hr;
                                guest=isGusetmode;
                                int three = bytesTwo.get(7) < 0 ? bytesTwo.get(7) + 256
                                        : bytesTwo.get(7);
                                int four = bytesTwo.get(8) < 0 ? bytesTwo.get(8) + 256
                                        : bytesTwo.get(8);
                                int five = bytesTwo.get(9) < 0 ? bytesTwo.get(9) + 256
                                        : bytesTwo.get(9);
                                int six = bytesTwo.get(10) < 0 ? bytesTwo.get(10) + 256
                                        : bytesTwo.get(10);
                                /**
                                 * ???????????????
                                 */
                                int[] times={three,four,five,six};
                                time= HomeUtil.BuleToTime(times);
                                Log.d("testing time",""+time);



                                Log.d("El PoLLA", "the result is ----------------SYSlow 8 bit=" + SYS);
                                Log.d("El PoLLA", "the result is -----------------DIA=" + dp);
                                Log.d("El PoLLA", "the result is -----------------PR=" + hr);
//                            Log.d("muk", "?????????---------MainActivity--------=" + MainActivity.isGusetmode);
                                Log.d("El PoLLA", "the result is ------------SettingUtil-----=" + isGusetmode);
                                //???...??? ...????
                                if (SYS == 0 || dp == 0 || hr == 0) {
                                if (SYS == 0) {
                                    obStr = activity.getApplication().getString(R.string.ble_test_error15);
                                } else if (dp == 0) {
                                    obStr = activity.getApplication().getString(R.string.ble_test_error16);
                                } else if (hr == 0) {
                                    obStr = activity.getApplication().getString(R.string.ble_test_error17);
                                }
                                    iserror = true;
                                } else {
                                obStr = "0";
                                }

                            } else {
                                Log.d("muk",
                                        "test result is -----------------wrong----bytesTwo.get(2)-----------------------"
                                                + bytesTwo.get(2));
                                iserror = true;
                                /**
                                 * ?????б?
                                 */
                            obStr = getErrorTip(bytesTwo,activity);
//
                            }
                            Log.d("mgf", "----------------bytesTwo------------------------" + bytesTwo.toString());

//                        if (BTState != 1) {
//                            BTState = 2;
//                        }
//                            byte[] t_data1 = { (byte) 0xAA, 0x04, 0x03, 41, 0x00, 0x00 };
//                            mBLE.writeCharacteristic_wbp(t_data1);
//                        if (config.getBPMeasurementFragment() != null) {
//                            MyHandlerUtil.sendMsg(Macro.MACRO_CODE_0, config.getBPMeasurementFragment(),
//                                    obStr);

//                        }

                            break;

                        case 42:// ??????(ID:42)
                            Log.d("muk", "PID??-------??????the wbp state????????????????????????????????????????????----------42");

                            int BTBatteryCopy = bytesTwo.get(1);
                            Log.d("WT", ".BTBatteryCopy...=" + BTBatteryCopy);
                            if (BTBatteryCopy < 0) {
                                BTBatteryCopy = BTBatteryCopy + 256;
                                SettingUtil.BTBattery = (bytesTwo.get(1) + 256) % 16;
                            } else {
                                SettingUtil.BTBattery = bytesTwo.get(1) % 16;
                            }
                            Log.d("muk", "BTBattery:::" + SettingUtil.BTBattery);
                            String tempB = setValue(Integer.toBinaryString(BTBatteryCopy));
                            Log.d("muk", "byte(1):::" + tempB);
                            String temp1 = tempB.charAt(3) + "";
                            SettingUtil.bleState = temp1;
                            String temp2 = tempB.charAt(5) + "" + tempB.charAt(6) + "" + tempB.charAt(7) + "";
                            String temp3 =tempB.charAt(2)+""+tempB.charAt(1)+""+tempB.charAt(0);
                            Log.d("lgc", "the state of the device:::" + temp1);
                            Log.d("lgc", "the work mode of the device:::" + Integer.valueOf(temp2, 2));
                            if (temp1.equals("1")) {
                                SettingUtil.isSyncFinish = false;
                            } else {
                                SettingUtil.isSyncFinish = true;
                                iserror = false;
                            }
                            Log.d("lgc", "isSyncFinish:::" + SettingUtil.isSyncFinish);
                            int version = bytesTwo.get(2);
                            SettingUtil.tempVersion = Integer.toString((version / 16)) + "."
                                    + Integer.toString((version % 16));
                            int three = bytesTwo.get(4) < 0 ? bytesTwo.get(4) + 256
                                    : bytesTwo.get(4);
                            int four = bytesTwo.get(5) < 0 ? bytesTwo.get(5) + 256
                                    : bytesTwo.get(5);
                            int five = bytesTwo.get(6) < 0 ? bytesTwo.get(6) + 256
                                    : bytesTwo.get(6);
                            int six = bytesTwo.get(7) < 0 ? bytesTwo.get(7) + 256
                                    : bytesTwo.get(7);
                            /**
                             * ???????????????
                             */
                            int[] times={three,four,five,six};
                             time= HomeUtil.BuleToTime(times);

                            /**
                             * ??????????????
                             */
//                        if (config.getMyFragmentHandler() != null) {
//                            MyHandlerUtil.sendMsg(Macro.MACRO_CODE_102, config.getMyFragmentHandler(), null);
//                        }
                            Log.d("WT", "BTBattery....." + SettingUtil.BTBattery);
                            Log.d("WT", "SettingUtil.tempVersion....." + SettingUtil.tempVersion);

                            byte[] t_data4 = { (byte) 0xAA, 0x04, 0x03, 42, 0x00, 0x00 };// ???
                            mBLE.writeCharacteristic_wbp(t_data4);
                            if (SettingUtil.isfirstLoad5 == 0) {
                                Log.d("lgc", "..1...");
                                /**
                                 * ?·?????????У??
                                 */
                                Timer timer = new Timer();
                                timer.schedule(new TimerTask() {

                                    @Override
                                    public void run() {
                                        getNowDateTime(mBLE);
                                    }
                                }, 800);
                            }
                            SettingUtil.isfirstLoad5++;
                            devstate=temp3;
                            battey= SettingUtil.BTBattery;
                            blestate=temp1;
                            ver=SettingUtil.tempVersion;


                            break;
                        case 43:// ??????????(ID:43)
                            Log.d("muk", "PID??-------sync??????????????????????????????????????????????????----------43");
                            Log.d("muk", stateCount
                                    + "+++++stateCount------------------------currentCount++++"
                                    + currentCount);

                            String byteData = byteToBit(bytesTwo.get(5));

                            String tempError = byteData.charAt(3) + "";
                            Log.d("muk", "sync--------------------------tempError------????" + tempError);
                            Log.d("muk", "bytesTwo.get(5)--------------" + bytesTwo.get(5));
                            // if (tempError.equals("0")) {
                            Log.d("muk", "great---------------");
                            iserror = false;
                            int SYS = bytesTwo.get(6);//

                            int dp = bytesTwo.get(7);//

                            int hr = bytesTwo.get(9);//
                            if (SYS < 0) {
                                SYS += 256;
                            }
                            if (dp < 0) {
                                dp += 256;
                            }
                            if (hr < 0) {
                                hr += 256;
                            }

                            String MeasureTime = getBleTestTime(bytesTwo.get(10), bytesTwo.get(11),
                                    bytesTwo.get(12), bytesTwo.get(13));
                             SycnBp sycnBp=new SycnBp();
                            sycnBp.setDia(dp);
                            sycnBp.setSys(SYS);
                            sycnBp.setHr(hr);
                            sycnBp.setTime(MeasureTime);

                            Log.d("muk", "sync?????? the result of SYS-----------------SYSlow8=" + SYS);
                            Log.d("muk", "sync?????? -----------------DIA=" + dp);
                            Log.d("muk", "sync?????? the result of-----------------PR=" + hr);
                            Log.d("muk", "sync?????? the result of-----------------MeasureTime=" + MeasureTime);

                            bps.add(sycnBp);
                            String str1 = byteData.charAt(1) + "";
                            String str2 = byteData.charAt(2) + "";
                            String str3 = str1 + str2;
                            // 0x01?????A??
                            // 0x02?????B??
                            // 0x03?????????
                            int user = Integer.valueOf(str3, 2);
                            Log.d("muk", ",,,,,,,,,,,,,,,,,,,,,,user," + user);
                            int FamilyId = -1;

                            if (SYS != 0 && dp != 0 && hr != 0) {

                                if (isGusetmode) {
                                    SettingUtil.Sbp = SYS;
                                    SettingUtil.Dbp = bytesTwo.get(7);
                                    SettingUtil.Hr = bytesTwo.get(9);
                                } else {
                                    if (user == 1 || user == 2) {
//                                    if (user == 1) {
//                                        FamilyId = config.getFamilys().get(0).getFamilyId();
//                                    } else if (user == 2) {
//                                        FamilyId = config.getFamilys().get(1).getFamilyId();
//                                    }
                                        Log.d("muk",
                                                "--------------------------------------sync-----------userID------------------------------------"
                                                        + user);
                                        String date = MyDateUtil.getDateFormatToString(null);
//                                        Log.d("muk", "??? ???????   ???????----------testTime------==" + MeasureTime);
//                                        Log.d("muk", "??? ???????   ???????---------date-------==" + date);
//                                    RecordBP recordBP = new RecordBP();
//                                    recordBP.setCreatedDate(date);
//                                    recordBP.setRecordDate(MeasureTime);
//                                    recordBP.setFamilyId(FamilyId);
//                                    Log.d("muk",
//                                            "--------------------------------------???------??????ID-----------------------------------------"
//                                                    + recordBP.getFamilyId());
//                                    recordBP.setUpdatedDate(date);
//                                    recordBP.setIsDeleted(0);
//                                    recordBP.setSbp(SYS);// ?????
//                                    recordBP.setDbp(dp);// ?????
//                                    recordBP.setHr(hr);// ????
//                                    recordBP.setBPID(StringUtill.getTimeNameSql());
//                                    recordBPDAO.insert(recordBP);
//                                        Log.d("muk",
//                                                "--------------------------------------???-?????????----------------------------------------------");

                                    } else {
                                        SettingUtil.Sbp = SYS;
                                        SettingUtil.Dbp = dp;
                                        SettingUtil.Hr = hr;
                                        SettingUtil.guestBpTime = MeasureTime;
                                    }
                                }
                            }
                            if (stateCount == currentCount) {// ???????
                                Log.d("muk",
                                        "--------------------------------------finish sync-------nice------------------------------------beforeErrorCount++++"
                                                + beforeErrorCount);
                                if (beforeErrorCount != 0) {// ?????????????????
                                    Log.d("muk",
                                            "--------------------------------------???-?????????????????--------????-------------------------------------");
                                    if (SettingUtil.resendData) {// ?????????????????????
                                        if (SampleGattAttributes.listdata.size() > 0) {
                                            SampleGattAttributes.listdata.remove(0);
                                        }
                                        if (SampleGattAttributes.listdata.size() > 0) {
                                            Log.d("muk",
                                                    "--------------------------------------???-???????????????--------------????--------------------------------");
                                            reSendData(mBLE);
                                        } else {// ??д???,???????
                                            syncFinish(mBLE);
                                        }
                                    } else {// ??????
                                        reSendData(mBLE);
                                    }
                                } else {// ??д???,???????

                                    syncFinish(mBLE);
                                }
                            }
                            // }
                            break;
                        case 44:// ???????????????(ID:44)

                            Log.d("muk", "PID??------???????????????-----------44");
                            Log.d("muk", "PID??-----------	1------44=====" + bytesTwo.get(1));// ?????????
                            // bit0~7
                            Log.d("muk", "PID??----------2-------44=====" + bytesTwo.get(2));// ?????????
                            // Bit8~15
                            Log.d("muk", "PID??-----------	3------44=====" + bytesTwo.get(3));
                            Log.d("muk", "PID??----------4-------44=====" + bytesTwo.get(4));
                            int a1 = bytesTwo.get(3);
                            int a2 = bytesTwo.get(4);
                            stateCount = a1 + a2;
                            Log.d("stateCount","??????.......????"+stateCount);
                            String t1 = setValue(Integer.toBinaryString(a1));
                            String t2 = setValue(Integer.toBinaryString(a2));
                            Log.d("muk", "PID??--------------t1--??????=====" + t1);
                            Log.d("muk", "PID??---------------t2-??????=====" + t2);
                            Log.d("muk", "PID??---------------stateCount-??????== ????????????===" + stateCount);
                            byte[] t_data2 = { (byte) 0xAA, 0x04, 0x03, 44, 0x00, 0x00 };
                            mBLE.writeCharacteristic_wbp(t_data2);
                            break;
                        case 45:// ????????(ID:45)

                            Log.d("muk", "PID??--------????????---------45");
                            Log.d("muk", "PID??-----------	1------45=====" + bytesTwo.get(1));// ?????????1
                            // bit0~7
                            Log.d("muk", "PID??----------2-------45=====" + bytesTwo.get(2));// ?????????0
                            // Bit8~15
						/*
						 * ?ж????????????0
						 */
                            int Data_blockCount = bytesTwo.get(1) + bytesTwo.get(2);
                            Log.d("muk", "Data_blockCount---------::" + Data_blockCount);
                            Log.d("muk", "?????---------::" + stateCount);
                            if (Data_blockCount > 0) {
                                if (SettingUtil.bleState.equals("0")) {
                                    if (SettingUtil.isfirstLoad6 == 0) {
                                        Timer timer = new Timer();
                                        timer.schedule(new TimerTask() {

                                            @Override
                                            public void run() {
                                                Log.d("muk", "=====??--------????????????????");

                                                mBLE.writeCharacteristic_wbp(SampleGattAttributes.data4); // ????????????????(ID:122)
                                            }
                                        }, 600);
                                    }
                                    SettingUtil.isfirstLoad6++;
                                    mBLE.writeCharacteristic_wbp(SampleGattAttributes.data4);
                                    // // ????????????????(ID:122)
                                }

                            } else {
                                SettingUtil.isSyncFinish = true;
                                // if (MainActivity.Position == 0) {
                                Log.d("muk", "=====????????(ID:45)--------stateCount++++" + stateCount);
                                if (stateCount != 0) {
                                    if (SettingUtil.isfirstLoad10 == 0) {
                                        Log.d("muk", "=====????????(ID:45)--------??????++++-----------");
//                                    webAPI.SynchronousBPThread(config, handler, config, 0);
//                                    if (config.getBPMeasurementFragment() != null) {
//                                        MyHandlerUtil.sendMsg(Macro.MACRO_CODE_306,
//                                                config.getBPMeasurementFragment(), null);
//
//                                    }

                                    }
                                    SettingUtil.isfirstLoad10++;

                                } else {
                                    Log.d("muk", "=====????????(ID:45)--------????????++++----------");


//                                if (config.getBPMeasurementFragment() != null) {
//                                    MyHandlerUtil.sendMsg(Macro.MACRO_CODE_302,
//                                            config.getBPMeasurementFragment(), null);
//                                }
                                }

                                // }

                            }

                            break;
                        case 3:// ?????
                            int tempid = bytesTwo.get(1);
                            if (tempid < 0) {
                                tempid = tempid + 256;
                            }
                            Log.d("muk", "PID??-----------?????·???????	------46=====????" + tempid);
                            Log.d("muk", "-----------------??????????====????" + bytesTwo.get(2));
                            if (tempid == 128) {
                                Log.d("??????", tempid + "");
                                if (!isGusetmode) {
//                                if (config.getBPMeasurementFragment() != null) {
//                                    MyHandlerUtil.sendMsg(Macro.MACRO_CODE_305,
//                                            config.getBPMeasurementFragment(), null);
//                                }
                                    if (WBPMODE == 0) {
                                        Timer timer = new Timer();
                                        timer.schedule(new TimerTask() {

                                            @Override
                                            public void run() {
                                                getNowDateTime(mBLE);
                                            }
                                        }, 600);
                                    }
                                }

                            }
                            if (tempid == 107) {
                                Log.d("muk", "SettingUtil.isfirstLoad7-------" + SettingUtil.isfirstLoad7);
                                if (SettingUtil.isfirstLoad7 == 0) {
                                    Log.d("sha", "??????????????-------" + SettingUtil.bleState);
                                    if (!isGusetmode && SettingUtil.bleState.equals("0")) {
//                                        Timer timer = new Timer();
//                                        timer.schedule(new TimerTask() {
//
//                                            @Override
//                                            public void run() {
//
//                                                Log.d("muk", "syncDataByBle-------?????????-?????????-------");
//
//                                                mBLE.writeCharacteristic_wbp(SampleGattAttributes.data7);
//                                            }
//                                        }, 800);
                                    } else {
                                        // ??????
                                        Log.d("muk", "syncDataByBle-------????????    ?·????-------");
                                        byte[] data = { (byte) 0xAA, 0x04, (byte) 0x80, 0x03, 0x00, 0x00 };
                                        mBLE.writeCharacteristic_wbp(data);
                                    }
                                }
                                SettingUtil.isfirstLoad7++;

                            }
                            if (bytesTwo.get(2) == 85) {
                                Log.d("muk", "----------???????=====" + DROPMEASURE);
                                if (bytesTwo.get(1) == 120 && DROPMEASURE == 8) {
                                    iserror = true;
                                    // cencelTimer();
//                                if (config.getBPMeasurementFragment() != null) {
//                                    MyHandlerUtil.sendMsg(Macro.MACRO_CODE_0,
//                                            config.getBPMeasurementFragment(), "0");
//                                }
//                                if (BTState != 1) {
//                                    BTState = 2;
//                                }
                                }
                            } else if (bytesTwo.get(2) == 0) {
                                Log.d("muk", "----------??????=====");
                            } else {
                                Log.d("muk", "----------????????=====");
                            }

                            Log.d("muk", "-----------------end====----------------");
                            break;
                        case 49:// ??????(ID:49)????????App??????????????????????????л??????????????????????????????????????
                            /**
                             * ?? ?????????? ?? ????App????????????????????????App????????????? ??
                             * ??????App???????????????????л?????????????????App????????????? ?? ?????
                             * ?????????App????ID?0x03
                             * ???????????????????л?????????????????????????????????????
                             * ??????????3s????????????????????????????????
                             *
                             */

                            // Byte1 Bit2~0
                            // 0x01?????A??
                            // 0x02?????B??
                            // 0x03?????????
                            Log.d("muk", "PID??-----------------49");
                            Log.d("muk", "WBPMODE-----------------::" + WBPMODE);
                            Log.d("muk", "??????----------------::" + isGusetmode);
                            if (!isGusetmode) {
                                if (!SettingUtil.isTest || SettingUtil.isFirstopenBle) {
                                    SettingUtil.isFirstopenBle = false;
//                                if (config.getBPMeasurementFragment() != null) {
//                                    MyHandlerUtil.sendMsg(Macro.MACRO_CODE_401,
//                                            config.getBPMeasurementFragment(), bytesTwo.get(1));
//                                }
                                    user_mode=bytesTwo.get(1);
                                    Log.d("user_mode::",""+user_mode);
                                    byte[] t_data3 = { (byte) 0xAA, 0x04, 0x03, 49, 0x00, 0x00 };
                                    mBLE.writeCharacteristic_wbp(t_data3);
                                }
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
            //if(bpl_mesurebp[0])
            Log.d("shani", "??????????data????????????????????" + tstr);
            bpDataList.clear();
            onWBPDataListener.onMeasurementBp(mesurebp);
            onWBPDataListener.onMeasurementfin(endsys,enddia,endhr,guest);
            onWBPDataListener.onErroer(obStr);
            onWBPDataListener.onState(battey,blestate,ver,devstate);
            onWBPDataListener.onSycnBp(bps);
            onWBPDataListener.onTime(time);
            onWBPDataListener.onUser(user_mode);


        } catch (Exception e) {
            e.printStackTrace();
            bpDataList.clear();
            e.printStackTrace();
        }
    }


    public void SendForAll(BluetoothLeClass mBLE)
    {
        mBLE.writeCharacteristic_wbp(SampleGattAttributes.data7);

    }


    public static byte BitToByte(String byteStr) {
        int re, len;
        if (null == byteStr) {
            return 0;
        }
        len = byteStr.length();
        if (len != 4 && len != 8) {
            return 0;
        }
        if (len == 8) {// 8 bit????
            if (byteStr.charAt(0) == '0') {// ????
                re = Integer.parseInt(byteStr, 2);
            } else {// ????
                re = Integer.parseInt(byteStr, 2) - 256;
            }
        } else {// 4 bit????
            re = Integer.parseInt(byteStr, 2);
        }
        return (byte) re;
    }


    public static String byteToBit(int b) {
        return "" + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1) + (byte) ((b >> 5) & 0x1)
                + (byte) ((b >> 4) & 0x1) + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)
                + (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);
    }

    private String setValue(String data) {
        int len = data.length();
        String tempDate = "";
        Log.d("muk", len + "data...." + data);
        switch (len) {
            case 1:
                tempDate = "0000000" + data;
                break;
            case 2:
                tempDate = "000000" + data;
                break;
            case 3:
                tempDate = "00000" + data;
                break;

            case 4:
                tempDate = "0000" + data;
                break;

            case 5:
                tempDate = "000" + data;
                break;

            case 6:
                tempDate = "00" + data;
                break;
            case 7:
                tempDate = "0" + data;
                break;
            default:
                tempDate = data;
                break;
        }
        return tempDate;
    }


    private void finishToResendData(BluetoothLeClass mBLE) {
        SettingUtil.resendData = false;
        SettingUtil.isHaveData = false;
        currentCount = 0;
        beforeErrorCount = 0;
        if (timerResendData != null) {
            timerResendData.cancel();
            timerResendData = null;
        }
        Log.d("muk",
                "--------------------?????????---???????---???????-------------------------------------------------");
        byte[] data6 = { (byte) 0xAA, 0x04, (byte) 0x7F, 0x01, 0x00, 0x00 };
        mBLE.writeCharacteristic_wbp(data6);
//        if (config.getBPMeasurementFragment() != null) {
//            MyHandlerUtil.sendMsg(Macro.MACRO_CODE_307, config.getBPMeasurementFragment(), null);
//        }
    }

    /**
     * ??????????????
     * @param b1
     * @param b2
     * @param b3
     * @param b4
     * @return
     */
    private String getBleTestTime(int b1, int b2, int b3, int b4) {
        String strNowTime = "";
        try {
            int a1 = b1;
            int a2 = b2;
            int a3 = b3;
            int a4 = b4;
            if (a1 < 0) {
                a1 += 256;
            }
            if (a2 < 0) {
                a2 += 256;
            }
            if (a3 < 0) {
                a3 += 256;
            }
            if (a4 < 0) {
                a4 += 256;
            }
            String t1 = setValue(Integer.toBinaryString(a1));
            String t2 = setValue(Integer.toBinaryString(a2));
            String t3 = setValue(Integer.toBinaryString(a3));
            String t4 = setValue(Integer.toBinaryString(a4));
            String temp4 = t4 + t3 + t2 + t1;
            BigInteger src1 = new BigInteger(temp4, 2);// ????BigInteger????
            String millSecond = src1.toString();// ????10???????????
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = df.parse("2000-01-01 00:00:00");
            long datet = date.getTime();
            long time = Long.parseLong(millSecond);
            long tempt = time * 1000;
            long nowTime = tempt + datet;
            Date d = new Date(nowTime);
            strNowTime = df.format(d).toString();
            Log.d("muk", "now time??????" + df.format(d).toString());
        } catch (Exception e) {
            e.printStackTrace();
            strNowTime = MyDateUtil.getDateFormatToString(null);
        }

        return strNowTime;
    }

    /**
     * ?·???????????У??
     */

    public void getNowDateTime(BluetoothLeClass mBLE) {
        Log.d("lgc", "?·????2....");
        try {
            String nowdata = MyDateUtil.getDateFormatToString(null);// ??????
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = df.parse(nowdata);
            Date date = df.parse("2000-01-01 00:00:00");
            long l = now.getTime() - date.getTime();
            long day = l / (24 * 60 * 60 * 1000);
            long hour = (l / (60 * 60 * 1000) - day * 24);
            long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
            long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
            int alltime = (int) (day * 24 * 60 * 60 + hour * 60 * 60 + min * 60 + s);
            Log.d("muk", "..???????? ??alltime??..+" + alltime);
            String str = print(alltime);
            String str1 = "";
            String str2 = "";
            String str3 = "";
            String str4 = "";
            for (int i = 0; i < str.length(); i++) {
                if (i < 8) {
                    str1 += str.charAt(i) + "";
                } else if (i < 16 && i >= 8) {
                    str2 += str.charAt(i) + "";
                } else if (i < 24 && i >= 16) {
                    str3 += str.charAt(i) + "";
                } else {
                    str4 += str.charAt(i) + "";
                }
            }
            int a = Integer.valueOf(str4, 2);
            final byte temp4 = (byte) a;
            int b = Integer.valueOf(str3, 2);
            final byte temp3 = (byte) b;
            int c = Integer.valueOf(str2, 2);
            final byte temp2 = (byte) c;
            int d = Integer.valueOf(str1, 2);
            final byte temp1 = (byte) d;
            Log.d("muk", "..???????? ??tempB4??..+" + temp4 + "-" + temp3 + "-" + temp2 + "-" + temp1);
            byte[] data6 = { (byte) 0xAA, 0x08, (byte) 0x6B, 0x00, temp4, temp3, temp2, temp1, 0x00, 0x00 };
            mBLE.writeCharacteristic_wbp(data6);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ?????????
     */
    private void reSendData(final BluetoothLeClass mBLE) {
        i = 0;
        /**
         * ?????????
         */
        timerResendData = new Timer();
        timerResendData.schedule(new TimerTask() {

            @Override
            public void run() {
                if (i == 3 && !SettingUtil.isHaveData) {
                    finishToResendData(mBLE);
                    SettingUtil.isHaveData = false;
                    timerResendData.cancel();
                    timerResendData = null;
                } else {
                    SettingUtil.resendData = true;
                    mBLE.writeCharacteristic_wbp(SampleGattAttributes.resendBleData(
                            SampleGattAttributes.listdata.get(0).getByte1(), SampleGattAttributes.listdata
                                    .get(0).getByte2(), SampleGattAttributes.listdata.get(0).getByte3(),
                            SampleGattAttributes.listdata.get(0).getByte4()));
                }
                i++;

            }
        }, 0, 1000); // ???0ms????У?1000ms??????
    }


    /**
     * ?????? ???????????? 127 +00
     */
    private void syncFinish(final BluetoothLeClass mBLE) {
        Log.d("muk", "--------------------------------------???-??д???,???????-------------nice---------------------------------");
        SettingUtil.resendData = false;
        SettingUtil.isHaveData = false;
        currentCount = 0;
        beforeErrorCount = 0;
        // ????????????
        mBLE.writeCharacteristic_wbp(SampleGattAttributes.data6);
        /**
         * ???????????? ?????????
         */
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                Log.d("muk", "syncDataByBle-------?????????-?????????-------");
                mBLE.writeCharacteristic_wbp(SampleGattAttributes.data7);
            }
        }, 1000);
    }


    private String print(int i) {
        String str = "";
        for (int j = 31; j >= 0; j--) {
            if (((1 << j) & i) != 0) {
                str += "1";
            } else {
                str += "0";
            }
        }
        return str;

    }


    /**
     * ali ??????????
     * @param datas
     */
    @Override
    public synchronized void resolveALiBPData(byte[] datas,Context context) {

        Log.d("?????????", datas.length + "");
        if (datas.length > 0) {
            switch (datas.length) {
                case 1://????
                    int battery = byte2Int(datas[0]);
                    Log.d("?????", battery + "");
                    if (battery > 0 && battery <= 25) {
                        SettingUtil.BTBattery = 0;
                    }else if (battery > 25 && battery <= 50) {
                        SettingUtil.BTBattery = 1;
                    }else if (battery > 50 && battery <= 75) {
                        SettingUtil.BTBattery = 2;
                    }else {
                        SettingUtil.BTBattery = 4;
                    }
                    break;
                case 12://??????
                    resolveRealTimeData(datas);
                    break;
                case 19://?????????
                    if (realDataTimer != null) {
                        realDataTimer.cancel();
                        realDataTimer = null;
                    }
                    resolveBleFinalData(datas,context);
                    break;
                default:
                    break;
            }
        }


    }

    private int byte2Int(byte data) {
        return (int)data < 0 ? (int)data + 256 : (int)data;
    }


    private void resolveRealTimeData(byte[] datas) {
//        if (config.getBPMeasurementFragment() != null) {
//            MyHandlerUtil.sendMsg(Macro.MACRO_CODE_302, config.getBPMeasurementFragment(),
//                    null);
//        }
        int Pr_low = byte2Int(datas[7]);
        int Pr_high = byte2Int(datas[8]);
        int user = byte2Int(datas[9]);
        int PrValue = (Pr_high << 8) + Pr_low;
        byte[] status = {datas[11], datas[10]};//??????

        if (WBPMODE == 1 && !isGusetmode) {
            if (!SettingUtil.isTest || SettingUtil.isFirstopenBle) {
                SettingUtil.isFirstopenBle = false;
                Log.d("????????????", "" + user);
//                if (config.getBPMeasurementFragment() != null) {
//                    MyHandlerUtil.sendMsg(Macro.MACRO_CODE_401,
//                            config.getBPMeasurementFragment(), user);
//                }
            }
        }
        if (((datas[11] << 8) + datas[10]) != 0) {
            Log.d("????", "???????????");
            return;
        } else if (((datas[11] << 8) + datas[10]) == 0) {//???????
            int tempL_low = byte2Int(datas[1]);//???????
            int tempL_high = byte2Int(datas[2]);//???????
            int tempLValue = (tempL_high << 8) + tempL_low;
            Log.d("mye", "????----" + tempLValue + ", ????????????: " + user);
//            if (BTState != 1) {
//                BTState = 5;
//            }
            SettingUtil.isNowTestBp = false;
            SettingUtil.isNowTestBpFinish = false;
//            if (config.getBPMeasurementFragment() != null) {
//                MyHandlerUtil.sendMsg(Macro.MACRO_CODE_3,
//                        config.getBPMeasurementFragment(), tempLValue);
//            }
        }
        SettingUtil.isSyncFinish = true;

//        if (realDataTimer != null) {
//            realDataTimer.cancel();
//            realDataTimer = null;
//        }
        realDataTimer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Log.d("????????", "------");
                SettingUtil.isSyncFinish = true;
                iserror = false;
//                if (config.getBPMeasurementFragment() != null) {
//                    MyHandlerUtil.sendMsg(Macro.MACRO_CODE_10017, config.getBPMeasurementFragment(), "0");
//                }
            }
        };
        realDataTimer.schedule(task, 1000);
    }


    //????????????????????
    private void resolveBleFinalData(byte[] datas,Context context) {
        Log.d("muk", "PID??-----------------41");
        SettingUtil.isNowTestBpFinish = true;
        int Sys_Low = byte2Int(datas[1]);
        int Sys_high = byte2Int(datas[2]);
        int Dia_low = byte2Int(datas[3]);
        int Dia_high = byte2Int(datas[4]);
        int Mean_low = byte2Int(datas[5]);
        int Mean_high = byte2Int(datas[6]);
        int Year_low = byte2Int(datas[7]);
        int Year_high = byte2Int(datas[8]);
        int mon = byte2Int(datas[9]);
        int day = byte2Int(datas[10]);
        int Hour = byte2Int(datas[11]);
        int Min = byte2Int(datas[12]);
        int Sec = byte2Int(datas[13]);
        int Pr_low = byte2Int(datas[14]);
        int Pr_high = byte2Int(datas[15]);
        int user = byte2Int(datas[16]);
        int SysValue = (Sys_high << 8) + Sys_Low;
        int DiaValue = (Dia_high << 8) + Dia_low;
        int MeanValue = (Mean_high << 8) + Mean_low;
        int PrValue = (Pr_high << 8) + Pr_low;
        String dateStr = ((Year_high << 8) + Year_low) + "-" + (mon > 9 ? mon : "0" + mon) + "-" + (day > 9 ? day : "0" + day) + " " + (Hour > 9 ? Hour : "0" + Hour) + ":" + (Min > 9 ? Min : "0" + Min) + ":" + (Sec > 9 ? Sec : "0" + Sec);

        Log.d("???????:", "???: " + ", sys: " + SysValue + ", dia: " + DiaValue + ", mean: " + MeanValue + ", pr: " + PrValue + ", time: " + dateStr);

        byte[] status = {datas[18], datas[17]};//??????

        Log.d("??????", new StringBuffer(hexString2binaryString(StringUtill.bytesToHexString(status))).reverse() + "");
        iserror = false;


        if (((datas[18] << 8) + datas[17]) == 0) {//?????????
            if (SysValue == 0 || DiaValue == 0 || PrValue == 0) {
                iserror = true;
            } else {
                if (isGusetmode && !SettingUtil.isGusetmode) {
                    if (SettingUtil.testType == 0) {
                        String date = MyDateUtil.getDateFormatToString(null);
//                        RecordBP recordBP = new RecordBP();
//                        recordBP.setCreatedDate(dateStr);
//                        recordBP.setRecordDate(dateStr);
//                        recordBP.setFamilyId(config.getFamilys().get(user - 1).getFamilyId());
//                        recordBP.setUpdatedDate(dateStr);
//                        recordBP.setIsDeleted(0);
//                        recordBP.setSbp(SysValue);
//                        recordBP.setDbp(DiaValue);
//                        recordBP.setHr(PrValue);
//                        recordBP.setBPID(StringUtill.getTimeNameSql());
//                        recordBPDAO.insert(recordBP);
//                        webAPI.SynchronousBPThread(config, handler, config, 0);
                    }
                } else if (SettingUtil.isGusetmode ||isGusetmode) {
                    SettingUtil.Sbp = SysValue;
                    SettingUtil.Dbp = DiaValue;
                    SettingUtil.Hr = PrValue;
                }

            }
        }else {
            iserror = true;
            obStr = getALiErrorTip(status,context);
        }
        Log.d("mgf", "----------------???????r------------2------------" + obStr);
//        if (BTState != 1) {
//            BTState = 2;
//        }
//        if (config.getBPMeasurementFragment() != null) {
//            MyHandlerUtil.sendMsg(Macro.MACRO_CODE_0, config.getBPMeasurementFragment(), obStr);
//        }
    }

    /**
     * ????????????
     *
     * @param bytesTwo
     * @return
     */
    private Object getErrorTip(List<Integer> bytesTwo,Activity activity) {
        Object obStr = "0";
        if (bytesTwo.get(2) == 7 || bytesTwo.get(2) == 14) {
            obStr =activity.getApplication().getString(R.string.ble_test_error3);
        } else if (bytesTwo.get(2) == 6 || bytesTwo.get(2) == 20) {
            obStr = activity.getApplication().getString(R.string.ble_test_error2);
        } else if (bytesTwo.get(2) == 2 || bytesTwo.get(2) == 8 || bytesTwo.get(2) == 10 || bytesTwo.get(2) == 12 || bytesTwo.get(2) == 15) {
            obStr = activity.getApplication().getString(R.string.ble_test_error6);
        } else if (bytesTwo.get(2) == 11 || bytesTwo.get(2) == 13) {
            obStr =activity.getApplication().getString(R.string.ble_test_error7);
        } else if (bytesTwo.get(2) == 9 || bytesTwo.get(2) == 19) {
            obStr =activity.getApplication().getString(R.string.ble_test_error5);
        } else {
            obStr =activity.getApplication().getString(R.string.ble_test_error14);
        }
        return obStr;
    }

    private Object getALiErrorTip(byte[] status, Context context) {
        String errBinStr = hexString2binaryString(StringUtill.bytesToHexString(status));
        String binStr = new StringBuffer(errBinStr).reverse().toString();
        Object obStr = "0";
        if (binStr.substring(0, 1).equals("1")) {
            obStr = context.getString(R.string.ble_test_error7);
        } else if (binStr.substring(1, 2).equals("1")) {
            obStr =context.getString(R.string.ble_test_error2);
        } else if (binStr.substring(2, 3).equals("1")) {
            obStr = context.getString(R.string.ble_test_error18);
        } else if (binStr.substring(3, 5).equals("01")) {
            obStr =context.getString(R.string.ble_test_error19);
        } else if (binStr.substring(3, 5).equals("10")) {
            obStr = context.getString(R.string.ble_test_error20);
        } else {
            obStr = context.getString(R.string.ble_test_error14);
        }
        return obStr;
   }


    public void onSingleCommand(BluetoothLeClass mBLE) {
        // TODO Auto-generated method stub


        mBLE.writeCharacteristic_wbp(SampleGattAttributes.data);

    }

    @Override
    public void onStopBleCommand(BluetoothLeClass mBLE) {
        // TODO Auto-generated method stub

        mBLE.writeCharacteristic_wbp(SampleGattAttributes.data2);

    }

    @Override
    public void sendUserInfoToBle(BluetoothLeClass mBLE) {
        // TODO Auto-generated method stub
        if ( mBLE!= null) {
            Log.d("muk", "?·?????...???:::" + SettingUtil.userModeSelect);

            if (SettingUtil.userModeSelect == 1) {
                byte[] data = { (byte) 0xAA, 0x04, (byte) 0x80, 0x01, 0x00, 0x00 };
                 mBLE.writeCharacteristic_wbp(data);
            } else if (SettingUtil.userModeSelect == 2) {
                byte[] data = { (byte) 0xAA, 0x04, (byte) 0x80, 0x02, 0x00, 0x00 };
                mBLE.writeCharacteristic_wbp(data);
            }
        }

    }


}
