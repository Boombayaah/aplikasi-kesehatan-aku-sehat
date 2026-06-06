package com.example.aplikasiwebmo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.aplikasiwebmo.model.LoginResponse;
import com.example.aplikasiwebmo.network.ApiService;
import com.example.aplikasiwebmo.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.example.akusehat.admin_staff;
// import com.example.akusehat.admin_leader;
import com.example.akusehat.dokter;

public class LoginActivity extends AppCompatActivity {
    private SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPrefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        EditText etNik = findViewById(R.id.etLoginIdentifier);
        EditText etPass = findViewById(R.id.etLoginPassword);
        Button btnLogin = findViewById(R.id.btnLoginSubmit);

        btnLogin.setOnClickListener(v -> {
            String phone = etNik.getText().toString();
            String pass = etPass.getText().toString();
            if (!phone.isEmpty() && !pass.isEmpty()) {
                // Task 2: Hardcoded Exceptions for Pasien A and B
                if ((phone.equals("081444444444") && pass.equals("PASSWORD")) || 
                    (phone.equals("081555555555") && pass.equals("PASSWORD"))) {
                    
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putBoolean("isLoggedIn", true);
                    if (phone.equals("081444444444")) {
                        editor.putInt("currentUserId", 4); 
                        editor.putInt("id_pasien", 1);
                        editor.putString("user_name", "Pasien A");
                        editor.putString("user_nik", "1234567800123454");
                    } else {
                        editor.putInt("currentUserId", 5);
                        editor.putInt("id_pasien", 2);
                        editor.putString("user_name", "Pasien B");
                        editor.putString("user_nik", "1234567800123455");
                    }
                    editor.putString("user_phone", phone);
                    editor.apply();
                    
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                    return;
                }
                performLogin(phone, pass);
            } else {
                Toast.makeText(this, "Harap isi Nomor Telepon dan Password", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btnRegister).setOnClickListener(v -> {
            finish();
        });
    }

    private void performLogin(String phone, String pass) {
        ApiService api = RetrofitClient.getInstance().create(ApiService.class);
        api.loginUser(phone, pass).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse res = response.body();
                    if ("success".equals(res.status)) {
                        SharedPreferences.Editor editor = sharedPrefs.edit();
                        editor.putBoolean("isLoggedIn", true);
                        editor.putInt("currentUserId", res.idUser);
                        editor.putInt("id_pasien", res.idPasien);
                        
                        if (res.user != null) {
                            editor.putString("user_name", res.user.namaLengkap);
                            editor.putString("user_nik", res.user.nik);
                            editor.putString("user_address", res.user.alamat);
                            editor.putString("user_phone", res.user.noHp);
                            editor.putString("user_dob", res.user.tanggalLahir);
                            editor.putString("user_role", res.user.namaKategori);
                        }
                        
                        if (res.pasien != null) {
                            editor.putString("user_bpjs", res.pasien.noBpjs);
                            editor.putString("user_gender", res.pasien.jenisKelamin);
                        }
                        
                        editor.apply();

                        Class<?> targetActivity = HomeActivity.class;
                        
                        if (res.user != null && res.user.namaKategori != null) {
                            String role = res.user.namaKategori.toLowerCase();
                            
                            if (role.equals("admin")) {
                                targetActivity = admin_staff.class; 
                                Toast.makeText(LoginActivity.this, "Welcome Admin", Toast.LENGTH_SHORT).show();
                            } else if (role.equals("dokter")) {
                                targetActivity = dokter.class; 
                                Toast.makeText(LoginActivity.this, "Welcome Dokter", Toast.LENGTH_SHORT).show();
                            } else if (role.equals("pasien")) {
                                targetActivity = HomeActivity.class;
                            } // else if (role.equals("admin_leader")) {
                                // targetActivity = .class
                            // }
                        }

                        Intent intent = new Intent(LoginActivity.this, targetActivity);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, res.message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Login Gagal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}