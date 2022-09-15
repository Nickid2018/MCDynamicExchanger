package io.github.nickid2018.mcde;

import io.github.nickid2018.mcde.remapper.FileProcessor;
import io.github.nickid2018.mcde.format.MappingFormat;
import io.github.nickid2018.mcde.format.MojangMappingFormat;
import io.github.nickid2018.mcde.format.YarnMappingFormat;
import io.github.nickid2018.mcde.util.ConsumerE;
import io.github.nickid2018.mcde.util.I18N;
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
            } catch (ParseException ignored) {
            } catch (Exception e) {
                e.printStackTrace();
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

        Option remap = new Option("remap", false, I18N.getTranslation("command.remap"));
        remap.setRequired(true);
        remapOptions.addOption(remap);

        Option input = new Option("i", "input", true, I18N.getTranslation("command.remap.source"));
        input.setRequired(true);
        input.setType(File.class);
        remapOptions.addOption(input);

        Option inputMapping = new Option("m", "mapping", true, I18N.getTranslation("command.remap.mapping"));
        inputMapping.setRequired(true);
        input.setType(File.class);
        remapOptions.addOption(inputMapping);

        Option output = new Option("o", "output", true, I18N.getTranslation("command.remap.output"));
        output.setRequired(false);
        input.setType(File.class);
        remapOptions.addOption(output);

        Option type = new Option("t", "type", true, I18N.getTranslation("command.remap.type"));
        type.setRequired(false);
        input.setType(String.class);
        remapOptions.addOption(type);


        Option server = new Option("s", "server", false, I18N.getTranslation("command.remap.server"));
        server.setRequired(false);
        remapOptions.addOption(server);

        return remapOptions;
    }

    private static Options initOptionHelp() {
        Options helpOptions = new Options();

        Option help = new Option("h", "help", false, I18N.getTranslation("command.help"));
        help.setRequired(true);
        helpOptions.addOption(help);

        return helpOptions;
    }

    private static void doRemap(CommandLine commandLine) throws Exception {
        File input = new File(commandLine.getOptionValue("input"));
        File mapping = new File(commandLine.getOptionValue("mapping"));
        File output = new File("remapped.jar");
        if (commandLine.hasOption("output"))
            output = new File(commandLine.getOptionValue("output"));

        try (ZipFile file = new ZipFile(input)) {
            String type = commandLine.hasOption("type") ? commandLine.getOptionValue("type") : "mojang";
            MappingFormat format = switch (type) {
                case "mojang" -> new MojangMappingFormat(Files.newInputStream(mapping.toPath()));
                case "yarn" -> new YarnMappingFormat(Files.newInputStream(mapping.toPath()));
                default -> throw new IllegalArgumentException(I18N.getTranslation("error.mapping.format"));
            };
            if (commandLine.hasOption("server"))
                FileProcessor.processServer(file, format, output);
            else
                FileProcessor.process(file, format, output);
        }
    }

    private static void doHelp(CommandLine commandLine) {
        showHelp();
    }
}
