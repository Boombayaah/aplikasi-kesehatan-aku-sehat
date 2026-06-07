<?php
include "connection.php";

$sql = "SELECT id_alur, nama_tahapan, deskripsi, dokumen FROM alur_layanan ORDER BY id_alur DESC";
$result = mysqli_query($conn, $sql);

$data = [];

while ($row = mysqli_fetch_assoc($result)) {
    $data[] = [
        "id_alur" => $row["id_alur"],
        "nama_tahapan" => $row["nama_tahapan"],
        "deskripsi" => $row["deskripsi"],
        "dokumen" => $row["dokumen"]
    ];
}

echo json_encode($data);
?>