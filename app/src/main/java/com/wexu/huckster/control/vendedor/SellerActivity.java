package com.wexu.huckster.control.vendedor;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wexu.huckster.R;
import com.wexu.huckster.control.HiloEsperaSA;
import com.wexu.huckster.control.HiloRefrescoV;
import com.wexu.huckster.control.chat.ChatsActivity;
import com.wexu.huckster.control.productos.ProductsActivity;
import com.wexu.huckster.control.productos.ProductsAdapter;
import com.wexu.huckster.modelo.Huckster;
import com.wexu.huckster.modelo.Producto;

import java.util.ArrayList;
import java.util.List;

public class SellerActivity extends AppCompatActivity implements
        ProfileFragment.OnFragmentInteractionListener,
        AdapterView.OnItemSelectedListener
{

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    /*
    Declarar instancias globales
    */

    private Typeface nameFont;
    private Typeface priceFont;
    private Huckster principal;
    private HiloRefrescoV hilo;
    private RecyclerView.Adapter adapter;
    private DrawerLayout drawerLayout;
    private  Switch disponible;
    private SharedPreferences preferencias;
    private boolean fuiYo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);

        Log.d("SELLER","SE CREO");
        agregarToolbar();
        Bundle extras=getIntent().getExtras();
        principal=(Huckster) extras.getSerializable("modelo");
        String info=extras.getString("info");
        agregarToolbar();
        fuiYo=false;

        preferencias=getSharedPreferences("info", Context.MODE_PRIVATE);
        String estado=preferencias.getString("estadoV","NO EXISTE");




        if(info==null||info.equals(""))
        {
            Log.d("SELLER","SE CREO EL HILO");
            new HiloEsperaSA(new ProgressDialog(this),this,principal).execute();
        }else {

            lanzarFragment();

        }
        //MANEJO DEL NAVIGATION VIEW
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            prepararDrawer(navigationView);
            if(estado.equals("ACTIVO"))
            {
                fuiYo=true;
                disponible.setChecked(true);

            }


            // Seleccionar item por defecto
            //seleccionarItem(navigationView.getMenu().getItem(0));
        }



        if(disponible!=null&&disponible.isChecked())
        activo();
        else
            inactivo();


    }

    //METODOS NAVIGATION VIEW
    private void prepararDrawer(NavigationView navigationView) {
        navigationView.setCheckedItem(R.id.item_sell);

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


        disponible = (Switch) headerLayout.findViewById(R.id.switch1);
        disponible.setEnabled(true);
        disponible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {




                SharedPreferences.Editor editor=preferencias.edit();
                Log.d("IMAGINATE",disponible.isChecked()+"");
                if(disponible.isChecked()&&!fuiYo)
                {
                    principal.getDataBase().cambiarEstado(true);
                    editor.putString("estadoV","ACTIVO");
                    editor.commit();
                    activo();
                    fuiYo=false;

                }else if(!disponible.isChecked())
                {
                    Log.d("SE METIO","ESTADOSSSS");
                    principal.getDataBase().cambiarEstado(false);
                    editor.putString("estadoV","INACTIVO");
                    editor.commit();
                    inactivo();
                    fuiYo=false;
                }

            }
        });

    }

    public void activo()
    {
        Toast.makeText(this,"USTED SE ENCUENTRA ACTIVO",Toast.LENGTH_SHORT).show();
    }

    public void inactivo()
    {

        Toast.makeText(this,"USTED SE ENCUENTRA NO DISPONIBLE",Toast.LENGTH_SHORT).show();
    }


    private void seleccionarItem(MenuItem itemDrawer) {

        Intent i= null;

        switch (itemDrawer.getItemId()) {
            case R.id.item_buy:
                i= new Intent(getApplicationContext(), ProductsActivity.class);
                if(hilo!=null)
                hilo.destruir();
                i.putExtra("modelo",principal);
                startActivity(i);
                break;
            case R.id.item_sell:
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.item_messages:
                if(hilo!=null)
                    hilo.destruir();
                i= new Intent(getApplicationContext(), ChatsActivity.class);
                i.putExtra("modelo",principal);
                startActivity(i);
                break;
            case R.id.item_logout:
                // Iniciar actividad de configuración
                break;
        }



    }

    private void agregarToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // Poner ícono del drawer toggle
            ab.setHomeAsUpIndicator(R.drawable.menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setTitle("Mis productos");

    }


    public void lanzarFragment()
    {


        RecyclerView rv= (RecyclerView)findViewById(R.id.reciclador5);
        rv.setHasFixedSize(true);

        ArrayList<Producto> misProductos=principal.darUsuario().getMisProductos();
        List items = misProductos;
        adapter= new ProductsAdapter(items);
        RecyclerView.LayoutManager lManager= new LinearLayoutManager(this);

        //**********
        rv.setAdapter(adapter);
        rv.setLayoutManager(lManager);
        SharedPreferences preferencias=getSharedPreferences("datos", Context.MODE_PRIVATE);
        hilo=new HiloRefrescoV(this,principal,preferencias);
        hilo.execute();

    }




    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Bundle bundle = new Bundle();
        ArrayList<Producto> misProductos=principal.darUsuario().getMisProductos();
        bundle.putSerializable("productos",misProductos);




        nameFont= Typeface.createFromAsset(getAssets(), "fonts/Raleway-Light.ttf");
        priceFont= Typeface.createFromAsset(getAssets(), "fonts/lilita.ttf");

        //adapter.addFragment(mf, "Mis productos");

        // adapter.addFragment(pf, "Mi perfil");

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    //métodos manejo toolbar

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_products, menu);
        return true;
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_SHORT).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    public void  agregarProducto(View view)
    {
        Intent i=new Intent(this,PublishActivity.class);
        i.putExtra("modelo",principal);
        startActivity(i);
        if(hilo!=null)
            hilo.destruir();
        finish();

    }




    public void refrescar()
    {

        adapter.notifyDataSetChanged();


    }



    //fin métodos toolbar
}