<?php
include "connection.php";

$nik = $_POST['nik'] ?? '';
$nama = $_POST['nama'] ?? '';
$jabatan = $_POST['jabatan'] ?? '';
$email = $_POST['email'] ?? '';
$no_hp = $_POST['no_hp'] ?? '';
$foto_profil = $_POST['foto_profil'] ?? '';

$password = password_hash("123456", PASSWORD_DEFAULT);

$sql = "
INSERT INTO users
(
    nama_kategori,
    nomor_induk_kependudukan,
    nama_lengkap,
    password,
    no_hp,
    email,
    foto_profil
)
VALUES
(
    '$jabatan',
    '$nik',
    '$nama',
    '$password',
    '$no_hp',
    '$email',
    '$foto_profil'
)
";

if (mysqli_query($conn, $sql)) {
    echo "success";
} else {
    echo "failed: " . mysqli_error($conn);
}
?>