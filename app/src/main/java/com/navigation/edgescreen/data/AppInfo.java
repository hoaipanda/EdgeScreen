package com.navigation.edgescreen.data;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class AppInfo implements Serializable {
    private String pack;
    private String name;
    private Drawable icon;

    public AppInfo() {
    }

    public AppInfo(String pack, String name, Drawable icon) {
        this.pack = pack;
        this.name = name;
        this.icon = icon;
    }

    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
}
