package com.example.appeventolandia.organizador;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.appeventolandia.ConexionBBDD.ConexionBBDD;
import com.example.appeventolandia.comun.MainAdminOrganizadorActivity;
import com.example.appeventolandia.R;
import com.example.appeventolandia.entidades.Evento;
import com.example.appeventolandia.entidades.Usuario;
import com.example.appeventolandia.comun.MapsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.Calendar;
import es.dmoral.toasty.Toasty;

public class EventoActivity extends AppCompatActivity {
    //variables de los elementos
    private EditText editNombreEvento;
    private EditText editDescripcionEvento;
    private Spinner spinnerTipoEvento;
    private Spinner spinnerClienteEvento;
    private Spinner spinnerOrganEvento;
    private EditText editUbicacionLatitudEvento;
    private EditText editUbicacionLongitudEvento;
    private EditText editFechaEvento;
    private EditText editDuracionEvento;
    private EditText editPrecioEvento;
    private FloatingActionButton buttonDeleteEvento;

    //variables necesarias para la clase
    private boolean salModificar; //variable para saber si hay que modificar o añadir
    private ConexionBBDD connection;
    private Evento evento;
    private ArrayList<Usuario> listUserOrganizador;
    private ArrayList<Usuario> listUserCliente;
    private Usuario userSesion;

