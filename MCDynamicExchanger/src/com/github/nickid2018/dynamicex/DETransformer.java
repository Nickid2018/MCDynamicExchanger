package com.github.nickid2018.dynamicex;

import java.util.*;
import java.security.*;
import org.objectweb.asm.*;
import java.lang.instrument.*;
import com.github.nickid2018.dynamicex.hacks.*;

public class DETransformer implements ClassFileTransformer {

	public static final Map<String, Class<? extends AbstractHackWriter>> transforms = new HashMap<>();

	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		if (transforms.containsKey(className)) {
			ClassReader reader = new ClassReader(classfileBuffer);
			ClassWriter writer = new ClassWriter(0);
			try {
				reader.accept(transforms.get(className).getConstructor(ClassWriter.class).newInstance(writer), 0);
				System.out.println("Hacked " + className);
				return writer.toByteArray();
			} catch (Exception e) {
				System.out.println("Error in hacking " + className + ": ");
				e.printStackTrace();
			}
		}
		return classfileBuffer;
	}

	static {
		try {
			transforms.put("net/minecraft/commands/Commands", HackCommandsWriter.class);
			transforms.put("net/minecraft/CrashReport", HackCrashReportWriter.class);
			transforms.put("net/minecraft/client/gui/Gui", HackGuiWriter.class);
			transforms.put("com/github/nickid2018/dynamicex/gui/RenderInterface", HackRenderInterfaceWriter.class);
		} catch (Exception e) {
			System.out.println("Error in initializing transform list!");
			e.printStackTrace();
		}
	}
}
