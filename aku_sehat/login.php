<?php
header('Content-Type: application/json');
include "connection.php";

$nik = $_POST['nomor_induk_kependudukan'] ?? '';
$password = $_POST['password'] ?? '';

if ($nik == '' || $password == '') {
    echo json_encode([
        "success" => false,
        "message" => "nik dan password wajib diisi"
    ]);
    exit;
}

$hashedPassword = hash('sha512', $password);

$sql = "
SELECT 
    u.id_user,
    u.nama_lengkap,
    u.nama_kategori,
    d.id_dokter
FROM users u
LEFT JOIN dokter d ON u.id_user = d.id_user
WHERE u.nomor_induk_kependudukan = ?
AND u.password = ?
LIMIT 1
";

$stmt = mysqli_prepare($conn, $sql);
mysqli_stmt_bind_param($stmt, "ss", $nik, $hashedPassword);
mysqli_stmt_execute($stmt);

$result = mysqli_stmt_get_result($stmt);

if ($row = mysqli_fetch_assoc($result)) {
    echo json_encode([
        "success" => true,
        "id_user" => $row["id_user"],
        "nama_lengkap" => $row["nama_lengkap"],
        "role" => $row["nama_kategori"],
        "id_dokter" => $row["id_dokter"]
    ]);
} else {
    echo json_encode([
        "success" => false,
        "message" => "nik atau password salah"
    ]);
}
?>