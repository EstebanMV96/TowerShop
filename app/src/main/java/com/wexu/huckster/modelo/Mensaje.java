package com.wexu.huckster.modelo;

import java.io.Serializable;

public class Mensaje implements Serializable, Comparable<Mensaje>{
	
	
	
	private String mensaje;
	private int id;
	private boolean left;
	public Mensaje(String mensaje, int id, boolean recibido) {
		
		this.mensaje = mensaje;
		this.id = id;
		left=recibido;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}


	@Override
	public int compareTo(Mensaje another) {
		if (id < another.id) {
			return -1;
		}
		if (id > another.id) {
			return 1;
		}
		return 0;
	}

	public boolean darLeft()
	{
		return left;
	}
}
	

	


