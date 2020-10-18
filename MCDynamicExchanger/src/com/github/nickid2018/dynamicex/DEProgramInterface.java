package com.github.nickid2018.dynamicex;

import java.io.*;
import java.lang.instrument.*;
import com.github.nickid2018.util.*;
import com.github.nickid2018.dynamicex.hacks.*;

public class DEProgramInterface {

	public static Instrumentation instrumention;

	public static void premain(String preargs, Instrumentation inst) {
		instrumention = inst;
		checkDir("dynamicexchanger/libs");
		checkDir("dynamicexchanger/mappings");
		AddClassPath.addClassPathInDirs("dynamicexchanger/libs");
		if (!ClassUtils.isClassExists("org.objectweb.asm.Opcodes")) {
			if (DownloadUtils.downloadResource(
					"https://repository.ow2.org/nexus/content/repositories/releases/org/ow2/asm/asm-all/6.0_BETA/asm-all-6.0_BETA.jar",
					"dynamicexchanger/libs/asm-all-6.0_BETA.jar"))
				AddClassPath.addClassPathInDirs("dynamicexchanger/libs");
			else {
				System.err.println("Resource downloading failed, please restrart the program!");
				System.exit(0);
			}
		}
		inst.addTransformer(new DETransformer());
		System.out.println("Class Hacking Preparation Over.");
		HackCommandsWriter.loadAccess();
		HackCrashReportWriter.loadAccess();
		HackGuiWriter.loadAccess();
		HackRenderInterfaceWriter.loadAccess();
		HackTitleScreenWriter.loadAccess();
		try {
			Class.forName("com.github.nickid2018.dynamicex.SharedAfterLoadConstants", false,
					Thread.currentThread().getContextClassLoader());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void checkDir(String name) {
		File file = new File(name);
		if (!file.isDirectory())
			file.mkdir();
	}
}
