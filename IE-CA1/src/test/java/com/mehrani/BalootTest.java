package com.mehrani;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BalootTest {
    private Baloot baloot = new Baloot();
    @Test
    public void addUserTest() {
        try {
            User user = new User();
            user.setUserData("ali", "123", "2001-05-06", "ali@b", "addr", 50);
            baloot.addUser(user);
            System.out.println(baloot.getBalootUsers().size());
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

    @Test(expected = Exception.class)
    public void wrongUsernameTest() throws Exception {
        System.out.println(baloot.getBalootUsers().size());
        User user = new User();
        user.setUserData("a#li", "123", "2001-05-06", "ali@b", "addr", 50);
        baloot.addUser(user);

    }

//    @Before
//    public void setUp() throws Exception {
//    }
//
//    @After
//    public void tearDown() throws Exception {
//    }
}