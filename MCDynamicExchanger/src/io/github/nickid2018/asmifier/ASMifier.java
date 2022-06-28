package io.github.nickid2018.asmifier;

import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

public class ASMifier {

    private final ClassReader reader;

    public boolean noVariableList = false;
    public boolean noFrames = false;
    public boolean noExtraCodes = false;
    public boolean noLines = false;
    public boolean noConvertConstants = false;

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
        line("ClassWriter cw = new ClassWriter(%s);", noFrames ? "ClassWriter.COMPUTE_FRAMES" : 0);
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
