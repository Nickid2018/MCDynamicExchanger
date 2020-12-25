package com.github.nickid2018.compare;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import com.github.nickid2018.*;
import com.github.nickid2018.util.*;

import static com.github.nickid2018.ProgramMain.logger;

public class CompareProgram {

	public List<String> files = new ArrayList<>();
	public Map<String, InputStream> oldVersionEntries = new HashMap<>();
	public Enumeration<? extends ZipEntry> newVersionEntries;
	private ZipFile oldV;
	private ZipFile newV;

	public CompareProgram(String oldVersion, String newVersion) throws IOException {
		oldV = new ZipFile(oldVersion);
		newV = new ZipFile(newVersion);
		newVersionEntries = newV.entries();
		initOldVersion();
	}

	public void initOldVersion() throws IOException {
		Enumeration<? extends ZipEntry> oldEntries = oldV.entries();
		while (oldEntries.hasMoreElements()) {
			ZipEntry entry = oldEntries.nextElement();
			String name = entry.getName().replace('/', '.');
			if (!name.endsWith(".class"))
				continue;
			name = name.substring(0, name.length() - 6);
			oldVersionEntries.put(name, oldV.getInputStream(entry));
			files.add(name);
		}
	}

	public boolean hasNext() {
		return !oldVersionEntries.isEmpty() && newVersionEntries.hasMoreElements();
	}

	public CompareResult next() throws IOException {
		if (newVersionEntries.hasMoreElements()) {
			String name = "";
			ZipEntry entry = null;
			while (!name.endsWith(".class") && newVersionEntries.hasMoreElements()) {
				entry = newVersionEntries.nextElement();
				name = entry.getName().replace('/', '.');
			}
			if (name.endsWith(".class")) {
				name = name.substring(0, name.length() - 6);
				files.remove(name);
				return new CompareResult(name, oldVersionEntries.remove(name), newV.getInputStream(entry));
			}
		}
		if (!oldVersionEntries.isEmpty()) {
			String name = files.remove(0);
			return new CompareResult(name, oldVersionEntries.remove(name), null);
		}
		return null;
	}

	public void close() throws IOException {
		oldV.close();
		newV.close();
	}

	public static void compareSimple(String oldVersion, String newVersion, boolean detailed) throws IOException {
		logger = new SortedConsoleLogger();
		if (!AddClassPath.tryToLoadMCLibrary("commons-io/commons-io")) {
			logger.info("Cannot load library \"commons-io\","
					+ " please ensure your running directory is right and your Minecraft has been downloaded.");
			return;
		}
		try {
			CompareProgram program = new CompareProgram(oldVersion, newVersion);
			while (program.hasNext()) {
				CompareResult result = program.next();
				if (result.type != CompareResultType.NONE)
					logger.info(result.getMessage()
							+ (detailed ? "[Old: " + result.oldMD5 + " New: " + result.newMD5 + "]" : ""));
			}
			program.close();
		} catch (Throwable e) {
			logger.error("Unknown error has been thrown!", e);
		}
		logger.flush();
	}
}
