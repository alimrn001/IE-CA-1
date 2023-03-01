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

    public Commodity getCommodityById(int id) throws Exception{
        if(balootCommodities.containsKey(id))
            return balootCommodities.get(id);
        else
            throw new Exception(error.getCommodityNotExists());
    }

    public void addProvider(Provider provider) throws Exception { // exception is necessary ???
        balootProviders.put(provider.getId(), provider);
    }
    public void addToBuyList(String username, int commodityId) throws Exception{
        if(!userExists(username))
            throw new Exception(error.getUserNotExists());
        if(!commodityExists(commodityId))
            throw new Exception(error.getCommodityNotExists());
        else if(balootCommodities.get(commodityId).getInStock()==0)
                throw new Exception(error.getProductNotInStorage());
        if(balootUsers.get(username).itemExistsInBuyList(commodityId))
            throw new Exception(error.getProductAlreadyExistsInBuyList());
        balootUsers.get(username).addToBuyList(commodityId);
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

    public String checkUserCmd(String userInput) throws Exception {
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
        else if(userCmd.equals("addToBuyList")) {
            JsonObject jsonObject = new Gson().fromJson(userData, JsonObject.class);
            addToBuyList(jsonObject.get("username").getAsString(), jsonObject.get("commodityId").getAsInt());
            return " ";
        }
        else if(userCmd.equals("removeFromBuyList")) {

        }
        else if(userCmd.equals("addCommodity")) {
            Gson gson2 = new GsonBuilder().create();
            Commodity commodity = gson2.fromJson(userData, Commodity.class);
            addCommodity(commodity);
            balootProviders.get(commodity.getProviderId()).updateCommoditiesData(commodity.getRating());
        }
        else if(userCmd.equals("getCommodityById")) {
            Gson gson2 = new GsonBuilder().create();
            JsonObject jObj = new Gson().fromJson(userData, JsonObject.class);
            int commodityId = jObj.get("id").getAsInt();
            Commodity commodity = getCommodityById(commodityId);
            Commodity tmp = commodity;
            String jsonRes = gson2.toJson(commodity, Commodity.class);
            JsonObject jsonObject = JsonParser.parseString(jsonRes).getAsJsonObject();
            jsonObject.remove("providerId");
            jsonObject.remove("price");
            jsonObject.remove("categories");
            jsonObject.remove("rating");
            jsonObject.remove("inStock");
            jsonObject.remove("numOfRatings");
            jsonObject.addProperty("provider", balootProviders.get(tmp.getProviderId()).getName());
            jsonObject.addProperty("price", tmp.getPrice());
            JsonArray ctgrs = new JsonArray();
            for(String ctgr : tmp.getCategories()) {
                ctgrs.add(new JsonPrimitive(ctgr));
            }
            jsonObject.add("categories", ctgrs);
            jsonObject.addProperty("rating", tmp.getRating());
            String resultData = jsonObject.toString();
            return resultData;
        }
        return " ";
    }

}
