package com.github.nickid2018.mcremap;

import org.objectweb.asm.*;

public class RemoveModCheck extends ClassVisitor {

	private ClassWriter writer;

	public RemoveModCheck(ClassWriter writer) {
		super(Opcodes.ASM6, writer);
		this.writer = writer;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		return new HackMethod(writer.visitMethod(access, name, desc, signature, exceptions),
				name.equals("isProbablyModded"));
	}

	public class HackMethod extends MethodVisitor {

		public HackMethod(MethodVisitor defaultVisitor, boolean is) {
			super(Opcodes.ASM6, is ? null : defaultVisitor);
			if (is) {
				defaultVisitor.visitInsn(Opcodes.ICONST_0);
				defaultVisitor.visitInsn(Opcodes.IRETURN);
				defaultVisitor.visitMaxs(1, 1);
				defaultVisitor.visitEnd();
			}
		}
	}
}
