package io.github.nickid2018.mcde;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.nickid2018.mcde.asmdl.ASMDLParser;
import io.github.nickid2018.mcde.asmdl.decompile.ClassDecompileVisitor;
import io.github.nickid2018.mcde.format.MappingFormat;
import io.github.nickid2018.mcde.format.MojangMappingFormat;
import io.github.nickid2018.mcde.format.YarnMappingFormat;
import io.github.nickid2018.mcde.remapper.FileProcessor;
import io.github.nickid2018.mcde.util.*;
import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.objectweb.asm.ClassReader;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;
import java.util.zip.ZipFile;

public class Main {

    public static final List<Pair<Options, ConsumerE<CommandLine>>> parsers = new ArrayList<>();

    public static void main(String[] args) {
        parsers.add(new Pair<>(initOptionHelp(), Main::doHelp));
        parsers.add(new Pair<>(initOptionRemap(), Main::doRemap));
        parsers.add(new Pair<>(initOptionDecompile(), Main::doDecompile));
        parsers.add(new Pair<>(initOptionAuto(), Main::doAuto));
        parsers.add(new Pair<>(initOptionASMDL(), Main::doASMDL));
        for (Pair<Options, ConsumerE<CommandLine>> pairs : parsers)
            try {
                CommandLine commandLine = new DefaultParser().parse(pairs.left(), args);
                pairs.right().accept(commandLine);
                return;
            } catch (ParseException ignored) {
            } catch (IllegalArgumentException e) {
                LogUtils.err(e.getMessage(), null);
                return;
            } catch (IOException e) {
                LogUtils.err("error.io", e);
                return;
            } catch (Exception e) {
                LogUtils.err("error.unknown", e);
                return;
            }
        LogUtils.err("error.argument.help", null);
        showHelp();
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

    private static Options initOptionAuto() {
        Options autoOptions = new Options();

        Option help = new Option("auto", false, I18N.getTranslation("command.auto"));
        help.setRequired(true);
        autoOptions.addOption(help);

        Option version = new Option("v", "version", true, I18N.getTranslation("command.auto.version"));
        version.setRequired(true);
        autoOptions.addOption(version);

        Option output = new Option("o", "output", true, I18N.getTranslation("command.auto.output"));
        output.setRequired(false);
        autoOptions.addOption(output);

        return autoOptions;
    }

    private static Options initOptionASMDL() {
        Options compileOptions = new Options();

        Option flag = new Option("asmdl", false, I18N.getTranslation("command.asmdl"));
        flag.setRequired(true);
        compileOptions.addOption(flag);

        Option compile = new Option("c", "compile", false, I18N.getTranslation("command.asmdl.compile"));
        compile.setRequired(false);
        compileOptions.addOption(compile);

        Option decompile = new Option("d", "decompile", false, I18N.getTranslation("command.asmdl.decompile"));
        decompile.setRequired(false);
        compileOptions.addOption(decompile);

        Option input = new Option("i", "input", true, I18N.getTranslation("command.asmdl.source"));
        input.setRequired(true);
        compileOptions.addOption(input);

        Option output = new Option("o", "output", true, I18N.getTranslation("command.asmdl.output"));
        output.setRequired(false);
        compileOptions.addOption(output);

        return compileOptions;
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
        LogUtils.log("process.remap.start");

        try (ZipFile file = new ZipFile(input)) {
            String type = commandLine.hasOption("type") ? commandLine.getOptionValue("type") : "mojang";
            LogUtils.log("process.remap.mapping");
            MappingFormat format = switch (type) {
                case "mojang" -> new MojangMappingFormat(Files.newInputStream(mapping.toPath()));
                case "yarn" -> new YarnMappingFormat(Files.newInputStream(mapping.toPath()));
                default -> throw new IllegalArgumentException(I18N.getTranslation("error.mapping.unsupported"));
            };
            LogUtils.log("process.remap.mapping.done");
            if (commandLine.hasOption("server"))
                FileProcessor.processServer(file, format, output);
            else
                FileProcessor.process(file, format, output, false);
        }
    }

    private static void doDecompile(CommandLine commandLine) throws Exception {
        File input = new File(commandLine.getOptionValue("input"));
        File output = new File("decompiled.jar");
        if (commandLine.hasOption("output"))
            output = new File(commandLine.getOptionValue("output"));
        LogUtils.log("process.decompile.start");
        org.benf.cfr.reader.Main.main(new String[]{
                input.getAbsolutePath(),
                "--outputpath", output.getAbsolutePath() + "-files",
                "--comments", "false",
                "--silent", "true",
                "--clobber", "true"
        });
        LogUtils.log("process.decompile.pack");
        ZipUtils.zipFolders(output.getAbsolutePath() + "-files", output.getAbsolutePath());
        LogUtils.log("process.decompile.clean");
        FileUtils.deleteDirectory(new File(output.getAbsolutePath() + "-files"));
        LogUtils.log("process.decompile.success");
    }

    private static void doAuto(CommandLine commandLine) throws Exception {
        File output = new File("decompiled.jar");
        if (commandLine.hasOption("output"))
            output = new File(commandLine.getOptionValue("output"));
        String version = commandLine.getOptionValue("version");

        LogUtils.log("process.auto.fetch");
        JsonObject data = JsonParser.parseString(IOUtils.toString(
                        new URL("https://piston-meta.mojang.com/mc/game/version_manifest.json"), StandardCharsets.UTF_8))
                .getAsJsonObject();
        String url = StreamSupport.stream(data.getAsJsonArray("versions").spliterator(), false)
                .map(JsonElement::getAsJsonObject)
                .filter(json -> json.get("id").getAsString().equals(version))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(I18N.getTranslation("error.version.notfound")))
                .get("url").getAsString();

        LogUtils.log("process.auto.file");
        JsonObject versionData = JsonParser.parseString(IOUtils.toString(
                new URL(url), StandardCharsets.UTF_8)).getAsJsonObject();
        String clientUrl = versionData.getAsJsonObject("downloads")
                .getAsJsonObject("client").get("url").getAsString();
        String clientMappingUrl = versionData.getAsJsonObject("downloads")
                .getAsJsonObject("client_mappings").get("url").getAsString();

        File clientJar = new File("temp-client.jar");
        File temp = new File("temp-de.jar");
        FileUtils.copyURLToFile(new URL(clientUrl), clientJar);
        InputStream clientMappingStream = new URL(clientMappingUrl).openStream();
        try (ZipFile file = new ZipFile(clientJar)) {
            LogUtils.log("process.remap.mapping");
            MappingFormat format = new MojangMappingFormat(clientMappingStream);
            LogUtils.log("process.remap.mapping.done");
            FileProcessor.process(file, format, temp, false);
        }

        LogUtils.log("process.decompile.start");
        org.benf.cfr.reader.Main.main(new String[]{
                temp.getAbsolutePath(),
                "--outputpath", output.getAbsolutePath() + "-files",
                "--comments", "false",
                "--silent", "true",
                "--clobber", "true"
        });
        LogUtils.log("process.decompile.pack");
        ZipUtils.zipFolders(output.getAbsolutePath() + "-files", output.getAbsolutePath());
        LogUtils.log("process.decompile.clean");
        FileUtils.deleteDirectory(new File(output.getAbsolutePath() + "-files"));
        clientJar.delete();
        temp.delete();
        LogUtils.log("process.decompile.success");
    }

