package model;

import android.os.Parcel;
import android.os.Parcelable;


public class RecordsDetail implements Parcelable{

    String Spo2;
    String heartrate;
    String PI;
    String testing_time;
    String duration;
    String device_mac_id;

    public RecordsDetail(String spo2, String heartrate, String PI, String testing_time, String duration,String device_mac_id) {
        Spo2 = spo2;
        this.heartrate = heartrate;
        this.PI = PI;
        this.testing_time = testing_time;
        this.duration = duration;
        this.device_mac_id=device_mac_id;
    }
        // for reading

    protected RecordsDetail(Parcel in){
        String[] data = new String[6];

        in.readStringArray(data);
        this.Spo2 = data[0];
        this.heartrate = data[1];
        this.PI = data[2];
        this.testing_time=data[3];
        this.duration=data[4];
        this.device_mac_id=data[5];
    }



    public String getDevice_mac_id() {
        return device_mac_id;
    }

    public void setDevice_mac_id(String device_mac_id) {
        this.device_mac_id = device_mac_id;
    }

    public String getSpo2() {
        return Spo2;
    }

    public void setSpo2(String spo2) {
        Spo2 = spo2;
    }

    public String getHeartrate() {
        return heartrate;
    }

    public void setHeartrate(String heartrate) {
        this.heartrate = heartrate;
    }

    public String getPI() {
        return PI;
    }

    public void setPI(String PI) {
        this.PI = PI;
    }

    public String getTesting_time() {
        return testing_time;
    }

    public void setTesting_time(String testing_time) {
        this.testing_time = testing_time;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(Spo2);
        dest.writeString(heartrate);
        dest.writeString(PI);
        dest.writeString(testing_time);
        dest.writeString(duration);
        dest.writeString(device_mac_id);


    }


    public static  final Creator CREATOR=new Creator()
    {

        @Override
        public RecordsDetail createFromParcel(Parcel source) {
            return new RecordsDetail(source);
        }

        @Override
        public RecordsDetail[] newArray(int size) {
            return new RecordsDetail[size];
        }
    };


}
