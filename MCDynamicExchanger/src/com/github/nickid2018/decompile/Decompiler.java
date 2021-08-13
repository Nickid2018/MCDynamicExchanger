package com.github.nickid2018.decompile;

import com.github.nickid2018.I18N;
import com.github.nickid2018.argparser.CommandResult;
import com.github.nickid2018.decompile.cfr.CFRDecompiler;
import com.github.nickid2018.decompile.jdcore.JDCoreDecompiler;
import com.github.nickid2018.util.ClassUtils;

import static com.github.nickid2018.ProgramMain.logger;

public interface Decompiler {

    static Decompiler getInstance(String arg) {
        String[] splited = arg.split(":");
        switch (splited[0]) {
            case "jdcore":
                if (!ClassUtils.isClassExists("org.jd.core.v1.ClassFileToJavaSourceDecompiler")) {
                    logger.formattedInfo("error.libraries.jdcore");
                    return null;
                }
                return new JDCoreDecompiler();
            case "cfr":
                String[] args = splited.length == 1 ? new String[0] : splited[1].split(",");
                for (int i = 0; i < args.length; i++) {
                    args[i] = args[i].replace('+', ' ');
                }
                if (!ClassUtils.isClassExists("org.benf.cfr.reader.api.CfrDriver")) {
                    logger.formattedInfo("error.libraries.cfr");
                    return null;
                }
                return new CFRDecompiler(args);
            default:
                throw new UnsupportedOperationException(I18N.getText("error.decompile.unsupport", splited[0]));
        }
    }

    void doDecompileSimple(CommandResult res);
}
