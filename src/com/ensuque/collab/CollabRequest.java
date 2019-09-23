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
public class CollabRequest<T extends Serializable> implements Serializable {

    //Object with the method to execute.
    private T obj;

    //A Method Object is not serializable, so we only save its name and arguments
    // so the method can be fetched later through Reflection.
    private String methodName;
    private Object[] args;

    public CollabRequest(T obj, String methodName, Object[] args)
            throws NoSuchMethodException, InvalidReturnType {
        this.obj = obj;
        this.args = args;
        this.methodName = methodName;

        try {
            //Verify if a method with those name and arguments exists and if it's serializable
            Method method = getMethod();

            if ( !isSerializable(method.getReturnType()) )
            {
                throw new InvalidReturnType("Le type de retour " + method.getReturnType().toString() + " de la méthode " + methodName + " n'est pas Serializable");
            }
        }
        catch (NoSuchMethodException err) {
            throw err;
        }
    }

    /**
     * Run the CollabRequest's method and returns the result within a CollabResponse object.
     * @return
     */
    public CollabResponse run() {

        CollabResponse cr;

        try {
            Method method = getMethod();

            cr = new CollabResponse(
                    method.invoke(obj, args),
                    true);
        } catch (Exception err) {
            cr = new CollabResponse(
                    null,
                    false,
                    err);
        }

        return cr;
    }

    @Override
    public String toString() {
        //Return  the class name, the name of the method, and the value of each arguments.
        //Exemple : " Calc : addition(4, 5) "
        String str = obj.getClass().getSimpleName() + " : " + methodName + "(";
        for (int i = 0; i < args.length; i++) {
            str += args[i].toString();
            if (i != args.length - 1)
                str += ", ";
        }
        str += ")";
        return str;
    }

    /**
     * Get the method to compute using the known informations about the methods
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

    /**
     * Return true if the class is serializable (implements Serializable or is a primitive type)
     * @param type
     * @return
     */
    private boolean isSerializable(Class<?> type) {
        return Serializable.class.isAssignableFrom(type) //If it's a serializable class
                || type.isPrimitive(); //n*or a primitive
    }

}
