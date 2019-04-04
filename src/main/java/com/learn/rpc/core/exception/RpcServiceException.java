package com.learn.rpc.core.exception;

public class RpcServiceException extends RpcAbstractException {

    public RpcServiceException(String message) {
        super(message);
    }

    public RpcServiceException(String message, RpcErrorMsg rpcErrorMsg) {
        super(message, rpcErrorMsg);
    }
}
