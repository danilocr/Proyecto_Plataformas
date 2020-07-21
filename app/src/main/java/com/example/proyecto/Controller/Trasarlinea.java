package com.example.proyecto.Controller;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.proyecto.Modelo.crearMarcas;
import com.example.proyecto.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Trasarlinea extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap nMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapaUbi);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        nMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;        }
        nMap.setMyLocationEnabled(true);
        nMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        ArrayList<LatLng> points = null;
        PolylineOptions lineOptions = null;
        for(int i = 0; i< crearMarcas.rutas.size(); i++){
            Log.d("aqui", String.valueOf(crearMarcas.rutas.size()));
            points = new ArrayList<LatLng>();
            lineOptions = new PolylineOptions();


            List<HashMap<String, String>> path = crearMarcas.rutas.get(i);


            for(int j=0;j<path.size();j++){
                HashMap<String,String> point = path.get(j);

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

                points.add(position);
            }


            lineOptions.addAll(points);

            lineOptions.width(9);

            lineOptions.color(Color.BLUE);
        }
    }
}