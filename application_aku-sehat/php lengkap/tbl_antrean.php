<?php
include "connection.php";

$sql = "
SELECT
    k.nomor_antrean AS no_urut,
    k.waktu_checkin,
    u.nomor_induk_kependudukan AS nik,
    u.nama_lengkap AS nama

FROM kunjungan_layanan k

JOIN pasien ps
    ON k.id_pasien = ps.id_pasien

JOIN users u
    ON ps.id_user = u.id_user

WHERE k.status_layanan = 'Dalam Antrean'

ORDER BY k.nomor_antrean ASC

LIMIT 5
";

$result = mysqli_query($conn, $sql);

$data = array();

while($row = mysqli_fetch_assoc($result)){
    $data[] = $row;
}

echo json_encode($data);
?>