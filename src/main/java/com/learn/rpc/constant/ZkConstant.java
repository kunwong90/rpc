package com.learn.rpc.constant;

public class ZkConstant {

    private ZkConstant() {

    }

    // ZK分隔符
    public static final String ZK_SEPARATOR = "/";

    //会话超时时间
    public static final int ZK_SESSION_TIMEOUT = 5000;

    public static final int ZK_CONNECTION_TIMEOUT = 1000;

    public static final String ZK_REGISTRY_PATH = "/registry";

    public static final String ZK_DATA_PATH = ZK_REGISTRY_PATH + "/data";

    // Root
    public static final String RPC_ZK_ROOT = ZK_SEPARATOR + "rpc";

    //Service
    public static final String RPC_ZK_SERVICE = "com.foo.BarService";

    //Type
    public static final String RPC_ZK_TYPE_PROVIDERS = ZK_SEPARATOR + "providers";

    public static final String RPC_ZK_TYPE_CONSUMERS = "consumers";

    //url
    public static final String RPC_ZK_URL = "10.20.153.10:20880";
}
