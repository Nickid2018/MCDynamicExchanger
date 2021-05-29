package com.github.nickid2018.decompile;

import static com.github.nickid2018.ProgramMain.logger;

import com.github.nickid2018.*;
import com.github.nickid2018.argparser.*;
import com.github.nickid2018.decompile.cfr.*;
import com.github.nickid2018.decompile.jdcore.*;
import com.github.nickid2018.util.ClassUtils;

public interface Decompiler {

	void doDecompileSimple(CommandResult res);

	static Decompiler getInstance(String arg) {
		String[] splited = arg.split(":");
		switch (splited[0]) {
		case "jdcore":
			if (!ClassUtils.isClassExists("org.jd.core.v1.ClassFileToJavaSourceDecompiler")) {
				logger.formattedInfo("error.libraries.jdcore");
				return null;
			}
			return new JDCoreDecompiler();
		case "cfr":
			String[] args = splited.length == 1 ? new String[0] : splited[1].split(",");
			for (int i = 0; i < args.length; i++) {
				args[i] = args[i].replace('+', ' ');
			}
			if (!ClassUtils.isClassExists("org.benf.cfr.reader.api.CfrDriver")) {
				logger.formattedInfo("error.libraries.cfr");
				return null;
			}
			return new CFRDecompiler(args);
		default:
			throw new UnsupportedOperationException(I18N.getText("error.decompile.unsupport", splited[0]));
		}
	}
}
