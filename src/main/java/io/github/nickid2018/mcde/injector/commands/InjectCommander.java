package io.github.nickid2018.mcde.injector.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.nickid2018.mcde.injector.ui.InjectorFrame;

import java.util.function.Consumer;

public class InjectCommander {

    //ldc string path
    //aload 0
    //checkcast java/lang/Object
    //ldc long 1000
    //invokestatic io/github/nickid2018/mcde/data/DataCollector.putWithTimeout(Ljava/lang/String;Ljava/lang/Object;J)V false
    //return

    private final CommandDispatcher<InjectorFrame> dispatcher = new CommandDispatcher<>();

    public InjectCommander() {
        ClassStatusCommand.register(dispatcher);
        DecompileClassCommand.register(dispatcher);
        SwapCommand.register(dispatcher);
        DataListCommand.register(dispatcher);
        DataLimitCommand.register(dispatcher);
        DataValueCommand.register(dispatcher);
    }

    public static LiteralArgumentBuilder<InjectorFrame> literal(String var0) {
        return LiteralArgumentBuilder.literal(var0);
    }

    public static <T> RequiredArgumentBuilder<InjectorFrame, T> argument(String var0, ArgumentType<T> var1) {
        return RequiredArgumentBuilder.argument(var0, var1);
    }

    public static Command<InjectorFrame> return0(Consumer<CommandContext<InjectorFrame>> consumer) {
        return context -> {
            consumer.accept(context);
            return 0;
        };
    }

    public void doCommand(String command, InjectorFrame frame) {
        try {
            dispatcher.execute(command, frame);
        } catch (CommandSyntaxException e) {
            frame.error(e.getMessage(), null);
        }
    }
}
