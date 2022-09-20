package io.github.nickid2018.mcde.injector.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.nickid2018.mcde.injector.InjectorFrame;

public class InjectCommander {

    private final CommandDispatcher<InjectorFrame> dispatcher = new CommandDispatcher<>();

    public InjectCommander() {
        ClassStatusCommand.register(dispatcher);
        DecompileClassCommand.register(dispatcher);
    }

    public static LiteralArgumentBuilder<InjectorFrame> literal(String var0) {
        return LiteralArgumentBuilder.literal(var0);
    }

    public static <T> RequiredArgumentBuilder<InjectorFrame, T> argument(String var0, ArgumentType<T> var1) {
        return RequiredArgumentBuilder.argument(var0, var1);
    }

    public void doCommand(String command, InjectorFrame frame) {
        try {
            dispatcher.execute(command, frame);
        } catch (CommandSyntaxException e) {
            frame.error(null, e);
        }
    }
}
