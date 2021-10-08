package io.github.nickid2018;

import io.github.nickid2018.argparser.*;
import io.github.nickid2018.asmifier.SimpleASMifier;
import io.github.nickid2018.compare.CompareProgram;
import io.github.nickid2018.decompile.DecompileProgram;
import io.github.nickid2018.mcfiledownload.MCFileDownloader;
import io.github.nickid2018.mcremap.RemapProgram;
import io.github.nickid2018.util.AddClassPath;
import io.github.nickid2018.util.ClassUtils;
import io.github.nickid2018.util.download.DownloadService;
import org.apache.commons.io.IOUtils;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ProgramMain {

    public static final String VERSION = "1.0 beta.8";

    public static ISystemLogger logger;

    public static void main(String[] args) throws Exception {
        long time = System.currentTimeMillis();
        AddClassPath.addClassPathInDirs("dynamicexchanger/libs");
        CommandModel model = getRemapperModel().subCommand("download", getDownloaderModel())
                .subCommand("compare", getComparatorModel()).subCommand("decompile", getDecompilerModel())
                .subCommand("asmifier", getASMifierModel()).subCommand("help", new CommandModel());
        try {
            CommandResult result = model.parse(args);
            I18N.init(result.getStringOrDefault("--lang", null));
            if (result.containsSwitch("help")) {
                if (!ClassUtils.isClassExists("org.apache.commons.io.IOUtils")
                        && !AddClassPath.tryToLoadMCLibrary("commons-io/commons-io")) {
                    logger.formattedInfo("error.libraries.io");
                } else
                    (logger = new DefaultConsoleLogger()).info(
                            IOUtils.toString(Objects.requireNonNull(ProgramMain.class.getResource("/assets/lang/helps/" + I18N.NOW + ".lang")),
                                    StandardCharsets.UTF_8));
            } else if (result.containsSwitch("compare"))
                CompareProgram.compareSimple(result);
            else if (result.containsSwitch("decompile"))
                DecompileProgram.decompileSimple(result);
            else if (result.containsSwitch("download"))
                MCFileDownloader.downloadMCFileSimple(result);
            else if (result.containsSwitch("asmifier"))
                SimpleASMifier.simpleAsmify(result);
            else
                RemapProgram.doSimpleRemap(result);
        } catch (CommandParseException e) {
            if (I18N.NOW == null)
                I18N.init(null);
            (logger = new DefaultConsoleLogger()).error(I18N.getText("command.illegal"), e);
        }
        logger.info(I18N.getText("time.used", System.currentTimeMillis() - time));
        DownloadService.stopExecutors();
    }

    private static CommandModel getComparatorModel() {
        CommandModel model = new CommandModel();
        UnorderSwitchTable table = new UnorderSwitchTable();
        model.switches.add(table);
        table.addSwitch("--lang", new StringArgumentSwitch("--lang"));
        table.addLiteral(new LiteralSwitch("-D", true));// Detail Output
        table.addLiteral(new LiteralSwitch("-Rs", true));// Resource Compare
        model.switches.add(new StringSwitch("old_version"));
        model.switches.add(new StringSwitch("new_version"));
        return model;
    }

    private static CommandModel getDecompilerModel() {
        CommandModel model = new CommandModel();
        UnorderSwitchTable table = new UnorderSwitchTable();
        model.switches.add(table);
        table.addSwitch("--lang", new StringArgumentSwitch("--lang"));
        table.addSwitch("--backend", new StringArgumentSwitch("--backend"));
        table.addSwitch("--indentTimes", new StringArgumentSwitch("--indentTimes"));
        table.addSwitch("--output", new StringArgumentSwitch("--output"));// Output File
        table.addLiteral(new LiteralSwitch("-D", true));// Detail Output
        table.addLiteral(new LiteralSwitch("-Ni", true));// No Infos
        table.addLiteral(new LiteralSwitch("-Ro", true));// Resource Output
        model.switches.add(new StringSwitch("source_file"));
        return model;
    }

    private static CommandModel getDownloaderModel() {
        CommandModel model = new CommandModel();
        UnorderSwitchTable table = new UnorderSwitchTable();
        model.switches.add(table);
        table.addSwitch("--lang", new StringArgumentSwitch("--lang"));
        table.addSwitch("--dir", new StringArgumentSwitch("--dir"));
        table.addLiteral(new LiteralSwitch("-D", true));// Detail Output
        model.switches.add(new StringSwitch("version"));
        return model;
    }

    private static CommandModel getASMifierModel(){
        CommandModel model = new CommandModel();
        UnorderSwitchTable table = new UnorderSwitchTable();
        model.switches.add(table);
        table.addSwitch("--lang", new StringArgumentSwitch("--lang"));
        table.addSwitch("--output", new StringArgumentSwitch("--output"));
        model.switches.add(new StringSwitch("classURL"));
        return model;
    }

    private static CommandModel getRemapperModel() {
        CommandModel model = new CommandModel();
        UnorderSwitchTable table = new UnorderSwitchTable();
        model.switches.add(table);
        table.addSwitch("--lang", new StringArgumentSwitch("--lang"));
        table.addLiteral(new LiteralSwitch("-Nl", true));
        table.addLiteral(new LiteralSwitch("-Nh", true));// No hacks
        table.addLiteral(new LiteralSwitch("-D", true));// Detail Output
        table.addLiteral(new LiteralSwitch("-Dy", true));// Dry Run
        table.addSwitch("--output", new StringArgumentSwitch("--output"));// Output File
        table.addSwitch("--outmap", new StringArgumentSwitch("--outmap"));// Output Formatted Output Map File
        table.addSwitch("--outrev", new StringArgumentSwitch("--outrev"));// Output Formatted Reverse Output Map File
        // (For vanilla version mapping)
        model.switches.add(new StringSwitch("mapping_url"));// URL of mapping
        model.switches.add(new StringSwitch("mc_file"));// Minecraft Main JAR
        return model;
    }
}
