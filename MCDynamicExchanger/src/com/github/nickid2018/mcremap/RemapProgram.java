package com.github.nickid2018.mcremap;

import com.github.nickid2018.*;
import com.github.nickid2018.util.*;
import com.github.nickid2018.argparser.*;

import static com.github.nickid2018.ProgramMain.logger;

public class RemapProgram {

	public static void doSimpleRemap(CommandResult result) {
		logger = new DefaultConsoleLogger();
		checkFiles();
		try {
			OfficalFormat format = new OfficalFormat(result);
			format.processInitMap(result.getSwitch("mapping_url").toString());
			logger.formattedInfo("remap.readmap.over");
			ClassTreeGenerator generator = new ClassTreeGenerator();
			generator.runGenerate(result, format);
			if (result.containsSwitch("-Dy")) {
				logger.formattedInfo("remap.dryrun");
				return;
			}
			if (!(result.containsSwitch("--outmap") || result.containsSwitch("--outrev"))) {
				FileRemapper remapper = new FileRemapper();
				remapper.remapAll(result, format);
			}
		} catch (Throwable e) {
			logger.error(I18N.getText("error.unknown"), e);
		}
	}

	private static void checkFiles() {
		boolean failed = false;
		if (!ClassUtils.isClassExists("org.apache.commons.io.IOUtils")
				&& !AddClassPath.tryToLoadMCLibrary("commons-io/commons-io")) {
			logger.formattedInfo("error.libraries.io");
			failed = true;
		}
		if (!ClassUtils.isClassExists("org.objectweb.asm.Opcodes")) {
			failed |= !DownloadUtils.downloadResource("https://repo1.maven.org/maven2/org/ow2/asm/asm/9.0/asm-9.0.jar",
					"dynamicexchanger/libs/asm-9.0.jar");
			AddClassPath.addClassPathInDirs("dynamicexchanger/libs");
		}
		if (!ClassUtils.isClassExists("org.objectweb.asm.commons.ClassRemapper")) {
			failed |= !DownloadUtils.downloadResource(
					"https://repo1.maven.org/maven2/org/ow2/asm/asm-commons/9.0/asm-commons-9.0.jar",
					"dynamicexchanger/libs/asm-commons-9.0.jar");
			AddClassPath.addClassPathInDirs("dynamicexchanger/libs");
		}
		if (!ClassUtils.isClassExists("org.objectweb.asm.tree.analysis.Analyzer")) {
			failed |= !DownloadUtils.downloadResource(
					"https://repo1.maven.org/maven2/org/ow2/asm/asm-analysis/9.0/asm-analysis-9.0.jar",
					"dynamicexchanger/libs/asm-analysis-9.0.jar");
			AddClassPath.addClassPathInDirs("dynamicexchanger/libs");
		}
		if (!ClassUtils.isClassExists("org.objectweb.asm.tree.ClassNode")) {
			failed |= !DownloadUtils.downloadResource(
					"https://repo1.maven.org/maven2/org/ow2/asm/asm-tree/9.0/asm-tree-9.0.jar",
					"dynamicexchanger/libs/asm-tree-9.0.jar");
			AddClassPath.addClassPathInDirs("dynamicexchanger/libs");
		}
		if (failed) {
			logger.formattedInfo("error.libraries.lost");
			System.exit(-1);
		}
	}
}
