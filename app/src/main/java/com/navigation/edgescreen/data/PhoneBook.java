package com.navigation.edgescreen.data;

public class PhoneBook {
    private int pos;
    private int color;

    public PhoneBook() {
    }

    public PhoneBook(int pos, int color) {
        this.pos = pos;
        this.color = color;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
