<?php
include "connection.php";

$id_alur = $_POST["id_alur"] ?? "";

if (empty($id_alur)) {
    echo "ID kosong";
    exit;
}

$sql = "DELETE FROM alur_layanan WHERE id_alur = '$id_alur'";

if (mysqli_query($conn, $sql)) {
    echo "success";
} else {
    echo mysqli_error($conn);
}
?>