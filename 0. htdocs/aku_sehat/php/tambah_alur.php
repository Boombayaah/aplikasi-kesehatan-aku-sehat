<?php
include "connection.php";

$nama_tahapan = $_POST["nama_tahapan"] ?? "";
$deskripsi = $_POST["deskripsi"] ?? "";
$dokumen = $_POST["dokumen"] ?? "";

if (
    empty($nama_tahapan) ||
    empty($deskripsi) ||
    empty($dokumen)
) {
    echo "Data tidak lengkap";
    exit;
}

$sql = "INSERT INTO alur_layanan (
            nama_tahapan,
            deskripsi,
            dokumen
        )
        VALUES (
            '$nama_tahapan',
            '$deskripsi',
            '$dokumen'
        )";

if (mysqli_query($conn, $sql)) {
    echo "success";
} else {
    echo mysqli_error($conn);
}
?>