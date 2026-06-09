package com.example.aplikasiwebmo;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class KlinikFlowActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_klinik_flow);

        setupCommonUI(false);
        trackFeatureUsage("Alur");

        setupExpandableSection(R.id.headerSchedule, R.id.contentSchedule, R.id.arrowSchedule);
        setupExpandableSection(R.id.headerConsultation, R.id.contentConsultation, R.id.arrowConsultation);
        setupExpandableSection(R.id.headerInsurance, R.id.contentInsurance, R.id.arrowInsurance);
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