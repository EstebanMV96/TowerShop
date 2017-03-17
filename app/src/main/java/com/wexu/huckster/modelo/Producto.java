package com.wexu.huckster.modelo;


import java.io.Serializable;

public class Producto implements Serializable{
	
	public static final String COMIDA="Comida";
	public static final String PAPELERIA="Papelería";
	public static final String OTROS="Otros";
	private String nombre;
	private String descripcion;
	private int precio;
	private String nickVendedor;
	private String categoria;
	private boolean inStock;
	private String nombreVendedor;
	//private int imagen;

//	public Producto(String name, String des, int img, int precio, String nick,String categoria)
//	{
//		nombre=name;
//		descripcion=des;
//		imagen=img;
//		this.precio=precio;
//	}

	private String nombreImagen;
	
	public Producto(String name, String des, int precio, String nick,String categoria,String nombreImage, String nomVendedor)
	{
		nombre=name;
		descripcion=des;
		this.precio=precio;
		nombreImagen=nombreImage;
		categoria=categoria.trim();
		this.categoria=categoria;
		nickVendedor=nick;
		nombreVendedor=nomVendedor;
	}

	public  Producto()
	{

	}


	public String getNombre() {
		return nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	//public int getImagen() {
		//return imagen;
	//}

	//public void setImagen(ImageIcon imagen) {
	//	this.imagen = imagen;
	//}


	public int getPrecio() {
		return precio;
	}

	public String getNickVendedor() {
		return nickVendedor;
	}

	public String getNombreVendedor() {
		return nombreVendedor;
	}

	public String getCategoria() {
		return categoria;
	}

	public String getNombreImagen() {
		return nombreImagen;
	}
}
