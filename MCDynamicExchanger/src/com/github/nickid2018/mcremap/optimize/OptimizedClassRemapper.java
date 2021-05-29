package com.github.nickid2018.mcremap.optimize;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.*;

public class OptimizedClassRemapper extends ClassRemapper {

	public OptimizedClassRemapper(ClassVisitor classVisitor, Remapper remapper) {
		super(classVisitor, remapper);
	}

	@Override
	protected MethodVisitor createMethodRemapper(MethodVisitor methodVisitor) {
		return new OptimizedMethodRemapper(methodVisitor, remapper);
	}
}
