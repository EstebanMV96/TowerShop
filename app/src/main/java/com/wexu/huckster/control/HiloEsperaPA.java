package com.wexu.huckster.control;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.wexu.huckster.control.vendedor.PublishActivity;

/**
 * Created by ASUS1 on 03/12/2016.
 */

public class HiloEsperaPA extends AsyncTask<Void,Void,Void> {


    private DataBase base;
    private PublishActivity interfaz;
    private ProgressDialog progreso;

    public HiloEsperaPA(ProgressDialog p, PublishActivity pa,DataBase data)
    {
        progreso=p;
        interfaz=pa;
        base=data;
        progreso.setMessage("SUBIENDO ARTÍCULO POR FAVOR ESPERE...");
        progreso.setProgressStyle(progreso.STYLE_HORIZONTAL);
        progreso.setProgress(0);
        progreso.setMax(100);

    }

    @Override
    protected Void doInBackground(Void... params) {

            while(!base.getProgress())
            {
                progreso.setProgress(10);
            }
            base.reiniciarProgresoDeCarga();
            publishProgress();

        return null;
    }

    @Override
    protected void onPreExecute() {


        progreso.show();
    }

    @Override
    protected void onPostExecute(Void aVoid) {

        progreso.dismiss();


    }

    @Override
    protected void onProgressUpdate(Void... values) {

        interfaz.cerrar();

    }

}
