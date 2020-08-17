package com.example.baksolapangantembaksenayan.model;

public class model_user {
    private String idUser,namaDepanUser,namaBelakangUser,email,gambar_profile,timestamp,tipe_akun;

    public model_user() {
    }

    public model_user(String idUser, String namaDepanUser, String namaBelakangUser, String email, String gambar_profile, String timestamp, String tipe_akun) {
        this.idUser = idUser;
        this.namaDepanUser = namaDepanUser;
        this.namaBelakangUser = namaBelakangUser;
        this.email = email;
        this.gambar_profile = gambar_profile;
        this.timestamp = timestamp;
        this.tipe_akun = tipe_akun;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getNamaDepanUser() {
        return namaDepanUser;
    }

    public void setNamaDepanUser(String namaDepanUser) {
        this.namaDepanUser = namaDepanUser;
    }

    public String getNamaBelakangUser() {
        return namaBelakangUser;
    }

    public void setNamaBelakangUser(String namaBelakangUser) {
        this.namaBelakangUser = namaBelakangUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGambar_profile() {
        return gambar_profile;
    }

    public void setGambar_profile(String gambar_profile) {
        this.gambar_profile = gambar_profile;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTipe_akun() {
        return tipe_akun;
    }

    public void setTipe_akun(String tipe_akun) {
        this.tipe_akun = tipe_akun;
    }
}
