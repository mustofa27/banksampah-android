package com.mustofa27.banksampah.model.entity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ahmad Mustofa, PT Trikarya Teknologi on 4/29/2016.
 */
public class BlackListApp{
    public static String table_name = "black_app",
            column_id = "id",
            column_name = "nm_app";
    private int id;
    private String name;

    public BlackListApp(JSONObject jsonObject) throws JSONException {
        setId(jsonObject.has(column_id) ? jsonObject.getInt(column_id) : 0);
        setName(jsonObject.has(column_name) ? jsonObject.getString(column_name) : "");
    }

    public BlackListApp(int id, String name) {
        this.id = id;
        this.name = name;
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
        this.name= name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
