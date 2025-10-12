package com.mustofa27.banksampah.model.entity;

import java.io.Serializable;

public class Link implements Serializable {
    private String url;
    private String label;
    private boolean active;

    public Link() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
