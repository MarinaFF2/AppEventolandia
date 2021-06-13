package com.example.appeventolandia.comun;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.appeventolandia.ConexionBBDD.ConexionBBDD;
import com.example.appeventolandia.R;
import com.example.appeventolandia.admin.MainAdminActivity;
import com.example.appeventolandia.cliente.MainClienteActivity;
import com.example.appeventolandia.entidades.Evento;
import com.example.appeventolandia.entidades.Usuario;
import com.example.appeventolandia.organizador.MainOrganizadorActivity;

import es.dmoral.toasty.Toasty;

public class InicioSesionActivity extends AppCompatActivity {
    //declaramos la bbdd
    private ConexionBBDD connection;

    /**
     * metodo para crear la actividad
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        //hacemos la conexión con la BBDD
        connection = new ConexionBBDD(InicioSesionActivity.this,"bd_events",null,2);

        addData();//añadimos datos a la BBDD si no hay
        addMenu(); //añadimos menu
        buttonEvent();//añadimos el evento del botón para iniciar sesión
    }

    /**
     * metodo para añadir los datos la primera vez que se ejecuta
     */
    private void addData() {
        //comprobamos si hay usuarios en la BBDD,
        //sino lanzamos los insert para insertar usuarios para iniciar la app con datos
       if(connection.listUsers()==null){
           Usuario.insertUsuarioIniciales(InicioSesionActivity.this);
       }
        //comprobamos si hay eventos en la BBDD,
        //sino lanxzamos los insert para insertar eventos para iniciar la app con datos
        if(connection.listEvents()==null){
            Evento.insertEventoIniciales(InicioSesionActivity.this);
        }
    }

    /**
     * metodo para añadir el menu
     */
    private void addMenu() {
        //añadimos el action bar a la activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        //ponemos el icono de la app
        getSupportActionBar().setIcon(R.drawable.eventolandia);
    }

    /**
     * metodo para iniciar sesion
     */
    private void buttonEvent() {
        //declamos los editText y el boton del layout activity_inicio_sesion.xml
        EditText editView_correo_inicioSesion = (EditText) findViewById(R.id.editView_correo_inicioSesion);
        EditText editView_pwd_inicioSesion = (EditText) findViewById(R.id.editView_pwd_inicioSesion);
        Button button_inicioSesion = (Button) findViewById(R.id.button_inicioSesion);

        //si hacemos click en el boton
        button_inicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //comprobamos que los editText no estén vacios
                if(editView_correo_inicioSesion.getText().toString() != null && editView_pwd_inicioSesion.getText().toString() != null &&
                editView_correo_inicioSesion.getText().toString() != "" && editView_pwd_inicioSesion.getText().toString() != ""){
                    Usuario user = connection.existUserByCorreoPwd(editView_correo_inicioSesion.getText().toString(),Usuario.codificaciónSHA512(editView_pwd_inicioSesion.getText().toString()));

                    if(user != null) {//indica que el usuario existe en la BBDD con ese correo y esa contraseña
                        //nos redirigimos a la bienvenida del usuario
                        Intent intent = null;
                        switch (user.getIdRol()){
                            case 0: // Bienvenida cliente
                                intent = new Intent(InicioSesionActivity.this, MainClienteActivity.class);
                                break;
                            case 1:// Bienvenida organizador
                                intent = new Intent(InicioSesionActivity.this, MainOrganizadorActivity.class);
                                break;
                            case 2:// Bienvenida administrador
                                intent = new Intent(InicioSesionActivity.this, MainAdminActivity.class);
                                break;
                            case 3:// Bienvenida admin-organizador
                                intent = new Intent(InicioSesionActivity.this, MainAdminOrganizadorActivity.class);
                                break;
                        }
                        intent.putExtra("userSesion", user); //guardamos el usuario para saber quien es
                        startActivity(intent);
                    }else{//indica que el usuario no existe en la BBDD con ese correo y esa contraseña
                        Toasty.error(InicioSesionActivity.this,"Error al iniciar sesión. ¡Vuelve a probar!",Toast.LENGTH_SHORT).show();
                    }
                }else{ //si están vacíos se envía un mensaje indicandolo
                    Toasty.info(InicioSesionActivity.this, "¡Tienes que rellenar los campos!", Toast.LENGTH_SHORT, true).show();
                }
            }
        });
    }
}