package com.basicJava.stack;

import java.util.*;
public class DequeExample {

	public static void main(String[] args) {
		Deque<String> dq = new ArrayDeque<>(); 
		        
		        dq.push("The");
		        dq.push("lib");
		        dq.push("books");
		        dq.push("The");
		        dq.push("car");
		        System.out.println(dq);
		        System.out.println(dq.pop()); 
		        System.out.println(dq.pop()); 
		        System.out.println(dq.pop()); 
		        System.out.println(dq.pop()); 
		        System.out.println(dq.pop()); 
		    }

	}

