package com.example.baksolapangantembaksenayan.model;

public class ModelCart {

    private String itemId, idMenu, nama_produk, harga_produk, total_harga_produk, quantity_produk;

    public ModelCart() {
    }

    public ModelCart(String itemId, String idMenu, String nama_produk, String harga_produk, String total_harga_produk, String quantity_produk) {
        this.itemId = itemId;
        this.idMenu = idMenu;
        this.nama_produk = nama_produk;
        this.harga_produk = harga_produk;
        this.total_harga_produk = total_harga_produk;
        this.quantity_produk = quantity_produk;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(String idMenu) {
        this.idMenu = idMenu;
    }

    public String getNama_produk() {
        return nama_produk;
    }

    public void setNama_produk(String nama_produk) {
        this.nama_produk = nama_produk;
    }

    public String getHarga_produk() {
        return harga_produk;
    }

    public void setHarga_produk(String harga_produk) {
        this.harga_produk = harga_produk;
    }

    public String getTotal_harga_produk() {
        return total_harga_produk;
    }

    public void setTotal_harga_produk(String total_harga_produk) {
        this.total_harga_produk = total_harga_produk;
    }

    public String getQuantity_produk() {
        return quantity_produk;
    }

    public void setQuantity_produk(String quantity_produk) {
        this.quantity_produk = quantity_produk;
    }
}