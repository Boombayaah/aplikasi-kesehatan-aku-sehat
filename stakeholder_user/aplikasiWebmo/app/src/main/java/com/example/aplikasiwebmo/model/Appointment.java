package com.example.aplikasiwebmo.model;

import com.google.gson.annotations.SerializedName;

public class Appointment {
    @SerializedName("id_kunjungan")
    public int idKunjungan;
    
    @SerializedName("id_pasien")
    public int idPasien;
    
    @SerializedName("id_dokter")
    public int idDokter;
    
    @SerializedName("tanggal_kunjungan")
    public String tanggalKunjungan;
    
    @SerializedName("waktu_kunjungan")
    public String waktuKunjungan;
    
    @SerializedName("nomor_antrean")
    public int nomorAntrean;

    @SerializedName("antrean_sekarang")
    public int antreanSekarang;
    
    @SerializedName("status_layanan")
    public String statusLayanan;
    
    @SerializedName("nama_dokter")
    public String namaDokter;
    
    @SerializedName("spesialisasi")
    public String spesialisasi;
}