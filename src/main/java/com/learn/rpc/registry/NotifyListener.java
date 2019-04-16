
package com.learn.rpc.registry;

import com.learn.rpc.init.URL;

import java.util.List;


/**
 * 
 * Notify when service changed.
 *
 * @author fishermen
 * @version V1.0 created at: 2013-5-16
 */

public interface NotifyListener {

    void notify(URL registryUrl, List<URL> urls);
}
