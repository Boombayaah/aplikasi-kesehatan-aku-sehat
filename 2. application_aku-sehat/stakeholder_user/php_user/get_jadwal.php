<?php
// get_jadwal.php
error_reporting(E_ALL);
ini_set('display_errors', 0);

include 'config.php';
header('Content-Type: application/json');

$id_p = isset($_GET['id_pasien']) ? (int)$_GET['id_pasien'] : 0;
$data = [];

try {
    if ($id_p > 0) {
        // AMBIL 1 DATA ABSOLUT TERBARU (Sesuai urutan ID Kunjungan terbesar)
        $sql = "SELECT k.id_kunjungan, k.tanggal_kunjungan, k.waktu_kunjungan, k.status_layanan 
                FROM kunjungan_layanan k
                WHERE k.id_pasien = '$id_p' 
                ORDER BY k.id_kunjungan DESC LIMIT 1";

        $result = $conn->query($sql);
        if ($result && $result->num_rows > 0) {
            while($row = $result->fetch_assoc()) {
                $data[] = $row;
            }
        }
    }
} catch (Exception $e) {
    // Error handling silent
}

echo json_encode($data);
?>