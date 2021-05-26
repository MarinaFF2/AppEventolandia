package com.example.appeventolandia.cliente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.widget.TextView;
import com.example.appeventolandia.R;
import com.example.appeventolandia.entidades.Evento;
import com.example.appeventolandia.entidades.Usuario;

public class VisualizarEventoActivity extends AppCompatActivity {
    private Usuario userSesion;
    private Evento eventSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_evento);

        addMenu();
        addSesion();
        addData();
    }
    private void addMenu() {
        //añadimos el action bar a la activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.eventolandia);
        //añadimos flecha para atrás a la activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    private void addSesion(){
        //recogemos la cookie del usuario
        userSesion = (Usuario) getIntent().getSerializableExtra("userSesion");
        eventSesion = (Evento) getIntent().getSerializableExtra("eventSesion");

    }
    private void addData() {
        TextView txNombreShowEvento = (TextView) findViewById(R.id.txNombreShowEvento);
        TextView txDescripcionShowEvento = (TextView) findViewById(R.id.txDescripcionShowEvento);
        TextView txTipoShowEvento = (TextView) findViewById(R.id.txTipoShowEvento);
        TextView txOrganShowEvento = (TextView) findViewById(R.id.txOrganShowEvento);
        TextView txUbicacionShowEvento = (TextView) findViewById(R.id.txUbicacionShowEvento);
        TextView txFechaShowEvento = (TextView) findViewById(R.id.txFechaShowEvento);
        TextView txDuracionShowEvento = (TextView) findViewById(R.id.txDuracionShowEvento);
        TextView txPrecioShowEvento = (TextView) findViewById(R.id.txPrecioShowEvento);

        txNombreShowEvento.setText(eventSesion.getNombre());
        txDescripcionShowEvento.setText(eventSesion.getDescripcion());
        txTipoShowEvento.setText(eventSesion.getTipoEvento());
        txOrganShowEvento.setText(eventSesion.getIdOrganizador()+"");
        txUbicacionShowEvento.setText(eventSesion.getUbicacion());
        txFechaShowEvento.setText(eventSesion.getFecha());
        txDuracionShowEvento.setText(eventSesion.getDuracion()+"h");
        txPrecioShowEvento.setText(eventSesion.getPrecio()+"€");
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}