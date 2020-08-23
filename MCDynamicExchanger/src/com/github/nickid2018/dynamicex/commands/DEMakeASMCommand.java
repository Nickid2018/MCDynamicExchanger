package com.github.nickid2018.dynamicex.commands;

import java.io.*;
import com.mojang.brigadier.*;
import org.apache.commons.io.*;
import net.minecraft.commands.*;
import com.github.nickid2018.util.*;
import net.minecraft.network.chat.*;
import com.mojang.brigadier.context.*;
import com.mojang.brigadier.exceptions.*;
import com.mojang.brigadier.arguments.*;
import com.github.nickid2018.dynamicex.*;
import com.github.nickid2018.asmexecutor.gen.*;

public class DEMakeASMCommand {

	public static final Dynamic2CommandExceptionType ASM_MAKING_ERROR = new Dynamic2CommandExceptionType(
			(errortype, error) -> {
				DEProgramInterface.logger.error("Error in invoking the method!", (Throwable) error);
				return new TextComponent(
						"Command Internal Error: " + errortype + "(" + ((Throwable) error).getMessage() + ")");
			});

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("de:makeasm").requires(context -> true)
				.then(Commands.argument("redirectable", BoolArgumentType.bool())
						.then(Commands.argument("className", ClassArgumentType.classes())
								.then(Commands.argument("filePath", StringArgumentType.greedyString())
										.executes(DEMakeASMCommand::doMakeASM)))));
	}

	private static int doMakeASM(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		boolean isRedirectable = BoolArgumentType.getBool(context, "redirectable");
		String className = ClassArgumentType.getClassName(context, "className");
		try {
			byte[] data = isRedirectable && DynamicClassesHolder.isCovered(className)
					? DynamicClassesHolder.getCoveredClass(className).definition.getDefinitionClassFile()
					: IOUtils.toByteArray(DEMakeASMCommand.class
							.getResourceAsStream("/" + ClassUtils.toInternalName(className) + ".class"));
			String value = new ClassTransformer(data).executeInString();
			File file = new File(StringArgumentType.getString(context, "filePath"));
			if (!file.exists())
				file.createNewFile();
			FileWriter writer = new FileWriter(file);
			writer.write(value);
			writer.close();
		} catch (IOException e) {
			throw ASM_MAKING_ERROR.create("IO_ERROR", e);
		} catch (Exception e) {
			throw ASM_MAKING_ERROR.create("UNKNOWN_ERROR", e);
		}
		context.getSource().sendSuccess(new TextComponent("\u00A7ASucceeded in exporting the class."), true);
		return 1;
	}
}
