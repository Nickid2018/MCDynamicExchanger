package com.github.nickid2018.mcremap;

import com.github.nickid2018.*;
import com.github.nickid2018.util.*;
import com.github.nickid2018.argparser.*;
import com.github.nickid2018.util.download.*;

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
		failed |= !DownloadService.waitDownloadOver();
		failed |= !DownloadService.FAILED_DOWNLOAD.isEmpty();
		DownloadService.stopDownloadInfoOutput();
		if (failed) {
			logger.formattedInfo("error.libraries.lost");
			System.exit(-1);
		}
		AddClassPath.addClassPathInDirs("dynamicexchanger/libs");
	}
}
