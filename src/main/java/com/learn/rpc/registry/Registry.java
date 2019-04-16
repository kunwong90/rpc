
package com.learn.rpc.registry;

import com.learn.rpc.core.extension.Scope;
import com.learn.rpc.core.extension.Spi;
import com.learn.rpc.init.URL;

/**
 * 
 * Used to register and discover.
 *
 */
@Spi(scope = Scope.SINGLETON)
public interface Registry extends RegistryService, DiscoveryService {

    URL getUrl();
}
