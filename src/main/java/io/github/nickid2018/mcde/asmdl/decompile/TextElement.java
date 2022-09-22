package io.github.nickid2018.mcde.asmdl.decompile;

import org.objectweb.asm.ConstantDynamic;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;

import java.io.StringWriter;
import java.util.HexFormat;
import java.util.Map;

public class TextElement {

    public static final String INDENT = "    ";

    protected final String text;

    public TextElement(String text) {
        this.text = text;
    }

    public void append(StringWriter writer, int indent) {
        writer.append(INDENT.repeat(indent)).append(text).append("\n");
    }

    public static final Map<Integer, String> HANDLE_TYPES = Map.of(
            Opcodes.H_GETFIELD, "getfield",
            Opcodes.H_GETSTATIC, "getstatic",
            Opcodes.H_PUTFIELD, "putfield",
            Opcodes.H_PUTSTATIC, "putstatic",
            Opcodes.H_INVOKEINTERFACE, "invokeinterface",
            Opcodes.H_INVOKEVIRTUAL, "invokevirtual",
            Opcodes.H_INVOKESPECIAL, "invokespecial",
            Opcodes.H_NEWINVOKESPECIAL, "newinvokespecial",
            Opcodes.H_INVOKESTATIC, "invokestatic"
    );

    // Properties.saveConvert
    public static String encode(String data) {
        int len = data.length();
        int bufLen = len * 2;
        if (bufLen < 0) {
            bufLen = Integer.MAX_VALUE;
        }
        StringBuilder outBuffer = new StringBuilder(bufLen);
        HexFormat hex = HexFormat.of().withUpperCase();
        for (int x = 0; x < len; x++) {
            char aChar = data.charAt(x);
            if ((aChar > 61) && (aChar < 127)) {
                if (aChar == '\\') {
                    outBuffer.append('\\');
                    outBuffer.append('\\');
                    continue;
                }
                outBuffer.append(aChar);
                continue;
            }
            switch (aChar) {
                case ' ' -> {
                    if (x == 0)
                        outBuffer.append('\\');
                    outBuffer.append(' ');
                }
                case '\t' -> {
                    outBuffer.append('\\');
                    outBuffer.append('t');
                }
                case '\n' -> {
                    outBuffer.append('\\');
                    outBuffer.append('n');
                }
                case '\r' -> {
                    outBuffer.append('\\');
                    outBuffer.append('r');
                }
                case '\f' -> {
                    outBuffer.append('\\');
                    outBuffer.append('f');
                }
                case '=', ':', '#', '!' -> {
                    outBuffer.append('\\');
                    outBuffer.append(aChar);
                }
                default -> {
                    if (((aChar < 0x0020) || (aChar > 0x007e))) {
                        outBuffer.append("\\u");
                        outBuffer.append(hex.toHexDigits(aChar));
                    } else {
                        outBuffer.append(aChar);
                    }
                }
            }
        }
        return outBuffer.toString();
    }

    public static String getPlainValue(Object value) {
        return switch (value.getClass().getSimpleName()) {
            case "String" -> "string %s".formatted(encode((String) value));
            case "Character" -> "char %c".formatted((char) value);
            case "Byte" -> "byte %d".formatted((byte) value);
            case "Short" -> "short %d".formatted((short) value);
            case "Integer" -> "int %d".formatted((int) value);
            case "Long" -> "long %d".formatted((long) value);
            case "Float" -> "float %f".formatted((float) value);
            case "Double" -> "double %f".formatted((double) value);
            case "Boolean" -> "boolean %b".formatted(value);
            case "Type" -> "type %s".formatted(value);
            case "Handle" -> {
                Handle handle = (Handle) value;
                yield "handle %s".formatted(getHandleValue(handle));
            }
            default ->
                    throw new IllegalArgumentException("Unknown value type: %s".formatted(value.getClass().getSimpleName()));
        };
    }

    public static String getHandleValue(Handle handle) {
        return "%s %s.%s%s".formatted(HANDLE_TYPES.get(handle.getTag()), handle.getOwner(), handle.getName(), handle.getDesc());
    }

    public static TextBlockElement getConstantDynamicValue(String name, ConstantDynamic value) {
        String base = "%s %s %s".formatted(name, value.getName(), value.getDescriptor());
        TextBlockElement element = new TextBlockElement(base);
        element.addElement(new TextElement("bootstrap %s".formatted(getHandleValue(value.getBootstrapMethod()))));
        for (int i = 0; i < value.getBootstrapMethodArgumentCount(); i++) {
            Object data = value.getBootstrapMethodArgument(i);
            if (data instanceof ConstantDynamic)
                element.addElement(getConstantDynamicValue("dynamic", (ConstantDynamic) data));
            else
                element.addElement(new TextElement("constant %s".formatted(getPlainValue(data))));
        }
        return element;
    }
}
