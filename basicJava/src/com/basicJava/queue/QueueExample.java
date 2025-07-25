package com.basicJava.queue;

import java.util.*;
public class QueueExample {

	public static void main(String[] args) {
		Queue<String> queue = new LinkedList<>();
		queue.add("BMW");
        queue.add("FERRARI");
        queue.add("AUDI");
        System.out.println("Removed: " + queue.poll()); 
        System.out.println("Removed: " + queue.remove());
        for (String car : queue) {
            System.out.println(car);
        }

	}

}
