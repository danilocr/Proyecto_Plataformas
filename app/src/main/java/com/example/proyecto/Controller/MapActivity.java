package com.example.proyecto.Controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;
import com.example.proyecto.R;
import com.example.proyecto.recursosProyecto.dialogRuta;
import com.example.proyecto.recursosProyecto.firebase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class MapActivity extends AppCompatActivity  implements OnMapReadyCallback,dialogRuta.DialogRutaListener{
    private GoogleMap mMap;
    private String tiempo = "";
    private LocationManager locationManager;
    private LocationListener listener;
    private double distancia=0;
    private Chronometer chronometer;
    private long pauseOffset=0;
    private boolean running;
    private boolean flag = false;
    private double velocidadGlobal = 0;
    private static final long MIN_TIME = 10000;
    Button startServiceButton, stopServiceButton,pause;
    private double velocidadProm = 0;
    private String nombreRuta;
    private  PolylineOptions polylineOptions;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent intent = getIntent();
        chronometer = findViewById(R.id.chronometer);
        chronometer.setFormat("Tiempo: %s");
        chronometer.setBase(SystemClock.elapsedRealtime());
        polylineOptions = new PolylineOptions();
        startServiceButton = (Button)findViewById(R.id.btnInicio);
        startServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=true;
                startChronometer(v);
                Intent intent = new Intent(MapActivity.this, ActivityService.class);
                intent.setAction(ActivityService.ACTION_START_FOREGROUND_SERVICE);
                startService(intent);
                startServiceButton.setEnabled(false);
                stopServiceButton.setEnabled(true);
                pause.setEnabled(true);
            }
        });

        stopServiceButton = (Button)findViewById(R.id.btnSalir);
        stopServiceButton.setEnabled(false);
        stopServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetChronometer(v);

                Intent intent = new Intent(MapActivity.this, ActivityService.class);
                intent.setAction(ActivityService.ACTION_STOP);
                startService(intent);
                openDialog();
                startServiceButton.setEnabled(true);

            }
        });

        pause = findViewById(R.id.btnPausar);
        pause.setEnabled(false);
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=false;
                startServiceButton.setEnabled(true);
                pauseChronometer(v);
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapaUbi);
        mapFragment.getMapAsync(this);
    }

    private void openDialog() {
        dialogRuta dialogNomRuta = new dialogRuta();
        dialogNomRuta.show(getSupportFragmentManager(),"dialog Ruta");
    }

    private void grabarRuta(String nombreRuta) {

        String id = getFromSharedPreferences("id");
        Map<String,Object> map = new HashMap<>();
        map.put("nombre", nombreRuta);
        map.put("velocidad", ""+velocidadProm);
        map.put("distancia", ""+distancia);
        map.put("tiempo", ""+tiempo);
        map.put("coordenadas",polylineOptions.getPoints());

        firebase.mDatabase.child("users").child(id).child("ruta").child(nombreRuta).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MapActivity.this,"Ruta Guardada correctamente",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MapActivity.this,"Error de los datos de la ruta",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void startChronometer(View v) {
        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime()- pauseOffset);
            chronometer.start();
            running = true;
        }
    }

    public void pauseChronometer(View v) {
        if (running) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
        }
    }

    public void resetChronometer(View v) {
        tiempo+=(SystemClock.elapsedRealtime() - chronometer.getBase());
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
    }

    @SuppressLint("ObsoleteSdkInt")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().isCompassEnabled();
        mMap.getUiSettings().isMapToolbarEnabled();

        polylineOptions.color(0xff000000);
        final double[] latIni = {0};
        final double[] latFin = {0};
        final double[] lonFin = {0};
        final double[] lonIni = {0};
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                TextView txtDistancia = (TextView) findViewById(R.id.textViewMapDist);
                TextView txtVelocidad = (TextView) findViewById(R.id.textViewMapSpeed);

                if(latIni[0] != location.getLatitude() && lonIni[0] != location.getLongitude()){
                    latFin[0] = latIni[0];
                    lonFin[0] = lonIni[0];
                }
                latIni[0] = location.getLatitude();
                lonIni[0] = location.getLongitude();
                float zoom = 20;
                LatLng posicion = new LatLng(location.getLatitude(), location.getLongitude());
                if(flag==true) {
                    distancia = distancia + (getDistanceFromLatLonInKm(latIni[0], lonIni[0], latFin[0], lonFin[0]));
                    txtDistancia.setText(distancia + " M");
                    txtVelocidad.setText(location.getSpeed() + " m/s");
                    velocidadProm = (velocidadGlobal + velocidadProm)/2;
                    velocidadGlobal = (double) location.getSpeed();

                    mMap.clear();
                    polylineOptions.add(posicion);
                    mMap.addPolyline(polylineOptions);

                }


                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posicion, zoom));
                CameraPosition camara = new CameraPosition.Builder()
                        .target(posicion)
                        .zoom(zoom)
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camara));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };
        //Revisar los permisos
        if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, listener);
        int permissioncheck = ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissioncheck == 0) {
            mMap.setMyLocationEnabled(true);
        }
        mMap.getUiSettings().isMyLocationButtonEnabled();
    }
    private String getFromSharedPreferences(String valorNombre) {
        SharedPreferences sharedPreferences = getSharedPreferences("usuario",Context.MODE_PRIVATE);
        return sharedPreferences.getString(valorNombre,"");
    }
    @Override
    public void applyText(String nombreRuta) {
        this.nombreRuta = nombreRuta;
        grabarRuta(nombreRuta);
        finish();
    }
    public static double getDistanceFromLatLonInKm(double lat1, double lon1,double lat2, double lon2) {
        double R = 6371;
        double dLat = deg2rad(lat2 - lat1);
        double dLon = deg2rad(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;
        BigDecimal bd = new BigDecimal(d*100);
        bd = bd.setScale(4, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    public static double deg2rad(double deg) {
        return deg * (Math.PI / 180);
    }
}
