<?php
include "connection.php";

$id_user = $_POST["id_user"] ?? "";
$nik = $_POST["nik"] ?? "";
$nama = $_POST["nama"] ?? "";
$jabatan = $_POST["jabatan"] ?? "";
$email = $_POST["email"] ?? "";
$no_hp = $_POST["no_hp"] ?? "";
$foto_profil = $_POST["foto_profil"] ?? "";

$sql = "
UPDATE users
SET
    nomor_induk_kependudukan = '$nik',
    nama_lengkap = '$nama',
    nama_kategori = '$jabatan',
    email = '$email',
    no_hp = '$no_hp',
    foto_profil = '$foto_profil'
WHERE id_user = '$id_user'
";

if (mysqli_query($conn, $sql)) {
    echo "success";
} else {
    echo "failed: " . mysqli_error($conn);
}
?>