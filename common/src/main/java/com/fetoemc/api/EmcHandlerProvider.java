package com.fetoemc.api;

/**
 * EMC 处理器静态提供者
 * <p>
 * 各平台模块在初始化时注入各自实现的 EmcHandler。
 * Forge 端注入对接 ProjectE 的实现，Fabric 端使用默认空实现。
 * </p>
 */
public final class EmcHandlerProvider {
    private static EmcHandler handler = (playerUuid, emcAmount) -> {
        // 默认空实现：无 ProjectE 时静默忽略
    };

    /** 设置 EMC 处理器（由平台模块在初始化时调用） */
    public static void setHandler(EmcHandler handler) {
        EmcHandlerProvider.handler = handler;
    }

    /** 获取当前 EMC 处理器 */
    public static EmcHandler getHandler() {
        return handler;
    }

    private EmcHandlerProvider() {}
}