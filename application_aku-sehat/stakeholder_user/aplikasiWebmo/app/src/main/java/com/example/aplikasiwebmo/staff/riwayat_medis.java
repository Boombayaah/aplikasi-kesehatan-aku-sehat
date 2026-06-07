package com.example.aplikasiwebmo.staff;

import com.example.aplikasiwebmo.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class riwayat_medis extends AppCompatActivity {

    TextView txtNik, txtPatientName, txtKeluhan;
    TableLayout tblRiwayat;
    LinearLayout btnDiagnosa;

    String BASE_URL = NetworkConfig.BASE_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.riwayat_medis);

        txtNik = findViewById(R.id.txtNik);
        txtPatientName = findViewById(R.id.txtPatientName);
        txtKeluhan = findViewById(R.id.txtKeluhan);
        tblRiwayat = findViewById(R.id.tblRiwayat);
        btnDiagnosa = findViewById(R.id.btnDiagnosa);

        TextView profileButton = findViewById(R.id.profileButton);

        String nik = getIntent().getStringExtra("nik");
        String nama = getIntent().getStringExtra("nama");
        String keluhan = getIntent().getStringExtra("keluhan");
        String idKunjungan = getIntent().getStringExtra("id_kunjungan");

        btnDiagnosa.setOnClickListener(v -> {

            Intent intent = new Intent(
                    riwayat_medis.this,
                    diagnosa.class
            );

            intent.putExtra("nik", nik);
            intent.putExtra("nama", nama);
            intent.putExtra("keluhan", keluhan);
            intent.putExtra("id_kunjungan", idKunjungan);

            startActivity(intent);
        });

        txtNik.setText(nik);
        txtPatientName.setText(nama);
        txtKeluhan.setText(keluhan);

        SharedPreferences sp = getSharedPreferences("USER_SESSION", MODE_PRIVATE);

        boolean isLoggedIn = sp.getBoolean("is_logged_in", false);
        String namaLengkap = sp.getString("nama_lengkap", "");

        if (!isLoggedIn) {
            profileButton.setText("Login");

            profileButton.setOnClickListener(v -> {
                Intent intent = new Intent(riwayat_medis.this, login.class);
                startActivity(intent);
            });

            return;
        }

        profileButton.setText(namaLengkap);

        profileButton.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(riwayat_medis.this, profileButton);
            popupMenu.getMenu().add("Keluar");

            popupMenu.setOnMenuItemClickListener(item -> {
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.apply();

                Intent intent = new Intent(
                        riwayat_medis.this,
                        riwayat_medis.class
                );

                intent.setFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK
                );

                startActivity(intent);
                return true;
            });

            popupMenu.show();
        });

        loadRiwayat(nik);
    }

    private TextView makeCell(String text) {
        TextView cell = new TextView(this);

        cell.setText(text);
        cell.setTextSize(12);
        cell.setPadding(12, 8, 12, 8);

        return cell;
    }

    private void loadRiwayat(String nik) {
        String url = BASE_URL + "riwayat_medis.php?nik=" + nik;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {

                    for (int i = 0; i < response.length(); i++) {

                        try {
                            JSONObject obj = response.getJSONObject(i);

                            TableRow row = new TableRow(this);

                            row.addView(makeCell(obj.optString("tanggal", "-")));
                            row.addView(makeCell(obj.optString("keluhan", "-")));
                            row.addView(makeCell(obj.optString("diagnosa", "-")));
                            row.addView(makeCell(obj.optString("tindakan", "-")));

                            tblRiwayat.addView(row);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                error -> {
                    Toast.makeText(this, "gagal load riwayat medis", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
        );

        queue.add(request);
    }

}
