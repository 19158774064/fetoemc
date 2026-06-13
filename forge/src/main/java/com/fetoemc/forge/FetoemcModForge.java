package com.fetoemc.forge;

import com.fetoemc.FetoemcMod;
import com.fetoemc.registry.ModBlockEntities;
import com.fetoemc.registry.ModBlocks;
import com.fetoemc.registry.ModCreativeTabs;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(FetoemcMod.MOD_ID)
public final class FetoemcModForge {
    public FetoemcModForge() {
        var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        EventBuses.registerModEventBus(FetoemcMod.MOD_ID, modEventBus);

        // 注册 Forge 配置（生成 config/fetoemc.toml）
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ForgeConfig.SPEC);

        ModBlocks.register();
        ModBlockEntities.register();
        ModCreativeTabs.register();

        new FetoemcMod();
        MinecraftForge.EVENT_BUS.register(this);
    }
}