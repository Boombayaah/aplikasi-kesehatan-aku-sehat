INSERT INTO `admin` (`id_admin`, `id_user`, `created_at`, `created_by`, `updated_at`, `updated_by`) VALUES
(1, 1, '2026-05-30 01:09:22', NULL, '2026-05-30 01:09:22', NULL);

INSERT INTO `alur_layanan` (`id_alur`, `nama_tahapan`, `deskripsi`, `dokumen`, `created_at`, `created_by`, `updated_at`, `updated_by`) VALUES
(6, 'Buat Jadwal', 'Berikut adalah proses untuk melakukan pembuatan jadwal dengan aplikasi mobile \"AkuSehat\".', 'buat_jadwal.png', '2026-05-30 12:35:47', NULL, '2026-05-30 12:35:47', NULL),
(7, 'Klaim Asuransi', 'Berikut adalah langkah-langkah yang dapat dilakukan pengguna untuk dapat melakukan claim asuransi.', 'klaim_asuransi.png', '2026-05-30 12:35:47', NULL, '2026-05-30 12:35:47', NULL),
(8, 'Konsultasi', 'Berikut adalah flowchart untuk dapat melakukan konsultasi dengan dokter melalui aplikasi mobile \"AkuSehat\"', 'konsultasi.png', '2026-05-30 12:35:47', NULL, '2026-05-30 12:35:47', NULL);

INSERT INTO `dokter` (`id_dokter`, `id_user`, `spesialisasi`, `no_izin_praktik`, `created_at`, `created_by`, `updated_at`, `updated_by`) VALUES
(1, 2, 'Anak', 'SIP-001', '2026-05-30 01:09:22', NULL, '2026-05-30 01:09:22', NULL),
(2, 3, 'Penyakit Dalam', 'SIP-002', '2026-05-30 01:09:22', NULL, '2026-05-30 01:09:22', NULL);

INSERT INTO `kategori_pengguna` (`nama_kategori`, `created_at`, `created_by`, `updated_at`, `updated_by`) VALUES
('Admin', '2026-05-30 01:09:02', NULL, '2026-05-30 01:09:02', NULL),
('Dokter', '2026-05-30 01:09:02', NULL, '2026-05-30 01:09:02', NULL),
('Pasien', '2026-05-30 01:09:02', NULL, '2026-05-30 01:09:02', NULL);

