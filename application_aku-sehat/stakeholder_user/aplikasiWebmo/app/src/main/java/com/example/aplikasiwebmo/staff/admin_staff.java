package com.example.aplikasiwebmo.staff;

import com.example.aplikasiwebmo.R;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.android.volley.toolbox.JsonArrayRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.widget.PopupMenu;

public class admin_staff extends AppCompatActivity {

    TableLayout tblPembayaran;
    TableLayout tblAntrean;
    TextView txtTotalServed, txtOnlineQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_staff);

        txtTotalServed = findViewById(R.id.txtTotalServed);
        txtOnlineQueue = findViewById(R.id.txtQueue);
        tblPembayaran = findViewById(R.id.tblPembayaran);
        tblAntrean = findViewById(R.id.tblAntrean);
        TextView profileButton = findViewById(R.id.profileButton);
        TextView tanggal = findViewById(R.id.tanggal);


        String currentDate = new SimpleDateFormat(
                "dd MMMM yyyy",
                new Locale("id", "ID")
        ).format(new Date());

        tanggal.setText(currentDate);

        SharedPreferences sp = getSharedPreferences("USER_SESSION", MODE_PRIVATE);

        boolean isLoggedIn = sp.getBoolean("is_logged_in", false);
        String namaLengkap = sp.getString("nama_lengkap", "");
        String role = sp.getString("role", "");

        if (!isLoggedIn) {
            profileButton.setText("Login");

            txtTotalServed.setText("0 Orang");
            txtOnlineQueue.setText("0 Orang");

            profileButton.setOnClickListener(v -> {
                Intent intent = new Intent(admin_staff.this, login.class);
                startActivity(intent);
            });

            return;
        }

        profileButton.setText(namaLengkap);

        profileButton.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(admin_staff.this, profileButton);
            popupMenu.getMenu().add("Keluar");

            popupMenu.setOnMenuItemClickListener(item -> {
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.apply();

                Intent intent = new Intent(admin_staff.this, admin_staff.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            });

            popupMenu.show();
        });

        loadPaymentTable();
        loadQueueTable();
        loadDashboardCount();
    }



    private void loadDashboardCount() {
        String url = NetworkConfig.BASE_URL + "dashboard_count.php";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    int totalSelesai = response.optInt("total_selesai", 0);
                    int totalAntrean = response.optInt("total_antrean", 0);

                    txtTotalServed.setText(totalSelesai + " Orang");
                    txtOnlineQueue.setText(totalAntrean + " Orang");
                },
                error -> {
                    txtTotalServed.setText("Error");
                    txtOnlineQueue.setText("Error");
                    error.printStackTrace();
                }
        );

        queue.add(request);
    }



    private void loadPaymentTable() {
        String url = NetworkConfig.BASE_URL + "tbl_pembayaran.php";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {

                    for (int i = 0; i < response.length(); i++) {

                        try {
                            JSONObject obj = response.getJSONObject(i);

                            String nikValue = obj.getString("nik");
                            String namaValue = obj.getString("nama");
                            String jumlahValue = obj.getString("jumlah");
                            String tanggalValue = obj.optString("tanggal_kunjungan", "-");
                            String waktuValue = obj.optString("waktu_kunjungan", "-");

                            String idPembayaranValue = obj.getString("id_pembayaran");

                            TableRow row = new TableRow(this);

                            TextView nik = new TextView(this);
                            nik.setText(nikValue);

                            TextView nama = new TextView(this);
                            nama.setText(namaValue);

                            TextView jumlah = new TextView(this);
                            jumlah.setText("Rp " + jumlahValue);

                            Button detail = new Button(this);
                            detail.setText("Detail");

                            String metodeValue = obj.optString("metode_pembayaran", "").trim();

                            detail.setOnClickListener(v -> {
                                Intent intent;


                                if (metodeValue.length() == 0 || metodeValue.equalsIgnoreCase("null")) {
                                    intent = new Intent(admin_staff.this, pembayaran.class);
                                } else {
                                    intent = new Intent(admin_staff.this, info_pembayaran.class);
                                }

                                intent.putExtra("nik", nikValue);
                                intent.putExtra("nama", namaValue);
                                intent.putExtra("jumlah", jumlahValue);
                                intent.putExtra("total_tagihan", "Rp" + jumlahValue);
                                intent.putExtra("tanggal", tanggalValue);
                                intent.putExtra("waktu", waktuValue);
                                intent.putExtra("id_pembayaran", idPembayaranValue);
                                intent.putExtra("metode_pembayaran", metodeValue);

                                startActivity(intent);
                            });

                            row.addView(nik);
                            row.addView(nama);
                            row.addView(jumlah);
                            row.addView(detail);

                            tblPembayaran.addView(row);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                error -> {
                    error.printStackTrace();
                }
        );

        queue.add(request);
    }

    private void loadQueueTable() {

        String url =
                NetworkConfig.BASE_URL + "tbl_antrean.php";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request =
                new JsonArrayRequest(
                        Request.Method.GET,
                        url,
                        null,

                        response -> {

                            for(int i = 0; i < response.length(); i++) {

                                try {

                                    JSONObject obj =
                                            response.getJSONObject(i);

                                    TableRow row =
                                            new TableRow(this);

                                    TextView no_urut =
                                            new TextView(this);
                                    no_urut.setText(
                                            obj.getString("no_urut")
                                    );

                                    TextView waktu_checkin =
                                            new TextView(this);
                                    waktu_checkin.setText(
                                            obj.getString("waktu_checkin")
                                    );

                                    TextView nik =
                                            new TextView(this);
                                    nik.setText(
                                            obj.getString("nik")
                                    );

                                    TextView nama =
                                            new TextView(this);
                                    nama.setText(
                                            obj.getString("nama")
                                    );

                                    row.addView(no_urut);
                                    row.addView(waktu_checkin);
                                    row.addView(nik);
                                    row.addView(nama);

                                    tblAntrean.addView(row);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        },

                        error -> {
                            error.printStackTrace();
                        }
                );

        queue.add(request);
    }
}
