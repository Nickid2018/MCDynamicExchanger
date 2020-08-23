package com.github.nickid2018.dynamicex.commands;

import java.net.*;
import java.util.*;
import java.io.File;
import java.util.jar.*;
import java.util.concurrent.*;
import com.mojang.brigadier.*;
import net.minecraft.commands.*;
import net.minecraft.network.chat.*;
import com.mojang.brigadier.context.*;
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.exceptions.*;
import com.mojang.brigadier.suggestion.*;

public class ClassArgumentType implements ArgumentType<String> {

	public static final Set<String> CLASSES_LOADED = new HashSet<>();
	public static final DynamicCommandExceptionType CLASS_UNKNOWN = new DynamicCommandExceptionType(clazz -> {
		return new TextComponent("Unknown Class: " + clazz);
	});

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		return SharedSuggestionProvider.suggest(CLASSES_LOADED, builder);
	}

	@Override
	public String parse(StringReader reader) throws CommandSyntaxException {
		String clazz = reader.readUnquotedString();
		if(!CLASSES_LOADED.contains(clazz))
			throw CLASS_UNKNOWN.create(clazz);
		return clazz;
	}

	public static String getClassName(CommandContext<CommandSourceStack> commandContext, String string) {
		return commandContext.getArgument(string, String.class);
	}

	public static ClassArgumentType classes() {
		return new ClassArgumentType();
	}

	static {
		doFindPackage("com");
		doFindPackage("net");
	}

	private static void doFindPackage(String packageName) {
		try {
			Enumeration<URL> urls = Thread.currentThread().getContextClassLoader()
					.getResources(packageName.replace('.', '/'));
			while (urls.hasMoreElements()) {
				URL url = urls.nextElement();
				String protocol = url.getProtocol();
				if (protocol.equals("file"))
					getClassesFromDir(url.getPath(), packageName);
				else if (protocol.equals("jar")) {
					JarFile jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
					if (jarFile != null)
						getClassesFromJar(jarFile.entries(), packageName);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void getClassesFromDir(String filePath, String packageName) {
		File file = new File(filePath);
		File[] files = file.listFiles();
		for (File child : files) {
			if (child.isDirectory()) {
				getClassesFromDir(child.getPath(), packageName + "." + child.getName());
			} else {
				String fileName = child.getName();
				if (fileName.endsWith(".class"))
					CLASSES_LOADED
							.add(packageName + "." + fileName.substring(0, fileName.length() - 6).replace('$', '.'));
			}
		}
	}

	private static void getClassesFromJar(Enumeration<JarEntry> entries, String packageName) {
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			if (!entry.isDirectory()) {
				String entryName = entry.getName();
				if (entryName.endsWith(".class"))
					CLASSES_LOADED.add(entryName.substring(0, entryName.length() - 6).replace('/', '.'));
			}
		}
	}
}
