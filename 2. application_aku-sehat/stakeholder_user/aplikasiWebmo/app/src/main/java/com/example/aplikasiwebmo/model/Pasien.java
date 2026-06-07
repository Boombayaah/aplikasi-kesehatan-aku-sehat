package com.example.aplikasiwebmo.model;

import com.google.gson.annotations.SerializedName;

public class Pasien {
    @SerializedName("id_pasien")
    public int idPasien;
    
    @SerializedName("id_user")
    public int idUser;
    
    @SerializedName("no_bpjs_asuransi")
    public String noBpjs;
    
    @SerializedName("jenis_kelamin")
    public String jenisKelamin;
}