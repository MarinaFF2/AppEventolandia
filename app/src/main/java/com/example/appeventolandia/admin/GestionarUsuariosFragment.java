package com.example.appeventolandia.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.example.appeventolandia.R;
import com.example.appeventolandia.entidades.Usuario;
import com.google.android.material.tabs.TabLayout;


public class GestionarUsuariosFragment extends Fragment {
    //variables necesarias para la clase
    private ViewPager pager;
    private Usuario userSesion;

    /**
     * llamamos al metodo onCreate
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * metodo para relacionar el fragment
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gestionar_usuarios, container, false);
    }
    /**
     * metodo para crear la vista
     * @param view
     * @param savedInstanceState
     */
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addViewPager(view); //añadimos el ViewPager
        addTabLayout(view);//añadimos el TabLayout
        addUserSesion(); // guardar la sesion
    }

    /**
     * metodo para guardar la sesion
     */
    private void addUserSesion(){
        //recogemos la cookie del usuario
        userSesion = null;
        Bundle data = this.getArguments();
        if(data != null){
            userSesion = (Usuario) data.getSerializable("userSesion");
        }
    }

    /**
     * metodo para añadir el ViewPager
     * @param view
     */
    private void addViewPager(View view) {
        //añadimos adaptador view pager
        SectionPagerAdapter pagerAdapter = new SectionPagerAdapter(getFragmentManager());

        //añadimos el apadtador al ViewPager
        pager = (ViewPager) view.findViewById(R.id.pagerGestionUsuarios);
        pager.setAdapter(pagerAdapter);
    }

    /**
     * metod para añadir los TabLayout
     * @param view
     */
    private void addTabLayout(View view) {
        //añadimos el TabLayout a la activity
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);
    }

    /**
     * declaracion de clase interna
     */
    private class SectionPagerAdapter extends FragmentPagerAdapter {

        /**
         * constructor
         * @param fm
         */
        public SectionPagerAdapter(@NonNull FragmentManager fm){
            super(fm);
        }

        /**
         * metodo para saber si hay que redirigirse a otro fragment
         * @param position
         * @return
         */
        @NonNull
        @Override
        public Fragment getItem(int position) {
            //el fragment que va a mostrar
            switch (position){
                case 0:
                    return new GestionarClienteFragment(userSesion);
                case 1:
                    return new GestionarOrganizadorFragment(userSesion);
                case 2:
                    return new GestionarAdministradorFragment(userSesion);
                case 3:
                    return new GestionarAdminOrgFragment(userSesion);
            }
            return null;
        }

        /**
         * metodo para saber el total
         * @return
         */
        @Override
        public int getCount() {
            //numero de paginas que va a mostrar
            return 4;
        }

        /**
         * metodo para saber titulo de las paginas
         * @param position
         * @return
         */
        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            //para cuando hagamos click en los titulos
            switch (position){
                case 0:
                    return getResources().getText(R.string.cliente_tab);
                case 1:
                    return getResources().getText(R.string.organizador_tab);
                case 2:
                    return getResources().getText(R.string.admin_tab);
                case 3:
                    return getResources().getText(R.string.admin_orga_tab);
            }
            return null;
        }
    }
}