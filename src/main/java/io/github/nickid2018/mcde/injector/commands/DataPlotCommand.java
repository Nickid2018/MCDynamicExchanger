package io.github.nickid2018.mcde.injector.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.github.nickid2018.mcde.injector.ui.DataPlotFrame;
import io.github.nickid2018.mcde.injector.ui.InjectorFrame;

public class DataPlotCommand {

    public static void register(CommandDispatcher<InjectorFrame> dispatcher) {
        dispatcher.register(InjectCommander.literal("dataplot")
                .then(InjectCommander.argument("names", StringArgumentType.string())
                        .executes(InjectCommander.return0(context -> {
                            String names = StringArgumentType.getString(context, "names");
                            String[] split = names.split(",");
                            new DataPlotFrame(split);
                        }))));
    }
}
