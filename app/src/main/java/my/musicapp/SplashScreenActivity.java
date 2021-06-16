package my.musicapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreenActivity extends AppCompatActivity {

    private static int Splash_timeout = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(ContextCompat.checkSelfPermission(SplashScreenActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE )== PackageManager.PERMISSION_GRANTED) {

                    Intent homeIntent = new Intent(SplashScreenActivity.this, AudioPermission.class);
                    startActivity(homeIntent);
                    finish();


                } else {
                    Intent ReqIntent = new Intent(SplashScreenActivity.this, StoragePermission.class);
                    startActivity(ReqIntent);
                    finish();
                }


            }
        },Splash_timeout);
    }
}
