package com.example.appeventolandia;

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
import com.example.appeventolandia.entidades.Usuario;
import com.example.appeventolandia.fragmentsComun.PerfilFragment;
import com.example.appeventolandia.fragmentsComun.WelcomeFragment;
import com.example.appeventolandia.admin.GestionarUsuariosFragment;
import com.example.appeventolandia.organizador.GestionarEventosFragment;
import com.google.android.material.navigation.NavigationView;

public class MainAdminOrganizadorActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {
    private Usuario userSesion = null;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin_organizador);

        addMenu(); //añadimos menu
        addFragment();//añadimos fragment
        addNavigationView(); // añadimos navigation view
        addUserSession();
    }

    private void addUserSession() {
        userSesion = (Usuario) getIntent().getExtras().getSerializable("userSesion");
    }

    private void addNavigationView() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void addFragment() {
        //mostramos el fragment
        Fragment fragment = new WelcomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.content_fragment,fragment);
        //pasamos el usuario de la sesion
        Bundle data = new Bundle();
        data.putSerializable("userSesion",userSesion);
        fragment.setArguments(data);

        fragmentTransaction.commit();
    }

    private void addMenu() {
        //añadimos el action bar a la activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //ponemos el icono de la app
        toolbar.setLogo(R.drawable.eventolandia);

        //añadimos el DrawerLayout
        DrawerLayout drawer_layout = (DrawerLayout) findViewById(R.id.drawer_admin_organizador_layout);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawer_layout,toolbar,R.string.nav_open_drawer,R.string.nav_close_drawer);
        drawer_layout.addDrawerListener(drawerToggle); //lo añadimos al drawer layout
        drawerToggle.syncState(); //sincronizamos
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.nav_perfil_adminorg:
                fragment = new PerfilFragment();
                break;
            case R.id.nav_usuarios_adminorg:
                fragment = new GestionarUsuariosFragment();
                break;
            case R.id.nav_eventos_adminorg:
                fragment = new GestionarEventosFragment();
                break;
            case R.id.nav_cerrarSesion_cliente:
                fragment = null;
                Intent intent = new Intent(this,InicioSesionActivity.class);
                startActivity(intent);
                break;
            default:
                fragment = new WelcomeFragment();
                break;
        }

        if(fragment != null){
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_fragment,fragment);

            //pasamos el usuario de la sesion
            Bundle data = new Bundle();
            data.putSerializable("userSesion",userSesion);
            fragment.setArguments(data);

            fragmentTransaction.commit();

            DrawerLayout drawer_layout = (DrawerLayout) findViewById(R.id.drawer_admin_organizador_layout);
            drawer_layout.closeDrawer(GravityCompat.START);
        }
        return true;
    }
    public void onBackPressed(){
        DrawerLayout drawer_layout = (DrawerLayout) findViewById(R.id.drawer_admin_organizador_layout);
        if(drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }
}