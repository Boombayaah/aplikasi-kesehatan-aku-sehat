<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "aku_sehat";

header('content-type:application/json');
$conn = mysqli_connect($servername, $username, $password, $dbname);

if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
}

$sql = "
SELECT
    SUM(CASE WHEN status_layanan = 'Selesai' THEN 1 ELSE 0 END) AS total_selesai,
    SUM(CASE WHEN status_layanan = 'Dalam Antrean' THEN 1 ELSE 0 END) AS total_antrean
FROM kunjungan_layanan
WHERE tanggal_kunjungan = CURDATE()
";

$result = mysqli_query($conn, $sql);
$row = mysqli_fetch_assoc($result);

echo json_encode([
    "total_selesai" => $row["total_selesai"] ?? 0,
    "total_antrean" => $row["total_antrean"] ?? 0
]);

?>