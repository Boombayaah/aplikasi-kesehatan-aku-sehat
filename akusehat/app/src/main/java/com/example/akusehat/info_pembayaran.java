package com.example.akusehat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;



public class info_pembayaran extends AppCompatActivity {

    TextView txtNik, txtPatientName, txtPaymentDate, txtPaymentMethod, txtTotalTagihan;


    String selectedMetode = "";
    String idPembayaran = "";

    ImageView imgPaymentMethod;
    LinearLayout btnUbahMetode, layoutOpsiMetode, btnKonfirmasi;
    LinearLayout cardCash, cardQris, cardBank, cardAsuransi;
    String metodeAwal;
    boolean metodeDiubah = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_pembayaran);

        txtNik = findViewById(R.id.txtNik);
        txtPatientName = findViewById(R.id.txtPatientName);
        txtPaymentDate = findViewById(R.id.txtPaymentDate);
        txtPaymentMethod = findViewById(R.id.txtPaymentMethod);
        imgPaymentMethod = findViewById(R.id.imgPaymentMethod);
        btnUbahMetode = findViewById(R.id.btnUbahMetode);
        layoutOpsiMetode = findViewById(R.id.layoutOpsiMetode);
        btnKonfirmasi = findViewById(R.id.btnKonfirmasi);

        btnKonfirmasi.setOnClickListener(v -> {

            if (!metodeDiubah) {
                Intent intent = new Intent(info_pembayaran.this, admin_staff.class);
                startActivity(intent);
                finish();
                return;
            }

            updateMetodePembayaran();
        });

        cardCash = findViewById(R.id.cardCash);
        cardQris = findViewById(R.id.cardQris);
        cardBank = findViewById(R.id.cardBank);
        cardAsuransi = findViewById(R.id.cardAsuransi);

        metodeAwal = getIntent().getStringExtra("metode_pembayaran");
        selectedMetode = metodeAwal;

        btnUbahMetode.setOnClickListener(v -> {

            if (layoutOpsiMetode.getVisibility() == View.VISIBLE) {
                layoutOpsiMetode.setVisibility(View.GONE);
            } else {
                layoutOpsiMetode.setVisibility(View.VISIBLE);
            }

        });

        cardCash.setOnClickListener(v -> {
            selectedMetode = "Tunai";
            metodeDiubah = true;

            txtPaymentMethod.setText(selectedMetode);

            resetCards();
            cardCash.setBackgroundResource(R.drawable.a_card_selected);
        });

        cardQris.setOnClickListener(v -> {
            selectedMetode = "QRIS";
            metodeDiubah = true;

            txtPaymentMethod.setText(selectedMetode);

            resetCards();
            cardQris.setBackgroundResource(R.drawable.a_card_selected);
        });

        cardBank.setOnClickListener(v -> {
            selectedMetode = "Transfer Bank";
            metodeDiubah = true;

            txtPaymentMethod.setText(selectedMetode);

            resetCards();
            cardBank.setBackgroundResource(R.drawable.a_card_selected);
        });

        cardAsuransi.setOnClickListener(v -> {
            selectedMetode = "Asuransi";
            metodeDiubah = true;

            txtPaymentMethod.setText(selectedMetode);

            resetCards();
            cardAsuransi.setBackgroundResource(R.drawable.a_card_selected);
        });



        idPembayaran = getIntent().getStringExtra("id_pembayaran");
        String nik = getIntent().getStringExtra("nik");
        String nama = getIntent().getStringExtra("nama");
        String tanggal = getIntent().getStringExtra("tanggal");
        String waktu = getIntent().getStringExtra("waktu");
        String metode = getIntent().getStringExtra("metode_pembayaran");
        String totalTagihan = getIntent().getStringExtra("total_tagihan");

        selectedMetode = metode;

        if (totalTagihan == null || totalTagihan.isEmpty()) {
            totalTagihan = "Rp " + getIntent().getStringExtra("jumlah");
        }

        txtTotalTagihan = findViewById(R.id.txtTotalTagihan);
        txtTotalTagihan.setText(totalTagihan);
        txtNik.setText(nik);
        txtPatientName.setText(nama);
        txtPaymentDate.setText(tanggal + ", " + waktu + " WIB");
        txtPaymentMethod.setText(metode);
        txtPaymentMethod.setText(metode);

        switch (metode) {

            case "Tunai":
                imgPaymentMethod.setImageResource(
                        R.drawable.b_ic_payment
                );
                break;

            case "QRIS":
                imgPaymentMethod.setImageResource(
                        R.drawable.b_ic_qris
                );
                break;

            case "Transfer Bank":
                imgPaymentMethod.setImageResource(
                        R.drawable.b_ic_bank
                );
                break;

            case "Asuransi":
                imgPaymentMethod.setImageResource(
                        R.drawable.b_ic_bank
                );
                break;
        }




    }

    private void resetCards() {
        cardCash.setBackgroundResource(R.drawable.a_card_outline);
        cardQris.setBackgroundResource(R.drawable.a_card_outline);
        cardBank.setBackgroundResource(R.drawable.a_card_outline);
        cardAsuransi.setBackgroundResource(R.drawable.a_card_outline);
    }

    private void updateMetodePembayaran() {
        String url = "http://192.168.1.8/aku_sehat/metode.php";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                response -> {
                    Toast.makeText(this, "Metode pembayaran diperbarui", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(info_pembayaran.this, admin_staff.class);
                    startActivity(intent);
                    finish();
                },
                error -> {
                    Toast.makeText(
                            info_pembayaran.this,
                            "gagal update metode pembayaran",
                            Toast.LENGTH_SHORT
                    ).show();

                    error.printStackTrace();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_pembayaran", idPembayaran);
                params.put("metode_pembayaran", selectedMetode);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}