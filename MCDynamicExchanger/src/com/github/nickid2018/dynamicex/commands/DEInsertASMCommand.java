package com.github.nickid2018.dynamicex.commands;

import java.io.*;
import java.util.function.*;
import org.objectweb.asm.*;
import java.lang.instrument.*;
import com.mojang.brigadier.*;
import org.apache.commons.io.*;
import net.minecraft.commands.*;
import com.github.nickid2018.util.*;
import net.minecraft.network.chat.*;
import com.mojang.brigadier.context.*;
import com.mojang.brigadier.exceptions.*;
import com.mojang.brigadier.arguments.*;
import com.github.nickid2018.dynamicex.*;
import com.github.nickid2018.dynamicex.debug.*;

public class DEInsertASMCommand {

	public static final Dynamic2CommandExceptionType INSERT_COMMAND_ERROR = new Dynamic2CommandExceptionType(
			(errortype, error) -> {
				DEProgramInterface.logger.error("Error in invoking the method!", (Throwable) error);
				return new TextComponent(
						"Command Internal Error: " + errortype + "(" + ((Throwable) error).getMessage() + ")");
			});

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("de:debuginsert")
				.requires(context -> context.getServer().isSingleplayer() || context.hasPermission(3))
				.then(Commands.argument("className", ClassArgumentType.classes()).then(Commands
						.argument("lineNumber", IntegerArgumentType.integer())
						.then(Commands.argument("isRedirectable", BoolArgumentType.bool())
								.then(Commands.literal("print_out").then(Commands
										.argument("isErrorStream", BoolArgumentType.bool())
										.then(Commands.argument("printString", StringArgumentType.greedyString())
												.executes(DEInsertASMCommand::doInsertPrint))))
								.then(Commands.literal("dump").executes(DEInsertASMCommand::doInsertDump))))));
	}

	private static int doInsertPrint(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		String className = ClassArgumentType.getClassName(context, "className");
		int lineNumber = IntegerArgumentType.getInteger(context, "lineNumber");
		boolean isRedirectable = BoolArgumentType.getBool(context, "isRedirectable");
		boolean isErrorStream = BoolArgumentType.getBool(context, "isErrorStream");
		String printString = StringArgumentType.getString(context, "printString");
		doInvokeReplace(className, isRedirectable, lineNumber, visitor -> {
			visitor.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", isErrorStream ? "err" : "out",
					"Ljava/io/PrintStream;");
			visitor.visitLdcInsn(printString);
			visitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V",
					false);
			return 8;
		});
		context.getSource().sendSuccess(new TextComponent("\u00A7ASucceeded in replacing the class."), true);
		return 1;
	}

	private static int doInsertDump(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		String className = ClassArgumentType.getClassName(context, "className");
		int lineNumber = IntegerArgumentType.getInteger(context, "lineNumber");
		boolean isRedirectable = BoolArgumentType.getBool(context, "isRedirectable");
		doInvokeReplace(className, isRedirectable, lineNumber, visitor -> {
			visitor.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Thread", "dumpStack", "()V", false);
			return 3;
		});
		context.getSource().sendSuccess(new TextComponent("\u00A7ASucceeded in replacing the class."), true);
		return 1;
	}

	private static void doInvokeReplace(String className, boolean isRedirectable, int lineNumber,
			ToIntFunction<MethodVisitor> function) throws CommandSyntaxException {
		try {
			byte[] data = isRedirectable && DynamicClassesHolder.isCovered(className)
					? DynamicClassesHolder.getCoveredClass(className).definition.getDefinitionClassFile()
					: IOUtils.toByteArray(DEMakeASMCommand.class
							.getResourceAsStream("/" + ClassUtils.toInternalName(className) + ".class"));
			LineNumberInserter inserter = new LineNumberInserter(data, new int[] { lineNumber }, function);
			DynamicClassesHolder.exchangeClassData(className, inserter.toByteArray());
		} catch (ClassNotFoundException e) {
			throw INSERT_COMMAND_ERROR.create("CLASS_NOT_FOUND", e);
		} catch (IOException e) {
			throw INSERT_COMMAND_ERROR.create("IO_ERROR", e);
		} catch (UnmodifiableClassException e) {
			throw INSERT_COMMAND_ERROR.create("CLASS_NOT_MODIFIABLE", e);
		} catch (Exception e) {
			throw INSERT_COMMAND_ERROR.create("UNKNOWN_EXCEPTION", e);
		} catch (UnsupportedClassVersionError e) {
			throw INSERT_COMMAND_ERROR.create("UNSUPPORTED_CLASS_VERSION", e);
		} catch (ClassFormatError e) {
			throw INSERT_COMMAND_ERROR.create("CLASS_FORMAT_ERROR", e);
		} catch (NoClassDefFoundError e) {
			throw INSERT_COMMAND_ERROR.create("CLASS_NO_DEFINED", e);
		} catch (ClassCircularityError e) {
			throw INSERT_COMMAND_ERROR.create("CLASS_CIRCULARITY", e);
		} catch (LinkageError e) {
			throw INSERT_COMMAND_ERROR.create("LINKAGE_ERROR", e);
		} catch (Error e) {
			throw INSERT_COMMAND_ERROR.create("UNKNOWN_ERROR", e);
		}
	}
}
