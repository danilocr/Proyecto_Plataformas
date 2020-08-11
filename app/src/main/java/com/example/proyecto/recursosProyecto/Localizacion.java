package com.example.proyecto.recursosProyecto;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.proyecto.Controller.MapActivity;
import com.example.proyecto.R;

public class Localizacion implements LocationListener {
    MapActivity mapActivity;

    public MapActivity getMainActivity(){
        return  mapActivity;
    }

    public void setMainActivity(MapActivity mapActivity){
        this.mapActivity = mapActivity;
    }

    @Override
    public void onLocationChanged(Location location) {
        mapa(location.getLatitude(), location.getLongitude());
    }

    private void mapa(double latitude, double longitude) {
        FragmentMaps fragmentMaps = new FragmentMaps();

        Bundle bundle = new Bundle();
        bundle.putDouble("lat", latitude);
        bundle.putDouble("lon", longitude);
        fragmentMaps.setArguments(bundle);

        FragmentManager fragmentManager = getMainActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.mapaUbi, fragmentMaps,null);
        fragmentTransaction.commit();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch (status){
            case LocationProvider.AVAILABLE:
                Log.d("debug", "LocationProvider.AVAILABLE");
                break;
            case LocationProvider.OUT_OF_SERVICE:
                Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                break;
        }
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
