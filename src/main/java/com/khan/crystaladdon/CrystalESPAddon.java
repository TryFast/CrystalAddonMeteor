package com.khan.crystaladdon;

import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.khan.crystaladdon.modules.CrystalESP;

public class CrystalESPAddon extends MeteorAddon {
    public static final Logger LOG = LoggerFactory.getLogger(CrystalESPAddon.class);
    public static final Category Main = new Category("CrystalAddon");

    @Override
    public void onInitialize() {
        LOG.info("Initializing CrystalESPAddon!");
        Modules.get().add(new CrystalESP());
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(Main);
    }

    @Override
    public String getPackage() {
        return "com.khan.crystaladdon";
    }
}
