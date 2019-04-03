
package com.learn.rpc.proxy;

import com.learn.rpc.core.exception.RpcServiceException;
import com.learn.rpc.init.DefaultRequest;
import com.learn.rpc.util.ReflectUtil;
import com.learn.rpc.util.RequestIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


public class RefererInvocationHandler<T> extends AbstractRefererHandler<T> implements InvocationHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RefererInvocationHandler.class);

    public RefererInvocationHandler(Class<T> clz) {
        this.clz = clz;
        interfaceName = clz.getName();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (isLocalMethod(method)) {
            if ("toString".equals(method.getName())) {
                return "";
            }
            if ("equals".equals(method.getName())) {
                return true;
            }
            throw new RpcServiceException("can not invoke local method:" + method.getName());
        }

        DefaultRequest request = new DefaultRequest();
        request.setRequestId(RequestIdGenerator.getRequestId());
        request.setArguments(args);
        String methodName = method.getName();
        boolean async = false;
        request.setMethodName(methodName);
        request.setParamtersDesc(ReflectUtil.getMethodParamDesc(method));
        request.setInterfaceName(interfaceName);

        return invokeRequest(request, getRealReturnType(async, this.clz, method, methodName), async);
    }

    /**
     * tostring,equals,hashCode,finalize等接口未声明的方法不进行远程调用
     *
     * @param method
     * @return
     */
    public boolean isLocalMethod(Method method) {
        if (method.getDeclaringClass().equals(Object.class)) {
            try {
                Method interfaceMethod = clz.getDeclaredMethod(method.getName(), method.getParameterTypes());
                return false;
            } catch (Exception e) {
                return true;
            }
        }
        return false;
    }


    private Class<?> getRealReturnType(boolean asyncCall, Class<?> clazz, Method method, String methodName) {
        if (asyncCall) {
            try {
                Method m = clazz.getMethod(methodName, method.getParameterTypes());
                return m.getReturnType();
            } catch (Exception e) {
                LOGGER.warn("RefererInvocationHandler get real return type fail. err:" + e.getMessage());
                return method.getReturnType();
            }
        } else {
            return method.getReturnType();
        }
    }

}
