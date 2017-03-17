package com.wexu.huckster.control.vendedor;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.wexu.huckster.R;
import com.wexu.huckster.control.HiloEsperaPA;
import com.wexu.huckster.modelo.Huckster;
import com.wexu.huckster.modelo.Producto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PublishActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Toolbar toolbar;
    public final static String RUTA_FOTOS = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/towershop/";
    private File file;
    private String nombreFotoActual;
    private ImageView picture;
    private static final int SOLICITUD_PERMISO_WRITE_CALL_LOG = 0;
    private static final int ENCODE= 60;
    private Huckster principal;
    private EditText txtNombre;
    private EditText txtPrecio;
    private EditText txtDescripcion;
    private  Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        nombreFotoActual="";
        file= new File(RUTA_FOTOS);
        file.mkdirs();
        picture=(ImageView) findViewById(R.id.imageView);
        // Conectamos el Layout al objeto Toolbar
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        // Configuración de la barra de herramientas (toolbar ) como la Barra de acciones (ActionBar)
        // con la llamada de setSupportActionBar ()
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        // Spinner element
       spinner = (Spinner) findViewById(R.id.spinner_categories);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add(Producto.COMIDA);
        categories.add(Producto.PAPELERIA);
        categories.add(Producto.OTROS);


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        txtNombre=(EditText) findViewById(R.id.nombreProducto);
        txtPrecio=(EditText) findViewById(R.id.precioProducto);
        txtDescripcion=(EditText) findViewById(R.id.descriProducto);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        Bundle extras=getIntent().getExtras();
        principal=(Huckster) extras.getSerializable("modelo");


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Comprovamos que la foto se a realizado
        Log.d("FOTO","SE TOMO  ");
        if (requestCode == 1 && resultCode == RESULT_OK) {
            //Creamos un bitmap con la imagen recientemente
            //almacenada en la memoria
            Log.d("FOTO","SE TOMO CORRECTAMENTE");
            Bitmap bMap = decodeFile1(new File(RUTA_FOTOS + nombreFotoActual),256,256);

            //DECO 1
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bMap.compress(Bitmap.CompressFormat.JPEG, ENCODE, out);
            Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

            // DECO 2
            ByteArrayOutputStream out1 = new ByteArrayOutputStream();
            decoded.compress(Bitmap.CompressFormat.JPEG, ENCODE, out1);
            Bitmap decoded1 = BitmapFactory.decodeStream(new ByteArrayInputStream(out1.toByteArray()));


            //DECO 3
            ByteArrayOutputStream out3 = new ByteArrayOutputStream();
            decoded1.compress(Bitmap.CompressFormat.JPEG, ENCODE, out3);
            Bitmap decoded2 = BitmapFactory.decodeStream(new ByteArrayInputStream(out3.toByteArray()));

            //DECO 4
            ByteArrayOutputStream out4 = new ByteArrayOutputStream();
            decoded2.compress(Bitmap.CompressFormat.JPEG, ENCODE, out4);
            Bitmap decoded3 = BitmapFactory.decodeStream(new ByteArrayInputStream(out4.toByteArray()));





            FileOutputStream out6 = null;
            try {
                out6 = new FileOutputStream(new File(RUTA_FOTOS+nombreFotoActual));
                decoded3.compress(Bitmap.CompressFormat.JPEG, ENCODE, out6); // bmp is your Bitmap instance
                // PNG is a lossless format, the compression factor (100) is ignored
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }



            //Añadimos el bitmap al imageView para
            //mostrarlo por pantalla
            //BitmapDrawable ob = new BitmapDrawable(getResources(), bMap);
            //picture.setBackground(ob);

            picture.setBackgroundResource(0);
            picture.setBackgroundColor(0);
            picture.setImageBitmap(decoded3);
        }

    }

    //ON_CLICK takepicture
    public void takePicture(View view)
    {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED)
        {
            openCamara();
        }else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},
                    SOLICITUD_PERMISO_WRITE_CALL_LOG);

        }
    }

    public void openCamara()
    {
        String file = RUTA_FOTOS + getCode();
        File mi_foto = new File( file );
        try {
            mi_foto.createNewFile();
        } catch (IOException ex) {
            Log.e("ERROR ", "Error:" + ex);
        }
        //
        Uri uri = Uri.fromFile( mi_foto );
        //Abre la camara para tomar la foto
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Guarda imagen
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        //Retorna a la actividad
        startActivityForResult(cameraIntent, 1);
    }

    private String getCode()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
        String date = dateFormat.format(new Date() );
        String photoCode = "pic_" + date+".jpg";
        nombreFotoActual=photoCode;
        return photoCode;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sell, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_comprar) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();


    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == SOLICITUD_PERMISO_WRITE_CALL_LOG) {
            if (grantResults.length== 2 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamara();
            } else {

                Toast.makeText(this,"SIN EL PERMISO NO SE PUEDE TOMAR FOTO DEL PRODUCTO", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //On-click de publicar
    public void publicar (View view)
    {
        String file = RUTA_FOTOS + nombreFotoActual;
        File mi_foto = new File( file );
        Uri uri = Uri.fromFile( mi_foto );

        String nombreP=txtNombre.getText().toString();
        String precioP=txtPrecio.getText().toString();

        String descriP=txtDescripcion.getText().toString();
        String categoria=spinner.getSelectedItem().toString();

        if(nombreP.equals(""))
            txtNombre.setError("Ingrese un nombre para su producto");
        else
        if(precioP.equals(""))
            txtPrecio.setError("Indique el valor de su producto");
        else
        {
            int valor=Integer.parseInt(precioP);
           if(principal.darUsuario().agregarProducto(nombreP,descriP,nombreFotoActual,valor,categoria))
           {
               principal.getDataBase().subirProducto(nombreP,principal.darUsuario().getNickName(),precioP,descriP,nombreFotoActual,categoria,principal.darUsuario().getNombre());
               principal.getDataBase().subirImagen(principal.darUsuario().getNickName(),nombreP,nombreFotoActual,uri);
               new HiloEsperaPA(new ProgressDialog(this),this,principal.getDataBase()).execute();
           }else
           {
               Toast.makeText(this,"USTED YA TIENE UN PRODUCTO REGISTRADO CON ESE NOMBRE", Toast.LENGTH_SHORT).show();
           }




        }


    }


    public void cerrar()
    {

        Intent i=new Intent(this,SellerActivity.class);
        i.putExtra("modelo",principal);
        i.putExtra("info","NH");
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {


        Intent i=new Intent(this,SellerActivity.class);
        i.putExtra("modelo",principal);
        i.putExtra("info","NH");
        startActivity(i);
        finish();

    }

    public static Bitmap decodeFile1(File f, int WIDTH, int HIGHT){
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);

            //The new size we want to scale to
            final int REQUIRED_WIDTH=WIDTH;
            final int REQUIRED_HIGHT=HIGHT;
            //Find the correct scale value. It should be the power of 2.
            int scale=1;
            while(o.outWidth/scale/2>=REQUIRED_WIDTH && o.outHeight/scale/2>=REQUIRED_HIGHT)
                scale*=2;

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }



}
