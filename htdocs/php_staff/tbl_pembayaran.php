<?php
include "connection.php";

$sql = "
SELECT
    u.nomor_induk_kependudukan AS nik,
    u.nama_lengkap AS nama,
    p.total_tagihan AS jumlah,
    p.id_pembayaran,
    p.metode_pembayaran,
    k.tanggal_kunjungan,
    k.waktu_kunjungan
FROM pembayaran p
JOIN kunjungan_layanan k
    ON p.id_kunjungan = k.id_kunjungan
JOIN pasien ps
    ON k.id_pasien = ps.id_pasien
JOIN users u
    ON ps.id_user = u.id_user
ORDER BY p.waktu_bayar DESC
LIMIT 5
";

$result = mysqli_query($conn, $sql);

$data = array();

while($row = mysqli_fetch_assoc($result)){
    $data[] = $row;
}


echo json_encode($data);
?>