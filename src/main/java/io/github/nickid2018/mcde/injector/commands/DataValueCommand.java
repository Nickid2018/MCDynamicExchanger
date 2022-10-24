package io.github.nickid2018.mcde.injector.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.github.nickid2018.mcde.injector.MCProgramInjector;
import io.github.nickid2018.mcde.injector.ui.DataValueFrame;
import io.github.nickid2018.mcde.injector.ui.InjectorFrame;

import java.util.HashMap;
import java.util.Map;

public class DataValueCommand {

    private static final Map<String, DataValueFrame> FRAMES = new HashMap<>();

    public static void register(CommandDispatcher<InjectorFrame> dispatcher) {
        dispatcher.register(InjectCommander.literal("datavalue")
                .then(InjectCommander.argument("name", StringArgumentType.string())
                        .executes(InjectCommander.return0(context -> {
                            String name = StringArgumentType.getString(context, "name");
                            DataValueFrame frame = FRAMES.get(name);
                            if (frame == null || !frame.isShowing()) {
                                frame = new DataValueFrame(name, MCProgramInjector.format.getToNamedMapper());
                                FRAMES.put(name, frame);
                            } else
                                frame.focus();
                        }))));
    }
}
