<?php
include 'config.php';
header('Content-Type: application/json');
include 'config.php';

$sql = "SELECT nama_tahapan, deskripsi, image_file FROM alur_layanan";
$result = $conn->query($sql);
$data = [];

while($row = $result->fetch_assoc()) {
    $data[] = $row;
}

echo json_encode($data);
?>