package com.learn.rpc.proxy;

import com.learn.rpc.init.DefaultRequest;
import com.learn.rpc.init.Request;
import com.learn.rpc.util.RequestIdGenerator;

import java.util.Map;


public class RefererCommonHandler<T> extends AbstractRefererHandler<T> implements CommonHandler {

    public RefererCommonHandler(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public Object call(String methodName, Object[] arguments, Class returnType, Map<String, String> attachments, boolean async) throws Throwable {
        DefaultRequest request = new DefaultRequest();
        request.setRequestId(RequestIdGenerator.getRequestId());
        request.setInterfaceName(interfaceName);
        request.setMethodName(methodName);
        request.setArguments(arguments);
        request.setAttachments(attachments);
        return invokeRequest(request, returnType, async);
    }

    @Override
    public Object call(String methodName, Object[] arguments, Class returnType) throws Throwable {
        return null;
    }

    @Override
    public Object asyncCall(String methodName, Object[] arguments, Class returnType) throws Throwable {
        return null;
    }

    @Override
    public Object call(Request request, Class returnType) throws Throwable {
        return null;
    }

    @Override
    public Object asyncCall(Request request, Class returnType) throws Throwable {
        return null;
    }

    @Override
    public Request buildRequest(String methodName, Object[] arguments) {
        return null;
    }

    /*@Override
    public Object call(String methodName, Object[] arguments, Class returnType) throws Throwable {
        return call(methodName, arguments, returnType, null, false);
    }

    @Override
    public Object asyncCall(String methodName, Object[] arguments, Class returnType) throws Throwable {
        return call(methodName, arguments, returnType, null, true);
    }

    @Override
    public Object call(Request request, Class returnType) throws Throwable {
        return invokeRequest(request, returnType, false);
    }

    @Override
    public Object asyncCall(Request request, Class returnType) throws Throwable {
        return invokeRequest(request, returnType, true);
    }

    @Override
    public Request buildRequest(String methodName, Object[] arguments) {
        DefaultRequest request = new DefaultRequest();
        request.setRequestId(RequestIdGenerator.getRequestId());
        request.setInterfaceName(interfaceName);
        request.setMethodName(methodName);
        request.setArguments(arguments);
        return request;
    }*/

}
