package com.github.nickid2018.dynamicex.commands.deo;

import com.mojang.brigadier.*;
import net.minecraft.commands.*;
import net.minecraft.network.chat.*;
import com.mojang.brigadier.context.*;
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.exceptions.*;
import com.github.nickid2018.dynamicex.*;
import com.github.nickid2018.dynamicex.objects.*;

public class DEOLoadObjectCommand {

	public static final DynamicCommandExceptionType LISTEN_COMMAND_ERROR = new DynamicCommandExceptionType(
			(errortype) -> {
				SharedAfterLoadConstants.logger.error("Error in invoking the method!");
				return new TextComponent("Command Internal Error: " + errortype);
			});

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("deo:loadobject")
				.then(Commands.argument("name", StringArgumentType.string()).executes(DEOLoadObjectCommand::doLoad)
						.then(Commands.argument("referenceLoad", BoolArgumentType.bool())
								.executes(DEOLoadObjectCommand::doReferenceLoad))));
	}

	private static int doLoad(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		String name = StringArgumentType.getString(context, "name");
		if (!ObjectsSet.INSTANCE.hasObject(name))
			throw LISTEN_COMMAND_ERROR.create("The object isn't defined in object set!");
		ObjectsSet.INSTANCE.loadToStack(name);
		context.getSource().sendSuccess(new TextComponent("\u00A7APut the object into running stack."), true);
		return 1;
	}

	private static int doReferenceLoad(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		String name = StringArgumentType.getString(context, "name");
		if (!ObjectsSet.INSTANCE.hasObject(name))
			throw LISTEN_COMMAND_ERROR.create("The object isn't defined in object set!");
		if (BoolArgumentType.getBool(context, "referenceLoad"))
			ObjectsSet.INSTANCE.loadReferenceToStack(name);
		else
			ObjectsSet.INSTANCE.loadToStack(name);
		context.getSource().sendSuccess(new TextComponent("\u00A7APut the object into running stack."), true);
		return 1;
	}
}
