package com.example.appeventolandia.entidades;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example.appeventolandia.ConexionBBDD.ConexionBBDD;
import com.example.appeventolandia.R;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Usuario implements Serializable {

    /***Atributos****/
    private int id;
    private String nombreApellidos;
    private String correo;
    private String pwd;
    private int idRol;
    private String foto;

    /***Constructor****/
    public Usuario() {}
    public Usuario( String nombreApellidos, String correo, String pwd, int idRol, String foto) {
        this.nombreApellidos = nombreApellidos;
        this.correo = correo;
        this.pwd = pwd;
        this.idRol = idRol;
        this.foto = foto;
    }
    public Usuario(int id, String nombreApellidos, String correo, String pwd, int idRol, String foto) {
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
    public String getFoto() {
        return foto;
    }
    public void setFoto(String foto) {
        this.foto = foto;
    }

    /***Metodos de la clase****/

    public static ArrayList<Usuario> list_user(){
        String img = "";
        String codPwd = codificaciónSHA512("1");
        ArrayList<Usuario> listUser = new ArrayList<>();
        listUser.add(new Usuario("Javier Diaz","jd@gmail.com",codPwd,0, img));
        listUser.add(new Usuario("Francisco Diaz","fd@gmail.com",codPwd,1,img));
        listUser.add(new Usuario("Carmen Diaz","cd@gmail.com",codPwd,2, img));
        listUser.add(new Usuario("María Diaz","md@gmail.com",codPwd,3, img));
        listUser.add(new Usuario("cliente","client@gmail.com",codPwd,0, img));
        listUser.add(new Usuario("organizador","org@gmail.com",codPwd,1, img));
        listUser.add(new Usuario("administrador","admin@gmail.com",codPwd,2, img));
        listUser.add(new Usuario("admin-organizador","admin-org@gmail.com",codPwd,3, img));

        return listUser;
    }
    public static void insertUsuarioIniciales(Activity activity){
        ArrayList<Usuario> listUser = list_user();
        ConexionBBDD connection = new ConexionBBDD(activity,"bd_events",null,2);
        for (Usuario user : listUser) {
            connection.insertUser(user);
        }
    }
    public String rolByID(){
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
    public static int rolByNombre(String rol){
        int idRol = -1;
        switch (rol){
            case "Cliente":
                idRol = 0;
                break;
            case "Organizador":
                idRol = 1;
                break;
            case "Administrador":
                idRol = 2;
                break;
            case "Administrador - Organizador":
                idRol = 3;
                break;
        }
        return idRol;
    }
    public static String codificaciónSHA512(String contraseña){
        String pwdCodificada = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] digest = md.digest(contraseña.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < digest.length; i++) {
                sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
            }
            pwdCodificada = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return pwdCodificada;
    }
    /**
     * Método para la imagen que sacamos codificada de la BBDD
     */
    public static Bitmap gestionarImg(String img){
        Bitmap imagenBitmap = base64ToBitmap(img);
        float proporcion = 600 / (float) imagenBitmap.getWidth();
        return Bitmap.createScaledBitmap(imagenBitmap, 600, (int) (imagenBitmap.getHeight() * proporcion), false);
    }
    /**
     * Para convertir base64 en bitmap
     */
    private static Bitmap base64ToBitmap(String imgCodificada) {
        byte[] imageAsBytes = Base64.decode(imgCodificada.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }
    /**
     * Para convertir bitmap en base64
     */
    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        byte[] byteArray = stream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
    @Override
    public String toString() {
        return "Id=: " + id + "\n" +
                "Nombre: " + nombreApellidos;
    }
}
