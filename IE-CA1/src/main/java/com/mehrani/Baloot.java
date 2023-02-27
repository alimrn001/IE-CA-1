package com.mehrani;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Baloot {
    private Map<String, User> BalootUsers = new HashMap<>();
    private Map<Integer, Commodity> BalootCommodities = new HashMap<>();
    private Map<Integer, Provider> BalootProviders = new HashMap<>();
    private Map<String, Rating> BalootRatings = new HashMap<>();
    private Error error = new Error();


    public void addUser(User user) throws Exception {
        if(BalootUsers.containsKey(user.getUsername())) {
            user.setBuyList(BalootUsers.get(user.getUsername()).getBuyList());
            BalootUsers.put(user.getUsername(), user);
        }
        else {
            if((user.getUsername().indexOf("!") != -1) || (user.getUsername().indexOf("#")!=-1) || (user.getUsername().indexOf("@")!=-1))
                throw new Exception(error.getUsernameWrongChar());
            else
                BalootUsers.put(user.getUsername(), user);
        }
    }
    public void addCommodity(Commodity commodity) throws Exception {
        if(!BalootProviders.containsKey(commodity.getProviderId()))
            throw new Exception(error.getProviderNotExists());
        else {
            BalootCommodities.put(commodity.getId(), commodity);
        }
    }
    public void getCommoditiesList() {

    }

    public Map<String, User> getBalootUsers() {
        return BalootUsers;
    }
    public Map<Integer, Commodity> getBalootCommodities() {
        return BalootCommodities;
    }
    public Map<Integer, Provider> getBalootProviders() {
        return BalootProviders;
    }
    public Map<String, Rating> getBalootRatings() {
        return BalootRatings;
    }


}
