package com.wexu.huckster.control;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.wexu.huckster.control.vendedor.PublishActivity;
import com.wexu.huckster.modelo.Huckster;
import com.wexu.huckster.modelo.Producto;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by ASUS1 on 08/11/2016.
 */

public class DataBase implements Serializable{


    private Huckster principal;
    private boolean imgCargo;
    private int progreso;
    public boolean hayImagenParaMostrar;
    private String token;
    public  DataBase(Huckster s)
    {

        imgCargo=false;
        progreso=0;
        principal=s;
        hayImagenParaMostrar=false;
        token="";

    }

    public String darToken()
    {
        return token;
    }

    public void reiniciarImgParaMostrar()
    {
        hayImagenParaMostrar=false;
    }

    public boolean darHayImagenParaMostrar()
    {
        return hayImagenParaMostrar;
    }

    public boolean getProgress()
    {
        return imgCargo;
    }

    public void agregarUsuario(String nombre,String nick,String token)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Usuarios");
        myRef.child(nick).child("nombre").setValue(nombre);
        myRef.child(nick).child("token").setValue(token);



    }


    public void cargarToken(String usuario)
    {
        Log.d("BASE","CARGANDO TOKEN "+usuario);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Usuarios").child(usuario).child("token");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue()!=null)
                {
                    Log.d("BASE","ASIGNO TOKEN "+dataSnapshot.getValue());
                    token=(String) dataSnapshot.getValue();
                    imgCargo=true;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }


    public void cambiarEstado(boolean disponible)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Activos").child(principal.darUsuario().getNickName());
        if(disponible)
        {
            myRef.setValue(1);
            final DatabaseReference myRef1 = database.getReference("Activos").child("Total");
            myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.getValue()!=null)
                    {

                        long totalA=(long)dataSnapshot.getValue();
                        myRef1.setValue(totalA+1);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else
        {
            myRef.removeValue();

            final DatabaseReference myRef3 = database.getReference("Activos").child("Total");
            myRef3.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.getValue()!=null)
                    {

                        long totalA=(long)dataSnapshot.getValue();
                        totalA--;
                        myRef3.setValue(totalA);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }


    public void cargarNumeroDeProductos()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Activos").child("Total");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue()!=null)
                {

                    principal.setTotalProductos((long)dataSnapshot.getValue());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void cargarTotalMP(String nick)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Productos").child(nick).child("Total");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue()!=null)
                {

                    principal.darUsuario().setTotalP((long)dataSnapshot.getValue());
                }else
                {
                    imgCargo=true;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void cargarProductosEnVenta()
    {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Activos");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();

                if (value != null) {
                    Set<String> llaves = value.keySet();
                    Iterator i = llaves.iterator();

                    while (i.hasNext()) {
                        String clave= i.next().toString();
                        if(!clave.equals("Total"))
                        auxCargarTodos(clave,database);
                    }
                    if(llaves.size()==1)
                    {
                        imgCargo=true;
                    }


                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void auxCargarTodos(final String nick, FirebaseDatabase f)
    {


        final DatabaseReference myref=f.getReference("Productos").child(nick);

        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();

                if (value != null) {
                    Set<String> llaves = value.keySet();
                    Iterator i = llaves.iterator();

                    while (i.hasNext()) {
                        String clave= i.next().toString();
                        Log.d("TOTAL ACTI",clave);
                        if(!clave.equals("Total"))
                        {
                            myref.child(clave).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if(dataSnapshot.getValue()!=null)
                                    {
                                        Producto nuevo=dataSnapshot.getValue(Producto.class);

                                        Log.d("EN BASE",nuevo.getNombre());
                                        String file = PublishActivity.RUTA_FOTOS + nuevo.getNombreImagen();
                                        File mi_foto = new File( file );
                                        if(!mi_foto.exists())
                                        {
                                            FirebaseStorage storage = FirebaseStorage.getInstance();
                                            StorageReference storageRef = storage.getReferenceFromUrl("gs://huckster-af89b.appspot.com");
                                            StorageReference riversRef = storageRef.child(nuevo.getNickVendedor()).child(nuevo.getNombre()).child(nuevo.getNombreImagen());
                                            riversRef.getFile(mi_foto).
                                                    addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                            double  progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                                            if(progress==100)
                                                            {
                                                                hayImagenParaMostrar=true;
                                                            }
                                                        }
                                                    });
                                        }
                                        principal.agregarProducto(nuevo);
                                        progreso++;
                                        if(progreso==1)
                                        {
                                            imgCargo=true;
                                            progreso=0;
                                        }

                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }


                    }


                }else
                {
                    imgCargo=true;
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    public void cargarMisProductos(String nick)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("Productos").child(nick);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                // This method is called once with the initial value and again
                // whenever data at this location is updated.





                HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();

                if (value != null) {
                    Set<String> llaves = value.keySet();
                    Iterator i = llaves.iterator();

                    while (i.hasNext()) {
                        String clave= i.next().toString();
                        if(!clave.equals("Total"))
                        auxCargaMisProductos(clave,myRef);

                    }


                }else
                {
                    imgCargo=true;
                }


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("S", "Failed to read value.", error.toException());
            }
        });






    }


    private void auxCargaMisProductos(String nomP,DatabaseReference myRef)
    {

        myRef.child(nomP).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue()!=null)
                {
                    Producto nuevo=dataSnapshot.getValue(Producto.class);
                    Log.d("EN BASE",nuevo.getNombre());
                    String file = PublishActivity.RUTA_FOTOS + nuevo.getNombreImagen();
                    File mi_foto = new File( file );
                    if(!mi_foto.exists())
                    {
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReferenceFromUrl("gs://huckster-af89b.appspot.com");
                        StorageReference riversRef = storageRef.child(nuevo.getNickVendedor()).child(nuevo.getNombre()).child(nuevo.getNombreImagen());
                        riversRef.getFile(mi_foto).
                        addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                double  progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                if(progress==100)
                                {
                                   hayImagenParaMostrar=true;
                                }
                            }
                        });
                    }
                    principal.darUsuario().cargarProducto(nuevo);
                    progreso++;
                    if(progreso==1)
                    {
                        Log.d("CARGOO","GOLLLLLLLL");
                        imgCargo=true;
                        progreso=0;
                    }


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void reiniciarProgresoDeCarga()
    {
        imgCargo=false;
    }



    public void subirProducto(String nombre,String nickVendedor,String precio, String descripcion, String nomImagen, String categoria,String nomVendedor)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("Productos");
        Producto nuevo=new Producto(nombre,descripcion,Integer.parseInt(precio),nickVendedor,categoria,nomImagen,nomVendedor);
        myRef.child(nickVendedor).child(nombre).setValue(nuevo);
        myRef.child(nickVendedor).child("Total").setValue(principal.darUsuario().getTotalP());
        myRef.child("Total").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue()!=null)
                {
                    long numero=(long) dataSnapshot.getValue();
                    numero++;
                    myRef.child("Total").setValue(numero);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public int darProgreso()
    {
        return progreso;
    }


    public void subirImagen(String nick,String producto,String nomIma, Uri imagen)
    {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://huckster-af89b.appspot.com");
        StorageReference riversRef = storageRef.child(nick).child(producto).child(nomIma);
        UploadTask uploadTask = riversRef.putFile(imagen);
        System.out.print("ESTA DOND NECESITO 111");
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                System.out.print("ESTA DOND NECESITO 22222");
               double  progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                progreso=(int)progress;
               if(progress==100)
               {
                   imgCargo=true;
               }

            }
        });
// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });


    }
}
