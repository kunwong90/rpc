package com.learn.rpc.init;

import java.util.concurrent.Executor;


public interface TraceableRequest extends Request {

    long getStartTime();

    void setStartTime(long startTime);

    long getEndTime();

    void addTraceInfo(String key, String value);

    String getTraceInfo(String key);

    void addFinishCallback(Runnable runnable, Executor executor);

    void onFinish();
}
