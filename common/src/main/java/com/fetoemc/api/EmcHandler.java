package com.fetoemc.api;

import java.util.UUID;

/**
 * EMC 发放处理器接口
 * <p>
 * 由平台特定模块（Forge）实现，用于通过 ProjectE API 给玩家发放 EMC。
 * 当模组运行在无 ProjectE 的平台（Fabric）时，默认使用空实现。
 * </p>
 */
@FunctionalInterface
public interface EmcHandler {

    /**
     * 向指定玩家发放 EMC
     *
     * @param playerUuid 玩家 UUID
     * @param emcAmount  发放的 EMC 量
     */
    void giveEmc(UUID playerUuid, long emcAmount);
}