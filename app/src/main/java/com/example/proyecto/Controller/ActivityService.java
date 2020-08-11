package com.example.proyecto.Controller;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class ActivityService extends Service {

    private static final String TAG_FOREGROUND_SERVICE = "FOREGROUND_SERVICE";

    public static final String ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE";

    public static final String ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE";

    public static final String ACTION_PAUSE = "ACTION_PAUSE";

    public static final String ACTION_STOP = "ACTION_STOP";
    public LocationManager manager;
    public ActivityService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG_FOREGROUND_SERVICE, "My foreground service onCreate().");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null)
        {
            String action = intent.getAction();

            switch (action)
            {
                case ACTION_START_FOREGROUND_SERVICE:
                    startForegroundService();
                    Toast.makeText(getApplicationContext(), "Se inicio el servicio", Toast.LENGTH_SHORT).show();
                    break;
                case ACTION_STOP_FOREGROUND_SERVICE:
                    stopForegroundService();
                    Toast.makeText(getApplicationContext(), "Se detuvo la actividad.", Toast.LENGTH_SHORT).show();
                    break;
                case ACTION_PAUSE:
                    Toast.makeText(getApplicationContext(), "Actividad pausada.", Toast.LENGTH_SHORT).show();
                    break;
                case ACTION_STOP:
                    Toast.makeText(getApplicationContext(), "Se detuvo el servicio.", Toast.LENGTH_SHORT).show();
                    stopForegroundService();
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void startForegroundService()
    {
        Log.d(TAG_FOREGROUND_SERVICE, "servicio iniciado.");
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    private void stopForegroundService()
    {
        Log.d(TAG_FOREGROUND_SERVICE, "servicio detenido.");
        stopForeground(true);
        stopSelf();
    }
}