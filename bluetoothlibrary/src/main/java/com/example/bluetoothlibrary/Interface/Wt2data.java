package com.example.bluetoothlibrary.Interface;

import android.app.Activity;

import com.example.bluetoothlibrary.BluetoothLeClass;
import com.example.bluetoothlibrary.Config;
import com.example.bluetoothlibrary.Impl.ResolveWt2;

/**
 * Created by laiyiwen on 2017/4/28.
 */

public interface Wt2data {

    void calculateData_WT2(byte[] datas, BluetoothLeClass mBLE, Activity activity);


    void SendForTime(BluetoothLeClass mBLE);



    void MyTimeTask(final BluetoothLeClass mBLE);


    void setOnWt2DataListener(ResolveWt2.OnWt2DataListener listener);
}
