<?php
if (!isset($_FILES["gambar"])) {
    echo "no_file";
    exit;
}

$targetDir = "uploads/alur/";

if (!is_dir($targetDir)) {
    mkdir($targetDir, 0777, true);
}

$fileName = time() . "_" . basename($_FILES["gambar"]["name"]);
$targetFile = $targetDir . $fileName;

if (move_uploaded_file($_FILES["gambar"]["tmp_name"], $targetFile)) {
    echo $targetFile;
} else {
    echo "failed";
}
?>