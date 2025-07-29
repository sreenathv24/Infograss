

import java.util.HashSet;
import java.util.Iterator;

public class HashSetDemo {

    static HashSet<RetailCustomer> hset = new HashSet<>();

    static {

        RetailCustomer rc1 = new RetailCustomer();
        rc1.setName("Vivek");

        RetailCustomer rc2 = new RetailCustomer();
        rc2.setName("Sara");

        hset.add(rc1);
        hset.add(rc2);
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
                this.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            RetailCustomer rc = new RetailCustomer();
            rc.setName("John");
            HashSetDemo.hset.add(rc);
        }
    }
}

class Thread2 extends Thread {
    public void run() {

        System.out.println("dd");

        try {
            this.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Iterator<RetailCustomer> iter = HashSetDemo.hset.iterator();
        while (iter.hasNext()) {
            RetailCustomer rc = iter.next();
            String name = rc.getName();
            System.out.println(name);
        }
    }
}