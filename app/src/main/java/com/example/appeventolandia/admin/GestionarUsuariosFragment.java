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
import com.google.android.material.tabs.TabLayout;


public class GestionarUsuariosFragment extends Fragment {
    private ViewPager pager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gestionar_usuarios, container, false);
    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addViewPager(view);
        addTabLayout(view);
    }

    private void addViewPager(View view) {
        //añadimos adaptador view pager
        SectionPagerAdapter pagerAdapter = new SectionPagerAdapter(getFragmentManager());
        pager = (ViewPager) view.findViewById(R.id.pager);
        pager.setAdapter(pagerAdapter);
    }

    private void addTabLayout(View view) {
        //añadimos el tabLayoutr a la activity
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);
    }

    private class SectionPagerAdapter extends FragmentPagerAdapter {

        public SectionPagerAdapter(@NonNull FragmentManager fm){
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            //el fragment que va a mostrar
            switch (position){
                case 0:
                    return new GestionarClienteFragment();
                case 1:
                    return new GestionarOrganizadorFragment();
                case 2:
                    return new GestionarAdministradorFragment();
                case 3:
                    return new GestionarAdminOrgFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            //numero de paginas que va a mostrar
            return 4;
        }

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