package com.example.appeventolandia;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.appeventolandia.entidades.Evento;
import com.example.appeventolandia.entidades.Usuario;

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

    private Evento evento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);

        declaracionElementos();
        addSpinnerTipoEvento();
        addSpinnerCliente();
        addSpinnerOrganizador();
        addFechaEvento();
        addButtonSave();
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

    private void declaracionElementos() {
        editNombreEvento = (EditText) findViewById(R.id.editNombreEvento);
        editDescripcionEvento = (EditText) findViewById(R.id.editDescripcionEvento);
        spinnerTipoEvento = (Spinner) findViewById(R.id.spinnerTipoEvento);
        spinnerClienteEvento = (Spinner) findViewById(R.id.spinnerClienteEvento);
        spinnerOrganEvento = (Spinner) findViewById(R.id.spinnerOrganEvento);
        editUbicacionEvento = (EditText) findViewById(R.id.editUbicacionEvento);
        editFechaEvento = (EditText) findViewById(R.id.editFechaEvento);
        editDuracionEvento = (EditText) findViewById(R.id.editDuracionEvento);
        editPrecioEvento = (EditText) findViewById(R.id.editPrecioEvento);
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


        //añadimos evento del spinnerOrganEvento
        spinnerOrganEvento.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Usuario user  = (Usuario) parent.getItemAtPosition(position);
                evento.setIdOrganizador(user.getId());
            }
        });
    }

    private void addSpinnerCliente() {


        //añadimos evento del spinnerClienteEvento
        spinnerClienteEvento.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Usuario user  = (Usuario) parent.getItemAtPosition(position);
                evento.setIdCliente(user.getId());
            }
        });
    }

    private void addSpinnerTipoEvento() {
        // Cree un ArrayAdapter usando un string array y un spinner layout predeterminado
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.tipoEvento_array, android.R.layout.simple_spinner_item);
        // especificamos el tipo de layout que queremos mostrar en el spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // aplicamos el adpatador al spinner
        spinnerTipoEvento.setAdapter(adapter);

        //añadimos evento del spinnerTipoEvento
        spinnerTipoEvento.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String tipo  = (String) parent.getItemAtPosition(position);
                evento.setTipoEvento(tipo);
            }
        });
    }
}