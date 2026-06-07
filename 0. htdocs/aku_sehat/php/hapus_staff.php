<?php
include "connection.php";

$id_user = $_POST['id_user'];

$sql = "DELETE FROM users WHERE id_user = '$id_user'";

if (mysqli_query($conn, $sql)) {
    echo "success";
} else {
    echo "failed";
}
?>