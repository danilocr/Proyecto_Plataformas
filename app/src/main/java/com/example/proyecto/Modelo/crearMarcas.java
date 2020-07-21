package com.example.proyecto.Modelo;

import android.content.Context;

import com.example.proyecto.Vista.Marcadores;
import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class crearMarcas {
    public static Coordenadas coordenadas = new Coordenadas();
    public static List<List<HashMap<String,String>>> rutas = new ArrayList<>();
    public static void marcarDdefault(GoogleMap nMap,Context context){
        new Marcadores(nMap, context).addMarkersDefault();

    }
}
