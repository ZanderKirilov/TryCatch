package com.example.sethcohen.muchotrabajo.Model;

import java.io.Serializable;
import java.util.ArrayList;


public class Item implements Serializable {
    private String title;
    private String description;
    private String companyName;
    private double salary;
    private String location;
    private int id;
    private int categoryId;

    public Item(String title, String description, String companyName, double salary, String location, int categoryId) {
        this.title = title;
        this.description = description;
        this.companyName = companyName;
        this.salary = salary;
        this.location = location;
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCompanyName() {
        return companyName;
    }

    public double getSalary() {
        return salary;
    }

    public String getLocation() {
        return location;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
