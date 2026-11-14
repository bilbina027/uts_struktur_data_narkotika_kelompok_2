package com.pengadilan.app.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class AktivitasLog {
    private final String deskripsi;
    private final LocalDateTime waktu;
    private final String user;
    private final String status;

    public AktivitasLog (String deskripsi, boolean berhasil) {
        this.deskripsi = deskripsi;
        this.waktu = LocalDateTime.now();
        this.user = "Pengguna Lokal";
        this.status = berhasil ? "BERHASIL" : "GAGAL";
    }

    public String getDeskripsi() { return deskripsi; }
    public LocalDateTime getWaktu () { return waktu; }
    public String getUser() { return user; }
    public String getStatus() { return status; }

    @Override
    public String toString() {
        return waktu.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
                + " | " + user + " | " + status + " | " + deskripsi;
    }
}