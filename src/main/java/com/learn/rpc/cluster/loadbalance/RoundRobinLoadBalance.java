package com.learn.rpc.cluster.loadbalance;

import com.learn.rpc.core.extension.SpiMeta;
import com.learn.rpc.init.Referer;
import com.learn.rpc.init.Request;
import com.learn.rpc.util.MathUtil;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * Round robin loadbalance.
 *
 */
@SpiMeta(name = "roundrobin")
public class RoundRobinLoadBalance<T> extends AbstractLoadBalance<T> {

    private AtomicInteger idx = new AtomicInteger(0);

    @Override
    protected Referer<T> doSelect(Request request) {
        List<Referer<T>> referers = getReferers();

        int index = getNextNonNegative();
        for (int i = 0; i < referers.size(); i++) {
            Referer<T> ref = referers.get((i + index) % referers.size());
            if (ref.isAvailable()) {
                return ref;
            }
        }
        return null;
    }

    @Override
    protected void doSelectToHolder(Request request, List<Referer<T>> refersHolder) {
        List<Referer<T>> referers = getReferers();

        int index = getNextNonNegative();
        for (int i = 0, count = 0; i < referers.size() && count < MAX_REFERER_COUNT; i++) {
            Referer<T> referer = referers.get((i + index) % referers.size());
            if (referer.isAvailable()) {
                refersHolder.add(referer);
                count++;
            }
        }
    }

    private int getNextNonNegative() {
        return MathUtil.getNonNegative(idx.incrementAndGet());
    }
}
