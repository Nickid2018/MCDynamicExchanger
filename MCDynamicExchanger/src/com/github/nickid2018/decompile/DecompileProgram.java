package com.github.nickid2018.decompile;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import org.jd.core.v1.*;
import com.github.nickid2018.*;
import org.apache.commons.io.*;
import com.github.nickid2018.util.*;
import com.github.nickid2018.argparser.*;

import static com.github.nickid2018.ProgramMain.logger;

public class DecompileProgram {

	public static void decompileSimple(CommandResult result) {
		logger = new DefaultConsoleLogger();
		if (!AddClassPath.tryToLoadMCLibrary("commons-io/commons-io")) {
			logger.info("Cannot load library \"commons-io\","
					+ " please ensure your running directory is right and your Minecraft has been downloaded.");
			return;
		}
		if (!ClassUtils.isClassExists("org.jd.core.v1.ClassFileToJavaSourceDecompiler")) {
			logger.info("Cannot load library \"jd-core\", please ensure the jd-core library has been put in libs.");
			logger.info("jd-core download page: https://bintray.com/java-decompiler/maven/org.jd%3Ajd-core/1.1.3");
			return;
		}
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
							logger.info("Resource Entry: " + entry.getName());
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
					logger.info("Decompiled Class: " + internalName);
				}
			}
			zos.close();
		} catch (Throwable e) {
			logger.error("Unknown error has been thrown!", e);
		}
	}
}
