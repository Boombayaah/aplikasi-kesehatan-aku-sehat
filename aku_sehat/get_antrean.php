<?php
include 'config.php';
header('Content-Type: application/json');

$id_pasien = (int)$_GET['id_pasien'];

// 1. Ambil data kunjungan terbaru user (ID Terbesar miliknya)
$sql = "SELECT id_kunjungan, id_dokter, tanggal_kunjungan, status_layanan 
        FROM kunjungan_layanan 
        WHERE id_pasien = '$id_pasien' AND status_layanan != 'Belum Checkin' 
        ORDER BY id_kunjungan DESC LIMIT 1";

$res = $conn->query($sql);
$data = $res->fetch_assoc();

if ($data) {
    $my_id = $data['id_kunjungan'];
    $doc   = $data['id_dokter'];
    $tgl   = $data['tanggal_kunjungan'];

    // 2. LOGIKA NOMOR ANTREAN USER: Hitung baris aktif dari ID 1 sampai ID User
    $sql_count_me = "SELECT COUNT(*) as urutan FROM kunjungan_layanan 
                     WHERE id_dokter = '$doc' AND tanggal_kunjungan = '$tgl' 
                     AND id_kunjungan <= '$my_id' AND status_layanan != 'Belum Checkin'";
    $data['nomor_antrean'] = (int)$conn->query($sql_count_me)->fetch_assoc()['urutan'];

    // 3. LOGIKA ANTREAN SEKARANG: Cari urutan baris dari ID terkecil yang masih 'Dalam Antrean'
    $sql_min_id = "SELECT MIN(id_kunjungan) as min_id FROM kunjungan_layanan 
                   WHERE id_dokter = '$doc' AND tanggal_kunjungan = '$tgl' 
                   AND status_layanan = 'Dalam Antrean'";
    $min_id_res = $conn->query($sql_min_id)->fetch_assoc()['min_id'];
    
    // Jika tidak ada yang 'Dalam Antrean', tunjukkan posisi user sendiri (sebagai penunjuk pemeriksaan sedang berlangsung)
    $target_id = $min_id_res ?? $my_id;

    $sql_count_now = "SELECT COUNT(*) as skrg FROM kunjungan_layanan 
                      WHERE id_dokter = '$doc' AND tanggal_kunjungan = '$tgl' 
                      AND id_kunjungan <= '$target_id' AND status_layanan != 'Belum Checkin'";
    $data['antrean_sekarang'] = (int)$conn->query($sql_count_now)->fetch_assoc()['skrg'];

    echo json_encode($data);
} else {
    echo json_encode(["id_kunjungan" => 0, "message" => "Tidak ada antrean aktif"]);
}
?>