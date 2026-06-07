package com.example.aplikasiwebmo.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Billing {
    @SerializedName("status")
    public String status;

    @SerializedName("message")
    public String message;

    @SerializedName("id_kunjungan")
    public int idKunjungan;

    @SerializedName("tanggal_kunjungan")
    public String tanggalKunjungan;

    @SerializedName("waktu_kunjungan")
    public String waktuKunjungan;

    @SerializedName("total_tagihan_akhir")
    public double totalTagihanAkhir;

    @SerializedName("status_pembayaran")
    public String statusPembayaran;

    @SerializedName("metode_pembayaran")
    public String metodePembayaran;

    @SerializedName("show_consultation_fee")
    public boolean showConsultationFee;

    @SerializedName("rincian_obat")
    public List<MedicationDetail> rincianObat;

    public static class MedicationDetail {
        @SerializedName("nama_obat")
        public String namaObat;

        @SerializedName("jumlah")
        public int jumlah;

        @SerializedName("harga_satuan")
        public double hargaSatuan;

        @SerializedName("subtotal")
        public double subtotal;
    }
}