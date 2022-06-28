package io.github.nickid2018.decompile;

import io.github.nickid2018.I18N;
import io.github.nickid2018.argparser.CommandResult;
import io.github.nickid2018.decompile.cfr.CFRDecompiler;

public interface Decompiler {

    static Decompiler getInstance(String arg) {
        String[] splited = arg.split(":");
        switch (splited[0]) {
            case "cfr":
                String[] args = splited.length == 1 ? new String[0] : splited[1].split(",");
                for (int i = 0; i < args.length; i++) {
                    args[i] = args[i].replace('+', ' ');
                }
                return new CFRDecompiler(args);
            default:
                throw new UnsupportedOperationException(I18N.getText("error.decompile.unsupport", splited[0]));
        }
    }

    void doDecompileSimple(CommandResult res);
}
