package com.qiratek.rnpsales.model.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.qiratek.rnpsales.view.adapter.GenericRecyclerAdapter;

import java.io.Serializable;
import java.util.ArrayList;
@Entity
public class Brand implements Serializable {
    public static final String
            table_name = "Brand",
            column_id = "id",
            column_name = "nama",
            column_kode = "kode",
            column_urutan = "urutan",
            column_count = "count",
            column_id_satuan = "satuan_id";

    @PrimaryKey
    private int id;
    private int urutan;
    @SerializedName(column_id_satuan)
    private int id_satuan;
    private int count;
    @SerializedName(column_name)
    private String name;
    private String kode;
    private String created_at;
    @Ignore
    private transient ArrayList<Product> all;
    @Ignore
    private transient ArrayList<Product> filtered;
    @Ignore
    private transient GenericRecyclerAdapter productAdapter;
    private int defaultSatuan = -1;

    public Brand() {
    }

    @Ignore
    public Brand(int id, String name, String kode, int urutan, int count, int id_satuan){
        this.id = id;
        this.name = name;
        this.kode = kode;
        this.urutan = urutan;
        this.count = count;
        this.id_satuan = id_satuan;
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

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public int getUrutan() {
        return urutan;
    }

    public void setUrutan(int urutan) {
        this.urutan = urutan;
    }

    public int getId_satuan() {
        return id_satuan;
    }

    public void setId_satuan(int id_satuan) {
        this.id_satuan = id_satuan;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public GenericRecyclerAdapter getProductAdapter() {
        return productAdapter;
    }

    public void setProductAdapter(GenericRecyclerAdapter productAdapter) {
        this.productAdapter = productAdapter;
    }

    public ArrayList<Product> getAll() {
        return all;
    }

    public void setAll(ArrayList<Product> all) {
        this.all = all;
    }

    public ArrayList<Product> getFiltered() {
        return filtered;
    }

    public void setFiltered(ArrayList<Product> filtered) {
        this.filtered = filtered;
    }

    public int getDefaultSatuan() {
        return defaultSatuan;
    }

    public void setDefaultSatuan(int defaultSatuan) {
        this.defaultSatuan = defaultSatuan;
    }

    @Override
    public String toString() {
        return getKode() + " - " + getName();
    }

}
