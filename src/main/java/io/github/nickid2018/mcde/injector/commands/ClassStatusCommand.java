package io.github.nickid2018.mcde.injector.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.github.nickid2018.mcde.injector.InjectorFrame;
import io.github.nickid2018.mcde.injector.MCProgramInjector;
import io.github.nickid2018.mcde.util.ClassUtils;
import io.github.nickid2018.mcde.util.I18N;

public class ClassStatusCommand {

    public static void register(CommandDispatcher<InjectorFrame> dispatcher) {
        dispatcher.register(InjectCommander.literal("classstatus")
                .then(InjectCommander.argument("class", StringArgumentType.greedyString())
                        .executes(context -> {
                            String className = StringArgumentType.getString(context, "class");
                            String remappedName = ClassUtils.toBinaryName(MCProgramInjector.format.getToSourceMapper().map(className));
                            for (Class<?> data : MCProgramInjector.instrumentation.getAllLoadedClasses())
                                if (data.getTypeName().equals(remappedName)) {
                                    context.getSource().info(I18N.getTranslation(
                                            "injector.command.classstatus.loaded", className, remappedName));
                                    return 1;
                                }
                            context.getSource().info(I18N.getTranslation(
                                    "injector.command.classstatus.notloaded", className, remappedName));
                            return 0;
                        })));
    }
}
