<?php
header('Content-Type: application/json');
include "connection.php";

$id_dokter = $_GET['id_dokter'] ?? '';

if ($id_dokter == '') {
    echo json_encode([
        "total_dilayani" => 0,
        "total_antrean" => 0
    ]);
    exit;
}

$sql = "
SELECT
    (
        SELECT COUNT(*)
        FROM kunjungan_layanan kl
        JOIN pemeriksaan pm ON kl.id_kunjungan = pm.id_kunjungan
        WHERE kl.id_dokter = ?
        AND pm.diagnosa IS NOT NULL
        AND pm.diagnosa != ''
        AND pm.catatan_dokter IS NOT NULL
        AND pm.catatan_dokter != ''
    ) AS total_dilayani,

    (
        SELECT COUNT(*)
        FROM kunjungan_layanan
        WHERE status_layanan = 'Dalam Antrean'
    ) AS total_antrean
";

$stmt = mysqli_prepare($conn, $sql);
mysqli_stmt_bind_param($stmt, "i", $id_dokter);
mysqli_stmt_execute($stmt);

$result = mysqli_stmt_get_result($stmt);
$row = mysqli_fetch_assoc($result);

echo json_encode([
    "total_dilayani" => $row["total_dilayani"] ?? 0,
    "total_antrean" => $row["total_antrean"] ?? 0
]);
?>