package com.example.appeventolandia.ConexionBBDD;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.example.appeventolandia.entidades.Evento;
import com.example.appeventolandia.entidades.Usuario;
import java.util.ArrayList;

public class ConexionBBDD extends SQLiteOpenHelper {
    //declaro la bariable para poder interactuar con la bbdd
    private SQLiteDatabase db;
    private Context context;

    public ConexionBBDD(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }


    /**
     * Metodo encargado de generar las tablas correspondientes a las clases en entidades
     * Se llama cuando se crea la base de datos por primera vez.
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //creamos tabla de usuario
        db.execSQL(Utilidades.CREAR_TABLA_USER);//Ejecuta una sola sentencia SQL que no es una sentencia SELECT o cualquier otra sentencia SQL que devuelva datos.
        //creamos tabla de evento
        db.execSQL(Utilidades.CREAR_TABLA_EVENT);
    }
    /**
     * MEtodo para comprobar si ya se ha creado la bbdd
     * Se llama cuando la base de datos necesita ser actualizada.
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //eliminamos la tabla usuario y evento si ya existe
        db.execSQL("DROP TABLE IF EXISTS usuario");
        db.execSQL("DROP TABLE IF EXISTS evento");
        onCreate(db);
    }

    public void deleteUser(int id){
        db = getWritableDatabase(); //Crear y / o abrir una base de datos que se utilizará para lectura y escritura.
        /*
            Método de conveniencia para insertar una fila en la base de datos.
            delete(String table, String whereClause, String[] whereArgs)
            devuelve int
         */
        String[] parametros={id+""};
        int result = db.delete(Utilidades.TABLA_USER,Utilidades.CAMPO_ID_USER+"=?",parametros);
        if(result > 0) {
            Toast.makeText(context, "Delete the user", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Error deleted the user", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }
    public void updateUser(Usuario user) {
        db = getWritableDatabase(); //Crear y / o abrir una base de datos que se utilizará para lectura y escritura.

        String[] parametros={user.getId()+""};
        ContentValues values=new ContentValues();
        values.put(Utilidades.CAMPO_NOMBRE_APELLIDOS_USER,user.getNombreApellidos());
        values.put(Utilidades.CAMPO_CORREO_USER,user.getCorreo());
        values.put(Utilidades.CAMPO_PWD_USER,user.getPwd());
        values.put(Utilidades.CAMPO_ID_ROL_USER,user.getIdRol());
        values.put(Utilidades.CAMPO_FOTO_USER,user.getFoto());

        int result = db.update(Utilidades.TABLA_USER,values,Utilidades.CAMPO_ID_USER+"=?",parametros);
        if(result > 0) {
            Toast.makeText(context, "Updated the user", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Error updated the user", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }
    public void insertUser(Usuario user, Activity activity) {
        //antes de insertar comprobamos que el correo no se repìta
        if(existUserByCorreo(user.getCorreo())==null){
            db = this.getWritableDatabase();//declaro la bariable para poder interactuar con la bbdd

            //metemos los valores a insertar
            ContentValues values = new ContentValues();
            values.put(Utilidades.CAMPO_PWD_USER, user.getPwd());
            values.put(Utilidades.CAMPO_NOMBRE_APELLIDOS_USER, user.getNombreApellidos());
            values.put(Utilidades.CAMPO_FOTO_USER, user.getFoto());
            values.put(Utilidades.CAMPO_ID_ROL_USER, user.getIdRol());
            values.put(Utilidades.CAMPO_CORREO_USER, user.getCorreo());
            //insertamos el usuario
            //devuelve un numero Long dependiento del resultado del insert
            //si idResult es igual 1 ha tenido exito
            Long result = db.insert(Utilidades.TABLA_USER, Utilidades.CAMPO_ID_USER, values);
            if(result > 0) {
                Toast.makeText(activity, "Inserted the user", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(activity, "Error inserted the user", Toast.LENGTH_SHORT).show();
            }
            db.close();
        }else{
            Toast.makeText(context,"Ya existe un usuario con este correo ",Toast.LENGTH_SHORT).show();
        }
    }
    public Usuario existUserByCorreoPwd(String correo,String pwd){
        db = getReadableDatabase();
        String[] parametros={correo,pwd};
        Usuario user = new Usuario();

        String[] campos={Utilidades.CAMPO_ID_USER,Utilidades.CAMPO_NOMBRE_APELLIDOS_USER,Utilidades.CAMPO_CORREO_USER,Utilidades.CAMPO_PWD_USER,Utilidades.CAMPO_ID_ROL_USER,Utilidades.CAMPO_FOTO_USER};
        try {
            Cursor cursor =db.query(Utilidades.TABLA_USER,campos,Utilidades.CAMPO_CORREO_USER+"=? AND "+Utilidades.CAMPO_PWD_USER+"=?",parametros,null,null,null);
            cursor.moveToFirst();
            user.setId(cursor.getInt(0));
            user.setNombreApellidos(cursor.getString(1));
            user.setCorreo(cursor.getString(2));
            user.setPwd(cursor.getString(3));
            user.setIdRol(cursor.getInt(4));
            user.setFoto(cursor.getInt(5));
            cursor.close();
        }catch (Exception e){
            user = null;
        }
        db.close();
        return user;
    }
    public Usuario existUserByCorreo(String correo){
        //declaramos e inicializamos la clase Usuario
        Usuario user = new Usuario();

        db = getReadableDatabase();
        String[] parametros={correo};

        String[] campos={Utilidades.CAMPO_ID_USER,Utilidades.CAMPO_NOMBRE_APELLIDOS_USER,Utilidades.CAMPO_CORREO_USER,Utilidades.CAMPO_PWD_USER,Utilidades.CAMPO_ID_ROL_USER,Utilidades.CAMPO_FOTO_USER};
        try {
            Cursor cursor =db.query(Utilidades.TABLA_USER,campos,Utilidades.CAMPO_CORREO_USER+"=?",parametros,null,null,null);
            cursor.moveToFirst();
            user.setId(cursor.getInt(0));
            user.setNombreApellidos(cursor.getString(1));
            user.setCorreo(cursor.getString(2));
            user.setPwd(cursor.getString(3));
            user.setIdRol(cursor.getInt(4));
            user.setFoto(cursor.getInt(5));
            cursor.close();
        }catch (Exception e){
            Toast.makeText(context,"Not existed the user",Toast.LENGTH_SHORT).show();
            user = null;
        }
        db.close();

        return user;
    }
    public Usuario existUserById(int id){
        db = getReadableDatabase();
        Usuario user = new Usuario();
        String[] parametros={id+""};

        //forma 1

        String[] campos={Utilidades.CAMPO_ID_USER,Utilidades.CAMPO_NOMBRE_APELLIDOS_USER,Utilidades.CAMPO_CORREO_USER,Utilidades.CAMPO_PWD_USER,Utilidades.CAMPO_ID_ROL_USER,Utilidades.CAMPO_FOTO_USER};
        try {
            Cursor cursor =db.query(Utilidades.TABLA_USER,campos,Utilidades.CAMPO_ID_USER+"=?",parametros,null,null,null);
            cursor.moveToFirst();
            user.setId(cursor.getInt(0));
            user.setNombreApellidos(cursor.getString(1));
            user.setCorreo(cursor.getString(2));
            user.setPwd(cursor.getString(3));
            user.setIdRol(cursor.getInt(4));
            user.setFoto(cursor.getInt(5));
            cursor.close();
        }catch (Exception e){
            Toast.makeText(context,"Not existed the user",Toast.LENGTH_SHORT).show();
            user = null;
        }
        db.close();
        return user;
    }
    public ArrayList<Usuario> listUsers(){
        //SACA TODAS LOS USUARIOS
        ArrayList<Usuario> listUsers  = new ArrayList<>();

        //Crear y / o abrir una base de datos.
        db = getReadableDatabase();

        String[] campos={Utilidades.CAMPO_ID_EVENT,Utilidades.CAMPO_NOMBRE_EVENT,Utilidades.CAMPO_DESCRIPCION_EVENT,
                Utilidades.CAMPO_TIPO_EVENT,Utilidades.CAMPO_ID_CLIENTE_EVENT, Utilidades.CAMPO_ID_ORGANIZADOR_EVENTO,
                Utilidades.CAMPO_UBICACION_EVENT,Utilidades.CAMPO_FECHA_EVENT,Utilidades.CAMPO_DURACION_EVENT,Utilidades.CAMPO_PRECIO_EVENT};
        //Consulta la tabla dada, devolviendo un cursor sobre el conjunto de resultados.
        Cursor cursor =db.query(Utilidades.TABLA_EVENT,campos,null,null,null,null,null);

        while (cursor.moveToNext()){

            Usuario user = new Usuario();
            user.setId(cursor.getInt(0));
            user.setNombreApellidos(cursor.getString(1));
            user.setCorreo(cursor.getString(2));
            user.setPwd(cursor.getString(3));
            user.setIdRol(cursor.getInt(4));
            user.setFoto(cursor.getInt(5));

            listUsers.add(user);
        }
        //comprobamos que el tamaño no sea 0
        if(listUsers.size() == 0){
            listUsers = null;
        }
        return listUsers;
    }
    public void insertEvento(Evento event, Activity activity) {
        //antes de insertar comprobamos que el correo no se repìta
        if(existEventByIDClienteIDOrganizadorFecha(event.getIdCliente(),event.getIdOrganizador(),event.getFecha()) == null) {
            db = getWritableDatabase();//declaro la bariable para poder interactuar con la bbdd

            //metemos los valores a insertar
            ContentValues values = new ContentValues();
            //values.put(Utilidades.CAMPO_ID_EVENT,event.getId());
            values.put(Utilidades.CAMPO_NOMBRE_EVENT, event.getNombre());
            values.put(Utilidades.CAMPO_DESCRIPCION_EVENT, event.getDescripcion());
            values.put(Utilidades.CAMPO_TIPO_EVENT, event.getTipoEvento());
            values.put(Utilidades.CAMPO_ID_CLIENTE_EVENT, event.getIdCliente());
            values.put(Utilidades.CAMPO_ID_ORGANIZADOR_EVENTO, event.getIdOrganizador());
            values.put(Utilidades.CAMPO_UBICACION_EVENT, event.getUbicacion());
            values.put(Utilidades.CAMPO_FECHA_EVENT, event.getFecha());
            values.put(Utilidades.CAMPO_DURACION_EVENT, event.getDuracion());
            values.put(Utilidades.CAMPO_PRECIO_EVENT, event.getPrecio());

            //insertamos el usuario
            //devuelve un numero Long dependiento del resultado del insert
            //si idResult es igual 1 ha tenido exito
            Long result = db.insert(Utilidades.TABLA_EVENT, Utilidades.CAMPO_ID_EVENT, values);
            if (result > 0) {
                Toast.makeText(activity, "Inserted the event", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity, "Error inserted the event", Toast.LENGTH_SHORT).show();
            }
            db.close();
        }else{
            Toast.makeText(context,"Ya existe un evento con este cliente, organizador en esa fecha ",Toast.LENGTH_SHORT).show();
        }
    }
    public void deleteEvento(int id){
        db = getWritableDatabase(); //Crear y / o abrir una base de datos que se utilizará para lectura y escritura.
        /*
            Método de conveniencia para insertar una fila en la base de datos.
            delete(String table, String whereClause, String[] whereArgs)
            devuelve int
         */
        String[] parametros={id+""};

        int result = db.delete(Utilidades.TABLA_EVENT,Utilidades.CAMPO_ID_EVENT+"=?",parametros);
        if(result > 0) {
            Toast.makeText(context, "Delete the event", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Error deleted the event", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }
    public void updateEvento(Evento event) {
        db = getWritableDatabase(); //Crear y / o abrir una base de datos que se utilizará para lectura y escritura.

        String[] parametros={event.getId()+""};
        ContentValues values=new ContentValues();
        //values.put(Utilidades.CAMPO_ID_EVENT,event.getId());
        values.put(Utilidades.CAMPO_NOMBRE_EVENT,event.getNombre());
        values.put(Utilidades.CAMPO_DESCRIPCION_EVENT,event.getDescripcion());
        values.put(Utilidades.CAMPO_TIPO_EVENT,event.getTipoEvento());
        values.put(Utilidades.CAMPO_ID_CLIENTE_EVENT,event.getIdCliente());
        values.put(Utilidades.CAMPO_ID_ORGANIZADOR_EVENTO,event.getIdOrganizador());
        values.put(Utilidades.CAMPO_UBICACION_EVENT,event.getUbicacion());
        values.put(Utilidades.CAMPO_FECHA_EVENT,event.getFecha());
        values.put(Utilidades.CAMPO_DURACION_EVENT,event.getDuracion());
        values.put(Utilidades.CAMPO_PRECIO_EVENT,event.getPrecio());

        int result = db.update(Utilidades.TABLA_USER,values,Utilidades.CAMPO_ID_USER+"=?",parametros);
        if(result > 0) {
            Toast.makeText(context, "Updated the event", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Error updated the event", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }
    public Evento existEventByIDClienteIDOrganizadorFecha(int idCliente, int idOrganizador, String fecha) {
        Evento event = null;

        db = getReadableDatabase();

        //forma 1
         try {
            Cursor cursor=db.rawQuery("SELECT * FROM "+Utilidades.TABLA_EVENT+" WHERE "+Utilidades.CAMPO_ID_CLIENTE_EVENT+" = '"+idCliente+"' AND "+Utilidades.CAMPO_ID_ORGANIZADOR_EVENTO+"='"+idOrganizador+"' AND "+Utilidades.CAMPO_FECHA_EVENT+"='"+fecha+"'",null);
            cursor.moveToFirst();
            event.setId(cursor.getInt(0));
            event.setNombre(cursor.getString(1));
            event.setDescripcion(cursor.getString(2));
            event.setTipoEvento(cursor.getString(3));
            event.setIdCliente(cursor.getInt(4));
            event.setIdOrganizador(cursor.getInt(5));
            event.setUbicacion(cursor.getString(6));
            event.setFecha(cursor.getString(7));
            event.setDuracion(cursor.getString(8));
            event.setPrecio(cursor.getDouble(9));
            cursor.close();
        }catch (Exception e){
            Toast.makeText(context,"Not existed the event",Toast.LENGTH_SHORT).show();
        }
        db.close();

        return event;
    }

    public ArrayList<Evento> listEventsByCliente(int idCliente) {
        //SACA TODAS LOS Eventos
        ArrayList<Evento> listEvents  = new ArrayList<>();

        db = getReadableDatabase();
        String[] parametros={idCliente+""};

        String[] campos={Utilidades.CAMPO_ID_EVENT,Utilidades.CAMPO_NOMBRE_EVENT,Utilidades.CAMPO_DESCRIPCION_EVENT,
                Utilidades.CAMPO_TIPO_EVENT,Utilidades.CAMPO_ID_CLIENTE_EVENT, Utilidades.CAMPO_ID_ORGANIZADOR_EVENTO,
                Utilidades.CAMPO_UBICACION_EVENT,Utilidades.CAMPO_FECHA_EVENT,Utilidades.CAMPO_DURACION_EVENT,Utilidades.CAMPO_PRECIO_EVENT};
        try {
            Cursor cursor =db.query(Utilidades.TABLA_EVENT,campos,Utilidades.CAMPO_ID_CLIENTE_EVENT+"=?",parametros,null,null,null);

            while (cursor.moveToNext()){
                Evento event = new Evento();
                event.setId(cursor.getInt(0));
                event.setNombre(cursor.getString(1));
                event.setDescripcion(cursor.getString(2));
                event.setTipoEvento(cursor.getString(3));
                event.setIdCliente(cursor.getInt(4));
                event.setIdOrganizador(cursor.getInt(5));
                event.setUbicacion(cursor.getString(6));
                event.setFecha(cursor.getString(7));
                event.setDuracion(cursor.getString(8));
                event.setPrecio(cursor.getDouble(9));

                listEvents.add(event);
            }
            cursor.close();
        }catch (Exception e){
            Toast.makeText(context,"Not existed the event",Toast.LENGTH_SHORT).show();
        }
        db.close();

        return listEvents;
    }
    public ArrayList<Evento> listEventsByOrganizador(int idOrganizador) {
        //SACA TODAS LOS Eventos
        ArrayList<Evento> listEvents  = new ArrayList<>();

        db = getReadableDatabase();
        String[] parametros={idOrganizador+""};

        String[] campos={Utilidades.CAMPO_ID_EVENT,Utilidades.CAMPO_NOMBRE_EVENT,Utilidades.CAMPO_DESCRIPCION_EVENT,
                Utilidades.CAMPO_TIPO_EVENT,Utilidades.CAMPO_ID_CLIENTE_EVENT, Utilidades.CAMPO_ID_ORGANIZADOR_EVENTO,
                Utilidades.CAMPO_UBICACION_EVENT,Utilidades.CAMPO_FECHA_EVENT,Utilidades.CAMPO_DURACION_EVENT,Utilidades.CAMPO_PRECIO_EVENT};
        try {
            Cursor cursor =db.query(Utilidades.TABLA_EVENT,campos,Utilidades.CAMPO_ID_ORGANIZADOR_EVENTO+"=?",parametros,null,null,null);

            while (cursor.moveToNext()){
                Evento event = new Evento();
                event.setId(cursor.getInt(0));
                event.setNombre(cursor.getString(1));
                event.setDescripcion(cursor.getString(2));
                event.setTipoEvento(cursor.getString(3));
                event.setIdCliente(cursor.getInt(4));
                event.setIdOrganizador(cursor.getInt(5));
                event.setUbicacion(cursor.getString(6));
                event.setFecha(cursor.getString(7));
                event.setDuracion(cursor.getString(8));
                event.setPrecio(cursor.getDouble(9));

                listEvents.add(event);
            }
            cursor.close();
        }catch (Exception e){
            Toast.makeText(context,"Not existed the event",Toast.LENGTH_SHORT).show();
        }
        db.close();

        return listEvents;
    }
    public ArrayList<Evento> listEvents(){
        //SACA TODAS LOS Eventos
        ArrayList<Evento> listEvents  = new ArrayList<>();

        //Crear y / o abrir una base de datos.
        db = getReadableDatabase();
        //Consulta la tabla dada, devolviendo un cursor sobre el conjunto de resultados.
        Cursor cursor=db.rawQuery("SELECT * FROM "+Utilidades.TABLA_EVENT,null);

        while (cursor.moveToNext()){

            Evento event = new Evento();
            event.setId(cursor.getInt(0));
            event.setNombre(cursor.getString(1));
            event.setDescripcion(cursor.getString(2));
            event.setTipoEvento(cursor.getString(3));
            event.setIdCliente(cursor.getInt(4));
            event.setIdOrganizador(cursor.getInt(5));
            event.setUbicacion(cursor.getString(6));
            event.setFecha(cursor.getString(7));
            event.setDuracion(cursor.getString(8));
            event.setPrecio(cursor.getDouble(9));

            listEvents.add(event);
        }
        cursor.close();
        db.close();
        //comprobamos que el tamaño no sea 0
        if(listEvents.size() == 0){
            listEvents = null;
        }
        return listEvents;
    }
}


