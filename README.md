# Aplikasi Pengelolaan Data Putusan Narkotika

Aplikasi desktop berbasis JavaFX untuk mengelola 50 data putusan pengadilan narkotika. Aplikasi ini dikembangkan menggunakan Java 21 dengan menerapkan struktur data dan algoritma untuk mendukung pengelolaan data secara terorganisasi.

## Technologies

Teknologi yang digunakan dalam pengembangan aplikasi:

- Java 21
- JavaFX
- Maven
- JUnit 5

## Main Features

Aplikasi menyediakan beberapa fitur utama:

1. Dashboard untuk menampilkan ringkasan data putusan.
2. Manajemen data untuk menampilkan dan mengelola putusan.
3. Pencarian data putusan berdasarkan atribut tertentu.
4. Filter dan pengurutan data.
5. Impor dan ekspor dataset menggunakan CSV.
6. Riwayat aktivitas pengelolaan data.
7. Operation trace untuk mencatat proses operasi struktur data.

## Data Structures

Aplikasi mengimplementasikan struktur data berikut:

- **DynamicArray:** Menyimpan dan mengelola kumpulan data putusan secara dinamis.
- **CustomStack:** Menyimpan riwayat aktivitas menggunakan prinsip Last In, First Out (LIFO).

Implementasi struktur data digunakan untuk mendukung operasi penambahan, penghapusan, pencarian, filter, dan pengurutan data.

## Project Structure

```text
project/
├── data/
│   └── README.md
├── docs/
│   ├── adr/
│   ├── demo/
│   ├── trace/
│   ├── README.md
│   └── data-provenance.csv
├── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   └── test/
│       └── java/
├── AI_USAGE.md
├── CONTRIBUTIONS.md
├── README.md
└── pom.xml
```

## Requirements

Sebelum menjalankan aplikasi, pastikan perangkat telah memiliki:

- JDK 21 atau versi yang kompatibel.
- Apache Maven.
- JavaFX sesuai konfigurasi proyek.

## Build Application

Jalankan perintah berikut melalui terminal pada direktori proyek:

```bash
mvn clean package
```

## Run Tests

Untuk menjalankan pengujian otomatis:

```bash
mvn test
```

## Dataset

Dataset penelitian terdiri dari 50 PDF putusan asli pengadilan terkait perkara narkotika.

Informasi setiap putusan dicatat dalam dataset aplikasi dan dokumentasi data provenance.

Dokumentasi provenance mencakup:

- Identitas putusan.
- Nomor dan tanggal putusan.
- Pengadilan yang menerbitkan putusan.
- Sumber PDF.
- Tautan sumber.
- Tanggal akses.
- Status verifikasi.

PDF asli digunakan sebagai sumber rujukan untuk pemeriksaan dan validasi data.

## Documentation

Dokumentasi proyek tersedia dalam direktori `docs/`, meliputi:

- Data provenance.
- Architecture Decision Records (ADR).
- Operation trace.
- Dokumentasi demonstrasi aplikasi.

Dokumentasi tambahan:

- `AI_USAGE.md`: Dokumentasi penggunaan kecerdasan buatan.
- `CONTRIBUTIONS.md`: Pembagian kontribusi anggota kelompok.

## Development

Proyek dikembangkan secara kolaboratif melalui GitHub menggunakan pembagian tugas backend, struktur data, frontend, dan integrasi antarmuka.

Setiap perubahan kode didokumentasikan melalui commit dan pull request.

## Project Status

Proyek ini dikembangkan untuk memenuhi Ujian Tengah Semester mata kuliah Struktur Data.
