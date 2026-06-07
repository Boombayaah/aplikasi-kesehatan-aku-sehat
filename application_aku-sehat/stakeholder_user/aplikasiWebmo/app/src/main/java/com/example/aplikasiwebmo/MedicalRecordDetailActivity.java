package com.example.aplikasiwebmo;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MedicalRecordDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_record_detail);

        findViewById(R.id.btnDetailBack).setOnClickListener(v -> finish());

        // Get data from Intent
        String date = getIntent().getStringExtra("date");
        String doctor = getIntent().getStringExtra("doctor");
        String diagnosis = getIntent().getStringExtra("diagnosis");
        String complaint = getIntent().getStringExtra("complaint");
        String action = getIntent().getStringExtra("action");
        String medName = getIntent().getStringExtra("medName");
        String medQty = getIntent().getStringExtra("medQty");
        String medInst = getIntent().getStringExtra("medInst");

        // Bind data to views
        if (date != null) ((TextView) findViewById(R.id.tvDetailDate)).setText(date);
        if (doctor != null) {
            ((TextView) findViewById(R.id.tvDetailDoctor)).setText(doctor);
            ((TextView) findViewById(R.id.tvExpandDoctor)).setText(doctor);
        }
        if (diagnosis != null) {
            ((TextView) findViewById(R.id.tvDetailDiagnosis)).setText(diagnosis);
            ((TextView) findViewById(R.id.tvExpandDiagnosis)).setText(diagnosis);
        }
        
        if (complaint != null) ((TextView) findViewById(R.id.tvExpandComplaint)).setText(complaint);
        if (action != null) ((TextView) findViewById(R.id.tvExpandAction)).setText(action);
        
        if (medName != null) ((TextView) findViewById(R.id.tvExpandMedicineName)).setText(medName);
        if (medQty != null) ((TextView) findViewById(R.id.tvExpandMedicineQty)).setText("Jumlah: " + medQty);
        if (medInst != null) ((TextView) findViewById(R.id.tvExpandMedicineInst)).setText("Instruksi: " + medInst);

        // Setup expandable card
        setupExpandableSection(R.id.cardDiagnosisExpandable, R.id.contentDiagnosis, R.id.arrowDiagnosis);
    }

    private void setupExpandableSection(int headerId, int contentId, int arrowId) {
        View header = findViewById(headerId);
        final View content = findViewById(contentId);
        final ImageView arrow = findViewById(arrowId);

        if (header != null && content != null && arrow != null) {
            header.setOnClickListener(v -> {
                if (content.getVisibility() == View.GONE) {
                    content.setVisibility(View.VISIBLE);
                    arrow.setRotation(180);
                } else {
                    content.setVisibility(View.GONE);
                    arrow.setRotation(0);
                }
            });
        }
    }
}