package io.github.nickid2018.mcremap;

import io.github.nickid2018.DefaultConsoleLogger;
import io.github.nickid2018.I18N;
import io.github.nickid2018.argparser.CommandResult;
import io.github.nickid2018.util.AddClassPath;
import io.github.nickid2018.util.ClassUtils;
import io.github.nickid2018.util.download.DownloadService;
import io.github.nickid2018.ProgramMain;

public class RemapProgram {

    public static void doSimpleRemap(CommandResult result) {
        ProgramMain.logger = new DefaultConsoleLogger();
        checkFiles();
        try {
            OfficalFormat format = new OfficalFormat(result);
            format.processInitMap(result.getSwitch("mapping_url").toString());
            ProgramMain.logger.formattedInfo("remap.readmap.over");
            ClassTreeGenerator generator = new ClassTreeGenerator();
            generator.runGenerate(result, format);
            if (result.containsSwitch("-Dy")) {
                ProgramMain.logger.formattedInfo("remap.dryrun");
                return;
            }
            if (!(result.containsSwitch("--outmap") || result.containsSwitch("--outrev"))) {
                FileRemapper remapper = new FileRemapper();
                remapper.remapAll(result, format);
            }
        } catch (Throwable e) {
            ProgramMain.logger.error(I18N.getText("error.unknown"), e);
        }
    }

    private static void checkFiles() {
        boolean failed = false;
        if (!ClassUtils.isClassExists("org.apache.commons.io.IOUtils")
                && !AddClassPath.tryToLoadMCLibrary("commons-io/commons-io")) {
            ProgramMain.logger.formattedInfo("error.libraries.io");
            failed = true;
        }
        if (!ClassUtils.isClassExists("org.objectweb.asm.Opcodes"))
            DownloadService.downloadResource("asm", "https://repo1.maven.org/maven2/org/ow2/asm/asm/9.2/asm-9.2.jar",
                    "dynamicexchanger/libs/asm-9.2.jar");
        if (!ClassUtils.isClassExists("org.objectweb.asm.commons.ClassRemapper"))
            DownloadService.downloadResource("asm-commons",
                    "https://repo1.maven.org/maven2/org/ow2/asm/asm-commons/9.2/asm-commons-9.2.jar",
                    "dynamicexchanger/libs/asm-commons-9.2.jar");
        if (!ClassUtils.isClassExists("org.objectweb.asm.tree.analysis.Analyzer"))
            DownloadService.downloadResource("asm-analysis",
                    "https://repo1.maven.org/maven2/org/ow2/asm/asm-analysis/9.2/asm-analysis-9.2.jar",
                    "dynamicexchanger/libs/asm-analysis-9.2.jar");
        if (!ClassUtils.isClassExists("org.objectweb.asm.tree.ClassNode"))
            DownloadService.downloadResource("asm-tree",
                    "https://repo1.maven.org/maven2/org/ow2/asm/asm-tree/9.2/asm-tree-9.2.jar",
                    "dynamicexchanger/libs/asm-tree-9.2.jar");
        DownloadService.startDownloadInfoOutput();
        failed |= !DownloadService.waitDownloadOver();
        failed |= !DownloadService.FAILED_DOWNLOAD.isEmpty();
        DownloadService.stopDownloadInfoOutput();
        if (failed) {
            ProgramMain.logger.formattedInfo("error.libraries.lost");
            System.exit(-1);
        }
        AddClassPath.addClassPathInDirs("dynamicexchanger/libs");
    }
}
