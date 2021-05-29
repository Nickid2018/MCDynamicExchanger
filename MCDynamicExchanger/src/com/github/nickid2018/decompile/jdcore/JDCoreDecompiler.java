package com.github.nickid2018.decompile.jdcore;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import org.jd.core.v1.*;
import com.github.nickid2018.*;
import org.apache.commons.io.*;
import com.github.nickid2018.argparser.*;
import com.github.nickid2018.decompile.*;
import static com.github.nickid2018.ProgramMain.logger;

public class JDCoreDecompiler implements Decompiler {

	public void doDecompileSimple(CommandResult result) {
		try {
			String sourceJar = result.getSwitch("source_file").toString();
			String to = result.getStringOrDefault("--output", "decompiled.jar");
			boolean detailed = result.containsSwitch("-D");
			boolean noInfos = result.containsSwitch("-Ni");
			boolean resourceOutput = result.containsSwitch("-Ro");
			Map<String, Object> config = new HashMap<>();
			ClassFileToJavaSourceDecompiler decompiler = new ClassFileToJavaSourceDecompiler();
			ZipFile file = new ZipFile(sourceJar);
			ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(to));
			DecompileClassFileLoader loader = new DecompileClassFileLoader(file);
			DecompileFilePrinter printer = new DecompileFilePrinter();
			Enumeration<? extends ZipEntry> entries = file.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				if (entry.isDirectory())
					continue;
				String name = entry.getName();
				if (!name.endsWith(".class")) {
					if (resourceOutput) {
						zos.putNextEntry(entry);
						IOUtils.copy(file.getInputStream(entry), zos);
						if (detailed) {
							logger.info(I18N.getText("decompile.resource", entry.getName()));
						}
					}
					continue;
				}
				if (name.contains("$"))
					continue;
				String internalName = name.substring(0, name.length() - 6);
				ZipEntry output = new ZipEntry(internalName + ".java");
				zos.putNextEntry(output);
				printer.next(internalName, noInfos, entry.getCompressedSize());
				decompiler.decompile(loader, printer, internalName, config);
				zos.write(printer.getBytes());
				if (detailed) {
					logger.info(I18N.getText("decompile.classover", internalName));
				}
			}
			zos.close();
		} catch (Throwable e) {
			logger.error(I18N.getText("error.unknown"), e);
		}
	}
}
