package com.example.bluetoothlibrary.Interface;

import android.app.Activity;

import com.example.bluetoothlibrary.BluetoothLeClass;
import com.example.bluetoothlibrary.Config;
import com.example.bluetoothlibrary.Impl.ResolveWt1;

/**
 * Created by laiyiwen on 2017/4/26.
 */

public interface Wt1data {

      void calculateData_WT1(byte[] datas, BluetoothLeClass mBLE, Activity activity);
      void setOnWt1DataListener(ResolveWt1.OnWt1DataListener listener);

      void MySendSyncEnd(int num, Activity activity, BluetoothLeClass mBLE);


      void MyReSendPackTask(int witch, final BluetoothLeClass mBLE);



      void SendRepeatRequest(int num, BluetoothLeClass mBLE);




      void SendRequestForDate(int num, BluetoothLeClass mBLE);




      void MyReSendTask(int witch, final BluetoothLeClass mBLE);


      void SendForTime(BluetoothLeClass mBLE);



      void MyTimeTask(final BluetoothLeClass mBLE);


      void SendForAll(BluetoothLeClass mBLE);

}
