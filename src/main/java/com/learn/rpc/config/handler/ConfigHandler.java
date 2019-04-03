package com.learn.rpc.config.handler;

import com.learn.rpc.core.extension.Scope;
import com.learn.rpc.core.extension.Spi;

/**
 * 
 * Handle urls which are from config.
 */
@Spi(scope = Scope.SINGLETON)
public interface ConfigHandler {

    /*<T> ClusterSupport<T> buildClusterSupport(Class<T> interfaceClass, List<URL> registryUrls);

    <T> Exporter<T> export(Class<T> interfaceClass, T ref, List<URL> registryUrls);

    <T> void unexport(List<Exporter<T>> exporters, Collection<URL> registryUrls);*/

    <T> T refer(Class<T> interfaceClass, String proxyType);
}
