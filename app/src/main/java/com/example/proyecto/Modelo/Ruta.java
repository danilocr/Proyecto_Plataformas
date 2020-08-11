package com.example.proyecto.Modelo;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Ruta {

    private ArrayList<LatLng> listaPunto;
    private String nombre;
    private String velocidad;
    private String distancia;
    private String tiempo;

    public Ruta(){
        listaPunto= new ArrayList<>();
    }

    @Override
    public String toString() {
        return  "Nombre:" + nombre + "\n"+
                "Velocidad:" + velocidad + "\t" +
                "Distancia: " + distancia + "\t" +
                "Tiempo: " + tiempo;
    }

    public ArrayList<LatLng> getListaPunto() {
        return listaPunto;
    }

    public void setListaPunto(ArrayList<LatLng> listaPunto) {
        this.listaPunto = listaPunto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(String velocidad) {
        this.velocidad = velocidad;
    }

    public String getDistancia() {
        return distancia;
    }

    public void setDistancia(String distancia) {
        this.distancia = distancia;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }



}
