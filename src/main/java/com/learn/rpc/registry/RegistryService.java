package com.learn.rpc.registry;

import com.learn.rpc.init.URL;

import java.util.Collection;


/**
 * Register service to Restery center.
 */

public interface RegistryService {

    /**
     * register service to registry
     *
     * @param url
     */
    void register(URL url);

    /**
     * unregister service to registry
     *
     * @param url
     */
    void unregister(URL url);

    /**
     * set service status to available, so clients could use it
     *
     * @param url service url to be available, <b>null</b> means all services
     */
    void available(URL url);

    /**
     * set service status to unavailable, client should not discover services of unavailable state
     *
     * @param url service url to be unavailable, <b>null</b> means all services
     */
    void unavailable(URL url);

    Collection<URL> getRegisteredServiceUrls();
}
