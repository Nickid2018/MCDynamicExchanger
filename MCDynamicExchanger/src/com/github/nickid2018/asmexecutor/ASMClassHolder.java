package com.github.nickid2018.asmexecutor;

import java.util.*;

public final class ASMClassHolder {

	private static final Set<ASMClass> classes = new TreeSet<>();

	public static final void addClass(ASMClass clazz) {
		classes.add(clazz);
	}
	
	public static final void deleteClass(ASMClass clazz) {
		classes.remove(clazz);
	}
}
