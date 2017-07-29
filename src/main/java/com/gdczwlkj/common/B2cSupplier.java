package com.gdczwlkj.common;

import java.util.Map;
import java.util.function.Supplier;

import org.slf4j.MDC;

/**
 * Created by ansion on 16/10/20.
 */
public class B2cSupplier<T> implements Supplier<T> {
    Map<String, String> map; //save parent's context
    Supplier<T> supplier;
    public B2cSupplier(Supplier<T> supplier) {
        map = MDC.getCopyOfContextMap();
        this.supplier = supplier;
    }

    @Override
    public T get() {
        //back this thread's context
        //clone parent context
        MDC.setContextMap(map);
        T result;
        try {
            result = supplier.get();
        } catch (Exception e) {
            throw  e;
        } finally {
            //recover context
            MDC.setContextMap(map);
        }
        return result;
    }
}
