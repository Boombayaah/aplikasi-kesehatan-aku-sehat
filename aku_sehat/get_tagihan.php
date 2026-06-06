<?php
// get_tagihan.php
error_reporting(E_ALL);
ini_set('display_errors', 0);

include 'config.php';
header('Content-Type: application/json');

$id_k = isset($_GET['id_kunjungan']) ? (int)$_GET['id_kunjungan'] : 0;

if ($id_k <= 0) {
    die(json_encode(["status" => "error", "message" => "ID tidak valid"]));
}

try {
    // 1. CEK STATUS TERBARU (Sistem Gatekeeper)
    $res_check = $conn->query("SELECT status_layanan FROM kunjungan_layanan WHERE id_kunjungan = '$id_k'");
    $status_row = $res_check->fetch_assoc();
    
    // Jika belum check-in, kirim status 'waiting' agar Android menampilkan pesan edukatif
    if (!$status_row || $status_row['status_layanan'] == 'Belum Checkin') {
        die(json_encode([
            "status" => "waiting", 
            "message" => "Tagihan hanya dapat dipantau setelah Anda melakukan Check-in di klinik."
        ]));
    }

    // 2. JIKA SUDAH CHECK-IN: Tarik Data Master & Total dari Tabel Pembayaran
    $sql_main = "SELECT k.tanggal_kunjungan, k.waktu_kunjungan, k.status_layanan, 
                        p.total_tagihan, p.status_pembayaran, p.metode_pembayaran 
                 FROM kunjungan_layanan k 
                 LEFT JOIN pembayaran p ON k.id_kunjungan = p.id_kunjungan 
                 WHERE k.id_kunjungan = '$id_k' LIMIT 1";
    
    $res_main = $conn->query($sql_main);
    $main = $res_main->fetch_assoc();

    // 3. TARIK RINCIAN OBAT (Chain SELECT: Pemeriksaan -> Resep -> Detail -> Obat)
    $sql_meds = "SELECT o.nama_obat, rd.jumlah, o.harga, (o.harga * rd.jumlah) as subtotal 
                 FROM pemeriksaan pem 
                 INNER JOIN resep r ON pem.id_pemeriksaan = r.id_pemeriksaan 
                 INNER JOIN resep_detail rd ON r.id_resep = rd.id_resep 
                 INNER JOIN obat o ON rd.id_obat = o.id_obat 
                 WHERE pem.id_kunjungan = '$id_k'";
    
    $res_meds = $conn->query($sql_meds);
    $obat_list = [];
    while($r = $res_meds->fetch_assoc()){ 
        $obat_list[] = [
            "nama_obat" => $r['nama_obat'],
            "jumlah" => (int)$r['jumlah'],
            "harga_satuan" => (float)$r['harga'],
            "subtotal" => (float)$r['subtotal']
        ];
    }

    // 4. OUTPUT JSON FINAL (Data murni pembacaan Database)
    echo json_encode([
        "status" => "success",
        "id_kunjungan" => (int)$id_k,
        "tanggal_kunjungan" => $main['tanggal_kunjungan'],
        "waktu_kunjungan" => $main['waktu_kunjungan'],
        "total_tagihan_akhir" => (float)($main['total_tagihan'] ?? 250000), 
        "status_pembayaran" => $main['status_pembayaran'] ?? "Belum bayar",
        "metode_pembayaran" => $main['metode_pembayaran'] ?? "-",
        "show_consultation_fee" => true,
        "rincian_obat" => $obat_list
    ]);

} catch (Exception $e) {
    echo json_encode(["status" => "error", "message" => "Database Error"]);
}
?>