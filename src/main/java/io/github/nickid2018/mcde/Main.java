package io.github.nickid2018.mcde;

import io.github.nickid2018.mcde.remapper.FileProcessor;
import io.github.nickid2018.mcde.remapper.MojangFormatRemapper;
import io.github.nickid2018.mcde.util.ConsumerE;
import org.apache.commons.cli.*;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipFile;

public class Main {

    public static final Map<Options, ConsumerE<CommandLine>> parsers = new HashMap<>();

    public static void main(String[] args) {
        parsers.put(initOptionHelp(), Main::doHelp);
        parsers.put(initOptionRemap(), Main::doRemap);
        for (Options options : parsers.keySet())
            try {
                CommandLine commandLine = new DefaultParser().parse(options, args);
                parsers.get(options).accept(commandLine);
                return;
            } catch (Exception ignored) {
            }
        showHelp();
    }

    private static void showHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(110);

        for (Options options : parsers.keySet())
            formatter.printHelp("java -jar mcde.jar", options, true);
    }

    private static Options initOptionRemap() {
        Options remapOptions = new Options();

        Option remap = new Option("remap", false, "Remap a Minecraft file");
        remap.setRequired(true);
        remapOptions.addOption(remap);

        Option input = new Option("i", "input", true, "Minecraft JAR file");
        input.setRequired(true);
        input.setType(File.class);
        remapOptions.addOption(input);

        Option inputMapping = new Option("m", "mapping", true, "Obfuscate mapping");
        inputMapping.setRequired(true);
        input.setType(File.class);
        remapOptions.addOption(inputMapping);

        Option output = new Option("o", "output", true, "Output file");
        output.setRequired(false);
        input.setType(File.class);
        remapOptions.addOption(output);

        Option type = new Option("t", "type", true, "Mapping type");
        type.setRequired(false);
        input.setType(String.class);
        remapOptions.addOption(type);

        return remapOptions;
    }

    private static Options initOptionHelp() {
        Options helpOptions = new Options();

        Option help = new Option("h", "help", false, "Get this help message");
        help.setRequired(true);
        helpOptions.addOption(help);

        return helpOptions;
    }

    private static void doRemap(CommandLine commandLine) throws Exception {
        File input = (File) commandLine.getParsedOptionValue("input");
        File mapping = (File) commandLine.getParsedOptionValue("mapping");
        File output = new File("remapped.jar");
        if (commandLine.hasOption("output"))
            output = (File) commandLine.getParsedOptionValue("output");

        try (ZipFile file = new ZipFile(input)) {
            FileProcessor.process(file, mapping, output);
        }
    }

    private static void doHelp(CommandLine commandLine) {
        showHelp();
    }
}
