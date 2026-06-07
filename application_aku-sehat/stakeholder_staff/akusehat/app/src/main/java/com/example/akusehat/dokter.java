package com.example.akusehat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.widget.PopupMenu;

public class dokter extends AppCompatActivity {

    TextView txtTotalDilayani, txtTotalAntrean;
    TableLayout tblAntrean;

    String BASE_URL = NetworkConfig.BASE_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dokter);

        txtTotalDilayani = findViewById(R.id.txtTotalDilayani);
        txtTotalAntrean = findViewById(R.id.txtTotalAntrean);
        tblAntrean = findViewById(R.id.tblAntrean);
        TextView profileButton = findViewById(R.id.profileButton);
        TextView tanggal = findViewById(R.id.tanggal);


        SharedPreferences sp = getSharedPreferences("USER_SESSION", MODE_PRIVATE);

        boolean isLoggedIn = sp.getBoolean("is_logged_in", false);
        String namaLengkap = sp.getString("nama_lengkap", "");
        String role = sp.getString("role", "");

        if (!isLoggedIn) {
            profileButton.setText("Login");

            txtTotalDilayani.setText("0 Orang");
            txtTotalAntrean.setText("0 Orang");

            profileButton.setOnClickListener(v -> {
                Intent intent = new Intent(dokter.this, login.class);
                startActivity(intent);
            });

            return;
        }

        profileButton.setText(namaLengkap);

        profileButton.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(dokter.this, profileButton);
            popupMenu.getMenu().add("Keluar");

            popupMenu.setOnMenuItemClickListener(item -> {
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.apply();

                Intent intent = new Intent(dokter.this, dokter.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            });

            popupMenu.show();
        });


        String currentDate = new SimpleDateFormat(
                "dd MMMM yyyy",
                new Locale("id", "ID")
        ).format(new Date());

        tanggal.setText(currentDate);

        loadDashboard();
        loadAntrean();
    }

    private void loadDashboard() {
        SharedPreferences sp = getSharedPreferences("USER_SESSION", MODE_PRIVATE);
        int idDokter = sp.getInt("id_dokter", -1);

        if (idDokter == -1) {
            txtTotalDilayani.setText("0 Orang");
            txtTotalAntrean.setText("0 Orang");
            return;
        }

        String url = BASE_URL + "dashboard_dokter.php?id_dokter=" + idDokter;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    txtTotalDilayani.setText(response.optString("total_dilayani", "0") + " Orang");
                    txtTotalAntrean.setText(response.optString("total_antrean", "0") + " Orang");
                },
                error -> {
                    txtTotalDilayani.setText("0 Orang");
                    txtTotalAntrean.setText("0 Orang");
                    Toast.makeText(this, "gagal load dashboard", Toast.LENGTH_SHORT).show();
                }
        );

        Volley.newRequestQueue(this).add(request);
    }

    private void loadAntrean() {

        SharedPreferences sp = getSharedPreferences("USER_SESSION", MODE_PRIVATE);
        int idDokter = sp.getInt("id_dokter", -1);

        if (idDokter == -1) {
            return;
        }

        String url = NetworkConfig.BASE_URL + "tbl_antrean_dokter.php?id_dokter=" + idDokter;

        JsonArrayRequest request =
                new JsonArrayRequest(
                        Request.Method.GET,
                        url,
                        null,

                        response -> {

                            for (int i = 0; i < response.length(); i++) {

                                try {
                                    JSONObject obj = response.getJSONObject(i);

                                    TableRow row = new TableRow(this);

                                    TextView noAntrean = new TextView(this);
                                    noAntrean.setText(obj.getString("nomor_antrean"));

                                    TextView keluhan = new TextView(this);
                                    keluhan.setText(obj.getString("keluhan"));

                                    TextView nik = new TextView(this);
                                    nik.setText(obj.getString("nik"));

                                    TextView nama = new TextView(this);
                                    nama.setText(obj.getString("nama"));

//                                    TextView detail = new TextView(this);
//                                    detail.setText("Detail");
//                                    detail.setPadding(8, 8, 8, 8);

                                    String nikValue = obj.getString("nik");
                                    String namaValue = obj.getString("nama");
                                    String keluhanValue = obj.getString("keluhan");
                                    String idKunjungan = obj.getString("id_kunjungan");

                                    Button detail = new Button(this);
                                    detail.setText("Detail");

                                    String metodeValue = obj.optString("metode_pembayaran", "").trim();

                                    detail.setOnClickListener(v -> {
                                        Intent intent = new Intent(
                                                dokter.this,
                                                riwayat_medis.class
                                        );

                                        intent.putExtra("nik", nikValue);
                                        intent.putExtra("nama", namaValue);
                                        intent.putExtra("keluhan", keluhanValue);
                                        intent.putExtra("id_kunjungan", idKunjungan);

                                        startActivity(intent);
                                    });

                                    row.addView(noAntrean);
                                    row.addView(keluhan);
                                    row.addView(nik);
                                    row.addView(nama);
                                    row.addView(detail);

                                    tblAntrean.addView(row);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        },

                        error -> {
                            Toast.makeText(this, "gagal load antrean", Toast.LENGTH_SHORT).show();
                            error.printStackTrace();
                        }
                );

        Volley.newRequestQueue(this).add(request);
    }
}