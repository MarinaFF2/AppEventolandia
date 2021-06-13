package com.example.appeventolandia.ConexionBBDD;

public class Utilidades {

    /************ Usuarios **************/
    public static final String TABLA_USER="usuario";
    public static final String CAMPO_ID_USER="id1_user";
    public static final String CAMPO_NOMBRE_APELLIDOS_USER="nameLastname_user";
    public static final String CAMPO_CORREO_USER="email1_user";
    public static final String CAMPO_PWD_USER="pwd1_user";
    public static final String CAMPO_ID_ROL_USER="idRol_user";
    public static final String CAMPO_FOTO_USER="foto_user";

    public static final String CREAR_TABLA_USER = " CREATE TABLE " +
            TABLA_USER+" ( "+CAMPO_ID_USER+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +CAMPO_NOMBRE_APELLIDOS_USER+" TEXT, "+CAMPO_CORREO_USER+" TEXT, "+
            CAMPO_PWD_USER+" TEXT, "+CAMPO_ID_ROL_USER+" INTEGER, "+
            CAMPO_FOTO_USER+" INTEGER ); ";

    /************ Eventos **************/
    public static final String TABLA_EVENT = "evento";
    public static final String CAMPO_ID_EVENT = "id_evento";
    public static final String CAMPO_NOMBRE_EVENT = "nombre_evento";
    public static final String CAMPO_DESCRIPCION_EVENT = "descripcion_evento";
    public static final String CAMPO_TIPO_EVENT = "tipoEvento_evento";
    public static final String CAMPO_ID_CLIENTE_EVENT = "id_Cliente_evento";
    public static final String CAMPO_ID_ORGANIZADOR_EVENTO="id_OrganizadorEvento";
    public static final String CAMPO_UBICACION_EVENT = "ubicacion";
    public static final String CAMPO_FECHA_EVENT = "fecha_evento";
    public static final String CAMPO_DURACION_EVENT = "duracion_evento";
    public static final String CAMPO_PRECIO_EVENT = "precio_evento";

    public static final String CREAR_TABLA_EVENT = "CREATE TABLE " +
            TABLA_EVENT+" ( "+CAMPO_ID_EVENT+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +CAMPO_NOMBRE_EVENT+" TEXT NOT NULL,"+CAMPO_DESCRIPCION_EVENT+" TEXT NOT NULL,"
            +CAMPO_TIPO_EVENT+" TEXT NOT NULL,"+CAMPO_ID_CLIENTE_EVENT+" INTEGER NOT NULL, "
            +CAMPO_ID_ORGANIZADOR_EVENTO+" INTEGER NOT NULL,"+CAMPO_UBICACION_EVENT+" TEXT NOT NULL,"
            +CAMPO_FECHA_EVENT+" TEXT NOT NULL,"+CAMPO_DURACION_EVENT+" TEXT NOT NULL,"+
            CAMPO_PRECIO_EVENT+" TEXT NOT NULL );";
}
