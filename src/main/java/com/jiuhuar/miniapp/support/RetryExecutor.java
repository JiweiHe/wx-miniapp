package com.jiuhuar.miniapp.support;

import java.util.Optional;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 提供可重试的方法执行
 */
public class RetryExecutor {
    private static Logger logger = LoggerFactory.getLogger(RetryExecutor.class);

    private static final int DEFAULT_MAX_RETRY_COUNT = 3;
    private static final int DEFAULT_INTERVAL = 500;

    /**
     * @param supplier 要执行的方法，如果执行不成功，需要抛出exception
     * @return supplier#get的结果
     */
    public static <T> Optional<T> execute(Supplier<T> supplier) {
        return execute(DEFAULT_MAX_RETRY_COUNT, supplier);
    }

    /**
     * @param maxRetryCnt 最大重试次数
     * @param supplier    要执行的方法，如果执行不成功，需要抛出exception
     * @return supplier#get的结果
     */
    public static <T> Optional<T> execute(int maxRetryCnt, Supplier<T> supplier) {
        return execute(maxRetryCnt, DEFAULT_INTERVAL, supplier);
    }

    /**
     * @param maxRetryCnt 最大重试次数
     * @param interval    每次重试之间的间隔，毫秒
     * @param supplier    要执行的方法，如果执行不成功，需要抛出exception
     * @return supplier#get的结果
     */
    public static <T> Optional<T> execute(int maxRetryCnt, int interval, Supplier<T> supplier) {
        int retryCnt = 0;
        T result = null;
        do {
            try {
                result = supplier.get();
                break;
            } catch (Exception e) {
                retryCnt++;
                logger.warn("failed {} times when execute {}, exception: {}", retryCnt, supplier, e.getLocalizedMessage());
                try {
                    Thread.sleep(interval);
                } catch (InterruptedException ignored) {
                }
            }
        } while (retryCnt < maxRetryCnt);

        if (result == null) {
            logger.error("failed to execute {} in total {} times", supplier, maxRetryCnt);
        }

        return Optional.ofNullable(result);
    }
}
