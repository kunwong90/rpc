
package com.learn.rpc.core.exception;

/**
 * wrapper client exception.
 *
 * 
 */
public class RpcFrameworkException extends RuntimeException {

    public RpcFrameworkException(String message) {
        super(message);
    }

    public RpcFrameworkException(String message, Throwable cause) {
        super(message, cause);
    }
}