INSERT INTO `kunjungan_layanan` (`id_kunjungan`, `id_pasien`, `id_dokter`, `tanggal_kunjungan`, `waktu_kunjungan`, `nomor_antrean`, `waktu_checkin`, `dokumen_checkin`, `status_layanan`, `created_at`, `created_by`, `updated_at`, `updated_by`) VALUES
(1, 1, 1, '2026-05-30', '08:00:00', 1, '08:10:00', '', 'Dalam Antrean', '2026-05-30 01:09:31', NULL, '2026-06-03 10:14:56', NULL),
(2, 2, 2, '2026-05-30', '08:00:00', 2, '08:15:00', '', 'Dalam Antrean', '2026-05-30 01:09:31', NULL, '2026-05-30 04:32:48', NULL),
(3, 1, 1, '2026-05-31', '09:00:00', 0, '00:00:00', '', 'Belum CheckIn', '2026-05-30 01:09:31', NULL, '2026-05-30 04:32:48', NULL),
(4, 2, 2, '2026-05-31', '09:00:00', 0, '00:00:00', '', 'Belum CheckIn', '2026-05-30 01:09:31', NULL, '2026-05-30 04:32:48', NULL),
(5, 1, 1, '2026-06-01', '09:00:00', 0, '00:00:00', '', 'Belum CheckIn', '2026-05-30 01:09:31', NULL, '2026-05-30 04:32:48', NULL),
(91, 24, 2, '2026-06-03', '18:00:00', 1, '18:48:03', 'NUSNOES/03_06_2026', 'Selesai', '2026-06-03 11:47:06', NULL, '2026-06-03 12:05:23', NULL),
(92, 25, 1, '2026-06-03', '18:00:00', 1, '18:50:33', 'jblorjeblor55/03_06_2026', 'Selesai', '2026-06-03 11:50:25', NULL, '2026-06-03 12:05:52', NULL),
(93, 24, 2, '2026-06-03', '19:00:00', 2, '19:04:02', 'NUSNOES/03_06_2026', 'Selesai', '2026-06-03 12:03:52', NULL, '2026-06-03 12:06:08', NULL),
(96, 25, 1, '2026-06-03', '19:00:00', 2, '19:21:57', 'jblorjeblor55/03_06_2026', 'Selesai', '2026-06-03 12:21:46', NULL, '2026-06-03 15:06:22', NULL),
(97, 24, 1, '2026-06-03', '19:00:00', 3, '19:23:11', 'NUSNOES/03_06_2026', 'Selesai', '2026-06-03 12:23:02', NULL, '2026-06-03 15:57:57', NULL),
(102, 24, 2, '2026-06-04', '14:00:00', 1, '14:42:44', 'NUSNOES/04_06_2026', 'Selesai', '2026-06-04 07:42:09', NULL, '2026-06-04 07:45:29', NULL),
(103, 24, 2, '2026-06-04', '15:00:00', 2, '15:06:13', 'NUSNOES/04_06_2026', 'Selesai', '2026-06-04 08:06:01', NULL, '2026-06-04 08:18:50', NULL),
(105, 24, 2, '2026-06-04', '15:00:00', 3, '15:19:22', 'NUSNOES/04_06_2026', 'Selesai', '2026-06-04 08:19:14', NULL, '2026-06-04 08:25:23', NULL),
(106, 24, 1, '2026-06-04', '17:00:00', 1, '17:31:23', 'NUSNOES/04_06_2026', 'Selesai', '2026-06-04 10:31:10', NULL, '2026-06-04 10:47:43', NULL),
(107, 24, 2, '2026-06-04', '18:00:00', 4, '18:16:36', 'NUSNOES/04_06_2026', 'Selesai', '2026-06-04 11:16:21', NULL, '2026-06-04 12:31:53', NULL),
(110, 24, 2, '2026-06-04', '18:00:00', 5, '18:53:37', 'NUSNOES/04_06_2026', 'Selesai', '2026-06-04 11:53:27', NULL, '2026-06-04 12:32:02', NULL),
(111, 24, 2, '2026-06-04', '19:00:00', 6, '19:32:19', 'NUSNOES/04_06_2026', 'Selesai', '2026-06-04 12:32:08', NULL, '2026-06-04 13:15:53', NULL),
(112, 24, 1, '2026-06-04', '20:00:00', 2, '20:15:13', 'NUSNOES/04_06_2026', 'Selesai', '2026-06-04 13:15:01', NULL, '2026-06-04 13:50:01', NULL),
(113, 24, 2, '2026-06-04', '20:00:00', 7, '20:50:14', 'NUSNOES/04_06_2026', 'Selesai', '2026-06-04 13:50:05', NULL, '2026-06-04 15:25:12', NULL),
(114, 24, 1, '2026-06-04', '20:00:20', 8, '20:25:20', 'varevarvar', 'Selesai', '2026-06-04 15:26:24', NULL, '2026-06-06 06:21:50', NULL),
(115, 24, 1, '2026-06-06', '13:00:00', 0, '00:00:00', '', 'Selesai', '2026-06-06 06:20:08', NULL, '2026-06-06 06:21:34', NULL),
(116, 24, 2, '2026-06-06', '13:00:00', 1, '13:20:48', 'NUSNOES/06_06_2026', 'Selesai', '2026-06-06 06:20:08', NULL, '2026-06-06 06:32:10', NULL),
(117, 24, 2, '2026-06-06', '13:00:00', 2, '13:31:35', 'NUSNOES/06_06_2026', 'Selesai', '2026-06-06 06:31:07', NULL, '2026-06-06 06:32:37', NULL);

