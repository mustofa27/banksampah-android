package com.mustofa27.banksampah.model.entity;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Ahmad Mustofa, PT Trikarya Teknologi on 3/23/2016.
 */
@Entity
public class VisitPlanDb implements Serializable{
    public static final String table_name = "VisitPlanDb", column_id = "uuid", column_kd_outlet = "kd_outlet", column_date_visit = "date_visit",
            column_date_create_visit = "date_create_visit", column_approve_visit = "approve_visit", column_status_visit = "status_visit",
            column_date_visiting = "date_visiting", column_skip_order_reason = "skip_order_reason", column_skip_reason = "skip_reason",
            column_is_checkout = "is_checkout", column_created_at = "created_at";

    @PrimaryKey
    @ColumnInfo(name = column_id)
    @SerializedName("id")
    @NonNull
    private String id;
    private String outlet_id;
    private String date_visit;
    private String date_created_visit;
    private int approve_visit;
    private int status_visit;
    private String date_visiting;
    private int user_id;
    private String skip_order_reason;
    private String skip_reason;
    private int if_close;
    private String foto_if_close;
    private String keterangan_if_close;
    private int is_checkout;
    private String date_checkout;
    private int starttime;
    private int endtime;
    private String gambar;
    private String created_at;
    private String updated_at;
    @Ignore
    private Outlet outlets;
    private String outletJson;

    public VisitPlanDb() {
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

    public String getDate_visit() {
        return date_visit;
    }

    public void setDate_visit(String date_visit) {
        this.date_visit = date_visit;
    }

    public String getDate_created_visit() {
        return date_created_visit;
    }

    public void setDate_created_visit(String date_created_visit) {
        this.date_created_visit = date_created_visit;
    }

    public int getApprove_visit() {
        return approve_visit;
    }

    public void setApprove_visit(int approve_visit) {
        this.approve_visit = approve_visit;
    }

    public int getStatus_visit() {
        return status_visit;
    }

    public void setStatus_visit(int status_visit) {
        this.status_visit = status_visit;
    }

    public String getDate_visiting() {
        return date_visiting;
    }

    public void setDate_visiting(String date_visiting) {
        this.date_visiting = date_visiting;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getSkip_order_reason() {
        return skip_order_reason;
    }

    public void setSkip_order_reason(String skip_order_reason) {
        this.skip_order_reason = skip_order_reason;
    }

    public String getSkip_reason() {
        return skip_reason;
    }

    public void setSkip_reason(String skip_reason) {
        this.skip_reason = skip_reason;
    }

    public int getIf_close() {
        return if_close;
    }

    public void setIf_close(int if_close) {
        this.if_close = if_close;
    }

    public String getFoto_if_close() {
        return foto_if_close;
    }

    public void setFoto_if_close(String foto_if_close) {
        this.foto_if_close = foto_if_close;
    }

    public String getKeterangan_if_close() {
        return keterangan_if_close;
    }

    public void setKeterangan_if_close(String keterangan_if_close) {
        this.keterangan_if_close = keterangan_if_close;
    }

    public int getIs_checkout() {
        return is_checkout;
    }

    public void setIs_checkout(int is_checkout) {
        this.is_checkout = is_checkout;
    }

    public String getDate_checkout() {
        return date_checkout;
    }

    public void setDate_checkout(String date_checkout) {
        this.date_checkout = date_checkout;
    }

    public int getStarttime() {
        return starttime;
    }

    public void setStarttime(int starttime) {
        this.starttime = starttime;
    }

    public int getEndtime() {
        return endtime;
    }

    public void setEndtime(int endtime) {
        this.endtime = endtime;
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

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public Outlet getOutlets() {
        return outlets;
    }

    public void setOutlets(Outlet outlets) {
        this.outlets = outlets;
    }

    public String getOutletJson() {
        return outletJson;
    }

    public void setOutletJson(String outletJson) {
        this.outletJson = outletJson;
    }
}
