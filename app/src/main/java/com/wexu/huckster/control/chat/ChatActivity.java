package com.wexu.huckster.control.chat;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;

import com.wexu.huckster.R;
import com.wexu.huckster.control.HiloCC;
import com.wexu.huckster.control.HiloConversa;
import com.wexu.huckster.modelo.Chat;
import com.wexu.huckster.modelo.Huckster;
import com.wexu.huckster.modelo.Mensaje;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class ChatActivity extends AppCompatActivity {
    private static final String TAG = "ChatActivity";

    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private FloatingActionButton buttonSend;
    private boolean side;
    private Chat principal;
    private Huckster hc;
    private HiloConversa hilo;
    private String nick;
    private  HiloCC hilo1;
    private String nameVendedor;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);

        Bundle extras=getIntent().getExtras();
        String info=extras.getString("info");
        String info1=extras.getString("info1");
        hc=(Huckster) extras.getSerializable("modelo");
        if(info!=null&&!info.equals(""))
        {
            Log.d("CHATACTI","LISTICO");
            nick=info;
            nameVendedor=info1;
          hilo1=  new HiloCC(this,hc,nick);
            hilo1.execute();

        }else {
            principal =(Chat)extras.getSerializable("chat");
            nick=principal.getUserName();
            Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
            // Configuración de la barra de herramientas (toolbar ) como la Barra de acciones (ActionBar)
            // con la llamada de setSupportActionBar ()
            String name=principal.darNombreDestinatario();

            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(name);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            buttonSend = (FloatingActionButton) findViewById(R.id.send);

            listView = (ListView) findViewById(R.id.msgview);

            chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.right);
            for(int i=0;i<principal.darConversacion().size();i++)
            {
                chatArrayAdapter.add(principal.darConversacion().get(i));
            }
            listView.setAdapter(chatArrayAdapter);

            chatText = (EditText) findViewById(R.id.msg);
            chatText.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        return sendChatMessage();
                    }
                    return false;
                }
            });

            buttonSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {


                    sendChatMessage();
                }
            });

            listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
            //listView.setAdapter(chatArrayAdapter);

            //to scroll the list view to bottom on data change
            chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    listView.setSelection(chatArrayAdapter.getCount() - 1);
                }
            });
            SharedPreferences preferencias=getSharedPreferences("datos", Context.MODE_PRIVATE);
            hilo=new HiloConversa(this,preferencias,hc,principal);
            hilo.execute();

        }





    }

    private boolean sendChatMessage() {

        Log.d("ENTRO CHAT","SE METIO");
        side = true;
        if(chatText.getText()!=null){

            String mensaje=chatText.getText().toString();
            Chat buscado= hc.darUsuario().buscarChat(nick);
            buscado.escribirMensaje(mensaje);
            refrescar(buscado);
            hilo.cargarInfo(principal.darToken(),principal.getNombreRemitente(),mensaje,principal.getUserName(),hc.darUsuario().getToken());
            hilo.enviarMensaje1();
            SharedPreferences preferencias=getSharedPreferences("datos", Context.MODE_PRIVATE);
            hc.darUsuario().salvarInfo(preferencias);
            chatText.setText("");

        }
        return true;
    }

    public void receiveChatMessage(String m){

    }


    public void lanzarFragment(String token)
    {

        chatText = (EditText) findViewById(R.id.msg);
        chatText.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }
                return false;
            }
        });

        Log.d("LANZO EL FRAGMENTO","OK");
        if(hc.darUsuario().buscarChat(nick)==null)
        {
            principal=new Chat(nick,hc.darUsuario().getNombre(),token,nameVendedor);
            hc.darUsuario().agregarChat(principal);
        }else
        {
            principal=hc.darUsuario().buscarChat(nick);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        // Configuración de la barra de herramientas (toolbar ) como la Barra de acciones (ActionBar)
        // con la llamada de setSupportActionBar ()
        String name=principal.darNombreDestinatario();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonSend = (FloatingActionButton) findViewById(R.id.send);

        listView = (ListView) findViewById(R.id.msgview);

        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.right);
        for(int i=0;i<principal.darConversacion().size();i++)
        {
            chatArrayAdapter.add(principal.darConversacion().get(i));
        }
        listView.setAdapter(chatArrayAdapter);


        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                sendChatMessage();
            }
        });

        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        //listView.setAdapter(chatArrayAdapter);

        //to scroll the list view to bottom on data change
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });
        SharedPreferences preferencias=getSharedPreferences("datos", Context.MODE_PRIVATE);
        hilo=new HiloConversa(this,preferencias,hc,principal);
        hilo.execute();
    }



    public void refrescar(Chat c)
    {


       chatArrayAdapter.add(c.getLastMessage());



    }

    public void refresh(Chat c)
    {
        chatArrayAdapter.add(c.getLastMessage());
    }



    @Override
    public void onBackPressed() {

        hilo.destruir();
        finish();


    }


}
