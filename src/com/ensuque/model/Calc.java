package com.ensuque.model;


public class Calc {
    public int add(String a, String b){
        int x = Integer.parseInt(a);
        int y = Integer.parseInt(b);
        return x + y;
    }

    public float multiply(Float x, Float y) {
        return x * y;
    }
}

