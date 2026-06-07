<?php

include "connection.php";

$id_pembayaran = $_POST['id_pembayaran'];
$metode_pembayaran = $_POST['metode_pembayaran'];

$sql = "
UPDATE pembayaran
SET metode_pembayaran = '$metode_pembayaran'
WHERE id_pembayaran = '$id_pembayaran'
";

if(mysqli_query($conn, $sql)){
    echo "success";
}else{
    echo "failed";
}

?>