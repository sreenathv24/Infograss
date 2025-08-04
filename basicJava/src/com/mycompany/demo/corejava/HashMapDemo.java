package com.mycompany.demo.corejava;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HashMapDemo {

    // Shared HashMap (Not thread-safe, hence synchronized externally)
    static final Map<Integer, RetailCustomer> hmap = new HashMap<>();
    static int idCounter = 0;

    static {
        RetailCustomer rc1 = new RetailCustomer();
        rc1.setName("Vivek");

        RetailCustomer rc2 = new RetailCustomer();
        rc2.setName("Sara");

        synchronized (hmap) {
            hmap.put(++idCounter, rc1);
            hmap.put(++idCounter, rc2);
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread1();
        Thread t2 = new Thread2();
        t1.start();
        t2.start();
    }
}

class RetailCustomer {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class Thread1 extends Thread {
    public void run() {
        for (int i = 0; i < 100; i++) {
            try {
                Thread.sleep(10); // simulate some delay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            RetailCustomer rc = new RetailCustomer();
            rc.setName("John");

            synchronized (HashMapDemo.hmap) {
                HashMapDemo.hmap.put(++HashMapDemo.idCounter, rc);
            }
        }
    }
}

class Thread2 extends Thread {
    public void run() {
        System.out.println("Starting iteration...");

        try {
            Thread.sleep(1000); // Ensure Thread1 has added some entries
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        synchronized (HashMapDemo.hmap) {
            Iterator<Map.Entry<Integer, RetailCustomer>> iterator = HashMapDemo.hmap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer, RetailCustomer> entry = iterator.next();
                System.out.println("ID: " + entry.getKey() + ", Name: " + entry.getValue().getName());
            }
        }
    }
}
