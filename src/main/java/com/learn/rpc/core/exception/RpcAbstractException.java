package com.learn.rpc.core.exception;

import com.learn.rpc.init.RpcContext;


public abstract class RpcAbstractException extends RuntimeException {
    private static final long serialVersionUID = -8742311167276890503L;

    protected RpcErrorMsg motanErrorMsg = RpcErrorMsgConstant.FRAMEWORK_DEFAULT_ERROR;
    protected String errorMsg = null;

    public RpcAbstractException() {
        super();
    }

    public RpcAbstractException(RpcErrorMsg motanErrorMsg) {
        super();
        this.motanErrorMsg = motanErrorMsg;
    }

    public RpcAbstractException(String message) {
        super(message);
        this.errorMsg = message;
    }

    public RpcAbstractException(String message, RpcErrorMsg motanErrorMsg) {
        super(message);
        this.motanErrorMsg = motanErrorMsg;
        this.errorMsg = message;
    }

    public RpcAbstractException(String message, Throwable cause) {
        super(message, cause);
        this.errorMsg = message;
    }

    public RpcAbstractException(String message, Throwable cause, RpcErrorMsg motanErrorMsg) {
        super(message, cause);
        this.motanErrorMsg = motanErrorMsg;
        this.errorMsg = message;
    }

    public RpcAbstractException(Throwable cause) {
        super(cause);
    }

    public RpcAbstractException(Throwable cause, RpcErrorMsg motanErrorMsg) {
        super(cause);
        this.motanErrorMsg = motanErrorMsg;
    }

    @Override
    public String getMessage() {
        String message = getOriginMessage();

        return "error_message: " + message + ", status: " + motanErrorMsg.getStatus() + ", error_code: " + motanErrorMsg.getErrorCode()
                + ",r=" + RpcContext.getContext().getRequestId();
    }

    public String getOriginMessage(){
        if (motanErrorMsg == null) {
            return super.getMessage();
        }

        String message;

        if (errorMsg != null && !"".equals(errorMsg)) {
            message = errorMsg;
        } else {
            message = motanErrorMsg.getMessage();
        }
        return message;
    }

    public int getStatus() {
        return motanErrorMsg != null ? motanErrorMsg.getStatus() : 0;
    }

    public int getErrorCode() {
        return motanErrorMsg != null ? motanErrorMsg.getErrorCode() : 0;
    }

    public RpcErrorMsg getMotanErrorMsg() {
        return motanErrorMsg;
    }
}
