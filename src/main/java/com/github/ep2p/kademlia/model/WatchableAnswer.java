package com.github.ep2p.kademlia.model;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class WatchableAnswer<ID extends Number> extends Answer<ID> implements Watchable {
    private final CountDownLatch countDownLatch = new CountDownLatch(1);
    public void watch() throws InterruptedException {
        countDownLatch.await();
    }

    public void watch(long val, TimeUnit timeUnit) throws InterruptedException {
        countDownLatch.await(val, timeUnit);
    }

    public void release() {
        countDownLatch.countDown();
    }
}
