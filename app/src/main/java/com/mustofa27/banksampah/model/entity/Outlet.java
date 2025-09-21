package com.mustofa27.banksampah.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Ahmad Mustofa, PT Trikarya Teknologi on 3/19/2016.
 */

@Entity
public class Outlet implements Serializable {
    public static final String table_name = "outlet", column_id = "uuid", column_kd_kota = "kota_id", column_kd_user = "user_id",
            column_kd_dist = "distributor_id", column_nm_outlet = "nama", column_almt_outlet = "alamat", column_kd_tipe = "tipe_id",
            column_rank_outlet = "rank", column_kodepos = "kodepos", column_reg_status = "reg_status", column_latitude = "latitude",
            column_longitude = "longitude", column_nm_pic = "nama_pic", column_tlp_pic = "telepon_pic", column_foto = "gambar",
            column_status_aktivasi = "status_aktivasi";

    @PrimaryKey
    @SerializedName("id")
    @ColumnInfo(name = column_id)
    @NonNull
    private String kd_outlet;
    @ColumnInfo(name = column_kd_kota)
    @SerializedName(column_kd_kota)
    private int kd_kota;
    @ColumnInfo(name = column_kd_dist)
    @SerializedName(column_kd_dist)
    private int kd_dist;
    @ColumnInfo(name = column_kd_user)
    @SerializedName(column_kd_user)
    private int kd_user;
    @ColumnInfo(name = column_kd_tipe)
    @SerializedName(column_kd_tipe)
    private int kd_tipe;
    @ColumnInfo(name = column_nm_outlet)
    @SerializedName(column_nm_outlet)
    private String nm_outlet;
    @ColumnInfo(name = column_nm_pic)
    @SerializedName(column_nm_pic)
    private String nm_pic;
    @ColumnInfo(name = column_almt_outlet)
    @SerializedName(column_almt_outlet)
    private String almt_outlet;
    @ColumnInfo(name = column_rank_outlet)
    @SerializedName(column_rank_outlet)
    private String rank_outlet;
    @ColumnInfo(name = column_kodepos)
    @SerializedName(column_kodepos)
    private String kodepos;
    @ColumnInfo(name = column_reg_status)
    @SerializedName(column_reg_status)
    private String reg_status;
    @ColumnInfo(name = column_longitude)
    private String longitude;
    @ColumnInfo(name = column_latitude)
    private String latitude;
    @ColumnInfo(name = column_tlp_pic)
    @SerializedName(column_tlp_pic)
    private String tlp_pic;
    @ColumnInfo(name = column_foto)
    @SerializedName(column_foto)
    private String foto;
    @ColumnInfo(name = column_status_aktivasi)
    private int status_aktivasi;
    private int toleransi_jarak;
    private int status_area;
    private String created_at;
    private String updated_at;
    private int divisi_id;
    private String kode;

    @Ignore
    Tipe tipe;

    @Ignore
    City city;

    public Outlet() {
    }

    public String getTlp_pic() {
        return tlp_pic;
    }

    public void setTlp_pic(String tlp_pic) {
        this.tlp_pic = tlp_pic;
    }

    public String getNm_pic() {
        return nm_pic;
    }

    public void setNm_pic(String nm_pic) {
        this.nm_pic = nm_pic;
    }

    public String getKd_outlet() {
        return kd_outlet;
    }

    public void setKd_outlet(String kd_outlet) {
        this.kd_outlet = kd_outlet;
    }

    public int getKd_kota() {
        return kd_kota;
    }

    public void setKd_kota(int kd_kota) {
        this.kd_kota = kd_kota;
    }

    public int getKd_dist() {
        return kd_dist;
    }

    public void setKd_dist(int kd_dist) {
        this.kd_dist = kd_dist;
    }

    public int getKd_user() {
        return kd_user;
    }

    public void setKd_user(int kd_user) {
        this.kd_user = kd_user;
    }

    public String getNm_outlet() {
        return nm_outlet;
    }

    public void setNm_outlet(String nm_outlet) {
        this.nm_outlet = nm_outlet;
    }

    public String getAlmt_outlet() {
        return almt_outlet;
    }

    public void setAlmt_outlet(String almt_outlet) {
        this.almt_outlet = almt_outlet;
    }

    public int getKd_tipe() {
        return kd_tipe;
    }

    public void setKd_tipe(int kd_tipe) {
        this.kd_tipe = kd_tipe;
    }

    public String getRank_outlet() {
        return rank_outlet;
    }

    public void setRank_outlet(String rank_outlet) {
        this.rank_outlet = rank_outlet;
    }

    public String getKodepos() {
        return kodepos;
    }

    public void setKodepos(String kodepos) {
        this.kodepos = kodepos;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getReg_status() {
        return reg_status;
    }

    public void setReg_status(String reg_status) {
        this.reg_status = reg_status;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }


    public int getStatus_aktivasi() {
        return status_aktivasi;
    }

    public void setStatus_aktivasi(int status_aktivasi) {
        this.status_aktivasi = status_aktivasi;
    }

    public Tipe getTipe() {
        return tipe;
    }

    public void setTipe(Tipe tipe) {
        this.tipe = tipe;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public int getToleransi_jarak() {
        return toleransi_jarak;
    }

    public void setToleransi_jarak(int toleransi_jarak) {
        this.toleransi_jarak = toleransi_jarak;
    }

    public int getStatus_area() {
        return status_area;
    }

    public void setStatus_area(int status_area) {
        this.status_area = status_area;
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

    public int getDivisi_id() {
        return divisi_id;
    }

    public void setDivisi_id(int divisi_id) {
        this.divisi_id = divisi_id;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }


    @Override
    public String toString() {
        return this.getNm_outlet();
    }
}
