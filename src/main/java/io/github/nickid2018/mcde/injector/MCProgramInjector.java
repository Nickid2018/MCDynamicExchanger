package io.github.nickid2018.mcde.injector;

import io.github.nickid2018.mcde.format.MappingFormat;
import io.github.nickid2018.mcde.format.MojangMappingFormat;
import io.github.nickid2018.mcde.format.YarnMappingFormat;
import io.github.nickid2018.mcde.remapper.FileProcessor;
import io.github.nickid2018.mcde.util.I18N;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.zip.ZipFile;

// Argument Format: <mapping type>;<mapping>;<mc jar file>
public class MCProgramInjector {

    public static Instrumentation instrumentation;
    public static MappingFormat format;

    public static void premain(String args, Instrumentation instrumentation) throws IOException {
        MCProgramInjector.instrumentation = instrumentation;
        createMapper(args);
    }

    public static void agentmain(String args, Instrumentation instrumentation) throws IOException {
        MCProgramInjector.instrumentation = instrumentation;
        createMapper(args);
    }

    private static void createMapper(String data) throws IOException {
        String[] typeAndPath = data.split(";", 3);
        format = switch (typeAndPath[0].toLowerCase(Locale.ROOT)) {
            case "mojang" -> new MojangMappingFormat(Files.newInputStream(Path.of(typeAndPath[1])));
            case "yarn" -> new YarnMappingFormat(Files.newInputStream(Path.of(typeAndPath[1])));
            default -> throw new IllegalArgumentException(I18N.getTranslation("error.mapping.format"));
        };
        try (ZipFile file = new ZipFile(typeAndPath[3])) {
            FileProcessor.addPlainClasses(file, format);
            FileProcessor.generateInheritTree(file, format);
        }
        format.createToSourceMapper();
    }
}
