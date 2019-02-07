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
