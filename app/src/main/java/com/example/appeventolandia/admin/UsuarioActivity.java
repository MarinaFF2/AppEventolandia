package com.example.appeventolandia.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.appeventolandia.R;
import com.example.appeventolandia.entidades.Usuario;

public class UsuarioActivity extends AppCompatActivity {

    private TextView title_usuario_activity;
    private EditText text_value_id_usuario;
    private EditText edit_nombre_usuario;
    private EditText edit_correo_usuario;
    private EditText edit_pwd_usuario;
    private Spinner spinner_rol_usuario;

    private Usuario user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        addMenu(); //añadimos menu
        addData();
        addEventSpinner();
        addButtonSave();
    }

    private void addEventSpinner() {
        //añadimos evento del spinner_rol_usuario
        spinner_rol_usuario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] cmd = getResources().getStringArray(R.array.rolUsuario_array);
                int rol  = Usuario.rolByNombre(cmd[position]);
                user.setIdRol(rol);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void addMenu() {
        //añadimos el action bar a la activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //ponemos el icono de la app
        toolbar.setLogo(R.drawable.eventolandia);
        //añadimos flecha para atrása la activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    private void addButtonSave() {
        Button buttonSaveUsuarioActivity = (Button) findViewById(R.id.buttonSaveUsuarioActivity);

        buttonSaveUsuarioActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    private void addData() {
        title_usuario_activity = (TextView) findViewById(R.id.title_usuario_activity);
        text_value_id_usuario = (EditText) findViewById(R.id.text_value_id_usuario);
        edit_nombre_usuario = (EditText) findViewById(R.id.edit_nombre_usuario);
        edit_correo_usuario = (EditText) findViewById(R.id.edit_correo_usuario);
        edit_pwd_usuario = (EditText) findViewById(R.id.edit_pwd_usuario);
        spinner_rol_usuario = (Spinner) findViewById(R.id.spinner_rol_usuario);

        // Cree un ArrayAdapter usando un string array y un spinner layout predeterminado
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.rolUsuario_array, android.R.layout.simple_spinner_item);
        // especificamos el tipo de layout que queremos mostrar en el spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // aplicamos el adpatador al spinner
        spinner_rol_usuario.setAdapter(adapter);

        if((Usuario) getIntent().getExtras().getSerializable("usuario") != null){
            title_usuario_activity.setText(R.string.title_usuario_modificar);
            user = (Usuario) getIntent().getExtras().getSerializable("usuario");
            text_value_id_usuario.setText(user.getId()+"");
            edit_nombre_usuario.setText(user.getNombreApellidos());
            edit_correo_usuario.setText(user.getCorreo());
            edit_pwd_usuario.setText(user.getPwd());
            spinner_rol_usuario.setSelection(user.getIdRol());
        }else{
            user = new Usuario();
            title_usuario_activity.setText(R.string.title_usuario_aniadir);
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}