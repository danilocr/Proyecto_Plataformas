package com.example.proyecto.Controller;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

public class VelocidadSensor implements SensorEventListener {

    private  static final float alpha1=0.8f;
    private static final String TAG="LocationReceiver";

    private float mLastX, mLastY, mLastZ;
    private float mHighPass1, mHighPass2, mHighPass3;
    private double acelTotal;

    public VelocidadSensor(){
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x=event.values[0];
        float y=event.values[1];
        float z=event.values[2];
        //Ejecutamos HighPass a los 3 ejes
        mHighPass1 = HighPass(x, mLastX, mHighPass1);
        mHighPass2 = HighPass(y, mLastY,mHighPass2);
        mHighPass3 = HighPass(z, mLastZ, mHighPass3);

        //Actualiza Valores
        mLastX=x;
        mLastY=y;
        mLastZ=z;

        //ACELERACION TOTAL DE LAS 3 DIMENSIONES
        acelTotal=Math.sqrt(Math.pow(mHighPass1,2)+Math.pow(mHighPass2,2)+Math.pow(mHighPass3,2));
        Log.d(TAG,  "aceleracion Total:"+acelTotal);
    }
    public double getAcelTotal(){
        return  acelTotal;
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    private float HighPass(float current, float last, float filtered){
        return alpha1*(filtered+current-last);
    }

}