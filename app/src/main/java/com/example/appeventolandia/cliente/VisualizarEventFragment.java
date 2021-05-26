package com.example.appeventolandia.cliente;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.appeventolandia.ConexionBBDD.ConexionBBDD;
<<<<<<< Updated upstream:app/src/main/java/com/example/appeventolandia/fragments/cliente/VisualizarEventFragment.java
=======
import com.example.appeventolandia.adaptadores.EventosCardViewAdapter;
>>>>>>> Stashed changes:app/src/main/java/com/example/appeventolandia/cliente/VisualizarEventFragment.java
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
<<<<<<< Updated upstream:app/src/main/java/com/example/appeventolandia/fragments/cliente/VisualizarEventFragment.java
        //declaramos el calendario
        calendario = (CalendarView) view.findViewById(R.id.calendarView_VisualizarEventFragment);
=======
        //extendes Fragment, hay que heredarlo para que funcione
        rvListEvents = (RecyclerView) view.findViewById(R.id.rvListShowEvents);
>>>>>>> Stashed changes:app/src/main/java/com/example/appeventolandia/cliente/VisualizarEventFragment.java

        addUserSesion();//recogemos la userSesion
        addData();//cargamos los eventos que organiza
    }

    private void addData() {
        int id = userSesion.getId();
        //recogemos los eventos del cliente
<<<<<<< Updated upstream:app/src/main/java/com/example/appeventolandia/fragments/cliente/VisualizarEventFragment.java
        listEvents = connection.listEventsByCliente(id);
=======
        listEvents = connection.listEventsByCliente(userSesion.getId());

        //añado layout de como se va a ver
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(),2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        rvListEvents.setLayoutManager(gridLayoutManager);

        //añado adaptador
        EventosCardViewAdapter adapter = new EventosCardViewAdapter(listEvents,userSesion,view.getContext());
        rvListEvents.setAdapter(adapter);
>>>>>>> Stashed changes:app/src/main/java/com/example/appeventolandia/cliente/VisualizarEventFragment.java
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