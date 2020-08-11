package com.example.proyecto.Modelo;

public class Punto {
    private Double Latitude;
    private Double Longitude;

    public Punto(){

    }
    public Punto (String latitude, String longitude) {
        this.Latitude = Double.parseDouble(latitude);
        this.Longitude = Double.parseDouble(longitude);
    }

    public Double getLatitude(){
        return Latitude;
    }
    public void setLatitude(String latitude) { this.Latitude = Double.parseDouble(latitude);}

    public Double getLongitude(){
        return Longitude;
    }
    public void setLongitude(String longitude){ this.Longitude = Double.parseDouble(longitude);}

}
