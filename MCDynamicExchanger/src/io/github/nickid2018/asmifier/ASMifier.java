package io.github.nickid2018.asmifier;

import io.github.nickid2018.DefaultConsoleLogger;
import io.github.nickid2018.I18N;
import io.github.nickid2018.argparser.CommandResult;
import io.github.nickid2018.util.ClassUtils;
import io.github.nickid2018.util.download.DownloadService;
import org.objectweb.asm.ClassReader;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;

import static io.github.nickid2018.ProgramMain.logger;

public class ASMifier {

    public static void simpleAsmify(CommandResult result){
        logger = new DefaultConsoleLogger();
        if (!ClassUtils.isClassExists("org.objectweb.asm.Opcodes"))
            DownloadService.downloadResource("asm", "https://repo1.maven.org/maven2/org/ow2/asm/asm/9.2/asm-9.2.jar",
                    "dynamicexchanger/libs/asm-9.2.jar");
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

    private final ClassReader reader;

    public ASMifier(InputStream stream) throws IOException {
        reader = new ClassReader(stream);
    }

    public static int indentWidth = 4;
    protected int indent = 0;
    protected PrintWriter output;

    public void analyze(PrintWriter output) {
        this.output = output;
        line("import org.objectweb.asm.*;");
        line("import static org.objectweb.asm.Opcodes.*;");
        line("");
        line("public class DumpClass {");
        indent++;
        line("public static byte[] dumpClass() {");
        indent++;
        line("ClassWriter cw = new ClassWriter(0);");
        reader.accept(new ClassAnalyzer(this), 0);
        line("return cw.toByteArray();");
        indent--;
        line("}");
        indent--;
        line("}");
    }

    protected String arrayToString(String[] strs) {
        if(strs == null || strs.length == 0)
            return "null";
        StringBuilder builder = new StringBuilder("new String[] {");
        builder.append(quote(strs[0]));
        for(int i = 1; i < strs.length; i++)
            builder.append(", ").append(quote(strs[i]));
        return builder.append("}").toString();
    }

    protected String uncountableToString(String... strs) {
        if(strs == null || strs.length == 0)
            return null;
        StringBuilder builder = new StringBuilder();
        builder.append(quote(strs[0]));
        for(int i = 1; i < strs.length; i++)
            builder.append(", ").append(quote(strs[i]));
        return builder.toString();
    }

    protected String quote(String str) {
        return str == null ? "null" : "\"" + str + "\"";
    }

    protected void line(String info, Object... args) {
        for(int i = 0; i < indentWidth * indent; i++)
            output.write(' ');
        output.printf(info, args);
        output.println();
    }
}
