package com.example.aplikasiwebmo.staff;

import com.example.aplikasiwebmo.HomeActivity;
import com.example.aplikasiwebmo.R;
import com.example.aplikasiwebmo.network.NetworkConfig;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.android.volley.toolbox.StringRequest;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import android.widget.Toast;

import android.content.Intent;

public class pembayaran extends AppCompatActivity {

    TextView txtNik, txtPatientName, txtPaymentDate;
    TextView txtDetailObat, txtHargaObat, txtTotalTagihan;

    LinearLayout cardCash, cardQris, cardBank, cardAsuransi;
    String selectedMethod = "";
    LinearLayout btnKonfirmasi;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pembayaran);

        txtNik = findViewById(R.id.txtNik);
        txtPatientName = findViewById(R.id.txtPatientName);
        txtPaymentDate = findViewById(R.id.txtPaymentDate);

        txtDetailObat = findViewById(R.id.detailObat);
        txtHargaObat = findViewById(R.id.hargaObat);
        txtTotalTagihan = findViewById(R.id.totalTagihan);

        cardCash = findViewById(R.id.cardCash);
        cardQris = findViewById(R.id.cardQris);
        cardBank = findViewById(R.id.cardBank);
        cardAsuransi = findViewById(R.id.cardAsuransi);

        btnKonfirmasi = findViewById(R.id.btnKonfirmasi);

        TextView profileButton = findViewById(R.id.profileButton);
        SharedPreferences sp = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        boolean isLoggedIn = sp.getBoolean("isLoggedIn", false);
        String namaLengkap = sp.getString("user_name", "");
        String role = sp.getString("role", "");

        if (!isLoggedIn) {
            profileButton.setText("Login");


            profileButton.setOnClickListener(v -> {
                Intent intent = new Intent(pembayaran.this, HomeActivity.class);
                startActivity(intent);
            });

            return;
        }

        profileButton.setText(namaLengkap);

        profileButton.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(pembayaran.this, profileButton);
            popupMenu.getMenu().add("Keluar");

            popupMenu.setOnMenuItemClickListener(item -> {
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.apply();

                Intent intent = new Intent(pembayaran.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            });

            popupMenu.show();
        });

        btnKonfirmasi.setOnClickListener(v -> {

            if (selectedMethod == null || selectedMethod.isEmpty()) {
                Toast.makeText(this, "Pilih metode pembayaran dulu", Toast.LENGTH_SHORT).show();
                return; // tetap di halaman pembayaran
            }

            updateMetodePembayaran();

            Intent intent = new Intent(pembayaran.this, info_pembayaran.class);

            intent.putExtra("nik", getIntent().getStringExtra("nik"));
            intent.putExtra("nama", getIntent().getStringExtra("nama"));
            intent.putExtra("jumlah", getIntent().getStringExtra("jumlah"));
            intent.putExtra("tanggal", getIntent().getStringExtra("tanggal"));
            intent.putExtra("waktu", getIntent().getStringExtra("waktu"));
            intent.putExtra("id_pembayaran", getIntent().getStringExtra("id_pembayaran"));
            intent.putExtra("total_tagihan", txtTotalTagihan.getText().toString());

            intent.putExtra("metode_pembayaran", selectedMethod);

            startActivity(intent);
        });

        cardCash.setOnClickListener(v -> {

            selectedMethod = "Tunai";

            resetCards();

            cardCash.setBackgroundResource(
                    R.drawable.a_card_selected
            );
        });

        cardQris.setOnClickListener(v -> {

            selectedMethod = "QRIS";

            resetCards();

            cardQris.setBackgroundResource(
                    R.drawable.a_card_selected
            );
        });

        cardBank.setOnClickListener(v -> {

            selectedMethod = "Transfer Bank";

            resetCards();

            cardBank.setBackgroundResource(
                    R.drawable.a_card_selected
            );
        });

        cardAsuransi.setOnClickListener(v -> {

            selectedMethod = "Asuransi";

            resetCards();

            cardAsuransi.setBackgroundResource(
                    R.drawable.a_card_selected
            );
        });

        String nik = getIntent().getStringExtra("nik");
        String nama = getIntent().getStringExtra("nama");
        String tanggal = getIntent().getStringExtra("tanggal");
        String waktu = getIntent().getStringExtra("waktu");

        txtNik.setText(nik);
        txtPatientName.setText(nama);
        txtPaymentDate.setText(tanggal + ", " + waktu + " WIB");

        tagihan();
    }

    private void resetCards() {

        cardCash.setBackgroundResource(
                R.drawable.a_card_outline
        );

        cardQris.setBackgroundResource(
                R.drawable.a_card_outline
        );

        cardBank.setBackgroundResource(
                R.drawable.a_card_outline
        );

        cardAsuransi.setBackgroundResource(
                R.drawable.a_card_outline
        );
    }

    private void tagihan() {
        String idPembayaran = getIntent().getStringExtra("id_pembayaran");

        String url = NetworkConfig.BASE_URL + "obat.php?id_pembayaran=" + idPembayaran;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    String namaObat = response.optString("nama_obat", "-");
                    int jumlahObat = response.optInt("jumlah", 0);
                    int hargaObat = response.optInt("harga", 0);
                    int subtotalObat = response.optInt("subtotal_obat", 0);
                    int biayaKonsultasi = 250000;
                    int totalBiaya = biayaKonsultasi + subtotalObat;

                    String jumlah = getIntent().getStringExtra("jumlah");
                    String totalTagihan = getIntent().getStringExtra("total_tagihan");

                    NumberFormat format =
                            NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

                    format.setMinimumFractionDigits(2);
                    format.setMaximumFractionDigits(2);

                    txtTotalTagihan.setText(format.format(totalBiaya));
                    txtDetailObat.setText(
                            namaObat + "\n" +
                                    jumlahObat + "pc @ Rp" + hargaObat
                    );

                    txtHargaObat.setText("Rp" + subtotalObat);
                },
                error -> {
                    error.printStackTrace();
                    txtDetailObat.setText("Gagal memuat obat");
                    txtHargaObat.setText("-");
                }
        );

        queue.add(request);
    }

    private void updateMetodePembayaran() {
        String idPembayaran = getIntent().getStringExtra("id_pembayaran");

        String url = NetworkConfig.BASE_URL + "metode.php";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                response -> {
                    Toast.makeText(this, "Pembayaran dikonfirmasi", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    Toast.makeText(this, "Gagal update pembayaran", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_pembayaran", idPembayaran);
                params.put("metode_pembayaran", selectedMethod);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
}
