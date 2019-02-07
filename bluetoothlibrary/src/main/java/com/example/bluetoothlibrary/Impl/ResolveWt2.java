package com.example.bluetoothlibrary.Impl;

import android.app.Activity;
import android.util.Log;

import com.example.bluetoothlibrary.BluetoothLeClass;
import com.example.bluetoothlibrary.Config;

import com.example.bluetoothlibrary.HomeUtil;
import com.example.bluetoothlibrary.Interface.Wt2data;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by laiyiwen on 2017/4/28.
 */

public  class ResolveWt2 implements Wt2data {



    public static int TEMPSTATE = 0;
    private List<Byte> bytes = new ArrayList<Byte>();

    public static boolean isHadTrimmedValue = false;
    public static int BTBattery = 4;


    public static String tempVersion = "";


    public boolean isFirstStatusPacket = true;

    public int ResponseID = 0;

    public boolean isCheckTime = false;

    public int DataBlock = 0;

    public int WitchDateBlock = 0;

    public boolean IsFirstPack = true;
    public static boolean IsSyncIng = false;

    private String BackTime;

    public String time_sycn,ver;
    public int bettray;
    public Double unblanceTemp,balancetemp=0.0;

    private OnWt2DataListener onWt2DataListener ;
    public interface OnWt2DataListener{

        void setUnbalanceTemp(Double unbalanceTemp);
        void setBanlaceTemp(Double banlaceTemp, int btBattery);
        void setWt2ver(String wt2ver);
        void onsycnResult(String time);

    }
    public void setOnWt2DataListener(OnWt2DataListener l){
        onWt2DataListener = l;
    }





