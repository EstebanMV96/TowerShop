package com.wexu.huckster.control;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.wexu.huckster.control.chat.ChatActivity;
import com.wexu.huckster.modelo.Chat;
import com.wexu.huckster.modelo.Huckster;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ASUS1 on 04/12/2016.
 */

public class HiloConversa extends AsyncTask<Void,Void,Void>{


    private ChatActivity chatActual;
    private boolean morirme;
    private  SharedPreferences preferences;
    private Huckster principal;
    private Chat primero;
    private final static String LLAVE="AAAAS1dq548:APA91bEbPqSdeg4ekLD5muRpMJry72Vj0gsmUKRlis-X0JP3y5UT59ec_0bxPcIM6drMT3AjcLi9jVzVpD0yBVmgqLkXAkCmc0og87GNC7xal2dJKphN0FVWQEhL0popZo_gL4kGYEQ62q0-YXaTrjp89Ags6P7z4w";
    private boolean modo;
    private String nick;
    private String nombre;
    private String mensaje;
    private String miToken;
    private String token;

    public HiloConversa(ChatActivity cha,SharedPreferences preferences, Huckster p,Chat c)
    {
        chatActual=cha;
        morirme=false;
        principal=p;
        primero=c;
        this.preferences=preferences;
        modo=false;

    }



    public void cargarInfo(String token,String nombre,String mensaje,String nick,String miToken)
    {
        this.nombre=nombre;
        this.nick=nick;
        this.mensaje=mensaje;
        this.token=token;
        this.miToken=miToken;
    }


    public void enviarmensaje()
    {
        Log.d("ENVIO DE MENSAJE","OKK");
        try {
            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty ("Authorization", "key="+LLAVE);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            String b="{ \"data\": { \"Nick\": \""+nick+"\", \"Nombre\": \""+nombre+"\", \"Mensaje\": \""+mensaje+"\", \"Token\": \""+miToken+"\" },\"to\" : \""+token+"\",}" ;
            OutputStream os = conn.getOutputStream();
            os.write(b.getBytes());

            os.flush();
            System.out.println("ESTO ES LA IGUALDAD "+HttpURLConnection.HTTP_CREATED);
            if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {

            }



            conn.disconnect();

        } catch (MalformedURLException e) {



        } catch (IOException e) {



        }



    }

    @Override
    protected Void doInBackground(Void... params) {
        while (!morirme)
        {

          if(modo)
          {
              enviarmensaje();
              modo=false;
          }

            String mensaje=preferences.getString("chat","NADA");
            if(!mensaje.equals("NADA"))
            {

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
        Log.d("ACTUALIZO","OKKK");

            principal.darUsuario().cargarChats();
            chatActual.refrescar(principal.darUsuario().buscarChat(primero.getUserName()));



    }

    public void destruir()
    {
        morirme=true;
    }

    public void enviarMensaje1()
    {
        modo=true;
    }

}
