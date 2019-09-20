package com.ensuque.model;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CollabRequest<T extends Serializable> implements Serializable {

    private T obj;
    private Object[] args;
    private String methodName;



    public CollabRequest(T obj, String methodName, Object[] args) throws NoSuchMethodException {
        this.obj = obj;
        this.args = args;
        this.methodName = methodName;

        Class<?>[] params = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            params[i] = args[i].getClass();
            //System.out.println(args[i].getClass().getName());
        }

        try {
            obj.getClass().getDeclaredMethod(methodName, params);
        }
        catch (NoSuchMethodException err) {
            System.out.println("La classe " + obj.getClass().getName() + " ne possède pas de méthode " + methodName + " !");
            throw err;
        }
    }

    public Object run() {

        Method method = getMethod();
        try {
            return method.invoke(obj, args);
        } catch (IllegalAccessException ill) {
            System.out.println("Ce thread n'as pas le droit d'accéder à cette méthode!");
            return null;
        } catch (InvocationTargetException ite) {
            System.out.println(ite.getStackTrace());
            return null;
        }
    }


    private Method getMethod() {

        Class<?>[] params = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            params[i] = args[i].getClass();
        }

        try {
            return obj.getClass().getDeclaredMethod(methodName, params);

        }
        catch (NoSuchMethodException err) {
            System.out.println("La classe " + obj.getClass().getName() + " ne possède pas de méthode " + methodName + " !");
            return null;
        }
    }

}
