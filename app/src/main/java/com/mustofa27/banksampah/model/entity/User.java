package com.mustofa27.banksampah.model.entity;

/**
 * Created by Ahmad Mustofa, PT Trikarya Teknologi on 3/19/2016.
 */
public class User{
    public static String table = "user";

    private int id;
    private int kd_role;
    private int status;
    private String name;
    private String phone;
    private String foto;
    private String username;
    private String email;
    private String fcm_id;
    private String imei;
    private Integer toleransi;
    private String api_token;
    private String uuid;
    private String nik;
    private String alamat;
    private int kota_id;
    private int office_id;
    private String join_date;

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getKd_role() {
        return kd_role;
    }

    public void setKd_role(int kd_role) {
        this.kd_role = kd_role;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFcm_id() {
        return fcm_id;
    }

    public void setFcm_id(String fcm_id) {
        this.fcm_id = fcm_id;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public Integer getToleransi() {
        return toleransi;
    }

    public void setToleransi(Integer toleransi) {
        this.toleransi = toleransi;
    }

    public String getApi_token() {
        return api_token;
    }

    public void setApi_token(String api_token) {
        this.api_token = api_token;
    }
}
