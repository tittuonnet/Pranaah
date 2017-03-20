package com.merclain.hackthon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.merclain.hackthon.Login.Login;
import com.merclain.hackthon.Utilities.PranaPreferences;

public class SplashScreen extends Activity {

    private static int SPLASH_TIME_OUT = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences(PranaPreferences.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                String loggedIn = sharedPreferences.getString(PranaPreferences.LOGGEDIN_SHARED_PREF, "");
                if(loggedIn.equals("true")){
                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                    finish();
                }else{
                    startActivity(new Intent(SplashScreen.this, Login.class));
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }
}
