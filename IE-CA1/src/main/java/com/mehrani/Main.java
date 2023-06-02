package com.mehrani;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        Baloot baloot = new Baloot();
        System.out.println("Enter command");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while(true) {
            String command = reader.readLine();
            String response = baloot.checkUserCmd(command);
            System.out.println("Command : \n" + command);
            System.out.println("Response : \n" + response);
        }

    }
}