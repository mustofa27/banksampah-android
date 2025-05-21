package com.qiratek.rnpsales.model.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class BillingData {
    public static final String table_name = "BillingData", column_id = "id", column_nama_bank = "nama_bank", column_nomor_giro = "nomor_giro",
            column_outlet_id = "outlet_id", column_kd_user = "kd_user", column_status = "status",
            column_metode_pembayaran = "metode_pembayaran", column_nilai_transfer = "nilai_transfer", column_nilai_cash = "nilai_cash",
            column_nominal_giro = "nominal_giro", column_due_date = "due_date", column_submit_at = "submit_at", column_note = "note";

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String outlet_id;
    private int user_id;
    private int status;
    @SerializedName("metode_pembayaran")
    private int payment_method;
    @SerializedName("nilai_transfer")
    private int transfer_value;
    @SerializedName("nilai_cash")
    private int cash_value;
    private int nominal_giro;
    @SerializedName("nama_bank")
    private String bank_nm;
    private String nomor_giro;
    private String due_date;
    private String note;
    private String gambar;
    private String created_at;
    private String updated_at;
    @Ignore
    private Outlet outlets;

    public BillingData() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOutlet_id() {
        return outlet_id;
    }

    public void setOutlet_id(String outlet_id) {
        this.outlet_id = outlet_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(int payment_method) {
        this.payment_method = payment_method;
    }

    public int getTransfer_value() {
        return transfer_value;
    }

    public void setTransfer_value(int transfer_value) {
        this.transfer_value = transfer_value;
    }

    public int getCash_value() {
        return cash_value;
    }

    public void setCash_value(int cash_value) {
        this.cash_value = cash_value;
    }

    public int getNominal_giro() {
        return nominal_giro;
    }

    public void setNominal_giro(int nominal_giro) {
        this.nominal_giro = nominal_giro;
    }

    public String getBank_nm() {
        return bank_nm;
    }

    public void setBank_nm(String bank_nm) {
        this.bank_nm = bank_nm;
    }

    public String getNomor_giro() {
        return nomor_giro;
    }

    public void setNomor_giro(String nomor_giro) {
        this.nomor_giro = nomor_giro;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
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
}
