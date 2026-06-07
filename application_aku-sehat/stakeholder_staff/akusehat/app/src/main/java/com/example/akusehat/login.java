package com.example.akusehat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import android.content.SharedPreferences;

public class login extends AppCompatActivity {

    EditText edtUsername, edtPassword;
    Button btnLogin;

    String url = NetworkConfig.BASE_URL + "login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> login());
    }

    private void login() {
        String nik = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        Toast.makeText(
                this,
                "nik: [" + nik + "] pass: [" + password + "]",
                Toast.LENGTH_LONG
        ).show();

        if (nik.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "nik dan password wajib diisi", Toast.LENGTH_SHORT).show();
            return;
        }


        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);

                        if (obj.getBoolean("success")) {
                            String role = obj.getString("role");
                            String namaLengkap = obj.getString("nama_lengkap");
                            int idUser = obj.getInt("id_user");

                            SharedPreferences sp = getSharedPreferences("USER_SESSION", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();

                            editor.putBoolean("is_logged_in", true);
                            editor.putInt("id_user", idUser);
                            editor.putString("nama_lengkap", namaLengkap);
                            editor.putString("role", role);

                            role = role.trim();

                            if (role.equalsIgnoreCase("Dokter")) {
                                int idDokter = obj.getInt("id_dokter");
                                editor.putInt("id_dokter", idDokter);
                                editor.apply();

                                Intent intent = new Intent(login.this, dokter.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();

                            } else if (role.equalsIgnoreCase("Admin Leader")) {

                                editor.apply();

                                Intent intent = new Intent(login.this, admin_leader.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();

                            } else if (role.equalsIgnoreCase("Admin")) {
                                editor.apply();

                                Intent intent = new Intent(login.this, admin_staff.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();                
                            } else {
                                Toast.makeText(this, "Role tidak dikenal: " + role, Toast.LENGTH_LONG).show();
                            }
                        }

                    } catch (Exception e) {
                        Toast.makeText(this, "response error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(this, "gagal konek server", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nomor_induk_kependudukan", nik);
                params.put("password", password);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
}