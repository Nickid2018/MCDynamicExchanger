package io.github.nickid2018.decompile.cfr;

import io.github.nickid2018.I18N;
import io.github.nickid2018.ProgramMain;
import io.github.nickid2018.argparser.CommandResult;
import io.github.nickid2018.decompile.Decompiler;
import org.apache.commons.io.IOUtils;
import org.benf.cfr.reader.api.CfrDriver;
import org.benf.cfr.reader.bytecode.analysis.parse.utils.Pair;
import org.benf.cfr.reader.util.getopt.GetOptParser;
import org.benf.cfr.reader.util.getopt.Options;
import org.benf.cfr.reader.util.getopt.OptionsImpl;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class CFRDecompiler implements Decompiler {

    private final String[] args;

    public CFRDecompiler(String[] args) {
        this.args = new String[args.length + 3];
        this.args[0] = "";
        this.args[1] = "--comments";
        this.args[2] = "false";
        System.arraycopy(args, 0, this.args, 3, args.length);
    }

    private final ExecutorService executors = Executors.newFixedThreadPool(8);
    private final Queue<Pair<String, String>> queueToWrite = new ConcurrentLinkedDeque<>();
    private final Queue<String> queueToAnalyze = new ConcurrentLinkedDeque<>();

    @Override
    public void doDecompileSimple(CommandResult result) {
        GetOptParser getOptParser = new GetOptParser();
        Options options;
        try {
            Pair<List<String>, Options> processedArgs = getOptParser.parse(args, OptionsImpl.getFactory());
            options = processedArgs.getSecond();
        } catch (Exception e) {
            getOptParser.showHelp(e);
            System.exit(1);
            return;
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
        try {
            ZipFile file = new ZipFile(sourceJar);

            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(to));
            Enumeration<? extends ZipEntry> entries = file.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.isDirectory())
                    continue;
                String name = entry.getName();
                if (!name.endsWith(".class")) {
                    if (resourceOutput) {
                        zos.putNextEntry(entry);
                        IOUtils.copy(file.getInputStream(entry), zos);
                        zos.closeEntry();
                        if (detailed)
                            ProgramMain.logger.info(I18N.getText("decompile.resource", entry.getName()));
                    }
                    continue;
                }
                if (name.contains("$"))
                    continue;
                queueToAnalyze.offer(name);
            }
            DecompileClassFileSource filesource = new DecompileClassFileSource(file);
            for (int i = 0; i < 8; i++) {
                executors.execute(() -> {
                    DecompileSinkFactory sink = new DecompileSinkFactory(noInfos, Integer.parseInt(result.getStringOrDefault("--indentTimes", "4")));
                    CfrDriver driver = new CfrDriver.Builder().withOverrideClassFileSource(filesource).withOutputSink(sink)
                            .withBuiltOptions(options).build();
                    List<String> argSupplier = new ArrayList<>(1);
                    argSupplier.add("");
                    String name;
                    while ((name = queueToAnalyze.poll()) != null) {
                        argSupplier.set(0, name);
                        sink.startNextFile(name, detailed);
                        try {
                            driver.analyse(argSupplier);
                            queueToWrite.offer(new Pair<>(name.split("\\.")[0] + ".java", sink.getInfo()));
                        } catch (Throwable e) {
                            System.err.println("In " + name + ":");
                            e.printStackTrace();
                            StringWriter writer = new StringWriter();
                            e.printStackTrace(new PrintWriter(writer));
                            queueToWrite.offer(new Pair<>(name.split("\\.")[0] + ".java", writer.toString()));
                        }
                    }
                });
            }
            executors.shutdown();
            while(!executors.isTerminated() || !queueToWrite.isEmpty()) {
                Pair<String, String> info = queueToWrite.poll();
                if (info != null) {
                    zos.putNextEntry(new ZipEntry(info.getFirst()));
                    zos.write(info.getSecond().getBytes(StandardCharsets.UTF_8));
                    zos.closeEntry();
                }
            }
            zos.close();
        } catch (Throwable e) {
            ProgramMain.logger.error(I18N.getText("error.unknown"), e);
        }
    }
}
