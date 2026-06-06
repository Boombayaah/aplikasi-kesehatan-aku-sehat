package com.example.akusehat;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class manajemen_alur extends AppCompatActivity {

    TextView txtTotalServed, txtOnlineQueue;
    TableLayout tblAlur;

    String BASE_URL = "http://192.168.1.8/aku_sehat/";

    @Override
    protected void onResume() {
        super.onResume();

        if (txtTotalServed != null && txtOnlineQueue != null && tblAlur != null) {
            loadDashboardCount();
            loadAlurTable();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manajemen_alur);

        txtTotalServed = findViewById(R.id.txtTotalServed);
        txtOnlineQueue = findViewById(R.id.txtQueue);
        tblAlur = findViewById(R.id.tblAlur);

        LinearLayout navStaff = findViewById(R.id.navStaff);
        LinearLayout navAdmin = findViewById(R.id.navAlur);
        LinearLayout btnTambahAlur = findViewById(R.id.btnTambahAlur);

        TextView tanggal = findViewById(R.id.tanggal);
        TextView profileButton = findViewById(R.id.profileButton);

        String currentDate = new SimpleDateFormat(
                "dd MMMM yyyy",
                new Locale("id", "ID")
        ).format(new Date());

        tanggal.setText(currentDate);

        SharedPreferences sp = getSharedPreferences("USER_SESSION", MODE_PRIVATE);

        boolean isLoggedIn = sp.getBoolean("is_logged_in", false);
        String namaLengkap = sp.getString("nama_lengkap", "");

        if (!isLoggedIn) {
            profileButton.setText("Login");

            txtTotalServed.setText("0 Orang");
            txtOnlineQueue.setText("0 Orang");

            profileButton.setOnClickListener(v -> {
                Intent intent = new Intent(manajemen_alur.this, login.class);
                startActivity(intent);
            });

            return;
        }

        profileButton.setText(namaLengkap);

        profileButton.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(manajemen_alur.this, profileButton);
            popupMenu.getMenu().add("Keluar");

            popupMenu.setOnMenuItemClickListener(item -> {
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.apply();

                Intent intent = new Intent(manajemen_alur.this, login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            });

            popupMenu.show();
        });

        btnTambahAlur.setOnClickListener(v -> {
            Intent intent = new Intent(manajemen_alur.this, form_gambar.class);
            intent.putExtra("mode", "tambah");
            startActivity(intent);
        });

        navStaff.setOnClickListener(v -> {
            Intent intent = new Intent(manajemen_alur.this, admin_leader.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        navAdmin.setOnClickListener(v -> {
            // sudah di halaman manajemen alur
        });

        loadDashboardCount();
        loadAlurTable();
    }

    private void loadDashboardCount() {
        String url = BASE_URL + "dashboard_count.php";

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

    private void loadAlurTable() {
        String url = BASE_URL + "get_alur.php";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    clearTableRows();

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);

                            String idAlur = obj.optString("id_alur", "");
                            String judul = obj.optString("nama_tahapan", "-");
                            String deskripsi = obj.optString("deskripsi", "-");
                            String gambar = obj.optString("dokumen", "-");

                            TableRow row = new TableRow(this);

                            row.addView(makeCell(judul));
                            row.addView(makeCell(deskripsi));

                            TextView txtGambar = makeClickableCell(gambar);

                            txtGambar.setOnClickListener(v -> {
                                Intent intent = new Intent(manajemen_alur.this, lihat_gambar.class);
                                intent.putExtra("gambar_url", buildImageUrl(gambar));
                                startActivity(intent);
                            });

                            row.addView(txtGambar);

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
                                Intent intent = new Intent(
                                        manajemen_alur.this,
                                        form_gambar.class
                                );

                                intent.putExtra("mode", "edit");
                                intent.putExtra("id_alur", idAlur);
                                intent.putExtra("nama_tahapan", judul);
                                intent.putExtra("deskripsi", deskripsi);
                                intent.putExtra("dokumen", gambar);

                                startActivity(intent);
                            });

                            btnHapus.setOnClickListener(v -> {
                                showDeleteConfirmation(idAlur, judul);
                            });

                            row.addView(operasiBox);
                            tblAlur.addView(row);

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

    private void hapusAlur(String idAlur) {
        String url = BASE_URL + "hapus_alur.php";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                response -> {
                    String res = response.trim();

                    Toast.makeText(this, res, Toast.LENGTH_SHORT).show();

                    if (res.equalsIgnoreCase("success")) {
                        loadAlurTable();
                    }
                },
                error -> {
                    Toast.makeText(this, "Gagal hapus alur", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_alur", idAlur);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    private void showDeleteConfirmation(String idAlur, String judulAlur) {
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi Hapus")
                .setMessage("Apakah kamu yakin ingin menghapus alur ini?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    showDeleteNameConfirmation(idAlur, judulAlur);
                })
                .setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    private void showDeleteNameConfirmation(String idAlur, String judulAlur) {
        EditText input = new EditText(this);
        input.setHint("Masukkan judul alur");
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setPadding(40, 20, 40, 20);

        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi Final")
                .setMessage("Ketik judul alur persis untuk menghapus:\n" + judulAlur)
                .setView(input)
                .setPositiveButton("Hapus", (dialog, which) -> {
                    String typedText = input.getText().toString();

                    if (typedText.equals(judulAlur)) {
                        hapusAlur(idAlur);
                    } else {
                        Toast.makeText(
                                this,
                                "Judul tidak cocok. Data batal dihapus.",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                })
                .setNegativeButton("Batal", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    private void clearTableRows() {
        while (tblAlur.getChildCount() > 1) {
            tblAlur.removeViewAt(1);
        }
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

    private TextView makeClickableCell(String text) {
        TextView tv = new TextView(this);

        tv.setText("📷 " + getFileNameOnly(text));
        tv.setTextSize(12);
        tv.setTextColor(getResources().getColor(R.color.tertiary));
        tv.setPadding(10, 8, 10, 8);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setPaintFlags(
                tv.getPaintFlags()
                        | android.graphics.Paint.UNDERLINE_TEXT_FLAG
        );

        return tv;
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

    private String getFileNameOnly(String path) {
        if (path == null || path.isEmpty()) {
            return "-";
        }

        int slashIndex = path.lastIndexOf("/");

        if (slashIndex >= 0 && slashIndex < path.length() - 1) {
            return path.substring(slashIndex + 1);
        }

        return path;
    }
}