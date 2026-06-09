package com.example.aplikasiwebmo.staff;

import com.example.aplikasiwebmo.R;
import com.example.aplikasiwebmo.network.NetworkConfig;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class form_gambar extends AppCompatActivity {

    EditText inputJudulAlur, inputDeskripsi;
    ImageView imgAlur;
    TextView btnUbahGambar, btnBatal, btnSimpan, profileButton;

    Uri selectedImageUri = null;

    String mode = "tambah";
    String idAlur = "";
    String oldDokumen = "";

    String BASE_URL = NetworkConfig.BASE_URL;

    ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            selectedImageUri = result.getData().getData();
                            imgAlur.setImageURI(selectedImageUri);
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_gambar);

        inputJudulAlur = findViewById(R.id.inputJudulAlur);
        inputDeskripsi = findViewById(R.id.inputDeskripsi);
        imgAlur = findViewById(R.id.imgAlur);
        btnUbahGambar = findViewById(R.id.btnUbahGambar);
        btnBatal = findViewById(R.id.btnBatal);
        btnSimpan = findViewById(R.id.btnSimpan);
        profileButton = findViewById(R.id.profileButton);

        SharedPreferences sp = getSharedPreferences("USER_SESSION", MODE_PRIVATE);
        profileButton.setText(sp.getString("nama_lengkap", "ADMIN_LEADER_DUMMY"));

        mode = getIntent().getStringExtra("mode");

        if (mode == null) {
            mode = "tambah";
        }

        if (mode.equals("edit")) {
            loadIntentDataForEdit();
        }

        btnUbahGambar.setOnClickListener(v -> openFileManager());

        btnBatal.setOnClickListener(v -> finish());

        btnSimpan.setOnClickListener(v -> {
            String judul = inputJudulAlur.getText().toString().trim();
            String deskripsi = inputDeskripsi.getText().toString().trim();

            if (judul.isEmpty() || deskripsi.isEmpty()) {
                Toast.makeText(this, "Judul dan deskripsi wajib diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            if (mode.equals("edit")) {
                if (selectedImageUri != null) {
                    uploadGambarLaluUpdateAlur(judul, deskripsi);
                } else {
                    updateAlur(judul, deskripsi, oldDokumen);
                }
            } else {
                if (selectedImageUri == null) {
                    Toast.makeText(this, "Gambar wajib dipilih", Toast.LENGTH_SHORT).show();
                    return;
                }

                uploadGambarLaluSimpanAlur(judul, deskripsi);
            }
        });
    }

    private void loadIntentDataForEdit() {
        idAlur = getIntent().getStringExtra("id_alur");

        String judul = getIntent().getStringExtra("nama_tahapan");
        String deskripsi = getIntent().getStringExtra("deskripsi");
        oldDokumen = getIntent().getStringExtra("dokumen");

        inputJudulAlur.setText(judul);
        inputDeskripsi.setText(deskripsi);

        if (oldDokumen != null && !oldDokumen.isEmpty()) {
            Glide.with(this)
                    .load(buildImageUrl(oldDokumen))
                    .into(imgAlur);
        } else {
            oldDokumen = "";
        }
    }

    private void openFileManager() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        imagePickerLauncher.launch(Intent.createChooser(intent, "Pilih Gambar Alur"));
    }

    private void uploadGambarLaluSimpanAlur(String judul, String deskripsi) {
        uploadGambar(gambarPath -> simpanAlur(judul, deskripsi, gambarPath));
    }

    private void uploadGambarLaluUpdateAlur(String judul, String deskripsi) {
        uploadGambar(gambarPath -> updateAlur(judul, deskripsi, gambarPath));
    }

    private interface UploadCallback {
        void onUploaded(String gambarPath);
    }

    private void uploadGambar(UploadCallback callback) {
        String url = BASE_URL + "upload_alur_gambar.php";

        VolleyMultipartRequest request = new VolleyMultipartRequest(
                Request.Method.POST,
                url,
                response -> {
                    String result = new String(response.data).trim();

                    if (!result.equals("failed") && !result.equals("no_file")) {
                        callback.onUploaded(result);
                    } else {
                        Toast.makeText(this, "Upload gambar gagal: " + result, Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(this, "Upload gambar error", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
        ) {
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();

                try {
                    byte[] imageData = getBytesFromUri(selectedImageUri);
                    String fileName = "alur_" + System.currentTimeMillis() + "_" + getFileName(selectedImageUri);

                    params.put("gambar", new DataPart(fileName, imageData, "image/jpeg"));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    private void simpanAlur(String judul, String deskripsi, String gambarPath) {
        String url = BASE_URL + "tambah_alur.php";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                response -> {
                    String res = response.trim();

                    Toast.makeText(this, res, Toast.LENGTH_LONG).show();

                    if (res.equalsIgnoreCase("success")) {
                        finish();
                    }
                },
                error -> {
                    Toast.makeText(this, "Koneksi gagal", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("nama_tahapan", judul);
                params.put("deskripsi", deskripsi);
                params.put("dokumen", gambarPath);

                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    private void updateAlur(String judul, String deskripsi, String gambarPath) {
        String url = BASE_URL + "update_alur.php";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                response -> {
                    String res = response.trim();

                    Toast.makeText(this, res, Toast.LENGTH_LONG).show();

                    if (res.equalsIgnoreCase("success")) {
                        finish();
                    }
                },
                error -> {
                    Toast.makeText(this, "Update gagal", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("id_alur", idAlur);
                params.put("nama_tahapan", judul);
                params.put("deskripsi", deskripsi);
                params.put("dokumen", gambarPath);

                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    private String buildImageUrl(String gambar) {
        if (gambar == null || gambar.isEmpty()) {
            return "";
        }

        if (gambar.startsWith("uploads/")) {
            return BASE_URL + gambar;
        }

        return BASE_URL + "uploads/alur/" + gambar;
    }

    private byte[] getBytesFromUri(Uri uri) throws Exception {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        inputStream.close();

        return byteBuffer.toByteArray();
    }

    private String getFileName(Uri uri) {
        String result = "alur.jpg";

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);

            if (nameIndex >= 0) {
                result = cursor.getString(nameIndex);
            }

            cursor.close();
        }

        return result;
    }
}
