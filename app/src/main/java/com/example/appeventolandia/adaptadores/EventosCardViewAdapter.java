package com.example.appeventolandia;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appeventolandia.ConexionBBDD.ConexionBBDD;
import com.example.appeventolandia.entidades.Evento;
import com.example.appeventolandia.entidades.Usuario;

import java.util.ArrayList;

public class EventosCardViewAdapter extends RecyclerView.Adapter<EventosCardViewAdapter.EventoViewHolder>{

    private ArrayList<Evento> listEvents;
    private Context context;
    private ConexionBBDD connection;
    private Evento e;
    private Usuario userSesion;

    public EventosCardViewAdapter(ArrayList<Evento> listEvents, Usuario userSesion, Context context) {
        connection = new ConexionBBDD(context,"bd_events",null,2);
        this.listEvents = listEvents;
        this.context = context;
        this.userSesion = userSesion;
    }

    @NonNull
    @Override
    public EventoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //enlazamos la clase con el layout
        return new EventoViewHolder((CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_evento,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull EventoViewHolder eventoViewHolder, int position) {
        //conseguimos el evento de la posición en la que estamos
        e = listEvents.get(position);
        //ponemos el evento para poder cambiar de color el cardview
        eventoViewHolder.setE(e);
        //mostramos textos
        //eventoViewHolder.imgCvFoto.setImageResource(p.getFoto());
        //eventoViewHolder.tvCvNombre.setText(p.getName());


        eventoViewHolder.cvEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creamos el bundle para pasarle la mascota
                Intent intent = new Intent(context, EventoActivity.class);
                intent.putExtra("evento",e);
                intent.putExtra("userSesion",userSesion);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public static class EventoViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgCvFoto;
        private TextView tvCvLike;
        private CardView cvEvento;
        private Evento e;

        public EventoViewHolder(@NonNull View itemView) {
            super(itemView);

            cvEvento = itemView.findViewById(R.id.cvEvento);
            imgCvFoto = (ImageView) itemView.findViewById(R.id.imgCvFoto);

            cvEvento.setCardBackgroundColor(e.getColor());

        }

        public void setE(Evento e) {
            this.e = e;
        }
    }
}
