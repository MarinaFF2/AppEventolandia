package com.example.appeventolandia.cliente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.TextView;
import com.example.appeventolandia.R;
import com.example.appeventolandia.entidades.Evento;
import com.example.appeventolandia.entidades.Usuario;
import com.example.appeventolandia.comun.MapsFragment;

public class VisualizarEventoActivity extends AppCompatActivity {
    //variables necesarias para la clase
    private Usuario userSesion;
    private Evento eventSesion;

    /**
     * metodo para crear la actividad
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_evento);

        addMenu();//añadimos menu
        addSesion();//guardamos la sesion
        addData(); //rellenamos los campos
    }

    /**
     * metodo para añadir el menu
     */
    private void addMenu() {
        //añadimos el action bar a la activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.eventolandia);
        //añadimos flecha para atrás a la activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    /**
     * metodo para guardar la sesion
     */
    private void addSesion(){
        //recogemos la cookie del usuario y del evento
        userSesion = (Usuario) getIntent().getSerializableExtra("userSesion");
        eventSesion = (Evento) getIntent().getSerializableExtra("eventSesion");
    }

    /**
     * metodo para rellenar los campos
     */
    private void addData() {
        //declaramos los elementos
        TextView txNombreShowEvento = (TextView) findViewById(R.id.txNombreShowEvento);
        TextView txDescripcionShowEvento = (TextView) findViewById(R.id.txDescripcionShowEvento);
        TextView txTipoShowEvento = (TextView) findViewById(R.id.txTipoShowEvento);
        TextView txOrganShowEvento = (TextView) findViewById(R.id.txOrganShowEvento);
        TextView txFechaShowEvento = (TextView) findViewById(R.id.txFechaShowEvento);
        TextView txDuracionShowEvento = (TextView) findViewById(R.id.txDuracionShowEvento);
        TextView txPrecioShowEvento = (TextView) findViewById(R.id.txPrecioShowEvento);

        //rellenamos los elementos
        txNombreShowEvento.setText(eventSesion.getNombre());
        txDescripcionShowEvento.setText(eventSesion.getDescripcion());
        txTipoShowEvento.setText(eventSesion.getTipoEvento());
        txOrganShowEvento.setText(eventSesion.getIdOrganizador()+"");
        txFechaShowEvento.setText(eventSesion.getFecha());
        txDuracionShowEvento.setText(eventSesion.getDuracion()+"h");
        txPrecioShowEvento.setText(eventSesion.getPrecio()+"€");
        String[] ubicacion = eventSesion.getUbicacion().split(",");
        double latitud = Double.parseDouble(ubicacion[0]);
        double longitud = Double.parseDouble(ubicacion[1]);
        //ponemos la nueva ubicacion
        addMap(latitud,longitud,txNombreShowEvento);
    }

    /**
     * metodo para poner la ubicacion en el mapa
     * @param latitud
     * @param longitud
     * @param txNombreShowEvento
     */
    private void addMap(double latitud, double longitud,TextView txNombreShowEvento) {
        //mostramos el fragment
        Fragment fragment = new MapsFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.mapView_fragment,fragment);
        //pasamos el usuario de la sesion
        try {
            Bundle data = new Bundle();
            data.putSerializable("nombreEvento", txNombreShowEvento.getText().toString());
            data.putSerializable("latitud", latitud);
            data.putSerializable("longitud", longitud);
            fragment.setArguments(data);
        } catch (NumberFormatException e){
        }
        fragmentTransaction.commit();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}