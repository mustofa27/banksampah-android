package com.mustofa27.banksampah.model.entity;

public class Withdraw {
    private int id;
    private int count;
    private int balance_used;
    private int status;
    private int withdraw_option_id;
    private String created_at;
    private String updated_at;
    private int user_id;
    private WithdrawOption selected_option;
    private User user;

    public Withdraw() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getBalance_used() {
        return balance_used;
    }

    public void setBalance_used(int balance_used) {
        this.balance_used = balance_used;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getWithdraw_option_id() {
        return withdraw_option_id;
    }

    public void setWithdraw_option_id(int withdraw_option_id) {
        this.withdraw_option_id = withdraw_option_id;
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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public WithdrawOption getSelected_option() {
        return selected_option;
    }

    public void setSelected_option(WithdrawOption selected_option) {
        this.selected_option = selected_option;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
