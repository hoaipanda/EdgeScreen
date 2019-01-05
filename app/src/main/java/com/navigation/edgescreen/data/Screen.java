package com.navigation.edgescreen.data;

import java.util.ArrayList;

public class Screen {
    private String image;
    private String name;

    public Screen() {
    }

    public Screen(String name, String image) {
        this.image = image;
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
