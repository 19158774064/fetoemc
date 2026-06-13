package com.fetoemc.registry;

import com.fetoemc.FetoemcMod;
import com.fetoemc.block.entity.FemcConverterBlockEntity;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;

/**
 * 方块实体类型注册类
 */
public final class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(FetoemcMod.MOD_ID, Registries.BLOCK_ENTITY_TYPE);

    public static final RegistrySupplier<BlockEntityType<FemcConverterBlockEntity>> FEMC_CONVERTER =
            BLOCK_ENTITIES.register("femc_converter", () ->
                    BlockEntityType.Builder.of(FemcConverterBlockEntity::new,
                            ModBlocks.FEMC_CONVERTER.get()).build(null));

    public static void register() {
        BLOCK_ENTITIES.register();
    }

    private ModBlockEntities() {}
}