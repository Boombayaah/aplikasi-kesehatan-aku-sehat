<?php
include "connection.php";

$id_kunjungan = $_POST["id_kunjungan"] ?? "";
$diagnosa = $_POST["diagnosa"] ?? "";
$tindakan = $_POST["tindakan"] ?? "";

if ($id_kunjungan == "") {
    echo "id_kunjungan kosong";
    exit;
}

// 1. cari id_pemeriksaan berdasarkan id_kunjungan
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

// 2. update pemeriksaan
$updatePemeriksaan = "
    UPDATE pemeriksaan
    SET 
        diagnosa = '$diagnosa',
        catatan_dokter = '$tindakan',
        waktu_periksa = NOW()
    WHERE id_pemeriksaan = '$id_pemeriksaan'
";

mysqli_query($conn, $updatePemeriksaan);

// 3. hapus resep lama biar gak double kalau konsultasi disimpan ulang
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

// 4. cek apakah ada obat yang dipilih
$adaObat = false;

for ($i = 1; $i <= 3; $i++) {
    if (!empty($_POST["id_obat$i"]) && !empty($_POST["jumlah$i"])) {
        $adaObat = true;
        break;
    }
}

// 5. kalau ada obat, buat resep baru
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

// 6. update status layanan jadi selesai
$updateKunjungan = "
    UPDATE kunjungan_layanan
    SET status_layanan = 'Selesai'
    WHERE id_kunjungan = '$id_kunjungan'
";

mysqli_query($conn, $updateKunjungan);

echo "success";
?>