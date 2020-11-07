package com.github.nickid2018.dynamicex.commands.de;

import com.mojang.brigadier.*;
import net.minecraft.commands.*;
import net.minecraft.network.chat.*;
import com.mojang.brigadier.builder.*;

public class DEVersionCommand {

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("de:version").requires(context -> true).executes(context -> {
			context.getSource().sendSuccess(new TextComponent("\u00A7ADynamic Exchanger Version 1.0"), true);
			return 1;
		}));
	}
	
	@SuppressWarnings("unchecked")
	public static void registerClient(CommandDispatcher<SharedSuggestionProvider> dispatcher) {
		@SuppressWarnings("rawtypes")
		LiteralArgumentBuilder builder = Commands.literal("de:version").executes(context -> {
			return 0;
		});
		dispatcher.register(builder);
	}
}
