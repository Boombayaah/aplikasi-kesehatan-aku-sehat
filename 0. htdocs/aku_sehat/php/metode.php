<?php

include "connection.php";

$id_pembayaran = $_POST['id_pembayaran'];
$metode_pembayaran = $_POST['metode_pembayaran'];

// update pembayaran
$sql = "
UPDATE pembayaran
SET
    metode_pembayaran = '$metode_pembayaran',
    status_pembayaran = 'Lunas',
    waktu_bayar = NOW()
WHERE id_pembayaran = '$id_pembayaran'
";

if(mysqli_query($conn, $sql)){

    // ambil id_kunjungan
    $query = mysqli_query(
        $conn,
        "SELECT id_kunjungan
         FROM pembayaran
         WHERE id_pembayaran = '$id_pembayaran'"
    );

    $data = mysqli_fetch_assoc($query);

    if($data){

        $id_kunjungan = $data['id_kunjungan'];

        mysqli_query(
            $conn,
            "UPDATE kunjungan_layanan
             SET status_layanan = 'Selesai'
             WHERE id_kunjungan = '$id_kunjungan'"
        );
    }

    echo "success";

}else{

    echo "failed";

}
?>