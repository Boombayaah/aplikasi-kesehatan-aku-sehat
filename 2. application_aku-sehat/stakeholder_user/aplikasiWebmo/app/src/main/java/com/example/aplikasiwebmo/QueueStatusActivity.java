package com.example.aplikasiwebmo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.aplikasiwebmo.model.LoginResponse;
import com.example.aplikasiwebmo.network.ApiService;
import com.example.aplikasiwebmo.network.RetrofitClient;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QueueStatusActivity extends BaseActivity {

    private TextView tvMyQueue, tvCurrentQueue, tvArrived, tvName, tvDate;
    private Button btnFinish;
    
    private final Handler syncHandler = new Handler(Looper.getMainLooper());
    private Runnable syncRunnable;
    private int activeKunjunganId = -1;
    private int myQueueNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue_status);

        setupCommonUI(false);
        trackFeatureUsage("Antrean");

        tvMyQueue = findViewById(R.id.tvQueueNumber);
        tvCurrentQueue = findViewById(R.id.tvCurrentQueue);
        tvArrived = findViewById(R.id.tvQueueArrived);
        tvName = findViewById(R.id.tvQueuePatientName);
        tvDate = findViewById(R.id.tvQueueDate);
        btnFinish = findViewById(R.id.btnFinishQueue);

        if (btnFinish != null) {
            btnFinish.setOnClickListener(v -> {
                startActivity(new Intent(QueueStatusActivity.this, MedicalHistoryActivity.class));
                finish();
            });
        }

        activeKunjunganId = getIntent().getIntExtra("id_kunjungan", -1);
        if (activeKunjunganId == -1) {
            activeKunjunganId = sharedPrefs.getInt("current_id_kunjungan", -1);
        }

        fetchLatestQueueFromDB();
        startRealTimePolling();
    }

    private void fetchLatestQueueFromDB() {
        int idPasien = sharedPrefs.getInt("id_pasien", -1);
        if (idPasien == -1) return;

        ApiService api = RetrofitClient.getInstance().create(ApiService.class);
        api.getAntrean(idPasien).enqueue(new Callback<com.example.aplikasiwebmo.model.Appointment>() {
            @Override
            public void onResponse(Call<com.example.aplikasiwebmo.model.Appointment> call, Response<com.example.aplikasiwebmo.model.Appointment> response) {
                if (response.isSuccessful() && response.body() != null) {
                    com.example.aplikasiwebmo.model.Appointment app = response.body();
                    if (app.idKunjungan > 0 && !"Belum Checkin".equalsIgnoreCase(app.statusLayanan)) {
                        activeKunjunganId = app.idKunjungan;
                        myQueueNumber = app.nomorAntrean; 
                        
                        View content = findViewById(R.id.scrollQueueContent);
                        if (content != null) content.setVisibility(View.VISIBLE);
                        View empty = findViewById(R.id.llQueueEmptyState);
                        if (empty != null) empty.setVisibility(View.GONE);
                        
                        if (tvMyQueue != null) tvMyQueue.setText(String.valueOf(myQueueNumber));
                        if (tvName != null) tvName.setText(sharedPrefs.getString("user_name", "Pasien"));
                        if (tvDate != null) tvDate.setText(app.tanggalKunjungan);
                        
                        updateGlobalQueueUI(app.statusLayanan, app.antreanSekarang);
                    }
                }
            }
            @Override public void onFailure(Call<com.example.aplikasiwebmo.model.Appointment> call, Throwable t) {}
        });
    }

    private void startRealTimePolling() {
        syncRunnable = new Runnable() {
            @Override
            public void run() {
                if (activeKunjunganId == -1) {
                    syncHandler.postDelayed(this, 5000);
                    return;
                }

                ApiService api = RetrofitClient.getInstance().create(ApiService.class);
                api.getStatusSync(activeKunjunganId).enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            String status = response.body().statusLayanan;
                            int currentServingOrder = response.body().antreanSekarang;
                            updateGlobalQueueUI(status, currentServingOrder);
                            if ("Selesai".equalsIgnoreCase(status)) {
                                stopPolling();
                                sharedPrefs.edit().remove("current_id_kunjungan").apply();
                                startActivity(new Intent(QueueStatusActivity.this, MedicalHistoryActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                finish();
                            } else {
                                syncHandler.postDelayed(syncRunnable, 10000);
                            }
                        }
                    }
                    @Override public void onFailure(Call<LoginResponse> call, Throwable t) {
                        syncHandler.postDelayed(syncRunnable, 10000);
                    }
                });
            }
        };
        syncHandler.post(syncRunnable);
    }

    private void updateGlobalQueueUI(String myStatus, int currentServingOrder) {
        if (tvCurrentQueue != null) tvCurrentQueue.setText(String.valueOf(currentServingOrder));
        if (tvArrived != null) {
            if ("Selesai".equalsIgnoreCase(myStatus)) {
                tvArrived.setVisibility(View.VISIBLE);
                tvArrived.setText("kunjungan anda telah selesai");
                if (btnFinish != null) btnFinish.setVisibility(View.VISIBLE);
            } else if (myQueueNumber == currentServingOrder && currentServingOrder > 0) {
                tvArrived.setVisibility(View.VISIBLE);
                tvArrived.setText("giliran anda telah tiba");
                if (btnFinish != null) btnFinish.setVisibility(View.GONE);
            } else {
                tvArrived.setVisibility(View.GONE);
                if (btnFinish != null) btnFinish.setVisibility(View.GONE);
            }
        }
    }

    private void stopPolling() {
        if (syncRunnable != null) syncHandler.removeCallbacks(syncRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPolling();
    }
}