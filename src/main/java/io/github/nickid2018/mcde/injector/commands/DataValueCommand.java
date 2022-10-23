package io.github.nickid2018.mcde.injector.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.github.nickid2018.mcde.injector.MCProgramInjector;
import io.github.nickid2018.mcde.injector.ui.DataValueFrame;
import io.github.nickid2018.mcde.injector.ui.InjectorFrame;

public class DataValueCommand {

    public static void register(CommandDispatcher<InjectorFrame> dispatcher) {
        dispatcher.register(InjectCommander.literal("datavalue")
                .then(InjectCommander.argument("name", StringArgumentType.string())
                        .executes(InjectCommander.return0(context ->
                                new DataValueFrame(StringArgumentType.getString(context, "name"),
                                        MCProgramInjector.format.getToNamedMapper())))));
    }
}
