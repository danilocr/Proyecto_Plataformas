package com.example.proyecto.Vista.Maps;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;


import com.example.proyecto.Modelo.Punto;
import com.example.proyecto.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class TrazarRutas extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    public ArrayList<Punto> puntos =  new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //Total 5

        //ListaPunto miListaPunto = ListaPunto.getMiListaPunto();

        //puntos = miListaPunto.getListaPunto();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trazar_rutas);
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapNearBy);
        mapFragment.getMapAsync(this);


        System.out.println("PUNTOS!!: " + puntos.size() +" "+ puntos);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("mylog", "Added Markers");
        //LatLng inicio = new LatLng( puntos.get(0).getLatitud(),puntos.get(0).getLongitud());
        LatLng inicio;
        float zoom = 17;
        //System.out.println("PUNTOS!!: " + puntos.size());

        googleMap.getUiSettings().setZoomControlsEnabled(true);

        for(int i=0;i<puntos.size();i++){
            LatLng points = new LatLng( puntos.get(i).getLatitud(),puntos.get(i).getLongitud());
            googleMap.addMarker(new MarkerOptions().position(points).title("Ruta Nro #").icon(BitmapDescriptorFactory.fromResource(R.drawable.bicycle1)));
        }

        if (puntos.size() == 0) {
            inicio = new LatLng( -16.404805,-71.528154);
        }

        else {
            inicio = new LatLng( puntos.get(0).getLatitud(),puntos.get(0).getLongitud());
        }

        UiSettings settings = googleMap.getUiSettings();

        settings.setZoomControlsEnabled(true);

        CameraPosition camara = new CameraPosition.Builder()
                .target(inicio)
                .zoom(17)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(camara));




    }
}
