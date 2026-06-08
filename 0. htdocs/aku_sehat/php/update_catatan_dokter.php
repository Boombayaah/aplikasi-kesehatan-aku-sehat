<?php
include "connection.php";

$id_kunjungan = $_POST["id_kunjungan"] ?? "";
$diagnosa = $_POST["diagnosa"] ?? "";
$tindakan = $_POST["tindakan"] ?? "";

if ($id_kunjungan == "") {
    echo "id_kunjungan kosong";
    exit;
}

$queryPemeriksaan = "
    SELECT id_pemeriksaan 
    FROM pemeriksaan 
    WHERE id_kunjungan = '$id_kunjungan'
";

$resultPemeriksaan = mysqli_query($conn, $queryPemeriksaan);

if (mysqli_num_rows($resultPemeriksaan) == 0) {
    echo "pemeriksaan tidak ditemukan";
    exit;
}

$row = mysqli_fetch_assoc($resultPemeriksaan);
$id_pemeriksaan = $row["id_pemeriksaan"];

$updatePemeriksaan = "
    UPDATE pemeriksaan
    SET 
        diagnosa = '$diagnosa',
        catatan_dokter = '$tindakan',
        waktu_periksa = NOW()
    WHERE id_pemeriksaan = '$id_pemeriksaan'
";

mysqli_query($conn, $updatePemeriksaan);

$getResepLama = "
    SELECT id_resep 
    FROM resep 
    WHERE id_pemeriksaan = '$id_pemeriksaan'
";

$resultResepLama = mysqli_query($conn, $getResepLama);

while ($resepLama = mysqli_fetch_assoc($resultResepLama)) {
    $id_resep_lama = $resepLama["id_resep"];

    mysqli_query($conn, "
        DELETE FROM resep_detail 
        WHERE id_resep = '$id_resep_lama'
    ");

    mysqli_query($conn, "
        DELETE FROM resep 
        WHERE id_resep = '$id_resep_lama'
    ");
}

$total_tagihan = 250000;
$adaObat = false;

for ($i = 1; $i <= 3; $i++) {
    if (!empty($_POST["id_obat$i"]) && !empty($_POST["jumlah$i"])) {
        $adaObat = true;
        break;
    }
}

if ($adaObat) {
    $insertResep = "
        INSERT INTO resep (id_pemeriksaan)
        VALUES ('$id_pemeriksaan')
    ";

    mysqli_query($conn, $insertResep);

    $id_resep = mysqli_insert_id($conn);

    for ($i = 1; $i <= 3; $i++) {
        $id_obat = $_POST["id_obat$i"] ?? "";
        $jumlah = $_POST["jumlah$i"] ?? "";
        $resep = $_POST["resep$i"] ?? "Obat umum";

        if ($id_obat != "" && $jumlah != "") {

            $getHargaObat = "
                SELECT harga
                FROM obat
                WHERE id_obat = '$id_obat'
            ";

            $resultHargaObat = mysqli_query($conn, $getHargaObat);
            $rowHargaObat = mysqli_fetch_assoc($resultHargaObat);

            $harga_obat = $rowHargaObat["harga"] ?? 0;
            $total_tagihan += ((int)$harga_obat * (int)$jumlah);

            $insertDetail = "
                INSERT INTO resep_detail 
                (id_resep, id_obat, jumlah, instruksi_konsumsi, kerahasiaan)
                VALUES 
                ('$id_resep', '$id_obat', '$jumlah', NULL, '$resep')
            ";

            mysqli_query($conn, $insertDetail);
        }
    }
}

$updateKunjungan = "
    UPDATE kunjungan_layanan
    SET status_layanan = 'Dalam Antrean'
    WHERE id_kunjungan = '$id_kunjungan'
";

mysqli_query($conn, $updateKunjungan);

$cekPembayaran = "
    SELECT id_pembayaran
    FROM pembayaran
    WHERE id_kunjungan = '$id_kunjungan'
";

$resultPembayaran = mysqli_query($conn, $cekPembayaran);

if (mysqli_num_rows($resultPembayaran) == 0) {
    $insertPembayaran = "
        INSERT INTO pembayaran
        (id_kunjungan, id_admin, total_tagihan, metode_pembayaran, status_pembayaran)
        VALUES
        ('$id_kunjungan', 1, '$total_tagihan', NULL, 'Belum bayar')
    ";

    mysqli_query($conn, $insertPembayaran);
} else {
    $updatePembayaran = "
        UPDATE pembayaran
        SET 
            total_tagihan = '$total_tagihan',
            metode_pembayaran = 'NULL',
            status_pembayaran = 'Belum bayar'
        WHERE id_kunjungan = '$id_kunjungan'
    ";

    mysqli_query($conn, $updatePembayaran);
}

echo "success";
?>