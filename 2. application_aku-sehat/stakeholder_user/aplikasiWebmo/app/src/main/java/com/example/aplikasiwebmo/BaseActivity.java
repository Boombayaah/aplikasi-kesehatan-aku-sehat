package com.example.aplikasiwebmo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aplikasiwebmo.model.LoginResponse;
import com.example.aplikasiwebmo.network.ApiService;
import com.example.aplikasiwebmo.network.RetrofitClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class BaseActivity extends AppCompatActivity {

    protected SharedPreferences sharedPrefs;
    protected boolean isLoggedIn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        isLoggedIn = sharedPrefs.getBoolean("isLoggedIn", false);
    }

    protected void setupCommonUI(boolean showMenuInNavbar) {
        // Setup Navbar Menu Visibility
        View btnMenu = findViewById(R.id.btnGlobalMenu);
        if (btnMenu != null) {
            btnMenu.setVisibility(showMenuInNavbar ? View.VISIBLE : View.GONE);
            if (showMenuInNavbar) {
                btnMenu.setOnClickListener(this::showGlobalMenuDropdown);
            }
        }

        // Setup Footer (Last 3 Features)
        updateFooterRecentFeatures();
        
        // Setup Home Button in Footer
        View btnHomeFooter = findViewById(R.id.btnNavHome);
        if (btnHomeFooter != null) {
            btnHomeFooter.setOnClickListener(v -> {
                if (!(this instanceof HomeActivity)) {
                    startActivity(new Intent(this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
            });
        }
    }

    private void updateFooterRecentFeatures() {
        LinearLayout container = findViewById(R.id.llRecentFeaturesContainer);
        if (container == null) return;

        String recentJson = sharedPrefs.getString("recent_features", "[]");
        List<String> recentList = new Gson().fromJson(recentJson, new TypeToken<List<String>>(){}.getType());

        container.removeAllViews();
        // Take only last 3
        int count = 0;
        for (String featureName : recentList) {
            if (count >= 3) break;
            addFeatureToFooter(container, featureName);
            count++;
        }
    }

    private void addFeatureToFooter(LinearLayout container, String featureName) {
        LinearLayout item = new LinearLayout(this);
        item.setOrientation(LinearLayout.VERTICAL);
        item.setGravity(android.view.Gravity.CENTER);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        item.setLayoutParams(params);
        item.setPadding(4, 8, 4, 8);
        item.setClickable(true);
        item.setFocusable(true);
        
        // FIX CRASH: Correct way to set selectable background
        TypedValue outValue = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        item.setBackgroundResource(outValue.resourceId);

        ImageView icon = new ImageView(this);
        // Convert 45dp to pixels for better visibility in taller footer
        int iconSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, getResources().getDisplayMetrics());
        icon.setLayoutParams(new LinearLayout.LayoutParams(iconSize, iconSize));
        
        TextView text = new TextView(this);
        text.setText(featureName);
        text.setTextSize(10);
        text.setGravity(android.view.Gravity.CENTER);
        text.setMaxLines(1);
        text.setEllipsize(android.text.TextUtils.TruncateAt.END);
        text.setTextColor(android.graphics.Color.parseColor("#333333"));
        text.setPadding(0, 4, 0, 0);

        int iconRes = R.drawable.d_logo1;
        Class<?> targetActivity = null;

        if (featureName.equals("Buat Jadwal")) { iconRes = R.drawable.d_calendaricon; targetActivity = ScheduleActivity.class; }
        else if (featureName.equals("CheckIn")) { iconRes = R.drawable.d_checklist; targetActivity = CheckinActivity.class; }
        else if (featureName.equals("Antrean")) { iconRes = R.drawable.d_waitinglist; targetActivity = QueueStatusActivity.class; }
        else if (featureName.equals("Riwayat")) { iconRes = R.drawable.d_riwayatmedis; targetActivity = MedicalHistoryActivity.class; }
        else if (featureName.equals("Tagihan")) { iconRes = R.drawable.d_paymentlogo; targetActivity = MedicalBillingActivity.class; }
        else if (featureName.equals("Alur")) { iconRes = R.drawable.d_logoalur; targetActivity = KlinikFlowActivity.class; }

        icon.setImageResource(iconRes);
        item.addView(icon);
        item.addView(text);

        final Class<?> finalTarget = targetActivity;
        item.setOnClickListener(v -> {
            if (finalTarget != null) {
                if (featureName.equals("Alur") || isLoggedIn) {
                    if (this.getClass() != finalTarget) {
                        startActivity(new Intent(this, finalTarget));
                    }
                } else {
                    Toast.makeText(this, "Silakan login terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
            }
        });

        container.addView(item);
    }

    protected void trackFeatureUsage(String featureName) {
        String recentJson = sharedPrefs.getString("recent_features", "[]");
        List<String> recentList = new Gson().fromJson(recentJson, new TypeToken<List<String>>(){}.getType());

        recentList.remove(featureName);
        recentList.add(0, featureName);
        // Keep only top 3 for the footer display
        if (recentList.size() > 5) recentList.remove(5);

        sharedPrefs.edit().putString("recent_features", new Gson().toJson(recentList)).apply();
    }

    private void showGlobalMenuDropdown(View view) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.layout_menu_dropdown, null);
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.8);
        final PopupWindow popupWindow = new PopupWindow(popupView, width, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setElevation(20);
        popupWindow.showAsDropDown(view, -width + view.getWidth(), 10);

        View guestButtons = popupView.findViewById(R.id.llMenuGuestButtons);
        View btnLogout = popupView.findViewById(R.id.btnMenuLogout);

        if (guestButtons != null) guestButtons.setVisibility(isLoggedIn ? View.GONE : View.VISIBLE);
        if (btnLogout != null) btnLogout.setVisibility(isLoggedIn ? View.VISIBLE : View.GONE);

        popupView.findViewById(R.id.btnCloseMenu).setOnClickListener(v -> popupWindow.dismiss());
        
        popupView.findViewById(R.id.llMenuCreateSchedule).setOnClickListener(v -> { popupWindow.dismiss(); navigateTo(ScheduleActivity.class, "Buat Jadwal"); });
        popupView.findViewById(R.id.llMenuCheckIn).setOnClickListener(v -> { popupWindow.dismiss(); navigateTo(CheckinActivity.class, "CheckIn"); });
        popupView.findViewById(R.id.llMenuQueueStatus).setOnClickListener(v -> { popupWindow.dismiss(); navigateTo(QueueStatusActivity.class, "Antrean"); });
        popupView.findViewById(R.id.llMenuMedicalHistory).setOnClickListener(v -> { popupWindow.dismiss(); navigateTo(MedicalHistoryActivity.class, "Riwayat"); });
        popupView.findViewById(R.id.llMenuMedicalBilling).setOnClickListener(v -> { popupWindow.dismiss(); navigateTo(MedicalBillingActivity.class, "Tagihan"); });
        popupView.findViewById(R.id.llMenuKlinikFlow).setOnClickListener(v -> { popupWindow.dismiss(); navigateTo(KlinikFlowActivity.class, "Alur"); });
        
        // Handle Masuk and Daftar buttons in Menu
        if (!isLoggedIn) {
            View btnMenuLogin = popupView.findViewById(R.id.btnMenuLogin);
            View btnMenuRegister = popupView.findViewById(R.id.btnMenuRegister);

            if (btnMenuLogin != null) {
                btnMenuLogin.setOnClickListener(v -> {
                    popupWindow.dismiss();
                    showLoginPopup();
                });
            }
            if (btnMenuRegister != null) {
                btnMenuRegister.setOnClickListener(v -> {
                    popupWindow.dismiss();
                    showRegisterPopup();
                });
            }
        }

        if (isLoggedIn) {
            popupView.findViewById(R.id.tvMenuProfile).setOnClickListener(v -> { popupWindow.dismiss(); startActivity(new Intent(this, SettingsActivity.class)); });
            popupView.findViewById(R.id.btnMenuLogout).setOnClickListener(v -> {
                // Clear all possible session keys
                sharedPrefs.edit().clear().apply();
                getSharedPreferences("USER_SESSION", MODE_PRIVATE).edit().clear().apply();
                isLoggedIn = false;
                startActivity(new Intent(this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finish();
            });
        }
    }

    private void navigateTo(Class<?> target, String featureName) {
        trackFeatureUsage(featureName);
        if (featureName.equals("Alur") || isLoggedIn) {
            if (this.getClass() != target) {
                startActivity(new Intent(this, target));
            }
        } else {
            Toast.makeText(this, "Silakan login terlebih dahulu", Toast.LENGTH_SHORT).show();
            showLoginPopup();
        }
    }

    protected void showLoginPopup() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.layout_login_popup, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, 
                (int) (getResources().getDisplayMetrics().widthPixels * 0.9), 
                LinearLayout.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        
        // Use a generic way to find a root view for showing at location
        View rootView = findViewById(android.R.id.content);
        popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);

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
                            
                            popupWindow.dismiss();
                            Toast.makeText(BaseActivity.this, "Login Berhasil!", Toast.LENGTH_SHORT).show();
                            
                            // Simple refresh by restarting current activity
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        } else {
                            Toast.makeText(BaseActivity.this, res.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                @Override public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(BaseActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    protected void saveSession(LoginResponse res) {
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
        
        // Also clear any staff specific session if this is a new login
        getSharedPreferences("USER_SESSION", MODE_PRIVATE).edit().clear().apply();
    }

    protected void showRegisterPopup() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.layout_register_popup, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, 
                (int) (getResources().getDisplayMetrics().widthPixels * 0.9), 
                (int) (getResources().getDisplayMetrics().heightPixels * 0.8), true);

        popupWindow.setAnimationStyle(android.R.style.Animation_InputMethod);
        View rootView = findViewById(android.R.id.content);
        popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);

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
        CheckBox cbTerms = popupView.findViewById(R.id.cbTerms);

        cbMale.setOnCheckedChangeListener((buttonView, isChecked) -> { if (isChecked) cbFemale.setChecked(false); });
        cbFemale.setOnCheckedChangeListener((buttonView, isChecked) -> { if (isChecked) cbMale.setChecked(false); });

        btnDoReg.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String nik = etNIK.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String address = etAddress.getText().toString().trim();
            String pass = etPass.getText().toString().trim();
            String passConf = etPassConf.getText().toString().trim();
            String gender = cbMale.isChecked() ? "L" : (cbFemale.isChecked() ? "P" : "");

            if (name.isEmpty() || nik.isEmpty() || phone.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Harap lengkapi data wajib (Nama, NIK, HP, Pass)", Toast.LENGTH_SHORT).show();
                return;
            }
            if (gender.isEmpty()) {
                Toast.makeText(this, "Harap pilih jenis kelamin", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!pass.equals(passConf)) {
                Toast.makeText(this, "Konfirmasi password tidak cocok", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!cbTerms.isChecked()) {
                Toast.makeText(this, "Harap setujui Syarat dan Ketentuan", Toast.LENGTH_SHORT).show();
                return;
            }
            performRegistration(name, nik, phone, pass, gender, address, popupWindow);
        });
    }

    protected void performRegistration(String name, String nik, String phone, String pass, String gender, String address, PopupWindow popupWindow) {
        ApiService api = RetrofitClient.getInstance().create(ApiService.class);
        
        // Log the registration attempt
        android.util.Log.d("REGISTER_DEBUG", "Name: " + name + ", NIK: " + nik + ", Phone: " + phone + ", Gender: " + gender);
        
        api.registerUser(name, nik, phone, pass, gender, address).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    if ("success".equals(loginResponse.status)) {
                        Toast.makeText(BaseActivity.this, "Pendaftaran Berhasil!", Toast.LENGTH_SHORT).show();
                        saveSession(loginResponse);
                        isLoggedIn = true;
                        popupWindow.dismiss();
                        
                        // Restart activity to refresh UI
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    } else {
                        String errMsg = loginResponse.message != null ? loginResponse.message : "Gagal mendaftar";
                        Toast.makeText(BaseActivity.this, errMsg, Toast.LENGTH_LONG).show();
                        android.util.Log.e("REGISTER_ERROR", "Server returned error: " + errMsg);
                    }
                } else {
                    Toast.makeText(BaseActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    try {
                        android.util.Log.e("REGISTER_ERROR", "Response failed: " + response.errorBody().string());
                    } catch (Exception e) { e.printStackTrace(); }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(BaseActivity.this, "Gagal koneksi ke server", Toast.LENGTH_SHORT).show();
                android.util.Log.e("REGISTER_ERROR", "Network failure: " + t.getMessage());
            }
        });
    }
}
