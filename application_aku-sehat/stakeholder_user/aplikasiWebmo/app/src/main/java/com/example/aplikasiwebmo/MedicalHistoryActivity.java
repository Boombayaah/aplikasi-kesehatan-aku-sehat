package com.example.aplikasiwebmo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.aplikasiwebmo.model.MedicalRecord;
import com.example.aplikasiwebmo.network.ApiService;
import com.example.aplikasiwebmo.network.RetrofitClient;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MedicalHistoryActivity extends BaseActivity {
    private LinearLayout llHistoryContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_history);

        setupCommonUI(false);
        trackFeatureUsage("Riwayat");

        llHistoryContainer = findViewById(R.id.llHistoryContainer);
        
        loadHistory();
    }

    private void loadHistory() {
        int idPasien = sharedPrefs.getInt("id_pasien", -1);
        if (idPasien == -1) return;

        ApiService api = RetrofitClient.getInstance().create(ApiService.class);
        api.getRiwayatMedis(idPasien).enqueue(new Callback<List<MedicalRecord>>() {
            @Override
            public void onResponse(Call<List<MedicalRecord>> call, Response<List<MedicalRecord>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    displayHistory(response.body());
                }
            }
            @Override public void onFailure(Call<List<MedicalRecord>> call, Throwable t) {}
        });
    }

    private void displayHistory(List<MedicalRecord> list) {
        if (llHistoryContainer == null) return;
        llHistoryContainer.removeAllViews();
        
        for (MedicalRecord record : list) {
            View itemView = LayoutInflater.from(this).inflate(R.layout.activity_medical_history_item, llHistoryContainer, false);
            TextView tvDate = itemView.findViewById(R.id.tvHistoryDate);
            TextView tvDiagnosis = itemView.findViewById(R.id.tvHistoryDiagnosis);
            
            if (tvDate != null) tvDate.setText(record.tanggalKunjungan);
            if (tvDiagnosis != null) tvDiagnosis.setText(record.diagnosa);
            
            llHistoryContainer.addView(itemView);
        }
    }
}