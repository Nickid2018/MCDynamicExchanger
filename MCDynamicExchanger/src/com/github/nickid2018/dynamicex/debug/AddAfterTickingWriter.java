package com.github.nickid2018.dynamicex.debug;

import java.util.function.*;
import org.objectweb.asm.*;
import com.github.nickid2018.dynamicex.*;
import com.github.nickid2018.dynamicex.objects.*;

public class AddAfterTickingWriter extends ClassVisitor {

	private ClassReader reader;
	private ClassWriter writer;
	private Consumer<MethodVisitor> consumer;
	private String methodName;

	public AddAfterTickingWriter(byte[] file, Consumer<MethodVisitor> consumer, String className) {
		super(Opcodes.ASM6);
		this.consumer = consumer;
		reader = new ClassReader(file);
		cv = writer = new ClassWriter(0);
		String nowClass = ObjectsSet.INSTANCE.getSourceClassName(className);
		methodName = ClassNameTransformer.getMethodName(nowClass, "tick()V");
		reader.accept(this, 0);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		return name.equals(methodName) && desc.equals("()V")
				? new HackMethod(super.visitMethod(access, name, desc, signature, exceptions))
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
