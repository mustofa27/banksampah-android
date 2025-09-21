package com.mustofa27.banksampah.model.entity;


import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Ahmad Mustofa, PT Trikarya Teknologi on 4/1/2016.
 */
@Entity
public class Product implements Serializable {
    public static final String table_name = "Product",
            column_id = "id";
    @PrimaryKey
    @SerializedName("id")
    @NonNull
    private String id;
    private int divisi_id, brand_id, qty = 0;
    private String kode, nama;
    @Ignore
    private Satuan satuan;
    private long harga;
    private String created_at, updated_at;
    @Ignore
    private boolean isSelected = false;
    @Ignore
    private transient ArrayAdapter satuanAdapter;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDivisi_id() {
        return divisi_id;
    }

    public void setDivisi_id(int divisi_id) {
        this.divisi_id = divisi_id;
    }

    public int getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(int brand_id) {
        this.brand_id = brand_id;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Satuan getSatuan() {
        return satuan;
    }

    public void setSatuan(Satuan satuan) {
        this.satuan = satuan;
    }

    public long getHarga() {
        return harga;
    }

    public void setHarga(long harga) {
        this.harga = harga;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public ArrayAdapter getSatuanAdapter() {
        return satuanAdapter;
    }

    public void setSatuanAdapter(ArrayAdapter satuanAdapter) {
        this.satuanAdapter = satuanAdapter;
    }

    public Product() {
    }



    @Override
    public String toString() {
        return getNama();
    }
}
