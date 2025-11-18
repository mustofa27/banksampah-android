package com.mustofa27.banksampah.model.entity;

import com.mustofa27.banksampah.model.helper.CustomActivityHelper;

import java.util.Calendar;

public class Discount {
    private int id;
    private int product_id;
    private String start_at;
    private String end_at;
    private String created_at;
    private String updated_at;
    private int percentage;
    Product product;

    public Discount() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getStart_at() {
        return start_at;
    }

    public void setStart_at(String start_at) {
        this.start_at = start_at;
    }

    public String getEnd_at() {
        return end_at;
    }

    public void setEnd_at(String end_at) {
        this.end_at = end_at;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }
    public boolean isValid(){
        return isDateTodayOrBefore(start_at) && isDateTodayOrAfter(end_at);
    }

    private boolean isDateTodayOrAfter(String date) {
        String regex = date.contains("T") ? "T" : " ";
        String[] tmp = date.split(regex);
        String[] tgl = tmp[0].split("-");
        Calendar now = Calendar.getInstance();
        Calendar temp = Calendar.getInstance();
        temp.set(Calendar.DAY_OF_MONTH, Integer.valueOf(tgl[2]));
        temp.set(Calendar.MONTH, Integer.valueOf(tgl[1]) - 1);
        temp.set(Calendar.YEAR, Integer.valueOf(tgl[0]));
        return temp.getTimeInMillis() >= now.getTimeInMillis();
    }

    private boolean isDateTodayOrBefore(String date) {
        String regex = date.contains("T") ? "T" : " ";
        String[] tmp = date.split(regex);
        String[] tgl = tmp[0].split("-");
        Calendar temp = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        temp.set(Calendar.DAY_OF_MONTH, Integer.valueOf(tgl[2]));
        temp.set(Calendar.MONTH, Integer.valueOf(tgl[1]) - 1);
        temp.set(Calendar.YEAR, Integer.valueOf(tgl[0]));
        return temp.getTimeInMillis() <= now.getTimeInMillis();
    }
}
