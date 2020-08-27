package com.github.nickid2018.dynamicex;

import java.lang.instrument.*;

public class DEProgramInterface {

	public static Instrumentation instrumention;

	public static void premain(String preargs, Instrumentation inst) {
		instrumention = inst;
		inst.addTransformer(new DETransformer());
		System.out.println("Class Hacking Preparation Over.");
	}
}
