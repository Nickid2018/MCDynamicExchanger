package com.github.nickid2018.dynamicex.commands.de;

import com.mojang.brigadier.*;
import net.minecraft.commands.*;
import com.mojang.brigadier.builder.*;
import com.github.nickid2018.dynamicex.commands.*;

public class DESideCommand {

	@SuppressWarnings("unchecked")
	public static void registerClient(CommandDispatcher<SharedSuggestionProvider> dispatcher) {
		@SuppressWarnings("rawtypes")
		LiteralArgumentBuilder builder = Commands.literal("de:side")
				.then(Commands.literal("client").executes(context -> {
					RunningEnvironment.now = RunningEnvironment.CLIENT;
					System.out.println("~");
					return 1;
				})).then(Commands.literal("server").executes(context -> {
					RunningEnvironment.now = RunningEnvironment.SERVER;
					System.out.println("~");
					return 1;
				}));
		dispatcher.register(builder);
	}
}
