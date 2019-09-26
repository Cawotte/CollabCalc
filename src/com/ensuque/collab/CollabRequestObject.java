package com.ensuque.collab;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * Encapsulate an object and one of its method so its execution can be performed by another device or thread.
 * <p/>
 * The method's arguments MUST ALL be Objects. It cannot be a primitive type (Must be Integer instead of int)
 * because of the limitations of Java's Reflection.
 * @param <T> The Object must implements Serializable, so it can be sent through the network.
 */
public class CollabRequestObject<T extends Serializable> extends CollabRequest implements Serializable {

    //Object with the method to execute.
    private T obj;


    public CollabRequestObject(T obj, String methodName, Object[] args)
            throws NoSuchMethodException, InvalidReturnType {
        super(obj.getClass(), methodName, args);
        this.obj = obj;
    }

    @Override
    protected T getClassObject() {
        return obj;
    }

}
