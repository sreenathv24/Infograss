package com.basicJava.queue;

import java.util.*;
public class ArrayDequeExample {

	public static void main(String[] args) {
		Deque<String> dq = new ArrayDeque<>(); 
		        dq.add("The");
		        dq.addFirst("lib");
		        dq.addLast("books");
                dq.addFirst("The");
                dq.addLast("car");
		        System.out.println(dq);
		        System.out.println(dq.pop());
		        System.out.println(dq.poll());
		        System.out.println(dq.pollFirst());
		        System.out.println(dq.pollLast());
		    }
	}

