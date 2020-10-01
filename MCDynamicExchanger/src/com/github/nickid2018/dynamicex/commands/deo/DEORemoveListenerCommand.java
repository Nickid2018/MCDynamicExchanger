package com.github.nickid2018.dynamicex.commands.deo;

import com.mojang.brigadier.*;
import net.minecraft.commands.*;
import net.minecraft.network.chat.*;
import com.mojang.brigadier.context.*;
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.exceptions.*;
import com.github.nickid2018.dynamicex.objects.*;

public class DEORemoveListenerCommand {

	public static final DynamicCommandExceptionType REMOVE_COMMAND_ERROR = new DynamicCommandExceptionType(
			name -> new TextComponent("The listener " + name + " can't be removed because it is null!"));

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("deo:removelisten").then(
				Commands.argument("name", StringArgumentType.string()).executes(DEORemoveListenerCommand::remove)));
	}

	private static int remove(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		String name = StringArgumentType.getString(context, "name");
		if(!ObjectInfosHolder.elements.containsKey(name))
			throw REMOVE_COMMAND_ERROR.create(name);
		ObjectInfosHolder.elements.remove(name);
		context.getSource().sendSuccess(new TextComponent("\u00A7ASucceeded in removing listener."), true);
		return 1;
	}
}
