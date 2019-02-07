package com.example.bluetoothlibrary.Impl;

import android.app.Activity;
import android.util.Log;

import com.example.bluetoothlibrary.BluetoothLeClass;
import com.example.bluetoothlibrary.Config;

import com.example.bluetoothlibrary.HomeUtil;
import com.example.bluetoothlibrary.Interface.Wt1data;
import com.example.bluetoothlibrary.MyDateUtil;
import com.example.bluetoothlibrary.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by laiyiwen on 2017/4/26.
 */



public class ResolveWt1 implements Wt1data {



    private List<Byte> bytes = new ArrayList<Byte>();

    public static int BTBattery = 4;// ???0-3???
    public static String tempVersion = ""; //?¶???
    String time;
    public static int TEMPSTATE = 0;
    public static final int MACRO_CODE_3 = 3;
    public static final int MACRO_CODE_4 = 4;
    public static final int MACRO_CODE_5 = 5;

    public boolean isFirstStatusPacket = true;

    public int ResponseID = 0;

    public boolean isCheckTime = false;

    public int DataBlock = 0;

    public int WitchDateBlock = 0;

    public int WitchPack = 0;
    public int total = 0,begin=0;
    public boolean IsFirstPack = true;
    public static boolean IsSyncIng = false;

    public static int firstIn=0;

    double temp;
    private String BackTime;

    private OnWt1DataListener onWt1DataListener ;
    public interface OnWt1DataListener {
        void setTemp(Double temp);
         void ontempState(int stateCode);
        void onBTBattery(String bTBattery);
        void onVersion(String version);
        void onTime(String time);
        void ontotal(int total);
        void onsycnResult(float BlueTem, String TempID);
        void onSycnState(int begin, int total, String backtime);

    }

    public void setOnWt1DataListener(OnWt1DataListener l){
        onWt1DataListener = l;
    }

