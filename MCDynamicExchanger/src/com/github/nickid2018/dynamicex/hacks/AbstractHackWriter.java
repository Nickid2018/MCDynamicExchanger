package com.github.nickid2018.dynamicex.hacks;

import org.objectweb.asm.*;

public abstract class AbstractHackWriter extends ClassVisitor {

	protected final ClassWriter writer;

	public AbstractHackWriter(ClassWriter writer) {
		super(Opcodes.ASM6, writer);
		this.writer = writer;
	}

	public static void loadAccess() {
	}
}
