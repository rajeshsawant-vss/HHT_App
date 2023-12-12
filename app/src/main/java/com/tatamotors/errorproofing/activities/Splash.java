package com.tatamotors.errorproofing.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.net.MacAddress;
import android.os.Bundle;
import android.view.View;

import com.tatamotors.errorproofing.MainActivity;
import com.tatamotors.errorproofing.R;
import com.tatamotors.errorproofing.databinding.ActivityLoginBinding;
import com.tatamotors.errorproofing.databinding.ActivitySplashBinding;
import com.tatamotors.errorproofing.util.AppPreference;

public class Splash extends AppCompatActivity {

    private int progress = 0;
    ActivitySplashBinding splashBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }catch (Exception e){

        }

      //  setContentView(R.layout.activity_splash);

        splashBinding = ActivitySplashBinding.inflate(getLayoutInflater());
        View view = splashBinding.getRoot();
        setContentView(view);

        new WaitTask().start();

    }

    class WaitTask extends Thread{
        boolean isProgress=true;
        @Override
        public void run() {
            super.run();

            while (isProgress){
                progress=progress+5;
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                splashBinding.progressBar.setProgress(progress);
                if(progress==100){
                    isProgress=false;
                }
            }
//
//            try {
//
//
//
//            } catch
//            (InterruptedException e) {
//                e.printStackTrace();
//            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    checkLogin();
                }
            });
        }
    }

    private void checkLogin(){



        if(!AppPreference.getInstance().get(getApplicationContext(),AppPreference.username).equalsIgnoreCase("")){

            Intent i=new Intent(Splash.this, MainActivity.class);
            startActivity(i);
            finish();

//            String key=AppPreference.getInstance().get(getApplicationContext(),AppPreference.username);
//            if(AppPreference.getInstance().getPartList(getApplicationContext(),key)!=null
//            && !AppPreference.getInstance().get(getApplicationContext(),AppPreference.module).equalsIgnoreCase(AppPreference.booking) ){
//
//                Intent i=new Intent(Splash.this, PartValidation.class);
//                startActivity(i);
//                finish();
//            }
//            else {
//                Intent i=new Intent(Splash.this, MainActivity.class);
//                startActivity(i);
//                finish();
//            }


        }
        else {
            Intent i=new Intent(Splash.this, Login.class);
            startActivity(i);
            finish();
        }
    }
}