<?php
error_reporting(E_ALL);
ini_set('display_errors', 0);

include 'config.php';
header('Content-Type: application/json');

$id_user = $_POST['id_user'] ?? 0;
$id_dokter = $_POST['id_dokter'] ?? 0;
$tanggal = $_POST['tanggal_kunjungan'] ?? '';
$waktu = $_POST['waktu_kunjungan'] ?? '';

if ($id_user == 0 || empty($tanggal) || empty($waktu)) {
    echo json_encode([
        "status" => "error",
        "message" => "Parameter tidak lengkap"
    ]);
    exit;
}

$sql_p = "SELECT id_pasien FROM pasien WHERE id_user = '$id_user' LIMIT 1";
$res_p = $conn->query($sql_p);

if (!$res_p || $res_p->num_rows == 0) {
    echo json_encode([
        "status" => "error",
        "message" => "ID Pasien tidak ditemukan untuk User ID ini"
    ]);
    exit;
}

$row_p = $res_p->fetch_assoc();
$id_pasien = $row_p['id_pasien'];

if ($id_dokter == 0) {
    $res_d = $conn->query("SELECT id_dokter FROM dokter ORDER BY RAND() LIMIT 1");
    $id_dokter = ($res_d && $res_d->num_rows > 0) ? $res_d->fetch_assoc()['id_dokter'] : 1;
}

$sql_ins = "
    INSERT INTO kunjungan_layanan 
    (id_pasien, id_dokter, tanggal_kunjungan, waktu_kunjungan, status_layanan, nomor_antrean) 
    VALUES 
    ('$id_pasien', '$id_dokter', '$tanggal', '$waktu', 'Belum Checkin', 0)
";

if ($conn->query($sql_ins) === TRUE) {
    $new_id = $conn->insert_id;

    $keluhan = "Belum Diisi";

    $insert_pemeriksaan = "
        INSERT INTO pemeriksaan
        (id_kunjungan, keluhan, diagnosa, catatan_dokter)
        VALUES
        ('$new_id', '$keluhan', NULL, NULL)
    ";

    if (!$conn->query($insert_pemeriksaan)) {
        echo json_encode([
            "status" => "error",
            "message" => "Gagal membuat pemeriksaan: " . $conn->error
        ]);
        exit;
    }

    echo json_encode([
        "status" => "success",
        "message" => "Jadwal Berhasil Masuk Database",
        "id_kunjungan" => (int)$new_id,
        "id_pasien" => (int)$id_pasien
    ]);
} else {
    echo json_encode([
        "status" => "error",
        "message" => "MySQL Error: " . $conn->error
    ]);
}
?>