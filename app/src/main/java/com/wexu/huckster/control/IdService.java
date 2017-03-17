package com.wexu.huckster.control;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by ASUS1 on 08/10/2016.
 */
public class IdService extends FirebaseInstanceIdService {


    @Override
    public void onTokenRefresh() {

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Hola", "Refreshed token: " + refreshedToken);
        SharedPreferences preferences=getSharedPreferences("info", Context.MODE_PRIVATE);
        String mensaje=preferences.getString("info","ERROR");
        if(!mensaje.equals("ERROR"))
        {
            String nickName=mensaje.split(";")[0];
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Usuarios");
            myRef.child(nickName).child("token").setValue(refreshedToken);
        }

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.




    }
}
