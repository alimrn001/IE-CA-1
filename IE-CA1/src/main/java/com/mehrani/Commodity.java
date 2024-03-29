package com.mehrani;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class Commodity {
    private int id;
    private String name;
    private int providerId;
    private int price;
    private ArrayList<String> categories;
    private double rating;
    private int inStock;
    private int numOfRatings;

    Commodity(int id, String name, int providerId, int price, ArrayList<String> categories, double rating, int inStock) {
        this.id = id;
        this.name = name;
        this.providerId = providerId;
        this.price = price;
        this.categories = categories;
        this.rating = rating;
        this.inStock = inStock;
        this.numOfRatings = 1;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }
    public void addCategory(String category) {
        this.categories.add(category);
    }
    public void setRating(double rating) {
        this.rating = rating;
    }
    public void setInStock(int inStock) {
        this.inStock = inStock;
    }
    public void reduceInStock(int amount) {
        this.inStock -= amount;
    }
    public void addNewRating(double newRating) {
        this.rating = (((this.rating*this.numOfRatings) + newRating)/(this.numOfRatings+1));
        this.numOfRatings ++;
    }
    public void updateRating(double previousRating, double newRating) {
        this.rating += ((newRating - previousRating)/numOfRatings);
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getProviderId() {
        return providerId;
    }
    public int getPrice() {
        return price;
    }
    public ArrayList<String> getCategories() {
        return categories;
    }
    public double getRating() {
        return rating;
    }
    public int getInStock() {
        return inStock;
    }
    public boolean hasCategory(String category) {
        for (String category_ : categories) {
            if(category.equals(category_)) {
                return true;
            }
        }
        return false;
    }

}
