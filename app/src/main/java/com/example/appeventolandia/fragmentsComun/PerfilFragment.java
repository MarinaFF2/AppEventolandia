package com.example.appeventolandia.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appeventolandia.ConexionBBDD.ConexionBBDD;
import com.example.appeventolandia.R;
import com.example.appeventolandia.entidades.Usuario;
import com.mikhaellopez.circularimageview.CircularImageView;


public class PerfilFragment extends Fragment {
    private CircularImageView circularImageView;
    private EditText edit_name_perfil;
    private EditText edit_email_perfil;
    private EditText edit_pwd_perfil;
    private Button button_save_perfil;
    private Usuario userSesion;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //identificamos los elementos del xml con java
        circularImageView = (CircularImageView) view.findViewById(R.id.imgFotoPerfil);
        edit_name_perfil = (EditText) view.findViewById(R.id.edit_name_perfil);
        edit_email_perfil = (EditText) view.findViewById(R.id.edit_email_perfil);
        edit_pwd_perfil = (EditText) view.findViewById(R.id.edit_pwd_perfil);
        button_save_perfil = (Button) view.findViewById(R.id.button_save_perfil);

        //recogemos la userSesion
        addUserSesion();
        //añadimos el circulo a la foto de perfil
        addCircularImageView();
        //añadimos datos del usuario
        addDataPerfil();
        //añadimos la opción del evento
        eventButtonSave();
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
        //mostramos la contraseña del usuario
        edit_pwd_perfil.setText(userSesion.getPwd());
        //mostramos la imagen del usuario
        circularImageView.setImageResource(userSesion.getFoto());
    }

    private void eventButtonSave() {
        button_save_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSesion.setNombreApellidos(edit_name_perfil.getText().toString());
                userSesion.setCorreo(edit_email_perfil.getText().toString());
                userSesion.setPwd(edit_pwd_perfil.getText().toString());

                //hacemos la conexión con la BBDD
                ConexionBBDD connection = new ConexionBBDD(v.getContext(),"bd_events",null,2);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_perfil, container, false);
    }
    private void addCircularImageView() {
        // Set Color
        circularImageView.setCircleColor(Color.WHITE);

        // Set Border
        circularImageView.setBorderWidth(5f);
        circularImageView.setBorderColor(Color.BLACK);
        // or with gradient
        circularImageView.setBorderColorStart(Color.BLACK);
        circularImageView.setBorderColorEnd(Color.GREEN);
        circularImageView.setBorderColorDirection(CircularImageView.GradientDirection.TOP_TO_BOTTOM);

        // Add Shadow with default param
        circularImageView.setShadowEnable(true);
        // or with custom param
        circularImageView.setShadowRadius(3f);
        circularImageView.setShadowColor(Color.GREEN);
        circularImageView.setShadowGravity(CircularImageView.ShadowGravity.CENTER);
    }
}