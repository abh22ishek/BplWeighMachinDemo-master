package biolight;


import java.io.*;

public class BPMeasurementModel implements Serializable{

    private static final long serialVersionUID = 122L;
    private String measurementTime;
    private float sysPressure;
    private float diabolicPressure;
    private float pulsePerMin;
    private String comments;
    private String typeBP;

    private String BT02macId;
    private String BT02SerialNo;

    public String getBT02macId() {
        return BT02macId;
    }

    public void setBT02macId(String BT02macId) {
        this.BT02macId = BT02macId;
    }

    public String getBT02SerialNo() {
        return BT02SerialNo;
    }

    public void setBT02SerialNo(String BT02SerialNo) {
        this.BT02SerialNo = BT02SerialNo;
    }

    public String getTypeBP() {
        return typeBP;
    }

    public void setTypeBP(String typeBP) {
        this.typeBP = typeBP;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getMeasurementTime() {
        return measurementTime;
    }

    public void setMeasurementTime(String measurementTime) {
        this.measurementTime = measurementTime;
    }

    public float getSysPressure() {
        return sysPressure;
    }

    public void setSysPressure(float sysPressure) {
        this.sysPressure = sysPressure;
    }

    public float getDiabolicPressure() {
        return diabolicPressure;
    }

    public void setDiabolicPressure(float diabolicPressure) {
        this.diabolicPressure = diabolicPressure;
    }

    public float getPulsePerMin() {
        return pulsePerMin;
    }

    public void setPulsePerMin(float pulsePerMin) {
        this.pulsePerMin = pulsePerMin;
    }





}
