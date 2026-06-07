<?php
// submit_checkin.php - PURE STATUS UPDATE VERSION
include 'config.php';
header('Content-Type: application/json');

$id_kunjungan = $_POST['id_kunjungan'];
$judul_dokumen = $_POST['dokumen_checkin']; 

// 1. Ambil info dokter dan tanggal pendaftaran untuk nomor antrean
$sql_info = "SELECT id_dokter, tanggal_kunjungan FROM kunjungan_layanan WHERE id_kunjungan = '$id_kunjungan'";
$res_info = $conn->query($sql_info);
$visit = $res_info->fetch_assoc();
$id_doc = $visit['id_dokter'];
$tgl = $visit['tanggal_kunjungan'];

// 2. LOGIKA FIFO: Hitung nomor antrean urutan berikutnya
$res_count = $conn->query("SELECT MAX(nomor_antrean) as terakhir FROM kunjungan_layanan 
                           WHERE id_dokter = '$id_doc' AND tanggal_kunjungan = '$tgl' 
                           AND status_layanan != 'Belum CheckIn'");
$row_count = $res_count->fetch_assoc();
$nomor_baru = ($row_count['terakhir'] ?? 0) + 1;

// 3. UPDATE: HANYA MENGUBAH STATUS LAYANAN SAJA
$sql_update = "UPDATE kunjungan_layanan 
               SET status_layanan = 'Dalam Antrean', 
                   nomor_antrean = '$nomor_baru', 
                   waktu_checkin = NOW(), 
                   dokumen_checkin = '$judul_dokumen' 
               WHERE id_kunjungan = '$id_kunjungan'";

if ($conn->query($sql_update) === TRUE) {
    echo json_encode([
        "status" => "success",
        "message" => "Status Berhasil Diperbarui Menjadi Dalam Antrean",
        "nomor_antrean" => (int)$nomor_baru
    ]);
} else {
    echo json_encode(["status" => "error", "message" => $conn->error]);
}
?>