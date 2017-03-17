package com.wexu.huckster.control;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.wexu.huckster.control.vendedor.SellerActivity;
import com.wexu.huckster.modelo.Huckster;

/**
 * Created by ASUS1 on 04/12/2016.
 */

public class HiloEsperaSA extends AsyncTask<Void,Void,Void>{

    private ProgressDialog progreso;
    private SellerActivity sellerActivity;
    private Huckster principal;
    private boolean abandone;

    public HiloEsperaSA(ProgressDialog p, SellerActivity pa, Huckster hc)
    {


        progreso=p;
        sellerActivity=pa;
        principal=hc;
        if(principal.darUsuario().getTotalP()!=0)
        {
            abandone=true;
        }else
        {
            principal.getDataBase().cargarTotalMP(principal.darUsuario().getNickName());
            principal.getDataBase().cargarMisProductos(principal.darUsuario().getNickName());
            progreso.setMessage("CARGANDO MIS PRODUCTOS...");
            abandone=false;
        }




    }


    @Override
    protected Void doInBackground(Void... params) {


            //HACER EL IFFFF Y TERMINAR LA LOGICA PARA COMPRAR
            while(!principal.getDataBase().getProgress()&&!abandone)
            {
                progreso.setProgress(10);

            }
           principal.getDataBase().reiniciarProgresoDeCarga();
            publishProgress();





        return null;
    }


    @Override
    protected void onProgressUpdate(Void... values) {

        super.onProgressUpdate(values);
        sellerActivity.lanzarFragment();

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

}
