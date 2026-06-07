package com.example.aplikasiwebmo.staff;

import com.example.aplikasiwebmo.LoginActivity;
import com.example.aplikasiwebmo.R;
import com.example.aplikasiwebmo.network.NetworkConfig;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.graphics.Color;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import android.app.AlertDialog;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONObject;

public class admin_leader extends AppCompatActivity {
    TextView txtTotalServed, txtOnlineQueue;
    TableLayout tblStaff;


    @Override
    protected void onResume() {
        super.onResume();

        loadDashboardCount();
        loadStaffTable();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_leader);

        txtTotalServed = findViewById(R.id.txtTotalServed);
        txtOnlineQueue = findViewById(R.id.txtQueue);

        TextView profileButton = findViewById(R.id.profileButton);
        TextView tanggal = findViewById(R.id.tanggal);


        tblStaff = findViewById(R.id.tblStaff);
        loadStaffTable();

        LinearLayout btnTambahStaff = findViewById(R.id.btnTambahStaff);

        LinearLayout navStaff = findViewById(R.id.navStaff);
        LinearLayout navAdmin = findViewById(R.id.navAlur);


        navAdmin.setOnClickListener(v -> {
            Intent intent = new Intent(admin_leader.this, manajemen_alur.class);
            startActivity(intent);
        });



//        navAdmin.setOnClickListener(v -> {
//            Intent intent = new Intent(admin_leader.this, manajemen_alur.class);
//
//            startActivity(intent);
//        });

        btnTambahStaff.setOnClickListener(v -> {
            Intent intent = new Intent(admin_leader.this, tambah_staff.class);

            startActivity(intent);
        });


        String currentDate = new SimpleDateFormat(
                "dd MMMM yyyy",
                new Locale("id", "ID")
        ).format(new Date());

        tanggal.setText(currentDate);

        SharedPreferences sp = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        boolean isLoggedIn = sp.getBoolean("isLoggedIn", false);
        String namaLengkap = sp.getString("user_name", "");
        String role = sp.getString("role", "");

        if (!isLoggedIn) {
            profileButton.setText("Login");

            profileButton.setOnClickListener(v -> {
                Intent intent = new Intent(admin_leader.this, LoginActivity.class);
                startActivity(intent);
            });

            return;
        }

        profileButton.setText(namaLengkap);

        profileButton.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(admin_leader.this, profileButton);
            popupMenu.getMenu().add("Keluar");

            popupMenu.setOnMenuItemClickListener(item -> {
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.apply();

                Intent intent = new Intent(admin_leader.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            });

            popupMenu.show();
        });

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

    private void loadStaffTable() {
        String url = NetworkConfig.BASE_URL + "tbl_staff.php";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {

                    while (tblStaff.getChildCount() > 1) {
                        tblStaff.removeViewAt(1);
                    }

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);

                            String idUser = obj.optString("id_user");
                            String nik = obj.optString("nik");
                            String nama = obj.optString("nama");
                            String jabatan = obj.optString("jabatan");

                            TableRow row = new TableRow(this);

                            row.addView(makeCell(String.valueOf(i + 1)));
                            row.addView(makeCell(nik));
                            row.addView(makeCell(nama));
                            row.addView(makeCell(jabatan));

                            LinearLayout operasiBox = new LinearLayout(this);
                            operasiBox.setOrientation(LinearLayout.HORIZONTAL);
                            operasiBox.setGravity(Gravity.CENTER);
                            operasiBox.setPadding(6, 6, 6, 6);

                            Button btnEdit = new Button(this);
                            btnEdit.setText("Edit");
                            btnEdit.setTextSize(10);

                            Button btnHapus = new Button(this);
                            btnHapus.setText("Hapus");
                            btnHapus.setTextSize(10);

                            operasiBox.addView(btnEdit);
                            operasiBox.addView(btnHapus);

                            btnEdit.setOnClickListener(v -> {

                                Intent intent =
                                        new Intent(admin_leader.this,
                                                tambah_staff.class);

                                intent.putExtra("mode", "edit");

                                intent.putExtra("id_user", idUser);
                                intent.putExtra("nik", nik);
                                intent.putExtra("nama", nama);
                                intent.putExtra("jabatan", jabatan);
                                intent.putExtra("email", obj.optString("email"));
                                intent.putExtra("no_hp", obj.optString("no_hp"));
                                intent.putExtra("foto_profil", obj.optString("foto_profil"));

                                startActivity(intent);
                            });

                            btnHapus.setOnClickListener(v -> {
                                showDeleteConfirmation(idUser, nama);
                            });

                            row.addView(operasiBox);

                            tblStaff.addView(row);

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

    private TextView makeCell(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(12);
        tv.setTextColor(Color.parseColor("#06161D"));
        tv.setPadding(10, 8, 10, 8);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        return tv;
    }

    private void hapusStaff(String idUser) {
        String url = NetworkConfig.BASE_URL + "hapus_staff.php";

        com.android.volley.toolbox.StringRequest request =
                new com.android.volley.toolbox.StringRequest(
                        Request.Method.POST,
                        url,
                        response -> {
                            loadStaffTable();
                        },
                        error -> {
                            error.printStackTrace();
                        }
                ) {
                    @Override
                    protected java.util.Map<String, String> getParams() {
                        java.util.Map<String, String> params = new java.util.HashMap<>();
                        params.put("id_user", idUser);
                        return params;
                    }
                };

        Volley.newRequestQueue(this).add(request);
    }

    private void showDeleteConfirmation(String idUser, String namaStaff) {
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi Hapus")
                .setMessage("Apakah kamu yakin ingin menghapus staff ini?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    showDeleteNameConfirmation(idUser, namaStaff);
                })
                .setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    private void showDeleteNameConfirmation(String idUser, String namaStaff) {
        EditText input = new EditText(this);
        input.setHint("Masukkan nama staff");
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setPadding(40, 20, 40, 20);

        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi Final")
                .setMessage("Ketik nama staff persis untuk menghapus:\n" + namaStaff)
                .setView(input)
                .setPositiveButton("Hapus", (dialog, which) -> {
                    String typedName = input.getText().toString();

                    if (typedName.equals(namaStaff)) {
                        hapusStaff(idUser);
                    } else {
                        Toast.makeText(
                                this,
                                "Nama tidak cocok. Data batal dihapus.",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                })
                .setNegativeButton("Batal", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }
}
