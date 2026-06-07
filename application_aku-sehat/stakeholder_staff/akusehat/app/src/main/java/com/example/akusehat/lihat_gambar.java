package com.example.akusehat;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class lihat_gambar extends AppCompatActivity {

    ImageView imgAlur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lihat_gambar);

        imgAlur = findViewById(R.id.imgAlur);

        String gambarUrl = getIntent().getStringExtra("gambar_url");

        Glide.with(this)
                .load(gambarUrl)
                .into(imgAlur);
    }
}