    @Override
    public void calculateData_WT2(byte[] datas, BluetoothLeClass mBLE, Activity activity) {


        if (datas != null) {
            for (int i = 0; i < datas.length; i++) {
                bytes.add(datas[i]);
            }
            int length = bytes.size();
            for (int j = 0; j < bytes.size(); j++) {

                if (bytes.get(j) == -86 && j < length - 1
                        && bytes.get(j + 1) != -86) {
                    int n = bytes.get(j + 1);

                    int sum = n;


                    if (j + 1 + n > length) {
                        break;
                    }

                    for (int k = 0; k < n - 1; k++) {
                        // ???????????????????????
                        if (j + 1 + n > length) {
                            break;
                        }

                        if (bytes.get(j + 1 + k + 1) == -86) {
                            if (bytes.get(j + 1 + k + 2) == -86) {
                                k++;
                                n++;
                            } else {

//                                Log.d("ll", "??????,?????д??????????0xAA");
                                // bytes.clear();
                                return;
                            }
                        }
                        int add = bytes.get(j + 1 + k + 1);
                        sum = sum + add;
                    }

                    if (j + 1 + n > length) {
                        break;
                    }

                    int checksum = 0;
                    try {
                        checksum = bytes.get(j + 1 + n);

                    } catch (Exception e) {

                    }

                    if (checksum != sum % 256) {

                        if ((checksum + 256) != sum % 256)
                            continue;
                    }

                    List<Byte> bytesTwo = new ArrayList<Byte>();
                    for (int m = 0; m < n - 1; m++) {
                        bytesTwo.add(bytes.get(j + 1 + m + 1));
                        if (bytes.get(j + 1 + m + 1) == -86
                                && bytes.get(j + 1 + m + 2) == -86) {
                            m++;
                        }
                    }
                    int pID = 0;
                    try {
                        pID = bytesTwo.get(0);
                    } catch (Exception e) {

                    }
                    switch (pID) {
                        case 1:
                            bytes.clear();
                            break;
                        case 2:
                            bytes.clear();
                            break;
                        case 3:

                            switch (ResponseID) {
                                case 0:

//                                    SendForAll(mBLE);
                                    isCheckTime = true;
                                    break;
                                case 1:

//                                    Log.d("test", " ?????????????????????::?????????");
                                    break;
                                case 2:

//                                    Log.d("test", "??????????????????????::?????????");
                                    break;
                                case 3:

//                                    Log.d("test", " ???????????????????????::?????????");
                                    break;
                                case 4:

//                                    Log.d("test", "?????????????????::?????????");
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case 17:
                            if ((byte) ((bytesTwo.get(1) >> 0) & 0x1)
                                    + (byte) ((bytesTwo.get(1) >> 1) & 0x1) == 0) {
                                TEMPSTATE = 0;
                                int tempH = bytesTwo.get(3);
                                int tempL = bytesTwo.get(2);
                                if (tempH < 0) {
                                    tempH += 256;
                                } else {
                                }
                                if (tempL < 0) {
                                    tempL += 256;
                                } else {
                                }
                                double temp = ((tempH * 256 + tempL) * 0.01);//????????
//                                Message msg = new Message();
//                                msg.obj = temp;
//                                Log.d("test", "bytesTwo.get(1)::" + bytesTwo.get(1));
//                                Log.d(TAG, "msg.obj::" + msg.obj);
                                if ((byte) ((bytesTwo.get(1) >> 2) & 0x1)
                                        + (byte) ((bytesTwo.get(1) >> 3) & 0x1) == 0) {
                                    Log.d("kk", "unbalance????");
//                                    msg.what = MACRO_CODE_6;
//                                    isHadTrimmedValue = false;
                                    unblanceTemp=temp;
//                                    onWt2DataListener.setUnbalanceTemp(temp);

                                } else if ((byte) ((bytesTwo.get(1) >> 2) & 0x1)
                                        + (byte) ((bytesTwo.get(1) >> 3) & 0x1) == 1) {
                                    Log.d("kk", "balance????");
//                                    if (!isHadTrimmedValue) {

//                                        msg.what = MACRO_CODE_7;
                                        balancetemp=temp;
//                                        onWt2DataListener.setBanlaceTemp(temp);
//                                        isHadTrimmedValue = true;
//                                    } else {

//                                        onWt2DataListener.setUnbalanceTemp(temp);;
//                                        msg.what = MACRO_CODE_6;
//                                    }
                                } else {

                                }
//                                config.getMyFragmentHandler().sendMessage(msg);
                                // config.sendhideTabmsg(msg);
                                // if( handler1!=null){
                                // handler1.sendMessage(msg);
                                // }
                                // } else if (bytesTwo.get(1) == 1) {
                            } else if ((byte) ((bytesTwo.get(1) >> 0) & 0x1)
                                    + (byte) ((bytesTwo.get(1) >> 1) & 0x1) == 1) {
                                TEMPSTATE = 1;
//                                Message msg = new Message();
//                                msg.what = MainActivity.SENDTEMPHIGHT;
//                                config.getFirstMainHandler().sendMessage(msg);
                                // } else if (bytesTwo.get(1) == 2) {
                            } else if ((byte) ((bytesTwo.get(1) >> 0) & 0x1)
                                    + (byte) ((bytesTwo.get(1) >> 1) & 0x1) == 2) {
                                TEMPSTATE = 2;
                            }
                            bytes.clear();
                            onWt2DataListener.setUnbalanceTemp(unblanceTemp);

                            break;
                        case 18:


                            if (isFirstStatusPacket) {
                                isFirstStatusPacket = false;
                                int three = bytesTwo.get(3) < 0 ? bytesTwo.get(3) + 256
                                        : bytesTwo.get(3);
                                int four = bytesTwo.get(4) < 0 ? bytesTwo.get(4) + 256
                                        : bytesTwo.get(4);
                                int five = bytesTwo.get(5) < 0 ? bytesTwo.get(5) + 256
                                        : bytesTwo.get(5);
                                int six = bytesTwo.get(6) < 0 ? bytesTwo.get(6) + 256
                                        : bytesTwo.get(6);
                                /**
                                 * ???????????????
                                 */
                                int[] times={three,four,five,six};
                               String time=HomeUtil.BuleToTime(times);
                                if (Math.abs(HomeUtil.DifferTime(six * 256 * 256
                                        * 256 + five * 256 * 256 + four * 256
                                        + three)) <= 60) {

                                    Log.d("buyong","no need to sync"+""+time);
                                    time_sycn=time;
//                                    onWt2DataListener.onsycnResult(time);
                                } else {
                                    SendForTime(mBLE);
                                }
                            }
                            int BTBatteryCopy = bytesTwo.get(1);
                            if (BTBatteryCopy < 0) {
                                BTBattery = (bytesTwo.get(1) + 256) % 16;
                            } else {
                                BTBattery = bytesTwo.get(1) % 16;
                            }
                            bettray=BTBattery;
//                            onWt2DataListener.setBTBattery(BTBattery);
                            Log.v("tkz", "in the bluetooth state the BTBattery ????" + BTBattery);
                            int version = bytesTwo.get(2);
                            tempVersion = Integer.toString((version / 16)) + "."
                                    + Integer.toString((version % 16));
                            ver=tempVersion;
//                            onWt2DataListener.setWt2ver(tempVersion);
                            bytes.clear();
                            break;
                    }
                    Log.d("time_sycn::",""+time_sycn+"bettray::"+bettray+"ver::"+ver+"unblanceTemp::"+unblanceTemp+"balancetemp::"+balancetemp);
                    onWt2DataListener.onsycnResult(time_sycn);
                    onWt2DataListener.setWt2ver(ver);
                    onWt2DataListener.setUnbalanceTemp(unblanceTemp);
                    onWt2DataListener.setBanlaceTemp(balancetemp,bettray);
//                    onWt2DataListener.setUnbalanceTemp(unblanceTemp);
                }
            }
        }


    }



    public void SendForTime(BluetoothLeClass mBLE) {
        Log.d("kk", "????????У??????????::");
        int[] nowTime = HomeUtil.getTimeByte();

        List<Byte> sendbytes = new ArrayList<Byte>();
        sendbytes.add((byte) 0xAA);//

        sendbytes.add((byte) 0x08);//

        sendbytes.add((byte) 0x6B);// beye0
        sendbytes.add((byte) 0x00);// beye1????

        sendbytes.add((byte) nowTime[0]);// beye2
        sendbytes.add((byte) nowTime[1]);// beye3
        sendbytes.add((byte) nowTime[2]);// beye4
        sendbytes.add((byte) nowTime[3]);// beye5

        sendbytes.add((byte) 0x00);// beye6

        int size = sendbytes.size();
        // crcУ??
        byte crc = 0x00;
        for (int i = 0; i < size; i++) {
            crc += sendbytes.get(i);
        }
        sendbytes.add(crc);
        if (mBLE != null) {
            mBLE.writeCharacteristic(HomeUtil
                    .CheckByte(sendbytes));
            ResponseID = 0;
            isCheckTime = false;

            MyTimeTask(mBLE);
        }
    }


    public void MyTimeTask(final BluetoothLeClass mBLE) {
        final Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (isCheckTime) {
                    timer.cancel();
                } else {
                    SendForTime(mBLE);
                }
            }
        };
        timer.schedule(task, 3000);
    }





}