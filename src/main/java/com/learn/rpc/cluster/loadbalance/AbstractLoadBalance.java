package com.learn.rpc.cluster.loadbalance;

import com.learn.rpc.cluster.LoadBalance;
import com.learn.rpc.core.exception.RpcServiceException;
import com.learn.rpc.init.Referer;
import com.learn.rpc.init.Request;
import com.learn.rpc.util.RpcFrameworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class AbstractLoadBalance<T> implements LoadBalance<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractLoadBalance.class);

    public static final int MAX_REFERER_COUNT = 10;

    private List<Referer<T>> referers;

    @Override
    public void onRefresh(List<Referer<T>> referers) {
        // 只能引用替换，不能进行referers update。
        this.referers = referers;
    }

    @Override
    public Referer<T> select(Request request) {
        List<Referer<T>> referers = this.referers;
        if (referers == null) {
            throw new RpcServiceException(this.getClass().getSimpleName() + " No available referers for call request:" + request);
        }
        Referer<T> ref = null;
        if (referers.size() > 1) {
            ref = doSelect(request);

        } else if (referers.size() == 1) {
            ref = referers.get(0).isAvailable() ? referers.get(0) : null;
        }

        if (ref != null) {
            return ref;
        }
        throw new RpcServiceException(this.getClass().getSimpleName() + " No available referers for call request:" + request);
    }

    @Override
    public void selectToHolder(Request request, List<Referer<T>> refersHolder) {
        List<Referer<T>> referers = this.referers;

        if (referers == null) {
            throw new RpcServiceException(this.getClass().getSimpleName() + " No available referers for call : referers_size= 0 "
                    + RpcFrameworkUtil.toString(request));
        }

        if (referers.size() > 1) {
            doSelectToHolder(request, refersHolder);

        } else if (referers.size() == 1 && referers.get(0).isAvailable()) {
            refersHolder.add(referers.get(0));
        }
        if (refersHolder.isEmpty()) {
            throw new RpcServiceException(this.getClass().getSimpleName() + " No available referers for call : referers_size="
                    + referers.size() + " " + RpcFrameworkUtil.toString(request));
        }
    }

    protected List<Referer<T>> getReferers() {
        return referers;
    }

    @Override
    public void setWeightString(String weightString) {
        LOGGER.info("ignore weightString:" + weightString);
    }

    protected abstract Referer<T> doSelect(Request request);

    protected abstract void doSelectToHolder(Request request, List<Referer<T>> refersHolder);
}
