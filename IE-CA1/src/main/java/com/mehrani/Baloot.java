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
    private Error error = new Error();

//    private static final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
//        @Override
//        public LocalDate deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
//            return LocalDate.parse(json.getAsJsonPrimitive().getAsString());
//        }
//    }).create();


    public boolean commodityExists(int commodityId) {
        return balootCommodities.containsKey(commodityId);
    }
    public boolean userExists(String username) {
        return balootUsers.containsKey(username);
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
            balootCommodities.put(commodity.getId(), commodity);
        }
    }
    public void addProvider(Provider provider) throws Exception {
        if(!balootProviders.containsKey(provider.getId())) {
            balootProviders.put(provider.getId(), provider);
        }
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
    public void checkUserCmd(String userCmd, String userData) throws Exception{

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();

        if(userCmd.equals("addUser")) {
            User user = gson.fromJson("{\"username\": \"user1\", \"password\": \"1234\", \"email\": \"user@gmail.com\", \"birthDate\": \"1977-09-15\", \"address\": \"address1\", \"credit\": 1500}",
                    User.class);
            addUser(user);
        }
        else if(userCmd.equals("rateCommodity")) {
            Gson gson2 = new GsonBuilder().create();
            Rating rating = gson2.fromJson(userData, Rating.class);
            addRating(rating);
        }
    }

}
