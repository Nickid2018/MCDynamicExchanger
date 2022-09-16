package io.github.nickid2018.mcde;

import io.github.nickid2018.mcde.remapper.FileProcessor;
import io.github.nickid2018.mcde.format.MappingFormat;
import io.github.nickid2018.mcde.format.MojangMappingFormat;
import io.github.nickid2018.mcde.format.YarnMappingFormat;
import io.github.nickid2018.mcde.util.ConsumerE;
import io.github.nickid2018.mcde.util.I18N;
import io.github.nickid2018.mcde.util.Pair;
import io.github.nickid2018.mcde.util.ZipUtils;
import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipFile;

public class Main {

    public static final List<Pair<Options, ConsumerE<CommandLine>>> parsers = new ArrayList<>();

    public static void main(String[] args) {
        parsers.add(new Pair<>(initOptionHelp(), Main::doHelp));
        parsers.add(new Pair<>(initOptionRemap(), Main::doRemap));
        parsers.add(new Pair<>(initOptionDecompile(), Main::doDecompile));
        for (Pair<Options, ConsumerE<CommandLine>> pairs : parsers)
            try {
                CommandLine commandLine = new DefaultParser().parse(pairs.left(), args);
                pairs.right().accept(commandLine);
                return;
            } catch (ParseException ignored) {
            } catch (Exception e) {
                e.printStackTrace();
            }
        showHelp();
    }

    public static void log(String message) {
        System.out.println(message);
    }
    public static void err(String message, Throwable throwable) {
        System.err.println(message);
        throwable.printStackTrace();
    }

    private static void showHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(110);

        for (Pair<Options, ConsumerE<CommandLine>> pairs : parsers)
            formatter.printHelp("java -jar mcde.jar", pairs.left(), true);
    }

    private static Options initOptionRemap() {
        Options remapOptions = new Options();

        Option remap = new Option("remap", false, I18N.getTranslation("command.remap"));
        remap.setRequired(true);
        remapOptions.addOption(remap);

        Option input = new Option("i", "input", true, I18N.getTranslation("command.remap.source"));
        input.setRequired(true);
        remapOptions.addOption(input);

        Option inputMapping = new Option("m", "mapping", true, I18N.getTranslation("command.remap.mapping"));
        inputMapping.setRequired(true);
        remapOptions.addOption(inputMapping);

        Option output = new Option("o", "output", true, I18N.getTranslation("command.remap.output"));
        output.setRequired(false);
        remapOptions.addOption(output);

        Option type = new Option("t", "type", true, I18N.getTranslation("command.remap.type"));
        type.setRequired(false);
        remapOptions.addOption(type);


        Option server = new Option("s", "server", false, I18N.getTranslation("command.remap.server"));
        server.setRequired(false);
        remapOptions.addOption(server);

        return remapOptions;
    }

    private static Options initOptionDecompile() {
        Options decompileOptions = new Options();

        Option decompile = new Option("decompile", false, I18N.getTranslation("command.decompile"));
        decompile.setRequired(true);
        decompileOptions.addOption(decompile);

        Option input = new Option("i", "input", true, I18N.getTranslation("command.decompile.source"));
        input.setRequired(true);
        decompileOptions.addOption(input);

        Option output = new Option("o", "output", true, I18N.getTranslation("command.decompile.output"));
        output.setRequired(false);
        decompileOptions.addOption(output);

        return decompileOptions;
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

    private static void doDecompile(CommandLine commandLine) throws Exception {
        File input = new File(commandLine.getOptionValue("input"));
        File output = new File("decompiled.jar");
        if (commandLine.hasOption("output"))
            output = new File(commandLine.getOptionValue("output"));

        org.benf.cfr.reader.Main.main(new String[] {
                input.getAbsolutePath(),
                "--outputpath", output.getAbsolutePath() + "-files",
                "--comments", "false",
                "--silent", "true",
                "--clobber", "true"
        });

        ZipUtils.zipFolders(output.getAbsolutePath() + "-files", output.getAbsolutePath());

        FileUtils.deleteDirectory(new File(output.getAbsolutePath() + "-files"));
    }

    private static void doHelp(CommandLine commandLine) {
        showHelp();
    }
}
