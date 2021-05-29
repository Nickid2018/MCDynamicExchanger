package com.github.nickid2018.dynamicex;

import java.io.*;
import java.lang.instrument.*;
import com.github.nickid2018.*;
import com.github.nickid2018.util.*;
import com.github.nickid2018.util.download.*;
import com.github.nickid2018.dynamicex.hacks.*;

public class DEProgramInterface {

	public static Instrumentation instrumention;

	public static void premain(String preargs, Instrumentation inst) {
		ProgramMain.logger = new DefaultConsoleLogger();
		instrumention = inst;
		checkDir("dynamicexchanger/libs");
		checkDir("dynamicexchanger/mappings");
		AddClassPath.addClassPathInDirs("dynamicexchanger/libs");
		checkFiles();
		inst.addTransformer(new DETransformer());
		System.out.println("Class Hacking Preparation Over.");
		HackCommandsWriter.loadAccess();
		HackCrashReportWriter.loadAccess();
		HackGuiWriter.loadAccess();
		HackRenderInterfaceWriter.loadAccess();
		HackTitleScreenWriter.loadAccess();
		HackCommandsListWriter.loadAccess();
		HackLocalPlayerWriter.loadAccess();
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

	private static void checkFiles() {
		boolean failed = false;
		if (!ClassUtils.isClassExists("org.objectweb.asm.Opcodes"))
			DownloadService.downloadResource("asm", "https://repo1.maven.org/maven2/org/ow2/asm/asm/9.0/asm-9.0.jar",
					"dynamicexchanger/libs/asm-9.0.jar");
		if (!ClassUtils.isClassExists("org.objectweb.asm.commons.ClassRemapper"))
			DownloadService.downloadResource("asm-commons",
					"https://repo1.maven.org/maven2/org/ow2/asm/asm-commons/9.0/asm-commons-9.0.jar",
					"dynamicexchanger/libs/asm-commons-9.0.jar");
		if (!ClassUtils.isClassExists("org.objectweb.asm.tree.analysis.Analyzer"))
			DownloadService.downloadResource("asm-analysis",
					"https://repo1.maven.org/maven2/org/ow2/asm/asm-analysis/9.0/asm-analysis-9.0.jar",
					"dynamicexchanger/libs/asm-analysis-9.0.jar");
		if (!ClassUtils.isClassExists("org.objectweb.asm.tree.ClassNode"))
			DownloadService.downloadResource("asm-tree",
					"https://repo1.maven.org/maven2/org/ow2/asm/asm-tree/9.0/asm-tree-9.0.jar",
					"dynamicexchanger/libs/asm-tree-9.0.jar");
		DownloadService.startDownloadInfoOutput();
		DownloadService.waitDownloadOver();
		DownloadService.stopDownloadInfoOutput();
		failed |= !DownloadService.FAILED_DOWNLOAD.isEmpty();
		if (failed) {
			System.out.println("Lost libraries!");
			System.exit(-1);
		}
		AddClassPath.addClassPathInDirs("dynamicexchanger/libs");
	}
}
