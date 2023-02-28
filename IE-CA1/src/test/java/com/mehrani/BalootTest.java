package com.mehrani;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class BalootTest {
    private Baloot baloot = new Baloot();
    @Test
    public void addUserTest() {
        try {
            User user = new User();
            user.setUserData("ali", "123", "2001-05-06", "ali@b", "addr", 50);
            baloot.addUser(user);
            //System.out.println(baloot.getBalootUsers().size());
            assertEquals(1, baloot.getBalootUsers().size());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            Assert.fail();
        }
    }

    @Test
    public void addExistingUserTest() {
        try {
            User user = new User();
            User user2 = new User();
            User user3 = new User();
            User user4 = new User();

            user.setUserData("ali", "123", "2001-05-06", "ali@b", "addr", 50);
            user2.setUserData("amir", "1234", "2001-06-06", "alin@b", "addr", 50);
            user3.setUserData("ali", "1234", "2001-05-06", "ali@b", "addr", 50);
            user4.setUserData("ahmad", "1234", "2001-05-06", "ali@b", "addr", 50);

            baloot.addUser(user);
            baloot.addUser(user2);
            baloot.addUser(user3);
            baloot.addUser(user4);
            //System.out.println(baloot.getBalootUsers().size());
            assertEquals("1234", baloot.getBalootUsers().get(user.getUsername()).getPassword());
            assertEquals(3, baloot.getBalootUsers().size());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            Assert.fail();
        }
    }

    @Test
    public void ratingAddCmdTest() throws Exception {
        try {

            User user = new User();
            user.setUserData("user1", "123", "2001-05-06", "ali@b", "addr", 50);
            baloot.addUser(user);

            Provider provider = new Provider();
            provider.setData(1, "ocso", "2001-05-06", false);
            baloot.addProvider(provider);
            System.out.println(baloot.getBalootProviders().size());

            ArrayList<String> categories = new ArrayList<>();
            categories.add("tech");
            Commodity commodity = new Commodity(3, "caj", 1, 1, categories, 3.5, 10);
            baloot.addCommodity(commodity);

            baloot.checkUserCmd("rateCommodity", "{\"username\": \"user1\", \"commodityId\": 3, \"score\": 7}");
        } catch(Exception e) {
            System.out.println(e.getMessage());
            Assert.fail();
        }
    }

    @Test(expected = Exception.class)
    public void wrongUsernameTest() throws Exception {
        System.out.println(baloot.getBalootUsers().size());
        User user = new User();
        user.setUserData("a#li", "123", "2001-05-06", "ali@b", "addr", 50);
        baloot.addUser(user);

    }
    @Test
    public void addRatingTest() {
        try {
            User user = new User();
            user.setUserData("user1", "123", "2001-05-06", "ali@b", "addr", 50);
            baloot.addUser(user);

            Provider provider = new Provider();
            provider.setData(1, "ocso", "2001-05-06", false);
            baloot.addProvider(provider);
            System.out.println(baloot.getBalootProviders().size());

            ArrayList<String> categories = new ArrayList<>();
            categories.add("tech");
            Commodity commodity = new Commodity(3, "caj", 1, 1, categories, 3.5, 10);
            baloot.addCommodity(commodity);
            Rating rating = new Rating();
            rating.setData("user1", 3, 7);
            baloot.addRating(rating);
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            Assert.fail();
        }
    }
//    @Before
//    public void setUp() throws Exception {
//    }
//
//    @After
//    public void tearDown() throws Exception {
//    }
}