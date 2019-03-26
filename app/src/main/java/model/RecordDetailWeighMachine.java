package model;


import java.io.*;

public class RecordDetailWeighMachine implements Serializable {

    private static final long serialVersionUID = 132L;
    float weight;
    float bmi;
    String date;

    float metabolism;
    float bodyWater;
    float bodyFat;
    float boneMass;
    float protein;


    float visceralFat;

    public float getMetabolism() {
        return metabolism;
    }

    public void setMetabolism(float metabolism) {
        this.metabolism = metabolism;
    }

    public float getBodyWater() {
        return bodyWater;
    }

    public void setBodyWater(float bodyWater) {
        this.bodyWater = bodyWater;
    }

    public float getBodyFat() {
        return bodyFat;
    }

    public void setBodyFat(float bodyFat) {
        this.bodyFat = bodyFat;
    }

    public float getBoneMass() {
        return boneMass;
    }

    public void setBoneMass(float boneMass) {
        this.boneMass = boneMass;
    }

    public float getProtein() {
        return protein;
    }

    public void setProtein(float protein) {
        this.protein = protein;
    }

    public float getVisceralFat() {
        return visceralFat;
    }

    public void setVisceralFat(float visceralFat) {
        this.visceralFat = visceralFat;
    }

    public int getBodyAge() {
        return BodyAge;
    }

    public void setBodyAge(int bodyAge) {
        BodyAge = bodyAge;
    }

    public float getMuscleMass() {
        return muscleMass;
    }

    public void setMuscleMass(float muscleMass) {
        this.muscleMass = muscleMass;
    }

    public float getLBM() {
        return LBM;
    }

    public void setLBM(float LBM) {
        this.LBM = LBM;
    }

    public float getObesity() {
        return Obesity;
    }

    public void setObesity(float obesity) {
        Obesity = obesity;
    }

    int BodyAge;
    float muscleMass;
    float LBM;
    float Obesity;



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getBmi() {
        return bmi;
    }

    public void setBmi(float bmi) {
        this.bmi = bmi;
    }
}
