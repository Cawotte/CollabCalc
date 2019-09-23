package com.ensuque;


import java.io.Serializable;

public class Calc implements Serializable {

    //A simple Calc class to test different use cases of the CollabRequests

    public Integer add(String a, String b){
        int x = Integer.parseInt(a);
        int y = Integer.parseInt(b);
        return x + y;
    }


    public float multiply(Float x, Float y) {
        return x * y;
    }

    /**
     * A simple division that raise an exception if y = 0.
     * @param x
     * @param y
     * @return
     */
    public float divide(Float x, Float y) {
        if (y == 0f) {
            throw new IllegalArgumentException("Divide by zero!");
        }
        return x / y;
    }

    /**
     * A simple addition. Doesn't work in a CollabRequest because it uses primitives types as parameters.
     * @param a
     * @param b
     * @return
     */
    public int add(int a, int b) {
        return a + b;
    }

}

