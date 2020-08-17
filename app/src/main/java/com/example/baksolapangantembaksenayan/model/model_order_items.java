package com.example.baksolapangantembaksenayan.model;

public class model_order_items {
    private String pId,namaProduk,hargaProduk,quantity,total_harga;

    public model_order_items() {
    }

    public model_order_items(String pId, String namaProduk, String hargaProduk,
                             String quantity, String total_harga) {
        this.pId = pId;
        this.namaProduk = namaProduk;
        this.hargaProduk = hargaProduk;
        this.quantity = quantity;
        this.total_harga = total_harga;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getNamaProduk() {
        return namaProduk;
    }

    public void setNamaProduk(String namaProduk) {
        this.namaProduk = namaProduk;
    }

    public String getHargaProduk() {
        return hargaProduk;
    }

    public void setHargaProduk(String hargaProduk) {
        this.hargaProduk = hargaProduk;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotal_harga() {
        return total_harga;
    }

    public void setTotal_harga(String total_harga) {
        this.total_harga = total_harga;
    }
}
