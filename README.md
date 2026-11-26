# ID Stock Broker Fee Calculator

Kalkulator simulasi biaya transaksi saham Bursa Efek Indonesia (IDX / BEI) berbasis web untuk menghitung biaya beli (*buy fee*), biaya jual (*sell fee*), pajak penjualan (*PPh Final*), estimasi bea meterai, harga impas (*break-even price*), hingga proyeksi keuntungan/kerugian bersih (*Net P&L*) secara akurat.

---

## Fitur Utama

- **Perhitungan Biaya Transaksi BEI Lengkap**: Menghitung komisi broker, PPN, *levy* BEI/KPEI/KSEI, serta PPh Final `0.1%` khusus transaksi jual.
- **Preset Sekuritas & Kustomisasi Fee**: Mendukung pemilihan tarif standar beberapa sekuritas di Indonesia maupun input persentase *buy/sell fee* secara manual.
- **Simulasi *Break-Even Point* (BEP)**: Menampilkan harga jual minimal (disesuaikan dengan aturan *fraksi harga* IDX) agar transaksi tidak mengalami kerugian setelah dipotong biaya.
- **Perhitungan *Net Profit / Loss* & ROI**: Menampilkan rincian modal awal, total potongan biaya, hasil bersih penjualan, serta persentase imbal hasil (*return*).
- **Dukungan Estimasi Bea Meterai**: Opsi perhitungan bea meterai `Rp 10.000` untuk nilai transaksi harian di atas `Rp 10.000.000`.
- **Antarmuka Responsif**: Tampilan bersih, ringan, dan nyaman diakses baik dari perangkat *desktop* maupun *mobile*.

---

## Teknologi yang Digunakan

*Sesuaikan daftar berikut apabila ada library tambahan di dalam repositori Anda:*

- **HTML5 & CSS3** — Struktur semantik dan tata letak responsif (*Flexbox / Grid* atau *Tailwind CSS*).
- **JavaScript (ES6+)** — Logika kalkulasi biaya transaksi, validasi input, dan manipulasi DOM secara *real-time*.
- **Git & GitHub** — Kontrol versi dan *hosting* repositori.

---

## Prasyarat Instalasi

Untuk menjalankan project ini secara lokal, Anda hanya membutuhkan:

- **Web Browser modern** (Google Chrome, Mozilla Firefox, Microsoft Edge, atau Safari).
- **Git** (opsional, untuk melakukan *clone* repositori).
- **Node.js & npm** (opsional, hanya jika project menggunakan *bundler* seperti Vite atau server pengembangan lokal).

---

## Cara Instalasi & Menjalankan Project

1. **Clone repositori ini:**
   ```bash
   git clone https://github.com/liwirya/ID-Stock-Broker-Fee-Calculator.git
   ```

2. **Masuk ke direktori project:**
   ```bash
   cd ID-Stock-Broker-Fee-Calculator
   ```

3. **Jalankan aplikasi:**
   - **Metode Langsung (Static HTML/JS):** Buka file `index.html` langsung di browser Anda (atau gunakan ekstensi *Live Server* di VS Code).
   - **Jika menggunakan Node.js / Vite:**
     ```bash
     npm install
     npm run dev
     ```

---

## Susunan Project

```text
ID-Stock-Broker-Fee-Calculator/
├── index.html          # Halaman utama kalkulator dan struktur antarmuka
├── css/
│   └── style.css       # Styling, tema warna, dan layout responsif
├── js/
│   ├── app.js          # Logika utama event listener & pembaruan UI
│   └── calculator.js   # Fungsi perhitungan fee beli, fee jual, pajak, dan BEP
├── assets/             # Ikon, gambar, atau aset statis pendukung
├── LICENSE             # File lisensi MIT
└── README.md           # Dokumentasi utama project
```

---

## Contoh Penggunaan

### 1. Skenario Transaksi di Aplikasi
Misalkan Anda membeli saham **BBCA** dengan rincian berikut:
- **Harga Beli**: `Rp 9.500`
- **Jumlah Lot**: `10 Lot` (`1.000` lembar saham)
- **Tarif Fee Broker**: Beli `0.15%` | Jual `0.25%`
- **Target Harga Jual**: `Rp 9.700`

### 2. Rincian Hasil Perhitungan
| Komponen | Nilai | Keterangan |
| :--- | :--- | :--- |
| **Nilai Beli Kotor** | `Rp 9.500.000` | `10 lot × 100 lembar × Rp 9.500` |
| **Biaya Beli (0.15%)** | `Rp 14.250` | Sudah termasuk komisi broker, levy & PPN |
| **Total Modal Keluar** | `Rp 9.514.250` | Nilai beli kotor + biaya beli |
| **Nilai Jual Kotor** | `Rp 9.700.000` | `10 lot × 100 lembar × Rp 9.700` |
| **Biaya Jual (0.25%)** | `Rp 24.250` | Termasuk PPh Final penjualan `0.1%` |
| **Hasil Jual Bersih** | `Rp 9.675.750` | Nilai jual kotor - biaya jual |
| **Net Profit / Loss** | **`+Rp 161.500`** | **ROI bersih: `+1.70%`** |

---

## Lisensi

Project ini dilisensikan di bawah **MIT License**. Anda bebas menggunakan, memodifikasi, dan mendistribusikan kode sumber ini dengan tetap menyertakan pemberitahuan hak cipta asli.

Lihat file [LICENSE](LICENSE) untuk informasi selengkapnya.
