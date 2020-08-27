package com.github.nickid2018.dynamicex.commands;

import java.io.*;
import java.lang.instrument.*;
import com.mojang.brigadier.*;
import net.minecraft.commands.*;
import net.minecraft.network.chat.*;
import com.mojang.brigadier.context.*;
import com.mojang.brigadier.exceptions.*;
import com.github.nickid2018.dynamicex.*;

public class DERecoverCommand {

	public static final Dynamic2CommandExceptionType RECOVER_ERROR = new Dynamic2CommandExceptionType(
			(errortype, error) -> {
				SharedAfterLoadConstants.logger.error("Error in invoking the method!", (Throwable) error);
				return new TextComponent(
						"Command Internal Error: " + errortype + "(" + ((Throwable) error).getMessage() + ")");
			});

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("de:recover")
				.requires(context -> context.getServer().isSingleplayer() || context.hasPermission(3)).then(Commands
						.argument("className", ClassArgumentType.classes()).executes(DERecoverCommand::doRecover)));
	}

	private static int doRecover(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		try {
			DynamicClassesHolder.recoverFile(ClassArgumentType.getClassName(context, "className"));
		} catch (IllegalArgumentException e) {
			throw RECOVER_ERROR.create("ILLEGAL_ARGS", e);
		} catch (ClassNotFoundException e) {
			throw RECOVER_ERROR.create("CLASS_NOT_FOUND", e);
		} catch (IOException e) {
			throw RECOVER_ERROR.create("IO_ERROR", e);
		} catch (UnmodifiableClassException e) {
			throw RECOVER_ERROR.create("CLASS_NOT_MODIFIABLE", e);
		} catch (Exception e) {
			throw RECOVER_ERROR.create("UNKNOWN_EXCEPTION", e);
		} catch (UnsupportedClassVersionError e) {
			throw RECOVER_ERROR.create("UNSUPPORTED_CLASS_VERSION", e);
		} catch (ClassFormatError e) {
			throw RECOVER_ERROR.create("CLASS_FORMAT_ERROR", e);
		} catch (NoClassDefFoundError e) {
			throw RECOVER_ERROR.create("CLASS_NO_DEFINED", e);
		} catch (ClassCircularityError e) {
			throw RECOVER_ERROR.create("CLASS_CIRCULARITY", e);
		} catch (LinkageError e) {
			throw RECOVER_ERROR.create("LINKAGE_ERROR", e);
		} catch (Error e) {
			throw RECOVER_ERROR.create("UNKNOWN_ERROR", e);
		}
		context.getSource().sendSuccess(new TextComponent("\u00A7ASucceeded in recovering the class."), true);
		return 1;
	}
}
