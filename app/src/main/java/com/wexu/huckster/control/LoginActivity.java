package com.wexu.huckster.control;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.wexu.huckster.R;
import com.wexu.huckster.control.productos.ProductsActivity;
import com.wexu.huckster.control.vendedor.SellerActivity;
import com.wexu.huckster.modelo.Huckster;


public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 1;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Huckster principal;
    private static  final String CLAVE_SERVIDOR="323589171087-5otvb726icmmvj41d6podpql9mjcl03n.apps.googleusercontent.com";
    private String info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("created");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        Bundle extras=getIntent().getExtras();
        info=extras.getString("info");
        TextView t= (TextView)findViewById(R.id.textLogin);

        t.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/advent.otf"));
        t= (TextView)findViewById(R.id.textAppName);
        t.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/nova.otf"));
        System.out.println("created afte setcontent");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(CLAVE_SERVIDOR)
                .requestEmail()
                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.sign_in_button).setOnClickListener(this);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    if(principal==null)
                    {
                        SharedPreferences preferences=getSharedPreferences("info", Context.MODE_PRIVATE);
                        String email=user.getEmail().split("@")[0].replace(".",";");

                        String photoUri= user.getPhotoUrl().toString();

                        principal=new Huckster(preferences,email,user.getDisplayName(),photoUri);
                        System.out.println("Esto es principal");
                        System.out.println(principal.toString());

                    }

                    if(info.equals("vender"))
                    {
                        lanzarViewVendedor();
                    }else
                    {
                        lanzarViewComprador();
                    }


                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            // ...
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());

                        }
                        // ...
                    }
                });
    }

    //METODOS PARA PASAR A LA SGUIENTE ACTIVIDAD

    public void lanzarViewVendedor()
    {
        System.out.println("SE PRENDIO ESTA MIERDA");
        Intent i= new Intent(this, SellerActivity.class);
        i.putExtra("modelo",principal);
        startActivity(i);
        finish();
    }


    private void lanzarViewComprador() {
        Intent i= new Intent(this, ProductsActivity.class);
        System.out.println("Esto es principal antes del intent");
        System.out.println(principal.toString());
        i.putExtra("modelo",principal);
        startActivity(i);
        finish();
    }


}



