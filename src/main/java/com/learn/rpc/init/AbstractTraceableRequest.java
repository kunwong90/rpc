package com.learn.rpc.init;


import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;




public abstract class AbstractTraceableRequest implements TraceableRequest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTraceableRequest.class);

    private List<Pair<Runnable, Executor>> taskList = new ArrayList<>();
    private ConcurrentHashMap<String, String> traceInfoMap = new ConcurrentHashMap<>();
    private AtomicBoolean isFinished = new AtomicBoolean();
    private AtomicLong startTime = new AtomicLong();
    private AtomicLong endTime = new AtomicLong();

    @Override
    public long getStartTime() {
        return startTime.get();
    }

    @Override
    public void setStartTime(long startTime) {
        this.startTime.compareAndSet(0, startTime);
    }

    @Override
    public long getEndTime() {
        return endTime.get();
    }

    @Override
    public void addTraceInfo(String key, String value) {
        traceInfoMap.put(key, value);
    }

    @Override
    public String getTraceInfo(String key) {
        return traceInfoMap.get(key);
    }

    @Override
    public void addFinishCallback(Runnable runnable, Executor executor) {
        if (!isFinished.get()) {
            taskList.add(Pair.of(runnable, executor));
        }
    }

    @Override
    public void onFinish() {
        if (!isFinished.compareAndSet(false, true)) {
            return;
        }
        endTime.set(System.currentTimeMillis());
        for (Pair<Runnable, Executor> pair : taskList) {
            Runnable runnable = pair.getKey();
            Executor executor = pair.getValue();
            if (executor == null) {
                runnable.run();
            } else {
                try {
                    executor.execute(runnable);
                } catch (Exception e) {
                    LOGGER.error("TraceableRequest exec callback task error, e: ", e);
                }
            }
        }
    }
}
