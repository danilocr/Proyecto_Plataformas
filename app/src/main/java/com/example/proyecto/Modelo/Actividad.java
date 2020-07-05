package com.example.proyecto.Modelo;

public class Actividad {
    public String id;
    public String idUsuario;
    public String descripcion;
    public String fechaInicio;
    public String fechaFin;
    public String tiempo;
    public String distancia;
    public String calorias;
    public String velocidad;

    public Actividad(String id, String idUsuario, String descripcion, String fechaInicio,
                     String fechaFin, String tiempo, String distancia, String calorias, String velocidad){
        this.id = id;
        this.idUsuario = idUsuario;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.tiempo = tiempo;
        this.distancia = distancia;
        this.calorias = calorias;
        this.velocidad = velocidad;
    }


}
