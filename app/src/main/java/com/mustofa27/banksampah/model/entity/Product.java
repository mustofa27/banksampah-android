package com.mustofa27.banksampah.model.entity;


import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ahmad Mustofa, PT Trikarya Teknologi on 4/1/2016.
 */
@Entity
public class Product implements Serializable {
    public static final String table_name = "Product",
            column_id = "id";
    @PrimaryKey
    @SerializedName("id")
    @NonNull
    private int id;
    private String name;
    private String description;
    private Long price;
    private int stock;
    private int point;
    private String created_at, updated_at;
    @Ignore
    ArrayList<Discount> discounts;
    @Ignore
    ArrayList<ProductComment> comments;
    @Ignore
    ArrayList<Wishlist> wishlists;
    String image_path;

    public Product() {
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

    public Long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
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

    public ArrayList<Discount> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(ArrayList<Discount> discounts) {
        this.discounts = discounts;
    }

    public ArrayList<ProductComment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<ProductComment> comments) {
        this.comments = comments;
    }

    public ArrayList<Wishlist> getWishlists() {
        return wishlists;
    }

    public void setWishlists(ArrayList<Wishlist> wishlists) {
        this.wishlists = wishlists;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return getName();
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }
}
