package com.example.appeventolandia.ConexionBBDD;

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

import es.dmoral.toasty.Toasty;

public class ConexionBBDD extends SQLiteOpenHelper {
    //declaro la variable para poder interactuar con la bbdd
    private SQLiteDatabase db;
    private Context context;

    /**
     * Contructor de la clase
     * @param context
     * @param name
     * @param factory
     * @param version
     */
    public ConexionBBDD(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        //guardamos el contexto
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

    /**
     * metodo para eliminar el usuario
     * @param id -> del usuario para eliminiar
     * @return int -> devuelve un entero que indica si se ha hecho bien o no
     */
    public int deleteUser(int id){
        db = getWritableDatabase(); //Crear y / o abrir una base de datos que se utilizará para lectura y escritura.
        //declaramos los parametros de la sql
        String[] parametros={id+""};
        //eliminamos el usuario
        int result = db.delete(Utilidades.TABLA_USER,Utilidades.CAMPO_ID_USER+"=?",parametros);
        //comprobamos si ha habido error
        if(result > 0) {
            //Mensaje de exito
            Toasty.success(context, "Delete the user", Toast.LENGTH_SHORT).show();
        }else{
            //mensaje de error
            Toasty.error(context, "Error deleted the user", Toast.LENGTH_SHORT).show();
        }
        //cerramos la escritura
        db.close();
        //devolvemos si hay error
        return result;
    }

    /**
     * metodo para actualizar un usuario
     * @param user -> usuario a actulizar
     * @return int -> devuelve un entero que indica si se ha hecho bien o no
     */
    public int updateUser(Usuario user) {
        db = getWritableDatabase(); //Crear y / o abrir una base de datos que se utilizará para lectura y escritura.
        //declaramos los parametros de la sql
        String[] parametros={user.getId()+""};
        //valores a actualizar
        ContentValues values=new ContentValues();
        values.put(Utilidades.CAMPO_NOMBRE_APELLIDOS_USER,user.getNombreApellidos());
        values.put(Utilidades.CAMPO_CORREO_USER,user.getCorreo());
        values.put(Utilidades.CAMPO_PWD_USER,user.getPwd());
        values.put(Utilidades.CAMPO_ID_ROL_USER,user.getIdRol());
        values.put(Utilidades.CAMPO_FOTO_USER,user.getFoto());
        //modificamos el usuario
        int result = db.update(Utilidades.TABLA_USER,values,Utilidades.CAMPO_ID_USER+"=?",parametros);
        //comprobamos si ha habido error
        if(result > 0) {
            //Mensaje de exito
            Toasty.success(context, "Updated the user", Toast.LENGTH_SHORT).show();
        }else{
            //mensaje de error
            Toasty.error(context, "Error updated the user", Toast.LENGTH_SHORT).show();
        }

        //cerramos la escritura
        db.close();
        //devolvemos si hay error
        return result;
    }

    /**
     * metodo para insertar un usuario
     * @param user -> usuario a insertar
     * @return int -> devuelve un entero que indica si se ha hecho bien o no
     */
    public Long insertUser(Usuario user) {
        Long result = Long.valueOf(0);
        //antes de insertar comprobamos que el correo no se repìta
        if(existUserByCorreo(user.getCorreo())==null){
            db = this.getWritableDatabase();//Crear y / o abrir una base de datos que se utilizará para lectura y escritura.

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
            result = db.insert(Utilidades.TABLA_USER, Utilidades.CAMPO_ID_USER, values);
            //comprobamos si ha habido error
            if(result > 0) {
                //Mensaje de exito
                Toasty.success(context, "Inserted the user", Toast.LENGTH_SHORT).show();
            }else{
                //mensaje de error
                Toasty.error(context, "Error inserted the user", Toast.LENGTH_SHORT).show();
            }
            //cerramos la escritura
            db.close();
        }else{
            //mensaje de información
            Toasty.info(context,"Ya existe un usuario con este correo ",Toast.LENGTH_SHORT).show();
        }
        //devolvemos si hay error
        return result;
    }

    /**
     * metodo para comprobar si existe un usuario con dicho correo o pwd
     * @param correo -> a comprobar
     * @param pwd -> a comprobar
     * @return Usuario
     */
    public Usuario existUserByCorreoPwd(String correo,String pwd){
        db = getReadableDatabase();//Crear y / o abrir una base de datos que se utilizará para lectura y escritura.
        //declaramos los parametros de la sql
        String[] parametros={correo,pwd};
        //declaramos el usuario
        Usuario user = new Usuario();
        //declaramos los campos de la sql
        String[] campos={Utilidades.CAMPO_ID_USER,Utilidades.CAMPO_NOMBRE_APELLIDOS_USER,Utilidades.CAMPO_CORREO_USER,Utilidades.CAMPO_PWD_USER,Utilidades.CAMPO_ID_ROL_USER,Utilidades.CAMPO_FOTO_USER};
        try {
            //Consulta la tabla dada, devolviendo un cursor sobre el conjunto de resultados.
            Cursor cursor =db.query(Utilidades.TABLA_USER,campos,Utilidades.CAMPO_CORREO_USER+"=? AND "+Utilidades.CAMPO_PWD_USER+"=?",parametros,null,null,null);
            cursor.moveToFirst();
            //guardamos el usuario
            user.setId(cursor.getInt(0));
            user.setNombreApellidos(cursor.getString(1));
            user.setCorreo(cursor.getString(2));
            user.setPwd(cursor.getString(3));
            user.setIdRol(cursor.getInt(4));
            user.setFoto(cursor.getString(5));

            //cerramos el cursor
            cursor.close();
        }catch (Exception e){
            // Mensaje de error
            user = null;
        }
        //cerramos la lectura
        db.close();
        //devolvemos usuario
        return user;
    }

    /**
     * metodo para comprobar si existe un usuario con dicho correo
     * @param correo -> a comprobar
     * @return Usuario
     */
    public Usuario existUserByCorreo(String correo){
        //declaramos e inicializamos la clase Usuario
        Usuario user = new Usuario();

        db = getReadableDatabase();//Crear y / o abrir una base de datos que se utilizará para lectura y escritura.
        //declaramos los parametros de la sql
        String[] parametros={correo};
        //declaramos los campos de la sql
        String[] campos={Utilidades.CAMPO_ID_USER,Utilidades.CAMPO_NOMBRE_APELLIDOS_USER,Utilidades.CAMPO_CORREO_USER,Utilidades.CAMPO_PWD_USER,Utilidades.CAMPO_ID_ROL_USER,Utilidades.CAMPO_FOTO_USER};
        try {
            //Consulta la tabla dada, devolviendo un cursor sobre el conjunto de resultados.
            Cursor cursor =db.query(Utilidades.TABLA_USER,campos,Utilidades.CAMPO_CORREO_USER+"=?",parametros,null,null,null);
            cursor.moveToFirst();
            //guardamos el usuario
            user.setId(cursor.getInt(0));
            user.setNombreApellidos(cursor.getString(1));
            user.setCorreo(cursor.getString(2));
            user.setPwd(cursor.getString(3));
            user.setIdRol(cursor.getInt(4));
            user.setFoto(cursor.getString(5));

            //mostramos mensaje
            Toasty.info(context,"Existed the user",Toast.LENGTH_SHORT).show();

            //cerramos el cursor
            cursor.close();
        }catch (Exception e){
            // Mensaje de error
            Toasty.info(context,"Not existed the user",Toast.LENGTH_SHORT).show();
            user = null;
        }
        //cerramos la lectura
        db.close();
        //devolvemos usuario
        return user;
    }

    /**
     * metodo para comprobar si existe un usuario con dicho id
     * @param id -> a comprobar
     * @return Usuario
     */
    public Usuario existUserById(int id){
        db = getReadableDatabase();//Crear y / o abrir una base de datos que se utilizará para lectura y escritura.

        //declaramos e inicializamos la clase Usuario
        Usuario user = new Usuario();
        //declaramos los parametros de la sql
        String[] parametros={id+""};
        //declaramos los campos de la sql
        String[] campos={Utilidades.CAMPO_ID_USER,Utilidades.CAMPO_NOMBRE_APELLIDOS_USER,Utilidades.CAMPO_CORREO_USER,Utilidades.CAMPO_PWD_USER,Utilidades.CAMPO_ID_ROL_USER,Utilidades.CAMPO_FOTO_USER};
        try {
            //Consulta la tabla dada, devolviendo un cursor sobre el conjunto de resultados.
            Cursor cursor =db.query(Utilidades.TABLA_USER,campos,Utilidades.CAMPO_ID_USER+"=?",parametros,null,null,null);
            cursor.moveToFirst();
            //guardamos el usuario
            user.setId(cursor.getInt(0));
            user.setNombreApellidos(cursor.getString(1));
            user.setCorreo(cursor.getString(2));
            user.setPwd(cursor.getString(3));
            user.setIdRol(cursor.getInt(4));
            user.setFoto(cursor.getString(5));

            //cerramos el cursor
            cursor.close();
        }catch (Exception e){
            // Mensaje de error
            Toasty.info(context,"Not existed the user",Toast.LENGTH_SHORT).show();
            user = null;
        }
        //cerramos la lectura
        db.close();
        //devolvemos usuario
        return user;
    }

    /**
     * metodo para obtener todos los usuarios
     * @return ArrayList<Usuario>
     */
    public ArrayList<Usuario> listUsers(){
        //declaramos e inicializamos la ArrayList<Usuario>
        ArrayList<Usuario> listUsers  = new ArrayList<>();

        //Crear y / o abrir una base de datos.
        db = getReadableDatabase();//Crear y / o abrir una base de datos que se utilizará para lectura y escritura.
        //declaramos los campos de la sql
        String[] campos={Utilidades.CAMPO_ID_EVENT,Utilidades.CAMPO_NOMBRE_EVENT,Utilidades.CAMPO_DESCRIPCION_EVENT,
                Utilidades.CAMPO_TIPO_EVENT,Utilidades.CAMPO_ID_CLIENTE_EVENT, Utilidades.CAMPO_ID_ORGANIZADOR_EVENTO,
                Utilidades.CAMPO_UBICACION_EVENT,Utilidades.CAMPO_FECHA_EVENT,Utilidades.CAMPO_DURACION_EVENT,Utilidades.CAMPO_PRECIO_EVENT};

        //Consulta la tabla dada, devolviendo un cursor sobre el conjunto de resultados.
        Cursor cursor =db.query(Utilidades.TABLA_EVENT,campos,null,null,null,null,null);
        //recorremos el cursor
        while (cursor.moveToNext()){
            //guardamos el usuario
            Usuario user = new Usuario();
            user.setId(cursor.getInt(0));
            user.setNombreApellidos(cursor.getString(1));
            user.setCorreo(cursor.getString(2));
            user.setPwd(cursor.getString(3));
            user.setIdRol(cursor.getInt(4));
            user.setFoto(cursor.getString(5));
            //añadimos el usuario
            listUsers.add(user);
        }
        //comprobamos que el tamaño no sea 0
        if(listUsers.size() == 0){
            listUsers = null;
        }

        //cerramos el cursor
        cursor.close();
        //cerramos la lectura
        db.close();
        //devolvemos lista de usuario
        return listUsers;
    }

    /**
     * metodo para obtener todos los administradores
     * @return ArrayList<Usuario>
     */
    public ArrayList<Usuario> listUsuariosByAdministrador() {
        //declaramos e inicializamos la ArrayList<Usuario>
        ArrayList<Usuario> listUsers  = new ArrayList<>();

        db = getReadableDatabase();//Crear y / o abrir una base de datos que se utilizará para lectura y escritura.

        try {
            //Consulta la tabla dada, devolviendo un cursor sobre el conjunto de resultados.
            Cursor cursor=db.rawQuery("SELECT * FROM "+Utilidades.TABLA_USER+" WHERE "+Utilidades.CAMPO_ID_ROL_USER+" = 2",null);
            //comprobamos si hay eventos
            if (cursor.moveToFirst()){
                //recogemos todos los eventos
                do {
                    //guardamos los datos en el evento
                    Usuario user = new Usuario();
                    user.setId(cursor.getInt(0));
                    user.setNombreApellidos(cursor.getString(1));
                    user.setCorreo(cursor.getString(2));
                    user.setPwd(cursor.getString(3));
                    user.setIdRol(cursor.getInt(4));
                    user.setFoto(cursor.getString(5));

                    //añadimos usuario a la arraylist
                    listUsers.add(user);

                } while(cursor.moveToNext());
            }
            //cerramos el cursor
            cursor.close();
        }catch (Exception e){
            // Mensaje de error
            Toasty.info(context,"Not existed the user",Toast.LENGTH_SHORT).show();
        }
        //cerramos la lectura
        db.close();

        //devolvemos el arraylist
        return listUsers;
    }

    /**
     * metodo para obtener todos los clientes
     * @return ArrayList<Usuario>
     */
    public ArrayList<Usuario> listUsuariosByCliente() {
        //declaramos e inicializamos la ArrayList<Usuario>
        ArrayList<Usuario> listUsers  = new ArrayList<>();

        db = getReadableDatabase();//Crear y / o abrir una base de datos que se utilizará para lectura y escritura.

        try {
            //Consulta la tabla dada, devolviendo un cursor sobre el conjunto de resultados.
            Cursor cursor=db.rawQuery("SELECT * FROM "+Utilidades.TABLA_USER+" WHERE "+Utilidades.CAMPO_ID_ROL_USER+" = 0",null);
            //comprobamos si hay eventos
            if (cursor.moveToFirst()){
                //recogemos todos los eventos
                do {
                    //guardamos los datos en el evento
                    Usuario user = new Usuario();
                    user.setId(cursor.getInt(0));
                    user.setNombreApellidos(cursor.getString(1));
                    user.setCorreo(cursor.getString(2));
                    user.setPwd(cursor.getString(3));
                    user.setIdRol(cursor.getInt(4));
                    user.setFoto(cursor.getString(5));

                    //añadimos usuario a la arraylist
                    listUsers.add(user);

                } while(cursor.moveToNext());
            }
            //cerramos el cursor
            cursor.close();
        }catch (Exception e){
            // Mensaje de error
            Toasty.info(context,"Not existed the user",Toast.LENGTH_SHORT).show();
        }
        //cerramos lectura
        db.close();

        //devolvemos el arraylist
        return listUsers;
    }

    /**
     * metodo para obtener todos los org-admin
     * @return ArrayList<Usuario>
     */
    public ArrayList<Usuario> listUsuariosByOrgaAdmin() {
        //declaramos e inicializamos la ArrayList<Usuario>
        ArrayList<Usuario> listUsers  = new ArrayList<>();

        db = getReadableDatabase();//Crear y / o abrir una base de datos que se utilizará para lectura y escritura.

        try {
            //Consulta la tabla dada, devolviendo un cursor sobre el conjunto de resultados.
            Cursor cursor=db.rawQuery("SELECT * FROM "+Utilidades.TABLA_USER+" WHERE "+Utilidades.CAMPO_ID_ROL_USER+" = 3",null);
            //comprobamos si hay eventos
            if (cursor.moveToFirst()){
                //recogemos todos los eventos
                do {
                    //guardamos los datos en el evento
                    Usuario user = new Usuario();
                    user.setId(cursor.getInt(0));
                    user.setNombreApellidos(cursor.getString(1));
                    user.setCorreo(cursor.getString(2));
                    user.setPwd(cursor.getString(3));
                    user.setIdRol(cursor.getInt(4));
                    user.setFoto(cursor.getString(5));

                    //añadimos usuario a la arraylist
                    listUsers.add(user);

                } while(cursor.moveToNext());
            }
            //cerramos el cursor
            cursor.close();
        }catch (Exception e){
            // Mensaje de error
            Toasty.info(context,"Not existed the user",Toast.LENGTH_SHORT).show();
        }
        //cerramos lectura
        db.close();

        //devolvemos el arraylist
        return listUsers;
    }

    /**
     * metodo para obtener organizador y admin-orga para rellenar el spinner
     * @return
     */
    public ArrayList<Usuario> spinnerByOrganizador(){
        //declaramos e inicializamos la ArrayList<Usuario>
        ArrayList<Usuario> listUsers  = new ArrayList<>();

        db = getReadableDatabase();//Crear y / o abrir una base de datos que se utilizará para lectura y escritura.

        try {
            //Consulta la tabla dada, devolviendo un cursor sobre el conjunto de resultados.
            Cursor cursor=db.rawQuery("SELECT * FROM "+Utilidades.TABLA_USER+" WHERE "+Utilidades.CAMPO_ID_ROL_USER+" = 1  OR  "+Utilidades.CAMPO_ID_ROL_USER+" = 3",null);
            //comprobamos si hay eventos
            if (cursor.moveToFirst()){
                //recogemos todos los eventos
                do {
                    //guardamos los datos en el evento
                    Usuario user = new Usuario();
                    user.setId(cursor.getInt(0));
                    user.setNombreApellidos(cursor.getString(1));
                    user.setCorreo(cursor.getString(2));
                    user.setPwd(cursor.getString(3));
                    user.setIdRol(cursor.getInt(4));
                    user.setFoto(cursor.getString(5));

                    //añadimos usuario a la arraylist
                    listUsers.add(user);
                } while(cursor.moveToNext());
            }
            //cerramos el cursor
            cursor.close();
        }catch (Exception e){
            // Mensaje de error
            Toasty.info(context,"Not existed the user",Toast.LENGTH_SHORT).show();
        }
        //cerramos lectura
        db.close();

        //devolvemos si arraylist
        return listUsers;
    }
    /**
     * metodo para obtener los organizadores
     * @return ArrayList<Usuario>
     */
    public ArrayList<Usuario> listUsuariosByOrganizador() {
        //declaramos e inicializamos la ArrayList<Usuario>
        ArrayList<Usuario> listUsers  = new ArrayList<>();

        db = getReadableDatabase();//Crear y / o abrir una base de datos que se utilizará para lectura y escritura.

        try {
            //Consulta la tabla dada, devolviendo un cursor sobre el conjunto de resultados.
            Cursor cursor=db.rawQuery("SELECT * FROM "+Utilidades.TABLA_USER+" WHERE "+Utilidades.CAMPO_ID_ROL_USER+" = 1 ",null);
            //comprobamos si hay eventos
            if (cursor.moveToFirst()){
                //recogemos todos los eventos
                do {
                    //guardamos los datos en el evento
                    Usuario user = new Usuario();
                    user.setId(cursor.getInt(0));
                    user.setNombreApellidos(cursor.getString(1));
                    user.setCorreo(cursor.getString(2));
                    user.setPwd(cursor.getString(3));
                    user.setIdRol(cursor.getInt(4));
                    user.setFoto(cursor.getString(5));

                    //añadimos usuario a la arraylist
                    listUsers.add(user);
                } while(cursor.moveToNext());
            }
            //cerramos el cursor
            cursor.close();
        }catch (Exception e){
            // Mensaje de error
            Toasty.info(context,"Not existed the user",Toast.LENGTH_SHORT).show();
        }
        //cerramos lectura
        db.close();

        //devolvemos si arraylist
        return listUsers;
    }

    /**
     * metodo para insertar un evento
     * @param event -> evento a insertar
     * @return int -> devuelve si hay algún error
     */
    public Long insertEvento(Evento event) {
        Long result = Long.valueOf(0);
        //antes de insertar comprobamos que el correo no se repìta
        if(existEventByIDClienteIDOrganizadorFecha(event.getIdCliente(),event.getIdOrganizador(),event.getFecha()) == null) {
            db = getWritableDatabase();//Crear y / o abrir una base de datos que se utilizará para lectura y escritura.

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
            result = db.insert(Utilidades.TABLA_EVENT, Utilidades.CAMPO_ID_EVENT, values);
            //comprobamos si hay algún error
            if (result > 0) {
                //mensaje de exito
                Toasty.success(context, "Inserted the event", Toast.LENGTH_SHORT).show();
            } else {
                //mensaje de error
                Toasty.error(context, "Error inserted the event", Toast.LENGTH_SHORT).show();
            }
            //cerramos escritura
            db.close();
        }else{
            //mensaje de informativo
            Toasty.info(context,"Ya existe un evento con este cliente, organizador en esa fecha ",Toast.LENGTH_SHORT).show();
        }
        //devolvemos si hay error
        return result;
    }

    /**
     * metodo para eliminar eventos
     * @param id -> del evento a eliminar
     * @return  int -> devuelve si hay algún error
     */
    public int deleteEvento(int id){
        db = getWritableDatabase(); //Crear y / o abrir una base de datos que se utilizará para lectura y escritura.
        //declaramos los parametros de la sql
        String[] parametros={id+""};
        //eliminamos el evento
        int result = db.delete(Utilidades.TABLA_EVENT,Utilidades.CAMPO_ID_EVENT+"=?",parametros);
        //comprobamos si hay algún error
        if(result > 0) {
            //mensaje de exito
            Toasty.success(context, "Delete the event", Toast.LENGTH_SHORT).show();
        }else{
            //mensaje de error
            Toasty.error(context, "Error deleted the event", Toast.LENGTH_SHORT).show();
        }
        //cerramos escritura
        db.close();
        //devolvemos si hay error
        return result;
    }

    /**
     * metodo para actualizar eventos
     * @param event -> evento a actualizar
     * @return int -> devuelve si hay algún error
     */
    public int updateEvento(Evento event) {
        db = getWritableDatabase(); //Crear y / o abrir una base de datos que se utilizará para lectura y escritura.
        //declaramos los parametros de la sql
        String[] parametros={event.getId()+""};
        //valores a actualizar
        ContentValues values=new ContentValues();
        values.put(Utilidades.CAMPO_NOMBRE_EVENT,event.getNombre());
        values.put(Utilidades.CAMPO_DESCRIPCION_EVENT,event.getDescripcion());
        values.put(Utilidades.CAMPO_TIPO_EVENT,event.getTipoEvento());
        values.put(Utilidades.CAMPO_ID_CLIENTE_EVENT,event.getIdCliente());
        values.put(Utilidades.CAMPO_ID_ORGANIZADOR_EVENTO,event.getIdOrganizador());
        values.put(Utilidades.CAMPO_UBICACION_EVENT,event.getUbicacion());
        values.put(Utilidades.CAMPO_FECHA_EVENT,event.getFecha());
        values.put(Utilidades.CAMPO_DURACION_EVENT,event.getDuracion());
        values.put(Utilidades.CAMPO_PRECIO_EVENT,event.getPrecio());
        //hacemos la inserción
        int result = db.update(Utilidades.TABLA_EVENT,values,Utilidades.CAMPO_ID_EVENT+"= ? ",parametros);
        //comprobamos si ha habido un error
        if(result > 0) {
            //mensaje con exito
            Toasty.success(context, "Updated the event", Toast.LENGTH_SHORT).show();
        }else{
            //mensaje de error
            Toasty.error(context, "Error updated the event", Toast.LENGTH_SHORT).show();
        }
        //cerramos escritura
        db.close();
        //devolvemos si hay error
        return result;
    }

    /**
     * metodo para comprobar si existe un evento con un msimo client, organizador y fecha
     * para no duplicar datos
     * @param idCliente -> id del cliente de los eventos
     * @param idOrganizador
     * @param fecha -> id del organizador de los eventos
     * @return evento
     */
    public Evento existEventByIDClienteIDOrganizadorFecha(int idCliente, int idOrganizador, String fecha) {
        //declaramos evento
        Evento event = null;

        db = getReadableDatabase();//Crear y / o abrir una base de datos que se utilizará para lectura y escritura.

         try {
             //Consulta la tabla dada, devolviendo un cursor sobre el conjunto de resultados.
            Cursor cursor=db.rawQuery("SELECT * FROM "+Utilidades.TABLA_EVENT+" WHERE "+Utilidades.CAMPO_ID_CLIENTE_EVENT+" = '"+idCliente+"' AND "+Utilidades.CAMPO_ID_ORGANIZADOR_EVENTO+"='"+idOrganizador+"' AND "+Utilidades.CAMPO_FECHA_EVENT+"='"+fecha+"'",null);
            cursor.moveToFirst();
            //guardamos evento
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

             //cerramos el cursor
             cursor.close();
         }catch (Exception e){
             // Mensaje de error
             Toasty.info(context,"Not existed the event",Toast.LENGTH_SHORT).show();
        }
        //cerramos lectura
        db.close();
        //devolvemos evento
        return event;
    }

    /**
     * metodo para obtener el arraylist de los eventos del cliente
     * @param idCliente -> id del cliente de los eventos
     * @return ArrayList<Evento>
     */
    public ArrayList<Evento> listEventsByCliente(int idCliente) {
        //SACA TODAS LOS Eventos
        ArrayList<Evento> listEvents  = new ArrayList<>();

        db = getReadableDatabase();//Crear y / o abrir una base de datos que se utilizará para lectura y escritura.

        try {
            //Consulta la tabla dada, devolviendo un cursor sobre el conjunto de resultados.
            Cursor cursor=db.rawQuery("SELECT * FROM "+Utilidades.TABLA_EVENT+" WHERE "+Utilidades.CAMPO_ID_CLIENTE_EVENT+" = "+idCliente,null);
            //comprobamos si hay eventos
            if (cursor.moveToFirst()){
                //recogemos todos los eventos
                do {
                    //guardamos los datos en el evento
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

                    //añadimos los eventos al arraylist
                    listEvents.add(event);
                } while(cursor.moveToNext());
            }
            //cerramos el cursor
            cursor.close();
        }catch (Exception e){
            // Mensaje de error
            Toasty.info(context,"Not existed the event",Toast.LENGTH_SHORT).show();
        }
        //cerramos lectura
        db.close();

        //devolvemos el arraylist
        return listEvents;
    }

    /**
     * metodo para obtener el arraylist de los eventos del organizador
     * @param idOrganizador -> id del organizador de los eventos
     * @return ArrayList<Evento>
     */
    public ArrayList<Evento> listEventsByOrganizador(int idOrganizador) {
        //declaramos el ArrayList del evento de un organizador
        ArrayList<Evento> listEvents  = new ArrayList<>();

        db = getReadableDatabase();//Crear y / o abrir una base de datos que se utilizará para lectura y escritura.

        try {
            //Consulta la tabla dada, devolviendo un cursor sobre el conjunto de resultados.
            Cursor cursor=db.rawQuery("SELECT * FROM "+Utilidades.TABLA_EVENT+" WHERE "+Utilidades.CAMPO_ID_ORGANIZADOR_EVENTO+" = "+idOrganizador,null);
            //comprobamos si hay eventos
            if (cursor.moveToFirst()){
                //recogemos todos los eventos
                do {
                    //guardamos los datos en el evento
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

                    //añadimos los eventos al arraylist
                    listEvents.add(event);
                } while(cursor.moveToNext());
            }
            //cerramos el cursor
            cursor.close();
        }catch (Exception e){
            // Mensaje de error
            Toasty.info(context,"Not existed the event",Toast.LENGTH_SHORT).show();
        }
        //cerramos lectura
        db.close();

        //devolvemos el arraylist
        return listEvents;
    }

    /**
     * metodo para obtener todos los eventos
     * @return ArrayList<Evento>
     */
    public ArrayList<Evento> listEvents(){
        //SACA TODAS LOS Eventos
        ArrayList<Evento> listEvents  = new ArrayList<>();

        //Crear y / o abrir una base de datos.
        db = getReadableDatabase();

        try {
            //Consulta la tabla dada, devolviendo un cursor sobre el conjunto de resultados.
            Cursor cursor=db.rawQuery("SELECT * FROM "+Utilidades.TABLA_EVENT,null);
            //comprobamos si hay eventos
            if (cursor.moveToFirst()){
                //recogemos todos los eventos
                do {
                    //guardamos los datos en el evento
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

                    //añadimos los eventos al arraylist
                    listEvents.add(event);
                } while(cursor.moveToNext());
            }
            //cerramos el cursor
            cursor.close();
        }catch (Exception e){
           // Mensaje de error
            Toasty.info(context,"Not existed the event",Toast.LENGTH_SHORT).show();
        }
        //cerramos lectura
        db.close();
        //comprobamos que el tamaño no sea 0
        if(listEvents.size() == 0){
            listEvents = null;
        }
        //devolvemos el arraylist
        return listEvents;
    }
}


