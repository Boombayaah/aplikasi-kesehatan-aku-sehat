<?php
include "connection.php";

$id_pembayaran = $_GET['id_pembayaran'];

$sql = "
SELECT 
    o.nama_obat,
    rd.jumlah,
    o.harga,
    (rd.jumlah * o.harga) AS subtotal_obat
FROM pembayaran p
JOIN kunjungan_layanan k 
    ON p.id_kunjungan = k.id_kunjungan
JOIN pemeriksaan pm 
    ON k.id_kunjungan = pm.id_kunjungan
JOIN resep r 
    ON pm.id_pemeriksaan = r.id_pemeriksaan
JOIN resep_detail rd 
    ON r.id_resep = rd.id_resep
JOIN obat o 
    ON rd.id_obat = o.id_obat
WHERE p.id_pembayaran = $id_pembayaran
";

$result = mysqli_query($conn, $sql);

$row = mysqli_fetch_assoc($result);

echo json_encode($row);
?>