<?php
// Mencegah output teks error yang bisa merusak format JSON
error_reporting(E_ALL);
ini_set('display_errors', 0);

include 'config.php';
header('Content-Type: application/json');

// 1. Tangkap Input POST dari Android (Sama seperti flow Register)
$id_user = $_POST['id_user'] ?? 0;
$id_dokter = $_POST['id_dokter'] ?? 0;
$tanggal = $_POST['tanggal_kunjungan'] ?? '';
$waktu = $_POST['waktu_kunjungan'] ?? '';

// Validasi Parameter
if ($id_user == 0 || empty($tanggal) || empty($waktu)) {
    die(json_encode(["status" => "error", "message" => "Parameter tidak lengkap"]));
}

// 2. LANGKAH VITAL: Cari id_pasien berdasarkan id_user (Relasi Database)
$sql_p = "SELECT id_pasien FROM pasien WHERE id_user = '$id_user' LIMIT 1";
$res_p = $conn->query($sql_p);

if ($res_p && $res_p->num_rows > 0) {
    $row_p = $res_p->fetch_assoc();
    $id_pasien = $row_p['id_pasien'];

    // 3. Pilih Dokter jika id_dokter dikirim 0
    if ($id_dokter == 0) {
        $res_d = $conn->query("SELECT id_dokter FROM dokter ORDER BY RAND() LIMIT 1");
        $id_dokter = ($res_d && $res_d->num_rows > 0) ? $res_d->fetch_assoc()['id_dokter'] : 1;
    }

    // 4. EKSEKUSI INSERT (State Machine: Belum Checkin)
    // Mengikuti urutan kolom database: id_pasien, id_dokter, tanggal, waktu, status
    $sql_ins = "INSERT INTO kunjungan_layanan (id_pasien, id_dokter, tanggal_kunjungan, waktu_kunjungan, status_layanan, nomor_antrean) 
                VALUES ('$id_pasien', '$id_dokter', '$tanggal', '$waktu', 'Belum Checkin', 0)";

    if ($conn->query($sql_ins) === TRUE) {
        $new_id = $conn->insert_id;
        
        // Output Murni JSON
        echo json_encode([
            "status" => "success",
            "message" => "Jadwal Berhasil Masuk Database",
            "id_kunjungan" => (int)$new_id,
            "id_pasien" => (int)$id_pasien
        ]);
    } else {
        echo json_encode(["status" => "error", "message" => "MySQL Error: " . $conn->error]);
    }
} else {
    echo json_encode(["status" => "error", "message" => "ID Pasien tidak ditemukan untuk User ID ini"]);
}
?>