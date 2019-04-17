
package com.learn.rpc.proxy;

import com.learn.rpc.core.exception.RpcServiceException;
import com.learn.rpc.protocol.RpcRequest;
import com.learn.rpc.register.ServiceDiscovery;
import com.learn.rpc.util.RequestIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


public class RefererInvocationHandler<T> extends AbstractRefererHandler<T> implements InvocationHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RefererInvocationHandler.class);

    public RefererInvocationHandler(Class<T> clz, ServiceDiscovery serviceDiscovery, String implCode) {
        this.clz = clz;
        this.interfaceName = clz.getName();
        super.serviceDiscovery = serviceDiscovery;
        super.implCode = implCode;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (isLocalMethod(method)) {
            if ("toString".equals(method.getName())) {
                return serviceDiscovery.toString();
            }
            if ("equals".equals(method.getName())) {
                return true;
            }
            throw new RpcServiceException("can not invoke local method:" + method.getName());
        }

        /*DefaultRequest request = new DefaultRequest();
        request.setRequestId(RequestIdGenerator.getRequestId());
        request.setArguments(args);
        String methodName = method.getName();
        request.setMethodName(methodName);
        request.setParamtersDesc(ReflectUtil.getMethodParamDesc(method));
        request.setInterfaceName(interfaceName);
        request.setParameterTypes(method.getParameterTypes());*/
        RpcRequest request = new RpcRequest();
        request.setInterfaceName(interfaceName);
        request.setMethodName(method.getName());
        request.setRequestId(String.valueOf(RequestIdGenerator.getRequestId()));
        request.setParameterTypes(method.getParameterTypes());
        request.setParameters(method.getParameters());
        return invokeRequest(request);
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
}
