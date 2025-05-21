package com.qiratek.rnpsales.model.entity;

import com.google.gson.annotations.SerializedName;

public class PriceList {

    private int id, brand_id, kota_id;
    private String nama;
    @SerializedName("gambar")
    private String path_image;
    private String created_at, updated_at;

    public PriceList() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(int brand_id) {
        this.brand_id = brand_id;
    }

    public int getKota_id() {
        return kota_id;
    }

    public void setKota_id(int kota_id) {
        this.kota_id = kota_id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPath_image() {
        return path_image;
    }

    public void setPath_image(String path_image) {
        this.path_image = path_image;
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
}
