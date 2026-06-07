<?php
include 'config.php';
header('Content-Type: application/json');

$nama = $_POST['nama_lengkap'];
$nik = $_POST['nik'];
$phone = $_POST['no_hp'];
$pass = hash('sha512', $_POST['password']);
$gender = $_POST['jenis_kelamin'];
$alamat = $_POST['alamat'];

// 1. Simpan ke tabel users
$sql_user = "INSERT INTO users (nama_lengkap, nomor_induk_kependudukan, no_hp, password, alamat, nama_kategori) 
             VALUES ('$nama', '$nik', '$phone', '$pass', '$alamat', 'Pasien')";

if ($conn->query($sql_user) === TRUE) {
    $new_user_id = $conn->insert_id;
    
    // 2. Simpan ke tabel pasien
    $sql_pasien = "INSERT INTO pasien (id_user, jenis_kelamin) VALUES ('$new_user_id', '$gender')";
    if ($conn->query($sql_pasien) === TRUE) {
        $new_pasien_id = $conn->insert_id; // Mendapatkan id_pasien yang baru
        
        // 3. Kirim balik ID ke Android agar sesi langsung sinkron
        echo json_encode([
            "status" => "success", 
            "id_user" => (int)$new_user_id, 
            "id_pasien" => (int)$new_pasien_id
        ]);
    } else {
        echo json_encode(["status" => "error", "message" => $conn->error]);
    }
} else {
    echo json_encode(["status" => "error", "message" => $conn->error]);
}
?>