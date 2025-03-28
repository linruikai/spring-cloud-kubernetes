package org.example.base.config;


import com.alibaba.ttl.TransmittableThreadLocal;

public class GrayHeaderContextHolder {
    // https://github.com/alibaba/transmittable-thread-local
    // TransmittableThreadLocal 是阿里开源的一个增强 InheritableThreadLocal 的库，能够很好地解决使用线程池在线程之间复制值混乱的问题
    private static final TransmittableThreadLocal<String> contextHolder = new TransmittableThreadLocal<>();

    public static void setGrayHeader(String header) {
        contextHolder.set(header);
    }

    public static String getGrayHeader() {
        return contextHolder.get();
    }

    public static void clearGrayHeader() {
        contextHolder.remove();
    }
}
