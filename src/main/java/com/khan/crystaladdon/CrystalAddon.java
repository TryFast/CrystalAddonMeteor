package com.khan.crystaladdon;

import com.khan.crystaladdon.commands.BlocklistCommand;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.commands.Command;
import meteordevelopment.meteorclient.commands.Commands;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.khan.crystaladdon.modules.CrystalESP;
import com.khan.crystaladdon.modules.BloodKillEffect;
import com.khan.crystaladdon.modules.iTristanGreet;

public class CrystalAddon extends MeteorAddon {
    public static final Logger LOG = LoggerFactory.getLogger(CrystalAddon.class);
    public static final Category Main = new Category("CrystalAddon");

    @Override
    public void onInitialize() {
        LOG.info("Initializing CrystalAddon!");
        Modules.get().add(new CrystalESP());
        Modules.get().add(new BloodKillEffect());
        Modules.get().add(new iTristanGreet());
        Commands.add(new BlocklistCommand());
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
