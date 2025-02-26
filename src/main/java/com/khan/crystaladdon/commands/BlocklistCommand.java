package com.khan.crystaladdon.commands;

import com.khan.crystaladdon.modules.iTristanGreet;
import meteordevelopment.meteorclient.commands.Command;
import meteordevelopment.meteorclient.systems.modules.Modules;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;

import static net.minecraft.server.command.CommandManager.literal;

public class BlocklistCommand extends Command {
    public BlocklistCommand() {
        super("blocklist", "Opens or reloads the blocklist file");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            info("Usage: /blocklist <open|reload>");
            return SINGLE_SUCCESS;
        });

        builder.then(literal("open")
            .executes(context -> {
                iTristanGreet module = Modules.get().get(iTristanGreet.class);
                if (module == null) {
                    error("iTristanGreet module not found!");
                    return 0;
                }
                module.openBlocklist();
                info("Opened blocklist file.");
                return SINGLE_SUCCESS;
            })
        );

        builder.then(literal("reload")
            .executes(context -> {
                iTristanGreet module = Modules.get().get(iTristanGreet.class);
                if (module == null) {
                    error("iTristanGreet module not found!");
                    return 0;
                }
                module.loadBlocklist();
                info("Reloaded blocklist.");
                return SINGLE_SUCCESS;
            })
        );
    }
}
