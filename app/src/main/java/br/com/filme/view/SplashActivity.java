package br.com.filme.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import br.com.filme.R;

public class SplashActivity extends AppCompatActivity implements Runnable{


    private Handler splashScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashScreen = new Handler();
        splashScreen.postDelayed(SplashActivity.this, 450);

        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorBlack));
    }



    @Override
    public void run() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        SplashActivity.this.startActivity(intent);
        SplashActivity.this.finish();
    }


}
