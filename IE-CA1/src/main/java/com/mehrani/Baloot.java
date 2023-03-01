package com.mehrani;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    public boolean categoryExists(String category) {
        return balootCategorySections.containsKey(category);
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
    public String addUser(User user) {
        Response response = new Response();
        Gson gsonProvider = new GsonBuilder().create();
        if(balootUsers.containsKey(user.getUsername())) {
            user.setBuyList(balootUsers.get(user.getUsername()).getBuyList());
            balootUsers.put(user.getUsername(), user);
            response.setSuccess(true);
            response.setData("");
        }
        else {
            if((user.getUsername().contains("!")) || (user.getUsername().contains("#")) || (user.getUsername().contains("@"))) {
                response.setSuccess(false);
                response.setData(error.getUsernameWrongChar());
                //throw new Exception(error.getUsernameWrongChar());
            }
            else {
                balootUsers.put(user.getUsername(), user);
                response.setSuccess(true);
                response.setData("");
            }
        }
        return gsonProvider.toJson(response);
    }
    public String addCommodity(Commodity commodity) {
        Gson gsonCommodity = new GsonBuilder().create();
        Response response = new Response();
        if(!balootProviders.containsKey(commodity.getProviderId())) {
            response.setSuccess(false);
            response.setData(error.getProviderNotExists());
            //return gsonCommodity.toJson(response);
        }
        else {
            if(!balootCommodities.containsKey(commodity.getId())) {
                balootProviders.get(commodity.getProviderId()).addProvidedCommodity(commodity.getId());
                for (String ctgr : commodity.getCategories()) {
                    updateCategorySection(ctgr, commodity.getId());
                }
                balootCommodities.put(commodity.getId(), commodity);
                response.setSuccess(true);
                response.setData("");
                balootProviders.get(commodity.getProviderId()).updateCommoditiesData(commodity.getRating());
                //return gsonCommodity.toJson(response);
            }
            else {
                response.setSuccess(false);
                response.setData(error.getCommodityIdExists());
                //return gsonCommodity.toJson(response);
            }
        }
        return gsonCommodity.toJson(response);
    }
    public Commodity getCommodityById(int id) throws Exception{
        if(balootCommodities.containsKey(id))
            return balootCommodities.get(id);
        else
            throw new Exception(error.getCommodityNotExists());
    }

    public String addProvider(Provider provider) { // exception is necessary ???
        Response response = new Response();
        if(providerExists(provider.getId())) {
            balootProviders.get(provider.getId()).setName(provider.getName());
            balootProviders.get(provider.getId()).setRegistryDate(provider.getRegistryDate().toString());
        }
        else
            balootProviders.put(provider.getId(), provider);
        response.setSuccess(true);
        response.setData("");
        Gson gsonProvider = new GsonBuilder().create();
        return gsonProvider.toJson(response);
    }
    public String addRemoveBuyList(String username, int commodityId, boolean isAdding) {
        Response response = new Response();
        Gson gsonaddRemove = new GsonBuilder().create();
        if(!userExists(username)) {
            response.setSuccess(false);
            response.setData(error.getUserNotExists());
            return gsonaddRemove.toJson(response);
            //throw new Exception(error.getUserNotExists());
        }
        if(!commodityExists(commodityId)) {
            response.setSuccess(false);
            response.setData(error.getCommodityNotExists());
            return gsonaddRemove.toJson(response);
            //throw new Exception(error.getCommodityNotExists());
        }
        else if(balootCommodities.get(commodityId).getInStock()==0 && isAdding) {
            response.setSuccess(false);
            response.setData(error.getProductNotInStorage());
            return gsonaddRemove.toJson(response);
            //throw new Exception(error.getProductNotInStorage());
        }
        if(balootUsers.get(username).itemExistsInBuyList(commodityId)) {
            if(isAdding) {
                response.setSuccess(false);
                response.setData(error.getProductAlreadyExistsInBuyList());
                return gsonaddRemove.toJson(response);
                //throw new Exception(error.getProductAlreadyExistsInBuyList());
            }
            else {
                balootUsers.get(username).removeFromBuyList(commodityId);
                balootCommodities.get(commodityId).reduceInStock(-1);
                response.setSuccess(true);
                response.setData("");
                return gsonaddRemove.toJson(response);
            }
        }
        else {
            if(!isAdding) {
                response.setSuccess(false);
                response.setData(error.getProductNotInBuyList());
                return gsonaddRemove.toJson(response);
                //throw new Exception(error.getProductNotInBuyList());
            }
        }
        response.setSuccess(true);
        response.setData("");
        balootUsers.get(username).addToBuyList(commodityId);
        balootCommodities.get(commodityId).reduceInStock(1); //assuming adding in buyLIst reduced product number in storage, if not set to 0
        return gsonaddRemove.toJson(response);
    }
    public String getCommoditiesByCategory(String category) {
        if(!categoryExists(category))
            return "[]";
        ArrayList<Integer> products = balootCategorySections.get(category).getCommodities();
        List<Commodity> objList = new ArrayList<Commodity>();
        int cnt=0;
        for (int i: products) {
            balootCommodities.get(i);
            objList.add(balootCommodities.get(i));
            cnt++;
        }
        String result = new Gson().toJson(objList);
//        for(int i = 0; i < cnt; i++)
//            JsonParser.parseString(result).getAsJsonArray().get(i).

        JsonObject jsonObject = JsonParser.parseString(result).getAsJsonObject();

        jsonObject.remove("inStock");
        jsonObject.remove("numOfRatings");
        return jsonObject.toString();
    }
    public String addRating(Rating rating) {
        Response response = new Response();
        Gson gsonRating = new GsonBuilder().create();
        if(rating.getScore() > 10 || rating.getScore() < 1) {
            response.setSuccess(false);
            response.setData(error.getRatingOutOfRange(rating.getScore()));
            //throw new Exception(error.getRatingOutOfRange(rating.getScore()));
        }
        else if(!userExists(rating.getUsername())) {
            response.setSuccess(false);
            response.setData(error.getUserNotExists());
            //throw new Exception(error.getUserNotExists());
        }
        else if(!commodityExists(rating.getCommodityId())) {
            response.setSuccess(false);
            response.setData(error.getCommodityNotExists());
            //throw new Exception(error.getCommodityNotExists());
        }
        else {
            String ratingKey = rating.getUsername() + "_" + rating.getCommodityId();
            balootRatings.put(ratingKey, rating);
            balootCommodities.get(rating.getCommodityId()).addNewRating(rating.getScore());
            response.setSuccess(true);
            response.setData("");
        }
        return gsonRating.toJson(response);
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
            return addUser(user);
        }
        else if(userCmd.equals("rateCommodity")) {
            Gson gsonCmdt = new GsonBuilder().create();
            Rating rating = gsonCmdt.fromJson(userData, Rating.class);
            return addRating(rating);
        }
        else if(userCmd.equals("addProvider")) {
            Provider provider = gson.fromJson(userData, Provider.class);
            return addProvider(provider);
        }
        else if(userCmd.equals("addToBuyList")) {
            JsonObject jsonObject = new Gson().fromJson(userData, JsonObject.class);
            return addRemoveBuyList(jsonObject.get("username").getAsString(), jsonObject.get("commodityId").getAsInt(), true);
        }
        else if(userCmd.equals("removeFromBuyList")) {
            JsonObject jsonObject = new Gson().fromJson(userData, JsonObject.class);
            return addRemoveBuyList(jsonObject.get("username").getAsString(), jsonObject.get("commodityId").getAsInt(), false);
        }
        else if(userCmd.equals("addCommodity")) {
            Gson gson2 = new GsonBuilder().create();
            Commodity commodity = gson2.fromJson(userData, Commodity.class);
            return addCommodity(commodity);
            //balootProviders.get(commodity.getProviderId()).updateCommoditiesData(commodity.getRating());
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
        else if(userCmd.equals("getCommoditiesByCategory")) {
            JsonObject jsonObject = new Gson().fromJson(userData, JsonObject.class);
            return getCommoditiesByCategory(jsonObject.get("category").getAsString());
        }
        return " ";
    }

}
