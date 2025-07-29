package com.mycompany.demo.corejava;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HashMapDemo {

    // Shared HashMap (not thread-safe by default)
    static final HashMap<Integer, RetailCustomer> hmap = new HashMap<>();
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
        Thread1 thread1 = new Thread1();
        Thread2 thread2 = new Thread2();
        thread1.start();
        thread2.start();
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
        for (int index = 0; index < 100; index++) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
            Thread.sleep(1000); // Delay to allow Thread1 to add entries
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        synchronized (HashMapDemo.hmap) {
            Iterator<Map.Entry<Integer, RetailCustomer>> iter = HashMapDemo.hmap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<Integer, RetailCustomer> entry = iter.next();
                RetailCustomer rc = entry.getValue();
                System.out.println("ID: " + entry.getKey() + ", Name: " + rc.getName());
            }
        }
    }
}
