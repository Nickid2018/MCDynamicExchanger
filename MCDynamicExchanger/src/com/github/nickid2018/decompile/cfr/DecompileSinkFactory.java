package com.github.nickid2018.decompile.cfr;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import java.nio.charset.*;
import org.benf.cfr.reader.api.*;
import com.github.nickid2018.*;

import static com.github.nickid2018.ProgramMain.logger;

public class DecompileSinkFactory implements OutputSinkFactory {

	private static final String lineSeparator = System.getProperty("line.separator");
	private static final Calendar INSTANCE = Calendar.getInstance();

	private ZipOutputStream zos;
	private boolean detailed;
	private boolean noInfos;

	public DecompileSinkFactory(String output, boolean detailed, boolean noInfos) throws IOException {
		zos = new ZipOutputStream(new FileOutputStream(output));
		this.detailed = detailed;
		this.noInfos = noInfos;
	}

	public ZipOutputStream getStream() {
		return zos;
	}
	
	private static final List<SinkClass> EXCEPTIONS = Collections.singletonList(SinkClass.EXCEPTION_MESSAGE);
	private static final List<SinkClass> DECOMPILED = Collections.singletonList(SinkClass.DECOMPILED);
	private static final List<SinkClass> STRING = Collections.singletonList(SinkClass.STRING);

	@Override
	public List<SinkClass> getSupportedSinks(SinkType sinkType, Collection<SinkClass> available) {
		if (sinkType == SinkType.JAVA && available.contains(SinkClass.DECOMPILED))
			return DECOMPILED;
		else if (sinkType == SinkType.EXCEPTION)
			return EXCEPTIONS;
		else
			return STRING;
	}

	@Override
	public <T> Sink<T> getSink(SinkType sinkType, SinkClass sinkClass) {
		switch (sinkType) {
		case EXCEPTION:
			return x -> {
				SinkReturns.ExceptionMessage message = (SinkReturns.ExceptionMessage) x;
				logger.error(I18N.getText("error.decompile.cfr", message.getPath(), message.getMessage()),
						message.getThrownException());
			};
		case JAVA:
			return x -> {
				SinkReturns.Decompiled decompiled = (SinkReturns.Decompiled) x;
				if (detailed) {
					logger.info(I18N.getText("decompile.classover",
							decompiled.getPackageName() + "." + decompiled.getClassName()));
				}
				ZipEntry output = new ZipEntry(
						decompiled.getPackageName().replace('.', '/') + "/" + decompiled.getClassName() + ".java");
				try {
					zos.putNextEntry(output);
					StringWriter writer = new StringWriter();
					if (!noInfos) {
						writer.write("/*" + lineSeparator);
						writer.write(" * This file is decompiled by MCDynamicExchanger." + lineSeparator);
						writer.write(
								" * This file can only be used to learn the code and you bear the risk of using this file."
										+ lineSeparator);
						writer.write(" * You may copy and use this file for your internal, reference purposes."
								+ lineSeparator);
						writer.write(" * ====== Decompiler Information ======" + lineSeparator);
						writer.write(" * MCDynamicExchanger version " + ProgramMain.VERSION + lineSeparator);
						writer.write(" * Decompile backend library: CFR" + lineSeparator);
						writer.write(" * ====== Build Information ======" + lineSeparator);
						writer.write(" * Source File: " + decompiled.getPackageName().replace('.', '/') + "/"
								+ decompiled.getClassName() + lineSeparator);
						writer.write(" * Build Time: " + String.format("%tc", INSTANCE) + lineSeparator);
						writer.write(" *" + lineSeparator);
						writer.write(" */" + lineSeparator);
					}
					writer.write(decompiled.getJava());
					zos.write(writer.toString().getBytes(Charset.forName("UTF-8")));
				} catch (IOException e) {
					logger.error(I18N.getText("error.decompile.cfr",
							decompiled.getPackageName() + "." + decompiled.getClassName(), e.getMessage()), e);
				}
			};
		default:
			return x -> {
			};
		}
	}

}
