package com.github.nickid2018.decompile.cfr;

import com.github.nickid2018.I18N;
import com.github.nickid2018.ProgramMain;
import org.benf.cfr.reader.api.OutputSinkFactory;
import org.benf.cfr.reader.api.SinkReturns;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.github.nickid2018.ProgramMain.logger;

public class DecompileSinkFactory implements OutputSinkFactory {

    private static final String lineSeparator = System.getProperty("line.separator");
    private static final Calendar INSTANCE = Calendar.getInstance();
    private static final List<SinkClass> EXCEPTIONS = Collections.singletonList(SinkClass.EXCEPTION_MESSAGE);
    private static final List<SinkClass> DECOMPILED = Collections.singletonList(SinkClass.TOKEN_STREAM);
    private static final List<SinkClass> STRING = Collections.singletonList(SinkClass.STRING);
    private final ZipOutputStream zos;
    private final boolean noInfos;
    private final int indentTime;
    private String className;
    private StringWriter writer;
    private int indent = 0;
    private boolean lineStart = false;

    public DecompileSinkFactory(String output, boolean noInfos, int indentTime) throws IOException {
        zos = new ZipOutputStream(new FileOutputStream(output));
        this.noInfos = noInfos;
        this.indentTime = indentTime;
    }

    public ZipOutputStream getStream() {
        return zos;
    }

    public void startNextFile(String className, boolean detailed) throws IOException {
        className = className.split("\\.")[0];
        if (detailed)
            logger.info(I18N.getText("decompile.classover", className));
        zos.putNextEntry(new ZipEntry(className + ".java"));
        indent = 0;
        lineStart = false;
        this.className = className;
        writer = new StringWriter();
        if (!noInfos) {
            writer.write("/*" + lineSeparator);
            writer.write(" * This file is decompiled by MCDynamicExchanger." + lineSeparator);
            writer.write(" * This file can only be used to learn the code and you bear the risk of using this file."
                    + lineSeparator);
            writer.write(" * You may copy and use this file for your internal, reference purposes." + lineSeparator);
            writer.write(" * ====== Decompiler Information ======" + lineSeparator);
            writer.write(" * MCDynamicExchanger version " + ProgramMain.VERSION + lineSeparator);
            writer.write(" * Decompile backend library: CFR 0.151" + lineSeparator);
            writer.write(" * ====== Build Information ======" + lineSeparator);
            writer.write(" * Source File: " + className + lineSeparator);
            writer.write(" * Build Time: " + String.format("%tc", INSTANCE) + lineSeparator);
            writer.write(" *" + lineSeparator);
            writer.write(" */" + lineSeparator);
        }
    }

    @Override
    public List<SinkClass> getSupportedSinks(SinkType sinkType, Collection<SinkClass> available) {
        if (sinkType == SinkType.JAVA)
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
                    message.getThrownException().printStackTrace();
                    logger.error(I18N.getText("error.decompile.cfr", message.getPath(), message.getMessage()),
                            message.getThrownException());
                    writer.write("/* " + lineSeparator);
                    writer.write(" * Cannot be decompiled:" + lineSeparator);
                    writer.write(" * " + message.getMessage() + lineSeparator);
                    writer.write(" * StackTrace:" + lineSeparator);
                    message.getThrownException().printStackTrace(new PrintWriter(writer));
                    writer.write(lineSeparator);
                    writer.write(" */");
                    try {
                        zos.write(writer.toString().getBytes(StandardCharsets.UTF_8));
                        zos.closeEntry();
                    } catch (IOException e) {
                        logger.error(I18N.getText("error.decompile.cfr", className, e.getMessage()), e);
                    }
                };
            case JAVA:
                if (sinkClass == SinkClass.TOKEN_STREAM)
                    return x -> {
                        SinkReturns.Token token = (SinkReturns.Token) x;
                        switch (token.getTokenType()) {
                            case WHITESPACE:
                            case KEYWORD:
                            case OPERATOR:
                            case SEPARATOR:
                            case LITERAL:
                            case COMMENT:
                            case IDENTIFIER:
                            case FIELD:
                            case METHOD:
                            case LABEL:
                            case UNCLASSIFIED:
                                if (lineStart)
                                    for (int i = 0; i < indentTime * indent; i++)
                                        writer.write(" ");
                                writer.write(token.getText());
                                lineStart = false;
                                break;
                            case NEWLINE:
                                if (lineStart)
                                    for (int i = 0; i < indentTime * indent; i++)
                                        writer.write(" ");
                                writer.write(token.getText());
                                lineStart = true;
                                break;
                            case EOF:
                                String get = writer.toString().replace("â˜ƒ", "variable");
                                try {
                                    zos.write(get.getBytes(StandardCharsets.UTF_8));
                                    zos.closeEntry();
                                } catch (IOException e) {
                                    logger.error(I18N.getText("error.decompile.cfr", className, e.getMessage()), e);
                                }
                                break;
                            case INDENT:
                                indent++;
                                break;
                            case UNINDENT:
                                indent--;
                                break;
                            case EXPLICIT_INDENT:
                                // ?
                                break;
                        }
                    };
            default:
                return x -> {

                };
        }
    }

}
