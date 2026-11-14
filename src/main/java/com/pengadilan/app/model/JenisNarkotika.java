package com.pengadilan.app.model;

public enum JenisNarkotika {
    SHABU("Shabu / Metamfetamina"),
    GANJA("Ganja / Kanabis"),
    EKSTASI("Ekstasi / MDMA"),
    TEMBAKAU_SINTETIS("Tembakau Sintetis"),
    OBAT_KERAS("Obat Keras / Pil Trex"),
    LAIN_NYA("Lainnya");
    private final String label;
    JenisNarkotika(String label) {
        this.label = label;
    }
    public String getLabel() {
        return label;
    }