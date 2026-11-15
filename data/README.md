# Dataset Putusan Narkotika

## 1. Deskripsi Dataset

Folder ini berisi dataset terstruktur dari 50 putusan pengadilan terkait tindak pidana narkotika di Pengadilan Negeri Surabaya.

Dataset disusun untuk memenuhi tugas Ujian Tengah Semester (UTS) mata kuliah Struktur Data, Fakultas Teknik, Universitas Muhammadiyah Malang, Semester Ganjil 2026/2027.

Dataset menjadi sumber utama aplikasi pengelolaan putusan narkotika berbasis Java dengan pendekatan Object-Oriented Programming (OOP) dan antarmuka JavaFX.

Aplikasi menggunakan struktur data Dynamic Array sebagai penyimpanan utama dan Custom Stack untuk mencatat riwayat aktivitas pengguna.

## 2. File Dataset

File utama dalam folder ini:

| Nama File | Keterangan |
|---|---|
| `putusan-50.csv` | Dataset yang memuat 50 record putusan pengadilan narkotika |

Dokumen PDF asli disimpan pada penyimpanan eksternal sesuai instruksi dosen.

## 3. Struktur Atribut Dataset

Dataset menggunakan 16 atribut yang disesuaikan dengan class `Putusan` pada program Java.

| No. | Atribut | Tipe Data Java | Keterangan |
|---|---|---|---|
| 1 | caseId | String | Kode unik internal perkara |
| 2 | nomorPerkara | String | Nomor perkara pengadilan |
| 3 | pengadilan | String | Nama Pengadilan Negeri |
| 4 | tanggalPutusan | LocalDate | Tanggal putusan |
| 5 | terdakwaKode | String | Kode anonim terdakwa |
| 6 | jenisNarkotika | String | Jenis narkotika |
| 7 | beratBarangBuktiGram | double | Berat barang bukti dalam gram |
| 8 | pasalDakwaan | String | Pasal yang didakwakan |
| 9 | pasalTerbukti | String | Pasal yang dinyatakan terbukti |
| 10 | tuntutanBulan | int | Lama tuntutan pidana penjara |
| 11 | vonisBulan | int | Lama pidana penjara |
| 12 | dendaRupiah | long | Besaran pidana denda |
| 13 | barangBukti | String | Ringkasan barang bukti |
| 14 | keadaanMemberatkan | String | Keadaan yang memberatkan |
| 15 | keadaanMeringankan | String | Keadaan yang meringankan |
| 16 | sourcePages | String | Referensi halaman sumber atribut |

Urutan dan penamaan atribut mengikuti method `headers()` pada class `Putusan`.

## 4. Sumber Data

Data diperoleh dari dokumen putusan pengadilan yang diterbitkan melalui Direktori Putusan Mahkamah Agung Republik Indonesia.

Sumber resmi:

https://putusan3.mahkamahagung.go.id

Seluruh putusan dalam dataset berasal dari Pengadilan Negeri Surabaya sesuai hasil konfirmasi dan revisi ketentuan dosen.

Dokumen PDF asli disimpan pada penyimpanan eksternal. Dokumen tersebut tidak diunggah ke repository publik.

## 5. Ketentuan Pengolahan dan Validasi Data

Pengolahan dataset mengikuti ketentuan berikut:

1. Dataset harus memuat tepat 50 record valid.
2. Setiap record wajib memiliki `caseId` dan `nomorPerkara` yang unik.
3. Seluruh record harus berasal dari Pengadilan Negeri yang sama.
4. Identitas terdakwa disamarkan menggunakan kode anonim.
5. Tanggal putusan menggunakan format `YYYY-MM-DD`.
6. Berat barang bukti menggunakan satuan gram.
7. Nilai numerik tidak boleh negatif dan berat barang bukti harus berupa angka finite.
8. Data yang tidak ditemukan tidak boleh diisi berdasarkan perkiraan.
9. Setiap atribut penting harus memiliki referensi halaman dokumen sumber.
10. Dataset harus menggunakan header dan urutan 16 atribut sesuai class `Putusan`.

