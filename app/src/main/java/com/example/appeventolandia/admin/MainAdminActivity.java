package com.example.appeventolandia.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.appeventolandia.comun.InicioSesionActivity;
import com.example.appeventolandia.R;
import com.example.appeventolandia.entidades.Usuario;
import com.example.appeventolandia.comun.PerfilFragment;
import com.example.appeventolandia.comun.WelcomeFragment;
import com.google.android.material.navigation.NavigationView;

public class MainAdminActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {
    //variables necesarias para la clase
    private Usuario userSesion;
    private Fragment fragment;

    /**
     * metodo para crear la actividad
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        addUserSession();//recogemos la userSesion
        addMenu(); //añadimos menu
        addFragment();//añadimos fragment
        addNavigationView(); // añadimos navigation view
    }

    /**
     * metodo para guardar el usuario de la sesion
     */
    private void addUserSession() {
        //guardamos el usario
        userSesion = (Usuario) getIntent().getExtras().getSerializable("userSesion");
    }

    /**
     * metodo para añadir el NavigationView
     */
    private void addNavigationView() {
        //añadimos el NavigationView
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * metodo para añadimos el fragment
     */
    private void addFragment() {
        //mostramos el fragment
        fragment = new WelcomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.content_fragment,fragment);
        fragmentTransaction.commit();
    }

    /**
     * metodo para añadir el menu
     */
    private void addMenu() {
        //añadimos el action bar a la activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //ponemos el icono de la app
        toolbar.setLogo(R.drawable.eventolandia);

        //añadimos el DrawerLayout
        DrawerLayout drawer_layout = (DrawerLayout) findViewById(R.id.drawer_admin_layout);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawer_layout,toolbar,R.string.nav_open_drawer,R.string.nav_close_drawer);
        drawer_layout.addDrawerListener(drawerToggle); //lo añadimos al drawer layout
        drawerToggle.syncState(); //sincronizamos
    }

    /**
     * metodo para recoger el tiem selecionado
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.nav_perfil_admin:
                fragment = new PerfilFragment();
                break;
            case R.id.nav_usuarios_admin:
                fragment = new GestionarUsuariosFragment();
                break;
            case R.id.nav_cerrarSesion_cliente:
                fragment = null;
                Intent intent = new Intent(this, InicioSesionActivity.class);
                startActivity(intent);
                break;
            default:
                fragment = new WelcomeFragment();
                break;
        }

        //si hay fragment rellenamos el fragment
        if(fragment != null){
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_fragment,fragment);

            //pasamos el usuario de la sesion
            Bundle data = new Bundle();
            data.putSerializable("userSesion",userSesion);
            fragment.setArguments(data);

            fragmentTransaction.commit();

            DrawerLayout drawer_layout = (DrawerLayout) findViewById(R.id.drawer_admin_layout);
            drawer_layout.closeDrawer(GravityCompat.START);
        }

        return true;
    }

    public void onBackPressed(){
        DrawerLayout drawer_layout = (DrawerLayout) findViewById(R.id.drawer_admin_layout);
        if(drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }
}