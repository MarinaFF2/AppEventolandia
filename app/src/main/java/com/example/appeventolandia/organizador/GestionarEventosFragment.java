package com.example.appeventolandia.organizador;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appeventolandia.ConexionBBDD.ConexionBBDD;
import com.example.appeventolandia.R;
import com.example.appeventolandia.adaptadores.EventosGestionarCardViewAdapter;
import com.example.appeventolandia.entidades.Evento;
import com.example.appeventolandia.entidades.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class GestionarEventosFragment extends Fragment {
    //variables necesarias para la clase
    private Usuario userSesion;
    private ArrayList<Evento> listEvents;
    private RecyclerView rvListEvents;

    /**
     * llamamos al metodo onCreate
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * metodo para crear la vista
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //declaracion del elemento
        rvListEvents = (RecyclerView) view.findViewById(R.id.rvListGestinarEvents);

        addUserSesion();//recogemos la userSesion
        addNewEvent(view);//ponemos funcionalidad al boton de añadir evento
        addRecyclerView(view);//cargamos los eventos que organiza
    }

    /**
     * metodo para añadir los datos del RecyclerView
     * @param view
     */
    private void addRecyclerView(View view) {
        //hacemos la conexión con la BBDD
        ConexionBBDD connection = new ConexionBBDD(view.getContext(),"bd_events",null,2);
        //recogemos los eventos que organiza
        listEvents = connection.listEventsByOrganizador(userSesion.getId());

        //añado layout de como se va a ver
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(),2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        rvListEvents.setLayoutManager(gridLayoutManager);

        //añado adaptador
        EventosGestionarCardViewAdapter adapter = new EventosGestionarCardViewAdapter(listEvents,userSesion,view.getContext());
        rvListEvents.setAdapter(adapter);
    }

    /**
     * metodo para añadir un nuevo evento
     * @param view
     */
    private void addNewEvent(View view) {
        //declaramos el boton
        FloatingActionButton buttonNewEvent = (FloatingActionButton) view.findViewById(R.id.buttonNewEvent);
        //le añadimos funcionalidad
        buttonNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //nos redirigimos a la activity EventoActivity para añadir un evento
                Intent intent = new Intent(view.getContext(), EventoActivity.class);
                //recogemos las variables que vamos a necesitar
                Evento event = null;
                intent.putExtra("evento",event);
                intent.putExtra("userSesion",userSesion);
                startActivity(intent);
            }
        });
    }

    /**
     * metodo para recoger el usuario de la sesion
     */
    private void addUserSesion(){
        //recogemos la cookie del usuario
        Bundle data = this.getArguments();
        if(data != null){
            userSesion = (Usuario) data.getSerializable("userSesion");
        }
    }

    /**
     * metodo para relacionar el fragment
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gestionar_eventos, container, false);
    }
}