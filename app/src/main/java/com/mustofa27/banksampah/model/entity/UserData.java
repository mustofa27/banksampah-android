package com.mustofa27.banksampah.model.entity;

public class UserData {
    private String uuid;
    private String nik;
    private String alamat;
    private int kota_id;
    private int provinsi_id;
    private int office_id;
    private String join_date;

    public UserData() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public int getKota_id() {
        return kota_id;
    }

    public void setKota_id(int kota_id) {
        this.kota_id = kota_id;
    }

    public int getProvinsi_id() {
        return provinsi_id;
    }

    public void setProvinsi_id(int provinsi_id) {
        this.provinsi_id = provinsi_id;
    }

    public int getOffice_id() {
        return office_id;
    }

    public void setOffice_id(int office_id) {
        this.office_id = office_id;
    }

    public String getJoin_date() {
        return join_date;
    }

    public void setJoin_date(String join_date) {
        this.join_date = join_date;
    }
}
