package com.github.nickid2018.dynamicex.commands;

import java.io.*;
import java.lang.instrument.*;
import com.mojang.brigadier.*;
import net.minecraft.commands.*;
import com.github.nickid2018.util.*;
import net.minecraft.network.chat.*;
import com.mojang.brigadier.context.*;
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.exceptions.*;
import com.github.nickid2018.dynamicex.*;

public class DEClassReplaceCommand {

	public static final Dynamic2CommandExceptionType REPLACE_ERROR = new Dynamic2CommandExceptionType(
			(errortype, error) -> {
				SharedAfterLoadConstants.logger.error("Error in invoking the method!", (Throwable) error);
				return new TextComponent(
						"Command Internal Error: " + errortype + "(" + ((Throwable) error).getMessage() + ")");
			});

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("de:replace")
				.requires(context -> context.getServer().isSingleplayer() || context.hasPermission(3))
				.then(Commands.argument("className", ClassArgumentType.classes())
						.then(Commands.literal("file")
								.then(Commands.argument("filePath", StringArgumentType.greedyString())
										.executes(context -> doReplace(context, DynamicClassesHolder::exchangeClass))))
						.then(Commands.literal("url")
								.then(Commands.argument("filePath", StringArgumentType.greedyString()).executes(
										context -> doReplace(context, DynamicClassesHolder::exchangeClassURL))))));
	}

	private static int doReplace(CommandContext<CommandSourceStack> context, BiExConsumer<String, String> command)
			throws CommandSyntaxException {
		try {
			command.accept(ClassArgumentType.getClassName(context, "className"),
					StringArgumentType.getString(context, "filePath"));
		} catch (ClassNotFoundException e) {
			throw REPLACE_ERROR.create("CLASS_NOT_FOUND", e);
		} catch (IOException e) {
			throw REPLACE_ERROR.create("IO_ERROR", e);
		} catch (UnmodifiableClassException e) {
			throw REPLACE_ERROR.create("CLASS_NOT_MODIFIABLE", e);
		} catch (Exception e) {
			throw REPLACE_ERROR.create("UNKNOWN_EXCEPTION", e);
		} catch (UnsupportedClassVersionError e) {
			throw REPLACE_ERROR.create("UNSUPPORTED_CLASS_VERSION", e);
		} catch (ClassFormatError e) {
			throw REPLACE_ERROR.create("CLASS_FORMAT_ERROR", e);
		} catch (NoClassDefFoundError e) {
			throw REPLACE_ERROR.create("CLASS_NO_DEFINED", e);
		} catch (ClassCircularityError e) {
			throw REPLACE_ERROR.create("CLASS_CIRCULARITY", e);
		} catch (LinkageError e) {
			throw REPLACE_ERROR.create("LINKAGE_ERROR", e);
		} catch (Error e) {
			throw REPLACE_ERROR.create("UNKNOWN_ERROR", e);
		}
		context.getSource().sendSuccess(new TextComponent("\u00A7ASucceeded in replacing the class."), true);
		return 1;
	}
}
