package com.wexu.huckster.control;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.wexu.huckster.control.productos.ProductsActivity;
import com.wexu.huckster.control.vendedor.SellerActivity;
import com.wexu.huckster.modelo.Huckster;

/**
 * Created by ASUS1 on 04/12/2016.
 */

public class HiloRefrescoV extends AsyncTask<Void,Void,Void>{

    private SellerActivity seller;
    private Huckster principal;
    private boolean morirme;
    private SharedPreferences preferences;


    public HiloRefrescoV(SellerActivity p, Huckster h, SharedPreferences pre)
    {
        seller=p;
        principal=h;
        morirme=false;
        preferences=pre;

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
        seller.refrescar();


    }

    public void destruir()
    {
        principal.getDataBase().reiniciarProgresoDeCarga();

        morirme=true;
    }

}
