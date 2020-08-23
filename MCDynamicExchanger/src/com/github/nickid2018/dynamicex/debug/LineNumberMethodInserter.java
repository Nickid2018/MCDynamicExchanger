package com.github.nickid2018.dynamicex.debug;

import java.util.*;
import java.util.function.*;
import org.objectweb.asm.*;

public class LineNumberMethodInserter extends MethodVisitor {

	private MethodVisitor visitor;
	private int[] lineNumbers;
	private ToIntFunction<MethodVisitor> function;
	private LineNumberInserter insert;

	public LineNumberMethodInserter(int[] lineNumbers, MethodVisitor visitor, ToIntFunction<MethodVisitor> function,
			LineNumberInserter insert) {
		super(Opcodes.ASM6, visitor);
		this.visitor = visitor;
		this.lineNumbers = lineNumbers;
		this.function = function;
		this.insert = insert;
	}

	@Override
	public void visitLineNumber(int line, Label start) {
		if (Arrays.binarySearch(lineNumbers, line) > -1) {
			visitor.visitLineNumber(insert.shear + line, start);
			insert.shear += function.applyAsInt(visitor);
		}
		visitor.visitLineNumber(insert.shear + line, start);
	}
}
