<?php
include 'config.php';
header('Content-Type: application/json');
$id_k = $_GET['id_kunjungan'];

$sql = "SELECT status_layanan, nomor_antrean, id_dokter, tanggal_kunjungan FROM kunjungan_layanan WHERE id_kunjungan = '$id_k'";
$res = $conn->query($sql);
$user = $res->fetch_assoc();

if ($user) {
    // CARI SIAPA YANG SEDANG DILAYANI SECARA GLOBAL
    $res_s = $conn->query("SELECT MIN(nomor_antrean) as skrg FROM kunjungan_layanan 
                           WHERE status_layanan = 'Dalam Antrean' 
                           AND id_dokter = '".$user['id_dokter']."' AND tanggal_kunjungan = '".$user['tanggal_kunjungan']."'");
    echo json_encode([
        "status" => "success",
        "status_layanan" => $user['status_layanan'],
        "nomor_antrean" => (int)$user['nomor_antrean'],
        "antrean_sekarang" => (int)($res_s->fetch_assoc()['skrg'] ?? 0)
    ]);
}
?>