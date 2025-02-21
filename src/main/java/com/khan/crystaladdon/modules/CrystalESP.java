package com.khan.crystaladdon.modules;

import com.khan.crystaladdon.CrystalAddon;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.settings.ColorSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.math.Box;

public class CrystalESP extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<SettingColor> crystalColor = sgGeneral.add(new ColorSetting.Builder()
        .name("crystal-color")
        .description("Color for rendering End Crystals.")
        .defaultValue(new SettingColor(255, 0, 0, 100))
        .build()
    );
    private final Setting<Integer> maxDistance = sgGeneral.add(new IntSetting.Builder()
        .name("max-distance")
        .description("Maximum distance (in blocks) to render ESP.")
        .defaultValue(12)
        .sliderRange(1, 300)
        .build()
    );

    public CrystalESP() {
        super(CrystalAddon.Main, "crystal-esp", "Highlights End Crystals in the world.");
    }

    @EventHandler
    private void onRender(Render3DEvent event) {
        if (mc.world == null || mc.player == null) return;

        mc.world.getEntitiesByClass(EndCrystalEntity.class, mc.player.getBoundingBox().expand(maxDistance.get()), e -> true)
            .forEach(crystal -> {
                if (mc.player.getPos().distanceTo(crystal.getPos()) > maxDistance.get()) return;

                Box box = crystal.getBoundingBox();
                event.renderer.box(box, crystalColor.get(), crystalColor.get(), ShapeMode.Both, 0);
            });
    }
}
