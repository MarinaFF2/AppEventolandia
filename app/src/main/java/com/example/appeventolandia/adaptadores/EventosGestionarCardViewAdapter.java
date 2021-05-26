package com.example.appeventolandia.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appeventolandia.ConexionBBDD.ConexionBBDD;
import com.example.appeventolandia.R;
import com.example.appeventolandia.entidades.Evento;
import com.example.appeventolandia.entidades.Usuario;
import com.example.appeventolandia.organizador.EventoActivity;
import java.util.ArrayList;

public class EventosGestionarCardViewAdapter extends RecyclerView.Adapter<EventosGestionarCardViewAdapter.EventoViewHolder>{

    private ArrayList<Evento> listEvents;
    private Context context;
    private ConexionBBDD connection;
    private Evento e;
    private Usuario userSesion;

    public EventosGestionarCardViewAdapter(ArrayList<Evento> listEvents, Usuario userSesion, Context context) {
        connection = new ConexionBBDD(context,"bd_events",null,2);
        this.listEvents = listEvents;
        this.context = context;
        this.userSesion = userSesion;
    }

    @NonNull
    @Override
    public EventoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //enlazamos la clase con el layout
        return new EventoViewHolder((CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_evento_gestionar,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull EventoViewHolder eventoViewHolder, int position) {
        //conseguimos el evento de la posición en la que estamos
        e = listEvents.get(position);
        //mostramos textos
        eventoViewHolder.ImgcvTipoEventoGestionar.setImageResource(e.getFoto());
        eventoViewHolder.tvcvFechaEventoGestionar.setText(e.getFecha());
        eventoViewHolder.tvcvNombreEventoGestionar.setText(e.getNombre());
        Usuario userCliente = connection.existUserById(e.getIdCliente());
        eventoViewHolder.tvcvClienteEventoGestionar.setText(userCliente.getNombreApellidos());
        //cambiar de color el cardview
        eventoViewHolder.cvEventoGestionar.setCardBackgroundColor(e.getColor());
        //cuando hacemos click
        eventoViewHolder.cvEventoGestionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creamos el bundle para pasar el evento
                Intent intent = new Intent(context, EventoActivity.class);
                intent.putExtra("evento",e);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listEvents.size();
    }

    public static class EventoViewHolder extends RecyclerView.ViewHolder {
        private ImageView ImgcvTipoEventoGestionar;
        private TextView tvcvFechaEventoGestionar;
        private TextView tvcvNombreEventoGestionar;
        private TextView tvcvClienteEventoGestionar;
        private CardView cvEventoGestionar;

        public EventoViewHolder(@NonNull View itemView) {
            super(itemView);

            cvEventoGestionar = (CardView) itemView.findViewById(R.id.cvEventoGestionar);
            ImgcvTipoEventoGestionar = (ImageView) itemView.findViewById(R.id.ImgcvTipoEventoGestionar);
            tvcvFechaEventoGestionar = (TextView) itemView.findViewById(R.id.tvcvFechaEventoGestionar);
            tvcvNombreEventoGestionar = (TextView) itemView.findViewById(R.id.tvcvNombreEventoGestionar);
            tvcvClienteEventoGestionar = (TextView) itemView.findViewById(R.id.tvcvClienteEventoGestionar);
        }
    }
}
