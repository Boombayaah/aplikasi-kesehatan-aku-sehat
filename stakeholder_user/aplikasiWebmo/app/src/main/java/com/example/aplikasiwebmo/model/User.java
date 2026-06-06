package com.example.aplikasiwebmo.model;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id_user")
    public int idUser;
    
    @SerializedName("nama_lengkap")
    public String namaLengkap;
    
    @SerializedName("nomor_induk_kependudukan")
    public String nik;
    
    @SerializedName("alamat")
    public String alamat;
    
    @SerializedName("no_hp")
    public String noHp;
    
    @SerializedName("tanggal_lahir")
    public String tanggalLahir;
}