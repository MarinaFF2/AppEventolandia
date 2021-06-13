package com.example.appeventolandia.organizador;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appeventolandia.ConexionBBDD.ConexionBBDD;
import com.example.appeventolandia.MainAdminOrganizadorActivity;
import com.example.appeventolandia.R;
import com.example.appeventolandia.admin.MainAdminActivity;
import com.example.appeventolandia.admin.UsuarioActivity;
import com.example.appeventolandia.entidades.Evento;
import com.example.appeventolandia.entidades.Usuario;
import com.example.appeventolandia.fragmentsComun.MapsFragment;
import com.example.appeventolandia.fragmentsComun.WelcomeFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;

public class EventoActivity extends AppCompatActivity {
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

    private boolean salModificar; //variable para saber si hay que modificar o añadir
    private ConexionBBDD connection;
    private Evento evento;
    private ArrayList<Usuario> listUserOrganizador;
    private ArrayList<Usuario> listUserCliente;
    private Usuario userSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);

        addMenu(); //añadimos menu
        addData();
        addEventSpinner();
        addFechaEvento();
        addButtonSave();
        addButtonDelete();
        addButtonRefresh();
    }
    private void addEventSpinner() {
        //añadimos evento del spinnerOrganEvento
        spinnerOrganEvento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Usuario user  = listUserOrganizador.get(position);
                evento.setIdOrganizador(user.getId());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        //añadimos evento del spinnerClienteEvento
        spinnerClienteEvento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Usuario user  = listUserCliente.get(position);
                evento.setIdCliente(user.getId());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        //añadimos evento del spinnerTipoEvento
        spinnerTipoEvento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] cmd = getResources().getStringArray(R.array.tipoEvento_array);
                String tipo  = cmd[position];
                evento.setTipoEvento(tipo);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
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
        Button buttonSave = (Button) findViewById(R.id.buttonSaveEvento);
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
                        int result = connection.updateEvento(evento);
                        if(result > 0) {
                            Toasty.success(v.getContext(), "Updated the event", Toast.LENGTH_SHORT).show();
                            //nos redirigimos al usuario
                            redireccionamiento();
                        }else{
                            Toasty.error(v.getContext(), "Error updated the event", Toast.LENGTH_SHORT).show();
                        }

                    }else{//si salModificar es false, significa que vamos a añadir un evento

                        long result =  connection.insertEvento(evento);
                        if(result > 0) {
                            Toasty.success(v.getContext(), "Updated the event", Toast.LENGTH_SHORT).show();
                            //nos redirigimos al evento
                            redireccionamiento();
                        }else{
                            Toasty.error(v.getContext(), "Error updated the event", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{//mostramos mensaje emergente
                    Toasty.info(v.getContext(), "Rellene todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void addData() {
        //recogemos la usuario de la sesion
        userSesion = (Usuario) getIntent().getExtras().getSerializable("userSesion");
        //hacemos la conexión con la BBDD
        connection = new ConexionBBDD(this,"bd_events",null,2);

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
        addSpinners();

        if((Evento) getIntent().getExtras().getSerializable("evento") != null){
            title_evento_gestionar.setText(R.string.title_evento_modificar);

            evento = (Evento) getIntent().getExtras().getSerializable("evento");

            editNombreEvento.setText(evento.getNombre());
            editDescripcionEvento.setText(evento.getDescripcion());
            editFechaEvento.setText(evento.getFecha());
            editDuracionEvento.setText(evento.getDuracion()+"h");
            editPrecioEvento.setText(evento.getPrecio()+"€");

            String[] ubicacion = evento.getUbicacion().split(",");
            editUbicacionLatitudEvento.setText(ubicacion[0]);
            editUbicacionLongitudEvento.setText(ubicacion[1]);
            double latitud = Double.parseDouble( editUbicacionLatitudEvento.getText().toString());
            double longitud = Double.parseDouble(editUbicacionLongitudEvento.getText().toString());
            addMap(latitud,longitud);

            //seleccionamos cliente
            for (int i=0; i < listUserCliente.size(); i++){
                if(listUserCliente.get(i).getId() == evento.getIdCliente()){
                    int id = listUserCliente.get(i).getId();
                    int position = i;
                    spinnerClienteEvento.setSelection(i);
                }
            }

            //seleccionamos organizador
           for (int i=0; i < listUserOrganizador.size(); i++){
               if(listUserOrganizador.get(i).getId() == evento.getIdOrganizador()){
                   int id = listUserOrganizador.get(i).getId();
                   int position = i;
                   spinnerOrganEvento.setSelection(i);
               }
           }

            //seleccionamos TipoEvento
            spinnerTipoEvento.setSelection(Evento.tipoEventoByInt(evento.getTipoEvento()));
            buttonDeleteEvento.show();
            salModificar = true;
        }else{
            evento = new Evento();
            title_evento_gestionar.setText(R.string.title_evento_aniadir);
            buttonDeleteEvento.hide();
            salModificar = false;
        }
    }

    private void addMap(double latitud, double longitud) {
        //mostramos el fragment
        Fragment fragment = new MapsFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.mapView_fragment,fragment);
        //pasamos el usuario de la sesion
        try {
            Bundle data = new Bundle();
            data.putSerializable("nombreEvento", editNombreEvento.getText().toString());
            data.putSerializable("latitud", latitud);
            data.putSerializable("longitud", longitud);
            fragment.setArguments(data);
        } catch (NumberFormatException e){
        }
        fragmentTransaction.commit();
    }
    private void addButtonDelete() {
        buttonDeleteEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long result =  connection.deleteEvento(evento.getId());
                if(result > 0) {
                    Toasty.success(v.getContext(), "Delete the event", Toast.LENGTH_SHORT).show();
                    //nos redirigimos al usuario
                    redireccionamiento();
                }else{
                    Toasty.error(v.getContext(), "Error deleted the event", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void addButtonRefresh() {
        FloatingActionButton buttonRefreshUbicacion = (FloatingActionButton) findViewById(R.id.buttonRefreshUbicacion);
        buttonRefreshUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double latitud = Double.parseDouble( editUbicacionLatitudEvento.getText().toString());
                double longitud = Double.parseDouble(editUbicacionLongitudEvento.getText().toString());
                addMap(latitud,longitud);
            }
        });
    }
    private void redireccionamiento(){
        //nos redirigimos al usuario
        Intent intent = null;
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
    private void addFechaEvento() {
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
    public void mostrarDatePickerDilog( int year, int month, int day){
        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDday) {
                //recogemos la fecha y la mostramos en el editText de la Fecha de Nacimiento
                editFechaEvento.setText(mDday+"/"+(mMonth+ 1)+"/"+mYear);
            }
        },day,month,year);
        //mostramos el dilog
        dpd.show();
    }
    private void addSpinners() {
        // addSpinnerOrganizador
        listUserOrganizador = connection.spinnerByOrganizador();
        // Cree un ArrayAdapter usando un string array y un spinner layout predeterminado
        ArrayAdapter<Usuario> adapterOrganizador = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item,listUserOrganizador);
        // especificamos el tipo de layout que queremos mostrar en el spinner
       adapterOrganizador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // aplicamos el adpatador al spinner
       spinnerOrganEvento.setAdapter(adapterOrganizador);

       //addSpinnerCliente
        listUserCliente = connection.listUsuariosByCliente();
        // Cree un ArrayAdapter usando un string array y un spinner layout predeterminado
        ArrayAdapter<Usuario> adapterCliente = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item,listUserCliente);
        // especificamos el tipo de layout que queremos mostrar en el spinner
        adapterCliente.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // aplicamos el adpatador al spinner
        spinnerClienteEvento.setAdapter(adapterCliente);

        //addSpinnerTipoEvento
        // Cree un ArrayAdapter usando un string array y un spinner layout predeterminado
        ArrayAdapter<CharSequence> adapterTipoEvento = ArrayAdapter.createFromResource(getApplicationContext(),R.array.tipoEvento_array, android.R.layout.simple_spinner_item);
        // especificamos el tipo de layout que queremos mostrar en el spinner
        adapterTipoEvento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // aplicamos el adpatador al spinner
        spinnerTipoEvento.setAdapter(adapterTipoEvento);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}