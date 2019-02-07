package biolight;

import java.util.*;

import model.*;

interface HomeActivityListner {

    void onDataPass(String data);
    void handleMessage(int state);
    void invokeFragment(String tag,String mSystolic,String mDiabolic,String mPulse,String mTestingTime,String mComment);
    void invokeRecordList(List<BPMeasurementModel> recordDetailList, String date);
}
