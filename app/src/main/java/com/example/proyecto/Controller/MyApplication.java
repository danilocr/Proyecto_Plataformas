package com.example.proyecto.Controller;

import android.app.Application;
import android.widget.Chronometer;

public class MyApplication extends Application {

    private double latitude;
    private double longitude;
    private int cont=0;
    private boolean flag=false;
    private Chronometer chronometer;
    private int distance;
    private float speed;

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed){
        this.speed = speed;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public Chronometer getChronometer() {
        return chronometer;
    }

    public void setChronometer(Chronometer chronometer) {
        this.chronometer = chronometer;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude){
        this.longitude = longitude;
    }

    public void addCont(){
        cont++;
    }

    public int getCont(){
        return cont;
    }

    public boolean getFlag(){return flag;}

    public void setFlag(boolean flag){this.flag = flag;}
}