package com.wexu.huckster.modelo;


/**
 * MOYA
 */

import android.os.Environment;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Chat implements Serializable{
	private static final long serialVersionUID = 8422230812867474835L;
	private String userName;
	private int ordenMensaje;
	public final static String RUTA_CHATS = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)+ "/towershop/";
	private ArrayList<Mensaje> enviados;
	private ArrayList<Mensaje> recibidos;
	private String nombreRemitente;
	private String nombreDestinatario;
	private ArrayList<Mensaje> conversacion;
	private String token;
	public Chat(String userName,String nombre,String token,String nomDesti) {
		
		this.userName = userName;
		ordenMensaje=0;
		enviados=new ArrayList<Mensaje>();
		recibidos=new ArrayList<Mensaje>();
		nombreRemitente=nombre;
		nombreDestinatario=nomDesti;
		this.token=token;
		armarConversacion();

		
	}


	public void armarConversacion()
	{
		conversacion=new ArrayList<Mensaje>();
		String mensaje="";
		if(recibidos.size()!=0&&enviados.size()!=0)
		{
			int largo=recibidos.size()+enviados.size();
			for(int i=0;i<largo;i++)
			{
				if(i<recibidos.size())
					conversacion.add(recibidos.get(i));
				if(i<enviados.size())
					conversacion.add(enviados.get(i));
			}
			Collections.sort(conversacion);


		}else

		{
			if(recibidos.size()>0)
			{
				conversacion=recibidos;
			}else
			{
				conversacion= enviados;
			}
		}

	}

	public void escribirMensaje(String mensaje)
	{
		Log.d("CHAT","ESCRIBIENDO MSJ"+mensaje);
		enviados.add(new Mensaje(mensaje,ordenMensaje,true));
		ordenMensaje++;
		armarConversacion();
	}

	public void recibirMensaje(String mensaje)
	{
		Mensaje nuevo=new Mensaje(mensaje,ordenMensaje,false);
		ordenMensaje++;
		recibidos.add(nuevo);
		armarConversacion();
	}

	public Mensaje getLastMessage(){

		try {
			if(enviados.size()>0&&recibidos.size()>0)
			{
				Mensaje lastSend= enviados.get(enviados.size()-1);
				Mensaje lastRec= recibidos.get(recibidos.size()-1);
				return lastSend.getId()>lastRec.getId()?lastSend:lastRec;
			}else if(enviados.size()>0)
			{
				return enviados.get(enviados.size()-1);
			}else
			{
				return recibidos.get(recibidos.size()-1);
			}

		}
		catch (Exception e){
			return null;
		}

	}

	public String darToken()
	{
		return token;
	}
	public ArrayList<Mensaje> darConversacion()
	{
		return conversacion;
	}


	public int getOrdenMensaje() {
		return ordenMensaje;
	}

	public String getNombreRemitente() {
		return nombreRemitente;
	}

	public String getUserName() {
		return userName;
	}

	public String  darNombreDestinatario()
	{
		return nombreDestinatario;
	}
}
