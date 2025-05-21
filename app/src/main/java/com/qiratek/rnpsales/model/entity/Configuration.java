package com.qiratek.rnpsales.model.entity;

public class Configuration {
    private int id;
    private int toleransi_max;
    private int target_visit;
    private int target_ec;
    private int toleransi_max_office;
    private int toleransi_draft;
    private String created_at;
    private String updated_at;

    public static String TOLERANSI = "TOLERANSI", TOLERANSI_DRAFT = "TOLERANSI_DRAFT";

    public Configuration() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getToleransi_max() {
        return toleransi_max;
    }

    public void setToleransi_max(int toleransi_max) {
        this.toleransi_max = toleransi_max;
    }

    public int getTarget_visit() {
        return target_visit;
    }

    public void setTarget_visit(int target_visit) {
        this.target_visit = target_visit;
    }

    public int getTarget_ec() {
        return target_ec;
    }

    public void setTarget_ec(int target_ec) {
        this.target_ec = target_ec;
    }

    public int getToleransi_max_office() {
        return toleransi_max_office;
    }

    public void setToleransi_max_office(int toleransi_max_office) {
        this.toleransi_max_office = toleransi_max_office;
    }

    public int getToleransi_draft() {
        return toleransi_draft;
    }

    public void setToleransi_draft(int toleransi_draft) {
        this.toleransi_draft = toleransi_draft;
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