INSERT INTO `obat` (`id_obat`, `nama_obat`, `stok`, `harga`, `created_at`, `created_by`, `updated_at`, `updated_by`) VALUES
(1, 'Paracetamol', 100, 5000.00, '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL),
(2, 'Amoxicillin', 50, 15000.00, '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL),
(3, 'Antasida', 75, 8000.00, '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL),
(4, 'Vitamin C', 200, 2000.00, '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL),
(5, 'Ibuprofen', 60, 10000.00, '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL),
(6, 'Biaya Konsultasi Dokter', 999999, 250000.00, '2026-06-03 13:40:27', NULL, '2026-06-03 13:40:27', NULL);

INSERT INTO `pasien` (`id_pasien`, `id_user`, `no_bpjs_asuransi`, `jenis_kelamin`, `created_at`, `created_by`, `updated_at`, `updated_by`) VALUES
(1, 4, 'BPJS-001', 'L', '2026-05-30 01:09:22', NULL, '2026-05-30 01:09:22', NULL),
(2, 5, 'BPJS-002', 'P', '2026-05-30 01:09:22', NULL, '2026-05-30 01:09:22', NULL),
(3, 6, NULL, 'L', '2026-05-31 12:01:29', NULL, '2026-05-31 12:01:29', NULL),
(4, 7, NULL, 'P', '2026-05-31 12:03:45', NULL, '2026-05-31 12:03:45', NULL),
(5, 8, NULL, 'P', '2026-05-31 12:22:42', NULL, '2026-05-31 12:22:42', NULL),
(6, 9, NULL, 'L', '2026-05-31 13:35:26', NULL, '2026-05-31 13:35:26', NULL),
(7, 10, NULL, 'P', '2026-05-31 13:37:03', NULL, '2026-05-31 13:37:03', NULL),
(8, 11, '', 'L', '2026-05-31 14:50:29', NULL, '2026-05-31 14:50:48', NULL),
(9, 12, NULL, 'L', '2026-05-31 15:01:19', NULL, '2026-05-31 15:01:19', NULL),
(10, 13, NULL, 'L', '2026-05-31 15:21:49', NULL, '2026-05-31 15:21:49', NULL),
(11, 14, NULL, 'L', '2026-05-31 15:43:39', NULL, '2026-05-31 15:43:39', NULL),
(12, 15, NULL, 'L', '2026-05-31 16:34:24', NULL, '2026-05-31 16:34:24', NULL),
(13, 16, NULL, 'L', '2026-06-01 03:52:59', NULL, '2026-06-01 03:52:59', NULL),
(14, 17, NULL, 'L', '2026-06-01 04:45:16', NULL, '2026-06-01 04:45:16', NULL),
(15, 18, NULL, 'L', '2026-06-01 04:52:28', NULL, '2026-06-01 04:52:28', NULL),
(16, 19, NULL, 'L', '2026-06-01 06:23:25', NULL, '2026-06-01 06:23:25', NULL),
(17, 20, NULL, 'L', '2026-06-01 06:32:58', NULL, '2026-06-01 06:32:58', NULL),
(18, 21, '', 'L', '2026-06-01 06:46:49', NULL, '2026-06-01 06:47:09', NULL),
(19, 22, NULL, 'L', '2026-06-01 06:57:07', NULL, '2026-06-01 06:57:07', NULL),
(20, 23, NULL, 'P', '2026-06-02 11:56:33', NULL, '2026-06-02 11:56:33', NULL),
(21, 24, NULL, 'L', '2026-06-02 12:14:01', NULL, '2026-06-02 12:14:01', NULL),
(22, 25, NULL, 'L', '2026-06-02 12:26:39', NULL, '2026-06-02 12:26:39', NULL),
(23, 26, NULL, 'L', '2026-06-03 09:32:07', NULL, '2026-06-03 09:32:07', NULL),
(24, 27, 'bpjs-11', 'L', '2026-06-03 10:10:15', NULL, '2026-06-06 06:41:06', NULL),
(25, 28, NULL, 'L', '2026-06-03 11:49:50', NULL, '2026-06-03 11:49:50', NULL);

INSERT INTO `pembayaran` (`id_pembayaran`, `id_kunjungan`, `id_admin`, `total_tagihan`, `metode_pembayaran`, `status_pembayaran`, `waktu_bayar`, `created_at`, `created_by`, `updated_at`, `updated_by`) VALUES
(1, 1, 1, 265000.00, 'Tunai', 'Lunas', '2026-05-30 13:21:40', '2026-05-30 01:09:31', NULL, '2026-06-04 06:21:49', NULL),
(2, 2, 1, 266000.00, 'QRIS', 'Lunas', '2026-05-30 13:21:50', '2026-05-30 01:09:31', NULL, '2026-06-04 06:21:54', NULL),
(8, 105, 1, 250000.00, '', 'Belum bayar', NULL, '2026-06-04 08:19:22', NULL, '2026-06-04 08:19:22', NULL),
(9, 106, 1, 250000.00, '', 'Belum bayar', NULL, '2026-06-04 10:31:23', NULL, '2026-06-04 10:31:23', NULL),
(10, 107, 1, 250000.00, '', 'Belum bayar', NULL, '2026-06-04 11:16:36', NULL, '2026-06-04 11:16:36', NULL),
(11, 110, 1, 250000.00, '', 'Belum bayar', NULL, '2026-06-04 11:53:37', NULL, '2026-06-04 11:53:37', NULL),
(12, 111, 1, 250000.00, '', 'Belum bayar', NULL, '2026-06-04 12:32:19', NULL, '2026-06-04 12:32:19', NULL),
(13, 112, 1, 250000.00, '', 'Belum bayar', NULL, '2026-06-04 13:15:13', NULL, '2026-06-04 13:15:13', NULL),
(14, 113, 1, 284000.00, NULL, 'Belum bayar', NULL, '2026-06-04 13:55:21', NULL, '2026-06-04 13:55:21', NULL),
(15, 114, 1, 280000.00, 'QRIS', 'Belum bayar', NULL, '2026-06-04 15:41:42', NULL, '2026-06-04 16:56:31', NULL),
(16, 116, 1, 280000.00, '', 'Belum bayar', NULL, '2026-06-06 06:23:54', NULL, '2026-06-06 06:27:45', NULL),
(17, 117, 1, 260000.00, NULL, 'Belum bayar', NULL, '2026-06-06 06:33:58', NULL, '2026-06-06 06:39:02', NULL);

INSERT INTO `pemeriksaan` (`id_pemeriksaan`, `id_kunjungan`, `keluhan`, `diagnosa`, `catatan_dokter`, `waktu_periksa`, `created_at`, `created_by`, `updated_at`, `updated_by`) VALUES
(1, 1, 'Demam', 'Flu', NULL, NULL, '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL),
(2, 2, 'Sakit Perut', 'Gastritis', NULL, NULL, '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL),
(3, 3, 'Batuk', 'Bronkitis', NULL, NULL, '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL),
(4, 4, 'Pusing', 'Hipertensi', NULL, NULL, '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL),
(5, 5, 'Nyeri Otot', 'Myalgia', NULL, NULL, '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL),
(6, 102, NULL, NULL, NULL, NULL, '2026-06-04 07:42:44', NULL, '2026-06-04 07:42:44', NULL),
(7, 103, NULL, NULL, NULL, NULL, '2026-06-04 08:06:13', NULL, '2026-06-04 08:06:13', NULL),
(8, 105, NULL, NULL, NULL, NULL, '2026-06-04 08:19:22', NULL, '2026-06-04 08:19:22', NULL),
(9, 106, NULL, NULL, NULL, NULL, '2026-06-04 10:31:23', NULL, '2026-06-04 10:31:23', NULL),
(10, 107, NULL, NULL, NULL, NULL, '2026-06-04 11:16:36', NULL, '2026-06-04 11:16:36', NULL),
(11, 110, NULL, NULL, NULL, NULL, '2026-06-04 11:53:37', NULL, '2026-06-04 11:53:37', NULL),
(12, 111, NULL, NULL, NULL, NULL, '2026-06-04 12:32:19', NULL, '2026-06-04 12:32:19', NULL),
(13, 112, NULL, NULL, NULL, NULL, '2026-06-04 13:15:13', NULL, '2026-06-04 13:15:13', NULL),
(14, 113, 'SAKIT HATI', 'PUTUS CYNTA', NULL, NULL, '2026-06-04 13:57:02', NULL, '2026-06-04 13:57:02', NULL),
(15, 113, 'sakit fantat', 'ambeien', 'jangan aneh2', '2026-06-04 20:37:27', '2026-06-04 15:38:04', NULL, '2026-06-04 15:38:04', NULL),
(16, 116, 'sakit fantat2', 'ambeien', 'kurang2 in dah ', NULL, '2026-06-06 06:25:58', NULL, '2026-06-06 06:25:58', NULL),
(17, 117, 'SAKIT HATI', 'putus cinta', 'sehat2', '2026-06-06 13:34:33', '2026-06-06 06:35:06', NULL, '2026-06-06 06:35:06', NULL);

INSERT INTO `resep` (`id_resep`, `id_pemeriksaan`, `created_at`, `created_by`, `updated_at`, `updated_by`) VALUES
(1, 1, '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL),
(2, 2, '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL),
(3, 3, '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL),
(4, 4, '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL),
(5, 5, '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL),
(6, 6, '2026-06-04 07:42:44', NULL, '2026-06-04 07:42:44', NULL),
(7, 7, '2026-06-04 08:06:13', NULL, '2026-06-04 08:06:13', NULL),
(8, 8, '2026-06-04 08:19:22', NULL, '2026-06-04 08:19:22', NULL),
(9, 9, '2026-06-04 10:31:23', NULL, '2026-06-04 10:31:23', NULL),
(10, 10, '2026-06-04 11:16:36', NULL, '2026-06-04 11:16:36', NULL),
(11, 11, '2026-06-04 11:53:37', NULL, '2026-06-04 11:53:37', NULL),
(12, 12, '2026-06-04 12:32:19', NULL, '2026-06-04 12:32:19', NULL),
(13, 13, '2026-06-04 13:15:13', NULL, '2026-06-04 13:15:13', NULL),
(14, 14, '2026-06-04 13:58:43', NULL, '2026-06-04 13:58:43', NULL),
(15, 15, '2026-06-04 15:39:08', NULL, '2026-06-04 15:39:08', NULL),
(16, 16, '2026-06-06 06:26:25', NULL, '2026-06-06 06:26:25', NULL),
(17, 17, '2026-06-06 06:35:29', NULL, '2026-06-06 06:35:29', NULL);

INSERT INTO `resep_detail` (`id_detail`, `id_resep`, `id_obat`, `jumlah`, `instruksi_konsumsi`, `kerahasiaan`, `created_at`, `created_by`, `updated_at`, `updated_by`) VALUES
(1, 1, 1, 3, '3x1', 'Obat umum', '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL),
(2, 2, 3, 2, '2x1', 'Dengan resep dokter', '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL),
(3, 3, 2, 5, '3x1', 'Dengan resep dokter', '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL),
(4, 4, 5, 2, '2x1', 'Dengan resep dokter', '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL),
(5, 5, 4, 10, '1x1', 'Obat umum', '2026-05-30 01:09:31', NULL, '2026-05-30 01:09:31', NULL),
(6, 6, 1, 2, 'jangan banyak banyak yah', 'Obat umum', '2026-06-04 07:44:54', NULL, '2026-06-04 07:44:54', NULL),
(7, 8, 1, 2, '3 x 1 sesudah eat', 'Obat umum', '2026-06-04 08:23:24', NULL, '2026-06-04 08:23:24', NULL),
(9, 9, 6, 1, 'Layanan Poliklinik', 'Dengan resep dokter', '2026-06-04 10:31:23', NULL, '2026-06-04 10:31:23', NULL),
(11, 10, 6, 1, 'Layanan Poliklinik', 'Dengan resep dokter', '2026-06-04 11:16:36', NULL, '2026-06-04 11:16:36', NULL),
(12, 11, 6, 1, 'Layanan Poliklinik', 'Dengan resep dokter', '2026-06-04 11:53:37', NULL, '2026-06-04 11:53:37', NULL),
(13, 12, 6, 1, 'Layanan Poliklinik', 'Dengan resep dokter', '2026-06-04 12:32:19', NULL, '2026-06-04 12:32:19', NULL),
(14, 13, 6, 1, 'Layanan Poliklinik', 'Dengan resep dokter', '2026-06-04 13:15:13', NULL, '2026-06-04 13:15:13', NULL),
(15, 14, 1, 2, NULL, '', '2026-06-04 13:59:31', NULL, '2026-06-04 13:59:31', NULL),
(16, 14, 1, 1, NULL, 'Obat umum', '2026-06-04 14:00:01', NULL, '2026-06-04 14:00:01', NULL),
(17, 15, 1, 2, '2x3', 'Obat umum', '2026-06-04 15:39:56', NULL, '2026-06-04 15:39:56', NULL),
(18, 16, 2, 2, '2x24jam', 'Obat umum', '2026-06-06 06:27:18', NULL, '2026-06-06 06:27:18', NULL),
(19, 17, 1, 2, '111', 'Obat umum', '2026-06-06 06:36:38', NULL, '2026-06-06 06:38:36', NULL);

INSERT INTO `users` (`id_user`, `nama_kategori`, `nomor_induk_kependudukan`, `nama_lengkap`, `password`, `tanggal_lahir`, `alamat`, `no_hp`, `created_at`, `created_by`, `updated_at`, `updated_by`) VALUES
(1, 'Admin', '1234567890123451', 'Admin Satu', '911b0a07a8cacfebc5f1f45596d67017136c950499fa5b4ff6faffa031f3cec7f197853d1660712c154e1f59c60f682e34ea9b5cbd2d8d5adb0c834f963f30de', '1990-01-01', 'Jakarta', '081111111111', '2026-05-30 01:09:22', NULL, '2026-05-30 01:09:22', NULL),
(2, 'Dokter', '1234567890123452', 'Dr. Budi Sp.A', '911b0a07a8cacfebc5f1f45596d67017136c950499fa5b4ff6faffa031f3cec7f197853d1660712c154e1f59c60f682e34ea9b5cbd2d8d5adb0c834f963f30de', '1985-05-10', 'Bandung', '081222222222', '2026-05-30 01:09:22', NULL, '2026-05-30 01:09:22', NULL),
(3, 'Dokter', '1234567890123453', 'Dr. Siti Sp.PD', '911b0a07a8cacfebc5f1f45596d67017136c950499fa5b4ff6faffa031f3cec7f197853d1660712c154e1f59c60f682e34ea9b5cbd2d8d5adb0c834f963f30de', '1988-06-15', 'Surabaya', '081333333333', '2026-05-30 01:09:22', NULL, '2026-05-30 01:09:22', NULL),
(4, 'Pasien', '1234567890123454', 'Pasien A', '911b0a07a8cacfebc5f1f45596d67017136c950499fa5b4ff6faffa031f3cec7f197853d1660712c154e1f59c60f682e34ea9b5cbd2d8d5adb0c834f963f30de', '2000-02-20', 'Medan', '081444444444', '2026-05-30 01:09:22', NULL, '2026-05-30 01:09:22', NULL),
(5, 'Pasien', '1234567890123455', 'Pasien B', '911b0a07a8cacfebc5f1f45596d67017136c950499fa5b4ff6faffa031f3cec7f197853d1660712c154e1f59c60f682e34ea9b5cbd2d8d5adb0c834f963f30de', '1995-03-30', 'Makassar', '081555555555', '2026-05-30 01:09:22', NULL, '2026-05-30 01:09:22', NULL),
(6, 'Pasien', '6666666666666666', 'Pasien c', '6bc5bcdf130ca34d04c83478c035c130a47e0ca6194045051da1c654e531bf9d6a581862d6dbfcf049180d0e64fc942ba361a3ca42c891090f571711aade112c', NULL, 'Medan', '6666666666666', '2026-05-31 12:01:29', NULL, '2026-05-31 12:01:29', NULL),
(7, 'Pasien', '1234567890111111', 'Pasien D', 'ce2a429a1c79d4068c0c7e54f5500ce16285d85730cb9ec0b61240f88ef9c870292200a1c069bd57d5e092874567058c91782513763bc30d86fedca63820c482', NULL, 'Summatra', '1111111116666', '2026-05-31 12:03:45', NULL, '2026-05-31 12:03:45', NULL),
(8, 'Pasien', '0999999999999999', 'Pasien E', '7b46cb5dd3030a293e09b32a4aabce6008035f46af61383a36568a1db16807939637de6a952e5a7d0f2eac12ccc657689c5bd600bbc88da9f9d7dc7f284ab7af', NULL, 'Pluto', '7677777777777', '2026-05-31 12:22:42', NULL, '2026-05-31 12:22:42', NULL),
(9, 'Pasien', '7878888888888888', 'Pasien f', '7d6f2cf39188e5a302788a7d9be164449af58b6b05162fd9f8813c5c33e2ddd5a36a999cdf28465da370662b9ee8bcddef815e04de7a157c5c0985db2fdf9b3e', NULL, 'pluto', '9999999999998', '2026-05-31 13:35:26', NULL, '2026-05-31 13:35:26', NULL),
(10, 'Pasien', '0000000000000009', 'pasien G', '34b98e5435b90896f51988970139ece738218aa2f5730fc635f151de377ffa864e090ba9593c0edb05fc7ddcd185de8f73fa9ccb0949a0ce02959dc4508dee79', NULL, 'PLANET LAIN', '0000000000009', '2026-05-31 13:37:03', NULL, '2026-05-31 13:37:03', NULL),
(11, 'Pasien', '1200000000000000', 'Pasien H', '7d6f2cf39188e5a302788a7d9be164449af58b6b05162fd9f8813c5c33e2ddd5a36a999cdf28465da370662b9ee8bcddef815e04de7a157c5c0985db2fdf9b3e', NULL, '..', '0812999999999', '2026-05-31 14:50:29', NULL, '2026-05-31 14:50:29', NULL),
(12, 'Pasien', '1211123333333333', 'Pasien J', '62670d1e1eea06b6c975e12bc8a16131b278f6d7bcbe017b65f854c58476baba86c2082b259fd0c1310935b365dc40f609971b6810b065e528b0b60119e69f61', NULL, '111', '081282007616', '2026-05-31 15:01:19', NULL, '2026-05-31 15:01:19', NULL),
(13, 'Pasien', '1312222222222222', 'KAPALKARAM', '733c8373edc5d58c828d4050aa493529731547eeed2bb9c3ca57da790c61171446adf27ee828e7337791b59a91dff30e9de1ce878b725dc8a4622e1e68c63f07', NULL, '', '2222222222233', '2026-05-31 15:21:49', NULL, '2026-05-31 15:21:49', NULL),
(14, 'Pasien', '1333333333333333', 'Pasien k', '089aad417e22c9c3c0b112c235ea1e0751f931868f125290d93b9cf34727add3912ba17f39dad3b704b51da0ec15e5fd088ba38fea3133ec5855013073fd11fc', NULL, '', '1231231231231', '2026-05-31 15:43:39', NULL, '2026-05-31 15:43:39', NULL),
(15, 'Pasien', '1313333333333333', 'josgandos', 'a118060ba3c0671b36005f785c818fd68e3478a91497019daa8cfa52dffe496445dbdbb4f0db6389056f4d26da4eb6bec5a3e3797de83649afb8f4eec5ace6dd', NULL, 'Makassar', '5555555555555', '2026-05-31 16:34:24', NULL, '2026-05-31 16:34:24', NULL),
(16, 'Pasien', '0000000000000001', 'Cukurukuk', '1f86c769b319d953ab017153897f602b2fac6b73b4e64bf942085bd03c414c203c9030d47f33b937c9a3e30ed3764cf60eecbfd4e2284b736302fa837f8751c4', NULL, '', '0812222222222', '2026-06-01 03:52:59', NULL, '2026-06-01 03:52:59', NULL),
(17, 'Pasien', '8888888888888888', 'Jeblar Jeblor', 'd2e67ffcbf4851b93a92521501f93dcbefb15926dc2217e60bf20b1a37605706460292783e50ceac6cde52a32e8f3c8b8a745f7fb2041ea1837f6695edd8f975', NULL, 'pluto', '8888888888889', '2026-06-01 04:45:16', NULL, '2026-06-01 04:45:16', NULL),
(18, 'Pasien', '1333333333333333', 'PALKOR 55', '62670d1e1eea06b6c975e12bc8a16131b278f6d7bcbe017b65f854c58476baba86c2082b259fd0c1310935b365dc40f609971b6810b065e528b0b60119e69f61', NULL, '', '0812767676767', '2026-06-01 04:52:28', NULL, '2026-06-01 04:52:28', NULL),
(19, 'Pasien', '2222222222222222', 'palkor66', '62670d1e1eea06b6c975e12bc8a16131b278f6d7bcbe017b65f854c58476baba86c2082b259fd0c1310935b365dc40f609971b6810b065e528b0b60119e69f61', NULL, 'pluto', '0811111111111', '2026-06-01 06:23:25', NULL, '2026-06-01 06:23:25', NULL),
(20, 'Pasien', '1222222222222222', 'Pasien Z', '62670d1e1eea06b6c975e12bc8a16131b278f6d7bcbe017b65f854c58476baba86c2082b259fd0c1310935b365dc40f609971b6810b065e528b0b60119e69f61', NULL, 'BEKASI', '0000000000000', '2026-06-01 06:32:58', NULL, '2026-06-01 06:32:58', NULL),
(21, 'Pasien', '1111111111111111', 'Pasien zero', '62670d1e1eea06b6c975e12bc8a16131b278f6d7bcbe017b65f854c58476baba86c2082b259fd0c1310935b365dc40f609971b6810b065e528b0b60119e69f61', NULL, 'Bekasi Barat', '1111111111111', '2026-06-01 06:46:49', NULL, '2026-06-01 06:46:49', NULL),
(22, 'Pasien', '1111111111111111', 'PASIEN L', '6bc5bcdf130ca34d04c83478c035c130a47e0ca6194045051da1c654e531bf9d6a581862d6dbfcf049180d0e64fc942ba361a3ca42c891090f571711aade112c', NULL, '', '0999999999999', '2026-06-01 06:57:07', NULL, '2026-06-01 06:57:07', NULL),
(23, 'Pasien', '1233333333333333', 'nusnus', 'a393e97bb61324d81ba98d84a4814da89931b3fafea3dea91dcac692c99de85c986765a454d3fc7e8c1e23414b2b298617dc0e1c60ced3a1caef020649c5ea97', NULL, 'bEKASEA', '3333333333333', '2026-06-02 11:56:33', NULL, '2026-06-02 11:56:33', NULL),
(24, 'Pasien', '1111111111111111', 'NUSNUS X CUKURUKUK', '7d6f2cf39188e5a302788a7d9be164449af58b6b05162fd9f8813c5c33e2ddd5a36a999cdf28465da370662b9ee8bcddef815e04de7a157c5c0985db2fdf9b3e', NULL, '', '0000000000999', '2026-06-02 12:14:01', NULL, '2026-06-02 12:14:01', NULL),
(25, 'Pasien', '1111111111111111', 'YESKING66', '62670d1e1eea06b6c975e12bc8a16131b278f6d7bcbe017b65f854c58476baba86c2082b259fd0c1310935b365dc40f609971b6810b065e528b0b60119e69f61', NULL, '', '7676767676767', '2026-06-02 12:26:39', NULL, '2026-06-02 12:26:39', NULL),
(26, 'Pasien', '6766666666666666', 'Davin', '733c8373edc5d58c828d4050aa493529731547eeed2bb9c3ca57da790c61171446adf27ee828e7337791b59a91dff30e9de1ce878b725dc8a4622e1e68c63f07', NULL, 'BEKASEA', '1233333333333', '2026-06-03 09:32:07', NULL, '2026-06-03 09:32:07', NULL),
(27, 'Pasien', '3333333333333333', 'NUSNOES', 'a118060ba3c0671b36005f785c818fd68e3478a91497019daa8cfa52dffe496445dbdbb4f0db6389056f4d26da4eb6bec5a3e3797de83649afb8f4eec5ace6dd', '2004-06-07', 'bekasi', '0000000000009', '2026-06-03 10:10:15', NULL, '2026-06-06 07:16:51', NULL),
(28, 'Pasien', '3333333333333333', 'jblorjeblor55', '62670d1e1eea06b6c975e12bc8a16131b278f6d7bcbe017b65f854c58476baba86c2082b259fd0c1310935b365dc40f609971b6810b065e528b0b60119e69f61', NULL, '', '9999999999999', '2026-06-03 11:49:50', NULL, '2026-06-03 11:49:50', NULL);