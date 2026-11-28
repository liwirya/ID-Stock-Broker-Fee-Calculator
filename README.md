# Kalkulator Fee Saham BEI

Aplikasi Android native (**Fee Saham BEI**) untuk simulasi dan verifikasi biaya transaksi saham Bursa Efek Indonesia (IDX / BEI) secara presisi mengikuti ketentuan **OJK, BEI, KPEI, KSEI, PMK 131/2024, dan PMK 134/2021**. Aplikasi ini menghitung otomatis komisi murni broker, PPN, *levy* bursa, kliring KPEI, pajak penjualan (*PPh Final*), estimasi *e-Meterai*, harga impas (*Break-Even Point / BEP*), hingga keuntungan/kerugian bersih masuk RDN (*Net P&L*) tanpa penghitungan ganda.

---

## Fitur Utama

- **Perhitungan Trade Confirmation BEI Lengkap**: Memecah otomatis tarif *All-In* maupun *Base Fee* menjadi komisi murni broker, PPN Komisi (`11%` efektif PMK 131/2024), *Levy* BEI & jasa KSEI (`0.03%`), biaya kliring KPEI (`0.01%` Reguler/Tunai & `0.009%` Pasar Negosiasi), PPN *Levy* (`0.0033%`), serta PPh Final penjualan (`0.10%`).
- **5 Jenis Pasar Transaksi BEI**: Mendukung **Pasar Reguler (RG | T+2)**, **Pasar Negosiasi (NG | Satuan Lembar)**, **Pasar Tunai (TN | T+0)**, **Day Trade / Intraday**, dan **Transaksi e-IPO** (`0%` biaya saat penjatahan beli).
- **38 Sekuritas Anggota Bursa OJK + Ikon Asli & Pencarian Cepat**: Dilengkapi ikon resmi masing-masing sekuritas (dimuat dari domain resmi dengan *disk cache* dan fallback emblem saat offline), pencarian instan berdasarkan nama/kode AB/nama aplikasi (`XL`, `YP`, `MOST`, `BIONS`, dll.), penyimpanan broker default otomatis offline, serta 2 mode tarif *Custom* (*All-In* & *Base Fee*).
- **Simulasi *Break-Even Point* (BEP) & Minimum Fee**: Menghitung harga jual minimal impas per lembar dan otomatis menerapkan aturan *minimum fee* harian untuk sekuritas tertentu (seperti Mandiri Sekuritas `Rp 5.000/hari`).
- **Estimasi *e-Meterai* (PMK 134/2021)**: Opsi perhitungan bea meterai `Rp 10.000` untuk nilai transaksi di atas `Rp 10.000.000` (termasuk aturan akumulasi harian untuk *Day Trade*).
- **Input Kode / Nama Saham & Pilihan Cepat**: Pengguna dapat memasukkan kode/nama emiten (misalnya `BBCA`, `BBRI`, `IRSX`) atau memilih ticker cepat sehingga hasil keuntungan/kerugian jelas terikat pada saham yang dihitung.
- **Animasi Loading Candlestick Interaktif**: Dilengkapi animasi pembuka (*splash overlay*) dan animasi kalkulasi grafik *candlestick* naik yang mulus dan selaras dengan tema Neo-Brutalist.
- **Portfolio Flexing Share Studio & Salin Ringkasan**: Satu ketukan untuk menyalin rincian *Trade Confirmation* ke clipboard atau membuka *Portfolio Flexing* dengan 6 tema kartu HD (`1080×1350`), opsi sembunyikan/tampilkan nominal Rupiah, serta tombol **Simpan ke Galeri**, **Instagram Story**, **WhatsApp**, **Facebook**, dan **Lainnya**.
- **Background Video Dinamis & Pemutar Musik MP3**: Latar belakang video penuh layar (*muted* & *auto-loop*) dipadukan dengan pemutar musik MP3 persisten di pojok kanan atas (tambah lagu MP3 tersimpan permanen, *pause/play*, *next/prev*, *repeat*, tetap menyala saat aplikasi diminimalkan, dan otomatis mati saat *Close All*).
- **Desain Neo-Brutalist Responsif & 100% Offline**: Antarmuka tegas dengan tipografi lokal *Space Grotesk* dan *Space Mono*, logo SPOT Sucor Sekuritas terbundel, tautan sosial Instagram & GitHub pembuat, serta dukungan layar ponsel maupun tablet.

