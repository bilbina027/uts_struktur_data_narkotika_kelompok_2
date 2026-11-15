# Documentation

## 1. Deskripsi

Folder ini berisi dokumentasi proyek UTS Struktur Data Pengelolaan Putusan Narkotika yang dikembangkan menggunakan Java dan JavaFX.

Dokumentasi mencakup sumber dataset, keputusan arsitektur, pelacakan operasi struktur data, serta demonstrasi fitur aplikasi.

## 2. Struktur Dokumentasi

Dokumentasi proyek terdiri atas beberapa bagian berikut.

### 2.1 Data Provenance

File: `data-provenance.csv`

Berisi informasi asal data putusan yang digunakan dalam dataset. Dokumentasi mencakup identitas perkara, atribut data, sumber dokumen PDF, nomor halaman, dan status verifikasi.

### 2.2 Architecture Decision Records (ADR)

Folder: `adr/`

Berisi dokumentasi keputusan desain arsitektur aplikasi, meliputi:

- Pemilihan DynamicArray.
- Pemilihan CustomStack.
- Alasan pemilihan struktur data.
- Pertimbangan implementasi dan kompleksitas operasi.

### 2.3 Operation Trace

Folder: `trace/`

Berisi dokumentasi pelacakan operasi struktur data dan aktivitas aplikasi, meliputi:

- Penambahan dan penghapusan data.
- Pencarian dan filter data.
- Pengurutan menggunakan Insertion Sort.
- Pencatatan perbandingan dan pergeseran elemen.
- Pengujian konsistensi struktur data.

### 2.4 Application Demo

Folder: `demo/`

Berisi screenshot demonstrasi aplikasi, meliputi:

- Dashboard aplikasi.
- Tampilan tabel data putusan.
- Proses impor dataset CSV.
- Hasil filter gabungan.
- Riwayat aktivitas aplikasi.

## 3. Tujuan Dokumentasi

Dokumentasi ini disusun untuk:

1. Menjelaskan sumber dan validitas data putusan.
2. Mendokumentasikan keputusan desain struktur data.
3. Memperlihatkan penerapan algoritma dan operasi struktur data.
4. Menyediakan bukti visual demonstrasi aplikasi.
5. Mendukung proses evaluasi dan pengujian proyek UTS Struktur Data.

## 4. Keterkaitan dengan Aplikasi

Seluruh dokumentasi mendukung pengembangan aplikasi Data Putusan Narkotika berbasis Java.

Aplikasi menggunakan DynamicArray sebagai struktur data utama untuk mengelola putusan dan CustomStack untuk mencatat riwayat aktivitas.

Algoritma Insertion Sort digunakan untuk mengurutkan data berdasarkan lama vonis.

Dokumentasi ini menjadi bagian pendukung dalam menjelaskan implementasi dan pengujian aplikasi.

---

**Catatan:** Seluruh informasi dataset harus dapat ditelusuri ke dokumen putusan asli. Bukti pengujian harus berasal dari pelaksanaan aplikasi.
