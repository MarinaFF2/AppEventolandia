package com.example.appeventolandia.admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.appeventolandia.ConexionBBDD.ConexionBBDD;
import com.example.appeventolandia.InicioSesionActivity;
import com.example.appeventolandia.R;
import com.example.appeventolandia.entidades.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class GestionarClienteFragment extends Fragment {
    private ListView list_gestionarUsuarios;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gestionar_cliente, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //asignamos el listener creado a la ListView
        list_gestionarUsuarios = (ListView) view.findViewById(R.id.list_gestionarUsuarios);

        addListView(view);//añadimos el listView
        addNewUser(view); //boton añadir nuevo usuario
    }


    private void addNewUser(View view) {
        FloatingActionButton buttonNewUser = (FloatingActionButton) view.findViewById(R.id.buttonNewUser);
        buttonNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(),UsuarioActivity.class);
                Usuario user = null;
                intent.putExtra("usuario",user);
                startActivity(intent);
            }
        });
    }
    private void addListView(View view) {
        //hacemos la conexión con la BBDD
        ConexionBBDD connection = new ConexionBBDD(view.getContext(),"bd_events",null,2);
        ArrayList<Usuario> listUser = connection.listUsuariosByCliente();

        //creamos el adaptador para acceder a los datos de la clase JAVA Usuario
        ArrayAdapter<Usuario> listAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1,listUser);
        //asignamos el adaptador al elemento grafico que lo va a usar
        list_gestionarUsuarios.setAdapter(listAdapter);

        //creaccion del listener, cuando hacemos clic en el listView
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View itemView, int position, long id) {
                Usuario user = listUser.get(position);
                Intent intent = new Intent(view.getContext(),UsuarioActivity.class);
                intent.putExtra("usuario",user);
                startActivity(intent);
            }
        };

        list_gestionarUsuarios.setOnItemClickListener(itemClickListener);
    }
}