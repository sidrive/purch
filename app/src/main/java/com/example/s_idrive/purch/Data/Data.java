package com.example.s_idrive.purch.Data;


/**
 * Created by IT Server on 4/10/2017.
 */

public class Data {
    private String id, nama, alamat, barcode, jumlah, total;
    private String idpo, kodesup, tanggal, totalpo, maga, suplier;


    public Data() {
    }

    public Data(String id, String nama, String alamat, String barcode, String jumlah, String total,
                String idpo, String kodesup, String tanggal, String totalpo, String maga, String suplier) {
        this.id = id;
        this.nama = nama;
        this.alamat = alamat;
        this.barcode = barcode;
        this.jumlah = jumlah;
        this.total = total;

        this.idpo   = idpo;
        this.kodesup    = kodesup;
        this.tanggal    = tanggal;
        this.totalpo    = totalpo;
        this.maga       = maga;
        this.suplier    = suplier;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String harga) {
        this.barcode = harga;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }


    //method untuk daftar PO dalam Proses
    public String getIdpo() {
        return idpo;
    }

    public void setIdpo(String idpo) {
        this.idpo = idpo;
    }

    public String getKodesup() {
        return kodesup;
    }

    public void setKodesup(String kodesup) {
        this.kodesup = kodesup;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getTotalpo() {
        return totalpo;
    }

    public void setTotalpo(String totalpo) {
        this.totalpo = totalpo;
    }

    public String getMaga() {
        return maga;
    }

    public void setMaga(String maga) {
        this.maga = maga;
    }

    public String getSuplier() {
        return suplier;
    }

    public void setSuplier(String suplier) {
        this.suplier = suplier;
    }
}
