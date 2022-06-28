package io.github.nickid2018.mcremap;

import io.github.nickid2018.DefaultConsoleLogger;
import io.github.nickid2018.I18N;
import io.github.nickid2018.argparser.CommandResult;
import io.github.nickid2018.ProgramMain;

public class RemapProgram {

    public static void doSimpleRemap(CommandResult result) {
        ProgramMain.logger = new DefaultConsoleLogger();
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
}
