package com.example.aplikasiwebmo;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.aplikasiwebmo.model.LoginResponse;
import com.example.aplikasiwebmo.network.ApiService;
import com.example.aplikasiwebmo.network.RetrofitClient;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private ImageView ivProfilePic;
    private EditText etName, etMobile, etBpjs, etAddress;
    private TextView tvNik, tvBirthDate;
    private SharedPreferences sharedPrefs;
    private String selectedDOB = "";
    private static final int CAMERA_PERMISSION_CODE = 101;

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    if (imageUri != null) saveImageToInternalStorage(imageUri);
                }
            }
    );

    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bitmap imageBitmap = (Bitmap) result.getData().getExtras().get("data");
                    saveBitmapToInternalStorage(imageBitmap);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sharedPrefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        ivProfilePic = findViewById(R.id.ivProfileUserPic);
        etName = findViewById(R.id.etProfileFirstName);
        tvNik = findViewById(R.id.tvProfileNIK);
        tvBirthDate = findViewById(R.id.tvProfileBirthDate);
        etMobile = findViewById(R.id.etProfileMobile);
        etBpjs = findViewById(R.id.etProfileBpjs);
        etAddress = findViewById(R.id.etProfileAddress);

        loadProfile();

        findViewById(R.id.btnProfileBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnProfileChangePic).setOnClickListener(v -> showImageSourceDialog());
        ivProfilePic.setOnClickListener(v -> showImageSourceDialog());
        findViewById(R.id.btnSelectBirthDate).setOnClickListener(v -> showDatePicker());
        findViewById(R.id.btnProfileSave).setOnClickListener(v -> saveProfile());
        
        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            sharedPrefs.edit().putBoolean("isLoggedIn", false).apply();
            Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void showDatePicker() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog picker = new DatePickerDialog(this, (view, y, m, d) -> {
            selectedDOB = String.format(Locale.getDefault(), "%04d-%02d-%02d", y, m + 1, d);
            tvBirthDate.setText(selectedDOB);
        }, year, month, day);
        picker.show();
    }

    private void showImageSourceDialog() {
        String[] options = {"Take Photo", "Choose from Gallery"};
        new AlertDialog.Builder(this)
                .setTitle("Select Image Source")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) checkCameraPermission();
                    else imagePickerLauncher.launch(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
                }).show();
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else openCamera();
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try { cameraLauncher.launch(takePictureIntent); } catch (Exception e) {}
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) openCamera();
    }

    private void saveImageToInternalStorage(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            saveBitmapToInternalStorage(bitmap);
        } catch (Exception e) {}
    }

    private void saveBitmapToInternalStorage(Bitmap bitmap) {
        try {
            String phone = sharedPrefs.getString("user_phone", "guest");
            File file = new File(getFilesDir(), "profile_pic_" + phone + ".jpg");
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.close();
            ivProfilePic.setImageBitmap(bitmap);
            sharedPrefs.edit().putString("profileImagePath_" + phone, file.getAbsolutePath()).apply();
        } catch (Exception e) {}
    }

    private void loadProfile() {
        etName.setText(sharedPrefs.getString("user_name", ""));
        tvNik.setText(sharedPrefs.getString("user_nik", "-"));
        
        selectedDOB = sharedPrefs.getString("user_dob", "");
        if (!selectedDOB.isEmpty()) {
            tvBirthDate.setText(selectedDOB);
        } else {
            tvBirthDate.setText("Pilih Tanggal Lahir");
        }
        
        etMobile.setText(sharedPrefs.getString("user_phone", ""));
        etBpjs.setText(sharedPrefs.getString("user_bpjs", ""));
        etAddress.setText(sharedPrefs.getString("user_address", ""));

        refreshProfileFromDB();

        String phone = sharedPrefs.getString("user_phone", "guest");
        String imagePath = sharedPrefs.getString("profileImagePath_" + phone, null);
        if (imagePath != null) {
            File file = new File(imagePath);
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                ivProfilePic.setImageBitmap(bitmap);
            }
        }
    }

    private void refreshProfileFromDB() {
        int idUser = sharedPrefs.getInt("currentUserId", -1);
        if (idUser == -1) return;

        ApiService api = RetrofitClient.getInstance().create(ApiService.class);
        api.getPasienProfile(idUser).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse res = response.body();
                    if (res.user != null) {
                        etName.setText(res.user.namaLengkap);
                        tvNik.setText(res.user.nik);
                        etMobile.setText(res.user.noHp);
                        etAddress.setText(res.user.alamat);
                        
                        selectedDOB = res.user.tanggalLahir;
                        if (selectedDOB != null && !selectedDOB.isEmpty()) {
                            tvBirthDate.setText(selectedDOB);
                        }

                        SharedPreferences.Editor editor = sharedPrefs.edit();
                        editor.putString("user_name", res.user.namaLengkap);
                        editor.putString("user_nik", res.user.nik);
                        editor.putString("user_phone", res.user.noHp);
                        editor.putString("user_address", res.user.alamat);
                        editor.putString("user_dob", res.user.tanggalLahir);
                        editor.apply();
                    }
                    if (res.pasien != null) {
                        etBpjs.setText(res.pasien.noBpjs);
                        sharedPrefs.edit().putString("user_bpjs", res.pasien.noBpjs).apply();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {}
        });
    }

    private void saveProfile() {
        int idUser = sharedPrefs.getInt("currentUserId", -1);
        if (idUser == -1) return;

        String name = etName.getText().toString().trim();
        String bpjs = etBpjs.getText().toString().trim();
        String mobile = etMobile.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String dob = selectedDOB;

        ApiService api = RetrofitClient.getInstance().create(ApiService.class);
        api.updateProfile(idUser, name, mobile, address, bpjs, dob).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if ("success".equals(response.body().status)) {
                        SharedPreferences.Editor editor = sharedPrefs.edit();
                        editor.putString("user_name", name);
                        editor.putString("user_phone", mobile);
                        editor.putString("user_bpjs", bpjs);
                        editor.putString("user_address", address);
                        editor.putString("user_dob", dob);
                        editor.apply();
                        
                        Toast.makeText(ProfileActivity.this, "Profile Saved to Database", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Update failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}