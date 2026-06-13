package com.fetoemc.forge;

import com.fetoemc.config.FetoemcConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

/**
 * Forge 平台配置文件（生成 .toml 到 config/ 目录）
 * <p>默认值：每 50k FE → 5000 EMC</p>
 */
@Mod.EventBusSubscriber(modid = "fetoemc", bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ForgeConfig {

    private ForgeConfig() {}

    // ====== ForgeConfigSpec 构建 ======

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.IntValue FE_PER_CONVERSION = BUILDER
            .comment("每次转换消耗的 FE 量（默认 50000）")
            .defineInRange("fe_per_conversion", 50_000, 1, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue EMC_PER_CONVERSION = BUILDER
            .comment("每次转换产出的 EMC 量（默认 5000）")
            .defineInRange("emc_per_conversion", 5000, 1, Integer.MAX_VALUE);

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    // ====== 从 config → FetoemcConfig（线程安全） ======

    @SubscribeEvent
    public static void onLoad(ModConfigEvent event) {
        FetoemcConfig.setFePerConversion(FE_PER_CONVERSION.get());
        FetoemcConfig.setEmcPerConversion(EMC_PER_CONVERSION.get());
    }
}