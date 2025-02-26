package com.khan.crystaladdon.modules;

import com.khan.crystaladdon.CrystalAddon;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.settings.StringSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket.Action;

import java.awt.Desktop;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class iTristanGreet extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<String> message = sgGeneral.add(new StringSetting.Builder()
        .name("greeting-message")
        .description("Customizable greeting message with {player} placeholder.")
        .defaultValue("Welcome {player} to the server!")
        .build()
    );

    private final Setting<Integer> messageDelay = sgGeneral.add(new IntSetting.Builder()
        .name("message-delay")
        .description("Delay in seconds between greeting messages.")
        .defaultValue(5)
        .min(1)
        .sliderMax(60)
        .build()
    );

    private List<String> blockedUsers = new ArrayList<>();
    private final Path blocklistPath = Paths.get("crystaladdon", "greet_blocklist.txt");

    public iTristanGreet() {
        super(CrystalAddon.Main, "itristan's-greet", "Greets players when they join the server.");
        ensureBlocklistExists();
        loadBlocklist();
    }

    @Override
    public void onActivate() {
        loadBlocklist();
    }

    private void ensureBlocklistExists() {
        try {
            if (!Files.exists(blocklistPath.getParent())) {
                Files.createDirectories(blocklistPath.getParent());
            }

            if (!Files.exists(blocklistPath)) {
                Files.createFile(blocklistPath);
                Files.write(blocklistPath, List.of("# Add player names to ignore (one per line)"));
            }
        } catch (IOException e) {
            error("Failed to create blocklist file: " + e.getMessage());
        }
    }

    public void loadBlocklist() {
        blockedUsers.clear();
        try {
            ensureBlocklistExists();

            List<String> lines = Files.readAllLines(blocklistPath);

            for (String line : lines) {
                line = line.trim();
                if (!line.isEmpty() && !line.startsWith("#")) {
                    blockedUsers.add(line);
                }
            }

            info("Loaded " + blockedUsers.size() + " blocked users.");
        } catch (IOException e) {
            error("Failed to load blocklist: " + e.getMessage());
        }
    }

    public void openBlocklist() {
        try {
            ensureBlocklistExists();

            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                desktop.open(blocklistPath.toFile());
                info("Opened blocklist file.");
            } else {
                error("Desktop not supported - cannot open file automatically.");
                info("Blocklist located at: " + blocklistPath.toAbsolutePath());
            }
        } catch (IOException e) {
            error("Failed to open blocklist file: " + e.getMessage());
        }
    }

    @EventHandler
    private void onPlayerJoin(PacketEvent.Receive event) {
        if (event.packet instanceof PlayerListS2CPacket packet && packet.getActions().contains(Action.ADD_PLAYER)) {
            packet.getEntries().forEach(entry -> {
                String playerName = entry.profile().getName();

                if (blockedUsers.contains(playerName)) {
                    info("Skipping greeting for blocked user: " + playerName);
                    return;
                }

                Thread thread = new Thread(() -> {
                    try {
                        int delayMs = messageDelay.get() * 1000;
                        Thread.sleep(delayMs);

                        String formattedMessage = message.get().replace("{player}", playerName);

                        MinecraftClient.getInstance().execute(() ->
                            MinecraftClient.getInstance().getNetworkHandler().sendChatMessage(formattedMessage)
                        );
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                thread.start();
            });
        }
    }

    private void info(String message) {
        ChatUtils.info("BlockListInfo", message);
    }

    private void error(String message) {
        ChatUtils.error("BlockListError", message);
    }

    public Path getBlocklistPath() {
        return blocklistPath;
    }
}
