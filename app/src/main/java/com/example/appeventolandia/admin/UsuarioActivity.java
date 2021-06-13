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
import android.widget.Toast;
import com.example.appeventolandia.ConexionBBDD.ConexionBBDD;
import com.example.appeventolandia.comun.MainAdminOrganizadorActivity;
import com.example.appeventolandia.R;
import com.example.appeventolandia.entidades.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import es.dmoral.toasty.Toasty;

public class UsuarioActivity extends AppCompatActivity {

    //variables de los elementos
    private TextView title_usuario_activity;
    private EditText text_value_id_usuario;
    private EditText edit_nombre_usuario;
    private EditText edit_correo_usuario;
    private EditText edit_pwd_usuario;
    private Spinner spinner_rol_usuario;
    private FloatingActionButton buttonDeleteUser;

    //variables necesarias para la clase
    private boolean salModificar; //variable para saber si hay que modificar o añadir
    private Usuario user; //usuario que se trata en la activity
    private Usuario userSesion;
    private ConexionBBDD connection;

    /**
     * metodo para cerar la activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        addMenu(); //añadimos menu
        addUserSession(); // guardar la sesion
        addData(); //rellenamos los campos
        addEventSpinner(); //llamamos al spinner
        addButtonSave(); //guardamos el usuario
        addButtonDelete(); //eliminamos el usuario
    }

    /**
     * metodo para eliminar el usuario
     */
    private void addButtonDelete() {
        //llamamos al evento
        buttonDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //eliminamos usuario
                long result =  connection.deleteUser(user.getId());
                //comprobamos si ha habido error
                if(result > 0) {
                    //mensaje de exito
                    Toasty.success(v.getContext(), "Delete the user", Toast.LENGTH_SHORT).show();
                    //nos redirigimos al usuario
                    redireccionamiento();
                }else{
                    //mensaje de error
                    Toasty.error(v.getContext(), "Error deleted the user", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * metodo para llamar al spinner
     */
    private void addEventSpinner() {
        //añadimos evento del spinner_rol_usuario
        spinner_rol_usuario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //recogemos la posicion del spinner
                String[] cmd = getResources().getStringArray(R.array.rolUsuario_array);
                int rol  = Usuario.rolByNombre(cmd[position]);
                //guardamos el rol
                user.setIdRol(rol);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    /**
     * metodo para añadir el menu
     */
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

    /**
     * metodo para guardar el usuario
     */
    private void addButtonSave() {
        //declaramos el elemento
        Button buttonSaveUsuarioActivity = (Button) findViewById(R.id.buttonSaveUsuarioActivity);
        //llamamos al evento
        buttonSaveUsuarioActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //comprobamos que todos los campos están seleccionados
                if(!edit_nombre_usuario.getText().toString().equals("") && !edit_correo_usuario.getText().toString().equals("")){
                    //recogemos los campos comunes
                    user.setNombreApellidos(edit_nombre_usuario.getText().toString());
                    user.setCorreo(edit_correo_usuario.getText().toString());

                    if(salModificar){//si salModificar es true, significa que vamos a modificar un usuario
                        //comprobamos que quiera cambiar la comtraseña
                        if( !edit_pwd_usuario.getText().toString().equals("")) {
                            //guardamos la nueva contraseña
                            user.setPwd(Usuario.codificaciónSHA512(edit_pwd_usuario.getText().toString()));
                        }

                        //actualizamos usuario
                        int result = connection.updateUser(user);
                        //comprobamos si ha habido error
                        if(result > 0) {
                            //mensaje de exito
                            Toasty.success(v.getContext(), "Updated the user", Toast.LENGTH_SHORT).show();
                            //nos redirigimos al usuario
                            redireccionamiento();
                        }else{
                            //mensaje de error
                            Toasty.error(v.getContext(), "Error updated the user", Toast.LENGTH_SHORT).show();
                        }

                    }else{//si salModificar es false, significa que vamos a añadir un usuario
                        //recogemos la contraseña
                        user.setPwd(Usuario.codificaciónSHA512(edit_pwd_usuario.getText().toString()));

                        //insertamos el nuevo usuario
                        long result =  connection.insertUser(user);
                        //comprobamos si ha habido error
                        if(result > 0) {
                            //mensaje de exito
                            Toasty.success(v.getContext(), "Updated the user", Toast.LENGTH_SHORT).show();
                            //nos redirigimos al usuario
                            redireccionamiento();
                        }else{
                            //mensaje de error
                            Toasty.error(v.getContext(), "Error updated the user", Toast.LENGTH_SHORT).show();
                        }
                    }

                }else{//mostramos mensaje emergente
                    Toasty.info(v.getContext(), "Rellene todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * metodo para redirigirnos a la siguiente activity
     */
    private void redireccionamiento(){
        //nos redirigimos al usuario
        Intent intent = null;
        switch (userSesion.getIdRol()){
            case 2:// Bienvenida administrador
                intent = new Intent(UsuarioActivity.this, MainAdminActivity.class);
                break;
            case 3:// Bienvenida admin-organizador
                intent = new Intent(UsuarioActivity.this, MainAdminOrganizadorActivity.class);
                break;
        }
        intent.putExtra("userSesion", userSesion); //guardamos el usuario para saber quien es
        startActivity(intent);

    }

    /**
     * metodo para guardar las sesiones
     */
    private void addUserSession() {
        //hacemos la conexión con la BBDD
        connection = new ConexionBBDD(this,"bd_events",null,2);
        //declaramos elemento
        buttonDeleteUser = (FloatingActionButton) findViewById(R.id.buttonDeleteUser);

        if((Usuario) getIntent().getExtras().getSerializable("userSesion") != null) { //modificar
            userSesion = (Usuario) getIntent().getExtras().getSerializable("userSesion");
        }
        if((Usuario) getIntent().getExtras().getSerializable("usuario") != null) { //modificar
            user = (Usuario) getIntent().getExtras().getSerializable("usuario");
            //mostramos boton de eliminar
            buttonDeleteUser.show();
        }else{//añadir
            user = null;
            //escondemos boton de eliminar
            buttonDeleteUser.hide();
        }
    }

    /**
     * metodo para rellenar los campos
     */
    private void addData() {
        //declaramos elementos
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

        if(user!= null){ //si es modificar
            //cambiamos titulo
            title_usuario_activity.setText(R.string.title_usuario_modificar);
            //rellenamos los campos
            text_value_id_usuario.setText(user.getId()+"");
            edit_nombre_usuario.setText(user.getNombreApellidos());
            edit_correo_usuario.setText(user.getCorreo());
            spinner_rol_usuario.setSelection(user.getIdRol());
            //ponemos que vamos a modificar
            salModificar = true;
        }else{ //si es añadir
            //declaramos usuario que vamos a ir rellenando
            user = new Usuario();
            //cambiamos titulo
            title_usuario_activity.setText(R.string.title_usuario_aniadir);
            //ponemos que vamos a añadir
            salModificar = false;
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}