---

## Daftar Sekuritas & Tarif Fee Terbaru (V2.1)

| Kategori | Sekuritas (Kode AB) | Fee Beli | Fee Jual |
| :--- | :--- | :---: | :---: |
| **Retail & Digital** | Stockbit Sekuritas Digital (`XL`) | `0.15%` | `0.25%` |
| **Retail & Digital** | Ajaib Sekuritas Asia (`XC`) | `0.1513%` | `0.2513%` |
| **Retail & Digital** | Mirae Asset Sekuritas (`YP`) *(Day Trade: 0.08% / 0.18%)* | `0.15%` | `0.25%` |
| **Retail & Digital** | Indo Premier Sekuritas / IPOT (`PD`) | `0.19%` | `0.29%` |
| **Retail & Digital** | Pluang Saham ID (`PLU`) | `0.15%` | `0.25%` |
| **Retail & Digital** | Pluang Maju Sekuritas (`RO`) | `0.30%` | `0.30%` |
| **BUMN & Bank** | Mandiri Sekuritas / MOST (`CC`) *(Min Fee Rp 5.000/hari)* | `0.18%` | `0.28%` |
| **BUMN & Bank** | BNI Sekuritas / BIONS (`NI`) | `0.17%` | `0.27%` |
| **BUMN & Bank** | BRI Danareksa / BRIGHTS (`OD`) | `0.17%` | `0.27%` |
| **BUMN & Bank** | BCA Sekuritas / BEST (`SQ`) | `0.18%` | `0.28%` |
| **BUMN & Bank** | Bahana Sekuritas / DX Trade (`DX`) | `0.18%` | `0.28%` |
| **Swasta & Asing** | Panca Global Sekuritas / Hei5 (`PG`) | `0.10%` | `0.20%` |
| **Swasta & Asing** | Paramitra Alfa Sekuritas (`PS`) | `0.10%` | `0.20%` |
| **Swasta & Asing** | Korea Investment & Sekuritas / KISI (`BQ`) | `0.13%` | `0.23%` |
| **Swasta & Asing** | Phintraco Sekuritas / Profits (`AT`) | `0.15%` | `0.18%` |
| **Swasta & Asing** | Sinarmas Sekuritas / SimInvest (`DH`) | `0.15%` | `0.25%` |
| **Swasta & Asing** | Lotus Andalan Sekuritas (`YJ`) | `0.15%` | `0.25%` |
| **Swasta & Asing** | KB Valbury (`CP`), Sucor (`AZ`), RHB (`DR`), Samuel (`IF`), Yuanta (`FS`) | `0.15%` | `0.25%` |
| **Swasta & Asing** | Mega Capital Sekuritas (`CD`) | `0.17%` | `0.28%` |
| **Swasta & Asing** | NH Korindo (`XA`), Phillip (`KK`), Pilarmas (`PO`), Profindo (`RG`), CGS (`YU`), Trimegah (`LG`), Henan (`HP`), MNC (`EP`), Kiwoom (`AG`), Ciptadana (`KI`), Maybank (`ZP`) | `0.18%` | `0.28%` |
| **Swasta & Asing** | Panin Sekuritas Tbk (`GR`), Pacific Sekuritas Indonesia (`AP`) | `0.20%` | `0.30%` |
| **Swasta & Asing** | Minna Padi Investama Sekuritas Tbk (`MU`), UOB Kay Hian (`AI`) | `0.25%` | `0.35%` |
| **Custom** | Custom Tarif *All-In* (`CST`) & Custom *Base Fee* Murni (`BASE`) | Kustom | Kustom |

---

## Teknologi yang Digunakan

- **Kotlin** — Bahasa pemrograman utama aplikasi Android.
- **Jetpack Compose & Material 3** — Antarmuka deklaratif native bergaya Neo-Brutalist dengan dukungan tata letak adaptif (ponsel & tablet) serta *edge-to-edge*.
- **Jetpack DataStore Preferences** — Penyimpanan preferensi broker default secara lokal (*offline*).
- **Coil Compose** — Pemuatan & *caching* ikon resmi 38 sekuritas dari domain resmi masing-masing dengan fallback otomatis saat offline.
- **MVVM (`ViewModel` + `StateFlow`)** — Manajemen state reaktif yang terpisah dari mesin kalkulasi domain.
- **Google Fonts Lokal (`Space Grotesk` & `Space Mono`)** — Bundling font `.ttf` langsung di resource aplikasi tanpa ketergantungan jaringan.
- **Gradle 9.3.1 & AGP 9.1.1 + GitHub Actions** — Otomatisasi build Debug APK dan publikasi langsung ke **GitHub Releases**.

