<?php
// CRUD admin_staff
// create: 
$nama_kategori = 'Admin';
$nomor_induk_kependudukan = ;
$nama_lengkap = ;
$user_password = hash('sha512', $user_password);
$tanggal_lahir = ;
$alamat = ;
$no_hp = ;
$created_by = $USER_SESSION['nama_lengkap'];

$sql = "INSERT INTO users('nama_kategori', 'nomor_induk_kependudukan', 'nama_lengkap', 'password', 'tanggal_lahir', 'alamat', 'no_hp', 'created_by') ";
$sql .= " VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
$conn->execute_query($sql, [$nama_kategori, $nomor_induk_kependudukan, $nama_lengkap, $user_password, $tanggal_lahir, $alamat, $no_hp, $created_by]);

$stmt = $conn->prepare("SELECT id_user FROM users WHERE nomor_induk_kependudukan = :nomor_induk_kependudukan");
$stmt->execute(['nomor_induk_kependudukan' => $nomor_induk_kependudukan]);
$user = $stmt->fetch();
$id_user = $user['id_user'];
$sql = "INSERT INTO admin('id_user', 'created_by')";
$sql .= " VALUES (?, ?);";
$conn->execute_query($sql, [$id_user, $created_by]);

// read:
$sql = "SELECT * FROM admin a INNER JOIN users u ON a.id_user = u.id_user ORDER BY u.created_at DESC;";
$result = $conn->query($sql);

// update:
$nama_kategori = 'Admin';
$nomor_induk_kependudukan = ;
$nama_lengkap = ;
$user_password = hash('sha512', $user_password);
$tanggal_lahir = ;
$alamat = ;
$no_hp = ;
$updated_by = $USER_SESSION['nama_lengkap'];
$sql = "UPDATE users";
$sql .= " SET 'nama_kategori' = ?, 'nomor_induk_kependudukan' = ?, 'nama_lengkap' = ?, 'password' = ?, 'tanggal_lahir' = ?, 'alamat' = ?, 'no_hp' = ?, 'updated_by' = ?";
$sql .= " WHERE 'nomor_induk_kependudukan' = ?;";
$conn->execute_query($sql, [$nama_kategori, $nomor_induk_kependudukan, $nama_lengkap, $user_password, $tanggal_lahir, $alamat, $no_hp, $updated_by, $nomor_induk_kependudukan]);

-- delete:
$nomor_induk_kependudukan = ;
$stmt = $conn->prepare("SELECT id_user FROM users WHERE nomor_induk_kependudukan = :nomor_induk_kependudukan");
$stmt->execute(['nomor_induk_kependudukan' => $nomor_induk_kependudukan]);
$user = $stmt->fetch();
$id_user = $user['id_user'];

$sql = "DELETE FROM admin WHERE id_user = ?;";
$conn->execute_query($sql, [$id_user]);

$sql = "DELETE FROM user WHERE id_user = ?;";
$conn->execute_query($sql, [$id_user]);




// CRUD admin_leader
// create: 
$nama_kategori = 'Admin Leader';
$nomor_induk_kependudukan = ;
$nama_lengkap = ;
$user_password = hash('sha512', $user_password);
$tanggal_lahir = ;
$alamat = ;
$no_hp = ;
$created_by = $USER_SESSION['nama_lengkap'];

$sql = "INSERT INTO users('nama_kategori', 'nomor_induk_kependudukan', 'nama_lengkap', 'password', 'tanggal_lahir', 'alamat', 'no_hp', 'created_by') ";
$sql .= " VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
$conn->execute_query($sql, [$nama_kategori, $nomor_induk_kependudukan, $nama_lengkap, $user_password, $tanggal_lahir, $alamat, $no_hp, $created_by]);

$stmt = $conn->prepare("SELECT id_user FROM users WHERE nomor_induk_kependudukan = :nomor_induk_kependudukan");
$stmt->execute(['nomor_induk_kependudukan' => $nomor_induk_kependudukan]);
$user = $stmt->fetch();
$id_user = $user['id_user'];
$sql = "INSERT INTO admin('id_user', 'created_by')";
$sql .= " VALUES (?, ?);";
$conn->execute_query($sql, [$id_user, $created_by]);

// read:
$sql = "SELECT * FROM admin a INNER JOIN users u ON a.id_user = u.id_user ORDER BY u.created_at DESC;";
$result = $conn->query($sql);