    /**
     * metodo para crear la activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);

        addMenu(); //añadimos menu
        addData(); //añadimos datos del evento
        addEventSpinner();//llamamos al evento para guardar los datos del spinner
        addFechaEvento(); //llamamos al evento para guardar la fecha
        addButtonSave(); //guardamos el evento
        addButtonDelete(); //llamamos al evento para eliminar el evento
        addButtonRefresh(); //llamamos al evento para refrescar el mapa de google
    }

    /**
     * metodo para añadir los eventos de los spinners
     */
    private void addEventSpinner() {
        //añadimos evento del spinnerOrganEvento
        spinnerOrganEvento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //conseguimos la posicion seleccionada del spinner
                Usuario user  = listUserOrganizador.get(position);
                //guardamos el organizador del evento
                evento.setIdOrganizador(user.getId());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        //añadimos evento del spinnerClienteEvento
        spinnerClienteEvento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //conseguimos la posicion seleccionada del spinner
                Usuario user  = listUserCliente.get(position);
                //guardamos el cliente del evento
                evento.setIdCliente(user.getId());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        //añadimos evento del spinnerTipoEvento
        spinnerTipoEvento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //conseguimos la posicion seleccionada del spinner
                String[] cmd = getResources().getStringArray(R.array.tipoEvento_array);
                String tipo  = cmd[position];
                //guardamos el tipo del evento
                evento.setTipoEvento(tipo);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    /**
     * metodo para añadir menu
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
     * metodo al hacer clic en guardar
     */
    private void addButtonSave() {
        //declaramos elemento
        Button buttonSave = (Button) findViewById(R.id.buttonSaveEvento);
        //llamamos al evento
        buttonSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //comprobamos que todos los campos están seleccionados
                if(!editNombreEvento.getText().toString().equals("") && !editDescripcionEvento.getText().toString().equals("") &&
                        !editUbicacionLatitudEvento.getText().toString().equals("") && !editUbicacionLongitudEvento.getText().toString().equals("") &&
                        !editDuracionEvento.getText().toString().equals("") && !editPrecioEvento.getText().toString().equals("")){
                    //recogemos los campos comunes
                    evento.setNombre(editNombreEvento.getText().toString());
                    evento.setDescripcion(editDescripcionEvento.getText().toString());
                    evento.setUbicacion(editUbicacionLatitudEvento.getText().toString()+","+editUbicacionLongitudEvento.getText().toString());

                    //comprobamos si hay fecha
                    if(!editFechaEvento.getText().toString().equals("")) {
                        evento.setFecha(editFechaEvento.getText().toString());
                    }
                    //comprobamos si tiene la h de horas
                    if(editDuracionEvento.getText().toString().contains("h")) {
                        evento.setDuracion(editDuracionEvento.getText().toString().replace("h", ""));
                    }else{
                        evento.setDuracion(editDuracionEvento.getText().toString());
                    }
                    //comprobamos si tiene la € de euros
                    if(editPrecioEvento.getText().toString().contains("€")) {
                        evento.setPrecio(Double.parseDouble(editPrecioEvento.getText().toString().replace("€", "")));
                    }else{
                        evento.setPrecio(Double.parseDouble(editPrecioEvento.getText().toString()));
                    }

                    if(salModificar){//si salModificar es true, significa que vamos a modificar un evento
                        //modificamos el evento
                        int result = connection.updateEvento(evento);
                        //comprobamos si ha habido error
                        if(result > 0) {
                            //mensaje de exito
                            Toasty.success(v.getContext(), "Updated the event", Toast.LENGTH_SHORT).show();
                            //nos redirigimos al usuario
                            redireccionamiento();
                        }else{
                            //mensaje de error
                            Toasty.error(v.getContext(), "Error updated the event", Toast.LENGTH_SHORT).show();
                        }

                    }else{//si salModificar es false, significa que vamos a añadir un evento
                        //insertamos el evento
                        long result =  connection.insertEvento(evento);
                        //comprobamos si ha habido error
                        if(result > 0) {
                            //mensaje de exito
                            Toasty.success(v.getContext(), "Updated the event", Toast.LENGTH_SHORT).show();
                            //nos redirigimos al evento
                            redireccionamiento();
                        }else{
                            //mensaje de error
                            Toasty.error(v.getContext(), "Error updated the event", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    //mostramos mensaje emergente
                    Toasty.info(v.getContext(), "Rellene todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * metodo para rellenar los datos del evento
     */
    private void addData() {
        //recogemos la usuario de la sesion
        userSesion = (Usuario) getIntent().getExtras().getSerializable("userSesion");
        //hacemos la conexión con la BBDD
        connection = new ConexionBBDD(this,"bd_events",null,2);

        //declaramos los elementos que vamos a necesitar
        TextView title_evento_gestionar = (TextView) findViewById(R.id.title_evento_gestionar);
        editNombreEvento = (EditText) findViewById(R.id.editNombreEvento);
        editDescripcionEvento = (EditText) findViewById(R.id.editDescripcionEvento);
        spinnerTipoEvento = (Spinner) findViewById(R.id.spinnerTipoEvento);
        spinnerClienteEvento = (Spinner) findViewById(R.id.spinnerClienteEvento);
        spinnerOrganEvento = (Spinner) findViewById(R.id.spinnerOrganEvento);
        editUbicacionLatitudEvento = (EditText) findViewById(R.id.editUbicacionLatitudEvento);
        editUbicacionLongitudEvento = (EditText) findViewById(R.id.editUbicacionLongitudEvento);
        editFechaEvento = (EditText) findViewById(R.id.editFechaEvento);
        editDuracionEvento = (EditText) findViewById(R.id.editDuracionEvento);
        editPrecioEvento = (EditText) findViewById(R.id.editPrecioEvento);
        buttonDeleteEvento = (FloatingActionButton) findViewById(R.id.buttonDeleteEvento);

        //rellenamos los spiner con los datos
        addSpinners();

        //Comprobamos si nos han pasado un evento o no, para saber si hay que añadir o modificar
        if((Evento) getIntent().getExtras().getSerializable("evento") != null){
            //cambiamos el titulo de la pantalla
            title_evento_gestionar.setText(R.string.title_evento_modificar);
            //recogemos el evento a modificar
            evento = (Evento) getIntent().getExtras().getSerializable("evento");

            //Rellenamos los campos de la pantalla
            editNombreEvento.setText(evento.getNombre());
            editDescripcionEvento.setText(evento.getDescripcion());
            editFechaEvento.setText(evento.getFecha());
            editDuracionEvento.setText(evento.getDuracion()+"h");
            editPrecioEvento.setText(evento.getPrecio()+"€");
            //conseguimos la latitud y longitud para ponerla en los campos correspondientes
            String[] ubicacion = evento.getUbicacion().split(",");
            editUbicacionLatitudEvento.setText(ubicacion[0]);
            editUbicacionLongitudEvento.setText(ubicacion[1]);
            //pasamos de cadena a decimal
            double latitud = Double.parseDouble( editUbicacionLatitudEvento.getText().toString());
            double longitud = Double.parseDouble(editUbicacionLongitudEvento.getText().toString());
            //refescamos el mapa para que aparezca en dicha ubicacion
            addMap(latitud,longitud);

            //seleccionamos cliente en el spinner
            //recorremos la lista de clientes
            for (int i=0; i < listUserCliente.size(); i++){
                //comprobamos el correspondiente al evento
                if(listUserCliente.get(i).getId() == evento.getIdCliente()){
                    //seleccionamos dicho cliente
                    spinnerClienteEvento.setSelection(i);
                }
            }

            //seleccionamos organizador
            //recorremos la lista de organizadores
           for (int i=0; i < listUserOrganizador.size(); i++){
               //comprobamos el correspondiente al evento
               if(listUserOrganizador.get(i).getId() == evento.getIdOrganizador()){
                   //seleccionamos dicho organizador
                   spinnerOrganEvento.setSelection(i);
               }
           }

            //seleccionamos TipoEvento en el spinner
            spinnerTipoEvento.setSelection(Evento.tipoEventoByInt(evento.getTipoEvento()));

           //mostrar el boton eliminar
            buttonDeleteEvento.show();
            //marcamos que se va a modificar un evento
            salModificar = true;
        }else{
            //es la pantalla de añadir evento

            //creamos evento vacio para ir rellenandolo poco a poco
            evento = new Evento();
            //cambiamos el titulo de la pantalla
            title_evento_gestionar.setText(R.string.title_evento_aniadir);
            //escondemos el boton eliminar
            buttonDeleteEvento.hide();
            //marcamos que se va a añadir un evento
            salModificar = false;
        }
    }

    /**
     * metodo para refrescar el fragment del mapa con la nueva ubicacion
     * @param latitud
     * @param longitud
     */
    private void addMap(double latitud, double longitud) {
        //mostramos el fragment de mapa
        Fragment fragment = new MapsFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.mapView_fragment,fragment);

        try {
            //pasamos la informacion necesaria para actualizar el mapa al fragment
            Bundle data = new Bundle();
            data.putSerializable("nombreEvento", editNombreEvento.getText().toString());
            data.putSerializable("latitud", latitud);
            data.putSerializable("longitud", longitud);
            fragment.setArguments(data);
        } catch (NumberFormatException e){
        }
        fragmentTransaction.commit();
    }

    /**
     * Metodo para eliminar el evento
     */
    private void addButtonDelete() {
        //declaramos el evento
        buttonDeleteEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //eliminamos el evento
                long result =  connection.deleteEvento(evento.getId());
                //Comprobamos si se ha hecho correctamente
                if(result > 0) {
                    //ha tenido exito
                    Toasty.success(v.getContext(), "Delete the event", Toast.LENGTH_SHORT).show();
                    //nos redirigimos al usuario
                    redireccionamiento();
                }else{
                    //ha habido un error por lo que muestra un mensaje de error
                    Toasty.error(v.getContext(), "Error deleted the event", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * metodo para llamar al boton actualizar del mapa de google
     */
    private void addButtonRefresh() {
        //declaracion del elemento
        FloatingActionButton buttonRefreshUbicacion = (FloatingActionButton) findViewById(R.id.buttonRefreshUbicacion);
        //declaración del evento
        buttonRefreshUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //recogemos la latitud y longitud de la ubicación del evento
                double latitud = Double.parseDouble( editUbicacionLatitudEvento.getText().toString());
                double longitud = Double.parseDouble(editUbicacionLongitudEvento.getText().toString());
                //actualizamos el mapa de google
                addMap(latitud,longitud);
            }
        });
    }

    /**
     * Metodo para redirigir al usuario
     */
    private void redireccionamiento(){
        Intent intent = null;
        //comprobamos el rol y hacemos la redireccion
        switch (userSesion.getIdRol()){
            case 1:// Bienvenida Organizador
                intent = new Intent(EventoActivity.this, MainOrganizadorActivity.class);
                break;
            case 3:// Bienvenida admin-organizador
                intent = new Intent(EventoActivity.this, MainAdminOrganizadorActivity.class);
                break;
        }
        intent.putExtra("userSesion", userSesion); //guardamos el usuario para saber quien es
        startActivity(intent);
    }

    /**
     * metodo para el evento de la fecha
     */
    private void addFechaEvento() {
        //declaramos el evento
        editFechaEvento.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //recogemos la fecha actual del dispositivo
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                //abrimos un dilog con el calendario y la fecha actual del dispositivo
                mostrarDatePickerDilog(year,month,day);
            }
        });
    }

    /**
     * Metodo para mostrar el DatePickerDilog para recoger la fecha
     * @param year -> año
     * @param month -> mes
     * @param day -> día
     */
    public void mostrarDatePickerDilog( int year, int month, int day){
        //cogemos evento del picker
        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDday) {
                //recogemos la fecha y la mostramos en el editText de la Fecha
                editFechaEvento.setText(mDday+"/"+(mMonth+ 1)+"/"+mYear);
            }
        },day,month,year);
        //mostramos el dilog
        dpd.show();
    }