---

## Prasyarat Instalasi

- **Untuk Pengguna Android (Langsung Pasang APK):** Perangkat Android minimal **Android 7.0 (API 24)** atau lebih baru.
- **Untuk Pengembang (Build dari Source Code):**
  - **JDK 17** (Temurin / OpenJDK 17 atau lebih baru).
  - **Android Studio** terbaru atau **Gradle 9.3.1** dengan Android SDK (compileSdk `36`).
  - **Git** untuk melakukan *clone* repositori.

---

## Cara Instalasi & Menjalankan Project

### A. Unduh APK Langsung (Disarankan untuk Pengguna HP)
1. Buka halaman **Releases** pada repositori GitHub ini.
2. Pilih rilis terbaru (**`Kalkulator Fee Saham BEI (V2.1)`**).
3. Unduh file `.apk` pada bagian **Assets** dan pasang di perangkat Android Anda. *(Catatan: jika sebelumnya pernah memasang versi `V1.0.0`/`V1.0.1` dengan keystore sementara lama, hapus instalasi lama satu kali sebelum memasang `V2.0+`; seluruh pembaruan dari `V2.0` ke `V2.1` dan seterusnya berjalan mulus tanpa hapus data.)*

### B. Build dari Source Code (Untuk Pengembang)
1. **Clone repositori ini:**
   ```bash
   git clone <URL_REPOSITORI_ANDA>
   ```

2. **Masuk ke direktori project:**
   ```bash
   cd Fee-Saham-BEI
   ```

3. **Build Debug APK menggunakan Gradle 9.3.1:**
   ```bash
   gradle :app:assembleDebug --stacktrace --no-daemon
   ```

4. **Jalankan Unit Test kalkulator:**
   ```bash
   gradle :app:testDebugUnitTest --tests "com.example.StockFeeCalculatorTest"
   ```

---

## Susunan Project

```text
Fee-Saham-BEI/
├── .github/
│   └── workflows/
│       └── build-apk.yml                # CI/CD otomatis build Debug APK & GitHub Release
├── app/
│   ├── build.gradle.kts                 # Konfigurasi modul Android, versi V2.1 & dependensi
│   └── src/
│       ├── main/
│       │   ├── java/com/example/
│       │   │   ├── MainActivity.kt                      # Entry point, Picker MP3, Galeri & Share Kartu HD
│       │   │   ├── data/
│       │   │   │   └── BrokerPreferencesRepository.kt   # DataStore broker default + corruption recovery
│       │   │   ├── domain/
│       │   │   │   ├── BrokerCatalog.kt                 # Katalog 38 AB OJK + 2 mode Custom
│       │   │   │   ├── MarketModels.kt                  # Model pasar BEI & tarif regulator
│       │   │   │   └── StockFeeCalculator.kt            # Mesin kalkulasi presisi & formatter
│       │   │   ├── music/
│       │   │   │   ├── MusicModels.kt                   # Model lagu tersimpan, state & mode pengulangan
│       │   │   │   ├── MusicPlaylistRepository.kt       # Penyimpanan file MP3 internal & DataStore playlist
│       │   │   │   └── MusicPlaybackService.kt          # Foreground mediaPlayback service (stopWithTask=true)
│       │   │   ├── presentation/
│       │   │   │   └── CalculatorViewModel.kt           # StateFlow, kode saham, loading & share state
│       │   │   └── ui/
│       │   │       ├── CalculatorScreen.kt              # Layar utama Neo-Brutalist + video background
│       │   │       ├── components/
│       │   │       │   ├── BrokerLogo.kt                # Ikon sekuritas (Coil + logo SPOT Sucor lokal)
│       │   │       │   ├── BrokerPickerSheet.kt         # Modal pencarian cepat 38 broker
│       │   │       │   ├── FlexingShareSheet.kt         # Modal Portfolio Flexing (6 tema & 5 tombol bagikan)
│       │   │       │   ├── MusicPlayerSheet.kt          # Modal pemutar musik MP3 & kelola daftar lagu
│       │   │       │   ├── NeoBrutalistPrimitives.kt    # Komponen kartu, tombol & input
│       │   │       │   ├── ResultSections.kt            # Kartu Net RDN & Trade Confirmation
│       │   │       │   ├── ShareCardModels.kt           # Model tema & tujuan bagikan kartu
│       │   │       │   ├── ShareCardRenderer.kt         # Renderer Canvas HD 1080×1350, MediaStore & FileProvider
│       │   │       │   ├── SocialAndMusicDecor.kt       # Tombol musik kanan atas & footer Instagram/GitHub
│       │   │       │   ├── StockLoadingAnimation.kt     # Animasi Candlestick Splash & Calculating
│       │   │       │   └── VideoBackground.kt           # Pemutar video latar belakang muted & auto-loop
│       │   │       └── theme/                           # Color, Typography & Neo-Brutalist Theme
│       │   └── res/
│       │       ├── font/                                # Space Grotesk & Space Mono (.ttf lokal)
│       │       ├── raw/bg_video.mp4                     # Video latar belakang looping tanpa suara
│       │       ├── drawable/ & mipmap-*/                # Ikon launcher V2.1 & logo SPOT Sucor Sekuritas
│       │       ├── xml/file_paths.xml                   # FileProvider cache path untuk share gambar
│       │       └── values/strings.xml                   # Resource teks Bahasa Indonesia
│       └── test/java/com/example/
│           └── StockFeeCalculatorTest.kt                # Unit test rumus regulator, broker & kode saham
├── gradle/libs.versions.toml            # Version catalog (AGP 9.1.1)
├── CHANGELOG.md                         # Catatan perubahan tiap versi (sumber Release Notes)
└── README.md                            # Dokumentasi utama project
```

