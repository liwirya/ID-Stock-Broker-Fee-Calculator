# Changelog — Kalkulator Fee Saham BEI

Seluruh perubahan, revisi tarif sekuritas, dan pembaruan fitur dicatat di file ini dan digunakan otomatis untuk Release Notes pada GitHub Releases.

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
