package com.example.aplikasiwebmo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;

import com.example.aplikasiwebmo.model.LoginResponse;
import com.example.aplikasiwebmo.network.ApiService;
import com.example.aplikasiwebmo.network.RetrofitClient;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleActivity extends BaseActivity {

    private LinearLayout llCalendarStrip;
    private View overlayPopup, containerSuccessPopup;
    private GridLayout glTimeSlots;
    private TextView tvMainTitle, tvMonthName1;
    private SeekBar sbSwipe;

    private boolean isTestingMode = false; 
    private Calendar currentCalendar, baseCalendar;
    private int selectedDayOffset = 0, selectedTimeSlotIdx = -1;

    private final String[] startTimes = {"09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00"};

    private String lastDate, lastTime;
    private int lastKunjunganId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        isTestingMode = sharedPrefs.getBoolean("isTestingMode", false);

        llCalendarStrip = findViewById(R.id.llCalendarStrip);
        glTimeSlots = findViewById(R.id.glTimeSlots);
        overlayPopup = findViewById(R.id.overlayPopup);
        containerSuccessPopup = findViewById(R.id.containerSuccessPopup);
        
        // Disable Dropdown Menu for this feature
        setupCommonUI(false);
        trackFeatureUsage("Buat Jadwal");

        tvMainTitle = findViewById(R.id.tvScheduleMainTitle);
        if (tvMainTitle != null) {
            tvMainTitle.setVisibility(View.VISIBLE);
            tvMainTitle.setText("Book Appointment");
            
            tvMainTitle.setOnLongClickListener(v -> {
                EditText input = new EditText(this);
                input.setHint("Keyword...");
                new AlertDialog.Builder(this)
                    .setTitle("Admin Settings")
                    .setMessage("Enter keyword to switch modes")
                    .setView(input)
                    .setPositiveButton("OK", (dialog, which) -> {
                        String keyword = input.getText().toString();
                        if ("NORMAL55".equals(keyword)) {
                            isTestingMode = false;
                            sharedPrefs.edit().putBoolean("isTestingMode", false).apply();
                            updateTimeSlotsUI();
                            Toast.makeText(this, "Mode Normal Aktif", Toast.LENGTH_SHORT).show();
                        } else if ("TEST55".equals(keyword)) {
                            isTestingMode = true;
                            sharedPrefs.edit().putBoolean("isTestingMode", true).apply();
                            updateTimeSlotsUI();
                            Toast.makeText(this, "Testing Mode Aktif", Toast.LENGTH_SHORT).show();
                        }
                    }).show();
                return true;
            });
        }

        tvMonthName1 = findViewById(R.id.tvMonthName1);
        ImageView btnPrevDay = findViewById(R.id.btnPrevDay);
        ImageView btnNextDay = findViewById(R.id.btnNextDay);
        sbSwipe = findViewById(R.id.sbSwipeToConfirm);

        baseCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+7"));
        currentCalendar = (Calendar) baseCalendar.clone();

        if (btnNextDay != null) btnNextDay.setOnClickListener(v -> { currentCalendar.add(Calendar.DAY_OF_YEAR, 1); renderCalendarStrip(); });
        if (btnPrevDay != null) btnPrevDay.setOnClickListener(v -> { if (currentCalendar.after(baseCalendar)) { currentCalendar.add(Calendar.DAY_OF_YEAR, -1); renderCalendarStrip(); } });

        setupSwipeButton();
        setupTimeSlots();
        renderCalendarStrip();
    }

    private void setupSwipeButton() {
        sbSwipe.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress > 95) {
                    String dateStr, timeStr;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    Calendar temp = (Calendar) currentCalendar.clone();
                    temp.add(Calendar.DAY_OF_YEAR, selectedDayOffset);
                    dateStr = sdf.format(temp.getTime());
                    timeStr = (selectedTimeSlotIdx != -1) ? startTimes[selectedTimeSlotIdx] + ":00" : "09:00:00";
                    performBooking(dateStr, timeStr, seekBar);
                }
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) { if (seekBar.getProgress() < 100) seekBar.setProgress(0); }
        });
    }

    private void performBooking(String dateStr, String timeStr, SeekBar seekBar) {
        int idUser = sharedPrefs.getInt("currentUserId", -1);
        if (idUser == -1) {
            Toast.makeText(this, "Sesi berakhir, silakan login ulang", Toast.LENGTH_SHORT).show();
            seekBar.setProgress(0);
            return;
        }

        ApiService api = RetrofitClient.getInstance().create(ApiService.class);
        api.submitSchedule(idUser, 0, dateStr, timeStr).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse res = response.body();
                    if ("success".equals(res.status)) {
                        int idKunjungan = res.idKunjungan;
                        sharedPrefs.edit().putInt("current_id_kunjungan", idKunjungan).apply();
                        
                        lastKunjunganId = idKunjungan;
                        lastDate = dateStr;
                        lastTime = timeStr;
                        
                        seekBar.setProgress(100);
                        seekBar.setEnabled(false);
                        showSuccessPopup();
                    } else {
                        seekBar.setProgress(0);
                        Toast.makeText(ScheduleActivity.this, "Gagal: " + res.message, Toast.LENGTH_LONG).show();
                    }
                } else {
                    seekBar.setProgress(0);
                    Toast.makeText(ScheduleActivity.this, "Server bermasalah. Cek XAMPP.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                seekBar.setProgress(0);
                Toast.makeText(ScheduleActivity.this, "Koneksi Gagal: Cek Firewall Laptop & WiFi", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showSuccessPopup() {
        overlayPopup.setVisibility(View.VISIBLE);
        ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1, android.view.animation.Animation.RELATIVE_TO_SELF, 0.5f, android.view.animation.Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(400);
        containerSuccessPopup.startAnimation(scale);
        
        findViewById(R.id.btnPopupBackHome).setOnClickListener(v -> {
            startActivity(new Intent(this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        });
        
        findViewById(R.id.btnPopupCheckin).setOnClickListener(v -> {
            Intent intent = new Intent(this, CheckinActivity.class);
            intent.putExtra("passed_id", lastKunjunganId);
            intent.putExtra("passed_date", lastDate);
            intent.putExtra("passed_time", lastTime);
            startActivity(intent);
            finish();
        });
    }

    private void setupTimeSlots() {
        if (glTimeSlots == null) return;
        for (int i = 0; i < glTimeSlots.getChildCount(); i++) {
            final int idx = i;
            glTimeSlots.getChildAt(i).setOnClickListener(v -> {
                if (!isTestingMode && isTodaySlotPassed(idx)) {
                    Toast.makeText(this, "Jam ini sudah terlewat", Toast.LENGTH_SHORT).show();
                    return;
                }
                selectedTimeSlotIdx = idx;
                updateTimeSlotsUI();
            });
        }
    }

    private boolean isTodaySlotPassed(int idx) {
        if (isTestingMode) return false;
        Calendar selectedDate = (Calendar) currentCalendar.clone();
        selectedDate.add(Calendar.DAY_OF_YEAR, selectedDayOffset);
        if (isToday(selectedDate)) {
            int slotHour = Integer.parseInt(startTimes[idx].split(":")[0]);
            int currentHour = Calendar.getInstance(TimeZone.getTimeZone("GMT+7")).get(Calendar.HOUR_OF_DAY);
            return slotHour < currentHour;
        }
        return false;
    }

    private boolean isToday(Calendar date) {
        Calendar today = Calendar.getInstance(TimeZone.getTimeZone("GMT+7"));
        return date.get(Calendar.YEAR) == today.get(Calendar.YEAR) && date.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR);
    }

    private void updateTimeSlotsUI() {
        if (glTimeSlots == null) return;
        for (int i = 0; i < glTimeSlots.getChildCount(); i++) {
            TextView tv = (TextView) glTimeSlots.getChildAt(i);
            boolean isPassed = !isTestingMode && isTodaySlotPassed(i);
            
            if (isPassed) {
                tv.setBackgroundResource(R.drawable.slot_bg_unselected);
                tv.setTextColor(getResources().getColor(android.R.color.darker_gray));
                tv.setAlpha(0.5f);
                tv.setClickable(false);
            } else {
                tv.setAlpha(1.0f);
                tv.setClickable(true);
                tv.setBackgroundResource(i == selectedTimeSlotIdx ? R.drawable.slot_bg_selected : R.drawable.slot_bg_unselected);
                tv.setTextColor(getResources().getColor(i == selectedTimeSlotIdx ? android.R.color.white : R.color.Black));
            }
        }
    }

    private void renderCalendarStrip() {
        if (llCalendarStrip == null) return;
        SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
        if (tvMonthName1 != null) tvMonthName1.setText(monthYearFormat.format(currentCalendar.getTime()));
        Calendar tempCal = (Calendar) currentCalendar.clone();
        SimpleDateFormat dayNameFormat = new SimpleDateFormat("EEE", Locale.ENGLISH);
        for (int i = 0; i < llCalendarStrip.getChildCount(); i++) {
            View card = llCalendarStrip.getChildAt(i);
            TextView tvDayName = card.findViewById(R.id.tvDayName);
            TextView tvDayNumber = card.findViewById(R.id.tvDayNumber);
            tvDayName.setText(dayNameFormat.format(tempCal.getTime()));
            tvDayNumber.setText(String.valueOf(tempCal.get(Calendar.DAY_OF_MONTH)));
            final int index = i;
            card.setBackgroundResource(i == selectedDayOffset ? R.drawable.slot_bg_selected : R.drawable.slot_bg_unselected);
            tvDayName.setTextColor(getResources().getColor(i == selectedDayOffset ? android.R.color.white : android.R.color.darker_gray));
            tvDayNumber.setTextColor(getResources().getColor(i == selectedDayOffset ? android.R.color.white : R.color.Black));
            card.setOnClickListener(v -> { selectedDayOffset = index; renderCalendarStrip(); updateTimeSlotsUI(); });
            tempCal.add(Calendar.DAY_OF_YEAR, 1);
        }
        updateTimeSlotsUI();
    }
}