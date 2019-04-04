
package com.learn.rpc.core.exception;

/**
 * wrapper client exception.
 *
 * 
 */
public class RpcFrameworkException extends RpcAbstractException {

    public RpcFrameworkException(String message) {
        super(message);
    }

    public RpcFrameworkException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcFrameworkException(String message, RpcErrorMsg rpcErrorMsg) {
        super(message, rpcErrorMsg);
    }
}
