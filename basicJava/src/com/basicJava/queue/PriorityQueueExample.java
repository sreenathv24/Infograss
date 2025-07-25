package com.basicJava.queue;

import java.util.*;
public class PriorityQueueExample {

	public static void main(String[] args) {
		Queue<Integer> p = new PriorityQueue<>(Collections.reverseOrder());
        System.out.println(p);

        p.add(3);
        p.add(10);
        p.add(7);
        p.add(2);
        
        System.out.println("peek vakue is" + p.peek());
        System.out.println("the priority queue is"+ p);
        System.out.println("p" + p.poll());


	}

}
