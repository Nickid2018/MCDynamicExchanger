package com.github.nickid2018.decompile;

import com.github.nickid2018.*;
import com.github.nickid2018.util.*;
import com.github.nickid2018.argparser.*;

import static com.github.nickid2018.ProgramMain.logger;

public class DecompileProgram {

	public static void decompileSimple(CommandResult result) {
		logger = new DefaultConsoleLogger();
		if (!ClassUtils.isClassExists("org.apache.commons.io.IOUtils")
				&& !AddClassPath.tryToLoadMCLibrary("commons-io/commons-io")) {
			logger.formattedInfo("error.libraries.io");
			return;
		}
		String decompiler = result.getStringOrDefault("--backend", "jdcore");
		Decompiler instance = Decompiler.getInstance(decompiler);
		if (instance == null)
			return;
		instance.doDecompileSimple(result);
	}
}
