package com.example.appeventolandia.cliente;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appeventolandia.ConexionBBDD.ConexionBBDD;
import com.example.appeventolandia.R;
import com.example.appeventolandia.adaptadores.EventosCardViewAdapter;
import com.example.appeventolandia.entidades.Evento;
import com.example.appeventolandia.entidades.Usuario;
import java.util.ArrayList;

public class VisualizarEventFragment extends Fragment {
    //variables necesarias para la clase
    private Usuario userSesion;
    private ConexionBBDD connection;
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

        //hacemos la conexión con la BBDD
        connection = new ConexionBBDD(view.getContext(),"bd_events",null,2);

        //extendes Fragment, hay que heredarlo para que funcione
        rvListEvents = (RecyclerView) view.findViewById(R.id.rvListShowEvents);

        addUserSesion();//recogemos la userSesion
        addData(view);//cargamos los eventos que organiza
    }

    /**
     * metodo para rellenar los campos
     * @param view
     */
    private void addData(View view) {
        //recogemos los eventos del cliente
        listEvents = connection.listEventsByCliente(userSesion.getId());

        //añado layout de como se va a ver
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(),2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        rvListEvents.setLayoutManager(gridLayoutManager);

        //añado adaptador
        EventosCardViewAdapter adapter = new EventosCardViewAdapter(listEvents,userSesion,view.getContext());
        rvListEvents.setAdapter(adapter);
    }
    /**
     * metodo para guardar el usuario de la sesion
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_visualizar_event, container, false);
    }
}