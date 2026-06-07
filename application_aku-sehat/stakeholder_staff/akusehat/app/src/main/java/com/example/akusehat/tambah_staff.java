package com.example.akusehat;

import android.content.Intent;
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

public class tambah_staff extends AppCompatActivity {

    EditText inputNik, inputNama, inputJabatan, inputEmail, inputNoTelp;
    TextView btnUploadProfil, btnSimpan, btnBatal;
    ImageView imgProfile;

    String mode = "tambah";
    String idUser = "";
    String oldFotoProfil = "";

    Uri selectedImageUri = null;

    String BASE_URL = NetworkConfig.BASE_URL;

    ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            selectedImageUri = result.getData().getData();
                            imgProfile.setImageURI(selectedImageUri);
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambah_staff);

        inputNik = findViewById(R.id.inputNik);
        inputNama = findViewById(R.id.inputNama);
        inputJabatan = findViewById(R.id.inputJabatan);
        inputEmail = findViewById(R.id.inputEmail);
        inputNoTelp = findViewById(R.id.inputNoTelp);

        imgProfile = findViewById(R.id.imgProfile);
        btnUploadProfil = findViewById(R.id.btnUploadProfil);
        btnSimpan = findViewById(R.id.btnSimpan);
        btnBatal = findViewById(R.id.btnBatal);

        mode = getIntent().getStringExtra("mode");

        if (mode == null) {
            mode = "tambah";
        }

        if (mode.equals("edit")) {
            loadIntentDataForEdit();
        }

        btnUploadProfil.setOnClickListener(v -> openFileManager());

        btnBatal.setOnClickListener(v -> finish());

        btnSimpan.setOnClickListener(v -> {
            String nik = inputNik.getText().toString().trim();
            String nama = inputNama.getText().toString().trim();
            String jabatan = inputJabatan.getText().toString().trim();
            String email = inputEmail.getText().toString().trim();
            String noHp = inputNoTelp.getText().toString().trim();

            if (nik.isEmpty() || nama.isEmpty() || jabatan.isEmpty()
                    || email.isEmpty() || noHp.isEmpty()) {
                Toast.makeText(this, "Data wajib diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            if (mode.equals("edit")) {
                if (selectedImageUri != null) {
                    uploadFotoLaluUpdateStaff(nik, nama, jabatan, email, noHp);
                } else {
                    updateStaff(nik, nama, jabatan, email, noHp, oldFotoProfil);
                }
            } else {
                if (selectedImageUri != null) {
                    uploadFotoLaluSimpanStaff(nik, nama, jabatan, email, noHp);
                } else {
                    simpanStaff(nik, nama, jabatan, email, noHp, "");
                }
            }
        });
    }

    private void loadIntentDataForEdit() {
        idUser = getIntent().getStringExtra("id_user");

        inputNik.setText(getIntent().getStringExtra("nik"));
        inputNama.setText(getIntent().getStringExtra("nama"));
        inputJabatan.setText(getIntent().getStringExtra("jabatan"));
        inputEmail.setText(getIntent().getStringExtra("email"));
        inputNoTelp.setText(getIntent().getStringExtra("no_hp"));

        oldFotoProfil = getIntent().getStringExtra("foto_profil");

        if (oldFotoProfil != null && !oldFotoProfil.isEmpty()) {
            String imageUrl = BASE_URL + oldFotoProfil;

            Glide.with(this)
                    .load(imageUrl)
                    .into(imgProfile);
        } else {
            oldFotoProfil = "";
        }
    }

    private void openFileManager() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        imagePickerLauncher.launch(Intent.createChooser(intent, "Pilih Foto Profil"));
    }

    private void uploadFotoLaluSimpanStaff(
            String nik,
            String nama,
            String jabatan,
            String email,
            String noHp
    ) {
        uploadFoto(
                fotoPath -> simpanStaff(nik, nama, jabatan, email, noHp, fotoPath)
        );
    }

    private void uploadFotoLaluUpdateStaff(
            String nik,
            String nama,
            String jabatan,
            String email,
            String noHp
    ) {
        uploadFoto(
                fotoPath -> updateStaff(nik, nama, jabatan, email, noHp, fotoPath)
        );
    }

    private interface UploadCallback {
        void onUploaded(String fotoPath);
    }

    private void uploadFoto(UploadCallback callback) {
        String url = BASE_URL + "upload_profile.php";

        VolleyMultipartRequest request = new VolleyMultipartRequest(
                Request.Method.POST,
                url,
                response -> {
                    String result = new String(response.data).trim();

                    if (!result.equals("failed") && !result.equals("no_file")) {
                        callback.onUploaded(result);
                    } else {
                        Toast.makeText(this, "Upload foto gagal: " + result, Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(this, "Upload foto error", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
        ) {
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();

                try {
                    byte[] imageData = getBytesFromUri(selectedImageUri);
                    String fileName = getFileName(selectedImageUri);

                    params.put("foto", new DataPart(fileName, imageData, "image/jpeg"));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    private void simpanStaff(
            String nik,
            String nama,
            String jabatan,
            String email,
            String noHp,
            String fotoProfil
    ) {
        String url = BASE_URL + "tambah_staff.php";

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

                params.put("nik", nik);
                params.put("nama", nama);
                params.put("jabatan", jabatan);
                params.put("email", email);
                params.put("no_hp", noHp);
                params.put("foto_profil", fotoProfil);

                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    private void updateStaff(
            String nik,
            String nama,
            String jabatan,
            String email,
            String noHp,
            String fotoProfil
    ) {
        String url = BASE_URL + "update_staff.php";

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

                params.put("id_user", idUser);
                params.put("nik", nik);
                params.put("nama", nama);
                params.put("jabatan", jabatan);
                params.put("email", email);
                params.put("no_hp", noHp);
                params.put("foto_profil", fotoProfil);

                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
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
        String result = "profile.jpg";

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