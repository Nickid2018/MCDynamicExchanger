package com.github.nickid2018.decompile.cfr;

import com.github.nickid2018.I18N;
import com.github.nickid2018.argparser.CommandResult;
import com.github.nickid2018.decompile.Decompiler;
import org.apache.commons.io.IOUtils;
import org.benf.cfr.reader.api.CfrDriver;
import org.benf.cfr.reader.bytecode.analysis.parse.utils.Pair;
import org.benf.cfr.reader.util.getopt.GetOptParser;
import org.benf.cfr.reader.util.getopt.Options;
import org.benf.cfr.reader.util.getopt.OptionsImpl;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.github.nickid2018.ProgramMain.logger;

public class CFRDecompiler implements Decompiler {

    private final String[] args;

    public CFRDecompiler(String[] args) {
        this.args = new String[args.length + 3];
        this.args[0] = "";
        this.args[1] = "--comments";
        this.args[2] = "false";
        System.arraycopy(args, 0, this.args, 3, args.length);
    }

    @Override
    public void doDecompileSimple(CommandResult result) {
        GetOptParser getOptParser = new GetOptParser();
        Options options = null;
        try {
            Pair<List<String>, Options> processedArgs = getOptParser.parse(args, OptionsImpl.getFactory());
            options = processedArgs.getSecond();
        } catch (Exception e) {
            getOptParser.showHelp(e);
            System.exit(1);
        }
        if (options.optionIsSet(OptionsImpl.HELP)) {
            getOptParser.showOptionHelp(OptionsImpl.getFactory(), options, OptionsImpl.HELP);
            return;
        }
        String sourceJar = result.getSwitch("source_file").toString();
        String to = result.getStringOrDefault("--output", "decompiled.jar");
        boolean detailed = result.containsSwitch("-D");
        boolean noInfos = result.containsSwitch("-Ni");
        boolean resourceOutput = result.containsSwitch("-Ro");
        List<String> argSupplier = new ArrayList<>(1);
        argSupplier.add("");
        try {
            ZipFile file = new ZipFile(sourceJar);
            DecompileClassFileSource filesource = new DecompileClassFileSource(file);
            DecompileSinkFactory sink = new DecompileSinkFactory(to, noInfos, Integer.parseInt(result.getStringOrDefault("--indentTimes", "4")));
            CfrDriver driver = new CfrDriver.Builder().withOverrideClassFileSource(filesource).withOutputSink(sink)
                    .withBuiltOptions(options).build();
            Enumeration<? extends ZipEntry> entries = file.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.isDirectory())
                    continue;
                String name = entry.getName();
                if (!name.endsWith(".class")) {
                    if (resourceOutput) {
                        sink.getStream().putNextEntry(entry);
                        IOUtils.copy(file.getInputStream(entry), sink.getStream());
                        sink.getStream().closeEntry();
                        if (detailed)
                            logger.info(I18N.getText("decompile.resource", entry.getName()));
                    }
                    continue;
                }
                if (name.contains("$"))
                    continue;
                argSupplier.set(0, name);
                sink.startNextFile(name, detailed);
                driver.analyse(argSupplier);
            }
            sink.getStream().close();
        } catch (Throwable e) {
            logger.error(I18N.getText("error.unknown"), e);
        }
    }
}
