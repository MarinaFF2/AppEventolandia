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
import com.example.appeventolandia.R;
import com.example.appeventolandia.cliente.VisualizarEventoActivity;
import com.example.appeventolandia.entidades.Evento;
import com.example.appeventolandia.entidades.Usuario;
import java.util.ArrayList;

public class EventosCardViewAdapter extends RecyclerView.Adapter<EventosCardViewAdapter.EventoViewHolder>{
    //variables necesarias para la clase
    private ArrayList<Evento> listEvents;
    private Context context;
    private Evento e;
    private Usuario userSesion;

    /**
     * constructor
     * @param listEvents
     * @param userSesion
     * @param context
     */
    public EventosCardViewAdapter(ArrayList<Evento> listEvents, Usuario userSesion, Context context) {
        //rellenamos las variables
        this.listEvents = listEvents;
        this.context = context;
        this.userSesion = userSesion;
    }

    /**
     * creamos el carview
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public EventoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //enlazamos la clase con el layout
        return new EventoViewHolder((CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_evento,parent,false));
    }

    /**
     * para trabajar el cardview
     * @param eventoViewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull EventoViewHolder eventoViewHolder, int position) {
        //conseguimos el evento de la posición en la que estamos
        e = listEvents.get(position);
        //mostramos textos
        eventoViewHolder.ImgcvTipoEvento.setImageResource(e.getFoto());
        eventoViewHolder.tvcvFechaEvento.setText(e.getFecha());
        eventoViewHolder.tvcvNombreEvento.setText(e.getNombre());
        eventoViewHolder.tvcvTipoEvento.setText(e.getTipoEvento());
        //cambiar de color el cardview
        eventoViewHolder.cvEvento.setCardBackgroundColor(e.getColor());
        //cuando hacemos click
        eventoViewHolder.cvEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //conseguimos el evento de la posición en la que estamos
                e = listEvents.get(position);
                //creamos el bundle para pasar el evento
                Intent intent = new Intent(context, VisualizarEventoActivity.class);
                //guardamos los datos que necesitamos
                intent.putExtra("eventSesion",e);
                intent.putExtra("userSesion",userSesion);
                context.startActivity(intent);
            }
        });
    }

    /**
     * metodo para saber el total de items
     * @return
     */
    @Override
    public int getItemCount() {
        return listEvents.size();
    }

    /**
     * clase interna para tratar del cardview
     */
    public static class EventoViewHolder extends RecyclerView.ViewHolder {
        //variables de los elementos
        private ImageView ImgcvTipoEvento;
        private TextView tvcvFechaEvento;
        private TextView tvcvNombreEvento;
        private TextView tvcvTipoEvento;
        private CardView cvEvento;

        /**
         * constructor
         * @param itemView
         */
        public EventoViewHolder(@NonNull View itemView) {
            super(itemView);

            //declaramos las variables de los elementos
            cvEvento = (CardView) itemView.findViewById(R.id.cvEvento);
            ImgcvTipoEvento = (ImageView) itemView.findViewById(R.id.ImgcvTipoEvento);
            tvcvFechaEvento = (TextView) itemView.findViewById(R.id.tvcvFechaEvento);
            tvcvNombreEvento = (TextView) itemView.findViewById(R.id.tvcvNombreEvento);
            tvcvTipoEvento = (TextView) itemView.findViewById(R.id.tvcvTipoEvento);
        }
    }
}
