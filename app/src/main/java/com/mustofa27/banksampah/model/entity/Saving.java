package com.mustofa27.banksampah.model.entity;



public class Saving {
    public static final String table_name = "saving", column_id = "id";
    private int id;
    private float weight;
    private long total_price;
    private int status;
    private int garbage_id;
    private String created_at;
    private String updated_at;
    private Garbage garbage;
    public Saving() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public long getTotal_price() {
        return total_price;
    }

    public void setTotal_price(long total_price) {
        this.total_price = total_price;
    }
    public String getStatusText() {
        return status == 0 ? "Menunggu" : (status == 1 ? "Diterima" : "Ditolak");
    }
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getGarbage_id() {
        return garbage_id;
    }

    public void setGarbage_id(int garbage_id) {
        this.garbage_id = garbage_id;
    }

    public Garbage getGarbage() {
        return garbage;
    }

    public void setGarbage(Garbage garbage) {
        this.garbage = garbage;
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
