package com.wexu.huckster.modelo;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.wexu.huckster.control.DataBase;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ASUS1 on 08/11/2016.
 */

public class Huckster implements Serializable{

    private String universidadActual;
    private Usuario user;
    private ArrayList<Producto> catalogo;
    private DataBase dataBase;
    private long totalProductos;



    private  boolean perdido;
    public Huckster(SharedPreferences info, String nickName, String nombre,String photo)

    {
        dataBase=new DataBase(this);
        String mensaje=info.getString("info","ERROR");
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        catalogo=new ArrayList<Producto>();
        if(mensaje.equals("ERROR"))
        {

            dataBase.agregarUsuario(nombre,nickName,refreshedToken);
            SharedPreferences.Editor editor=info.edit();
            editor.putString("info",nickName+";"+nombre);
            editor.commit();
        }

        user=new Usuario(nombre,nickName,refreshedToken,photo);
        totalProductos=0;
        perdido=false;


    }

    public String darNickNamePorNombreVendedor(String nomVendedor)
    {
        String h="";
        for(int i=0;i<catalogo.size()&&h.equals("");i++)
        {

            if(catalogo.get(i).getNombreVendedor().equals(nomVendedor))
            {
                h=catalogo.get(i).getNickVendedor();
            }
        }

        return h;
    }

    public boolean darPerdido()
    {
        return  perdido;

    }

    public void setPerdido(boolean v)
    {
        perdido=v;
    }


    public ArrayList<Producto>  filtrarProductos(String categoria)
    {
        ArrayList<Producto> nuevo=new ArrayList<Producto>();
        for(int i=0;i<catalogo.size();i++)
        {
            Log.d("Nombre",catalogo.get(i).getNombre());
            Log.d("categoria",catalogo.get(i).getCategoria());
            Log.d("Analice",categoria);
            if(catalogo.get(i).getCategoria().equals(categoria))
            {
                Log.d("ENTRO",categoria);
                nuevo.add(catalogo.get(i));
            }

        }
        return nuevo;

    }

    public void inicializarProdcutos()
    {



    }

    public DataBase getDataBase()
    {
        return  dataBase;
    }

    public  Usuario darUsuario()
    {
        return user;
    }

    public void agregarProducto(Producto p)
    {
        Log.d("HC",p.getNombre());
        boolean chao=false;
        for(int i=0;i<catalogo.size()&&!chao;i++)
        {
            if(catalogo.get(i).getNombre().equals(p.getNombre()))
            {
                chao=true;
            }
        }
        if(!chao)
            catalogo.add(p);


    }

    public ArrayList<Producto> getCatalogo() {
        return catalogo;
    }

    public boolean hayImagenParaMostrar()
    {
        return dataBase.darHayImagenParaMostrar();
    }

    public void cambiarEstadoImgPM()
    {
        dataBase.reiniciarImgParaMostrar();
    }

    public long getTotalProductos() {
        return totalProductos;
    }

    public void setTotalProductos(long totalProductos) {
        this.totalProductos = totalProductos;
    }
}




