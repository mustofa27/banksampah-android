package com.mustofa27.banksampah.model.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserPersonalize {
    public static final String table_name = "UserPersonalize", column_id = "id";
    public static final int TAMPIL_DETAIL = 1, TAMPIL_SINGKAT = 0;

    @PrimaryKey
    private int id;
    private int user_id;
    private int pilihan_tampil;
    private String created_at;
    private String updated_at;

    public UserPersonalize() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getPilihan_tampil() {
        return pilihan_tampil;
    }

    public void setPilihan_tampil(int pilihan_tampil) {
        this.pilihan_tampil = pilihan_tampil;
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
