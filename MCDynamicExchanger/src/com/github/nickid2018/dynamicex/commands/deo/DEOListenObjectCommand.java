package com.github.nickid2018.dynamicex.commands.deo;

import java.io.*;
import java.lang.instrument.*;
import com.mojang.brigadier.*;
import net.minecraft.commands.*;
import net.minecraft.network.chat.*;
import com.mojang.brigadier.context.*;
import com.mojang.brigadier.exceptions.*;
import com.mojang.brigadier.arguments.*;
import com.github.nickid2018.dynamicex.*;
import com.github.nickid2018.dynamicex.objects.*;

public class DEOListenObjectCommand {

	public static final Dynamic2CommandExceptionType LISTEN_COMMAND_ERROR = new Dynamic2CommandExceptionType(
			(errortype, error) -> {
				SharedAfterLoadConstants.logger.error("Error in invoking the method!", (Throwable) error);
				return new TextComponent(
						"Command Internal Error: " + errortype + "(" + ((Throwable) error).getMessage() + ")");
			});

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("deo:listenobject")
				.then(Commands.argument("name", StringArgumentType.string()).executes(DEOListenObjectCommand::doListen)
						.then(Commands.argument("x", IntegerArgumentType.integer())
								.then(Commands.argument("y", IntegerArgumentType.integer())
										.executes(DEOListenObjectCommand::doListenWithPos)))));
	}

	private static int doListen(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		String name = StringArgumentType.getString(context, "name");
		listen(name, 0, 0);
		context.getSource().sendSuccess(new TextComponent("\u00A7ASucceeded in adding listener."), true);
		return 1;
	}

	private static int doListenWithPos(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		String name = StringArgumentType.getString(context, "name");
		int x = IntegerArgumentType.getInteger(context, "x");
		int y = IntegerArgumentType.getInteger(context, "y");
		listen(name, x, y);
		context.getSource().sendSuccess(new TextComponent("\u00A7ASucceeded in adding listener."), true);
		return 1;
	}

	private static void listen(String name, int x, int y) throws CommandSyntaxException {
		try {
			ListeningClassHolder.addGlobalTickListen(name, x, y);
		} catch (IllegalArgumentException e) {
			throw LISTEN_COMMAND_ERROR.create("ILLEGAL_ARGS", e);
		} catch (ClassNotFoundException e) {
			throw LISTEN_COMMAND_ERROR.create("CLASS_NOT_FOUND", e);
		} catch (IOException e) {
			throw LISTEN_COMMAND_ERROR.create("IO_ERROR", e);
		} catch (UnmodifiableClassException e) {
			throw LISTEN_COMMAND_ERROR.create("CLASS_NOT_MODIFIABLE", e);
		} catch (Exception e) {
			throw LISTEN_COMMAND_ERROR.create("UNKNOWN_EXCEPTION", e);
		} catch (UnsupportedClassVersionError e) {
			throw LISTEN_COMMAND_ERROR.create("UNSUPPORTED_CLASS_VERSION", e);
		} catch (ClassFormatError e) {
			throw LISTEN_COMMAND_ERROR.create("CLASS_FORMAT_ERROR", e);
		} catch (NoClassDefFoundError e) {
			throw LISTEN_COMMAND_ERROR.create("CLASS_NO_DEFINED", e);
		} catch (ClassCircularityError e) {
			throw LISTEN_COMMAND_ERROR.create("CLASS_CIRCULARITY", e);
		} catch (LinkageError e) {
			throw LISTEN_COMMAND_ERROR.create("LINKAGE_ERROR", e);
		} catch (Error e) {
			throw LISTEN_COMMAND_ERROR.create("UNKNOWN_ERROR", e);
		}
	}
}
