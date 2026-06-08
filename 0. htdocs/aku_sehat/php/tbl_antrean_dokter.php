<?php
header('Content-Type: application/json');
include "connection.php";

$id_dokter = $_GET['id_dokter'] ?? '';

if ($id_dokter == '') {
    echo json_encode([]);
    exit;
}

$sql = "
SELECT
    kl.id_kunjungan,
    kl.nomor_antrean,
    pm.keluhan,
    u.nomor_induk_kependudukan AS nik,
    u.nama_lengkap AS nama
FROM kunjungan_layanan kl
JOIN pemeriksaan pm ON kl.id_kunjungan = pm.id_kunjungan
JOIN pasien p ON kl.id_pasien = p.id_pasien
JOIN users u ON p.id_user = u.id_user
WHERE kl.id_dokter = ?
AND kl.status_layanan = 'Dalam Antrean'
AND (
    pm.diagnosa IS NULL OR pm.diagnosa = ''
    OR pm.catatan_dokter IS NULL OR pm.catatan_dokter = ''
)
ORDER BY kl.nomor_antrean ASC
";

$stmt = mysqli_prepare($conn, $sql);
mysqli_stmt_bind_param($stmt, "i", $id_dokter);
mysqli_stmt_execute($stmt);

$result = mysqli_stmt_get_result($stmt);

$data = [];

while ($row = mysqli_fetch_assoc($result)) {
    $data[] = $row;
}

echo json_encode($data);
?>