    private static void doASMDL(CommandLine commandLine) throws Exception {
        File input = new File(commandLine.getOptionValue("input"));

        boolean compile = commandLine.hasOption("compile");
        boolean decompile = commandLine.hasOption("decompile");

        if (compile == decompile)
            throw new IllegalArgumentException(I18N.getTranslation("error.asmdl.invalid"));

        if (compile) {
            File output = new File("compiled.class");
            if (commandLine.hasOption("output"))
                output = new File(commandLine.getOptionValue("output"));

            String data;
            try (InputStream fileInput = new FileInputStream(input)) {
                data = IOUtils.toString(fileInput, StandardCharsets.UTF_8);
            }
            ASMDLParser parser = new ASMDLParser(data);
            byte[] bytes = parser.toClass();
            try (FileOutputStream fileOutput = new FileOutputStream(output)) {
                fileOutput.write(bytes);
            }
        } else {
            File output = new File("decompiled.txt");
            if (commandLine.hasOption("output"))
                output = new File(commandLine.getOptionValue("output"));

            byte[] bytes;
            try (InputStream fileInput = new FileInputStream(input)) {
                bytes = IOUtils.toByteArray(fileInput);
            }
            ClassReader reader = new ClassReader(bytes);
            ClassDecompileVisitor visitor = new ClassDecompileVisitor();
            reader.accept(visitor, 0);
            try (FileWriter fileOutput = new FileWriter(output)) {
                fileOutput.write(visitor.decompiledString());
            }
        }
        LogUtils.log("process.asmdl.success");
    }

    private static void doHelp(CommandLine commandLine) {
        showHelp();
    }
}
