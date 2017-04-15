package com.example.s_idrive.purch.Model;

/**
 * Created by s_idrive on 08-Apr-17.
 */

import com.example.s_idrive.purch.BaseApp;


public class detailPO extends BaseApp{

    private String barcode, nama_brg, harga_sup, jumlah;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getNama_brg() {
        return nama_brg;
    }

    public void setNama_brg(String nama_brg) {
        this.nama_brg = nama_brg;
    }

    public Integer getHarga_sup() {
        return Integer.valueOf(harga_sup);
    }

    public void setHarga_sup(Integer harga_sup) {
        this.harga_sup = String.valueOf(harga_sup);
    }

    public Integer getJumlah() {
        return Integer.valueOf(jumlah);
    }

    public void setJumlah(Integer jumlah) {
        this.jumlah = String.valueOf(jumlah);
    }
}
