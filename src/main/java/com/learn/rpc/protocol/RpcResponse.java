package com.learn.rpc.protocol;

import java.io.Serializable;

public class RpcResponse implements Serializable {

    private String requestId;

    private Throwable throwable;

    private Object result;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    @Override
    public String toString() {
        return "RpcResponse{" +
                "requestId='" + requestId + '\'' +
                ", throwable=" + throwable +
                ", result=" + result +
                '}';
    }
}
