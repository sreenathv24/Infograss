package com.basicJava.threads;

import java.util.*;

 public class car {

    public static void main(String[] args) {
        Vector<String> car = new Vector<>();
        car.add("BMW");
        car.add("VOLVO");
        car.add("FERRARI");

        System.out.println("Before threads: " + car);

        Thread t1 = new Thread(() -> {
            car.set(1, "Mercedes");
        });

        Thread t2 = new Thread(() -> {
            car.set(1, "AUDI");
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (Exception e) {
            System.out.println(e);
        }

        System.out.println("After threads: " + car);
    }
}
