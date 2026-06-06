<?php
include "connection.php";

$sql = "
SELECT
id_user,
nomor_induk_kependudukan AS nik,
nama_lengkap AS nama,
nama_kategori AS jabatan,
email,
no_hp,
foto_profil
FROM users
WHERE nama_kategori != 'Pasien'
ORDER BY id_user DESC
LIMIT 5
";

$result = mysqli_query($conn, $sql);

$data = [];

while ($row = mysqli_fetch_assoc($result)) {
    $data[] = $row;
}

echo json_encode($data);
?>