<?php
include "connection.php";

if (!isset($_FILES['foto'])) {
    echo "no_file";
    exit;
}

$folder = "uploads/profiles/";

if (!is_dir($folder)) {
    mkdir($folder, 0777, true);
}

$fileName = time() . "_" . basename($_FILES["foto"]["name"]);
$targetPath = $folder . $fileName;

if (move_uploaded_file($_FILES["foto"]["tmp_name"], $targetPath)) {
    echo $targetPath;
} else {
    echo "failed";
}
?>