Program melakukan validasi melalui `CsvService` dan `DataManager`. Jika proses impor tidak memenuhi ketentuan, aplikasi menolak impor dan mempertahankan data sebelumnya.

## 6. Implementasi Struktur Data

Aplikasi menggunakan dua struktur data utama dalam pengelolaan informasi.

### 6.1 Dynamic Array

Class `DynamicArray<T>` digunakan sebagai struktur data utama untuk menyimpan objek `Putusan`.

Operasi yang digunakan meliputi:

- Add untuk menambahkan data.
- Insert untuk menyisipkan data.
- Get untuk mengambil data berdasarkan indeks.
- Set untuk memperbarui elemen.
- Remove untuk menghapus data.
- Search untuk mencari elemen.
- Contains untuk memeriksa keberadaan data.
- Clear untuk mengosongkan struktur.
- ValidateInvariant untuk memeriksa konsistensi struktur.

### 6.2 Custom Stack

Class `CustomStack<T>` digunakan sebagai struktur data sekunder untuk menyimpan riwayat aktivitas aplikasi.

Setiap aktivitas mencatat deskripsi operasi, waktu pelaksanaan, pengguna lokal, dan status keberhasilan.

Operasi Stack menggunakan prinsip LIFO (Last In, First Out).

## 7. Fitur Pengolahan Dataset

Dataset digunakan dalam aplikasi untuk mendukung fitur berikut.

| Fitur | Implementasi |
|---|---|
| Impor | Membaca tepat 50 record CSV dan memvalidasi data |
| Tampil | Menampilkan seluruh putusan melalui TableView |
| Tambah | Menambahkan putusan baru ke Dynamic Array |
| Hapus | Menghapus putusan berdasarkan caseId |
| Cari | Mencari berdasarkan caseId atau nomor perkara |
| Filter | Memfilter jenis narkotika, berat, pasal, dan rentang vonis |
| Agregasi | Menghitung jumlah perkara, rata-rata vonis, dan total denda |
| Distribusi | Menghitung jumlah perkara berdasarkan jenis narkotika |
| Pengurutan | Mengurutkan vonis menggunakan Insertion Sort |
| Audit | Menampilkan riwayat aktivitas menggunakan Custom Stack |
| Ekspor | Mengekspor data, hasil filter, laporan, dan trace pengurutan |

## 8. Visualisasi Data

Aplikasi menyediakan dashboard untuk menampilkan ringkasan dataset.

Visualisasi menggunakan komponen JavaFX Chart, yaitu:

- Pie Chart untuk menampilkan distribusi jenis narkotika.
- Bar Chart untuk menampilkan rata-rata vonis seluruh perkara.

Dashboard juga menampilkan jumlah perkara, rata-rata vonis dalam bulan, dan total denda.

## 9. Dokumentasi Provenance

Informasi asal data dicatat dalam file:

`docs/data-provenance.csv`

File tersebut menggunakan atribut:

`case_id,field_name,value,source_file,source_page,extractor,reviewer,status`

Satu baris mewakili satu atribut penting dalam satu putusan.

Dokumentasi provenance digunakan untuk memastikan setiap informasi dapat ditelusuri kembali ke dokumen PDF sumber.

## 10. Penggunaan Dataset

Dataset digunakan sebagai sumber pengujian aplikasi Java, khususnya untuk menguji:

1. Ketepatan proses impor dan validasi CSV.
2. Operasi penambahan dan penghapusan Dynamic Array.
3. Pencarian berdasarkan identitas perkara.
4. Proses filter dengan kriteria gabungan.
5. Perhitungan agregasi dan distribusi narkotika.
6. Pengurutan menggunakan Insertion Sort.
7. Konsistensi struktur data melalui pemeriksaan invariant.
8. Pencatatan riwayat aktivitas menggunakan Custom Stack.
9. Proses ekspor data dan laporan.

---

**Catatan:** Dataset ini merupakan bagian dari tugas akademik mata kuliah Struktur Data. Seluruh informasi harus sesuai dengan dokumen putusan asli dan dapat diverifikasi melalui dokumentasi provenance.
