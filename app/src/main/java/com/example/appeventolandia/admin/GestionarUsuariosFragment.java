package com.example.appeventolandia.fragments.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.appeventolandia.R;
import com.example.appeventolandia.entidades.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;


public class GestionarUsuariosFragment extends Fragment {
    private ListView list_gestionarUsuarios;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gestionar_usuarios, container, false);
    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //asignamos el listener creado a la ListView
        list_gestionarUsuarios = (ListView) view.findViewById(R.id.list_gestionarUsuarios);
        addListView(view);//añadimos el listView
        addEventListView();//cuando hacemos clic en el listView
        addNewUser(view); //boton añadir nuevo usuario
    }

    private void addNewUser(View view) {
        FloatingActionButton buttonNewUser = (FloatingActionButton) view.findViewById(R.id.buttonNewUser);
        buttonNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
    private void addListView(View view) {
        ArrayList<Usuario> listUser = Usuario.list_user();

        //creamos el adaptador para acceder a los datos de la clase JAVA Usuario
        ArrayAdapter<Usuario> listAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1,listUser);
        //asignamos el adaptador al elemento grafico que lo va a usar
        list_gestionarUsuarios.setAdapter(listAdapter);
    }
    private void addEventListView() {
        //creaccion del listener
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View itemView, int position, long id) {
                if(position == 0) {//1º opcion
                }
            }
        };

        list_gestionarUsuarios.setOnItemClickListener(itemClickListener);
    }
}