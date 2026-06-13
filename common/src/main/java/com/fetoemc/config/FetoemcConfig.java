package com.fetoemc.config;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * FEMC 转换器配置持有类（线程安全）
 * <p>由各平台在初始化时注入实际值，FemcConverterBlockEntity 通过此类读取。</p>
 */
public final class FetoemcConfig {

    private FetoemcConfig() {}

    /** 每次转换消耗的 FE 量，默认 50k */
    private static final AtomicInteger fePerConversion = new AtomicInteger(50_000);

    /** 每次转换产出的 EMC 量，默认 5000 */
    private static final AtomicInteger emcPerConversion = new AtomicInteger(5000);

    // ====== 读取 ======

    public static int getFePerConversion() {
        return fePerConversion.get();
    }

    public static int getEmcPerConversion() {
        return emcPerConversion.get();
    }

    // ====== 写入（由各平台配置系统调用） ======

    public static void setFePerConversion(int value) {
        fePerConversion.set(value);
    }

    public static void setEmcPerConversion(int value) {
        emcPerConversion.set(value);
    }
}