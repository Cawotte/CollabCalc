package com.ensuque.collab;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

/**
 * Encapsulate the result of a CollabRequest : The method's result,  if it's has been successfully executed,
 * and the raised exception if it wasn't the case.
 */
public class CollabResult implements Serializable {

    //Result of the executed method
    //Note : A null object DOESN'T mean the execution failed.
    private Object result;

    //If the method successfully executed or not
    private boolean isSuccess;

    //The raised exception if the method did not execute properly.
    private Exception error;

    CollabResult(Object result, boolean isSuccess) {
        this(result, isSuccess, null);
    }

    CollabResult(Object result, boolean isSuccess, Exception err) {
        this.result = result;
        this.isSuccess = isSuccess;
        this.error = err;
    }

    public boolean isSuccessful() {
        return isSuccess;
    }

    public Object getResult() {
        return result;
    }

    public Exception getError() {
        return error;
    }

    /**
     * Print the exception's name, and if it's an InvocationTargetException, also prints the exception that caused it.
     */
    public void printError() {
        if (error == null)
            System.out.println("There's no error.");
        else {
            System.out.println(error.toString());
            if (error instanceof InvocationTargetException) {
                InvocationTargetException exc = (InvocationTargetException)error;
                System.out.println("\t" + exc.getTargetException().toString());
            }
        }
    }
}
