package com.example.aplikasiwebmo.staff;

import com.example.aplikasiwebmo.LoginActivity;
import com.example.aplikasiwebmo.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.widget.Spinner;
import android.widget.CheckBox;
import android.widget.ArrayAdapter;

import org.json.JSONObject;




public class diagnosa extends AppCompatActivity {

    public class Obat {

        String idObat;
        String namaObat;

        public Obat(String idObat, String namaObat) {
            this.idObat = idObat;
            this.namaObat = namaObat;
        }

        @Override
        public String toString() {
            return namaObat;
        }
    }

    TextView txtNik, txtPatientName, txtKeluhan;
    EditText inputDiagnosa;

    TextView btnOperasi, btnObat, btnCekLab, btnImunisasi, btnRawat;
    LinearLayout btnSelesaiKonsultasi;

    ArrayList<String> selectedTindakan = new ArrayList<>();

    Spinner spinnerObat1, spinnerObat2, spinnerObat3;
    EditText inputJumlah1, inputJumlah2, inputJumlah3;
    CheckBox checkResep1, checkResep2, checkResep3;

    ArrayList<Obat> listObat = new ArrayList<>();

    String BASE_URL = NetworkConfig.BASE_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diagnosa);

        txtNik = findViewById(R.id.txtNik);
        txtPatientName = findViewById(R.id.txtPatientName);
        txtKeluhan = findViewById(R.id.txtKeluhan);
        inputDiagnosa = findViewById(R.id.inputDiagnosa);

        btnOperasi = findViewById(R.id.btnOperasi);
        btnObat = findViewById(R.id.btnObat);
        btnCekLab = findViewById(R.id.btnCekLab);
        btnImunisasi = findViewById(R.id.btnImunisasi);
        btnRawat = findViewById(R.id.btnRawat);
        btnSelesaiKonsultasi = findViewById(R.id.btnSelesaiKonsultasi);

        TextView profileButton = findViewById(R.id.profileButton);

        spinnerObat1 = findViewById(R.id.spinnerObat1);
        spinnerObat2 = findViewById(R.id.spinnerObat2);
        spinnerObat3 = findViewById(R.id.spinnerObat3);

        inputJumlah1 = findViewById(R.id.inputJumlah1);
        inputJumlah2 = findViewById(R.id.inputJumlah2);
        inputJumlah3 = findViewById(R.id.inputJumlah3);

        checkResep1 = findViewById(R.id.checkResep1);
        checkResep2 = findViewById(R.id.checkResep2);
        checkResep3 = findViewById(R.id.checkResep3);

        SharedPreferences sp = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        boolean isLoggedIn = sp.getBoolean("isLoggedIn", false);
        String namaLengkap = sp.getString("user_name", "");
        String role = sp.getString("role", "");

        if (!isLoggedIn) {
            profileButton.setText("Login");

            profileButton.setOnClickListener(v -> {
                Intent intent = new Intent(diagnosa.this, LoginActivity.class);
                startActivity(intent);
            });

            return;
        }

        profileButton.setText(namaLengkap);

        profileButton.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(diagnosa.this, profileButton);
            popupMenu.getMenu().add("Keluar");

            popupMenu.setOnMenuItemClickListener(item -> {
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.apply();

                Intent intent = new Intent(diagnosa.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            });

            popupMenu.show();
        });

        loadObatDropdown();

        String idKunjungan = getIntent().getStringExtra("id_kunjungan");
        String nik = getIntent().getStringExtra("nik");
        String nama = getIntent().getStringExtra("nama");
        String keluhan = getIntent().getStringExtra("keluhan");

        txtNik.setText(nik);
        txtPatientName.setText(nama);
        txtKeluhan.setText(keluhan);

        inputDiagnosa.setText(keluhan);

        setupTindakanButton(btnOperasi, "Operasi");
        setupTindakanButton(btnObat, "Obat");
        setupTindakanButton(btnCekLab, "Cek Laboratorium");
        setupTindakanButton(btnImunisasi, "Imunisasi");
        setupTindakanButton(btnRawat, "Rawat Inap");

        btnSelesaiKonsultasi.setOnClickListener(v -> {
            String diagnosa = inputDiagnosa.getText().toString().trim();
            String tindakan = String.join(", ", selectedTindakan);

            simpanCatatanDokter(idKunjungan, diagnosa, tindakan);
        });
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }

    private void resetPillPadding(TextView button) {
        button.setPadding(dp(12), dp(6), dp(12), dp(6));
    }

    private void setupTindakanButton(TextView button, String value) {
        button.setOnClickListener(v -> {
            if (selectedTindakan.contains(value)) {
                selectedTindakan.remove(value);

                button.setBackgroundResource(R.drawable.a_card_outline);
                button.setTextColor(getResources().getColor(R.color.secondary));
            } else {
                selectedTindakan.add(value);

                button.setBackgroundResource(R.drawable.a_pill_selected);
                button.setTextColor(getResources().getColor(R.color.secondary));
            }

            resetPillPadding(button);
        });
    }

    private void simpanCatatanDokter(String idKunjungan, String diagnosa, String tindakan) {
        String url = BASE_URL + "update_catatan_dokter.php";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                response -> {
                    Toast.makeText(this, "Konsultasi selesai", Toast.LENGTH_SHORT).show();
                    finish();
                },
                error -> {
                    Toast.makeText(this, "Gagal simpan catatan dokter", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("id_kunjungan", idKunjungan);
                params.put("diagnosa", diagnosa);
                params.put("tindakan", tindakan);

                Obat obat1 = (Obat) spinnerObat1.getSelectedItem();
                Obat obat2 = (Obat) spinnerObat2.getSelectedItem();
                Obat obat3 = (Obat) spinnerObat3.getSelectedItem();

                String jumlah1 = inputJumlah1.getText().toString().trim();
                String jumlah2 = inputJumlah2.getText().toString().trim();
                String jumlah3 = inputJumlah3.getText().toString().trim();

                if (obat1 != null && !obat1.idObat.isEmpty() && !jumlah1.isEmpty()) {
                    params.put("id_obat1", obat1.idObat);
                    params.put("jumlah1", jumlah1);
                    params.put("resep1", checkResep1.isChecked() ? "Dengan resep dokter" : "Obat umum");
                }

                if (obat2 != null && !obat2.idObat.isEmpty() && !jumlah2.isEmpty()) {
                    params.put("id_obat2", obat2.idObat);
                    params.put("jumlah2", jumlah2);
                    params.put("resep2", checkResep2.isChecked() ? "Dengan resep dokter" : "Obat umum");
                }

                if (obat3 != null && !obat3.idObat.isEmpty() && !jumlah3.isEmpty()) {
                    params.put("id_obat3", obat3.idObat);
                    params.put("jumlah3", jumlah3);
                    params.put("resep3", checkResep3.isChecked() ? "Dengan resep dokter" : "Obat umum");
                }

                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
    private void loadObatDropdown() {
        String url = BASE_URL + "get_obat.php";

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    listObat.clear();

                    listObat.add(new Obat("", "-- Pilih Obat --"));

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject obj = response.optJSONObject(i);

                        String idObat = obj.optString("id_obat");
                        String namaObat = obj.optString("nama_obat");

                        listObat.add(new Obat(idObat, namaObat));
                    }

                    ArrayAdapter<Obat> adapter = new ArrayAdapter<>(
                            this,
                            android.R.layout.simple_spinner_item,
                            listObat
                    );

                    adapter.setDropDownViewResource(
                            android.R.layout.simple_spinner_dropdown_item
                    );

                    spinnerObat1.setAdapter(adapter);
                    spinnerObat2.setAdapter(adapter);
                    spinnerObat3.setAdapter(adapter);
                },
                error -> {
                    Toast.makeText(this, "gagal load obat", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
        );

        Volley.newRequestQueue(this).add(request);
    }
}
