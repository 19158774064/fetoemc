package com.fetoemc.registry;

import com.fetoemc.FetoemcMod;
import com.fetoemc.block.FemcConverterBlock;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * 方块注册类 — 集中管理所有自定义方块
 * 使用 DeferredRegister 保证注册表解冻前完成注册
 */
public final class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(FetoemcMod.MOD_ID, Registries.BLOCK);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(FetoemcMod.MOD_ID, Registries.ITEM);

    // FEMC 转换器 — 具有 BlockEntity 的方块，支持 FE 能量存储
    public static final RegistrySupplier<Block> FEMC_CONVERTER = BLOCKS.register("femc_converter",
            () -> new FemcConverterBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistrySupplier<Item> FEMC_CONVERTER_ITEM = ITEMS.register("femc_converter",
            () -> new BlockItem(FEMC_CONVERTER.get(), new Item.Properties()));

    /**
     * 初始化注册（各平台在合适的时机调用，先注册方块再注册物品）
     */
    public static void register() {
        BLOCKS.register();
        ITEMS.register();
    }

    private ModBlocks() {}
}