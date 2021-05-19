package com.example.appeventolandia.fragments.cliente;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.appeventolandia.ConexionBBDD.ConexionBBDD;
import com.example.appeventolandia.R;
import com.example.appeventolandia.entidades.Evento;
import com.example.appeventolandia.entidades.Usuario;
import java.util.ArrayList;

public class VisualizarEventFragment extends Fragment {
    private Usuario userSesion;
    private ConexionBBDD connection;
    private ArrayList<Evento> listEvents;
    private CalendarView calendario;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //hacemos la conexión con la BBDD
        connection = new ConexionBBDD(view.getContext(),"bd_events",null,2);
        //declaramos el calendario
        calendario = (CalendarView) view.findViewById(R.id.calendarView_VisualizarEventFragment);

        addUserSesion();//recogemos la userSesion
        addData();//cargamos los eventos que organiza
    }

    private void addData() {
        int id = userSesion.getId();
        //recogemos los eventos del cliente
        listEvents = connection.listEventsByCliente(id);
    }
    private void addUserSesion(){
        //recogemos la cookie del usuario
        Bundle data = this.getArguments();
        if(data != null){
            userSesion = (Usuario) data.getSerializable("userSesion");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_visualizar_event, container, false);
    }
}