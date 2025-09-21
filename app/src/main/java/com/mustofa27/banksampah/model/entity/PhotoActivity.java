package com.mustofa27.banksampah.model.entity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ahmad Mustofa, PT Trikarya Teknologi on 4/10/2016.
 */
public class PhotoActivity {
    public static String TABLE_PHOTO = "photo",
            KEY_KODE_PHOTO = "kd_photo",
            KEY_KODE_KOMPETITOR = "kd_kompetitor",
            KEY_NAMA_PHOTO = "nm_photo",
            KEY_JENIS_PHOTO = "jenis_photo",
            KEY_DATE_TAKE_PHOTO = "date_take_photo",
            KEY_ALAMAT_PHOTO = "alamat_comp_activity",
            KEY_DATE_UPLOAD_PHOTO = "date_upload_photo",
            KEY_KETERANGAN = "keterangan",
            KEY_KODE_USER = "kd_user",
            KEY_FOTO = "foto",
            KEY_KODE_OUTLET = "kd_outlet",
            KEY_NAMA_CHILD = "child";
    private int id, kd_user, kd_outlet, kd_kompetitor, kd_tipe;
    private String nama, tgl_take, tgl_upload, alamat, keterangan, foto;
    private int child;

    public PhotoActivity(int id, int kd_user, int kd_outlet, int kd_kompetitor, int kd_tipe, String nama, String tgl_take, String alamat, String keterangan, String tgl_upload, String foto, int child) throws JSONException {
        setId(id);
        setKd_user(kd_user);
        setKd_outlet(kd_outlet);
        setKd_kompetitor(kd_kompetitor);
        setKd_tipe(kd_tipe);
        setNama(nama);
        setTgl_take(tgl_take);
        setAlamat(alamat);
        setKeterangan(keterangan);
        setTgl_upload(tgl_upload);
        setFoto(foto);
        setChild(child);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(KEY_KODE_PHOTO, getId());
        jsonObject.put(KEY_KODE_USER, getKd_user());
        jsonObject.put(KEY_KODE_OUTLET, getKd_outlet());
        jsonObject.put(KEY_KODE_KOMPETITOR, getKd_kompetitor());
        jsonObject.put(KEY_JENIS_PHOTO, getKd_tipe());
        jsonObject.put(KEY_NAMA_PHOTO, getNama());
        jsonObject.put(KEY_DATE_TAKE_PHOTO, getTgl_take());
        jsonObject.put(KEY_ALAMAT_PHOTO, getAlamat());
        jsonObject.put(KEY_KETERANGAN, getKeterangan());
        jsonObject.put(KEY_DATE_UPLOAD_PHOTO, getTgl_upload());
        jsonObject.put(KEY_FOTO, getFoto());
        jsonObject.put(KEY_NAMA_CHILD, getChild());
    }

    public PhotoActivity(JSONObject jsonObject) throws JSONException {
        setId(jsonObject.has(KEY_KODE_PHOTO) ? jsonObject.getInt(KEY_KODE_PHOTO) : 0);
        setKd_user(jsonObject.has(KEY_KODE_USER) ? jsonObject.getInt(KEY_KODE_USER) : 0);
        setKd_outlet(jsonObject.has(KEY_KODE_OUTLET) ? jsonObject.getInt(KEY_KODE_OUTLET) : 0);
        setKd_kompetitor(jsonObject.has(KEY_KODE_KOMPETITOR) ? jsonObject.getInt(KEY_KODE_KOMPETITOR) : 0);
        setKd_tipe(jsonObject.has(KEY_JENIS_PHOTO) ? jsonObject.getInt(KEY_JENIS_PHOTO) : 0);
        setNama(jsonObject.has(KEY_NAMA_PHOTO) ? jsonObject.getString(KEY_NAMA_PHOTO) : "");
        setTgl_take(jsonObject.has(KEY_DATE_TAKE_PHOTO) ? jsonObject.getString(KEY_DATE_TAKE_PHOTO) : "");
        setAlamat(jsonObject.has(KEY_ALAMAT_PHOTO) ? jsonObject.getString(KEY_ALAMAT_PHOTO) : "");
        setKeterangan(jsonObject.has(KEY_KETERANGAN) ? jsonObject.getString(KEY_KETERANGAN) : "");
        setTgl_upload(jsonObject.has(KEY_DATE_UPLOAD_PHOTO) ? jsonObject.getString(KEY_DATE_UPLOAD_PHOTO) : "");
        setFoto(jsonObject.has(KEY_FOTO) ? jsonObject.getString(KEY_FOTO) : "");
        setChild(jsonObject.has(KEY_NAMA_CHILD) ? jsonObject.getInt(KEY_NAMA_CHILD) : 0);
    }

    public int getKd_tipe() {
        return kd_tipe;
    }

    public void setKd_tipe(int kd_tipe) {
        this.kd_tipe = kd_tipe;
    }

    public void setTgl_upload(String tgl_upload) {
        this.tgl_upload = tgl_upload;
    }

    public String getTgl_upload() {
        return tgl_upload;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getFoto() {
        return foto;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public void setKd_kompetitor(int kd_kompetitor) {
        this.kd_kompetitor = kd_kompetitor;
    }

    public void setKd_outlet(int kd_outlet) {
        this.kd_outlet = kd_outlet;
    }

    public void setKd_user(int kd_user) {
        this.kd_user = kd_user;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setTgl_take(String tgl_take) {
        this.tgl_take = tgl_take;
    }

    public int getId() {
        return id;
    }

    public int getKd_kompetitor() {
        return kd_kompetitor;
    }

    public int getKd_outlet() {
        return kd_outlet;
    }

    public int getKd_user() {
        return kd_user;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public String getNama() {
        return nama;
    }

    public String getTgl_take() {
        return tgl_take;
    }

    public int getChild() {
        return child;
    }

    public void setChild(int child) {
        this.child = child;
    }
}
