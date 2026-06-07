<?php
include 'config.php';
header('Content-Type: application/json');

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Menangkap input secara fleksibel (bisa dikirim sebagai 'no_hp' atau 'nik')
    $identifier = $_POST['no_hp'] ?? $_POST['nik'] ?? '';
    $password = $_POST['password'] ?? '';

    if (empty($identifier) || empty($password)) {
        die(json_encode(["status" => "error", "message" => "Harap isi identitas dan password"]));
    }

    // Hash password menggunakan SHA-512 sesuai standar sistem aplikasi ini
    $hashed_password = hash('sha512', $password);

    // Query JOIN untuk mendapatkan data User (termasuk nama_kategori/role), data Pasien, dan data Dokter
    $sql = "SELECT u.id_user, u.nama_lengkap, u.alamat, u.no_hp, u.nomor_induk_kependudukan, u.tanggal_lahir, u.nama_kategori,
                   p.id_pasien, p.no_bpjs_asuransi, p.jenis_kelamin,
                   d.id_dokter, d.spesialisasi, d.no_izin_praktik
            FROM users u 
            LEFT JOIN pasien p ON u.id_user = p.id_user 
            LEFT JOIN dokter d ON u.id_user = d.id_user
            WHERE (u.no_hp = '$identifier' OR u.nomor_induk_kependudukan = '$identifier')
            AND u.password = '$hashed_password' LIMIT 1";

    $result = $conn->query($sql);

    if ($result && $result->num_rows > 0) {
        $row = $result->fetch_assoc();
        
        // Response JSON harus presisi agar bisa dibaca oleh Retrofit/Volley di Android
        $response = [
            "status" => "success",
            "id_user" => (int)$row['id_user'],
            "id_pasien" => (int)$row['id_pasien'],
            "id_dokter" => (int)$row['id_dokter'],
            "user" => [
                "nama_lengkap" => $row['nama_lengkap'],
                "nik" => $row['nomor_induk_kependudukan'],
                "alamat" => $row['alamat'],
                "no_hp" => $row['no_hp'],
                "tanggal_lahir" => $row['tanggal_lahir'],
                "nama_kategori" => $row['nama_kategori'] // Data penting untuk pemisah role
            ],
            "pasien" => [
                "no_bpjs_asuransi" => $row['no_bpjs_asuransi'],
                "jenis_kelamin" => $row['jenis_kelamin']
            ]
        ];

        // Tambahkan data dokter jika id_dokter tersedia di hasil query
        if (isset($row['id_dokter']) && $row['id_dokter'] !== '') {
            $response["dokter"] = [
                "id_dokter" => (int)$row['id_dokter'],
                "id_user" => (int)$row['id_user'],
                "spesialisasi" => $row['spesialisasi'],
                "no_izin_praktik" => $row['no_izin_praktik']
            ];
            $response["spesialisasi"] = $row['spesialisasi'];
            $response["no_izin_praktik"] = $row['no_izin_praktik'];
        }

        echo json_encode($response);
    } else {
        // Pesan jika akun tidak ditemukan atau password salah
        echo json_encode(["status" => "error", "message" => "Nomor Telepon/NIK atau Password Salah"]);
    }
} else {
    echo json_encode(["status" => "error", "message" => "Metode Request Tidak Valid"]);
}
?>