# Application Demo

## 1. Deskripsi

Folder ini berisi dokumentasi demonstrasi aplikasi Data Putusan Narkotika yang dikembangkan menggunakan Java dan JavaFX.

Dokumentasi berupa screenshot yang menunjukkan tampilan antarmuka, proses pengolahan dataset, penerapan struktur data, serta fitur utama aplikasi.

Demonstrasi ini digunakan sebagai bukti bahwa aplikasi dapat menjalankan fungsi pengelolaan dan analisis data putusan pengadilan.

## 2. Dokumentasi Tampilan Aplikasi

Screenshot demonstrasi aplikasi meliputi:

| No. | Nama File | Keterangan |
|---|---|---|
| 1 | `01-dashboard.png` | Tampilan dashboard setelah dataset berhasil diimpor |
| 2 | `02-data-putusan.png` | Tampilan tabel yang memuat 50 data putusan |
| 3 | `03-import-csv.png` | Bukti keberhasilan proses impor 50 putusan |
| 4 | `04-filter-gabungan.png` | Tampilan hasil filter berdasarkan jenis narkotika dan kriteria lainnya |
| 5 | `05-riwayat-aktivitas.png` | Tampilan riwayat aktivitas menggunakan CustomStack |
| 6 | `06-dashboard-awal.png` | Tampilan dashboard sebelum dataset diimpor |

## 3. Demonstrasi Fitur Aplikasi

### 3.1 Dashboard

Dashboard menampilkan ringkasan dataset berupa:

- Jumlah perkara.
- Rata-rata vonis dalam bulan.
- Total denda.
- Distribusi jenis narkotika menggunakan Pie Chart.
- Rata-rata vonis menggunakan Bar Chart.

### 3.2 Pengelolaan Data Putusan

Halaman Data Putusan menampilkan data dalam bentuk tabel.

Fitur yang tersedia meliputi penambahan, penghapusan, pencarian, pengurutan berdasarkan vonis, dan ekspor data.

### 3.3 Impor dan Ekspor CSV

Aplikasi menyediakan fitur impor dataset menggunakan format CSV dengan 16 atribut.

Proses impor memvalidasi jumlah record dan konsistensi data. Dataset yang berhasil diimpor memuat 50 putusan dari Pengadilan Negeri Surabaya.

Aplikasi juga menyediakan ekspor seluruh data dan laporan dalam bentuk teks.

### 3.4 Filter Gabungan

Fitur filter memungkinkan pengguna menyaring data berdasarkan:

- Jenis narkotika.
- Berat minimum dan maksimum barang bukti.
- Pasal terbukti.
- Vonis minimum dan maksimum.

Hasil filter ditampilkan dalam tabel dan dilengkapi ringkasan jumlah perkara, rata-rata vonis, serta total denda.

### 3.5 Riwayat Aktivitas

Riwayat aktivitas menggunakan struktur data CustomStack dengan prinsip LIFO (Last In, First Out).

Aktivitas terbaru ditampilkan pada urutan paling atas. Pembacaan riwayat tidak mengubah urutan elemen dalam stack.

### 3.6 Pengurutan Data

Aplikasi menggunakan algoritma Insertion Sort untuk mengurutkan putusan berdasarkan lama vonis.

Proses pengurutan dilengkapi pencatatan perbandingan dan pergeseran elemen sebagai trace algoritma.

## 4. Hasil Demonstrasi

Berdasarkan demonstrasi aplikasi, fitur yang diperlihatkan meliputi:

1. Penampilan ringkasan data melalui dashboard.
2. Penampilan 50 record putusan.
3. Keberhasilan impor dataset CSV.
4. Penyaringan data menggunakan beberapa kriteria.
5. Pengurutan data berdasarkan lama vonis.
6. Pencatatan aktivitas menggunakan CustomStack.
7. Penyediaan fitur ekspor data dan laporan.

Screenshot digunakan sebagai bukti visual pelaksanaan demonstrasi. Pengujian otomatis dan validasi sumber putusan didokumentasikan secara terpisah.

## 5. Catatan

Seluruh screenshot merupakan dokumentasi demonstrasi aplikasi. Keabsahan data putusan tetap mengacu pada dokumen PDF asli dan dokumentasi provenance.

