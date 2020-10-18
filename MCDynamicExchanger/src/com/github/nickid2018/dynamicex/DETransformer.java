package com.github.nickid2018.dynamicex;

import java.util.*;
import java.security.*;
import org.objectweb.asm.*;
import java.lang.instrument.*;

public class DETransformer implements ClassFileTransformer {

	public static final Map<String, String> transforms = new HashMap<>();
	public static final Map<String, String> sourceName = new HashMap<>();

	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		if (transforms.containsKey(className)) {
			ClassReader reader = new ClassReader(classfileBuffer);
			ClassWriter writer = new ClassWriter(0);
			try {
				reader.accept((ClassVisitor) Class.forName(transforms.get(className)).getConstructor(ClassWriter.class)
						.newInstance(writer), 0);
				System.out.println("Hacked " + (ClassNameTransformer.isRemapped() ? className
						: (className + " (Source Name: " + sourceName.get(className) + ")")));
				return ClassNameTransformer.isRemapped() ? writer.toByteArray()
						: ClassNameTransformer.changeClassData(writer.toByteArray());
			} catch (Throwable e) {
				System.out.println("Error in hacking " + className + ": ");
				e.printStackTrace();
			}
		}
		if (!ClassNameTransformer.isRemapped() && className.startsWith("com/github/nickid2018")) {
			System.out.println("Redefined " + className);
			return ClassNameTransformer.changeClassData(classfileBuffer);
		}
		return classfileBuffer;
	}

	static {
		try {
			addTransform("net/minecraft/commands/Commands", "HackCommandsWriter");
			addTransform("net/minecraft/CrashReport", "HackCrashReportWriter");
			addTransform("net/minecraft/client/gui/Gui", "HackGuiWriter");
			addTransform("com/github/nickid2018/dynamicex/gui/RenderInterface", "HackRenderInterfaceWriter");
			addTransform("net/minecraft/client/gui/screens/TitleScreen", "HackTitleScreenWriter");
		} catch (Exception e) {
			System.out.println("Error in initializing transform list!");
			e.printStackTrace();
		}
	}

	private static void addTransform(String className, String clazz) {
		transforms.put(ClassNameTransformer.getResourceName(className),
				"com.github.nickid2018.dynamicex.hacks." + clazz);
		if (!ClassNameTransformer.isRemapped())
			sourceName.put(ClassNameTransformer.getResourceName(className), className);
	}
}
