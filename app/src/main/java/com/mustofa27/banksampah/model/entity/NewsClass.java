package com.mustofa27.banksampah.model.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by LENOVO on 3/31/2017.
 */

public class NewsClass implements Serializable, Comparable<NewsClass> {
    public static String table_name = "news";
    int id;
    @SerializedName("title")
    String judul;
    String content;
    @SerializedName("image_path")
    String path_image;
    String created_at;
    User author;
    ArrayList<NewsComment> comments;
    ArrayList<NewsLike> likes;

    public NewsClass() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPath_image() {
        return path_image;
    }

    public void setPath_image(String path_image) {
        this.path_image = path_image;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public ArrayList<NewsComment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<NewsComment> comments) {
        this.comments = comments;
    }

    public ArrayList<NewsLike> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<NewsLike> likes) {
        this.likes = likes;
    }

    @Override
    public int compareTo(NewsClass another) {
        //yyyy-mm-dd hh:ii:ss
        Calendar current = Calendar.getInstance();
        String[] date = getCreated_at().split("T");
        String[] tanggal = date[0].split("-");
        String[] jam = date[1].split(":");
        current.set(Integer.valueOf(tanggal[0]), Integer.valueOf(tanggal[1])-1, Integer.valueOf(tanggal[2])
                , Integer.valueOf(jam[0]), Integer.valueOf(jam[1]), Integer.valueOf(jam[2]));
        Calendar other = Calendar.getInstance();
        String[] date1 = another.getCreated_at().split(" ");
        String[] tanggal1 = date1[0].split("-");
        String[] jam1 = date1[1].split(":");
        other.set(Integer.valueOf(tanggal1[0]), Integer.valueOf(tanggal1[1])-1, Integer.valueOf(tanggal1[2])
                , Integer.valueOf(jam1[0]), Integer.valueOf(jam1[1]), Integer.valueOf(jam1[2]));
        return current.getTimeInMillis() > other.getTimeInMillis() ? -1
                : current.getTimeInMillis() < other.getTimeInMillis() ? 1
                : 0;
    }
}
