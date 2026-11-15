# Operation Trace

## 1. Deskripsi

Folder ini berisi dokumentasi pelacakan operasi struktur data dan aktivitas aplikasi Data Putusan Narkotika.

Dokumentasi digunakan untuk memperlihatkan proses kerja struktur data DynamicArray dan CustomStack selama aplikasi dijalankan.

Pelacakan operasi membantu menunjukkan bagaimana aplikasi memproses penambahan, penghapusan, pencarian, filter, dan pengurutan data putusan.

## 2. Ruang Lingkup Operasi

Dokumentasi Operation Trace mencakup beberapa operasi berikut.

### 2.1 Penambahan Data

Operasi penambahan menggunakan method `add()` pada DynamicArray.

Proses ini menambahkan objek Putusan ke dalam struktur data. Jika kapasitas array penuh, struktur data melakukan penyesuaian kapasitas secara otomatis.

### 2.2 Penghapusan Data

Operasi penghapusan menggunakan method `remove()` pada DynamicArray.

Proses ini menghapus elemen berdasarkan posisi yang ditentukan. Elemen setelah posisi tersebut bergeser untuk mempertahankan urutan data.

### 2.3 Pencarian Data

Aplikasi menyediakan pencarian berdasarkan:

- caseId.
- Nomor perkara.

Pencarian dilakukan untuk menemukan putusan yang sesuai dengan kata kunci pengguna.

### 2.4 Filter Data

Aplikasi menyediakan filter gabungan berdasarkan:

- Jenis narkotika.
- Berat minimum barang bukti.
- Berat maksimum barang bukti.
- Pasal terbukti.
- Vonis minimum.
- Vonis maksimum.

Hasil filter menampilkan data yang memenuhi seluruh kriteria yang ditentukan pengguna.

### 2.5 Pengurutan Data

Aplikasi menggunakan algoritma Insertion Sort untuk mengurutkan putusan berdasarkan lama vonis pidana penjara.

Proses pengurutan mencatat:

- Jumlah perbandingan.
- Jumlah pergeseran elemen.
- Hasil akhir pengurutan.

Aplikasi menyediakan fitur ekspor trace untuk mendokumentasikan proses pengurutan.

### 2.6 Riwayat Aktivitas

Pencatatan aktivitas menggunakan CustomStack dengan prinsip LIFO (Last In, First Out).

Setiap aktivitas dicatat berdasarkan waktu pelaksanaan. Aktivitas terbaru ditampilkan pada urutan paling atas.

Pembacaan riwayat tidak mengubah urutan elemen dalam stack.

## 3. Pengujian Konsistensi Struktur Data

Pengujian konsistensi dilakukan menggunakan method `validateInvariant()` pada DynamicArray dan CustomStack.

Pemeriksaan bertujuan memastikan:

1. Ukuran elemen tidak negatif.
2. Ukuran tidak melebihi kapasitas.
3. Elemen aktif tidak bernilai null.
4. Struktur internal tetap konsisten setelah operasi.

Hasil pengujian dicatat berdasarkan pelaksanaan program dan pengujian yang benar-benar dilakukan.

## 4. Dokumentasi Hasil Operasi

Bukti pelaksanaan operasi dapat disimpan dalam bentuk:

| No. | Dokumentasi | Bukti |
|---|---|---|
| 1 | Penambahan data | Hasil penambahan putusan |
| 2 | Penghapusan data | Hasil penghapusan putusan |
| 3 | Pencarian data | Hasil pencarian berdasarkan identitas perkara |
| 4 | Filter data | Hasil penyaringan putusan |
| 5 | Pengurutan | File hasil ekspor trace Insertion Sort |
| 6 | Riwayat aktivitas | Tampilan riwayat CustomStack |
| 7 | Pengujian invariant | Hasil pengujian konsistensi struktur data |

## 5. Kesimpulan

Operation Trace digunakan untuk mendokumentasikan penerapan struktur data dalam aplikasi Data Putusan Narkotika.

DynamicArray mendukung pengelolaan data putusan, sedangkan CustomStack mencatat riwayat aktivitas aplikasi.

Algoritma Insertion Sort digunakan untuk mengurutkan data berdasarkan lama vonis.

Dokumentasi ini menjadi bukti pendukung implementasi dan pengujian struktur data dalam aplikasi.

---

**Catatan:** File trace dan hasil pengujian harus berasal dari pelaksanaan aplikasi. Dokumentasi ini tidak menggantikan bukti pengujian aktual.
