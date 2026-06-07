package com.example.aplikasiwebmo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.aplikasiwebmo.model.Appointment;
import com.example.aplikasiwebmo.model.Billing;
import com.example.aplikasiwebmo.network.ApiService;
import com.example.aplikasiwebmo.network.RetrofitClient;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MedicalBillingActivity extends BaseActivity {
    private LinearLayout llServiceCosts, llMedicineCosts;
    private TextView tvBillingDate, tvBillingTime, tvBillingTotal, tvNoBillingText;
    private View scrollContent, llEmptyState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_billing);

        setupCommonUI(false);
        trackFeatureUsage("Tagihan");

        llServiceCosts = findViewById(R.id.llServiceCosts);
        llMedicineCosts = findViewById(R.id.llMedicineCosts);
        tvBillingDate = findViewById(R.id.tvBillingDate);
        tvBillingTime = findViewById(R.id.tvBillingTime);
        tvBillingTotal = findViewById(R.id.tvBillingTotal);
        tvNoBillingText = findViewById(R.id.tvNoBillingText);
        scrollContent = findViewById(R.id.scrollBillingContent);
        llEmptyState = findViewById(R.id.llBillingEmptyState);

        findViewById(R.id.btnBillingBack).setOnClickListener(v -> finish());

        loadLatestAppointment();
    }

    private void loadLatestAppointment() {
        int idPasien = sharedPrefs.getInt("id_pasien", -1);
        if (idPasien == -1) { showEmptyState("Sesi berakhir"); return; }

        ApiService api = RetrofitClient.getInstance().create(ApiService.class);
        api.getJadwal(idPasien).enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    Appointment latest = response.body().get(0);
                    fetchBillingDetails(latest.idKunjungan);
                } else { showEmptyState("Belum ada jadwal"); }
            }
            @Override public void onFailure(Call<List<Appointment>> call, Throwable t) { showEmptyState("Koneksi gagal"); }
        });
    }

    private void fetchBillingDetails(int idKunjungan) {
        ApiService api = RetrofitClient.getInstance().create(ApiService.class);
        api.getTagihan(idKunjungan).enqueue(new Callback<Billing>() {
            @Override
            public void onResponse(Call<Billing> call, Response<Billing> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Billing b = response.body();
                    if ("success".equals(b.status)) { showPriceMonitor(b); }
                    else if ("waiting".equals(b.status)) { showEmptyState(b.message); }
                    else { showEmptyState("Data tagihan tidak tersedia"); }
                }
            }
            @Override public void onFailure(Call<Billing> call, Throwable t) { showEmptyState("Server tidak merespon"); }
        });
    }

    private void showPriceMonitor(Billing b) {
        if (scrollContent != null) scrollContent.setVisibility(View.VISIBLE);
        if (llEmptyState != null) llEmptyState.setVisibility(View.GONE);
        
        if (tvBillingDate != null) tvBillingDate.setText(b.tanggalKunjungan != null ? b.tanggalKunjungan : "-");
        if (tvBillingTime != null) tvBillingTime.setText(b.waktuKunjungan != null ? b.waktuKunjungan : "-");
        if (tvBillingTotal != null) tvBillingTotal.setText(formatCurrency(b.totalTagihanAkhir));
        
        if (llServiceCosts != null) llServiceCosts.removeAllViews();
        if (llMedicineCosts != null) llMedicineCosts.removeAllViews();
        
        if (b.showConsultationFee && llServiceCosts != null) { 
            addCostItem(llServiceCosts, "Biaya Konsultasi Dokter", 1, 250000, 250000); 
        }
        
        if (b.rincianObat != null) {
            for (Billing.MedicationDetail item : b.rincianObat) {
                if (item.namaObat != null && !item.namaObat.toLowerCase().contains("konsultasi")) {
                    if (llMedicineCosts != null) {
                        addCostItem(llMedicineCosts, item.namaObat, item.jumlah, item.hargaSatuan, item.subtotal);
                    }
                }
            }
        }
    }

    private void addCostItem(LinearLayout parent, String name, int qty, double price, double subtotal) {
        View itemView = LayoutInflater.from(this).inflate(R.layout.activity_medical_billing_item, parent, false);
        TextView tvName = itemView.findViewById(R.id.tvBillingItemName);
        TextView tvDesc = itemView.findViewById(R.id.tvBillingItemDesc);
        TextView tvPrice = itemView.findViewById(R.id.tvBillingItemPrice);
        if (tvName != null) tvName.setText("- " + name);
        if (tvDesc != null) {
            if (qty > 1 || !name.toLowerCase().contains("konsultasi")) {
                tvDesc.setVisibility(View.VISIBLE);
                tvDesc.setText(String.format(Locale.getDefault(), "%dx @ %s", qty, formatCurrency(price)));
            } else { tvDesc.setVisibility(View.GONE); }
        }
        if (tvPrice != null) tvPrice.setText(formatCurrency(subtotal));
        parent.addView(itemView);
    }

    private void showEmptyState(String msg) {
        if (scrollContent != null) scrollContent.setVisibility(View.GONE);
        if (llEmptyState != null) llEmptyState.setVisibility(View.VISIBLE);
        if (tvNoBillingText != null) tvNoBillingText.setText(msg);
    }

    private String formatCurrency(double amount) {
        java.text.NumberFormat formatter = java.text.NumberFormat.getCurrencyInstance(new java.util.Locale("in", "ID"));
        return formatter.format(amount).replace("Rp", "Rp ");
    }
}