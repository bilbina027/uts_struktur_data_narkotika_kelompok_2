package com.pengadilan.app.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AktivitasLog {
    private final String deskripsi;
    private final LocalDateTime waktu;
    private final String user;
    private final String status;
    public AktivitasLog(String deskripsi) {
        this.deskripsi = deskripsi;
        this.waktu = LocalDateTime.now();
        this.user = "Administrator";
        this.status = "Sistem Aktif";
    }
    public String getDeskripsi() { return deskripsi; }
    public LocalDateTime getWaktu() { return waktu; }
    public String getWaktuFormatted() {
        return waktu.format(DateTimeFormatter.ofPattern("d
                d MMM yyyy · HH:mm:ss"));
    }
    public String getUser() { return user; }
    public String getStatus() { return status; }
}