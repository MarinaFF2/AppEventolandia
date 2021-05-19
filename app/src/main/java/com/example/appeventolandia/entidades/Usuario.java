package com.example.appeventolandia.entidades;

import android.app.Activity;
import com.example.appeventolandia.ConexionBBDD.ConexionBBDD;
import com.example.appeventolandia.InicioSesionActivity;
import com.example.appeventolandia.R;
import java.io.Serializable;
import java.util.ArrayList;

public class Usuario implements Serializable {

    /***Atributos****/
    private int id;
    private String nombreApellidos;
    private String correo;
    private String pwd;
    private int idRol;
    private int foto;

    /***Constructor****/
    public Usuario() {
    }
    public Usuario( String nombreApellidos, String correo, String pwd, int idRol, int foto) {
        this.nombreApellidos = nombreApellidos;
        this.correo = correo;
        this.pwd = pwd;
        this.idRol = idRol;
        this.foto = foto;
    }
    public Usuario(int id, String nombreApellidos, String correo, String pwd, int idRol, int foto) {
        this.id = id;
        this.nombreApellidos = nombreApellidos;
        this.correo = correo;
        this.pwd = pwd;
        this.idRol = idRol;
        this.foto = foto;
    }

    /***Getters && Setters****/
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreApellidos() {
        return nombreApellidos;
    }

    public void setNombreApellidos(String nombreApellidos) {this.nombreApellidos = nombreApellidos; }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }

    public static ArrayList<Usuario> list_user(){
        ArrayList<Usuario> listUser = new ArrayList<>();
        listUser.add(new Usuario("Javier Diaz","jd@gmail.com","1",0, R.drawable.user));
        listUser.add(new Usuario("Francisco Diaz","fd@gmail.com","1",1, R.drawable.user));
        listUser.add(new Usuario("Carmen Diaz","cd@gmail.com","1",2, R.drawable.user));
        listUser.add(new Usuario("María Diaz","md@gmail.com","1",3, R.drawable.user));
        listUser.add(new Usuario("cliente","client@gmail.com","1",0, R.drawable.user));
        listUser.add(new Usuario("organizador","org@gmail.com","1",1, R.drawable.user));
        listUser.add(new Usuario("administrador","admin@gmail.com","1",2, R.drawable.user));
        listUser.add(new Usuario("admin-organizador","admin-org@gmail.com","1",3, R.drawable.user));

        return listUser;
    }
    /***Metodos de la clase****/
    public static void insertUsuarioIniciales(Activity activity){
        ArrayList<Usuario> listUser = list_user();
        ConexionBBDD connection = new ConexionBBDD(activity,"bd_events",null,2);
        for (Usuario user : listUser) {
            connection.insertUser(user,activity);
        }
    }
    public String rol(){
        String rol = null;
        switch (this.idRol){
            case 0:
                rol = "cliente";
                break;
            case 1:
                rol = "organizador";
                break;
            case 2:
                rol = "administrador";
                break;
            case 3:
                rol = "admin-organizador";
                break;
        }
        return rol;
    }
    @Override
    public String toString() {
        return "Id=: " + id + "\n" +
                "Nombre y Apellidos: '" + nombreApellidos+"'"+ "\n" +
                "Rol: '" + rol()+"'";
    }
}
