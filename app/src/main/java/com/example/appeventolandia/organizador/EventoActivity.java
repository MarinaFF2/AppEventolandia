package com.example.appeventolandia.organizador;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.appeventolandia.ConexionBBDD.ConexionBBDD;
import com.example.appeventolandia.R;
import com.example.appeventolandia.entidades.Evento;
import com.example.appeventolandia.entidades.Usuario;

import java.util.ArrayList;
import java.util.Calendar;

public class EventoActivity extends AppCompatActivity {
    private EditText editNombreEvento;
    private EditText editDescripcionEvento;
    private Spinner spinnerTipoEvento;
    private Spinner spinnerClienteEvento;
    private Spinner spinnerOrganEvento;
    private EditText editUbicacionEvento;
    private EditText editFechaEvento;
    private EditText editDuracionEvento;
    private EditText editPrecioEvento;

    private ConexionBBDD connection;
    private Evento evento;
    private ArrayList<Usuario> listUserOrganizador;
    private ArrayList<Usuario> listUserCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);

        addMenu(); //añadimos menu
        addData();
        addEventSpinner();
        addFechaEvento();
        addButtonSave();
    }

    private void addEventSpinner() {
        //añadimos evento del spinnerOrganEvento
     /*   spinnerOrganEvento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

      */
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
                saveEvento();
            }
        });
    }
    private void saveEvento() {

    }
    private void addData() {
        //hacemos la conexión con la BBDD
        connection = new ConexionBBDD(this,"bd_events",null,2);

        TextView title_evento_gestionar = (TextView) findViewById(R.id.title_evento_gestionar);
        editNombreEvento = (EditText) findViewById(R.id.editNombreEvento);
        editDescripcionEvento = (EditText) findViewById(R.id.editDescripcionEvento);
        spinnerTipoEvento = (Spinner) findViewById(R.id.spinnerTipoEvento);
        spinnerClienteEvento = (Spinner) findViewById(R.id.spinnerClienteEvento);
        spinnerOrganEvento = (Spinner) findViewById(R.id.spinnerOrganEvento);
        editUbicacionEvento = (EditText) findViewById(R.id.editUbicacionEvento);
        editFechaEvento = (EditText) findViewById(R.id.editFechaEvento);
        editDuracionEvento = (EditText) findViewById(R.id.editDuracionEvento);
        editPrecioEvento = (EditText) findViewById(R.id.editPrecioEvento);

        addSpinnerOrganizador();
        addSpinnerCliente();
        addSpinnerTipoEvento();

        if((Evento) getIntent().getExtras().getSerializable("evento") != null){
            title_evento_gestionar.setText(R.string.title_evento_modificar);

            evento = (Evento) getIntent().getExtras().getSerializable("evento");

            editNombreEvento.setText(evento.getNombre());
            editDescripcionEvento.setText(evento.getDescripcion());
            editUbicacionEvento.setText(evento.getUbicacion());
            editFechaEvento.setText(evento.getFecha());
            editDuracionEvento.setText(evento.getDuracion()+"");
            editPrecioEvento.setText(evento.getPrecio()+"€");

            //seleccionamos cliente
            for (int i=0; i < listUserCliente.size(); i++){
                if(listUserCliente.get(i).getId() == evento.getIdCliente()){
                    spinnerClienteEvento.setSelection(i);
                    break;
                }
            }
            spinnerClienteEvento.setSelection(evento.getIdCliente());
            //seleccionamos organizador
           for (int i=0; i < listUserOrganizador.size(); i++){
               if(listUserOrganizador.get(i).getId() == evento.getIdOrganizador()){
                   spinnerOrganEvento.setSelection(i);
                   break;
               }
           }
            //seleccionamos TipoEvento
            spinnerTipoEvento.setSelection(Evento.tipoEventoByInt(evento.getTipoEvento()));
        }else{
            evento = new Evento();
            title_evento_gestionar.setText(R.string.title_evento_aniadir);
        }
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
    private void addSpinnerOrganizador() {
        listUserOrganizador = connection.listUsuariosByOrganizador();
        // Cree un ArrayAdapter usando un string array y un spinner layout predeterminado
        ArrayAdapter<Usuario> adapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item,listUserOrganizador);
        // especificamos el tipo de layout que queremos mostrar en el spinner
       adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // aplicamos el adpatador al spinner
       spinnerOrganEvento.setAdapter(adapter);
    }
    private void addSpinnerCliente() {
        listUserCliente = connection.listUsuariosByCliente();
        // Cree un ArrayAdapter usando un string array y un spinner layout predeterminado
        ArrayAdapter<Usuario> adapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item,listUserCliente);
        // especificamos el tipo de layout que queremos mostrar en el spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // aplicamos el adpatador al spinner
        spinnerClienteEvento.setAdapter(adapter);
    }
    private void addSpinnerTipoEvento() {
        // Cree un ArrayAdapter usando un string array y un spinner layout predeterminado
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.tipoEvento_array, android.R.layout.simple_spinner_item);
        // especificamos el tipo de layout que queremos mostrar en el spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // aplicamos el adpatador al spinner
        spinnerTipoEvento.setAdapter(adapter);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}