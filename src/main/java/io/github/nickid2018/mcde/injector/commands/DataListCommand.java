package io.github.nickid2018.mcde.injector.commands;

import com.mojang.brigadier.CommandDispatcher;
import io.github.nickid2018.mcde.data.DataCollector;
import io.github.nickid2018.mcde.injector.ui.InjectorFrame;
import io.github.nickid2018.mcde.util.I18N;

public class DataListCommand {

    public static void register(CommandDispatcher<InjectorFrame> dispatcher) {
        dispatcher.register(InjectCommander.literal("datalist")
                .executes(InjectCommander.return0(context ->
                        context.getSource().info("%s\n%s".formatted(
                                I18N.getTranslation("injector.command.datalist"),
                                String.join("\n", DataCollector.getKeys())))
                )));
    }
}
