package com.wexu.huckster.control;

import android.os.AsyncTask;
import android.util.Log;

import com.wexu.huckster.control.chat.ChatActivity;
import com.wexu.huckster.modelo.Huckster;

/**
 * Created by ASUS1 on 05/12/2016.
 */

public class HiloCC  extends AsyncTask<Void,Void,Void>{

    private ChatActivity chat;
    private Huckster base;
    private String token;

    public  HiloCC(ChatActivity c,Huckster b, String nick)
    {
        chat=c;
        base=b;
        base.getDataBase().cargarToken(nick);

    }


    @Override
    protected Void doInBackground(Void... params) {

        Log.d("HOLAAA","HPTTAA");

                while(!base.getDataBase().getProgress())
                {
                  //  progreso.setProgress(10);
                }
                base.getDataBase().reiniciarProgresoDeCarga();
                publishProgress();

        return null;
    }


    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        Log.d("SALIO","OKK");
        chat.lanzarFragment(base.getDataBase().darToken());

    }
}
