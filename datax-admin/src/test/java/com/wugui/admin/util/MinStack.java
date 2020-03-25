package com.wugui.admin.util;

import java.util.Stack;

public class MinStack {
    private Stack<Integer> stack;
    private Stack<Integer> minStack;

    public MinStack(){
        stack= new Stack<>();
        minStack= new Stack<>();
    }

    public void push(int v){
        stack.push(v);
        if(minStack.empty()|| minStack.peek()>=v){
            minStack.push(v);
        }
    }
    public void pop(){
        if(minStack.peek().equals(stack.peek())){ //因为是Integer类型，所以千万不能==判断，要用equals
            minStack.pop();
        }
        stack.pop();
    }
    public int top(){
        return stack.peek();
    }
    public int getMin(){
        return minStack.peek();
    }

    public static void main(String[] args) {
        MinStack m = new MinStack();
        m.push(512);
        m.push(-1024);
        m.push(-1024);
        m.push(512);
        m.pop();
        System.out.println(m.getMin());
        m.pop();
        System.out.println(m.getMin());
        m.pop();
        System.out.println(m.getMin());
    }
}
