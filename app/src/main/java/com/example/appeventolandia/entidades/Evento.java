package com.example.appeventolandia.entidades;

import android.app.Activity;
import android.graphics.Color;
import com.example.appeventolandia.ConexionBBDD.ConexionBBDD;
import com.example.appeventolandia.R;
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
    /** metodo para identificar el tipo de evento a partir de una cadena para el spinner*/
    public static int tipoEventoByInt(String sTipoEvento){
        int nTipoEvento = -1;
        switch (sTipoEvento){
            case "Bautizo":
                nTipoEvento = 0;
                break;
            case "Boda":
                nTipoEvento = 1;
                break;
            case "Comunion":
                nTipoEvento = 2;
                break;
            case "Compromiso":
                nTipoEvento = 3;
                break;
            case "Aniversario":
                nTipoEvento = 4;
                break;
            case "Cumpleanios":
                nTipoEvento = 5;
                break;
        }
        return nTipoEvento;
    }
    /** Metodo para poner color al cardview según el tipo de evento*/
    public int getColor(){
        int color = 0;
        switch (this.tipoEvento){
            case "Boda":
                color = Color.BLUE;
                break;
            case "Bautizo":
                color = Color.RED;
                break;
            case "Comunion":
                color = Color.GREEN;
                break;
            case "Compromiso":
                color = Color.MAGENTA;
                break;
            case "Aniversario":
                color = Color.YELLOW;
                break;
            case "Cumpleaños":
                color = Color.WHITE;
                break;
        }
        return color;
    }
    /** Metodo para poner la foto al cardview según el tipo de evento*/
    public int getFoto(){
        int foto = 0;
        switch (this.tipoEvento){
            case "Boda":
                foto = R.drawable.boda;
                break;
            case "Bautizo":
                foto = R.drawable.bautizo;
                break;
            case "Comunion":
                foto = R.drawable.comunion;
                break;
            case "Compromiso":
                foto = R.drawable.compromiso;
                break;
            case "Aniversario":
                foto = R.drawable.aniversario;
                break;
            case "Cumpleaños":
                foto = R.drawable.cumpleanos;
                break;
        }
        return foto;
    }

    /**metodo para crear el arraylist de los eventos por defecto e insertar eventos*/
    public static void insertEventoIniciales(Activity activity) {
        //creamos el arraylist de eventos por defecto
        String ubicacion ="38.97876, -3.92874";
        ArrayList<Evento> listEvent = new ArrayList<>();
        listEvent.add(new Evento("Bautizo Vazquez","Es un bautizo en la tarde, va a haber banquete, cocteles y entrega de regalos","Bautizo",1,8,ubicacion,"10/06/2021","5",150.50));
        listEvent.add(new Evento("Bautizo Gomez","Es un bautizo en la tarde, va a haber banquete, cocteles y entrega de regalos","Bautizo",1,8,ubicacion,"15/06/2021","5",150.50));
        listEvent.add(new Evento("Bautizo Vazquez","Es un bautizo en la tarde, va a haber banquete, cocteles y entrega de regalos","Bautizo",5,6,ubicacion,"20/06/2021","5",150.50));
        listEvent.add(new Evento("Bautizo Gomez","Es un bautizo en la tarde, va a haber banquete, cocteles y entrega de regalos","Bautizo",5,8,ubicacion,"25/06/2021","5",150.50));
        listEvent.add(new Evento("Boda Vazquez","Es un boda en la tarde, va a haber banquete, cocteles y entrega de regalos","Boda",5,6,ubicacion,"21/06/2021","5",150.50));
        listEvent.add(new Evento("Boda Gomez","Es un Boda en la tarde, va a haber banquete, cocteles y entrega de regalos","Boda",1,6,ubicacion,"25/06/2021","5",150.50));
        listEvent.add(new Evento("Compromiso Vazquez","Es un Compromiso en la tarde, va a haber banquete, cocteles y entrega de regalos","Compromiso",5,8,ubicacion,"10/06/2021","5",150.50));
        listEvent.add(new Evento("Compromiso Gomez","Es un Compromiso en la tarde, va a haber banquete, cocteles y entrega de regalos","Compromiso",1,8,ubicacion,"15/06/2021","5",150.50));
        //hacemos la conexion a la bbdd
        ConexionBBDD connection = new ConexionBBDD(activity,"bd_events",null,2);
        //recorremos el arraylist
        for (Evento evento:listEvent) {
            //realizamos la insercion del evento
            connection.insertEvento(evento);
        }
    }
}
