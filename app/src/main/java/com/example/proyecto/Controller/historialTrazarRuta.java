package com.example.proyecto.Controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;

import com.example.proyecto.Modelo.Ruta;
import com.example.proyecto.R;
import com.example.proyecto.recursosProyecto.firebase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class historialTrazarRuta extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnPolylineClickListener {

    private LocationManager locationManager;
    private GoogleMap mMap;
    private LocationListener listener;
    private PolylineOptions polylineOptions;
    private String id;
    String nomRutaSelec;
    private Ruta ruta;
    private boolean flag = false;
    private TextView txtDistancia, txtVelocidad, txtTiempo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_trazar_ruta);
        txtDistancia = (TextView) findViewById(R.id.textViewMapDistRuta);
        txtVelocidad = (TextView) findViewById(R.id.textViewMapSpeedRuta);
        txtTiempo = (TextView) findViewById(R.id.chronometerRuta);

        nomRutaSelec = getIntent().getStringExtra("rutaSelec");
        ruta = new Ruta();
        polylineOptions = new PolylineOptions();
        id = cargarUsuario("id");
        capturarDatosdeRuta();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapaUbiRuta);
        mapFragment.getMapAsync(this);
    }


    private void cargandoDatosdeRutaAMapa() {
        String dist = ruta.getDistancia() + " m";
        String vel = ruta.getVelocidad() + " m/s";
        String tiempo = pasarATiempo(ruta.getTiempo());
        txtDistancia.setText(dist);
        txtVelocidad.setText(vel);
        txtTiempo.setText(tiempo);
        flag = true;
    }

    private String pasarATiempo(String tiempo) {
        double d = Double.parseDouble(tiempo.substring(0, 2));
        String minuto = ((int) (d / 60) == 0) ? "00" : "" + (int) (d / 60);
        String segundo = ((int) (d % 60) != 0) ? "" + (int) (d % 60) : "00";

        return "" + minuto + ":" + segundo;
    }

    private void capturarDatosdeRuta() {
        firebase.mDatabase.child("users").child(id).child("ruta").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()) {
                    if (objSnaptshot.getKey().equals(nomRutaSelec)) {
                        ruta = objSnaptshot.getValue(Ruta.class);
                        for (DataSnapshot objSnaptshot1 : objSnaptshot.getChildren()) {
                            if (objSnaptshot1.getKey().toString().equals("coordenadas")) {
                                for (DataSnapshot objSnaptshot2 : objSnaptshot1.getChildren()) {
                                    String latitudeAux = objSnaptshot2.child("latitude").getValue().toString();
                                    String longitudeAux = objSnaptshot2.child("longitude").getValue().toString();
                                    ruta.getListaPunto().add(new LatLng(Double.parseDouble(latitudeAux), Double.parseDouble(longitudeAux)));
                                }

                            }

                        }
                    }
                }
                cargandoDatosdeRutaAMapa();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @SuppressLint("ObsoleteSdkInt")
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().isCompassEnabled();
        mMap.getUiSettings().isMapToolbarEnabled();

        polylineOptions.color(0xff000000);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                float zoom = 20;
                if (flag) {
                    for (int i = 0; i < ruta.getListaPunto().size(); i++) {
                        polylineOptions.add(ruta.getListaPunto().get(i));
                        mMap.addPolyline(polylineOptions);
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ruta.getListaPunto().get(0), zoom));
                    flag = false;
                }
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, listener);
        mMap.getUiSettings().isMyLocationButtonEnabled();
    }

    private String cargarUsuario(String valor) {
        SharedPreferences sharedPreferences = getSharedPreferences("usuario", Context.MODE_PRIVATE);
        return sharedPreferences.getString(valor,"");
    }

    @Override
    public void onPolylineClick(Polyline polyline) {

    }
}