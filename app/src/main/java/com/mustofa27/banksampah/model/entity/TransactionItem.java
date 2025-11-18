package com.mustofa27.banksampah.model.entity;

public class TransactionItem {
    private int id;
    private int transaction_id;
    private int product_id;
    private int quantity;
    private int subtotal_price;
    private int subtotal_discount;
    private int subtotal_point;
    private String created_at;
    private String updated_at;
    private Product product;

    public TransactionItem() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSubtotal_price() {
        return subtotal_price;
    }

    public void setSubtotal_price(int subtotal_price) {
        this.subtotal_price = subtotal_price;
    }

    public int getSubtotal_discount() {
        return subtotal_discount;
    }

    public void setSubtotal_discount(int subtotal_discount) {
        this.subtotal_discount = subtotal_discount;
    }

    public int getSubtotal_point() {
        return subtotal_point;
    }

    public void setSubtotal_point(int subtotal_point) {
        this.subtotal_point = subtotal_point;
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
