package com.qiratek.rnpsales.model.entity;

/**
 * Created by Ahmad Mustofa, PT Trikarya Teknologi on 4/29/2016.
 */
public class TipePhoto{
    public static String TABLE_TIPE_PHOTO = "TipePhoto",
            KEY_ID_TIPE = "id",
            KEY_NM_TIPE = "nama_tipe",
            KEY_PARENT = "has_parent";
    private int id;
    private String nama_tipe;
    private int parent;

    public TipePhoto(int id, String nama_tipe, int parent) {
        this.id = id;
        this.nama_tipe = nama_tipe;
        setParent(parent);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama_tipe() {
        return nama_tipe;
    }

    public void setNama_tipe(String nama_tipe) {
        this.nama_tipe = nama_tipe;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return getNama_tipe();
    }
}