    /**
     * Metodo para rellanar los spinners
     */
    private void addSpinners() {
        //Añadimos los datos de SpinnerOrganizador
        listUserOrganizador = connection.spinnerByOrganizador();
        // Cree un ArrayAdapter usando un string array y un spinner layout predeterminado
        ArrayAdapter<Usuario> adapterOrganizador = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item,listUserOrganizador);
        // especificamos el tipo de layout que queremos mostrar en el spinner
       adapterOrganizador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // aplicamos el adpatador al spinner
       spinnerOrganEvento.setAdapter(adapterOrganizador);

       //Añadimos los datos de SpinnerCliente
        listUserCliente = connection.listUsuariosByCliente();
        // Cree un ArrayAdapter usando un string array y un spinner layout predeterminado
        ArrayAdapter<Usuario> adapterCliente = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item,listUserCliente);
        // especificamos el tipo de layout que queremos mostrar en el spinner
        adapterCliente.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // aplicamos el adpatador al spinner
        spinnerClienteEvento.setAdapter(adapterCliente);

        //Añadimos los datos de SpinnerTipoEvento
        // Cree un ArrayAdapter usando un string array y un spinner layout predeterminado
        ArrayAdapter<CharSequence> adapterTipoEvento = ArrayAdapter.createFromResource(getApplicationContext(),R.array.tipoEvento_array, android.R.layout.simple_spinner_item);
        // especificamos el tipo de layout que queremos mostrar en el spinner
        adapterTipoEvento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // aplicamos el adpatador al spinner
        spinnerTipoEvento.setAdapter(adapterTipoEvento);
    }

    /**
     * Metodo para ir para atrás
     * @return
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}