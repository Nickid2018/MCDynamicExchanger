package com.github.nickid2018.decompile.cfr;

import java.util.*;
import java.util.zip.*;
import org.benf.cfr.reader.api.*;
import com.github.nickid2018.*;
import org.apache.commons.io.*;
import org.benf.cfr.reader.util.getopt.*;
import com.github.nickid2018.argparser.*;
import com.github.nickid2018.decompile.*;
import org.benf.cfr.reader.bytecode.analysis.parse.utils.*;
import static com.github.nickid2018.ProgramMain.logger;

public class CFRDecompiler implements Decompiler {

	private String[] args;

	public CFRDecompiler(String[] args) {
		this.args = new String[args.length + 1];
		this.args[0] = "";
		System.arraycopy(args, 0, this.args, 1, args.length);
	}

	@Override
	public void doDecompileSimple(CommandResult result) {
		GetOptParser getOptParser = new GetOptParser();
		Options options = null;
		try {
			Pair<List<String>, Options> processedArgs = getOptParser.parse(args, OptionsImpl.getFactory());
			options = processedArgs.getSecond();
		} catch (Exception e) {
			getOptParser.showHelp(e);
			System.exit(1);
		}
		if (options.optionIsSet(OptionsImpl.HELP)) {
			getOptParser.showOptionHelp(OptionsImpl.getFactory(), options, OptionsImpl.HELP);
			return;
		}
		String sourceJar = result.getSwitch("source_file").toString();
		String to = result.getStringOrDefault("--output", "decompiled.jar");
		boolean detailed = result.containsSwitch("-D");
		boolean noInfos = result.containsSwitch("-Ni");
		boolean resourceOutput = result.containsSwitch("-Ro");
		List<String> argSupplier = new ArrayList<>(1);
		argSupplier.add("");
		try {
			ZipFile file = new ZipFile(sourceJar);
			DecompileClassFileSource filesource = new DecompileClassFileSource(file);
			DecompileSinkFactory sink = new DecompileSinkFactory(to, detailed, noInfos);
			CfrDriver driver = new CfrDriver.Builder().withOverrideClassFileSource(filesource).withOutputSink(sink)
					.withBuiltOptions(options).build();
			Enumeration<? extends ZipEntry> entries = file.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				if (entry.isDirectory())
					continue;
				String name = entry.getName();
				if (!name.endsWith(".class")) {
					if (resourceOutput) {
						sink.getStream().putNextEntry(entry);
						IOUtils.copy(file.getInputStream(entry), sink.getStream());
						if (detailed) {
							logger.info(I18N.getText("decompile.resource", entry.getName()));
						}
					}
					continue;
				}
				if (name.contains("$"))
					continue;
				argSupplier.set(0, name);
				driver.analyse(argSupplier);
			}
			sink.getStream().close();
		} catch (Throwable e) {
			logger.error(I18N.getText("error.unknown"), e);
		}
	}

}
