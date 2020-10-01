package com.github.nickid2018.dynamicex.commands.deo;

import com.mojang.brigadier.*;
import net.minecraft.commands.*;
import net.minecraft.network.chat.*;
import com.mojang.brigadier.context.*;
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.exceptions.*;
import com.github.nickid2018.dynamicex.*;
import com.github.nickid2018.dynamicex.objects.*;

public class DEOAddListeningFieldCommand {

	public static final Dynamic2CommandExceptionType LISTEN_COMMAND_ERROR = new Dynamic2CommandExceptionType(
			(errortype, error) -> {
				SharedAfterLoadConstants.logger.error("Error in invoking the method!", (Throwable) error);
				return new TextComponent(
						"Command Internal Error: " + errortype + "(" + ((Throwable) error).getMessage() + ")");
			});

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("deo:addfield")
				.then(Commands.argument("name", StringArgumentType.string()).then(Commands
						.argument("field", StringArgumentType.string()).executes(DEOAddListeningFieldCommand::doAdd))));
	}

	private static int doAdd(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		ObjectElement element = ObjectInfosHolder.elements.get(StringArgumentType.getString(context, "name"));
		if (element == null || !(element instanceof ObjectOnlyElement)) {
			throw LISTEN_COMMAND_ERROR.create("ILLEGAL_ARGS",
					new Exception("This listener is not a object-only listener!"));
		}
		ObjectOnlyElement e = (ObjectOnlyElement) element;
		try {
			e.putNewField(StringArgumentType.getString(context, "field"));
		} catch (Throwable e1) {
			throw LISTEN_COMMAND_ERROR.create("ADD_FIELD_ERROR", e1);
		}
		context.getSource().sendSuccess(new TextComponent("\u00A7ASucceeded in adding field."), true);
		return 1;
	}
}
