package com.ensuque.collab;

import java.io.Serializable;

/**
 * A CollabRequest that encapsulate an instance of the object.
 * The object MUST be Serializable.
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
    protected T getObjectInstance() {
        return obj;
    }

}
