package com.mycompany.demo.corejava;

import java.util.HashSet;
import java.util.Iterator;

public class HashSetDemo {

    static final HashSet<RetailCustomer1> hset = new HashSet<>();

    static {
        RetailCustomer1 rc1 = new RetailCustomer1();
        rc1.setName("Vivek");

        RetailCustomer1 rc2 = new RetailCustomer1();
        rc2.setName("Sara");

        synchronized (hset) {
            hset.add(rc1);
            hset.add(rc2);
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread1();
        Thread t2 = new Thread2();
        t1.start();
        t2.start();
    }
}

class RetailCustomer1 {
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
                Thread.currentThread().interrupt();
            }

            RetailCustomer1 rc = new RetailCustomer1();
            rc.setName("John");

            synchronized (HashSetDemo.hset) {
                HashSetDemo.hset.add(rc);
            }
        }
    }
}

class Thread2 extends Thread {
    public void run() {
        System.out.println("Starting iteration...");

        try {
            Thread.sleep(1000); // Let Thread1 add some elements
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        synchronized (HashSetDemo.hset) {
            Iterator<RetailCustomer1> iter = HashSetDemo.hset.iterator();
            while (iter.hasNext()) {
                RetailCustomer1 rc = iter.next();
                System.out.println("Name: " + rc.getName());
            }
        }
    }
}
