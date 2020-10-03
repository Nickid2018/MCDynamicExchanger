package com.github.nickid2018.dynamicex;

import java.lang.instrument.*;
import com.github.nickid2018.dynamicex.hacks.*;

public class DEProgramInterface {

	public static Instrumentation instrumention;

	public static void premain(String preargs, Instrumentation inst) {
		instrumention = inst;
		inst.addTransformer(new DETransformer());
		System.out.println("Class Hacking Preparation Over.");
		HackCommandsWriter.loadAccess();
		HackCrashReportWriter.loadAccess();
		HackGuiWriter.loadAccess();
		HackRenderInterfaceWriter.loadAccess();
		try {
			Class.forName("com.github.nickid2018.dynamicex.SharedAfterLoadConstants", false,
					Thread.currentThread().getContextClassLoader());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
