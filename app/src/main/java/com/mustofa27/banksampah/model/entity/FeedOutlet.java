package com.mustofa27.banksampah.model.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FeedOutlet implements Serializable {
    private String uuid;
    private int type;
    @SerializedName("nama")
    private String nm_photo;
    private String note;
    @SerializedName("date_take_photo")
    private String date_upload;
    @SerializedName("gambar")
    private String gambar1;
    @SerializedName("path_image_2")
    private String gambar2;
    @SerializedName("path_image_3")
    private String gambar3;
    private Outlet outlets;

    public FeedOutlet() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getNm_photo() {
        return nm_photo;
    }

    public void setNm_photo(String nm_photo) {
        this.nm_photo = nm_photo;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate_upload() {
        return date_upload;
    }

    public void setDate_upload(String date_upload) {
        this.date_upload = date_upload;
    }

    public String getGambar1() {
        return gambar1;
    }

    public void setGambar1(String gambar1) {
        this.gambar1 = gambar1;
    }

    public String getGambar2() {
        return gambar2;
    }

    public void setGambar2(String gambar2) {
        this.gambar2 = gambar2;
    }

    public String getGambar3() {
        return gambar3;
    }

    public void setGambar3(String gambar3) {
        this.gambar3 = gambar3;
    }

    public Outlet getOutlets() {
        return outlets;
    }

    public void setOutlets(Outlet outlets) {
        this.outlets = outlets;
    }
}