---

## Contoh Penggunaan

### 1. Skenario Transaksi di Aplikasi (Preset `Contoh: BBCA 10 Lot`)
Misalkan Anda membeli saham **BBCA** pada **Pasar Reguler (RG)** dengan rincian berikut:
- **Sekuritas**: Stockbit Sekuritas Digital (`XL`) — Beli `0.15%` | Jual `0.25%` (*All-In*)
- **Harga Beli**: `Rp 9.500`
- **Harga Jual**: `Rp 9.800`
- **Jumlah Lot**: `10 Lot` (`1.000` lembar saham)
- **e-Meterai**: Aktif (tidak terkena karena beli `Rp 9.500.000` & jual `Rp 9.800.000` masih ≤ `Rp 10.000.000`)

### 2. Rincian Hasil Perhitungan
| Komponen | Nilai | Keterangan |
| :--- | :--- | :--- |
| **Total Nilai Beli Bruto** | `Rp 9.500.000` | `10 lot × 100 lembar × Rp 9.500` |
| **Total Nilai Jual Bruto** | `Rp 9.800.000` | `10 lot × 100 lembar × Rp 9.800` |
| **Profit Kotor (Gross)** | `Rp 300.000` | Selisih nilai jual dan beli bruto |
| **Total Biaya Beli (0.15%)** | `Rp 14.250` | Termasuk komisi murni, PPN, Levy BEI/KSEI & KPEI |
| **Total Biaya Jual (0.25%)** | `Rp 24.500` | Termasuk komisi murni, PPN, Levy, KPEI & PPh Final `0.10%` |
| **Total Semua Biaya & Pajak** | `Rp 38.750` | Dipecah detail di kartu Trade Confirmation |
| **Harga Breakeven (BEP)** | `Rp 9.539` | Harga jual minimal impas per lembar |
| **Profit / Loss Bersih (Net RDN)** | **`Rp 261.250`** | **ROI bersih: `+2.75%`** |

---

## Riwayat Pembaruan (Changelog)

