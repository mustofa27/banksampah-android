package com.mustofa27.banksampah.model.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Ahmad Mustofa, PT Trikarya Teknologi on 3/19/2016.
 */

@Entity
public class City implements Serializable {
    public static final String table_name = "city", column_id = "id", column_kd_area = "divisi_id", column_nama = "nama";

    @PrimaryKey
    private int id;
    @ColumnInfo(name = column_kd_area)
    @SerializedName(column_kd_area)
    private int kd_area;
    @ColumnInfo(name = column_nama)
    @SerializedName(column_nama)
    private String nm_kota;
    private String kode;
    private String created_at;
    private String updated_at;

    public City() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getKd_area() {
        return kd_area;
    }

    public void setKd_area(int kd_area) {
        this.kd_area = kd_area;
    }

    public String getNm_kota() {
        return nm_kota;
    }

    public void setNm_kota(String nm_kota) {
        this.nm_kota = nm_kota;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
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

    @Override
    public String toString() {
        return getNm_kota();
    }
}
