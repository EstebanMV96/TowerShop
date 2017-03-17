package com.wexu.huckster.control;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;

import com.wexu.huckster.control.productos.ProductsActivity;
import com.wexu.huckster.control.vendedor.PublishActivity;
import com.wexu.huckster.control.vendedor.SellerActivity;
import com.wexu.huckster.modelo.Huckster;


/**
 * Created by ASUS1 on 30/11/2016.
 */

public class HiloEspera extends AsyncTask<Void,Void,Void> {

    private DataBase base;
    private PublishActivity interfaz;
    private ProgressDialog progreso;
    private Huckster principal;
    private SellerActivity sellerActivity;
    private ProductsActivity productosActivity;


    public HiloEspera(ProgressDialog p, PublishActivity pa,DataBase data)
    {
        progreso=p;
        interfaz=pa;
        base=data;
        progreso.setMessage("SUBIENDO ARTÍCULO POR FAVOR ESPERE...");
        progreso.setProgressStyle(progreso.STYLE_HORIZONTAL);
        progreso.setProgress(0);
        progreso.setMax(100);
        principal=null;
        sellerActivity=null;


    }

    public HiloEspera(ProgressDialog p, SellerActivity pa, Huckster hc)
    {
        progreso=p;
        sellerActivity=pa;
        principal=hc;
        base=principal.getDataBase();
        base.cargarTotalMP(principal.darUsuario().getNickName());
        base.cargarMisProductos(principal.darUsuario().getNickName());
        progreso.setMessage("CARGANDO MIS PRODUCTOS...");


    }




    public HiloEspera(ProgressDialog p, ProductsActivity pa, Huckster hc)
    {
        System.out.print("ESTA EN EL HILO HPTA");
        progreso=p;
        productosActivity=pa;
        sellerActivity=null;
        principal=hc;
        base=principal.getDataBase();
        progreso.setMessage("CARGANDO PRODUCTOS ACTIVOS...");
        base.cargarNumeroDeProductos();
        base.cargarProductosEnVenta();
    }

    @Override
    protected Void doInBackground(Void... params) {



        if(principal==null)
        {
            while (!base.getProgress())
            {
                progreso.setProgress(base.darProgreso());

            }
            base.reiniciarProgresoDeCarga();
            publishProgress();
        }else
        {

            //HACER EL IFFFF Y TERMINAR LA LOGICA PARA COMPRAR
                while(!base.getProgress())
                {
                    progreso.setProgress(10);

                }
                base.reiniciarProgresoDeCarga();
                publishProgress();


        }


        return null;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progreso.show();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progreso.dismiss();


    }

    @Override
    protected void onProgressUpdate(Void... values) {

        super.onProgressUpdate(values);
        if(principal!=null&&sellerActivity!=null)
        sellerActivity.lanzarFragment();
        else
        {
            if(productosActivity!=null)
            {
                productosActivity.lanzarFragment();
                System.out.print("HOLAAAAAAAA");
            }else
            {
                interfaz.cerrar();
            }


        }




    }
}
