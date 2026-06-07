package com.example.aplikasiwebmo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logo = findViewById(R.id.logoSplash);

        // Menjalankan animasi breathing kembang kempis
        Animation breathAnim = AnimationUtils.loadAnimation(this, R.anim.breath);
        logo.startAnimation(breathAnim);

        // Menahan layar selama 3 detik lalu lompat ke halaman Home
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, HomeActivity.class));
            finish();
        }, 3000);
    }
}