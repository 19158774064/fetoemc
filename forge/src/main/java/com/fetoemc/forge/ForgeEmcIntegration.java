package com.fetoemc.forge;

import com.fetoemc.FetoemcMod;
import com.fetoemc.api.EmcHandlerProvider;
import moze_intel.projecte.api.capabilities.PECapabilities;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.math.BigInteger;
import java.util.UUID;

/**
 * Forge 端 ProjectE EMC 集成
 * <p>
 * 在服务器启动时注册 EmcHandler，通过 ProjectE 的 IKnowledgeProvider
 * Capability 将 EMC 发送给指定玩家。
 * Team ProjectE 会自动拦截 setEmc() 调用实现团队 EMC 共享。
 * </p>
 */
@Mod.EventBusSubscriber(modid = FetoemcMod.MOD_ID)
public final class ForgeEmcIntegration {

    @SubscribeEvent
    public static void onServerAboutToStart(ServerAboutToStartEvent event) {
        MinecraftServer server = event.getServer();

        EmcHandlerProvider.setHandler((playerUuid, emcAmount) -> {
            // 在服务端线程中安全执行
            if (server.isSameThread()) {
                giveEmcInternal(server, playerUuid, emcAmount);
            } else {
                // 如果从其他线程调用，提交到服务端线程执行
                server.execute(() -> giveEmcInternal(server, playerUuid, emcAmount));
            }
        });

        FetoemcMod.LOGGER.info("[Fetoemc] Forge EmcHandler registered (ProjectE integration)");
    }

    /**
     * 实际执行 EMC 发放
     */
    private static void giveEmcInternal(MinecraftServer server, UUID playerUuid, long emcAmount) {
        if (server == null) return;

        ServerPlayer player = server.getPlayerList().getPlayer(playerUuid);
        if (player == null) return;

        player.getCapability(PECapabilities.KNOWLEDGE_CAPABILITY).ifPresent(knowledge -> {
            BigInteger current = knowledge.getEmc();
            knowledge.setEmc(current.add(BigInteger.valueOf(emcAmount)));
            knowledge.syncEmc(player);

            FetoemcMod.LOGGER.debug(
                    "[Fetoemc] Gave {} EMC to {} (total: {})",
                    emcAmount, player.getName().getString(), current.add(BigInteger.valueOf(emcAmount))
            );
        });
    }

    private ForgeEmcIntegration() {}
}