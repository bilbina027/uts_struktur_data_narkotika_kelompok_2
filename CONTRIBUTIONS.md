# Team Contributions

Dokumen ini menjelaskan pembagian tanggung jawab dan kontribusi setiap anggota dalam pengembangan Aplikasi Pengelolaan 50 Putusan Pengadilan Narkotika.

Pengembangan aplikasi dibagi menjadi dua tanggung jawab utama, yaitu Backend & Struktur Data serta Frontend & Integrasi UI.

## Member 1: Anggota A

Nama: Alifatus Bilbina
NIM: 202510370110067

Peran: Backend & Struktur Data

Kontribusi:

- Mengembangkan model data, meliputi Putusan, JenisNarkotika, dan AktivitasLog.
- Mengimplementasikan struktur data DynamicArray dan CustomStack.
- Mengembangkan StrukturData sebagai kontrak struktur data.
- Mengembangkan DataManager untuk pengelolaan data putusan dan riwayat aktivitas.
- Mengembangkan CsvService untuk proses impor dan ekspor dataset.
- Mengimplementasikan algoritma pencarian, pengurutan, filter, dan pencatatan operation trace.
- Mengembangkan validasi dataset, termasuk pemeriksaan duplikasi dan konsistensi data.
- Melakukan pengujian backend, struktur data, dan invariant.
- Memperbaiki kesalahan pemrosesan CSV dan pengelolaan data.

## Member 2: Anggota B

Nama: Abdul Hakim
NIM: 202510370110091
Peran: Frontend & Integrasi UI

Kontribusi:

- Mengembangkan aplikasi berbasis JavaFX.
- Mengimplementasikan MainApp dan MainController.
- Mengembangkan DashboardController dan DashboardView.
- Mengembangkan DataPutusanController dan DataPutusanView.
- Mengembangkan FilterController dan FilterView.
- Mengembangkan RiwayatController dan RiwayatView.
- Mengembangkan ImporEksporController dan ImporEksporView.
- Mengimplementasikan FXML dan CSS untuk tampilan aplikasi.
- Mengintegrasikan antarmuka dengan DataManager dan CsvService.
- Mengembangkan tampilan hasil pencarian, filter, pengurutan, dan status impor.
- Melakukan pengujian alur aplikasi dan memastikan seluruh halaman berjalan dengan baik.

## Collaboration

Kedua anggota bekerja sama dalam proses pengembangan aplikasi melalui beberapa kegiatan:

1. Mengintegrasikan backend dan frontend.
2. Melakukan pemeriksaan dan review kode.
3. Menguji fungsi struktur data dan fitur aplikasi.
4. Memeriksa kesesuaian dataset dengan 50 PDF putusan asli.
5. Memperbaiki kesalahan berdasarkan hasil pengujian.
6. Menyiapkan dokumentasi dan demonstrasi aplikasi.

## Revision and Commit Responsibility

Pembagian pekerjaan mengikuti empat tahap revisi:

- Revisi 1: Fondasi aplikasi, model data, struktur data, service, dan fitur dasar.
- Revisi 2: Refactoring backend, integrasi CSV, controller, dan pengujian awal.
- Revisi 3: Validasi dataset, konsistensi data, resize DynamicArray, dan regression test.
- Revisi 4: Penyempurnaan pencarian, sorting, operation trace, refresh UI, dan CSV parser.

Setiap anggota bertanggung jawab terhadap file yang dikerjakan sesuai pembagian tugas dan riwayat commit pada repository GitHub.

## Accountability

Setiap anggota bertanggung jawab memahami, menguji, dan menjelaskan hasil pekerjaannya.

Seluruh kontribusi harus tercermin dalam perubahan kode, riwayat commit, dan pull request yang relevan.

Dokumen ini menjadi catatan pembagian tanggung jawab dalam pengembangan proyek UTS Struktur Data.
