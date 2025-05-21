package com.qiratek.rnpsales.model.entity;

import com.google.gson.annotations.SerializedName;
import com.qiratek.rnpsales.view.adapter.GenericRecyclerAdapter;

/**
 * Created by Ahmad Mustofa, PT Trikarya Teknologi on 4/1/2016.
 */
public class TakeOrder {
    @SerializedName("id")
    private String uuid;
    private int user_id;
    private String alias;
    private String outlet_id;
    private int satuan_id;
    private int produk_id;
    @SerializedName("kode")
    private String kode_order;
    private String kode_visit, satuan;
    @SerializedName("qty_order")
    private int quantity;

    private int status_order;
    private int sku_baru;
    @SerializedName("catatan")
    private String note;
    @SerializedName("other_produk")
    private String other_product;
    private String status, comment, date_order, created_at, updated_at;
    private Outlet outlets;
    private boolean isVisible = false, isHasSetVisible = false;
    private GenericRecyclerAdapter adapter;

    public TakeOrder() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getOutlet_id() {
        return outlet_id;
    }

    public void setOutlet_id(String outlet_id) {
        this.outlet_id = outlet_id;
    }

    public int getSatuan_id() {
        return satuan_id;
    }

    public void setSatuan_id(int satuan_id) {
        this.satuan_id = satuan_id;
    }

    public int getProduk_id() {
        return produk_id;
    }

    public void setProduk_id(int produk_id) {
        this.produk_id = produk_id;
    }

    public String getKode_order() {
        return kode_order;
    }

    public void setKode_order(String kode_order) {
        this.kode_order = kode_order;
    }

    public String getKode_visit() {
        return kode_visit;
    }

    public void setKode_visit(String kode_visit) {
        this.kode_visit = kode_visit;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getStatus_order() {
        return status_order;
    }

    public void setStatus_order(int status_order) {
        this.status_order = status_order;
    }

    public int getSku_baru() {
        return sku_baru;
    }

    public void setSku_baru(int sku_baru) {
        this.sku_baru = sku_baru;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getOther_product() {
        return other_product;
    }

    public void setOther_product(String other_product) {
        this.other_product = other_product;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate_order() {
        return date_order;
    }

    public void setDate_order(String date_order) {
        this.date_order = date_order;
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

    public Outlet getOutlets() {
        return outlets;
    }

    public void setOutlets(Outlet outlets) {
        this.outlets = outlets;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public boolean isHasSetVisible() {
        return isHasSetVisible;
    }

    public void setHasSetVisible(boolean hasSetVisible) {
        isHasSetVisible = hasSetVisible;
    }

    public GenericRecyclerAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(GenericRecyclerAdapter adapter) {
        this.adapter = adapter;
    }
}