### `V2.1`
- **Revisi Branding, Header & Transisi Loading**: Loading screen penuh tertutup menggunakan logo aplikasi kustom dengan animasi cincin berputar & progres `0%–100%` (jeda transisi halus sebelum masuk ke halaman utama), teks `CREDIT BY LIWIRYA` / `VERSION: 2.1`, tata letak header 2 kolom ringkas (`CREATED BY LIWIRYA` & `Perhitungan Trade Fee`), serta watermark kartu bagikan `CREDIT BY LIWIRYA`.
- **Ikon Aplikasi & Logo Sucor Sekuritas Baru**: Pembaruan ikon launcher adaptif menggunakan logo kustom terbaru serta penggantian ikon **Sucor Sekuritas (AZ)** menggunakan logo resmi **SPOT**.
- **Background Video Looping Tanpa Suara**: Penambahan latar belakang video penuh layar yang otomatis berjalan terus-menerus dalam kondisi bisu (*muted*).
- **Pemutar Musik MP3 Persisten**: Tombol musik di pojok kanan atas untuk menambahkan & menyimpan file MP3 lokal, kontrol *play/pause*, *next/prev*, *repeat*, *seekbar*, tetap menyala saat aplikasi diminimalkan, dan otomatis berhenti saat aplikasi ditutup penuh (*Close All*).
- **Footer Sosial & Kredit Terpisah**: Penambahan ikon Instagram (`mynameisliwirya`), ikon GitHub (`liwirya`), dan teks `CREDIT BY LIWIRYA - THANKS FOR USED` secara terpisah di bawah frame kartu *Transparansi Perhitungan*.

### `V2.0`
- **Perbaikan Konflik Update APK**: Menggunakan *keystore* konsisten (`debug.keystore.base64`) pada build GitHub Actions serta menambahkan `ReplaceFileCorruptionHandler` pada DataStore agar pembaruan APK ke versi berikutnya berjalan mulus tanpa gagal pasang atau bentrok data.
- **Penyederhanaan Badge Jumlah Broker**: Mengubah tampilan `38 AB` menjadi `38`.
- **Fitur Input Kode / Nama Saham**: Menambahkan kolom nama/kode saham + pilihan cepat ticker BEI yang tampil otomatis di hasil Net RDN, rincian Trade Confirmation, teks salin, dan kartu bagikan.
- **Animasi Loading Candlestick**: Menambahkan *splash loading overlay* dan kartu animasi menghitung dengan grafik *candlestick* naik.
- **Portfolio Flexing Share Studio**: Modal bagikan kartu bergaya *Portfolio Flexing* dengan 6 tema visual HD (`1080×1350`), toggle nominal Rupiah, serta tombol **Simpan**, **Story**, **WhatsApp**, **Facebook**, dan **Lainnya**.

### `V1.0.1`
- **Revisi Tarif Broker**:
  - Panca Global Sekuritas (`PG`): Beli `0.10%` / Jual `0.20%`.
  - NH Korindo Sekuritas Indonesia (`XA`): Beli `0.18%` / Jual `0.28%`.
  - Sinarmas Sekuritas (`DH`): Beli `0.15%` / Jual `0.25%`.
  - Panin Sekuritas Tbk (`GR`): Beli `0.20%` / Jual `0.30%`.
  - Phillip Sekuritas Indonesia (`KK`): Beli `0.18%` / Jual `0.28%`.
  - Phintraco Sekuritas (`AT`): Beli `0.15%` / Jual `0.18%`.
- **Penambahan 8 Sekuritas Baru**: Paramitra Alfa (`PS`), Lotus Andalan (`YJ`), Mega Capital (`CD`), Minna Padi Investama (`MU`), Pacific Sekuritas (`AP`), Pilarmas Investindo (`PO`), Pluang Maju (`RO`), dan Profindo Sekuritas (`RG`).
- **Ikon Resmi Sekuritas**: Menampilkan ikon asli dari situs resmi masing-masing sekuritas di sebelah kiri nama broker (menggunakan Coil + cache lokal dan fallback emblem saat offline).
- **Pembaruan CI/CD & Dokumentasi**: Penamaan rilis GitHub `Kalkulator Fee Saham BEI (V1.0.1)`, tag berbasis nama aplikasi & versi, Release Notes otomatis dari `CHANGELOG.md`, serta pembaruan `README.md`.

### `V1.0.0`
- Rilis perdana aplikasi Android native **Fee Saham BEI** bergaya Neo-Brutalist dengan perhitungan Trade Confirmation BEI lengkap, 5 jenis pasar, pencarian broker & broker default offline, serta fitur salin/bagikan ringkasan.

Lihat [CHANGELOG.md](CHANGELOG.md) untuk catatan lengkap setiap versi.

---

## Lisensi

Project ini dilisensikan di bawah **MIT License**. Anda bebas menggunakan, memodifikasi, dan mendistribusikan kode sumber ini dengan tetap menyertakan pemberitahuan hak cipta asli.
