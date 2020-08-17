package com.example.baksolapangantembaksenayan.model;

public class model_menu_admin {

    private String IdMenu,deskripsiMenu,gambarMenu,hargaMenu,kategoriMenu,namaMenu,satuanMenu,timestamp,uid;

    public model_menu_admin() {
    }

    public model_menu_admin(String idMenu, String deskripsiMenu, String gambarMenu, String hargaMenu,
                            String kategoriMenu, String namaMenu, String satuanMenu, String timestamp, String uid) {
        IdMenu = idMenu;
        this.deskripsiMenu = deskripsiMenu;
        this.gambarMenu = gambarMenu;
        this.hargaMenu = hargaMenu;
        this.kategoriMenu = kategoriMenu;
        this.namaMenu = namaMenu;
        this.satuanMenu = satuanMenu;
        this.timestamp = timestamp;
        this.uid = uid;
    }

    public String getIdMenu() {
        return IdMenu;
    }

    public void setIdMenu(String idMenu) {
        IdMenu = idMenu;
    }

    public String getDeskripsiMenu() {
        return deskripsiMenu;
    }

    public void setDeskripsiMenu(String deskripsiMenu) {
        this.deskripsiMenu = deskripsiMenu;
    }

    public String getGambarMenu() {
        return gambarMenu;
    }

    public void setGambarMenu(String gambarMenu) {
        this.gambarMenu = gambarMenu;
    }

    public String getHargaMenu() {
        return hargaMenu;
    }

    public void setHargaMenu(String hargaMenu) {
        this.hargaMenu = hargaMenu;
    }

    public String getKategoriMenu() {
        return kategoriMenu;
    }

    public void setKategoriMenu(String kategoriMenu) {
        this.kategoriMenu = kategoriMenu;
    }

    public String getNamaMenu() {
        return namaMenu;
    }

    public void setNamaMenu(String namaMenu) {
        this.namaMenu = namaMenu;
    }

    public String getSatuanMenu() {
        return satuanMenu;
    }

    public void setSatuanMenu(String satuanMenu) {
        this.satuanMenu = satuanMenu;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
