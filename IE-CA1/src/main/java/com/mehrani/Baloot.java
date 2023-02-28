package com.mehrani;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Baloot {
    private Map<String, User> balootUsers = new HashMap<>();
    private Map<Integer, Commodity> balootCommodities = new HashMap<>();
    private Map<Integer, Provider> balootProviders = new HashMap<>();
    private Map<String, Rating> balootRatings = new HashMap<>();
    private Map<String, Category> balootCategorySections = new HashMap<>();
    private Error error = new Error();

    public boolean commodityExists(int commodityId) {
        return balootCommodities.containsKey(commodityId);
    }
    public boolean userExists(String username) {
        return balootUsers.containsKey(username);
    }
    public boolean providerExists(int providerId) {
        return balootProviders.containsKey(providerId);
    }
    public void updateCategorySection(String categoryName, int commodityId) {
        if(balootCategorySections.containsKey(categoryName)) {
            balootCategorySections.get(categoryName).addCommodityToCategory(commodityId);
        }
        else {
            Category category = new Category();
            category.setCategoryName(categoryName);
            category.addCommodityToCategory(commodityId);
            balootCategorySections.put(categoryName, category);
        }
    }
    public void addUser(User user) throws Exception {
        if(balootUsers.containsKey(user.getUsername())) {
            user.setBuyList(balootUsers.get(user.getUsername()).getBuyList());
            balootUsers.put(user.getUsername(), user);
        }
        else {
            if((user.getUsername().contains("!")) || (user.getUsername().contains("#")) || (user.getUsername().contains("@")))
                throw new Exception(error.getUsernameWrongChar());
            else
                balootUsers.put(user.getUsername(), user);
        }
    }
    public void addCommodity(Commodity commodity) throws Exception {
        if(!balootProviders.containsKey(commodity.getProviderId()))
            throw new Exception(error.getProviderNotExists());
        else {
            //balootCommodities.put(commodity.getId(), commodity);
            if(!balootCommodities.containsKey(commodity.getId())) {
                balootProviders.get(commodity.getProviderId()).addProvidedCommodity(commodity.getId());
                for (String ctgr : commodity.getCategories()) {
                    updateCategorySection(ctgr, commodity.getId());
                }
                balootCommodities.put(commodity.getId(), commodity);
            }
//            else {
//
//            }
        }
    }
    public void addProvider(Provider provider) throws Exception {
        balootProviders.put(provider.getId(), provider);
    }
    public void addRating(Rating rating) throws Exception {
        if(rating.getScore() > 10 || rating.getScore() < 1)
            throw new Exception(error.getRatingOutOfRange(rating.getScore()));
        else if(!userExists(rating.getUsername()))
            throw new Exception(error.getUserNotExists());
        else if(!commodityExists(rating.getCommodityId()))
            throw new Exception(error.getCommodityNotExists());
        else {
            String ratingKey = rating.getUsername() + "_" + rating.getCommodityId();
            balootRatings.put(ratingKey, rating);
        }
    }
    public Map<String, User> getBalootUsers() {
        return balootUsers;
    }
    public Map<Integer, Commodity> getBalootCommodities() {
        return balootCommodities;
    }
    public Map<Integer, Provider> getBalootProviders() {
        return balootProviders;
    }
    public Map<String, Rating> getBalootRatings() {
        return balootRatings;
    }
    public Map<String, Category> getBalootCategorySections() {
        return balootCategorySections;
    }

    public void checkUserCmd(String userInput) throws Exception {
        String userCmd, userData;
        userCmd = userInput.substring(0, userInput.indexOf(" "));
        userData = userInput.substring(userInput.indexOf(" ")+1);
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
        if(userCmd.equals("addUser")) {
            User user = gson.fromJson(userData, User.class);
            addUser(user);
        }
        else if(userCmd.equals("rateCommodity")) {
            Gson gson2 = new GsonBuilder().create();
            Rating rating = gson2.fromJson(userData, Rating.class);
            addRating(rating);
        }
        else if(userCmd.equals("addProvider")) {
            Provider provider = gson.fromJson(userData, Provider.class);
            if(providerExists(provider.getId())) {
                balootProviders.get(provider.getId()).setName(provider.getName());
                balootProviders.get(provider.getId()).setRegistryDate(provider.getRegistryDate().toString());
            }
            else
                addProvider(provider);
        }
        else if(userCmd.equals("addCommodity")) {
            Gson gson2 = new GsonBuilder().create();
            Commodity commodity = gson2.fromJson(userData, Commodity.class);
            balootProviders.get(commodity.getProviderId()).updateCommoditiesData(commodity.getRating());
            addCommodity(commodity);
        }
    }

}
