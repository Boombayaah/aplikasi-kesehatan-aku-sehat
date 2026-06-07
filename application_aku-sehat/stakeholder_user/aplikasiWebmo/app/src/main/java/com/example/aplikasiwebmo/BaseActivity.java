package com.example.aplikasiwebmo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.List;

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
        }
    }
}