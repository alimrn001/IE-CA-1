package com.mehrani;
import java.util.ArrayList;
public class Commodity {
    private int id;
    private String name;
    private int providerId;
    private int price;
    private ArrayList<String> categories; //creating a 'Category' might also be considered
    private double rating;
    private int inStock;

    Commodity(int id, String name, int providerId, int price, ArrayList<String> categories, double rating, int inStock) {
        this.id = id;
        this.name = name;
        this.providerId = providerId;
        this.price = price;
        this.categories = categories;
        this.rating = rating;
        this.inStock = inStock;
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
