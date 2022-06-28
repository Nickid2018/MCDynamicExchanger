package io.github.nickid2018.asmifier;

import io.github.nickid2018.DefaultConsoleLogger;
import io.github.nickid2018.I18N;
import io.github.nickid2018.argparser.CommandResult;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URL;

import static io.github.nickid2018.ProgramMain.logger;

public class SimpleASMifier {

    public static void simpleAsmify(CommandResult result){
        logger = new DefaultConsoleLogger();
        try {
            URL url = new URL(result.getSwitch("classURL").toString());
            ASMifier asmifier = new ASMifier(url.openConnection().getInputStream());
            asmifier.noVariableList = result.containsSwitch("-Nvl");
            asmifier.noLines = result.containsSwitch("-Nl");
            asmifier.noFrames = result.containsSwitch("-Nf");
            asmifier.noExtraCodes = result.containsSwitch("-Nec");
            asmifier.noConvertConstants = result.containsSwitch("-Ncc");
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
