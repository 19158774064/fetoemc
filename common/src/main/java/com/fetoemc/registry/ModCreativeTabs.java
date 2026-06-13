package com.fetoemc.registry;

import com.fetoemc.FetoemcMod;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

/**
 * 创造模式物品栏分类注册
 */
public final class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(FetoemcMod.MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> FEMC_TAB = TABS.register("femc_tab",
            () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                    .title(Component.translatable("itemGroup." + FetoemcMod.MOD_ID + ".femc_tab"))
                    .icon(() -> new ItemStack(ModBlocks.FEMC_CONVERTER.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ModBlocks.FEMC_CONVERTER.get());
                    })
                    .build()
    );

    public static void register() {
        TABS.register();
    }

    private ModCreativeTabs() {}
}