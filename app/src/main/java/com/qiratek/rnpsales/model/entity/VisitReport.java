package com.qiratek.rnpsales.model.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class VisitReport implements Serializable {

    public static final String table_name = "VisitReport", column_id = "id";

    @PrimaryKey
    @NonNull
    private String id;
    private String outlet_id;
    private String note;
    private String path_image_1;
    private String path_image_2;
    private String path_image_3;

    public VisitReport() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOutlet_id() {
        return outlet_id;
    }

    public void setOutlet_id(String outlet_id) {
        this.outlet_id = outlet_id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPath_image_1() {
        return path_image_1;
    }

    public void setPath_image_1(String path_image_1) {
        this.path_image_1 = path_image_1;
    }

    public String getPath_image_2() {
        return path_image_2;
    }

    public void setPath_image_2(String path_image_2) {
        this.path_image_2 = path_image_2;
    }

    public String getPath_image_3() {
        return path_image_3;
    }

    public void setPath_image_3(String path_image_3) {
        this.path_image_3 = path_image_3;
    }
}
