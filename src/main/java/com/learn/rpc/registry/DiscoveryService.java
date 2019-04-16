
package com.learn.rpc.registry;

import com.learn.rpc.init.URL;

import java.util.List;


/**
 * 
 * Deicovery service.
 *
 */

public interface DiscoveryService {

    void subscribe(URL url, NotifyListener listener);

    void unsubscribe(URL url, NotifyListener listener);

    List<URL> discover(URL url);
}
