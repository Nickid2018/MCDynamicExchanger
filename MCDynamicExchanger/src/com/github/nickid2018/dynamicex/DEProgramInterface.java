package com.github.nickid2018.dynamicex;

import java.lang.instrument.*;
import com.github.nickid2018.util.*;
import com.github.nickid2018.dynamicex.hacks.*;

public class DEProgramInterface {

	public static Instrumentation instrumention;

	public static void premain(String preargs, Instrumentation inst) {
		instrumention = inst;
		AddClassPath.addClassPathInDirs("dynamicexchanger/libs");
		if (!ClassUtils.isClassExists("org.objectweb.asm.Opcodes")) {
			DownloadUtils.downloadResource(
					"https://repository.ow2.org/nexus/content/repositories/releases/org/ow2/asm/asm-all/6.0_BETA/asm-all-6.0_BETA.jar",
					"dynamicexchanger/libs/asm-all-6.0_BETA.jar");
			AddClassPath.addClassPathInDirs("dynamicexchanger/libs");
		}
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
