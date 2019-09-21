package com.ensuque.collab;

import java.io.Serializable;

public class CollabResult implements Serializable {

    private Object result;
    private boolean isSuccess;
    private Exception error;

    public CollabResult(Object result, boolean isSuccess) {
        this(result, isSuccess, null);
    }

    public CollabResult(Object result, boolean isSuccess, Exception err) {
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

}
