<?php
include 'config.php';
header('Content-Type: application/json');

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $id_user = $_POST['id_user'];
    $nama = $_POST['nama_lengkap'];
    $phone = $_POST['no_hp'];
    $alamat = $_POST['alamat'];
    $bpjs = $_POST['no_bpjs'];
    $dob = $_POST['tanggal_lahir']; // FITUR BARU

    // Update tabel users termasuk kolom tanggal_lahir
    $sql_user = "UPDATE users SET nama_lengkap='$nama', no_hp='$phone', alamat='$alamat', tanggal_lahir='$dob' WHERE id_user='$id_user'";
    $conn->query($sql_user);

    // Update nomor BPJS di tabel pasien (Fitur lama tetap terjaga)
    $sql_pasien = "UPDATE pasien SET no_bpjs_asuransi='$bpjs' WHERE id_user='$id_user'";
    $conn->query($sql_pasien);

    echo json_encode(["status" => "success", "message" => "Profil berhasil diperbarui"]);
}
?>
