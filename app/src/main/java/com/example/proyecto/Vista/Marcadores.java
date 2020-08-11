package com.example.proyecto.Vista;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.example.proyecto.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Marcadores {
    GoogleMap nMap;
    Context context;

    public Marcadores(GoogleMap nMap, Context context){
        this.nMap = nMap;
        this.context = context;
    }
    public void addMarkersDefault(){


        marcaUno(-12.198191,-77.013433,"Punto1");
        marcaDos(-12.204041,-77.005951,"Punto2");
        marcaTres(-12.176885, -76.971918,"Punto3");

    }
    public void marcaUno(Double latitud, Double longitud, String titulo){
        LatLng punto = new LatLng(latitud,longitud);
        int height = 140;
        int width = 165;

        BitmapDrawable uno = (BitmapDrawable) context.getResources().getDrawable(R.drawable.bicycle1);
        Bitmap unos = uno.getBitmap();
        Bitmap uns = Bitmap.createScaledBitmap(unos,width,height,false);
        nMap.addMarker(new MarkerOptions().position(punto).title(titulo).snippet("uno").icon(BitmapDescriptorFactory.fromBitmap(uns)));

    }

    public void marcaDos(Double latitud, Double longitud, String titulo){
        LatLng punto = new LatLng(latitud,longitud);
        int height = 140;
        int width = 165;

        BitmapDrawable uno = (BitmapDrawable) context.getResources().getDrawable(R.drawable.bicycle1);
        Bitmap unos = uno.getBitmap();
        Bitmap uns = Bitmap.createScaledBitmap(unos,width,height,false);
        nMap.addMarker(new MarkerOptions().position(punto).title(titulo).snippet("uno").icon(BitmapDescriptorFactory.fromBitmap(uns)));

    }
    public void marcaTres(Double latitud, Double longitud, String titulo){
        LatLng punto = new LatLng(latitud,longitud);
        int height = 140;
        int width = 165;

        BitmapDrawable uno = (BitmapDrawable) context.getResources().getDrawable(R.drawable.bicycle1);
        Bitmap unos = uno.getBitmap();
        Bitmap uns = Bitmap.createScaledBitmap(unos,width,height,false);
        nMap.addMarker(new MarkerOptions().position(punto).title(titulo).snippet("uno").icon(BitmapDescriptorFactory.fromBitmap(uns)));

    }
}
