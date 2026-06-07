package com.example.aplikasiwebmo;

import android.content.Intent;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aplikasiwebmo.staff.admin_leader;
import com.example.aplikasiwebmo.staff.admin_staff;
import com.example.aplikasiwebmo.staff.dokter;
import com.example.aplikasiwebmo.model.LoginResponse;
import com.example.aplikasiwebmo.network.ApiService;
import com.example.aplikasiwebmo.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.widget.ImageView;

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
                    editor.putString("user_role", "Pasien");
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
            showRegisterDialog();
        });
    }

    private void showRegisterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.layout_register_popup, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();

        EditText etName = view.findViewById(R.id.etRegName);
        EditText etNik = view.findViewById(R.id.etRegNIK);
        EditText etPhone = view.findViewById(R.id.etRegPhone);
        EditText etPass = view.findViewById(R.id.etRegPass);
        EditText etPassConfirm = view.findViewById(R.id.etRegPassConfirm);
        EditText etAddress = view.findViewById(R.id.etRegAddress);
        CheckBox cbMale = view.findViewById(R.id.cbMale);
        CheckBox cbFemale = view.findViewById(R.id.cbFemale);
        CheckBox cbTerms = view.findViewById(R.id.cbTerms);
        Button btnDoRegister = view.findViewById(R.id.btnDoRegister);
        ImageView btnClose = view.findViewById(R.id.btnCloseReg);

        btnClose.setOnClickListener(v -> dialog.dismiss());

        btnDoRegister.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String nik = etNik.getText().toString();
            String phone = etPhone.getText().toString();
            String pass = etPass.getText().toString();
            String confirm = etPassConfirm.getText().toString();
            String address = etAddress.getText().toString();
            String gender = cbMale.isChecked() ? "L" : (cbFemale.isChecked() ? "P" : "");

            if (name.isEmpty() || nik.isEmpty() || phone.isEmpty() || pass.isEmpty() || gender.isEmpty()) {
                Toast.makeText(this, "Harap lengkapi data wajib", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!pass.equals(confirm)) {
                Toast.makeText(this, "Konfirmasi password tidak cocok", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!cbTerms.isChecked()) {
                Toast.makeText(this, "Harap setujui Terms & Conditions", Toast.LENGTH_SHORT).show();
                return;
            }

            performRegister(name, nik, phone, pass, gender, address, dialog);
        });

        dialog.show();
    }

    private void performRegister(String name, String nik, String phone, String pass, String gender, String address, AlertDialog dialog) {
        ApiService api = RetrofitClient.getInstance().create(ApiService.class);
        api.registerUser(name, nik, phone, pass, gender, address).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse res = response.body();
                    if ("success".equals(res.status)) {
                        Toast.makeText(LoginActivity.this, "Registrasi Berhasil! Silakan Login", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(LoginActivity.this, res.message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Registrasi Gagal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
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
                            editor.putString("user_role", res.user.namaKategori != null ? res.user.namaKategori : "Pasien");
                        }
                        
                        if (res.pasien != null) {
                            editor.putString("user_bpjs", res.pasien.noBpjs);
                            editor.putString("user_gender", res.pasien.jenisKelamin);
                            if (res.user != null && res.user.namaKategori == null) {
                                editor.putString("user_role", "Pasien");
                            }
                        }
                        
                        editor.apply();

                        Class<?> targetActivity = HomeActivity.class;
                        
                        if (res.user != null && res.user.namaKategori != null) {
                            String role = res.user.namaKategori.toLowerCase();
                            
                            if (role.contains("admin") && !role.contains("leader")) {
                                targetActivity = admin_staff.class; 
                                Toast.makeText(LoginActivity.this, "Selamat Datang, Admin Staff", Toast.LENGTH_SHORT).show();
                            } else if (role.contains("dokter")) {
                                targetActivity = dokter.class; 
                                Toast.makeText(LoginActivity.this, "Selamat Datang, Dokter", Toast.LENGTH_SHORT).show();
                            } else if (role.contains("leader")) {
                                targetActivity = admin_leader.class;
                                Toast.makeText(LoginActivity.this, "Selamat Datang, Admin Leader", Toast.LENGTH_SHORT).show();
                            } else {
                                // Default for Pasien
                                Toast.makeText(LoginActivity.this, "Selamat Datang Kembali", Toast.LENGTH_SHORT).show();
                            }
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