package com.example.proyecto.Vista.Maps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyecto.Controller.Localizacion;
import com.example.proyecto.Controller.Trasarlinea;
import com.example.proyecto.Modelo.crearMarcas;
import com.example.proyecto.R;
import com.example.proyecto.Service.ActivityService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {
    private GoogleMap nMap;
    private String tiempo = "";
    private LocationManager locationManager;
    private LocationListener listener;
    private int distancia = 0;
    private Chronometer chronometer;
    private long pauseOffset = 0;
    private boolean running;
    private boolean flag = false;
    float velocidadGlobal = 0;
    private static final long MIN_TIME = 10000;
    crearMarcas crearmarca;
    RequestQueue request;
    Localizacion gpsTracker;
    JsonObjectRequest jsonObjectRequest;
    Location location;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapaUbi);
        mapFragment.getMapAsync(this);
        request = Volley.newRequestQueue(getApplicationContext());

        crearmarca = new crearMarcas();

        Intent intent = getIntent();
        chronometer = findViewById(R.id.chronometer);
        chronometer.setFormat("Tiempo: %s");
        chronometer.setBase(SystemClock.elapsedRealtime());
        gpsTracker = new Localizacion(getApplicationContext());

        Button startServiceButton = (Button) findViewById(R.id.btnInicio);
        startServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = true;
                startChronometer(v);
                Intent intent = new Intent(MapActivity.this, ActivityService.class);
                intent.setAction(ActivityService.ACTION_START_FOREGROUND_SERVICE);
                startService(intent);
            }
        });

        Button stopServiceButton = (Button) findViewById(R.id.btnSalir);
        stopServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        Button pause = findViewById(R.id.btnPausar);
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = false;
                pauseChronometer(v);
            }
        });


    }

    public void startChronometer(View v) {
        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
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
        tiempo += (SystemClock.elapsedRealtime() - chronometer.getBase());
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        nMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        nMap.setMyLocationEnabled(true);
        LatLng n = new LatLng(-12.176885,-77.005951);
        nMap.animateCamera(CameraUpdateFactory.newLatLngZoom(n,10));
        crearMarcas.marcarDdefault(nMap,getApplicationContext());
        nMap.setOnMapClickListener(this);
        nMap.setOnMarkerClickListener(this);

    }
    @Override
    public void onMapLongClick(LatLng latLng) {
        crearMarcas.coordenadas.setOrigenLat(latLng.latitude);
        crearMarcas.coordenadas.setOrigenLon(latLng.longitude);
        Toast.makeText(MapActivity.this,"Toque icono para que seleccione",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onMapClick(LatLng latLng) {
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {

                Toast.makeText(this, "You didn't give permission to access device location", Toast.LENGTH_LONG).show();
                startInstalledAppDetailsActivity();
            }
        }
    }
    public void ObtenerRuta(String latInicial, String lngInicial, String latFinal, String lngFinal){

        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + latInicial + "," + lngInicial + "&destination=" + latFinal + "," + lngFinal + "&key=AIzaSyDwhjSIP_3WV3c8CUMAuInBst62XitWp4E&mode=drive";

        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jRoutes = null;
                JSONArray jLegs = null;
                JSONArray jSteps = null;
                try {
                    jRoutes = response.getJSONArray("routes");


                    for(int i=0;i<jRoutes.length();i++){

                        jLegs = ( (JSONObject)jRoutes.get(i)).getJSONArray("legs");
                        List<HashMap<String, String>> path = new ArrayList<HashMap<String, String>>();

                        for(int j=0;j<jLegs.length();j++){
                            jSteps = ( (JSONObject)jLegs.get(j)).getJSONArray("steps");


                            for(int k=0;k<jSteps.length();k++){

                                String polyline = "";
                                polyline = (String)((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                                List<LatLng> list = decodePoly(polyline);

                                for(int l=0;l<list.size();l++){

                                    HashMap<String, String> hm = new HashMap<String, String>();
                                    hm.put("lat", Double.toString(((LatLng)list.get(l)).latitude) );
                                    hm.put("lng", Double.toString(((LatLng)list.get(l)).longitude) );
                                    path.add(hm);

                                }
                            }

                            crearMarcas.rutas.add(path);

                            Intent intent = new Intent(MapActivity.this, Trasarlinea.class);
                            startActivity(intent);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }catch (Exception e){
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "No se puede conectar "+error.toString(), Toast.LENGTH_LONG).show();
                System.out.println();
                Log.d("ERROR: ", error.toString());
            }
        }
        );

        request.add(jsonObjectRequest);

    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }


    public  class MyAsyncTask extends AsyncTask<Integer, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(Integer... integers) {

            try {
                while (location == null){
                    location = gpsTracker.getLocation();
                    publishProgress(1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            location = gpsTracker.getLocation();
            publishProgress(2);

            return "Fin";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            if(values[0] == 0){
                Log.d("Asyntask", "null");
            }else{
                Log.d("Asyntask", "Coordenadas");
                Toast.makeText(MapActivity.this, "LISTO", Toast.LENGTH_SHORT).show();
                crearMarcas.coordenadas.setOrigenLat(location.getLatitude());
                crearMarcas.coordenadas.setOrigenLon(location.getLongitude());
                Log.d("Asyntask", String.valueOf(location.getLatitude()));
                Log.d("Asyntask", String.valueOf(location.getLongitude()));
                ObtenerRuta(String.valueOf(crearMarcas.coordenadas.getOrigenLat()), String.valueOf(crearMarcas.coordenadas.getOrigenLon()),
                        String.valueOf(crearMarcas.coordenadas.getDestinoLat()), String.valueOf(crearMarcas.coordenadas.getOrigenLon()));

            }

        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("asyntask", "FIN");

        }
    }
    public void AlertShow(String title, final LatLng latLng){
        AlertDialog.Builder builder= new AlertDialog.Builder(MapActivity.this);

        builder.setMessage("Desea ir este punto?");
        builder.setTitle(title);
        builder.setCancelable(false);

        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                crearMarcas.coordenadas.setDestinoLat(latLng.latitude);
                crearMarcas.coordenadas.setDestinoLon(latLng.longitude);

                new MyAsyncTask().execute(0);

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        AlertShow(marker.getTitle(),marker.getPosition());
        return false;
    }
    private Boolean permissionsGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == ((PackageManager.PERMISSION_GRANTED));
    }
    private void startInstalledAppDetailsActivity() {
        Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