// update:
$nama_kategori = 'Admin Leader';
$nomor_induk_kependudukan = ;
$nama_lengkap = ;
$user_password = hash('sha512', $user_password);
$tanggal_lahir = ;
$alamat = ;
$no_hp = ;
$updated_by = $USER_SESSION['nama_lengkap'];
$sql = "UPDATE users";
$sql .= " SET 'nama_kategori' = ?, 'nomor_induk_kependudukan' = ?, 'nama_lengkap' = ?, 'password' = ?, 'tanggal_lahir' = ?, 'alamat' = ?, 'no_hp' = ?, 'updated_by' = ?";
$sql .= " WHERE 'nomor_induk_kependudukan' = ?;";
$conn->execute_query($sql, [$nama_kategori, $nomor_induk_kependudukan, $nama_lengkap, $user_password, $tanggal_lahir, $alamat, $no_hp, $updated_by, $nomor_induk_kependudukan]);

// delete:
$nomor_induk_kependudukan = ;
$stmt = $conn->prepare("SELECT id_user FROM users WHERE nomor_induk_kependudukan = :nomor_induk_kependudukan");
$stmt->execute(['nomor_induk_kependudukan' => $nomor_induk_kependudukan]);
$user = $stmt->fetch();
$id_user = $user['id_user'];

$sql = "DELETE FROM admin WHERE id_user = ?;";
$conn->execute_query($sql, [$id_user]);

$sql = "DELETE FROM user WHERE id_user = ?;";
$conn->execute_query($sql, [$id_user]);




// CRUD dokter
// create: 
$nama_kategori = 'Dokter';
$nomor_induk_kependudukan = ;
$nama_lengkap = ;
$user_password = hash('sha512', $user_password);
$tanggal_lahir = ;
$alamat = ;
$no_hp = ;
$spesialisasi = ;
$no_izin_praktik = ; 
$created_by = $USER_SESSION['nama_lengkap'];

$sql = "INSERT INTO users('nama_kategori', 'nomor_induk_kependudukan', 'nama_lengkap', 'password', 'tanggal_lahir', 'alamat', 'no_hp', 'created_by') ";
$sql .= " VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
$conn->execute_query($sql, [$nama_kategori, $nomor_induk_kependudukan, $nama_lengkap, $user_password, $tanggal_lahir, $alamat, $no_hp, $created_by]);

$stmt = $conn->prepare("SELECT id_user FROM users WHERE nomor_induk_kependudukan = :nomor_induk_kependudukan");
$stmt->execute(['nomor_induk_kependudukan' => $nomor_induk_kependudukan]);
$user = $stmt->fetch();
$id_user = $user['id_user'];
$sql = "INSERT INTO dokter('id_user', 'spesialisasi', 'no_izin_praktik', 'created_by')";
$sql .= " VALUES (?, ?);";
$conn->execute_query($sql, [$id_user, $spesialisasi, $no_izin_praktik, $created_by]);

// read:
$sql = "SELECT * FROM dokter d INNER JOIN users u ON d.id_user = u.id_user ORDER BY u.created_at DESC;";
$result = $conn->query($sql);

// update:
$nama_kategori = 'Dokter';
$nomor_induk_kependudukan = ;
$nama_lengkap = ;
$user_password = hash('sha512', $user_password);
$tanggal_lahir = ;
$alamat = ;
$no_hp = ;
$updated_by = $USER_SESSION['nama_lengkap'];
$sql = "UPDATE users";
$sql .= " SET 'nama_kategori' = ?, 'nomor_induk_kependudukan' = ?, 'nama_lengkap' = ?, 'password' = ?, 'tanggal_lahir' = ?, 'alamat' = ?, 'no_hp' = ?, 'updated_by' = ?";
$sql .= " WHERE 'nomor_induk_kependudukan' = ?;";
$conn->execute_query($sql, [$nama_kategori, $nomor_induk_kependudukan, $nama_lengkap, $user_password, $tanggal_lahir, $alamat, $no_hp, $updated_by, $nomor_induk_kependudukan]);

$spesialisasi = ;
$no_izin_praktik = ;
$sql = "UPDATE dokter";
$sql .= " SET 'spesialisasi' = ?, 'no_izin_praktik' = ?, 'updated_by' = ?";
$sql .= " WHERE 'nomor_induk_kependudukan' = ?;";
$conn->execute_query($sql, [$spesialisasi, $no_izin_praktik]);

// delete:
$nomor_induk_kependudukan = ;
$stmt = $conn->prepare("SELECT id_user FROM users WHERE nomor_induk_kependudukan = :nomor_induk_kependudukan");
$stmt->execute(['nomor_induk_kependudukan' => $nomor_induk_kependudukan]);
$user = $stmt->fetch();
$id_user = $user['id_user'];

$sql = "DELETE FROM dokter WHERE id_user = ?;";
$conn->execute_query($sql, [$id_user]);

$sql = "DELETE FROM user WHERE id_user = ?;";
$conn->execute_query($sql, [$id_user]);