package com.example.akusehat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class main_activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = getSharedPreferences("USER_SESSION", MODE_PRIVATE);

        boolean isLoggedIn = sp.getBoolean("is_logged_in", false);
        String role = sp.getString("role", "");

        Intent intent;

        if (!isLoggedIn) {
            intent = new Intent(this, admin_staff.class);
        } else if (role.contains("Dokter")) {
            intent = new Intent(this, dokter.class);
        } else if (role.contains("Admin Leader")) {
            intent = new Intent(this, admin_leader.class);
        } else if (role.contains("Admin")) {
            intent = new Intent(this, admin_staff.class);
        } else {
            sp.edit().clear().apply();
            intent = new Intent(this, admin_staff.class);
        }

        startActivity(intent);
        finish();
    }
}