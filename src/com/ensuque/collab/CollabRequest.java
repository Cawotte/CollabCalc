package com.ensuque.collab;

import java.io.Serializable;
import java.lang.reflect.Method;

public class CollabRequest<T extends Serializable> implements Serializable {

    private T obj;
    private Object[] args;
    private String methodName;


    public CollabRequest(T obj, String methodName, Object[] args)
            throws NoSuchMethodException, InvalidReturnType {
        this.obj = obj;
        this.args = args;
        this.methodName = methodName;

        try {
            //Verify if the method with those arguments exists and is serializable
            Method method = getMethod();

            if ( !isSerializable(method.getReturnType()) )
            {
                throw new InvalidReturnType("Le type de retour " + method.getReturnType().toString() + " de la méthode " + methodName + " n'est pas Serializable");
            }
        }
        catch (NoSuchMethodException err) {
            System.out.println("La classe " + obj.getClass().getName() + " ne possède pas de méthode " + methodName + " !");
            throw err;
        }
    }

    public CollabResult run() {

        CollabResult cr;

        try {
            Method method = getMethod();

            cr = new CollabResult(
                    method.invoke(obj, args),
                    true);
        } catch (Exception err) {
            cr = new CollabResult(
                    null,
                    false,
                    err);
        }

        return cr;
    }


    /**
     * Get the method to compute using the known information about the methods
     * @return
     * @throws NoSuchMethodException
     */
    private Method getMethod() throws NoSuchMethodException {

        //A method is not serializable, so we fetch the method through the known infos about it.

        Class<?>[] params = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            params[i] = args[i].getClass();
        }

        return obj.getClass().getDeclaredMethod(methodName, params);
    }

    private boolean isSerializable(Class<?> type) {
        return Serializable.class.isAssignableFrom(type) //If it's not a serializable class
                || type.isPrimitive(); //nor a primitive
    }

}
