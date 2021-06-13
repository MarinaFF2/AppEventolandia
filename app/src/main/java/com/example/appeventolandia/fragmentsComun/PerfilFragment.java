package com.example.appeventolandia.fragmentsComun;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.example.appeventolandia.ConexionBBDD.ConexionBBDD;
import com.example.appeventolandia.R;
import com.example.appeventolandia.entidades.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.IOException;

import es.dmoral.toasty.Toasty;

public class PerfilFragment extends Fragment {
    private CircularImageView circularImageView;
    private EditText edit_name_perfil;
    private EditText edit_email_perfil;
    private EditText edit_pwd_perfil;
    private FloatingActionButton floatCamaraPerfil;
    private Button button_save_perfil;

    //Constantes
    private static final int GALERIA = 1;
    private static final int CAMARA = 2;

    private Usuario userSesion;
    private String b64;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //identificamos los elementos del xml con java
        circularImageView = (CircularImageView) view.findViewById(R.id.imgFotoPerfil);
        edit_name_perfil = (EditText) view.findViewById(R.id.edit_name_perfil);
        edit_email_perfil = (EditText) view.findViewById(R.id.edit_email_perfil);
        edit_pwd_perfil = (EditText) view.findViewById(R.id.edit_pwd_perfil);
        floatCamaraPerfil = (FloatingActionButton) view.findViewById(R.id.floatCamaraPerfil);
        button_save_perfil = (Button) view.findViewById(R.id.button_save_perfil);

        addUserSesion();//recogemos la userSesion
        addCircularImageView();//añadimos el circulo a la foto de perfil
        addDataPerfil(); //añadimos datos del usuario
        addEventFloat(); //recogemos la nueva foto
        eventButtonSave();//añadimos la opción del evento
    }

    private void addEventFloat() {
        floatCamaraPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoFoto();
            }
        });
    }

    private void addUserSesion(){
        //recogemos la cookie del usuario
        Bundle data = this.getArguments();
        if(data != null){
            userSesion = (Usuario) data.getSerializable("userSesion");
        }
    }

    private void addDataPerfil() {
        //mostramos el nombre y apellidos del usuario
        edit_name_perfil.setText(userSesion.getNombreApellidos());
        //mostramos el correo del usuario
        edit_email_perfil.setText(userSesion.getCorreo());
        //mostramos la imagen del usuario
        b64 = userSesion.getFoto();
        if(b64!=""){
            circularImageView.setImageBitmap(Usuario.gestionarImg(b64));
        }
    }
    /**
     * Se muestra un dialog que da al usuario a elegir entre sacar una foto
     * con la cámara o bien elegirla de la galería
     */
    private void mostrarDialogoFoto() {
        AlertDialog.Builder fotoDialogo = new AlertDialog.Builder(getContext());
        fotoDialogo.setTitle("Elige un método de entrada");
        String[] fotoDialogoItems = {
                "Seleccionar fotografía de galería",
                "Capturar fotografía desde la cámara"};
        fotoDialogo.setItems(fotoDialogoItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        elegirFotoGaleria();
                        break;
                    case 1:
                        tomarFotoCamara();
                        break;
                }
            }
        });
        fotoDialogo.show();
    }
    /**
     * Llamamos a al galería
     */
    public void elegirFotoGaleria() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALERIA);
    }

    /**
     * Llamamos a la cámara
     */
    private void tomarFotoCamara() {
        // Eso para alta o baja
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMARA);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALERIA) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    b64 =  Usuario.bitmapToBase64(bitmap);
                    circularImageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    Snackbar.make(getView(), "Fallo en la galería", Snackbar.LENGTH_LONG).show();
                }
            }
        } else if (requestCode == CAMARA) {
            Bitmap thumbnail = null;
            try {
                thumbnail = (Bitmap) data.getExtras().get("data");
                b64 = Usuario.bitmapToBase64(thumbnail);
                circularImageView.setImageBitmap(thumbnail);
            } catch (Exception e) {
                Snackbar.make(getView(), "Fallo en la cámara", Snackbar.LENGTH_LONG).show();
            }
        }
    }
    private void eventButtonSave() {
        button_save_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //comprobamos que todos los campos esten rellenos
                if(!edit_name_perfil.getText().toString().equals("") && !edit_email_perfil.getText().toString().equals("")){
                    //recogemos los nuevos valores
                    userSesion.setNombreApellidos(edit_name_perfil.getText().toString());
                    userSesion.setCorreo(edit_email_perfil.getText().toString());
                    userSesion.setFoto(b64);

                    //comprobamos que quiera cambiar la comtraseña
                    if(!edit_pwd_perfil.getText().toString().equals("")) {
                        //guardamos la nueva contraseña
                        userSesion.setPwd(Usuario.codificaciónSHA512(edit_pwd_perfil.getText().toString()));
                    }

                    //hacemos la conexión con la BBDD
                    ConexionBBDD connection = new ConexionBBDD(v.getContext(),"bd_events",null,2);
                    connection.updateUser(userSesion);
                }else{//si no estan rellenos sale un mensaje emergente
                    Toasty.info(v.getContext(), "Rellene todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_perfil, container, false);
    }
    private void addCircularImageView() {
        // Set Color
        circularImageView.setCircleColor(Color.WHITE);

        // Set Border
        circularImageView.setBorderWidth(8f);
        circularImageView.setBorderColor(Color.BLACK);
        // or with gradient
        circularImageView.setBorderColorStart(Color.BLACK);
        circularImageView.setBorderColorEnd(Color.GRAY);
        circularImageView.setBorderColorDirection(CircularImageView.GradientDirection.TOP_TO_BOTTOM);

        // Add Shadow with default param
        circularImageView.setShadowEnable(true);
        // or with custom param
        circularImageView.setShadowRadius(5f);
        circularImageView.setShadowColor(Color.GRAY);
        circularImageView.setShadowGravity(CircularImageView.ShadowGravity.CENTER);
    }
}