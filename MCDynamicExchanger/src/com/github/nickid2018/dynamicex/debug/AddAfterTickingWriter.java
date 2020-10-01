package com.github.nickid2018.dynamicex.debug;

import java.util.function.*;
import org.objectweb.asm.*;

public class AddAfterTickingWriter extends ClassVisitor {

	private ClassReader reader;
	private ClassWriter writer;
	private Consumer<MethodVisitor> consumer;

	public AddAfterTickingWriter(byte[] file, Consumer<MethodVisitor> consumer) {
		super(Opcodes.ASM6);
		this.consumer = consumer;
		reader = new ClassReader(file);
		cv = writer = new ClassWriter(0);
		reader.accept(this, 0);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		return name.equals("tick") ? new HackMethod(super.visitMethod(access, name, desc, signature, exceptions))
				: super.visitMethod(access, name, desc, signature, exceptions);
	}

	public byte[] toByteArray() {
		return writer.toByteArray();
	}

	public class HackMethod extends MethodVisitor {

		public HackMethod(MethodVisitor defaultVisitor) {
			super(Opcodes.ASM6, defaultVisitor);
			consumer.accept(mv);
		}
	}
}
