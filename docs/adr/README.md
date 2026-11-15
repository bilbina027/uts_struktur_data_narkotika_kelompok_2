# Architecture Decision Records (ADR)

## 1. Deskripsi

Folder ini berisi dokumentasi keputusan arsitektur (Architecture Decision Records) dalam pengembangan aplikasi Dataset Putusan Narkotika.

Dokumentasi ini menjelaskan alasan pemilihan struktur data, pertimbangan desain, implementasi, serta kompleksitas operasi yang digunakan dalam aplikasi.

Aplikasi dikembangkan menggunakan Java dengan pendekatan Object-Oriented Programming (OOP) dan antarmuka JavaFX.

## 2. Ruang Lingkup Keputusan Arsitektur

Keputusan arsitektur berfokus pada dua struktur data utama.

### 2.1 DynamicArray

`DynamicArray<T>` digunakan sebagai struktur data utama untuk menyimpan objek `Putusan`.

Alasan pemilihan:

1. Mampu menyimpan data secara dinamis.
2. Mendukung akses elemen berdasarkan indeks.
3. Mendukung operasi tambah, sisip, ubah, hapus, dan pencarian.
4. Kapasitas dapat bertambah otomatis ketika jumlah elemen mencapai batas.
5. Sesuai untuk mengelola dataset yang terdiri dari 50 putusan.

Implementasi terdapat pada:

`src/main/java/com/pengadilan/app/util/DynamicArray.java`

### 2.2 CustomStack

`CustomStack<T>` digunakan untuk menyimpan riwayat aktivitas aplikasi dalam bentuk objek `AktivitasLog`.

Struktur ini menerapkan prinsip LIFO (Last In, First Out).

Alasan pemilihan:

1. Aktivitas terbaru dapat diakses terlebih dahulu.
2. Mendukung operasi push, pop, dan peek.
3. Memudahkan pengelolaan riwayat aktivitas.
4. Mendukung pencarian tanpa mengubah urutan elemen.
5. Kapasitas bertambah otomatis ketika ruang penyimpanan penuh.

Implementasi terdapat pada:

`src/main/java/com/pengadilan/app/util/CustomStack.java`

## 3. Pertimbangan Desain

Pemilihan kedua struktur data didasarkan pada kebutuhan pengelolaan dataset dan pencatatan aktivitas aplikasi.

`DynamicArray` dipilih karena aplikasi membutuhkan penyimpanan data putusan yang dapat diakses, dicari, diurutkan, dan difilter.

`CustomStack` dipilih karena aplikasi membutuhkan pencatatan aktivitas dengan urutan berdasarkan waktu operasi, sehingga aktivitas terbaru dapat ditampilkan terlebih dahulu.

Kedua struktur data dibuat secara manual menggunakan array internal tanpa bergantung pada implementasi `ArrayList` atau `Stack` bawaan Java.

## 4. Kompleksitas Operasi

Kompleksitas operasi setiap struktur data dijelaskan sebagai berikut.

### 4.1 Kompleksitas DynamicArray

| Operasi | Kompleksitas |
|---|---|
| Add | O(1) amortized |
| Get | O(1) |
| Set | O(1) |
| Insert | O(n) |
| Remove | O(n) |
| Search | O(n) |
| Contains | O(n) |
| Clear | O(n) |
| Resize | O(n) |

Keterangan:

- Add memiliki kompleksitas O(1) secara amortisasi. Ketika kapasitas penuh, proses penambahan dapat memerlukan O(n) karena resize.
- Get dan Set menggunakan indeks sehingga membutuhkan O(1).
- Insert dan Remove memerlukan pergeseran elemen sehingga memiliki kompleksitas O(n).
- Search dan Contains memeriksa elemen secara berurutan sehingga memiliki kompleksitas O(n).

### 4.2 Kompleksitas CustomStack

| Operasi | Kompleksitas |
|---|---|
| Push | O(1) amortized |
| Pop | O(1) |
| Peek | O(1) |
| GetFromTop | O(1) |
| SearchPreserve | O(n) |
| Restore | O(n) |
| Clear | O(n) |

Keterangan:

- Push memiliki kompleksitas O(1) secara amortisasi. Proses resize dapat memerlukan O(n).
- Pop dan Peek mengakses elemen paling atas sehingga membutuhkan O(1).
- GetFromTop mengakses elemen berdasarkan jarak dari bagian atas stack.
- SearchPreserve memeriksa elemen tanpa mengubah isi dan urutan stack.
- Restore memindahkan elemen dari stack sementara ke stack utama.
- Clear menghapus elemen satu per satu menggunakan operasi pop.

## 5. Algoritma Pengurutan

Aplikasi menggunakan algoritma Insertion Sort untuk mengurutkan data putusan berdasarkan lama vonis pidana penjara dari yang terkecil hingga terbesar.

Kompleksitas algoritma:

| Kondisi | Kompleksitas |
|---|---|
| Best Case | O(n) |
| Average Case | O(n²) |
| Worst Case | O(n²) |
| Space Complexity | O(1) |

Aplikasi juga menyediakan trace pengurutan untuk memperlihatkan proses perbandingan dan pergeseran elemen selama algoritma berjalan.

## 6. Konsistensi Struktur Data

Kedua struktur data memiliki method `validateInvariant()` untuk memeriksa konsistensi internal.

Pemeriksaan mencakup:

1. Array internal tidak bernilai null.
2. Ukuran elemen tidak negatif.
3. Ukuran elemen tidak melebihi kapasitas.
4. Seluruh elemen aktif tidak bernilai null.
5. Slot di luar ukuran aktif harus bernilai null.

Pemeriksaan ini membantu memastikan operasi struktur data berjalan sesuai aturan implementasi.

## 7. Integrasi dengan Aplikasi

Struktur data terintegrasi dengan class dan komponen aplikasi berikut:

| Komponen | Fungsi |
|---|---|
| `Putusan` | Model data perkara |
| `AktivitasLog` | Model riwayat aktivitas |
| `DynamicArray` | Penyimpanan utama data putusan |
| `CustomStack` | Penyimpanan riwayat aktivitas |
| `DataManager` | Pengelolaan data dan operasi aplikasi |
| `CsvService` | Impor dan ekspor dataset CSV |
| `DashboardController` | Menampilkan ringkasan dan visualisasi |
| `DataPutusanController` | Mengelola data putusan |
| `FilterController` | Memproses pencarian dan filter |

## 8. Kesimpulan

Pemilihan DynamicArray dan CustomStack didasarkan pada kebutuhan fungsional aplikasi Dataset Putusan Narkotika.

DynamicArray mendukung pengelolaan 50 record putusan secara dinamis. CustomStack mendukung pencatatan riwayat aktivitas berdasarkan prinsip LIFO.

Kedua struktur data diimplementasikan secara manual agar konsep struktur data dapat diterapkan dan diuji secara langsung dalam aplikasi Java.

---

**Catatan:** Dokumentasi ini mengikuti implementasi program pada Codingan Program UTS Struktur Data Revisi 9.
