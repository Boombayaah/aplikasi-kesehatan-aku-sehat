<?php
include "connection.php";

header("Content-Type: application/json");

$query = "SELECT id_obat, nama_obat FROM obat ORDER BY nama_obat ASC";
$result = mysqli_query($conn, $query);

$data = [];

while ($row = mysqli_fetch_assoc($result)) {
    $data[] = [
        "id_obat" => $row["id_obat"],
        "nama_obat" => $row["nama_obat"]
    ];
}

echo json_encode($data);
?>