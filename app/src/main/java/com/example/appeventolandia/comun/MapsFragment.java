package com.example.appeventolandia.comun;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appeventolandia.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsFragment extends Fragment{
    //variables necesarias para la clase
    private double latitud;
    private double longitud;
    private String nombreEvento;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            //creamos la nueva marca con la ubicacion
            LatLng ubicacion = new LatLng(latitud, longitud);
            googleMap.addMarker(new MarkerOptions().position(ubicacion).title("Marcada la ubicación del evento: "+ nombreEvento));

            UiSettings uiSettings = googleMap.getUiSettings();
            uiSettings.setCompassEnabled(true);
            uiSettings.setZoomControlsEnabled(true);
            uiSettings.setTiltGesturesEnabled(true);
            uiSettings.setScrollGesturesEnabled(true);
            uiSettings.setZoomGesturesEnabled(true);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion,20)); //creamos nueva ubicacion
        }
    };
    /**
     * metodo para relacionar el fragment
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }
    /**
     * metodo para crear la vista
     * @param savedInstanceState
     * @return
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        //recogemos los parametros que nos han pasado
        Bundle data = getArguments();
        if(data !=null) {
            //los guardamos
            latitud = data.getDouble("latitud");
            longitud = data.getDouble("longitud");
            nombreEvento = data.getString("nombreEvento");

            if (mapFragment != null) {
                mapFragment.getMapAsync(callback);
            }
        }
    }

}