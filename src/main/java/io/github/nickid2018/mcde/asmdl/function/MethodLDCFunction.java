package io.github.nickid2018.mcde.asmdl.function;

import io.github.nickid2018.mcde.asmdl.ASMDLSyntaxException;
import io.github.nickid2018.mcde.asmdl.DescFunction;
import io.github.nickid2018.mcde.asmdl.DescFunctionContext;
import io.github.nickid2018.mcde.asmdl.DescFunctions;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.util.Arrays;

public class MethodLDCFunction extends DescFunction {

    public MethodLDCFunction() {
        super("ldc");
    }

    // Properties.loadConvert
    public static String decode(String data) {
        int off = 0;
        int len = data.length();
        char[] in = data.toCharArray();
        StringBuilder out = new StringBuilder(len);
        char aChar;
        int end = off + len;
        int start = off;
        while (off < end) {
            aChar = in[off++];
            if (aChar == '\\')
                break;
        }
        if (off == end)
            return new String(in, start, len);
        out.setLength(0);
        off--;
        out.append(in, start, off - start);
        while (off < end) {
            aChar = in[off++];
            if (aChar == '\\') {
                aChar = in[off++];
                if (aChar == 'u') {
                    if (off > end - 4)
                        throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = in[off++];
                        value = switch (aChar) {
                            case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> (value << 4) + aChar - '0';
                            case 'a', 'b', 'c', 'd', 'e', 'f' -> (value << 4) + 10 + aChar - 'a';
                            case 'A', 'B', 'C', 'D', 'E', 'F' -> (value << 4) + 10 + aChar - 'A';
                            default -> throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
                        };
                    }
                    out.append((char) value);
                } else {
                    if (aChar == 't') aChar = '\t';
                    else if (aChar == 'r') aChar = '\r';
                    else if (aChar == 'n') aChar = '\n';
                    else if (aChar == 'f') aChar = '\f';
                    out.append(aChar);
                }
            } else {
                out.append(aChar);
            }
        }
        return out.toString();
    }

    public static Object formatLDC(String[] args) throws ASMDLSyntaxException {
        return switch (args[0]) {
            case "int" -> Integer.parseInt(args[1]);
            case "long" -> Long.parseLong(args[1]);
            case "float" -> Float.parseFloat(args[1]);
            case "double" -> Double.parseDouble(args[1]);
            case "short" -> Short.parseShort(args[1]);
            case "byte" -> Byte.parseByte(args[1]);
            case "char" -> args[1].charAt(0);
            case "boolean" -> Boolean.parseBoolean(args[1]);
            case "string" -> decode(String.join(" ", Arrays.copyOfRange(args, 1, args.length)));
            case "type" -> Type.getType(args[1]);
            case "handle" -> HandleFunction.formatAsHandle(args[1], args[2], Boolean.parseBoolean(args[3]));
            default -> throw new ASMDLSyntaxException("invalid constant type");
        };
    }

    @Override
    public void process(DescFunctionContext context) throws ASMDLSyntaxException {
        if (context.environment() != DescFunctions.METHOD && context.environment() != DescFunctions.LABEL)
            throw new ASMDLSyntaxException("ldc function must be in a method block or a label block");

        String[] args = context.args();
        MethodVisitor mv = (MethodVisitor) context.visitor();
        mv.visitLdcInsn(formatLDC(args));
    }
}
