package com.khan.crystaladdon.modules;

import com.khan.crystaladdon.CrystalAddon;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;

import java.util.HashSet;
import java.util.Set;

public class BloodKillEffect extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> playersOnly = sgGeneral.add(new BoolSetting.Builder()
        .name("players-only")
        .description("Only spawn particles when players are killed (not mobs).")
        .defaultValue(true)
        .build()
    );

    private final Setting<Integer> particleMultiplier = sgGeneral.add(new IntSetting.Builder()
        .name("particle-multiplier")
        .description("Number of particles to spawn on death.")
        .defaultValue(128)
        .min(1)
        .max(256)
        .sliderMin(1)
        .sliderMax(256)
        .build()
    );

    private final Set<Integer> processedEntities = new HashSet<>();

    public BloodKillEffect() {
        super(CrystalAddon.Main, "blood-kill-effect", "Renders a redstone block particle effect when a living entity dies.");
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (mc.world == null) return;

        processedEntities.removeIf(id -> mc.world.getEntityById(id) == null);

        for (Entity entity : mc.world.getEntities()) {
            if (!(entity instanceof LivingEntity)) continue;

            if (playersOnly.get() && !(entity instanceof PlayerEntity)) continue;

            LivingEntity living = (LivingEntity) entity;

            if (living.getHealth() <= 0 && !processedEntities.contains(entity.getId())) {
                processedEntities.add(entity.getId());

                double x = living.getX();
                double y = living.getY() + living.getEyeHeight(living.getPose());
                double z = living.getZ();
                BlockState blockState = Blocks.REDSTONE_BLOCK.getDefaultState();

                for (int i = 0; i < particleMultiplier.get(); i++) {
                    mc.world.addParticle(
                        new BlockStateParticleEffect(ParticleTypes.BLOCK, blockState),
                        x, y, z,
                        0, 0, 0
                    );
                }
            }
        }
    }
}
