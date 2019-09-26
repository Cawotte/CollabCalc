package com.ensuque.collab;

import java.io.Serializable;
import java.lang.reflect.Method;

public abstract class CollabRequest<T> implements Serializable {


    //A Method Object is not serializable, so we only save its name and arguments
    // so the method can be fetched later through Reflection.
    private String methodName;
    private Object[] args;

    protected CollabRequest(Class<?> clazz, String methodName, Object[] args)
            throws NoSuchMethodException, InvalidReturnType {
        this.args = args;
        this.methodName = methodName;

        try {
            //Verify if a method with those name and arguments exists
            Method method = getMethod(clazz);

            //and if its return type is serializable
            if ( !isSerializable(method.getReturnType()) )
            {
                throw new InvalidReturnType("The return type " + method.getReturnType().toString() + " of the method " + methodName + " is not serializable.");
            }
        }
        catch (NoSuchMethodException err) {
            throw err;
        }
    }

    //region Abstract Methods

    protected abstract T getClassObject();
    //endregion

    /**
     * Run the CollabRequestObject's method and returns the result within a CollabResponse object.
     * @return
     */
    public CollabResponse run() {

        Method method;
        CollabResponse cr;
        T instance = getClassObject();

        try {
            method = getMethod(instance.getClass());

            cr = new CollabResponse(
                    method.invoke(instance, args),
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
        String str = getClassObject().getClass().getSimpleName() + " : " + methodName + "(";
        for (int i = 0; i < args.length; i++) {
            str += args[i].toString();
            if (i != args.length - 1)
                str += ", ";
        }
        str += ")";
        return str;
    }


    /**
     * Get the method to compute using the known serializable information about it
     * @return
     * @throws NoSuchMethodException
     */
    private Method getMethod(Class<?> clazz) throws NoSuchMethodException {

        //A method is not serializable, so we fetch the method through the known infos about it.

        Class<?>[] params = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            params[i] = args[i].getClass();
        }

        return clazz.getDeclaredMethod(methodName, params);
    }

    /**
     * Return true if the class is serializable (implements Serializable or is a primitive type)
     * @param type
     * @return
     */
    private boolean isSerializable(Class<?> type) {
        return Serializable.class.isAssignableFrom(type) //If it's a serializable class
                || type.isPrimitive(); //or a primitive
    }

}
