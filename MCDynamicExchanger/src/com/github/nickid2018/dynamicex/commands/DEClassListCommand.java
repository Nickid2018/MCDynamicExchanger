package com.github.nickid2018.dynamicex.commands;

import java.util.*;
import java.util.regex.*;
import com.mojang.brigadier.*;
import net.minecraft.commands.*;
import net.minecraft.network.chat.*;
import com.mojang.brigadier.context.*;
import com.mojang.brigadier.exceptions.*;
import com.mojang.brigadier.arguments.*;
import com.github.nickid2018.dynamicex.*;

public class DEClassListCommand {

	public static final SimpleCommandExceptionType LIST_REGEX_ERROR = new SimpleCommandExceptionType(
			new TextComponent("Regex Error"));

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("de:list").requires(context -> true)
				.executes(DEClassListCommand::printInName)
				.then(Commands.literal("names").executes(DEClassListCommand::printInName))
				.then(Commands.literal("details").executes(context -> printInDetails(context, ".*"))
						.then(Commands.argument("regex", StringArgumentType.greedyString()).executes(
								context -> printInDetails(context, StringArgumentType.getString(context, "regex"))))));
	}

	private static int printInName(CommandContext<CommandSourceStack> context) {
		context.getSource().sendSuccess(new TextComponent("\u00A76Classes Redefined:"), true);
		Set<String> clazz = DynamicClassesHolder.listInNames();
		for (String name : clazz) {
			context.getSource().sendSuccess(new TextComponent("\u00A7A" + name), true);
		}
		return clazz.size() + 1;
	}

	private static int printInDetails(CommandContext<CommandSourceStack> context, String regex)
			throws CommandSyntaxException {
		context.getSource().sendSuccess(new TextComponent("\u00A76Classes Redefined:"), true);
		Map<String, DynamicClass> classes = DynamicClassesHolder.list();
		int all = 1;
		for (Map.Entry<String, DynamicClass> entry : classes.entrySet()) {
			String name = entry.getKey();
			try {
				if (name.matches(regex)) {
					DynamicClass clazz = entry.getValue();
					context.getSource().sendSuccess(new TextComponent(name + "(File " + clazz.filePath + ", Length "
							+ clazz.definition.getDefinitionClassFile().length + ")"), true);
					all++;
				}
			} catch (PatternSyntaxException e) {
				throw LIST_REGEX_ERROR.create();
			}
		}
		return all;
	}
}
