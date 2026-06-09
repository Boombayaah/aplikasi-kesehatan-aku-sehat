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
    (
        SELECT COUNT(*)
        FROM kunjungan_layanan
        WHERE status_layanan = 'Selesai'
        AND tanggal_kunjungan = CURDATE()
    ) AS total_selesai,

    (
        SELECT COUNT(*)
        FROM kunjungan_layanan
        WHERE status_layanan = 'Dalam Antrean'
    ) AS total_antrean
";

$result = mysqli_query($conn, $sql);
$row = mysqli_fetch_assoc($result);

echo json_encode([
    "total_selesai" => $row["total_selesai"] ?? 0,
    "total_antrean" => $row["total_antrean"] ?? 0
]);

?>