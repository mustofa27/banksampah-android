package com.mustofa27.banksampah.model.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Garbage {
    public static final String table_name = "garbage", column_id = "id";
    @PrimaryKey
    private int id;
    private String name;
    private int category;
    private String description;
    private long price_per_kg;
    private String created_at;
    private String updated_at;

    public Garbage() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getPrice_per_kg() {
        return price_per_kg;
    }

    public void setPrice_per_kg(long price_per_kg) {
        this.price_per_kg = price_per_kg;
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
