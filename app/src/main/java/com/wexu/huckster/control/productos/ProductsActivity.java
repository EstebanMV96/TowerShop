package com.wexu.huckster.control.productos;


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
import com.wexu.huckster.control.HiloEspera;
import com.wexu.huckster.control.HiloRefresco;
import com.wexu.huckster.control.chat.ChatActivity;
import com.wexu.huckster.control.chat.ChatsActivity;
import com.wexu.huckster.control.vendedor.ProfileFragment;
import com.wexu.huckster.control.vendedor.SellerActivity;
import com.wexu.huckster.modelo.Huckster;
import com.wexu.huckster.modelo.Producto;

import java.util.ArrayList;
import java.util.List;


public class ProductsActivity extends AppCompatActivity implements
        FoodFragment.OnFragmentInteractionListener,
        StationeryFragment.OnFragmentInteractionListener,
        OthersFragment.OnFragmentInteractionListener,
        AdapterView.OnItemSelectedListener
{

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;


    private Typeface nameFont;
    private Typeface priceFont;
    private Huckster principal;

    private DrawerLayout drawerLayout;

    private HiloRefresco hilo;
    private FoodFragment ff;
    private OthersFragment of;
    private StationeryFragment sf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        Bundle extras=getIntent().getExtras();
        principal= (Huckster) extras.getSerializable("modelo");
        agregarToolbar();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        //MANEJO DEL NAVIGATION VIEW
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        if (navigationView != null) {
            prepararDrawer(navigationView);
            // Seleccionar item por defecto
            //seleccionarItem(navigationView.getMenu().getItem(0));
        }


        String info=extras.getString("info");
        // viewPager = (ViewPager) findViewById(R.id.viewpager);

        if(info==null||info.equals(""))
        {
            System.out.print("AUXILIOOO");
            new HiloEspera(new ProgressDialog(this),this,principal).execute();
        }else {

            lanzarFragment();

        }

    }

    //METODOS NAVIGATION VIEW
    private void prepararDrawer(NavigationView navigationView) {
        navigationView.setCheckedItem(R.id.item_buy);
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



        Switch disponible = (Switch) headerLayout.findViewById(R.id.switch1);
        disponible.setEnabled(false);
        disponible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


            }
        });

    }
    private void seleccionarItem(MenuItem itemDrawer) {

        Intent i= null;

        switch (itemDrawer.getItemId()) {
            case R.id.item_buy:
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.item_sell:
                if(hilo!=null)
                    hilo.destruir();
                i= new Intent(getApplicationContext(), SellerActivity.class);
                i.putExtra("modelo",principal);
                startActivity(i);
                finish();

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.search_bar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // Poner ícono del drawer toggle
            ab.setHomeAsUpIndicator(R.drawable.menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setTitle("TowerShop");
    }


    public void lanzarFragment()
    {
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        SharedPreferences preferencias=getSharedPreferences("datos", Context.MODE_PRIVATE);
        hilo=new HiloRefresco(this,principal,preferencias);
        hilo.execute();

    }

    private void  setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Bundle bundle = new Bundle();
        ArrayList<Producto> filtro=principal.filtrarProductos(Producto.COMIDA);
        bundle.putSerializable("productos",filtro);
        ff= new FoodFragment();
        ff.setArguments(bundle);
        Bundle bundle1 = new Bundle();
        ArrayList<Producto> filtro1=principal.filtrarProductos(Producto.PAPELERIA);
        bundle1.putSerializable("productos",filtro1);
        sf= new StationeryFragment();
        sf.setArguments(bundle1);
        Bundle bundle2 = new Bundle();
        ArrayList<Producto> filtro2=principal.filtrarProductos(Producto.OTROS);
        bundle2.putSerializable("productos",filtro2);
        of= new OthersFragment();
        of.setArguments(bundle2);
        ProfileFragment pf=new ProfileFragment();
        nameFont= Typeface.createFromAsset(getAssets(), "fonts/Raleway-Light.ttf");
        priceFont= Typeface.createFromAsset(getAssets(), "fonts/lilita.ttf");

        adapter.addFragment(ff, "Comida");
        adapter.addFragment(sf, "Papelería");
        adapter.addFragment(of, "Otros");

        viewPager.setAdapter(adapter);
        // viewPager.setCurrentItem(0);
   }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d("AQUI","HOLAAA");
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        Producto item =(Producto) parent.getItemAtPosition(position);
        // Showing selected spinner item
        Log.d("AQUI","HOLAAA");

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void detalles(View view) {

        Log.d("DETALLES","ENTROOOOOOO1234");

        Log.d("DETALLES","OK");
        TextView c= (TextView)view.findViewById(R.id.contacto);
        String contacto= c.getText().toString();

        String[] contactoFull=c.getText().toString().split(" ");
        String machete="";
        for(int i=2;i<contactoFull.length;i++)
        {
            machete+=contactoFull[i]+" ";
        }
        machete=machete.trim();
        String busqueda=principal.darNickNamePorNombreVendedor(machete);
        hilo.destruir();
        Intent i= new Intent(this, ChatActivity.class);
        i.putExtra("modelo",principal);
        i.putExtra("info",busqueda);
        i.putExtra("info1",machete);
        startActivity(i);

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
        getMenuInflater().inflate(R.menu.menu_products, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return true;

    }

    //fin métodos toolbar

    public void goToMessages (View v){
        Intent i= new Intent(getApplicationContext(), ChatsActivity.class);
        startActivity(i);
    }


    public void refrescar()
    {
        ff.refresh();
        of.refresh();
        sf.refresh();
    }

}
