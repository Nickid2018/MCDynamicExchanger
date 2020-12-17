package com.github.nickid2018.decompile;

import java.io.*;
import java.nio.charset.*;
import org.jd.core.v1.api.printer.*;

public class DecompileFilePrinter implements Printer {

	private StringWriter writer;
	private static final String lineSeparator = System.getProperty("line.separator");

	public void next() {
		writer = new StringWriter();
	}

	public byte[] getBytes() {
		// An interesting thing..
		return writer.toString().replace("\u2603", "variable").getBytes(Charset.forName("UTF-8"));
	}

	@Override
	public void start(int var1, int var2, int var3) {
//		writer.append("// Start:" + var1 + " " + var2 + " " + var3);
	}

	@Override
	public void end() {
//		writer.append("// End");
	}

	@Override
	public void printText(String var1) {
		writer.append(var1);
	}

	@Override
	public void printNumericConstant(String var1) {
		writer.append(var1);
	}

	@Override
	public void printStringConstant(String var1, String var2) {
		writer.append(var1);
	}

	@Override
	public void printKeyword(String var1) {
		writer.append(var1);
	}

	@Override
	public void printDeclaration(int var1, String var2, String var3, String var4) {
		switch (var1) {
		case 1:
		case 2:
		case 3:
		case 4:
			writer.append(var3);
			break;
		default:
			writer.append(var1 + var2 + var3 + var4 + "DEC!");
		}
	}

	@Override
	public void printReference(int var1, String var2, String var3, String var4, String var5) {
		switch (var1) {
		case 1:
		case 2:
		case 3:
			writer.append(var3);
			break;
		default:
			writer.append(var1 + var2 + var3 + var4 + var5 + "REF!");
		}
	}

	private int indent = 0;

	@Override
	public void indent() {
		indent++;
	}

	@Override
	public void unindent() {
		indent--;
	}

	@Override
	public void startLine(int var1) {
	}

	@Override
	public void endLine() {
		writer.append(lineSeparator);
		for (int i = 0; i < indent; i++) {
			writer.append("    ");
		}
	}

	@Override
	public void extraLine(int var1) {
		writer.append("// Extra Line: " + var1);
	}

	@Override
	public void startMarker(int var1) {
//		writer.append("// Start Marker: " + var1);
	}

	@Override
	public void endMarker(int var1) {
//		writer.append("// End Marker: " + var1);
	}

}
