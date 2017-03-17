package com.wexu.huckster.control;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.wexu.huckster.control.productos.ProductsActivity;
import com.wexu.huckster.control.vendedor.PublishActivity;
import com.wexu.huckster.control.vendedor.SellerActivity;
import com.wexu.huckster.modelo.Huckster;

/**
 * Created by ASUS1 on 03/12/2016.
 */

public class HiloRefresco extends AsyncTask<Void,Void,Void>{



    private ProductsActivity productsActivity;
    private Huckster principal;
    private boolean morirme;
    private SharedPreferences preferences;

    public HiloRefresco(ProductsActivity p, Huckster h, SharedPreferences a)
    {
        productsActivity=p;
        principal=h;
        morirme=false;
        preferences=a;

    }


    @Override
    protected Void doInBackground(Void... params) {
        while (!morirme)
        {

           if(principal.hayImagenParaMostrar())
           {
               publishProgress();
               principal.cambiarEstadoImgPM();
           }
            String mensaje=preferences.getString("chat","NADA");
            if(!mensaje.equals("NADA"))
            {
                principal.darUsuario().cargarChats();
                SharedPreferences.Editor editor=preferences.edit();
                editor.clear();
                editor.commit();

            }


        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        productsActivity.refrescar();


    }

    public void destruir()
    {
        principal.getDataBase().reiniciarProgresoDeCarga();
        morirme=true;
    }
}
