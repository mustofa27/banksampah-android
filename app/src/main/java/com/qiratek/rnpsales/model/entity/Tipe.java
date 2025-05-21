package com.qiratek.rnpsales.model.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by Ahmad Mustofa, PT Trikarya Teknologi on 3/29/2016.
 */
@Entity
public class Tipe implements Serializable {
    public static final String TABLE_TIPE = "Tipe",
            KEY_KODE_TIPE = "id",
            KEY_NAMA_TIPE = "nama";

    @PrimaryKey
    private int id;
    private String nama;
    private String created_at;
    private String updated_at;
    public Tipe(int id, String nama)
    {
        setId(id);
        setNama(nama);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNama() {
        return nama;
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
        return getNama();
    }
}
