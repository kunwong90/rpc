package com.learn.rpc.config.springsupport;

import com.learn.rpc.annotation.Reference;
import com.learn.rpc.client.RpcClientHandler;
import com.learn.rpc.protocol.RpcRequest;
import com.learn.rpc.protocol.RpcResponse;
import com.learn.rpc.register.ServiceDiscovery;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.UUID;

public class ReferenceAnnotationBeanPostProcessor implements BeanPostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReferenceAnnotationBeanPostProcessor.class);

    private ServiceDiscovery serviceDiscovery;

    private String[] annotationPackages;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (!isMatchPackage(bean)) {
            return bean;
        }
        Class<?> clazz = bean.getClass();
        if (isProxyBean(bean)) {
            clazz = AopUtils.getTargetClass(bean);
        }
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            String name = method.getName();
            if (name.length() > 3 && name.startsWith("set")
                    && method.getParameterTypes().length == 1
                    && Modifier.isPublic(method.getModifiers())
                    && !Modifier.isStatic(method.getModifiers())) {
                try {
                    Reference reference = method.getAnnotation(Reference.class);
                    if (reference != null) {
                        Object value = refer(reference, method.getParameterTypes()[0]);
                        if (value != null) {
                            method.invoke(bean, new Object[]{value});
                        }
                    }
                } catch (Exception e) {
                    throw new BeanInitializationException("Failed to init remote service reference at method " + name
                            + " in class " + bean.getClass().getName(), e);
                }
            }
        }


        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            try {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                Reference reference = field.getAnnotation(Reference.class);
                if (reference != null) {
                    if (!Modifier.isStatic(field.getModifiers())) {
                        Object value = refer(reference, field.getType());
                        if (value != null) {
                            field.set(bean, value);
                        }
                    }
                }
            } catch (Exception e) {
                throw new BeanInitializationException("Failed to init remote service reference at filed " + field.getName()
                        + " in class " + bean.getClass().getName(), e);
            }
        }
        return bean;
    }




    private boolean isMatchPackage(Object bean) {
        if (annotationPackages == null || annotationPackages.length == 0) {
            return true;
        }
        Class clazz = bean.getClass();
        if (isProxyBean(bean)) {
            clazz = AopUtils.getTargetClass(bean);
        }
        String beanClassName = clazz.getName();
        for (String pkg : annotationPackages) {
            if (beanClassName.startsWith(pkg)) {
                return true;
            }
        }
        return false;
    }

    private boolean isProxyBean(Object bean) {
        return AopUtils.isAopProxy(bean);
    }


    private <T> Object refer(Reference reference, Class<T> referenceClass) {
        if (!referenceClass.isInterface()) {
            throw new IllegalStateException("The @Reference undefined interfaceClass or interfaceName, and the property type "
                    + referenceClass.getName() + " is not a interface.");
        }
        String implCode = reference.implCode();
        return (T) Proxy.newProxyInstance(
                referenceClass.getClassLoader(),
                new Class<?>[]{referenceClass}, (Object proxy, Method method, Object[] args) -> {
                    // 创建 RPC 请求对象并设置请求属性
                    RpcRequest request = new RpcRequest();
                    request.setRequestId(UUID.randomUUID().toString());
                    request.setInterfaceName(method.getDeclaringClass().getName());
                    request.setMethodName(method.getName());
                    request.setParameterTypes(method.getParameterTypes());
                    request.setParameters(args);
                    String serviceAddress = null;
                    // 获取 RPC 服务地址
                    if (serviceDiscovery != null) {
                        String serviceName = referenceClass.getName();
                        if (StringUtils.isNotBlank(implCode)) {
                            serviceName = serviceName + "#" + implCode;
                        }
                        serviceAddress = serviceDiscovery.discover(serviceName);
                        LOGGER.info("discover service: {} => {}", serviceName, serviceAddress);
                    }
                    if (StringUtils.isEmpty(serviceAddress)) {
                        throw new RuntimeException("server address is empty");
                    }
                    // 从 RPC 服务地址中解析主机名与端口号
                    String[] array = StringUtils.split(serviceAddress, ":");
                    String host = array[0];
                    int port = Integer.parseInt(array[1]);
                    // 创建 RPC 客户端对象并发送 RPC 请求
                    RpcClientHandler rpcClientHandler = new RpcClientHandler(host, port);
                    long time = System.currentTimeMillis();
                    RpcResponse response = rpcClientHandler.sendRequest(request);
                    LOGGER.info("cost time: {} ms", System.currentTimeMillis() - time);
                    if (response == null) {
                        throw new RuntimeException("response is null");
                    }
                    // 返回 RPC 响应结果
                    return response.getResult();
                }
        );
    }

    public ServiceDiscovery getServiceDiscovery() {
        return serviceDiscovery;
    }

    public void setServiceDiscovery(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    public String[] getAnnotationPackages() {
        return annotationPackages;
    }

    public void setAnnotationPackages(String[] annotationPackages) {
        this.annotationPackages = annotationPackages;
    }
}
