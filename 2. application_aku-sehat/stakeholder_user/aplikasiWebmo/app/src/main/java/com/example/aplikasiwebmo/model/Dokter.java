package com.example.aplikasiwebmo.model;

import com.google.gson.annotations.SerializedName;

public class Dokter {
    @SerializedName("id_dokter")
    public int idDokter;

    @SerializedName("id_user")
    public int idUser;

    @SerializedName("spesialisasi")
    public String spesialisasi;

    @SerializedName("no_izin_praktik")
    public String noIzinPraktik;
}