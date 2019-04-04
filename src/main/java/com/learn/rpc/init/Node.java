
package com.learn.rpc.init;

/**
 * node manage interface
 * 
 */
public interface Node {

    void init();

    void destroy();

    boolean isAvailable();

    String desc();

    URL getUrl();
}
