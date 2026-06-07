package com.example.aplikasiwebmo.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("status")
    public String status;
    
    @SerializedName("message")
    public String message;
    
    @SerializedName("id_user")
    public int idUser;
    
    @SerializedName("id_pasien")
    public int idPasien;

    @SerializedName("id_dokter")
    public int idDokter;

    @SerializedName("spesialisasi")
    public int spesialisasi;

    @SerializedName("no_izin_praktik")
    public int noIzinPraktik;

    @SerializedName("id_kunjungan")
    public int idKunjungan;

    @SerializedName("nomor_antrean")
    public int nomorAntrean;

    @SerializedName("status_layanan")
    public String statusLayanan;

    @SerializedName("antrean_sekarang")
    public int antreanSekarang;
    
    @SerializedName("user")
    public User user;
    
    @SerializedName("pasien")
    public Pasien pasien;
}