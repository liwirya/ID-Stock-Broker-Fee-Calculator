# Changelog — Kalkulator Fee Saham BEI

Seluruh perubahan, revisi tarif sekuritas, dan pembaruan fitur dicatat di file ini dan digunakan otomatis untuk Release Notes pada GitHub Releases.

## [V2.1]

### Revisi Tampilan & Branding
- **Loading Screen & Versi**: Mengubah teks pembuka loading screen menjadi `CREDIT BY LIWIRYA` diikuti baris baru `VERSION: 2.1`, serta menaikkan versi build menjadi `2.1` (`versionCode = 4`).
- **Header & Subjudul Utama**: Mengubah tag atas menjadi `CREATED BY LIWIRYA` dan menyederhanakan subjudul menjadi `Perhitungan Trade Fee`.
- **Watermark Kartu Bagikan**: Mengubah subjudul watermark di bawah `Fee Saham BEI` pada kartu bagikan *Portfolio Flexing* menjadi `CREDIT BY LIWIRYA`.
- **Ikon Aplikasi Baru**: Memperbarui *adaptive launcher icon* (dan seluruh resolusi `mipmap-*`) dengan desain perisai emas & candlestick neon bergaya modern.
- **Logo Sucor Sekuritas (AZ)**: Mengganti ikon Sucor Sekuritas menggunakan logo resmi **SPOT** terbaru yang dibundel langsung di dalam aplikasi.

### Pembaruan Fitur
- **Background Video Looping Tanpa Suara**: Menambahkan latar belakang video dinamis penuh layar yang otomatis berputar terus-menerus dalam kondisi bisu (*muted*) dengan pemutaran mengikuti siklus hidup layar.
- **Pemutar Musik MP3 Persisten (Pojok Kanan Atas)**: Menambahkan tombol ikon musik di pojok kanan atas untuk mengimpor lagu MP3 dari perangkat (tanpa izin penyimpanan luas), menyimpan lagu secara permanen di penyimpanan internal aplikasi, lengkap dengan kontrol *Play/Pause*, *Previous/Next*, *Seekbar*, mode pengulangan (*Off / Semua / 1 Lagu*), dan hapus lagu. Musik tetap berjalan saat aplikasi diminimalkan (*close sementara*) dan otomatis berhenti saat aplikasi ditutup penuh dari *Recents / Close All*.
- **Footer Sosial & Kredit**: Menambahkan tautan ikon **Instagram** (`@mynameisliwirya`), ikon **GitHub** (`liwirya`), serta teks `CREDIT BY LIWIRYA - THANKS FOR USED` di bagian bawah kartu *Transparansi Perhitungan*.

## [V2.0]

### Perbaikan
- **Perbaikan Konflik Update APK**: Menyatukan *signing keystore* build GitHub Actions menggunakan `debug.keystore.base64` yang konsisten dan menambahkan pemulihan otomatis (*corruption handler*) pada DataStore preferensi agar pembaruan aplikasi versi selanjutnya berjalan mulus tanpa gagal pasang atau bentrok data.
- **Tampilan Badge Broker**: Mengubah label statistik jumlah sekuritas dari `38 AB` menjadi `38` agar lebih bersih dan ringkas.

### Pembaruan Fitur
- **Input Kode / Nama Saham**: Menambahkan kolom opsional *Kode / Nama Saham* beserta pilihan cepat ticker populer BEI (`BBCA`, `BBRI`, `BMRI`, `TLKM`, `ANTM`, `BREN`). Nama saham tampil di hasil profit/loss bersih, rincian *Trade Confirmation*, teks salinan, dan kartu bagikan.
- **Animasi Loading Candlestick Saham**: Menambahkan animasi pembuka (*splash overlay*) dan animasi saat menghitung hasil (*calculating card*) dengan grafik *candlestick* naik bergaya Neo-Brutalist.
- **Portfolio Flexing Share Studio**: Mengubah tombol **Bagikan Kartu** menjadi modal interaktif bergaya *Portfolio Flexing* dengan pratinjau langsung, 6 tema visual kartu (3D Candlestick Dark, Neon Arrow, Emerald Light, Neo-Brutalist, Midnight Gold, Cyber Terminal), tombol tampil/sembunyikan nominal Rupiah, serta pintasan **Simpan ke Galeri**, **Instagram Story**, **WhatsApp**, **Facebook**, dan **Lainnya**.

