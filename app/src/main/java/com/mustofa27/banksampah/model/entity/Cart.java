package com.mustofa27.banksampah.model.entity;

public class Cart {
    private int id;
    private int user_id;
    private int product_id;
    private Product product;
    private int count = 0;
    private long subtotal, subtotal_discount;
    private int subtotal_point;
    private boolean selected = false;

    public Cart() {
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

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(long subtotal) {
        this.subtotal = subtotal;
    }

    public int getSubtotal_point() {
        return subtotal_point;
    }

    public void setSubtotal_point(int subtotal_point) {
        this.subtotal_point = subtotal_point;
    }

    public void countTotal(){
        subtotal = product.getPrice()*count;
        subtotal_point = product.getPoint()*count;
        if(product.getValidDiscount() != null){
            subtotal = (100-product.getValidDiscount().getPercentage())*subtotal/100;
            subtotal_discount = product.getValidDiscount().getPercentage()*subtotal/100;
        }
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public long getSubtotal_discount() {
        return subtotal_discount;
    }

    public void setSubtotal_discount(long subtotal_discount) {
        this.subtotal_discount = subtotal_discount;
    }
}
