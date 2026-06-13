package com.fetoemc.block.entity;

import com.fetoemc.api.EmcHandlerProvider;
import com.fetoemc.config.FetoemcConfig;
import com.fetoemc.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * FEMC 转换器方块实体
 * <p>
 * 存储 FE 能量（最大 100k FE），每 10 tick 将 50k FE 转换为 1000 EMC
 * 发放给放置该方块的玩家（兼容 Team ProjectE 团队 EMC 共享）。
 * </p>
 */
public class FemcConverterBlockEntity extends BlockEntity {

    /** 最大能量容量（100k FE） */
    public static final int MAX_ENERGY = 100_000;

    /** 每 tick 最大输入 */
    private static final int MAX_RECEIVE = 5_000;

    /** 每 tick 最大输出 */
    private static final int MAX_EXTRACT = 5_000;

    /** 转换间隔（tick） */
    public static final int CONVERSION_INTERVAL = 10;

    /** 当前存储能量，使用 AtomicInteger 保证并发安全 */
    private final AtomicInteger energy = new AtomicInteger(0);

    /** 放置该方块的玩家 UUID（序列化到 NBT） */
    @Nullable
    private UUID placerUuid;

    public FemcConverterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FEMC_CONVERTER.get(), pos, state);
    }

    // ====== 放置者管理 ======

    /** 设置放置者 */
    public void setPlacer(@Nullable Player player) {
        this.placerUuid = player != null ? player.getUUID() : null;
        setChanged();
    }

    /** 获取放置者 UUID */
    @Nullable
    public UUID getPlacerUuid() {
        return placerUuid;
    }

    // ====== 能量访问接口 ======

    /** 获取当前能量 */
    public int getEnergy() {
        return energy.get();
    }

    /** 获取最大容量 */
    public int getMaxEnergy() {
        return MAX_ENERGY;
    }

    /**
     * 接收能量
     */
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int toReceive = Math.min(maxReceive, Math.min(MAX_RECEIVE, MAX_ENERGY - energy.get()));
        if (!simulate && toReceive > 0) {
            energy.addAndGet(toReceive);
            setChanged();
        }
        return toReceive;
    }

    /**
     * 提取能量
     */
    public int extractEnergy(int maxExtract, boolean simulate) {
        int current = energy.get();
        int toExtract = Math.min(maxExtract, Math.min(MAX_EXTRACT, current));
        if (!simulate && toExtract > 0) {
            energy.addAndGet(-toExtract);
            setChanged();
        }
        return toExtract;
    }

    /** 当前是否可提取 */
    public boolean canExtract() {
        return energy.get() > 0;
    }

    /** 当前是否可接收 */
    public boolean canReceive() {
        return energy.get() < MAX_ENERGY;
    }

    // ====== 数据持久化 ======

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("Energy", energy.get());
        if (placerUuid != null) {
            tag.putUUID("PlacerUUID", placerUuid);
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        energy.set(tag.getInt("Energy"));
        placerUuid = tag.hasUUID("PlacerUUID") ? tag.getUUID("PlacerUUID") : null;
    }

    // ====== Tick 逻辑 ======

    /**
     * 每 tick 调用（服务端）
     * <p>
     * 每 10 tick 检查一次：若能量 ≥ 配置的 FE 量，扣除并发放对应 EMC
     * 给放置方块的玩家。所有方块在同一 tick 计算以节省性能。
     * </p>
     */
    @SuppressWarnings("unused")
    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level == null || level.isClientSide) return;
        if (placerUuid == null) return;

        // 统一在 tick % 10 == 0 时检查，所有方块同 tick 计算
        if (level.getGameTime() % CONVERSION_INTERVAL != 0) return;

        int feCost = FetoemcConfig.getFePerConversion();
        int emcReward = FetoemcConfig.getEmcPerConversion();

        // 检查 FE 是否足够
        if (energy.get() < feCost) return;

        // 原子操作：扣除 FE
        int current = energy.get();
        if (current < feCost) return;

        energy.addAndGet(-feCost);
        setChanged();

        // 通过 EmcHandler 发放 EMC（平台无关的抽象）
        EmcHandlerProvider.getHandler().giveEmc(placerUuid, emcReward);
    }

    // ====== 同步客户端 ======

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        tag.putInt("Energy", energy.get());
        return tag;
    }
}