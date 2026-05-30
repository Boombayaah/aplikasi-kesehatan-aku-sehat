-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 30, 2026 at 01:53 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `aku_sehat`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `id_admin` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp() COMMENT 'Untuk fungsi accounting (audit)',
  `created_by` varchar(100) DEFAULT NULL COMMENT 'Untuk fungsi accounting (audit)',
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT 'Untuk fungsi accounting (audit)',
  `updated_by` varchar(100) DEFAULT NULL COMMENT 'Untuk fungsi accounting (audit)'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`id_admin`, `id_user`, `created_at`, `created_by`, `updated_at`, `updated_by`) VALUES
(1, 1, '2026-05-30 01:09:22', NULL, '2026-05-30 01:09:22', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `alur_layanan`
--

CREATE TABLE `alur_layanan` (
  `id_alur` int(11) NOT NULL,
  `nama_tahapan` varchar(100) NOT NULL,
  `deskripsi` text DEFAULT NULL COMMENT 'Deskripsi tambahan seperti prosedur',
  `dokumen` text DEFAULT NULL COMMENT 'Berkas-berkas seperti gambar dan dokumen',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp() COMMENT 'Untuk fungsi accounting (audit)',
  `created_by` varchar(100) DEFAULT NULL COMMENT 'Untuk fungsi accounting (audit)',
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT 'Untuk fungsi accounting (audit)',
  `updated_by` varchar(100) DEFAULT NULL COMMENT 'Untuk fungsi accounting (audit)'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `alur_layanan`
--

INSERT INTO `alur_layanan` (`id_alur`, `nama_tahapan`, `deskripsi`, `dokumen`, `created_at`, `created_by`, `updated_at`, `updated_by`) VALUES
(1, 'Registrasi', 'Pendaftaran antrean', NULL, '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL),
(2, 'Pemeriksaan', 'Tindakan medis', NULL, '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL),
(3, 'Resep', 'Pemberian obat', NULL, '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL),
(4, 'Pembayaran', 'Proses kasir', NULL, '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL),
(5, 'Selesai', 'Pasien pulang', NULL, '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `dokter`
--

CREATE TABLE `dokter` (
  `id_dokter` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `spesialisasi` varchar(100) NOT NULL,
  `no_izin_praktik` varchar(100) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp() COMMENT 'Untuk fungsi accounting (audit)',
  `created_by` varchar(100) DEFAULT NULL COMMENT 'Untuk fungsi accounting (audit)',
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT 'Untuk fungsi accounting (audit)',
  `updated_by` varchar(100) DEFAULT NULL COMMENT 'Untuk fungsi accounting (audit)'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `dokter`
--

INSERT INTO `dokter` (`id_dokter`, `id_user`, `spesialisasi`, `no_izin_praktik`, `created_at`, `created_by`, `updated_at`, `updated_by`) VALUES
(1, 2, 'Anak', 'SIP-001', '2026-05-30 01:09:22', NULL, '2026-05-30 01:09:22', NULL),
(2, 3, 'Penyakit Dalam', 'SIP-002', '2026-05-30 01:09:22', NULL, '2026-05-30 01:09:22', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `kategori_pengguna`
--

CREATE TABLE `kategori_pengguna` (
  `nama_kategori` varchar(50) NOT NULL COMMENT 'Jenis kategori pengguna',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp() COMMENT 'Untuk fungsi accounting (audit)',
  `created_by` varchar(100) DEFAULT NULL COMMENT 'Untuk fungsi accounting (audit)',
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT 'Untuk fungsi accounting (audit)',
  `updated_by` varchar(100) DEFAULT NULL COMMENT 'Untuk fungsi accounting (audit)'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `kategori_pengguna`
--

INSERT INTO `kategori_pengguna` (`nama_kategori`, `created_at`, `created_by`, `updated_at`, `updated_by`) VALUES
('Admin', '2026-05-30 01:09:02', NULL, '2026-05-30 01:09:02', NULL),
('Dokter', '2026-05-30 01:09:02', NULL, '2026-05-30 01:09:02', NULL),
('Pasien', '2026-05-30 01:09:02', NULL, '2026-05-30 01:09:02', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `kunjungan_layanan`
--

CREATE TABLE `kunjungan_layanan` (
  `id_kunjungan` int(11) NOT NULL,
  `id_pasien` int(11) NOT NULL,
  `id_dokter` int(11) NOT NULL,
  `tanggal_kunjungan` date NOT NULL COMMENT 'Tanggal kunjungan',
  `waktu_kunjungan` time NOT NULL COMMENT 'Waktu mulai kunjungan',
  `nomor_antrean` int(11) DEFAULT NULL COMMENT 'COMMENT ''Bisa didapatkan jika sudah checkin''',
  `waktu_checkin` time NOT NULL,
  `dokumen_checkin` varchar(200) NOT NULL,
  `status_layanan` enum('Belum CheckIn','Dalam Antrean','Selesai') NOT NULL DEFAULT 'Belum CheckIn',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp() COMMENT 'Untuk fungsi accounting (audit)',
  `created_by` varchar(100) DEFAULT NULL COMMENT 'Untuk fungsi accounting (audit)',
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT 'Untuk fungsi accounting (audit)',
  `updated_by` varchar(100) DEFAULT NULL COMMENT 'Untuk fungsi accounting (audit)'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `kunjungan_layanan`
--

INSERT INTO `kunjungan_layanan` (`id_kunjungan`, `id_pasien`, `id_dokter`, `tanggal_kunjungan`, `waktu_kunjungan`, `nomor_antrean`, `waktu_checkin`, `dokumen_checkin`, `status_layanan`, `created_at`, `created_by`, `updated_at`, `updated_by`) VALUES
(1, 1, 1, '2026-05-30', '08:00:00', 1, '08:10:00', '', 'Dalam Antrean', '2026-05-30 01:09:31', NULL, '2026-05-30 04:32:48', NULL),
(2, 2, 2, '2026-05-30', '08:00:00', 2, '08:15:00', '', 'Dalam Antrean', '2026-05-30 01:09:31', NULL, '2026-05-30 04:32:48', NULL),
(3, 1, 1, '2026-05-31', '09:00:00', 0, '00:00:00', '', 'Belum CheckIn', '2026-05-30 01:09:31', NULL, '2026-05-30 04:32:48', NULL),
(4, 2, 2, '2026-05-31', '09:00:00', 0, '00:00:00', '', 'Belum CheckIn', '2026-05-30 01:09:31', NULL, '2026-05-30 04:32:48', NULL),
(5, 1, 1, '2026-06-01', '09:00:00', 0, '00:00:00', '', 'Belum CheckIn', '2026-05-30 01:09:31', NULL, '2026-05-30 04:32:48', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `obat`
--

CREATE TABLE `obat` (
  `id_obat` int(11) NOT NULL,
  `nama_obat` varchar(100) NOT NULL,
  `stok` int(11) DEFAULT 0,
  `harga` decimal(10,2) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp() COMMENT 'Untuk fungsi accounting (audit)',
  `created_by` varchar(100) DEFAULT NULL COMMENT 'Untuk fungsi accounting (audit)',
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT 'Untuk fungsi accounting (audit)',
  `updated_by` varchar(100) DEFAULT NULL COMMENT 'Untuk fungsi accounting (audit)'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `obat`
--

INSERT INTO `obat` (`id_obat`, `nama_obat`, `stok`, `harga`, `created_at`, `created_by`, `updated_at`, `updated_by`) VALUES
(1, 'Paracetamol', 100, 5000.00, '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL),
(2, 'Amoxicillin', 50, 15000.00, '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL),
(3, 'Antasida', 75, 8000.00, '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL),
(4, 'Vitamin C', 200, 2000.00, '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL),
(5, 'Ibuprofen', 60, 10000.00, '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `pasien`
--

CREATE TABLE `pasien` (
  `id_pasien` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `no_bpjs_asuransi` varchar(100) DEFAULT NULL,
  `jenis_kelamin` enum('L','P') NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp() COMMENT 'Untuk fungsi accounting (audit)',
  `created_by` varchar(100) DEFAULT NULL COMMENT 'Untuk fungsi accounting (audit)',
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT 'Untuk fungsi accounting (audit)',
  `updated_by` varchar(100) DEFAULT NULL COMMENT 'Untuk fungsi accounting (audit)'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pasien`
--

INSERT INTO `pasien` (`id_pasien`, `id_user`, `no_bpjs_asuransi`, `jenis_kelamin`, `created_at`, `created_by`, `updated_at`, `updated_by`) VALUES
(1, 4, 'BPJS-001', 'L', '2026-05-30 01:09:22', NULL, '2026-05-30 01:09:22', NULL),
(2, 5, 'BPJS-002', 'P', '2026-05-30 01:09:22', NULL, '2026-05-30 01:09:22', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `pembayaran`
--

CREATE TABLE `pembayaran` (
  `id_pembayaran` int(11) NOT NULL,
  `id_kunjungan` int(11) NOT NULL,
  `id_admin` int(11) NOT NULL,
  `total_tagihan` decimal(10,2) DEFAULT 0.00,
  `metode_pembayaran` enum('Tunai','QRIS','Transfer ke Bank','Asuransi') DEFAULT NULL,
  `status_pembayaran` enum('Belum bayar','Lunas','Klaim asuransi') DEFAULT 'Belum bayar',
  `waktu_bayar` datetime DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp() COMMENT 'Untuk fungsi accounting (audit)',
  `created_by` varchar(100) DEFAULT NULL COMMENT 'Untuk fungsi accounting (audit)',
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT 'Untuk fungsi accounting (audit)',
  `updated_by` varchar(100) DEFAULT NULL COMMENT 'Untuk fungsi accounting (audit)'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pembayaran`
--

INSERT INTO `pembayaran` (`id_pembayaran`, `id_kunjungan`, `id_admin`, `total_tagihan`, `metode_pembayaran`, `status_pembayaran`, `waktu_bayar`, `created_at`, `created_by`, `updated_at`, `updated_by`) VALUES
(1, 1, 1, 50000.00, 'Tunai', 'Lunas', NULL, '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL),
(2, 2, 1, 75000.00, 'QRIS', 'Lunas', NULL, '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL),
(3, 3, 1, 100000.00, 'Transfer ke Bank', 'Lunas', NULL, '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL),
(4, 4, 1, 150000.00, 'Asuransi', 'Klaim asuransi', NULL, '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL),
(5, 5, 1, 45000.00, 'Tunai', 'Lunas', NULL, '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `pemeriksaan`
--

CREATE TABLE `pemeriksaan` (
  `id_pemeriksaan` int(11) NOT NULL,
  `id_kunjungan` int(11) NOT NULL,
  `keluhan` text DEFAULT NULL COMMENT 'Keluhan dari pengguna',
  `diagnosa` text DEFAULT NULL COMMENT 'Diagnosa dari dokter',
  `catatan_dokter` text DEFAULT NULL,
  `waktu_periksa` datetime DEFAULT NULL COMMENT 'Mengambil tanggal dan waktu saat pemeriksaan selesai',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp() COMMENT 'Untuk fungsi accounting (audit)',
  `created_by` varchar(100) DEFAULT NULL COMMENT 'Untuk fungsi accounting (audit)',
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT 'Untuk fungsi accounting (audit)',
  `updated_by` varchar(100) DEFAULT NULL COMMENT 'Untuk fungsi accounting (audit)'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pemeriksaan`
--

INSERT INTO `pemeriksaan` (`id_pemeriksaan`, `id_kunjungan`, `keluhan`, `diagnosa`, `catatan_dokter`, `waktu_periksa`, `created_at`, `created_by`, `updated_at`, `updated_by`) VALUES
(1, 1, 'Demam', 'Flu', NULL, NULL, '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL),
(2, 2, 'Sakit Perut', 'Gastritis', NULL, NULL, '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL),
(3, 3, 'Batuk', 'Bronkitis', NULL, NULL, '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL),
(4, 4, 'Pusing', 'Hipertensi', NULL, NULL, '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL),
(5, 5, 'Nyeri Otot', 'Myalgia', NULL, NULL, '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `resep`
--

CREATE TABLE `resep` (
  `id_resep` int(11) NOT NULL,
  `id_pemeriksaan` int(11) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp() COMMENT 'Untuk fungsi accounting (audit)',
  `created_by` varchar(100) DEFAULT NULL COMMENT 'Untuk fungsi accounting (audit)',
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT 'Untuk fungsi accounting (audit)',
  `updated_by` varchar(100) DEFAULT NULL COMMENT 'Untuk fungsi accounting (audit)'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `resep`
--

INSERT INTO `resep` (`id_resep`, `id_pemeriksaan`, `created_at`, `created_by`, `updated_at`, `updated_by`) VALUES
(1, 1, '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL),
(2, 2, '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL),
(3, 3, '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL),
(4, 4, '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL),
(5, 5, '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `resep_detail`
--

CREATE TABLE `resep_detail` (
  `id_detail` int(11) NOT NULL,
  `id_resep` int(11) NOT NULL,
  `id_obat` int(11) NOT NULL,
  `jumlah` int(11) DEFAULT 1,
  `instruksi_konsumsi` text DEFAULT NULL,
  `kerahasiaan` enum('Dengan resep dokter','Obat umum') NOT NULL COMMENT 'Obat umum berarti obat yang di-edar bebas',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp() COMMENT 'Untuk fungsi accounting (audit)',
  `created_by` varchar(100) DEFAULT NULL COMMENT 'Untuk fungsi accounting (audit)',
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT 'Untuk fungsi accounting (audit)',
  `updated_by` varchar(100) DEFAULT NULL COMMENT 'Untuk fungsi accounting (audit)'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `resep_detail`
--

INSERT INTO `resep_detail` (`id_detail`, `id_resep`, `id_obat`, `jumlah`, `instruksi_konsumsi`, `kerahasiaan`, `created_at`, `created_by`, `updated_at`, `updated_by`) VALUES
(1, 1, 1, 3, '3x1', 'Obat umum', '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL),
(2, 2, 3, 2, '2x1', 'Dengan resep dokter', '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL),
(3, 3, 2, 5, '3x1', 'Dengan resep dokter', '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL),
(4, 4, 5, 2, '2x1', 'Dengan resep dokter', '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL),
(5, 5, 4, 10, '1x1', 'Obat umum', '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id_user` int(11) NOT NULL,
  `nama_kategori` varchar(50) NOT NULL,
  `nomor_induk_kependudukan` varchar(16) NOT NULL,
  `nama_lengkap` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL COMMENT 'Akan menggunakan fungsi HASHBYTES("SHA2_512","PASSWORD")',
  `tanggal_lahir` date DEFAULT NULL,
  `alamat` text DEFAULT NULL,
  `no_hp` varchar(15) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp() COMMENT 'Untuk fungsi accounting (audit)',
  `created_by` varchar(100) DEFAULT NULL COMMENT 'Untuk fungsi accounting (audit)',
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT 'Untuk fungsi accounting (audit)',
  `updated_by` varchar(100) DEFAULT NULL COMMENT 'Untuk fungsi accounting (audit)'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id_user`, `nama_kategori`, `nomor_induk_kependudukan`, `nama_lengkap`, `password`, `tanggal_lahir`, `alamat`, `no_hp`, `created_at`, `created_by`, `updated_at`, `updated_by`) VALUES
(1, 'Admin', '1234567890123451', 'Admin Satu', '911b0a07a8cacfebc5f1f45596d67017136c950499fa5b4ff6faffa031f3cec7f197853d1660712c154e1f59c60f682e34ea9b5cbd2d8d5adb0c834f963f30de', '1990-01-01', 'Jakarta', '081111111111', '2026-05-30 01:09:22', NULL, '2026-05-30 01:09:22', NULL),
(2, 'Dokter', '1234567890123452', 'Dr. Budi Sp.A', '911b0a07a8cacfebc5f1f45596d67017136c950499fa5b4ff6faffa031f3cec7f197853d1660712c154e1f59c60f682e34ea9b5cbd2d8d5adb0c834f963f30de', '1985-05-10', 'Bandung', '081222222222', '2026-05-30 01:09:22', NULL, '2026-05-30 01:09:22', NULL),
(3, 'Dokter', '1234567890123453', 'Dr. Siti Sp.PD', '911b0a07a8cacfebc5f1f45596d67017136c950499fa5b4ff6faffa031f3cec7f197853d1660712c154e1f59c60f682e34ea9b5cbd2d8d5adb0c834f963f30de', '1988-06-15', 'Surabaya', '081333333333', '2026-05-30 01:09:22', NULL, '2026-05-30 01:09:22', NULL),
(4, 'Pasien', '1234567890123454', 'Pasien A', '911b0a07a8cacfebc5f1f45596d67017136c950499fa5b4ff6faffa031f3cec7f197853d1660712c154e1f59c60f682e34ea9b5cbd2d8d5adb0c834f963f30de', '2000-02-20', 'Medan', '081444444444', '2026-05-30 01:09:22', NULL, '2026-05-30 01:09:22', NULL),
(5, 'Pasien', '1234567890123455', 'Pasien B', '911b0a07a8cacfebc5f1f45596d67017136c950499fa5b4ff6faffa031f3cec7f197853d1660712c154e1f59c60f682e34ea9b5cbd2d8d5adb0c834f963f30de', '1995-03-30', 'Makassar', '081555555555', '2026-05-30 01:09:22', NULL, '2026-05-30 01:09:22', NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id_admin`),
  ADD KEY `id_user` (`id_user`);

--
-- Indexes for table `alur_layanan`
--
ALTER TABLE `alur_layanan`
  ADD PRIMARY KEY (`id_alur`);

--
-- Indexes for table `dokter`
--
ALTER TABLE `dokter`
  ADD PRIMARY KEY (`id_dokter`),
  ADD KEY `id_user` (`id_user`);

--
-- Indexes for table `kategori_pengguna`
--
ALTER TABLE `kategori_pengguna`
  ADD PRIMARY KEY (`nama_kategori`);

--
-- Indexes for table `kunjungan_layanan`
--
ALTER TABLE `kunjungan_layanan`
  ADD PRIMARY KEY (`id_kunjungan`),
  ADD KEY `id_pasien` (`id_pasien`),
  ADD KEY `id_dokter` (`id_dokter`);

--
-- Indexes for table `obat`
--
ALTER TABLE `obat`
  ADD PRIMARY KEY (`id_obat`);

--
-- Indexes for table `pasien`
--
ALTER TABLE `pasien`
  ADD PRIMARY KEY (`id_pasien`),
  ADD KEY `id_user` (`id_user`);

--
-- Indexes for table `pembayaran`
--
ALTER TABLE `pembayaran`
  ADD PRIMARY KEY (`id_pembayaran`),
  ADD KEY `id_kunjungan` (`id_kunjungan`),
  ADD KEY `id_admin` (`id_admin`);

--
-- Indexes for table `pemeriksaan`
--
ALTER TABLE `pemeriksaan`
  ADD PRIMARY KEY (`id_pemeriksaan`),
  ADD KEY `id_kunjungan` (`id_kunjungan`);

--
-- Indexes for table `resep`
--
ALTER TABLE `resep`
  ADD PRIMARY KEY (`id_resep`),
  ADD KEY `id_pemeriksaan` (`id_pemeriksaan`);

--
-- Indexes for table `resep_detail`
--
ALTER TABLE `resep_detail`
  ADD PRIMARY KEY (`id_detail`),
  ADD KEY `id_resep` (`id_resep`),
  ADD KEY `id_obat` (`id_obat`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id_user`),
  ADD KEY `nama_kategori` (`nama_kategori`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admin`
--
ALTER TABLE `admin`
  MODIFY `id_admin` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `alur_layanan`
--
ALTER TABLE `alur_layanan`
  MODIFY `id_alur` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `dokter`
--
ALTER TABLE `dokter`
  MODIFY `id_dokter` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `kunjungan_layanan`
--
ALTER TABLE `kunjungan_layanan`
  MODIFY `id_kunjungan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `obat`
--
ALTER TABLE `obat`
  MODIFY `id_obat` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `pasien`
--
ALTER TABLE `pasien`
  MODIFY `id_pasien` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `pembayaran`
--
ALTER TABLE `pembayaran`
  MODIFY `id_pembayaran` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `pemeriksaan`
--
ALTER TABLE `pemeriksaan`
  MODIFY `id_pemeriksaan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `resep`
--
ALTER TABLE `resep`
  MODIFY `id_resep` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `resep_detail`
--
ALTER TABLE `resep_detail`
  MODIFY `id_detail` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id_user` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `admin`
--
ALTER TABLE `admin`
  ADD CONSTRAINT `admin_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `users` (`id_user`);

--
-- Constraints for table `dokter`
--
ALTER TABLE `dokter`
  ADD CONSTRAINT `dokter_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `users` (`id_user`);

--
-- Constraints for table `kunjungan_layanan`
--
ALTER TABLE `kunjungan_layanan`
  ADD CONSTRAINT `kunjungan_layanan_ibfk_1` FOREIGN KEY (`id_pasien`) REFERENCES `pasien` (`id_pasien`),
  ADD CONSTRAINT `kunjungan_layanan_ibfk_2` FOREIGN KEY (`id_dokter`) REFERENCES `dokter` (`id_dokter`);

--
-- Constraints for table `pasien`
--
ALTER TABLE `pasien`
  ADD CONSTRAINT `pasien_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `users` (`id_user`);

--
-- Constraints for table `pembayaran`
--
ALTER TABLE `pembayaran`
  ADD CONSTRAINT `pembayaran_ibfk_1` FOREIGN KEY (`id_kunjungan`) REFERENCES `kunjungan_layanan` (`id_kunjungan`),
  ADD CONSTRAINT `pembayaran_ibfk_2` FOREIGN KEY (`id_admin`) REFERENCES `admin` (`id_admin`);

--
-- Constraints for table `pemeriksaan`
--
ALTER TABLE `pemeriksaan`
  ADD CONSTRAINT `id_kunjungan` FOREIGN KEY (`id_kunjungan`) REFERENCES `kunjungan_layanan` (`id_kunjungan`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `resep`
--
ALTER TABLE `resep`
  ADD CONSTRAINT `resep_ibfk_1` FOREIGN KEY (`id_pemeriksaan`) REFERENCES `pemeriksaan` (`id_pemeriksaan`);

--
-- Constraints for table `resep_detail`
--
ALTER TABLE `resep_detail`
  ADD CONSTRAINT `resep_detail_ibfk_1` FOREIGN KEY (`id_resep`) REFERENCES `resep` (`id_resep`),
  ADD CONSTRAINT `resep_detail_ibfk_2` FOREIGN KEY (`id_obat`) REFERENCES `obat` (`id_obat`);

--
-- Constraints for table `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `users_ibfk_1` FOREIGN KEY (`nama_kategori`) REFERENCES `kategori_pengguna` (`nama_kategori`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
