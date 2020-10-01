package com.github.nickid2018.dynamicex.commands.deo;

import com.mojang.brigadier.*;
import net.minecraft.commands.*;
import net.minecraft.network.chat.*;
import com.mojang.brigadier.context.*;
import com.github.nickid2018.dynamicex.objects.*;

public class DEOObjectsFlushCommand {

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("deo:flush").executes(DEOObjectsFlushCommand::doFlush));
	}

	private static int doFlush(CommandContext<CommandSourceStack> context) {
		ObjectsSet.INSTANCE.flushBase();
		context.getSource().sendSuccess(new TextComponent("\u00A7ASucceeded in flushing."), true);
		return 1;
	}
}
