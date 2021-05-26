package com.example.appeventolandia.fragments.organizador;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.appeventolandia.ConexionBBDD.ConexionBBDD;
import com.example.appeventolandia.R;
import com.example.appeventolandia.entidades.Evento;
import com.example.appeventolandia.entidades.Usuario;
import java.util.ArrayList;

public class GestionarEventosFragment extends Fragment {
    private Usuario userSesion = null;
    private ConexionBBDD connection;
    private ArrayList<Evento> listEvents;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addUserSesion();//recogemos la userSesion
        addData();//cargamos los eventos que organiza
        //hacemos la conexión con la BBDD
        connection = new ConexionBBDD(view.getContext(),"bd_events",null,2);
    }

    private void addData() {
        //recogemos los eventos que organiza
        listEvents = connection.listEventsByOrganizador(userSesion.getId());
    }
    private void addUserSesion(){
        //recogemos la cookie del usuario
        userSesion = null;
        Bundle data = this.getArguments();
        if(data != null){
            userSesion = (Usuario) data.getSerializable("userSesion");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gestionar_eventos, container, false);
    }
}