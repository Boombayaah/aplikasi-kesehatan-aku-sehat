package com.example.aplikasiwebmo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.io.File;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences sharedPrefs;
    private ImageView ivProfilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPrefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        ivProfilePic = findViewById(R.id.ivSettingsProfilePic);

        findViewById(R.id.btnSettingsBack).setOnClickListener(v -> finish());

        findViewById(R.id.btnResetProfilePic).setOnClickListener(v -> {
            String phone = sharedPrefs.getString("user_phone", "guest");
            sharedPrefs.edit().remove("profileImagePath_" + phone).apply();
            ivProfilePic.setImageResource(R.drawable.d_logo1);
            Toast.makeText(this, "Profile picture reset", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.btnLogoutBottom).setOnClickListener(v -> {
            sharedPrefs.edit().clear().apply(); // Clear all session data
            Toast.makeText(this, "Berhasil Keluar", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        findViewById(R.id.btnNavUserProfile).setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileActivity.class));
        });

        findViewById(R.id.btnNavChangePassword).setOnClickListener(v -> {
            showChangePasswordBottomSheet();
        });

        updateUserInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUserInfo();
    }

    private void updateUserInfo() {
        String name = sharedPrefs.getString("user_name", "Guest");
        TextView tvName = findViewById(R.id.tvSettingsUserName);
        if (tvName != null) tvName.setText(name);

        // Load profile picture
        String phone = sharedPrefs.getString("user_phone", "guest");
        String imagePath = sharedPrefs.getString("profileImagePath_" + phone, null);
        if (imagePath != null) {
            File file = new File(imagePath);
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                ivProfilePic.setImageBitmap(bitmap);
            } else {
                ivProfilePic.setImageResource(R.drawable.d_logo1);
            }
        } else {
            ivProfilePic.setImageResource(R.drawable.d_logo1);
        }
    }

    private void showChangePasswordBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.layout_bottom_sheet_change_password, null);
        bottomSheetDialog.setContentView(view);

        FrameLayout bottomSheet = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheet);
            behavior.setSkipCollapsed(false);
            behavior.setFitToContents(false);
            behavior.setHideable(true);
            
            ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            bottomSheet.setLayoutParams(layoutParams);

            behavior.setPeekHeight(getResources().getDisplayMetrics().heightPixels / 2);
            behavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        }

        EditText etNewPass = view.findViewById(R.id.etNewPassword);
        EditText etConfirmPass = view.findViewById(R.id.etConfirmNewPassword);
        Button btnSave = view.findViewById(R.id.btnSaveNewPassword);

        btnSave.setOnClickListener(v -> {
            String newPass = etNewPass.getText().toString();
            String confirmPass = etConfirmPass.getText().toString();

            if (newPass.isEmpty()) {
                Toast.makeText(this, "Password baru tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPass.equals(confirmPass)) {
                Toast.makeText(this, "Konfirmasi password tidak sesuai", Toast.LENGTH_SHORT).show();
                return;
            }

            String phone = sharedPrefs.getString("user_phone", "");
            if (!phone.isEmpty()) {
                Toast.makeText(this, "Fitur ubah password terhubung ke database", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
    }
}