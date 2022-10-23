package io.github.nickid2018.mcde.injector.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.github.nickid2018.mcde.injector.ClassDataRepository;
import io.github.nickid2018.mcde.injector.ui.InjectorFrame;
import io.github.nickid2018.mcde.injector.MCProgramInjector;
import io.github.nickid2018.mcde.util.ClassUtils;
import io.github.nickid2018.mcde.util.I18N;

public class ClassStatusCommand {

    public static void register(CommandDispatcher<InjectorFrame> dispatcher) {
        dispatcher.register(InjectCommander.literal("classstatus")
                .then(InjectCommander.argument("class", StringArgumentType.greedyString())
                        .executes(InjectCommander.return0(context -> {
                            String className = StringArgumentType.getString(context, "class");
                            String remappedName = ClassUtils.toBinaryName(MCProgramInjector.format.getToSourceMapper().map(className));
                            if (ClassDataRepository.getInstance().isLoaded(remappedName))
                                context.getSource().info(I18N.getTranslation(
                                        "injector.command.classstatus.loaded", className, remappedName));
                            else
                                context.getSource().info(I18N.getTranslation(
                                        "injector.command.classstatus.notloaded", className, remappedName));
                        }))));
    }
}
