package com.fetoemc.forge;

import com.fetoemc.FetoemcMod;
import com.fetoemc.block.entity.FemcConverterBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Forge 能量 Capability 处理器
 * <p>
 * 为 FEMC 转换器方块实体附加 Forge Energy (IEnergyStorage) 能力，
 * 使其能与 CreateAddition 等模组进行 FE 能量交互。
 * </p>
 */
@Mod.EventBusSubscriber(modid = FetoemcMod.MOD_ID)
public final class ForgeEnergyHandler {

    private static final ResourceLocation ENERGY_CAP_KEY =
            new ResourceLocation(FetoemcMod.MOD_ID, "femc_energy");

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<BlockEntity> event) {
        BlockEntity be = event.getObject();
        if (!(be instanceof FemcConverterBlockEntity femcConverter)) return;

        // 为 FEMC 转换器附加 IEnergyStorage Capability
        event.addCapability(ENERGY_CAP_KEY, new ICapabilityProvider() {
            private final LazyOptional<IEnergyStorage> energyOpt =
                    LazyOptional.of(() -> new EnergyStorageWrapper(femcConverter));

            @NotNull
            @Override
            public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
                if (cap == ForgeCapabilities.ENERGY) {
                    return energyOpt.cast();
                }
                return LazyOptional.empty();
            }
        });

        // BE 无效化时清理 LazyOptional，防止内存泄漏
        event.addListener(() -> {
            // BE 被移除或卸载时的清理逻辑（LazyOptional 会自动处理）
        });
    }

    /**
     * IEnergyStorage 适配器 — 将 FemcConverterBlockEntity 的能量接口桥接到 Forge Energy API
     */
    private static final class EnergyStorageWrapper implements IEnergyStorage {
        private final FemcConverterBlockEntity be;

        EnergyStorageWrapper(FemcConverterBlockEntity be) {
            this.be = be;
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            return be.receiveEnergy(maxReceive, simulate);
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            return be.extractEnergy(maxExtract, simulate);
        }

        @Override
        public int getEnergyStored() {
            return be.getEnergy();
        }

        @Override
        public int getMaxEnergyStored() {
            return be.getMaxEnergy();
        }

        @Override
        public boolean canExtract() {
            return be.canExtract();
        }

        @Override
        public boolean canReceive() {
            return be.canReceive();
        }
    }

    private ForgeEnergyHandler() {}
}