package com.github.nickid2018.dynamicex;

import java.security.*;
import org.objectweb.asm.*;
import java.lang.instrument.*;
import com.github.nickid2018.dynamicex.hacks.*;

public class DETransformer implements ClassFileTransformer {

	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		// Hack net.minecraft.commands.Commands
		if (className.equals("net/minecraft/commands/Commands")) {
			ClassReader reader = new ClassReader(classfileBuffer);
			ClassWriter writer = new ClassWriter(0);
			reader.accept(new HackCommandsWriter(writer), 0);
			return writer.toByteArray();
		}
		return classfileBuffer;
	}
}
