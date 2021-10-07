package io.github.nickid2018.decompile.jdcore;

import io.github.nickid2018.ProgramMain;
import org.jd.core.v1.api.printer.Printer;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;

public class DecompileFilePrinter implements Printer {

    private static final String lineSeparator = System.getProperty("line.separator");
    private static final Calendar INSTANCE = Calendar.getInstance();
    private StringWriter writer;
    private int indentTimes;
    private int indent = 0;

    public void next(String fileName, boolean noInfos, int indentTimes) {
        writer = new StringWriter();
        this.indentTimes = indentTimes;
        if (noInfos)
            return;
        writer.write("/*" + lineSeparator);
        writer.write(" * This file is decompiled by MCDynamicExchanger." + lineSeparator);
        writer.write(" * This file can only be used to learn the code and you bear the risk of using this file."
                + lineSeparator);
        writer.write(" * You may copy and use this file for your internal, reference purposes." + lineSeparator);
        writer.write(" * ====== Decompiler Information ======" + lineSeparator);
        writer.write(" * MCDynamicExchanger version " + ProgramMain.VERSION + lineSeparator);
        writer.write(" * Decompile backend library: JD-Core 1.1.3" + lineSeparator);
        writer.write(" * ====== Build Information ======" + lineSeparator);
        writer.write(" * Source File: " + fileName + lineSeparator);
        writer.write(" * Build Time: " + String.format("%tc", INSTANCE) + lineSeparator);
        writer.write(" *" + lineSeparator);
        writer.write(" */" + lineSeparator);
    }

    public byte[] getBytes() {
        // An interesting thing..
        return writer.toString().replace("â˜ƒ", "variable").getBytes(StandardCharsets.UTF_8);
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
                writer.append(String.valueOf(var1)).append(var2).append(var3).append(var4).append("DEC!");
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
                writer.append(String.valueOf(var1)).append(var2).append(var3).append(var4).append(var5).append("REF!");
        }
    }

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
        for (int i = 0; i < indent * indentTimes; i++)
            writer.append(" ");
    }

    @Override
    public void extraLine(int var1) {
        writer.append("// Extra Line: ").append(String.valueOf(var1));
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
