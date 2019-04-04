package com.learn.rpc.init;

/**
 * 
 * 类说明
 *
 */

public interface Caller<T> extends Node {

    Class<T> getInterface();

    Response call(Request request);
}
