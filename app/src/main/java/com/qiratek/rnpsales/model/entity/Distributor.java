package com.qiratek.rnpsales.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ahmad Mustofa, PT Trikarya Teknologi on 3/27/2016.
 */
@Entity
public class Distributor {
    public static final String table_name = "distributor", column_id = "uuid", column_kd_dist = "kd_dist", column_kd_tipe = "kd_tipe",
            column_kd_kota = "kd_kota", column_nm_dist = "nama", column_alamat = "almt_dist", column_telepon = "telp_dist";

    @PrimaryKey
    @SerializedName("id")
    @ColumnInfo(name = column_id)
    @NonNull
    private String id;
    private int kota_id;
    private String kode, tipe, nama, alamat, telepon;

    public Distributor() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getKode() {
        return kode;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public String getTipe() {
        return tipe;
    }

    public void setKota_id(int kota_id) {
        this.kota_id = kota_id;
    }

    public int getKota_id() {
        return kota_id;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNama() {
        return nama;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }

    public String getTelepon() {
        return telepon;
    }

    @Override
    public String toString() {
        return getNama();
    }
}
