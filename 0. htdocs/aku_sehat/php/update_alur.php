<?php
include "connection.php";

$id_alur = $_POST["id_alur"] ?? "";
$nama_tahapan = $_POST["nama_tahapan"] ?? "";
$deskripsi = $_POST["deskripsi"] ?? "";
$dokumen = $_POST["dokumen"] ?? "";

if (
    empty($id_alur) ||
    empty($nama_tahapan) ||
    empty($deskripsi) ||
    empty($dokumen)
) {
    echo "Data tidak lengkap";
    exit;
}

$sql = "UPDATE alur_layanan SET
            nama_tahapan = '$nama_tahapan',
            deskripsi = '$deskripsi',
            dokumen = '$dokumen'
        WHERE id_alur = '$id_alur'";

if (mysqli_query($conn, $sql)) {
    echo "success";
} else {
    echo mysqli_error($conn);
}
?>