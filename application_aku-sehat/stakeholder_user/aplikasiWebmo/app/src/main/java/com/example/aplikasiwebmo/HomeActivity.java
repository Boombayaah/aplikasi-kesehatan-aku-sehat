package com.example.aplikasiwebmo;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplikasiwebmo.model.LoginResponse;
import com.example.aplikasiwebmo.network.ApiService;
import com.example.aplikasiwebmo.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.aplikasiwebmo.staff.admin_leader;
import com.example.aplikasiwebmo.staff.admin_staff;
import com.example.aplikasiwebmo.staff.dokter;

public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check for Auto-Redirection if Staff is already logged in
        if (checkStaffAutoRedirect()) return;

        setContentView(R.layout.activity_home);

        // Home Activity shows the Dropdown Menu
        setupCommonUI(true);
        updateHeaderUI();

        ScrollView mainScrollView = findViewById(R.id.mainScrollView);
        View btnNavHome = findViewById(R.id.btnNavHome);
        if (btnNavHome != null) btnNavHome.setOnClickListener(v -> {
            if (mainScrollView != null) mainScrollView.smoothScrollTo(0, 0);
        });

        Button btnHomeLogin = findViewById(R.id.btnHomeLogin);
        if (btnHomeLogin != null) btnHomeLogin.setOnClickListener(v -> showLoginPopup());

        findViewById(R.id.btnNavCreateSchedule).setOnClickListener(v -> handleFeatureClick(ScheduleActivity.class, "Buat Jadwal"));
        findViewById(R.id.btnNavCheckIn).setOnClickListener(v -> handleFeatureClick(CheckinActivity.class, "CheckIn"));
        findViewById(R.id.btnNavWaitingList).setOnClickListener(v -> handleFeatureClick(QueueStatusActivity.class, "Antrean"));
        findViewById(R.id.btnNavMedicalHistory).setOnClickListener(v -> handleFeatureClick(MedicalHistoryActivity.class, "Riwayat"));
        findViewById(R.id.btnNavMedicalBilling).setOnClickListener(v -> handleFeatureClick(MedicalBillingActivity.class, "Tagihan"));
        findViewById(R.id.btnNavKlinikFlow).setOnClickListener(v -> handleFeatureClick(KlinikFlowActivity.class, "Alur"));

        // New: Check Role and show Admin/Doctor Dashboard button if applicable
        updateStaffNavigation();
    }

    private void updateStaffNavigation() {
        String role = sharedPrefs.getString("user_role", "").toLowerCase();
        
        // Final safeguard for auto-redirect
        if (checkStaffAutoRedirect()) return;

        View staffSection = findViewById(R.id.llStaffDashboardSection); 
        
        if (role.contains("admin") || role.contains("dokter") || role.contains("leader")) {
            if (staffSection != null) staffSection.setVisibility(View.VISIBLE);
            
            Button btnGoToStaff = findViewById(R.id.btnGoToStaffDashboard);
            if (btnGoToStaff != null) {
                btnGoToStaff.setVisibility(View.VISIBLE);
                btnGoToStaff.setOnClickListener(v -> {
                    Class<?> target = HomeActivity.class;
                    if (role.equals("admin")) target = admin_staff.class;
                    else if (role.equals("dokter")) target = dokter.class;
                    else if (role.contains("leader")) target = admin_leader.class;
                    
                    startActivity(new Intent(this, target));
                });
            }
        } else {
            if (staffSection != null) staffSection.setVisibility(View.GONE);
        }
    }

    private boolean checkStaffAutoRedirect() {
        if (!sharedPrefs.getBoolean("isLoggedIn", false)) return false;
        
        String role = sharedPrefs.getString("user_role", "").toLowerCase();
        Class<?> target = null;

        if (role.contains("admin") && !role.contains("leader")) target = admin_staff.class;
        else if (role.contains("dokter")) target = dokter.class;
        else if (role.contains("leader")) target = admin_leader.class;

        if (target != null) {
            Intent intent = new Intent(this, target);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return false;
    }

    private void handleFeatureClick(Class<?> activityClass, String featureName) {
        trackFeatureUsage(featureName);
        if (featureName.equals("Alur") || isLoggedIn) {
            startActivity(new Intent(this, activityClass));
        } else {
            Toast.makeText(this, "Silakan login terlebih dahulu", Toast.LENGTH_SHORT).show();
            showLoginPopup();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isLoggedIn = sharedPrefs.getBoolean("isLoggedIn", false);
        updateHeaderUI();
        updateStaffNavigation();
        setupCommonUI(true); 
    }

    private void updateHeaderUI() {
        View guestSection = findViewById(R.id.llHomeGuestSection);
        if (guestSection != null) guestSection.setVisibility(isLoggedIn ? View.GONE : View.VISIBLE);
        
        TextView tvGreeting = findViewById(R.id.tvHomeGreeting);
        if (tvGreeting != null) {
            String name = sharedPrefs.getString("user_name", "");
            if (isLoggedIn && !name.isEmpty()) {
                tvGreeting.setText("Selamat pagi, " + name);
            } else {
                tvGreeting.setText("Selamat pagi,");
            }
        }
    }

    private void showLoginPopup() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.layout_login_popup, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, 
                (int) (getResources().getDisplayMetrics().widthPixels * 0.9), 
                LinearLayout.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        popupWindow.showAtLocation(findViewById(R.id.activityHomeRoot), Gravity.CENTER, 0, 0);

        popupView.findViewById(R.id.btnCloseLogin).setOnClickListener(v -> popupWindow.dismiss());
        popupView.findViewById(R.id.tvToRegister).setOnClickListener(v -> {
            popupWindow.dismiss();
            showRegisterPopup();
        });

        Button btnDoLogin = popupView.findViewById(R.id.btnDoLogin);
        EditText etPhone = popupView.findViewById(R.id.etLoginIdentifier);
        EditText etPass = popupView.findViewById(R.id.etLoginPassword);

        btnDoLogin.setOnClickListener(v -> {
            String phone = etPhone.getText().toString();
            String pass = etPass.getText().toString();

            if (phone.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Harap isi nomor telepon dan password", Toast.LENGTH_SHORT).show();
                return;
            }

            ApiService api = RetrofitClient.getInstance().create(ApiService.class);
            api.loginUser(phone, pass).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        LoginResponse res = response.body();
                        if ("success".equals(res.status)) {
                            saveSession(res);
                            isLoggedIn = true;
                            updateHeaderUI();
                            updateStaffNavigation();
                            setupCommonUI(true);
                            popupWindow.dismiss();
                            Toast.makeText(HomeActivity.this, "Login Berhasil!", Toast.LENGTH_SHORT).show();
                            
                            // Check for staff redirect
                            if (res.user != null && res.user.namaKategori != null) {
                                String role = res.user.namaKategori.toLowerCase();
                                if (role.contains("admin") || role.contains("dokter") || role.contains("leader")) {
                                    Toast.makeText(HomeActivity.this, "Selamat Datang, " + res.user.namaKategori, Toast.LENGTH_LONG).show();
                                }
                            }
                        } else {
                            Toast.makeText(HomeActivity.this, res.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                @Override public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(HomeActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void saveSession(LoginResponse res) {
        android.content.SharedPreferences.Editor editor = sharedPrefs.edit();
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
    }

    private void showRegisterPopup() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.layout_register_popup, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, 
                (int) (getResources().getDisplayMetrics().widthPixels * 0.9), 
                (int) (getResources().getDisplayMetrics().heightPixels * 0.8), true);

        popupWindow.setAnimationStyle(android.R.style.Animation_InputMethod);
        popupWindow.showAtLocation(findViewById(R.id.activityHomeRoot), Gravity.CENTER, 0, 0);

        popupView.findViewById(R.id.btnCloseReg).setOnClickListener(v -> popupWindow.dismiss());
        popupView.findViewById(R.id.tvToLogin).setOnClickListener(v -> {
            popupWindow.dismiss();
            showLoginPopup();
        });

        Button btnDoReg = popupView.findViewById(R.id.btnDoRegister);
        EditText etName = popupView.findViewById(R.id.etRegName);
        EditText etNIK = popupView.findViewById(R.id.etRegNIK);
        EditText etPhone = popupView.findViewById(R.id.etRegPhone);
        EditText etAddress = popupView.findViewById(R.id.etRegAddress);
        EditText etPass = popupView.findViewById(R.id.etRegPass);
        EditText etPassConf = popupView.findViewById(R.id.etRegPassConfirm);
        CheckBox cbMale = popupView.findViewById(R.id.cbMale);
        CheckBox cbFemale = popupView.findViewById(R.id.cbFemale);

        cbMale.setOnCheckedChangeListener((buttonView, isChecked) -> { if (isChecked) cbFemale.setChecked(false); });
        cbFemale.setOnCheckedChangeListener((buttonView, isChecked) -> { if (isChecked) cbMale.setChecked(false); });

        btnDoReg.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String nik = etNIK.getText().toString();
            String phone = etPhone.getText().toString();
            String address = etAddress.getText().toString();
            String pass = etPass.getText().toString();
            String passConf = etPassConf.getText().toString();

            if (name.isEmpty() || nik.isEmpty() || phone.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Harap lengkapi data", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!pass.equals(passConf)) {
                Toast.makeText(this, "Password tidak sama", Toast.LENGTH_SHORT).show();
                return;
            }
            performRegistration(name, nik, phone, pass, cbMale.isChecked() ? "L" : "P", address, popupWindow);
        });
    }

    private void performRegistration(String name, String nik, String phone, String pass, String gender, String address, PopupWindow popupWindow) {
        ApiService api = RetrofitClient.getInstance().create(ApiService.class);
        api.registerUser(name, nik, phone, pass, gender, address).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if ("success".equals(response.body().status)) {
                        Toast.makeText(HomeActivity.this, "Pendaftaran Berhasil!", Toast.LENGTH_SHORT).show();
                        saveSession(response.body());
                        isLoggedIn = true;
                        updateHeaderUI();
                        setupCommonUI(true);
                        popupWindow.dismiss();
                    } else {
                        Toast.makeText(HomeActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}