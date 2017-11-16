package com.example.sethcohen.muchotrabajo.Model;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;


public class User implements Serializable{

    private String name;
    private String email;
    private String password;
    private byte[] userImageBytes = null;
    private ArrayList<Item> userItems;

    private int id;


    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String name, String email, String password) {
        this(email, password);
        this.name = name;

        this.userItems = new ArrayList<>();
    }


    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getUserImageBytes() {

        return this.userImageBytes;
    }

    public void setUserImageBytes(byte[] userImageBytes) {
        this.userImageBytes = userImageBytes;
    }

    public void addItem(Item item) {

        userItems.add(item);
    }

}
