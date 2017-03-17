package com.wexu.huckster.control;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;


import com.wexu.huckster.control.chat.ChatsActivity;

import com.wexu.huckster.modelo.Huckster;

/**
 * Created by ASUS1 on 04/12/2016.
 */

public class HiloRefrescoC extends AsyncTask<Void,Void,Void>{

    private ChatsActivity chats;
    private Huckster principal;
    private boolean morirme;
    private SharedPreferences preferences;

    public HiloRefrescoC(ChatsActivity p, Huckster h, SharedPreferences pre)
    {
        chats=p;
        principal=h;
        morirme=false;
        preferences=pre;

    }

    @Override
    protected Void doInBackground(Void... params) {
        while (!morirme)
        {


            if(principal.darPerdido())
            {
                principal.setPerdido(false);
                break;
            }

            String mensaje=preferences.getString("chat","NADA");
            if(!mensaje.equals("NADA"))
            {
                principal.darUsuario().cargarChats();
                publishProgress();
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
        chats.refrescar();
    }

    public void destruir()
    {

        morirme=true;
    }


}
