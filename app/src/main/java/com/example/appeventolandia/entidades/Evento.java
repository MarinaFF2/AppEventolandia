package com.example.appeventolandia.entidades;

import android.app.Activity;

import com.example.appeventolandia.ConexionBBDD.ConexionBBDD;

import java.io.Serializable;
import java.util.ArrayList;

public class Evento  implements Serializable {
    /***Atributos****/
    private int id;
    private String ubicacion;
    private String fecha;
    private String duracion;
    private int idCliente;
    private int idOrganizador;
    private double precio;
    private String descripcion;
    private String nombre;
    private String tipoEvento;

    /***Constructor****/
    public Evento() {
    }
    public Evento(String nombre, String descripcion, String tipoEvento, int idCliente, int idOrganizador, String ubicacion, String fecha, String duracion, double precio) {
        this.ubicacion = ubicacion;
        this.fecha = fecha;
        this.duracion = duracion;
        this.idCliente = idCliente;
        this.idOrganizador = idOrganizador;
        this.precio = precio;
        this.descripcion = descripcion;
        this.nombre = nombre;
        this.tipoEvento = tipoEvento;
    }
    public Evento(int id, String nombre, String descripcion, String tipoEvento, int idCliente, int idOrganizador, String ubicacion, String fecha, String duracion, double precio) {
        this.id = id;
        this.ubicacion = ubicacion;
        this.fecha = fecha;
        this.duracion = duracion;
        this.idCliente = idCliente;
        this.idOrganizador = idOrganizador;
        this.precio = precio;
        this.descripcion = descripcion;
        this.nombre = nombre;
        this.tipoEvento = tipoEvento;
    }

    /***Getters && Setters****/
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdOrganizador() {return idOrganizador;}

    public void setIdOrganizador(int idOrganizador) {this.idOrganizador = idOrganizador;}

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    /***Metodos de la clase****/
    public static void insertEventoIniciales(Activity activity) {
        ArrayList<Evento> listEvent = new ArrayList<Evento>();
        listEvent.add(new Evento("Bautizo Vazquez","Es un bautizo en la tarde, va a haber banquete, cocteles y entrega de regalos","Bautizo",1,2,"ubicación","10/06/2020","5h",150.50));
        listEvent.add(new Evento("Bautizo Gomez","Es un bautizo en la tarde, va a haber banquete, cocteles y entrega de regalos","Bautizo",5,2,"ubicación","15/06/2020","5h",150.50));

        ConexionBBDD connection = new ConexionBBDD(activity,"bd_events",null,1);
        for (Evento evento:listEvent) {
            connection.insertEvento(evento, activity);
        }
    }
}
