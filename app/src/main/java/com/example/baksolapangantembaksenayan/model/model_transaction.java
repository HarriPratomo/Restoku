package com.example.baksolapangantembaksenayan.model;

public class model_transaction {
    private String idOrder,idUser,jamOrder,tanggalOrder,totalPesanan,alamat,telepon,statusOrder,email,nama_customer;


    public model_transaction() {
    }

    public model_transaction(String idOrder, String idUser, String jamOrder, String tanggalOrder, String totalPesanan,
                             String alamat, String telepon, String statusOrder, String email, String nama_customer) {
        this.idOrder = idOrder;
        this.idUser = idUser;
        this.jamOrder = jamOrder;
        this.tanggalOrder = tanggalOrder;
        this.totalPesanan = totalPesanan;
        this.alamat = alamat;
        this.telepon = telepon;
        this.statusOrder = statusOrder;
        this.email = email;
        this.nama_customer = nama_customer;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getNama_customer() {
        return nama_customer;
    }

    public void setNama_customer(String nama_customer) {
        this.nama_customer = nama_customer;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNamaCustomer() {
        return nama_customer;
    }

    public void setNamaCustomer(String namaCustomer) {
        this.nama_customer = namaCustomer;
    }

    public String getStatusOrder() {
        return statusOrder;
    }

    public void setStatusOrder(String statusOrder) {
        this.statusOrder = statusOrder;
    }

    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getJamOrder() {
        return jamOrder;
    }

    public void setJamOrder(String jamOrder) {
        this.jamOrder = jamOrder;
    }

    public String getTanggalOrder() {
        return tanggalOrder;
    }

    public void setTanggalOrder(String tanggalOrder) {
        this.tanggalOrder = tanggalOrder;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }

    public String getTotalPesanan() {
        return totalPesanan;
    }

    public void setTotalPesanan(String totalPesanan) {
        this.totalPesanan = totalPesanan;
    }
}
