package com.wexu.huckster.control.chat;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.wexu.huckster.modelo.Chat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ASUS1 on 04/12/2016.
 */

public class ChatService  extends FirebaseMessagingService {


    private ArrayList<Chat> chats;
    private File arch;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        Log.d("CHAT","VAMOS QUE VAMOS");
        File archivo =new File(Chat.RUTA_CHATS);
        archivo.mkdirs();
        arch=new File(Chat.RUTA_CHATS+"chats.towershop");
        recibirNuevoMensaje(remoteMessage);

//            Log.d("ARCHIVO","CREADO");



        //SharedPreferences preferencias=getSharedPreferences("datos", Context.MODE_PRIVATE);
        //SharedPreferences.Editor editor=preferencias.edit();
        //editor.putString("chat",remoteMessage.getData()+"");
        //editor.commit();
    }


    public void recibirNuevoMensaje(RemoteMessage nuevo)
    {


            if(arch.exists())
            {
                cargarChat();
                Map<String, String> info= nuevo.getData();
                String nombre=info.get("Nombre");
                String nick=info.get("Nick");
                String mensaje=info.get("Mensaje");
                String token=info.get("Token");
                boolean viejo=false;
                for(int i=0;i<chats.size()&&!viejo;i++)
                {
                    if(chats.get(i).getUserName().equals(nick))
                    {
                        chats.get(i).recibirMensaje(mensaje);
                        viejo=true;
                    }
                }
                if(!viejo)
                {
                    Chat chatNuevo=new Chat(nick,nombre,token,nombre);
                    chatNuevo.recibirMensaje(mensaje);
                    chats.add(chatNuevo);
                }

                salvarChats(chats);

            }else
            {
                chats=new ArrayList<Chat>();
                Map<String, String> info= nuevo.getData();
                String nombre=info.get("Nombre");
                String nick=info.get("Nick");
                String mensaje=info.get("Mensaje");
                String token=info.get("Token");
                Chat chatNuevo=new Chat(nick,nombre,token,nombre);
                chatNuevo.recibirMensaje(mensaje);
                chats.add(chatNuevo);
                salvarChats(chats);

            }

        SharedPreferences preferencias=getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferencias.edit();
        editor.putString("chat","MENSAJE POR LEER");
        editor.commit();
    }

    public void salvarChats(ArrayList<Chat> chat)
    {
        try{
            FileOutputStream fos = new FileOutputStream(arch);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(chat);
            oos.flush();
            oos.close();

        }catch (Exception e)
        {

        }

    }

    public void cargarChat()
    {
        try {
            FileInputStream fis=new FileInputStream(arch);
            ObjectInputStream ois=new ObjectInputStream(fis);
            chats=(ArrayList<Chat>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }




}
