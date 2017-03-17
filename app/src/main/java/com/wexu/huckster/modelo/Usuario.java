package com.wexu.huckster.modelo;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;


public class Usuario implements Serializable{
	
	
	private String nombre;
	private String nickName;
	private String token;
	private boolean estado;
	private ArrayList<Producto> misProductos;
	private ArrayList<Chat>  chats;
	private long totalP;

	private String imagen;

	private File arch;


	
	public Usuario(String nombre, String nickName, String token, String photo) {
		this.nombre = nombre;
		this.nickName = nickName;
		misProductos=new ArrayList<Producto>();
		this.token=token;
		this.imagen=photo;
		totalP=0;
		File archivo =new File(Chat.RUTA_CHATS);
		archivo.mkdirs();
		arch=new File(Chat.RUTA_CHATS+"chats.towershop");
		cargarChats();
	}

	
	public void cargarChats()
	{
		if(arch.exists())
		{
			Log.d("HC","ENCONTRO EL ARCHIVO");
			try {
				FileInputStream fis=new FileInputStream(arch);
				ObjectInputStream ois=new ObjectInputStream(fis);
				chats=(ArrayList<Chat>) ois.readObject();
				ois.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}else
		{
			chats=new ArrayList<Chat>();

		}


	}

	public void salvarInfo(SharedPreferences preferencias)
	{
		try{
			FileOutputStream fos = new FileOutputStream(arch);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(chats);
			oos.flush();
			oos.close();


		}catch (Exception e)
		{

		}
	}


	public Chat buscarChat(String nickName)
	{
		Chat encontrado=null;
		for(int i=0;i<chats.size()&&encontrado==null;i++)
		{
			if(chats.get(i).getUserName().equals(nickName))
			{
				encontrado=chats.get(i);
			}
		}
		return encontrado;
	}


	public boolean isEstado() {
		return estado;
	}


	public ArrayList<Producto> getMisProductos() {
		return misProductos;
	}


	public String getNombre() {
		return nombre;
	}



	public String getNickName() {
		return nickName;
	}

	public String getToken() {
		return token;
	}
	
	public boolean agregarProducto(String name, String des, String nomImg, int precio,String categoria)
	{
		boolean sePudo=false;
		Producto nuevo=new Producto(name,des,precio,nickName,categoria,nomImg,nombre);
		if(buscarProducto(name)==null)
		{
			misProductos.add(nuevo);
			totalP++;
			sePudo=true;
		}
		return sePudo;
	}
	
	public void eliminarProducto(String name)throws Exception
	{
		
	}

	public long getTotalP()
	{
		return totalP;
	}

	public void setTotalP(long total)
	{

		totalP=total;
	}
	
	public void enviarMensaje(String user,String token)
	{
		
	}

	public void listenerReciboMensajes()
	{


	}

	public void cargarProducto(Producto p)
	{

		Log.d("AGREGO P","TRUE");
		misProductos.add(p);

		Log.d("TAMAÑO DEL BUFFER",misProductos.size()+"");
	}

	public Producto buscarProducto(String nombre)
	{
		nombre=nombre.trim();
		Producto encontrado=null;
		Log.d("TAMAÑO DEL BUFFER",misProductos.size()+"");
		for (int i=0;i<misProductos.size()&&encontrado==null;i++)
		{
			if(misProductos.get(i).getNombre().trim().equalsIgnoreCase(nombre))
			{
				encontrado=misProductos.get(i);
			}
		}

		return encontrado;
	}

	public void agregarChat(Chat nuevo)
	{
		if(buscarChat(nuevo.getUserName())==null)
		{
			chats.add(nuevo);
		}
	}


	public ArrayList<Chat> getChats() {
		return chats;
	}





	public String getImg() {
		return imagen;
	}
}
