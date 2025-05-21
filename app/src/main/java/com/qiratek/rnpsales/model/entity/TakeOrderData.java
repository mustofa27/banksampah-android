package com.qiratek.rnpsales.model.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TakeOrderData {

    public static final String table_name = "TakeOrderData", column_id = "id";

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String jsonContent;

    public TakeOrderData() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJsonContent() {
        return jsonContent;
    }

    public void setJsonContent(String jsonContent) {
        this.jsonContent = jsonContent;
    }
}
