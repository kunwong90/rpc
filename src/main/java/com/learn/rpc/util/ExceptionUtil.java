package com.learn.rpc.util;

import com.alibaba.fastjson.JSONObject;
import com.learn.rpc.core.exception.RpcAbstractException;
import com.learn.rpc.core.exception.RpcBizException;
import com.learn.rpc.core.exception.RpcErrorMsg;
import com.learn.rpc.core.exception.RpcFrameworkException;
import com.learn.rpc.core.exception.RpcServiceException;
import org.apache.commons.lang3.StringUtils;

/**
 * @author maijunsheng
 * @version 创建时间：2013-6-14
 */
public class ExceptionUtil {

    public static final StackTraceElement[] REMOTE_MOCK_STACK = new StackTraceElement[]{new StackTraceElement("remoteClass",
            "remoteMethod", "remoteFile", 1)};

    /**
     * 判定是否是业务方的逻辑抛出的异常
     * <p>
     * <pre>
     * 		true: 来自业务方的异常
     * 		false: 来自框架本身的异常
     * </pre>
     *
     * @param e
     * @return
     */
    @Deprecated
    public static boolean isBizException(Exception e) {
        return e instanceof RpcBizException;
    }

    public static boolean isBizException(Throwable t) {
        return t instanceof RpcBizException;
    }


    /**
     * 是否框架包装过的异常
     *
     * @param e
     * @return
     */
    @Deprecated
    public static boolean isMotanException(Exception e) {
        return e instanceof RpcAbstractException;
    }

    public static boolean isMotanException(Throwable t) {
        return t instanceof RpcAbstractException;
    }

    public static String toMessage(Exception e) {
        JSONObject jsonObject = new JSONObject();
        int type = 1;
        int code = 500;
        String errmsg = null;

        if (e instanceof RpcFrameworkException) {
            RpcFrameworkException mfe = (RpcFrameworkException) e;
            type = 0;
            code = mfe.getErrorCode();
            errmsg = mfe.getOriginMessage();
        } else if (e instanceof RpcServiceException) {
            RpcServiceException mse = (RpcServiceException) e;
            type = 1;
            code = mse.getErrorCode();
            errmsg = mse.getOriginMessage();
        } else if (e instanceof RpcBizException) {
            RpcBizException mbe = (RpcBizException) e;
            type = 2;
            code = mbe.getErrorCode();
            errmsg = mbe.getOriginMessage();
            if(mbe.getCause() != null){
                errmsg = errmsg + ", org err:" + mbe.getCause().getMessage();
            }
        } else {
            errmsg = e.getMessage();
        }
        jsonObject.put("errcode", code);
        jsonObject.put("errmsg", errmsg);
        jsonObject.put("errtype", type);
        return jsonObject.toString();
    }

    public static RpcAbstractException fromMessage(String msg) {
        if (StringUtils.isNotBlank(msg)) {
            try {
                JSONObject jsonObject = JSONObject.parseObject(msg);
                int type = jsonObject.getIntValue("errtype");
                int errcode = jsonObject.getIntValue("errcode");
                String errmsg = jsonObject.getString("errmsg");
                RpcAbstractException e = null;
                switch (type) {
                    case 1:
                        e = new RpcServiceException(errmsg, new RpcErrorMsg(errcode, errcode, errmsg));
                        break;
                    case 2:
                        e = new RpcBizException(errmsg, new RpcErrorMsg(errcode, errcode, errmsg));
                        break;
                    default:
                        e = new RpcFrameworkException(errmsg, new RpcErrorMsg(errcode, errcode, errmsg));
                }
                return e;
            } catch (Exception e) {
                //LoggerUtil.warn("build exception from msg fail. msg:" + msg);
            }
        }
        return null;
    }

    /**
     * 覆盖给定exception的stack信息，server端产生业务异常时调用此类屏蔽掉server端的异常栈。
     *
     * @param e
     */
    public static void setMockStackTrace(Throwable e) {
        if (e != null) {
            try {
                e.setStackTrace(REMOTE_MOCK_STACK);
            } catch (Exception e1) {
                //LoggerUtil.warn("replace remote exception stack fail!" + e1.getMessage());
            }
        }
    }
}
