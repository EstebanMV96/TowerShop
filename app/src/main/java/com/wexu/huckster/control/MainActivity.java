package com.wexu.huckster.control;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wexu.huckster.R;


public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((TextView)findViewById(R.id.textMain)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/bocadillo.otf"));
        ((TextView)findViewById(R.id.textWelcome)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/bocadillo.otf"));


    }

    public void start(View v){

        if(redDisponible())
        {
            if(v.getId()==R.id.btnComprar){
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                i.putExtra("info","comprar");
                startActivity(i);
                System.out.print("ESTA EN EL HILO HPTA");
                finish();

            }
            else if(v.getId()==R.id.btnVender){

                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                i.putExtra("info","vender");
                System.out.println("LOGIN");
                startActivity(i);
                finish();
            }
        }else
        {
            Toast notificacion= Toast.makeText(this,"POR FAVOR REVISE SU CONEXIÓN A INTERNET", Toast.LENGTH_SHORT);
            notificacion.show();
        }

    }


    private Boolean redDisponible(){
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
           NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info == null || !info.isConnected() || !info.isAvailable()) {


                return false;
            }

        }
        return true;
    }


}

