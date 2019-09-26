package com.ensuque.testComputing;


import java.io.Serializable;

public class CalcSerializable extends Calc implements Serializable {

    /**
     * Identical to the Calc class, except it's Serializable.
     * As such, it can be used with CollabRequestObject, that requires Serializable objects.
     */
}

