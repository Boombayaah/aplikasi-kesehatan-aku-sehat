-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 27, 2026 at 11:09 AM
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
  `nomor_antrean` int(11) DEFAULT NULL,
  `status_layanan` enum('Belum CheckIn','Dalam Antrean','Selesai') NOT NULL DEFAULT 'Belum CheckIn',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp() COMMENT 'Untuk fungsi accounting (audit)',
  `created_by` varchar(100) DEFAULT NULL COMMENT 'Untuk fungsi accounting (audit)',
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT 'Untuk fungsi accounting (audit)',
  `updated_by` varchar(100) DEFAULT NULL COMMENT 'Untuk fungsi accounting (audit)'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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
  MODIFY `id_admin` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `alur_layanan`
--
ALTER TABLE `alur_layanan`
  MODIFY `id_alur` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `dokter`
--
ALTER TABLE `dokter`
  MODIFY `id_dokter` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `kunjungan_layanan`
--
ALTER TABLE `kunjungan_layanan`
  MODIFY `id_kunjungan` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `obat`
--
ALTER TABLE `obat`
  MODIFY `id_obat` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `pasien`
--
ALTER TABLE `pasien`
  MODIFY `id_pasien` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `pembayaran`
--
ALTER TABLE `pembayaran`
  MODIFY `id_pembayaran` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `pemeriksaan`
--
ALTER TABLE `pemeriksaan`
  MODIFY `id_pemeriksaan` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `resep`
--
ALTER TABLE `resep`
  MODIFY `id_resep` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `resep_detail`
--
ALTER TABLE `resep_detail`
  MODIFY `id_detail` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id_user` int(11) NOT NULL AUTO_INCREMENT;

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
