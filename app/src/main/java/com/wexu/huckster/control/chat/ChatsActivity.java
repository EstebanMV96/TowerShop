package com.wexu.huckster.control.chat;

import android.content.Context;
import android.content.Intent;

import android.net.Uri;

import android.content.SharedPreferences;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wexu.huckster.R;
import com.wexu.huckster.control.HiloRefrescoC;
import com.wexu.huckster.control.productos.ProductsActivity;
import com.wexu.huckster.control.vendedor.SellerActivity;
import com.wexu.huckster.modelo.Huckster;
import com.wexu.huckster.modelo.Producto;

import java.util.ArrayList;

public class ChatsActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Huckster principal;
    private HiloRefrescoC hilo;
    private ConversationsFragment leadsFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);
        Bundle extras=getIntent().getExtras();
        principal=(Huckster) extras.getSerializable("modelo");

        String info=extras.getString("info");

        SharedPreferences preferencias=getSharedPreferences("datos", Context.MODE_PRIVATE);


        agregarToolbar();

         leadsFragment = (ConversationsFragment)
                getSupportFragmentManager().findFragmentById(R.id.leads_container);
            Log.d("ANTES","OK");
        if (leadsFragment == null) {
            Log.d("DESPUES","OK");
            leadsFragment = ConversationsFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putSerializable("modelo",principal);
            leadsFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.leads_container, leadsFragment)
                    .commit();
        }

        //MANEJO DEL NAVIGATION VIEW
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            prepararDrawer(navigationView);
            // Seleccionar item por defecto
            //seleccionarItem(navigationView.getMenu().getItem(0));
        }


        hilo=new HiloRefrescoC(this,principal,preferencias);
        hilo.execute();


    }

    //METODOS NAVIGATION VIEW
    private void prepararDrawer(NavigationView navigationView) {
        navigationView.setCheckedItem(R.id.item_messages);
        View headerLayout = navigationView.getHeaderView(0);
        // PONER FOTO DE PERFIL
        final ImageView profile = (ImageView) headerLayout.findViewById(R.id.profilePicture);
        Uri photo= Uri.parse(principal.darUsuario().getImg());
        Glide.with(getApplicationContext()).load(photo).into(profile);

        //PONER NOMBRE DE LA PERSONA
        final TextView nombre= (TextView)headerLayout.findViewById(R.id.txtNombre);
        nombre.setText(principal.darUsuario().getNombre());
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        seleccionarItem(menuItem);
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });

    }
    private void seleccionarItem(MenuItem itemDrawer) {

        Intent i= null;

        switch (itemDrawer.getItemId()) {
            case R.id.item_buy:
                if(hilo!=null)
                    hilo.destruir();
                i= new Intent(getApplicationContext(), ProductsActivity.class);
                i.putExtra("modelo",principal);
                startActivity(i);
                break;
            case R.id.item_sell:
                if(hilo!=null)
                    hilo.destruir();
                i= new Intent(getApplicationContext(), SellerActivity.class);
                i.putExtra("modelo",principal);
                startActivity(i);
                break;
            case R.id.item_messages:
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.item_logout:
                // Iniciar actividad de configuración
                break;
        }



    }


    private void agregarToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // Poner ícono del drawer toggle
            ab.setHomeAsUpIndicator(R.drawable.menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setTitle("TowerShop");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void refrescar()
    {

        leadsFragment.refresh();
    }



}
