package io.github.nickid2018.mcde.injector.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.github.nickid2018.mcde.data.DataCollector;
import io.github.nickid2018.mcde.injector.ui.InjectorFrame;
import io.github.nickid2018.mcde.util.I18N;

public class DataLimitCommand {

    public static void register(CommandDispatcher<InjectorFrame> dispatcher) {
        dispatcher.register(InjectCommander.literal("datalimit")
                .then(InjectCommander.argument("name", StringArgumentType.string())
                        .then(InjectCommander.argument("limit", IntegerArgumentType.integer())
                                .executes(InjectCommander.return0(context -> {
                                    String name = StringArgumentType.getString(context, "name");
                                    int limit = IntegerArgumentType.getInteger(context, "limit");
                                    DataCollector.setDataThreshold(name, limit);
                                    context.getSource().info(I18N.getTranslation("injector.command.datalimit", name, limit));
                                })))));
    }
}
