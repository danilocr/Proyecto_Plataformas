package com.example.proyecto.Modelo;

public class Punto {
    private Double latitud;
    private Double longitud;

    public Punto (String latitud, String longitud) {
        this.latitud = Double.parseDouble(latitud);
        this.longitud = Double.parseDouble(longitud);
    }

    public Double getLatitud(){
        return latitud;
    }
    public void setLatitud(String latitud) { this.latitud = Double.parseDouble(latitud);}

    public Double getLongitud(){
        return longitud;
    }
    public void setLongitud(String longitud){ this.longitud = Double.parseDouble(longitud);}

}
