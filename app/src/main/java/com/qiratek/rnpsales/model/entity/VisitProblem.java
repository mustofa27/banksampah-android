package com.qiratek.rnpsales.model.entity;

/**
 * Created by LENOVO on 3/31/2017.
 */

public class VisitProblem {
    int id,kd_visit;
    String detail_problem,date_upload,date_take, foto;
    public static String table_name = "visit_problem",column_id = "id",culumn_kd_visit = "kd_visit",
            column_detail_problem = "detail_problem",column_date_up = "date_upload",column_date_take = "date_take",column_foto = "foto";

    public VisitProblem(int id, int kd_visit, String detail_problem, String date_upload, String date_take, String foto) {
        this.id = id;
        this.kd_visit = kd_visit;
        this.detail_problem = detail_problem;
        this.date_upload = date_upload;
        this.date_take = date_take;
        this.foto = foto;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getKd_visit() {
        return kd_visit;
    }

    public void setKd_visit(int kd_visit) {
        this.kd_visit = kd_visit;
    }

    public String getDetail_problem() {
        return detail_problem;
    }

    public void setDetail_problem(String detail_problem) {
        this.detail_problem = detail_problem;
    }

    public String getDate_upload() {
        return date_upload;
    }

    public void setDate_upload(String date_upload) {
        this.date_upload = date_upload;
    }

    public String getDate_take() {
        return date_take;
    }

    public void setDate_take(String date_take) {
        this.date_take = date_take;
    }
}
