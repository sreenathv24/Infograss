package com.basicJava.stack;

import java.util.Stack;

public class StackExample {
    public static void main(String[] args) {
        Stack<String> stack = new Stack<>();

        stack.push("Apple");
        stack.push("Banana");
        stack.push("Cherry");

        System.out.println("Top item: " + stack.peek()); 
        System.out.println("Pop item: " + stack.pop()); 
        
}

}
