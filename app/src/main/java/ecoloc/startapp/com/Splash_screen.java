package ecoloc.startapp.com;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Space;
import android.widget.Toast;

public class Splash_screen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                NetworkInfo activeNetwork = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))
                        .getActiveNetworkInfo();

                if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {

                    // Load Webview
                    startActivity(new Intent(Splash_screen.this, AuthentificationActivity.class));
                } else {

                    // Show No internet
                    Toast.makeText(Splash_screen.this, "pas de connexion Internet", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }, 5000);
    }


}