    public static int byteToInt(byte b) {
        //Java ????? byte ?????з???????????????????? 0xFF ???ж?????????????????
        return b & 0xFF;
    }
    @Override
    public synchronized void calculateData_WT1(byte[] datas,BluetoothLeClass mBLE, Activity activity)  {
        if (datas != null) {
            for (int i = 0; i < datas.length; i++) {
                bytes.add(datas[i]);
            }
            Log.d("length",""+bytes.size());

            int length = bytes.size();
            for (int j = 0; j < bytes.size(); j++) {

                if (bytes.get(j) == -86 && j < length - 1 && bytes.get(j + 1) != -86) {

                    int n =byteToInt(bytes.get(j + 1)) ; // ????????????n??????????????0xAA????????????????????У??????
                    int sum = n; // ??????????Checksum?????????????????????????????
                    // ???????????????????????


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
                                // ??????----
//                                Log.d("test", "??????,?????д??????????0xAA");
                                // bytes.clear();
                                return;
                            }
                        }
                        int add = byteToInt(bytes.get(j + 1 + k + 1));
                        sum = sum + add;//?????Checksum?????????????????????????????
                        sum=sum%256;
                    }

                    if (j + 1 + n > length) {

                        break;
                    }
////                     ???У??????
                    int checksum = byteToInt(bytes.get(j + 1 + n));// ?????n??????????0xAA????????
//                  Log.d("checksum----",""+checksum+"j="+j+",---n="+n+"sum="+sum+",sum % 256="+sum % 256);
//                    for (int i=0;i<bytes.size();i++)
//                    {
//                        Log.d("?","??"+i+"????"+bytes.get(i).intValue());
//                    }
//            //   У??
//
                    if (checksum != sum % 256) {
                        // У????????????

                        if ((checksum + 256) != sum % 256)
                            continue;
                    }

                    List<Byte> bytesTwo = new ArrayList<Byte>();

                    for (int m = 0; m < n - 1; m++) {
                        bytesTwo.add(bytes.get(j + 1 + m + 1));
                        if (bytes.get(j + 1 + m + 1) == -86 && bytes.get(j + 1 + m + 2) == -86) {
                            m++;
                        }
                    }

//                    for (int i=0;i<bytesTwo.size();i++)
//                    {
//                        Log.d("????????bytesTwo?????","??"+i+"????"+bytesTwo.get(i).intValue());
//                    }
//                    for (Byte b:bytesTwo)
//                    {
//                        Log.d("",""+b.intValue());
//                    }


                    int pID = bytesTwo.get(0);
                    Log.d("test", "pID =" + pID);

                    switch (pID) {
                        case 1://??????????
                            bytes.clear();//???????,??????????
                            break;
                        case 2://?????????
                            bytes.clear();
                            break;
                        case 3:

                            switch (ResponseID) {
                                case 0:

//                                    SendForAll(mBLE);
                                    isCheckTime = true;
                                    break;
                                case 1:

                                    break;
                                case 2:

                                    break;
                                case 3:

                                    break;
                                case 4:

                                    break;
                                default:
                                    break;
                            }
                            bytes.clear();
                            break;
                        case 17:
                            if (bytesTwo.get(1) == 0) {
                                TEMPSTATE = 0;
                                int tempH = bytesTwo.get(3);//
                                int tempL = bytesTwo.get(2);//
                                if (tempH < 0) {
                                    tempH += 256;
                                }
                                if (tempL < 0) {
                                    tempL += 256;
                                }
                                 temp = ((tempH * 256 + tempL) * 0.01);//
                                Log.d("test", "case 17::tempH =" + tempH);
                                Log.d("test", "case 17::tempL =" + tempL);
                                Log.d("test", "case 17::temp =" + temp);
//                                Log.d("????????", "111111111");


                            } else if (bytesTwo.get(1) == 1) {//?¶????(>50)
                                Log.i("test", "SENDTEMPHIGHT");
                                TEMPSTATE = 1;

//
                            } else if (bytesTwo.get(1) == 2) {//?¶????(<0)
//
                                TEMPSTATE = 2;
                            }
                            bytes.clear();
                            break;
                        case 18:


                                int BTBatteryCopy = bytesTwo.get(1);
                                if (BTBatteryCopy < 0) {
                                    BTBattery = (bytesTwo.get(1) + 256) % 16;

                                } else {
                                    BTBattery = bytesTwo.get(1) % 16;
                                }

//                            onWt1DataListener.onBTBattery(""+BTBattery);

                                int version = bytesTwo.get(2);

                                tempVersion = Integer.toString((version / 16)) + "."
                                        + Integer.toString((version % 16));

//                            onWt1DataListener.onVersion(""+version);
                                int three = bytesTwo.get(3) < 0 ? bytesTwo.get(3) + 256
                                        : bytesTwo.get(3);
                                int four = bytesTwo.get(4) < 0 ? bytesTwo.get(4) + 256
                                        : bytesTwo.get(4);
                                int five = bytesTwo.get(5) < 0 ? bytesTwo.get(5) + 256
                                        : bytesTwo.get(5);
                                int six = bytesTwo.get(6) < 0 ? bytesTwo.get(6) + 256
                                        : bytesTwo.get(6);
                            bytes.clear();
                                int[] times = {three, four, five, six};
                                 time = HomeUtil.BuleToTime(times);

//
                            break;
                        case 20:
//                            Log.d("IsFirstPack", "" + IsFirstPack);
//                            if (IsFirstPack) {
//                                IsFirstPack = false;
                            WitchDateBlock=bytesTwo.get(1)+bytesTwo.get(2)*256;
                            WitchPack=bytesTwo.get(3)+bytesTwo.get(4)*256;
//                            if (WitchDateBlock<DataBlock)//???С?????????
//                            {

                                    for (n=5;n<bytesTwo.size();n=n+2)
                                    {
                                        float BlueTem = (float) ((((bytesTwo.get(n) < 0 ? bytesTwo
                                                .get(n) + 256 : bytesTwo.get(n)) + (bytesTwo
                                                .get(n+1) < 0 ? bytesTwo.get(n+1) + 256
                                                : bytesTwo.get(n+1)) * 256)) * 0.01);
//                                        Log.d("test", "??" + n + "????????¶????" + BlueTem);
                                    }

//                                if (WitchPack==total-1)
//                                {
//
////
//                                        bytes.clear();
//                                        WitchDateBlock++;
//                                        SendRequestForDate(WitchDateBlock,mBLE);
////
//                                }
//                            }



                                String TempID = HomeUtil.Date2ID(BackTime);
                                Log.d("????????",""+TempID);

                                bytes.clear();
//                                if (WitchDateBlock < DataBlock) {
//                                    SendRequestForDate(WitchDateBlock,mBLE);
//                                    MyReSendPackTask(WitchDateBlock,mBLE);
//                                    WitchDateBlock++;
//                                } else {
//                                    Log.d("test", "?????????????????????????????????????????????");
//                                    MySendSyncEnd(0,activity,mBLE);
//                                }
//
////                                onWt1DataListener.onsycnResult(BlueTem,TempID);
//                            }

//                            bytes.clear();
//                            onWt1DataListener.onsycnResult(BlueTem,TempID);

//                            bytes.clear();
                            break;
                        case 21:
//                            IsFirstPack = true;
//                            Log.d("test", "?????????????????");
//                            Log.d("test", "????????????????????????"
//                                    + (bytesTwo.get(1) + bytesTwo.get(2) * 256));
                             begin=bytesTwo.get(1) + bytesTwo.get(2) * 256;
//                            Log.d("test", "????????????????????????????????"
//                                    + (bytesTwo.get(7) + bytesTwo.get(8) * 256));
                             total=bytesTwo.get(7) + bytesTwo.get(8) * 256;
                            BackTime = HomeUtil.BuleToTime(new int[]{
                                    bytesTwo.get(3), bytesTwo.get(4),
                                    bytesTwo.get(5), bytesTwo.get(6)});
//                            Log.d("test", "????????????????????????" + BackTime);
                            bytes.clear();
//                            bytes.clear();
//                            onWt1DataListener.onSycnState(begin,total,BackTime);
//                            MyReSendPackTask(WitchDateBlock,mBLE);
//                            WitchDateBlock++;
//                            WitchDateBlock++;
//                            bytes.clear();
                            break;
                        case 22:
                            int all = bytesTwo.get(1) + bytesTwo.get(2) * 256;
                            bytes.clear();
//                            onWt1DataListener.ontotal(all);
//                            Log.d("test", "ID????0x16 ????????????:?????????ж????::" + all);
                            if (all > 0) {
                                // ??????????????????????????
                                DataBlock = all;
                                SendRequestForDate(WitchDateBlock,mBLE);
                            } else {
                                DataBlock = 0;
                                IsSyncIng = false;
                                // ???????????????
//                                Log.d("test", "?????????????0????");
                                SharedPreferencesUtil
                                        .setEquipmentSynchronizationTime(
                                                activity,
                                                MyDateUtil.getDateFormatToString("yyyy-MM-dd HH:mm"));
                            }
                            bytes.clear();
//                            onWt1DataListener.ontotal(all);
//                            bytes.clear();
                            break;

                    }



                }
                onWt1DataListener.setTemp(temp);
                if (TEMPSTATE==1)
                {
                    onWt1DataListener.ontempState(MACRO_CODE_4);
                }
                if (TEMPSTATE==2)
                {
                    onWt1DataListener.ontempState(MACRO_CODE_5);
                }
                onWt1DataListener.onBTBattery("" + BTBattery);
                onWt1DataListener.onVersion("" + tempVersion);
                onWt1DataListener.onTime(time);

            }


        }
    }


    /**
     *  ??????????????????? 0???????????? 1 ???? ??????????
     * @param num
     * @param activity
     * @param mBLE
     */


    public void MySendSyncEnd(int num, Activity activity, BluetoothLeClass mBLE) {

        List<Byte> sendforAll = new ArrayList<Byte>();
        sendforAll.add((byte) 0xAA);// ?????????0xAA?? -86??????
        sendforAll.add((byte) 0x04);// ???? ?????????
        // ?????
        sendforAll.add((byte) 0x6A);

        sendforAll.add((byte) num);// 0
        sendforAll.add((byte) 0x00);// ????
        // У??
        int CRS = 0;
        for (int i = 2; i < sendforAll.size(); i++) {
            CRS += sendforAll.get(i);
        }
        sendforAll.add((byte) CRS);
//        Log.d("test", "????????????????????????::" + num);
        if (mBLE != null) {
            mBLE.writeCharacteristic(HomeUtil
                    .CheckByte(sendforAll));
        }
        ResponseID = 3;
        IsSyncIng = false;
        SharedPreferencesUtil.setEquipmentSynchronizationTime(activity,
                MyDateUtil.getDateFormatToString("yyyy-MM-dd HH:mm"));
    }



    public void MyReSendPackTask(int witch, final BluetoothLeClass mBLE) {
        final Timer timer = new Timer();
        final int num = witch;
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (num == WitchDateBlock - 1 && IsFirstPack) {
                    // ????????????????????
                    SendRepeatRequest(WitchDateBlock,mBLE);
                } else {
                    timer.cancel();
                }
            }
        };
        timer.schedule(task, 15000);
    }


    public void SendRepeatRequest(int num,BluetoothLeClass mBLE) {

        List<Byte> sendforAll = new ArrayList<Byte>();
        sendforAll.add((byte) 0xAA);// ?????????0xAA?? -86??????
        sendforAll.add((byte) 0x07);// ???? ?????????
        // ?????
        sendforAll.add((byte) 0x69);
        // byte2 byte3????????? ??0???????
        int[] ID = HomeUtil.getDateID(num);
        sendforAll.add((byte) ID[0]);
        sendforAll.add((byte) ID[1]);
        sendforAll.add((byte) 0x00);// 0
        sendforAll.add((byte) 0x00);// 0
        sendforAll.add((byte) 0x00);// ????
        // У??
        int CRS = 0;
        for (int i = 2; i < sendforAll.size(); i++) {
            CRS += sendforAll.get(i);
        }
        sendforAll.add((byte) CRS);
//        Log.d("test", "???????????????????????num?????????::" + num);
        if (mBLE != null) {
            mBLE.writeCharacteristic(HomeUtil
                    .CheckByte(sendforAll));
        }
        ResponseID = 2;
    }


    public void SendRequestForDate(int num,BluetoothLeClass mBLE) {

        List<Byte> sendforAll = new ArrayList<Byte>();
        sendforAll.add((byte) 0xAA);// ?????????0xAA?? -86??????
        sendforAll.add((byte) 0x05);// ???? ?????????
        // ?????
        sendforAll.add((byte) 0x68);
        sendforAll.add((byte) 0x00);// ????
        // byte2 byte3????????? ??0???????
        int[] ID = HomeUtil.getDateID(num);
        sendforAll.add((byte) ID[0]);
        sendforAll.add((byte) ID[1]);
        // У??
        int CRS = 0;
        for (int i = 2; i < sendforAll.size(); i++) {
            CRS += sendforAll.get(i);
        }
        sendforAll.add((byte) CRS);
//        Log.d("test", "???????????????????????::" + num);
        if (mBLE != null) {
            mBLE.writeCharacteristic(HomeUtil
                    .CheckByte(sendforAll));
            ResponseID = 1;
            // ????????????5????????????????????????????????????????????
            // ????????
//            MyReSendTask(num,mBLE);
        }
    }


    public void MyReSendTask(int witch, final BluetoothLeClass mBLE) {
//        Log.d("", "?????????????????::");
        final Timer timer = new Timer();
        final int num = witch;
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (num == WitchDateBlock) {
                    // ????????????????????
                    SendRequestForDate(WitchDateBlock,mBLE);
                } else {
                    timer.cancel();
                }
            }
        };
        timer.schedule(task, 5000);
    }


    public void SendForTime(BluetoothLeClass mBLE) {
//        Log.d("", "????????У??????????::");
        int[] nowTime = HomeUtil.getTimeByte();
        // app????????????????60??У??????byte2-byte5?????? byte
        // 6????
        // byte0 ??? byte1????
        // ?????0xAA?? ???? ????? У??Checksum
        // ?????????0xAA??
        // ???????????????????????????????У????????????????0xAA??
        // ??????????????????????
        // Checksum??У????????????Checksum????????????????У????
        List<Byte> sendbytes = new ArrayList<Byte>();
        sendbytes.add((byte) 0xAA);// ?????????0xAA??
        // //170??????
        sendbytes.add((byte) 0x08);// ???? ?????????
        // ????7byte+1byteУ??
        // ???????????
        sendbytes.add((byte) 0x6B);// beye0
        sendbytes.add((byte) 0x00);// beye1????
        // byte2-byte5??????
        sendbytes.add((byte) nowTime[0]);// beye2
        sendbytes.add((byte) nowTime[1]);// beye3
        sendbytes.add((byte) nowTime[2]);// beye4
        sendbytes.add((byte) nowTime[3]);// beye5

        sendbytes.add((byte) 0x00);// beye6

        int size = sendbytes.size();

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
            // ??????? ??3??????isCheckTime???true????????·???????У???????
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


      public void SendForAll(BluetoothLeClass mBLE) {
//        Log.d("show", "??????????????????????::");
        IsSyncIng = true;
        ResponseID = 4;
        List<Byte> sendforAll = new ArrayList<Byte>();
        sendforAll.add((byte) 0xAA);// ?????????0xAA?? //170??????
        sendforAll.add((byte) 0x03);// ???? ?????????
        // ?????
        sendforAll.add((byte) 0x67);
        sendforAll.add((byte) 0x00);
        // У??
        sendforAll.add((byte) 0x67);
//        Log.d("test", "??????????????????????::");
        if (mBLE != null) {
            mBLE.writeCharacteristic(HomeUtil
                    .CheckByte(sendforAll));
        }
    }

}
