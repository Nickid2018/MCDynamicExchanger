package io.github.nickid2018.mcde.injector.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.github.nickid2018.mcde.injector.ui.InjectorFrame;

public class DataPlotCommand {

    public static void register(CommandDispatcher<InjectorFrame> dispatcher) {
        dispatcher.register(InjectCommander.literal("dataplot")
                .then(InjectCommander.argument("name", StringArgumentType.string())
                        .executes(InjectCommander.return0(context -> {
                            String name = StringArgumentType.getString(context, "name");

                        }))));
    }
}
