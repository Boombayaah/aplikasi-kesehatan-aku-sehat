<?php
error_reporting(0);
include 'config.php';
header('Content-Type: application/json');

$id_pasien = $_GET['id_pasien'] ?? 0;

// Menghubungkan Kunjungan, Dokter, dan Hasil Pemeriksaan (Keluhan & Diagnosa)
$sql = "SELECT k.tanggal_kunjungan, k.status_layanan, 
               u_doc.nama_lengkap as nama_dokter, 
               p.keluhan, p.diagnosa
        FROM kunjungan_layanan k
        LEFT JOIN dokter d ON k.id_dokter = d.id_dokter
        LEFT JOIN users u_doc ON d.id_user = u_doc.id_user
        LEFT JOIN pemeriksaan p ON k.id_kunjungan = p.id_kunjungan
        WHERE k.id_pasien = '$id_pasien'
        ORDER BY k.id_kunjungan DESC";

$result = $conn->query($sql);
$data = [];
while($row = $result->fetch_assoc()) {
    $data[] = $row;
}
echo json_encode($data);
?>