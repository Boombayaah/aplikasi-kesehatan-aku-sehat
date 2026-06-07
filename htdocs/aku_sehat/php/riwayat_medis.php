<?php
header('Content-Type: application/json');
include "connection.php";

$nik = $_GET['nik'] ?? '';

if ($nik == '') {
    echo json_encode([]);
    exit;
}

$sql = "
SELECT
    kl.tanggal_kunjungan AS tanggal,
    pm.keluhan,
    pm.diagnosa,
    pm.catatan_dokter AS tindakan
FROM kunjungan_layanan kl
JOIN pemeriksaan pm
    ON kl.id_kunjungan = pm.id_kunjungan
JOIN pasien p
    ON kl.id_pasien = p.id_pasien
JOIN users u
    ON p.id_user = u.id_user
WHERE u.nomor_induk_kependudukan = ?
AND kl.status_layanan = 'Selesai'
ORDER BY kl.tanggal_kunjungan DESC, pm.waktu_periksa DESC
LIMIT 3
";

$stmt = mysqli_prepare($conn, $sql);
mysqli_stmt_bind_param($stmt, "s", $nik);
mysqli_stmt_execute($stmt);

$result = mysqli_stmt_get_result($stmt);

$data = [];

while ($row = mysqli_fetch_assoc($result)) {
    $data[] = [
        "tanggal" => $row["tanggal"] ?? "",
        "keluhan" => $row["keluhan"] ?? "",
        "diagnosa" => $row["diagnosa"] ?? "",
        "tindakan" => $row["tindakan"] ?? ""
    ];
}

echo json_encode($data);
?>