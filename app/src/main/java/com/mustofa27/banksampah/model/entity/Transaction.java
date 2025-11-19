package com.mustofa27.banksampah.model.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class Transaction implements Serializable {
    private int id;
    private int user_id;
    private int total_price;
    private int status;
    private int unique_code;
    private int balance_used;
    private String image_path;
    private int total_point;
    private int total_discount;
    private ArrayList<TransactionItem> items;
    private User user;
    private String created_at;
    private String updated_at;

    public Transaction() {
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

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUnique_code() {
        return unique_code;
    }

    public void setUnique_code(int unique_code) {
        this.unique_code = unique_code;
    }

    public int getBalance_used() {
        return balance_used;
    }

    public void setBalance_used(int balance_used) {
        this.balance_used = balance_used;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public int getTotal_point() {
        return total_point;
    }

    public void setTotal_point(int total_point) {
        this.total_point = total_point;
    }

    public int getTotal_discount() {
        return total_discount;
    }

    public void setTotal_discount(int total_discount) {
        this.total_discount = total_discount;
    }

    public ArrayList<TransactionItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<TransactionItem> items) {
        this.items = items;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public String getStatusText() {
        return status == 0 ? "Menunggu" : (status == 1 ? "Diterima" : (status == 2 ? "Selesai" : "Ditolak"));
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
