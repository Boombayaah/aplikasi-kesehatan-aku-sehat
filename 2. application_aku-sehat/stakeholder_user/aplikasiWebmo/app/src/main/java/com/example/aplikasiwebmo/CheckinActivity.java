package com.example.aplikasiwebmo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.aplikasiwebmo.model.Appointment;
import com.example.aplikasiwebmo.model.LoginResponse;
import com.example.aplikasiwebmo.network.ApiService;
import com.example.aplikasiwebmo.network.RetrofitClient;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckinActivity extends BaseActivity {

    private TextView tvDate, tvTime, tvStatus, tvUploadHint, tvDoctor;
    private ImageView ivPreview, ivSuccessIcon;
    private Button btnSubmit;
    private int currentAppointmentId = -1;

    private final ActivityResultLauncher<Intent> pickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    if (imageUri != null) {
                        ivPreview.setImageURI(imageUri);
                        onImageUploaded();
                    }
                }
            }
    );

    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bitmap imageBitmap = (Bitmap) result.getData().getExtras().get("data");
                    ivPreview.setImageBitmap(imageBitmap);
                    onImageUploaded();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);

        setupCommonUI(false);
        trackFeatureUsage("CheckIn");

        tvStatus = findViewById(R.id.tvCheckinStatus);
        tvUploadHint = findViewById(R.id.tvUploadHint);
        tvDate = findViewById(R.id.tvCheckinDate);
        tvTime = findViewById(R.id.tvCheckinTime);
        tvDoctor = findViewById(R.id.tvCheckinDoctor);
        ivPreview = findViewById(R.id.ivCheckinPreview);
        ivSuccessIcon = findViewById(R.id.ivCheckinSuccessIcon);
        btnSubmit = findViewById(R.id.btnCheckinSubmit);

        findViewById(R.id.btnCheckinUpload).setOnClickListener(v -> showImageSourceDialog());
        btnSubmit.setOnClickListener(v -> performCheckin());

        int passedId = getIntent().getIntExtra("passed_id", -1);
        String passedDate = getIntent().getStringExtra("passed_date");
        String passedTime = getIntent().getStringExtra("passed_time");

        if (passedId == -1) {
            passedId = sharedPrefs.getInt("current_id_kunjungan", -1);
        }

        if (passedId != -1) {
            currentAppointmentId = passedId;
            
            // Set initial data if passed from ScheduleActivity
            if (passedDate != null) tvDate.setText(passedDate);
            if (passedTime != null) tvTime.setText(formatVisitTime(passedTime));

            View content = findViewById(R.id.scrollCheckinContent);
            if (content != null) content.setVisibility(View.VISIBLE);
            View empty = findViewById(R.id.llCheckinEmptyState);
            if (empty != null) empty.setVisibility(View.GONE);
            
            // Fetch full details (especially Doctor Name) from backend
            fetchAppointmentDetails(passedId);
        } else {
            updateViewVisibility();
        }
    }

    private void fetchAppointmentDetails(int kunjunganId) {
        ApiService api = RetrofitClient.getInstance().create(ApiService.class);
        api.getVisitDetails(kunjunganId).enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    Appointment app = response.body().get(0);
                    tvDoctor.setText(app.namaDokter != null ? app.namaDokter : "Dokter Umum");
                    tvDate.setText(app.tanggalKunjungan);
                    tvTime.setText(formatVisitTime(app.waktuKunjungan));
                }
            }
            @Override public void onFailure(Call<List<Appointment>> call, Throwable t) {
                tvDoctor.setText("Gagal memuat detail");
            }
        });
    }

    private void performCheckin() {
        if (currentAppointmentId == -1) {
            Toast.makeText(this, "ID Kunjungan tidak ditemukan", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = sharedPrefs.getString("user_name", "User");
        String dateStr = new SimpleDateFormat("dd_MM_yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime());
        String formalTitle = name + "/" + dateStr;

        ApiService api = RetrofitClient.getInstance().create(ApiService.class);
        api.submitCheckin(currentAppointmentId, formalTitle).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null && "success".equals(response.body().status)) {
                    int nomorAntrean = response.body().nomorAntrean;
                    ivSuccessIcon.setVisibility(View.VISIBLE);
                    tvStatus.setText("Check-in Berhasil!");
                    btnSubmit.setVisibility(View.GONE);
                    sharedPrefs.edit().remove("current_id_kunjungan").apply();
                    btnSubmit.postDelayed(() -> {
                        Intent intent = new Intent(CheckinActivity.this, QueueStatusActivity.class);
                        intent.putExtra("id_kunjungan", currentAppointmentId);
                        intent.putExtra("nomor_antrean", nomorAntrean);
                        startActivity(intent);
                        finish();
                    }, 1500);
                }
            }
            @Override public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(CheckinActivity.this, "Koneksi gagal", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateViewVisibility() {
        int idPasien = sharedPrefs.getInt("id_pasien", -1);
        if (idPasien == -1) { showEmptyState("Sesi berakhir"); return; }

        ApiService api = RetrofitClient.getInstance().create(ApiService.class);
        api.getJadwal(idPasien).enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    Appointment latest = null;
                    for (Appointment app : response.body()) {
                        if ("Belum Checkin".equalsIgnoreCase(app.statusLayanan)) { latest = app; break; }
                    }
                    if (latest != null) {
                        currentAppointmentId = latest.idKunjungan;
                        findViewById(R.id.scrollCheckinContent).setVisibility(View.VISIBLE);
                        findViewById(R.id.llCheckinEmptyState).setVisibility(View.GONE);
                        showAppointmentConfirmation(latest);
                    } else { showEmptyState("Tidak ada jadwal aktif"); }
                } else { showEmptyState("Belum bikin jadwal"); }
            }
            @Override public void onFailure(Call<List<Appointment>> call, Throwable t) { showEmptyState("Koneksi gagal"); }
        });
    }

    private void showEmptyState(String msg) {
        findViewById(R.id.scrollCheckinContent).setVisibility(View.GONE);
        findViewById(R.id.llCheckinEmptyState).setVisibility(View.VISIBLE);
        TextView tvMsg = findViewById(R.id.tvNoScheduleText);
        if (tvMsg != null) tvMsg.setText(msg);
    }

    private void showAppointmentConfirmation(Appointment app) {
        tvDate.setText(app.tanggalKunjungan);
        tvTime.setText(formatVisitTime(app.waktuKunjungan));
        tvDoctor.setText(app.namaDokter != null ? app.namaDokter : "Dokter Umum");
        btnSubmit.setVisibility(View.GONE);
        ivPreview.setImageResource(android.R.drawable.ic_menu_upload);
        tvUploadHint.setText("Upload & Ambil Foto");
    }

    private String formatVisitTime(String rawTime) {
        if (rawTime == null || rawTime.isEmpty()) return "09:00 - 10:00 WIB";
        try {
            String startTime = rawTime.substring(0, 5);
            int startHour = Integer.parseInt(startTime.split(":")[0]);
            String endTime = String.format(Locale.getDefault(), "%02d:00", startHour + 1);
            return startTime + " - " + endTime + " WIB";
        } catch (Exception e) { return rawTime; }
    }

    private void onImageUploaded() {
        String name = sharedPrefs.getString("user_name", "User");
        String dateStr = new SimpleDateFormat("dd_MM_yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime());
        tvUploadHint.setText(name + "/" + dateStr);
        btnSubmit.setVisibility(View.VISIBLE);
    }

    private void showImageSourceDialog() {
        String[] options = {"Buka kamera", "Galeri"};
        new AlertDialog.Builder(this).setTitle("Pilih Sumber").setItems(options, (dialog, which) -> {
            if (which == 0) checkCameraPermission();
            else pickerLauncher.launch(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
        }).show();
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 102);
        } else { openCamera(); }
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try { cameraLauncher.launch(takePictureIntent); } catch (Exception e) {}
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 102 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { openCamera(); }
    }
}