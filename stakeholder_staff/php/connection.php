<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "aku_sehat";

header('content-type:application/json');
$conn = mysqli_connect($servername, $username, $password, $dbname);

if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
}
?>