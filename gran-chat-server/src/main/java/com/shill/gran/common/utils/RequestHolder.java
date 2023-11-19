package com.shill.gran.common.utils;

import com.shill.gran.common.framworkEntiry.dto.RequestInfo;
import org.springframework.stereotype.Component;

/**
 * 请求上下文-可以将IP放入此类中
 */
public class RequestHolder {
    private static final ThreadLocal<RequestInfo> threadLocal = new ThreadLocal<>();

    public static void set(RequestInfo requestInfo) {
        threadLocal.set(requestInfo);
    }

    public static RequestInfo get() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }
}
