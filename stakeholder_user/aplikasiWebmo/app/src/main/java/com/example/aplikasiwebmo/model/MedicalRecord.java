package com.example.aplikasiwebmo.model;

import com.google.gson.annotations.SerializedName;

public class MedicalRecord {
    @SerializedName("keluhan")
    public String keluhan;
    
    @SerializedName("diagnosa")
    public String diagnosa;
    
    @SerializedName("nama_obat")
    public String namaObat;
    
    @SerializedName("jumlah")
    public String jumlah;
    
    @SerializedName("instruksi_konsumsi")
    public String instruksiKonsumsi;
    
    @SerializedName("tanggal_kunjungan")
    public String tanggalKunjungan;
    
    @SerializedName("nama_dokter")
    public String namaDokter;

    @SerializedName("status_layanan")
    public String statusLayanan;
}