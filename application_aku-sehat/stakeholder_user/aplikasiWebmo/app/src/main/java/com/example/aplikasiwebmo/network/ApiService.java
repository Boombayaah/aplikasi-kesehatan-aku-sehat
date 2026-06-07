package com.example.aplikasiwebmo.network;

import com.example.aplikasiwebmo.model.Appointment;
import com.example.aplikasiwebmo.model.Billing;
import com.example.aplikasiwebmo.model.LoginResponse;
import com.example.aplikasiwebmo.model.MedicalRecord;
import com.example.aplikasiwebmo.model.Pasien;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @FormUrlEncoded
    @POST("login.php")
    Call<LoginResponse> loginUser(
        @Field("no_hp") String phone,
        @Field("password") String password
    );

    @GET("get_jadwal.php")
    Call<List<Appointment>> getJadwal(@Query("id_pasien") int idPasien);

    @GET("get_jadwal.php")
    Call<List<Appointment>> getVisitDetails(@Query("id_kunjungan") int idKunjungan);

    @FormUrlEncoded
    @POST("submit_checkin.php")
    Call<LoginResponse> submitCheckin(
        @Field("id_kunjungan") int idKunjungan,
        @Field("dokumen_checkin") String documentTitle
    );

    @GET("get_antrean.php")
    Call<Appointment> getAntrean(@Query("id_pasien") int idPasien);

    @GET("get_riwayat_medis.php")
    Call<List<MedicalRecord>> getRiwayatMedis(@Query("id_pasien") int idPasien);

    @GET("get_tagihan.php")
    Call<Billing> getTagihan(@Query("id_kunjungan") int idKunjungan);

    @GET("get_status_sync.php")
    Call<LoginResponse> getStatusSync(@Query("id_kunjungan") int idKunjungan);

    @GET("get_pasien_profile.php")
    Call<LoginResponse> getPasienProfile(@Query("id_user") int idUser);

    @FormUrlEncoded
    @POST("register.php")
    Call<LoginResponse> registerUser(
        @Field("nama_lengkap") String nama,
        @Field("nik") String nik,
        @Field("no_hp") String phone,
        @Field("password") String pass,
        @Field("jenis_kelamin") String gender,
        @Field("alamat") String alamat
    );

    @FormUrlEncoded
    @POST("update_profile.php")
    Call<LoginResponse> updateProfile(
        @Field("id_user") int idUser,
        @Field("nama_lengkap") String nama,
        @Field("no_hp") String phone,
        @Field("alamat") String alamat,
        @Field("no_bpjs") String bpjs,
        @Field("tanggal_lahir") String dob
    );

    @FormUrlEncoded
    @POST("submit_schedule.php")
    Call<LoginResponse> submitSchedule(
        @Field("id_user") int idUser,
        @Field("id_dokter") int idDokter,
        @Field("tanggal_kunjungan") String date,
        @Field("waktu_kunjungan") String time
    );
}