## [V1.0.1]

### Revisi Tarif Fee Sekuritas
- **Panca Global Sekuritas (PG)**: Tarif terverifikasi Beli `0.10%` / Jual `0.20%`.
- **NH Korindo Sekuritas Indonesia (XA)**: Revisi tarif menjadi Beli `0.18%` / Jual `0.28%`.
- **Sinarmas Sekuritas (DH)**: Revisi tarif menjadi Beli `0.15%` / Jual `0.25%`.
- **Panin Sekuritas Tbk (GR)**: Revisi tarif menjadi Beli `0.20%` / Jual `0.30%`.
- **Phillip Sekuritas Indonesia (KK)**: Pembaruan nama resmi & tarif Beli `0.18%` / Jual `0.28%`.
- **Phintraco Sekuritas (AT)**: Revisi tarif menjadi Beli `0.15%` / Jual `0.18%`.

### Penambahan Sekuritas Baru (Anggota Bursa OJK)
- **Paramitra Alfa Sekuritas (PS)** — Beli `0.10%` / Jual `0.20%`
- **Lotus Andalan Sekuritas (YJ)** — Beli `0.15%` / Jual `0.25%`
- **Mega Capital Sekuritas (CD)** — Beli `0.17%` / Jual `0.28%`
- **Minna Padi Investama Sekuritas Tbk (MU)** — Beli `0.25%` / Jual `0.35%`
- **Pacific Sekuritas Indonesia (AP)** — Beli `0.20%` / Jual `0.30%`
- **Pilarmas Investindo Sekuritas (PO)** — Beli `0.18%` / Jual `0.28%`
- **Pluang Maju Sekuritas (RO)** — Beli `0.30%` / Jual `0.30%`
- **Profindo Sekuritas Indonesia (RG)** — Beli `0.18%` / Jual `0.28%`

### Peningkatan UI, Dokumentasi & Rilis
- **Ikon Asli Sekuritas (38 AB OJK)**: Menampilkan ikon resmi dari masing-masing situs sekuritas di sebelah kiri nama broker (dimuat & di-cache otomatis via Coil dengan fallback emblem saat offline).
- **Format Rilis GitHub**: Judul rilis menggunakan format `Kalkulator Fee Saham BEI (V1.0.1)`, tag berbasis nama aplikasi & versi, serta Release Notes otomatis mengikuti catatan perubahan tiap versi.
- **Dokumentasi `README.md`**: Penambahan dokumentasi lengkap proyek, daftar sekuritas, panduan instalasi/build, dan contoh perhitungan transaksi.

## [V1.0.0]

### Rilis Perdana
- Implementasi aplikasi Android native Jetpack Compose dengan gaya **Neo-Brutalist** dan tipografi lokal *Space Grotesk* & *Space Mono*.
- Perhitungan presisi Trade Confirmation BEI: pemecahan otomatis komisi murni broker, PPN Komisi (PMK 131/2024), Levy BEI & KSEI (`0.03%`), Kliring KPEI (`0.01%` Reguler / `0.009%` Negosiasi), PPN Levy (`11%`), PPh Final Jual (`0.10%`), e-Meterai (PMK 134/2021), Minimum Fee harian, dan Harga BEP.
- Dukungan 5 jenis pasar BEI: Pasar Reguler (RG), Pasar Negosiasi (NG), Pasar Tunai (TN), Day Trade/Intraday, dan e-IPO.
- Pencarian cepat daftar sekuritas OJK, penyimpanan broker default offline via DataStore, serta fitur salin dan bagikan ringkasan Trade Confirmation.
