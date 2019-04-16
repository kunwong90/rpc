package com.learn.rpc.registry;

import com.learn.rpc.core.extension.Scope;
import com.learn.rpc.core.extension.Spi;
import com.learn.rpc.init.URL;

/**
 * 
 * To create registry
 *
 */
@Spi(scope = Scope.SINGLETON)
public interface RegistryFactory {

    Registry getRegistry(URL url);
}
