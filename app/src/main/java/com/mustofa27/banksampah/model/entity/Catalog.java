package com.mustofa27.banksampah.model.entity;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class Catalog {
    public static String
            table_name = "catalog",
            column_id = "id",
            column_name = "nama",
            column_id_brand = "id_brand",
            column_path_image = "path_image";

    private int id, brand_id;
    private String nama;
    @SerializedName("gambar")
    private String path_image;

    public Catalog(JSONObject jsonObject) throws JSONException {
        setId(jsonObject.has(column_id) ? jsonObject.getInt(column_id) : 0);
        setBrand_id(jsonObject.has(column_id_brand) ? jsonObject.getInt(column_id_brand) : 0);
        setNama(jsonObject.has(column_name) ? jsonObject.getString(column_name) : "");
        setPath_image(jsonObject.has(column_path_image) ? jsonObject.getString(column_path_image) : "");
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

}
