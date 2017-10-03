package com.learn.rpc.constant;

public class ZkConstant {

    private ZkConstant() {

    }
    //会话超时时间
    public static final int ZK_SESSION_TIMEOUT = 5000;

    public static final int ZK_CONNECTION_TIMEOUT = 1000;

    public static final String ZK_REGISTRY_PATH = "/registry";

    public static final String ZK_DATA_PATH = ZK_REGISTRY_PATH + "/data";
}
