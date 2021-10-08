package io.github.nickid2018.asmifier;

import io.github.nickid2018.DefaultConsoleLogger;
import io.github.nickid2018.I18N;
import io.github.nickid2018.ProgramMain;
import io.github.nickid2018.argparser.CommandResult;
import io.github.nickid2018.util.AddClassPath;
import io.github.nickid2018.util.ClassUtils;
import io.github.nickid2018.util.download.DownloadService;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URL;

import static io.github.nickid2018.ProgramMain.logger;

public class SimpleASMifier {

    public static void simpleAsmify(CommandResult result){
        logger = new DefaultConsoleLogger();
        if (!ClassUtils.isClassExists("org.objectweb.asm.Opcodes"))
            DownloadService.downloadResource("asm", "https://repo1.maven.org/maven2/org/ow2/asm/asm/9.2/asm-9.2.jar",
                    "dynamicexchanger/libs/asm-9.2.jar");
        DownloadService.startDownloadInfoOutput();
        boolean failed = !DownloadService.waitDownloadOver();
        failed |= !DownloadService.FAILED_DOWNLOAD.isEmpty();
        DownloadService.stopDownloadInfoOutput();
        if (failed) {
            ProgramMain.logger.formattedInfo("error.libraries.lost");
            System.exit(-1);
        }
        AddClassPath.addClassPathInDirs("dynamicexchanger/libs");
        try {
            URL url = new URL(result.getSwitch("classURL").toString());
            ASMifier asmifier = new ASMifier(url.openConnection().getInputStream());
            PrintWriter writer = result.containsSwitch("--output")
                    ? new PrintWriter(new FileWriter(result.getSwitch("--output").toString()))
                    : new PrintWriter(System.out);
            asmifier.analyze(writer);
            writer.flush();
        } catch (Throwable e) {
            logger.error(I18N.getText("error.unknown"), e);
        }
    }
}
