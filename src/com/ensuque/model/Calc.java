package com.ensuque.model;


import java.io.Serializable;

public class Calc implements Serializable {
    public int add(String a, String b){
        int x = Integer.parseInt(a);
        int y = Integer.parseInt(b);
        return x + y;
    }

    public float multiply(Float x, Float y) {
        return x * y;
    }
}

