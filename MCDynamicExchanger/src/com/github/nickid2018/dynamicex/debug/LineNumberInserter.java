package com.github.nickid2018.dynamicex.debug;

import java.util.*;
import java.util.function.*;
import org.objectweb.asm.*;

public class LineNumberInserter extends ClassVisitor {

	private ClassReader reader;
	private ClassWriter writer;
	private int[] lineNumbers;
	private ToIntFunction<MethodVisitor> function;
	protected int shear;

	public LineNumberInserter(byte[] file, int[] lineNumbers, ToIntFunction<MethodVisitor> insertFunction) {
		super(Opcodes.ASM6);
		Arrays.sort(lineNumbers);
		this.lineNumbers = lineNumbers;
		function = insertFunction;
		reader = new ClassReader(file);
		cv = writer = new ClassWriter(0);
		reader.accept(this, 0);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		return new LineNumberMethodInserter(lineNumbers, writer.visitMethod(access, name, desc, signature, exceptions),
				function, this);
	}

	public byte[] toByteArray() {
		return writer.toByteArray();
	}
}
