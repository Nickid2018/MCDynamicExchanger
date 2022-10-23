package io.github.nickid2018.mcde.asmdl.decompile;

import org.objectweb.asm.ConstantDynamic;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.TypeReference;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.Map;

public class TextElement {

    public static final Map<Integer, String> TYPE_REFERENCE_KINDS;

    static {
        TYPE_REFERENCE_KINDS = new HashMap<>();
        TYPE_REFERENCE_KINDS.put(TypeReference.CLASS_TYPE_PARAMETER, "class_type_parameter");
        TYPE_REFERENCE_KINDS.put(TypeReference.METHOD_TYPE_PARAMETER, "method_type_parameter");
        TYPE_REFERENCE_KINDS.put(TypeReference.CLASS_EXTENDS, "class_extends");
        TYPE_REFERENCE_KINDS.put(TypeReference.CLASS_TYPE_PARAMETER_BOUND, "class_type_parameter_bound");
        TYPE_REFERENCE_KINDS.put(TypeReference.METHOD_TYPE_PARAMETER_BOUND, "method_type_parameter_bound");
        TYPE_REFERENCE_KINDS.put(TypeReference.FIELD, "field");
        TYPE_REFERENCE_KINDS.put(TypeReference.METHOD_RETURN, "method_return");
        TYPE_REFERENCE_KINDS.put(TypeReference.METHOD_RECEIVER, "method_receiver");
        TYPE_REFERENCE_KINDS.put(TypeReference.METHOD_FORMAL_PARAMETER, "method_formal_parameter");
        TYPE_REFERENCE_KINDS.put(TypeReference.THROWS, "throws");
        TYPE_REFERENCE_KINDS.put(TypeReference.LOCAL_VARIABLE, "local_variable");
        TYPE_REFERENCE_KINDS.put(TypeReference.RESOURCE_VARIABLE, "resource_variable");
        TYPE_REFERENCE_KINDS.put(TypeReference.EXCEPTION_PARAMETER, "exception_parameter");
        TYPE_REFERENCE_KINDS.put(TypeReference.INSTANCEOF, "instanceof");
        TYPE_REFERENCE_KINDS.put(TypeReference.NEW, "new");
        TYPE_REFERENCE_KINDS.put(TypeReference.CONSTRUCTOR_REFERENCE, "constructor_reference");
        TYPE_REFERENCE_KINDS.put(TypeReference.METHOD_REFERENCE, "method_reference");
        TYPE_REFERENCE_KINDS.put(TypeReference.CAST, "cast");
        TYPE_REFERENCE_KINDS.put(TypeReference.CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT, "constructor_invocation_type_argument");
        TYPE_REFERENCE_KINDS.put(TypeReference.METHOD_INVOCATION_TYPE_ARGUMENT, "method_invocation_type_argument");
        TYPE_REFERENCE_KINDS.put(TypeReference.CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT, "constructor_reference_type_argument");
        TYPE_REFERENCE_KINDS.put(TypeReference.METHOD_REFERENCE_TYPE_ARGUMENT, "method_reference_type_argument");
    }

    public static final String INDENT = "    ";

    protected final String text;

    public TextElement(String text) {
        this.text = text;
    }

    public void append(StringWriter writer, int indent) {
        writer.append(INDENT.repeat(indent)).append(text).append("\n");
    }

    public static final Map<Integer, String> HANDLE_TYPES = Map.of(Opcodes.H_GETFIELD, "getfield", Opcodes.H_GETSTATIC, "getstatic", Opcodes.H_PUTFIELD, "putfield", Opcodes.H_PUTSTATIC, "putstatic", Opcodes.H_INVOKEINTERFACE, "invokeinterface", Opcodes.H_INVOKEVIRTUAL, "invokevirtual", Opcodes.H_INVOKESPECIAL, "invokespecial", Opcodes.H_NEWINVOKESPECIAL, "newinvokespecial", Opcodes.H_INVOKESTATIC, "invokestatic");

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
                    if (x == 0) outBuffer.append('\\');
                    outBuffer.append("\\u0020");
                }
                case '#' -> {
                    outBuffer.append("\\u0023");
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
                case '=', ':', '!' -> {
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
        return "%s %s.%s%s %s".formatted(HANDLE_TYPES.get(handle.getTag()), handle.getOwner(), handle.getName(), handle.getDesc(), handle.isInterface());
    }

    public static TextBlockElement getConstantDynamicValue(String name, ConstantDynamic value) {
        String base = "%s %s %s".formatted(name, value.getName(), value.getDescriptor());
        TextBlockElement element = new TextBlockElement(base);
        element.addElement(new TextElement("bootstrap %s".formatted(getHandleValue(value.getBootstrapMethod()))));
        for (int i = 0; i < value.getBootstrapMethodArgumentCount(); i++) {
            Object data = value.getBootstrapMethodArgument(i);
            if (data instanceof ConstantDynamic)
                element.addElement(getConstantDynamicValue("dynamic", (ConstantDynamic) data));
            else element.addElement(new TextElement("constant %s".formatted(getPlainValue(data))));
        }
        return element;
    }

    public static String getTypeReference(int typeRef) {
        TypeReference typeReference = new TypeReference(typeRef);
        String name = TYPE_REFERENCE_KINDS.get(typeReference.getSort());
        return switch (typeReference.getSort()) {
            case TypeReference.FIELD,
                    TypeReference.METHOD_RETURN,
                    TypeReference.METHOD_RECEIVER,
                    TypeReference.LOCAL_VARIABLE,
                    TypeReference.RESOURCE_VARIABLE,
                    TypeReference.INSTANCEOF,
                    TypeReference.NEW,
                    TypeReference.CONSTRUCTOR_REFERENCE,
                    TypeReference.METHOD_REFERENCE -> name;
            case TypeReference.CLASS_TYPE_PARAMETER,
                    TypeReference.METHOD_TYPE_PARAMETER ->
                    "%s,%d".formatted(name, typeReference.getTypeParameterIndex());
            case TypeReference.CLASS_TYPE_PARAMETER_BOUND,
                    TypeReference.METHOD_TYPE_PARAMETER_BOUND ->
                    "%s,%d,%d".formatted(name, typeReference.getTypeParameterIndex(), typeReference.getTypeParameterBoundIndex());
            case TypeReference.CLASS_EXTENDS -> "%s %s".formatted(name, typeReference.getSuperTypeIndex());
            case TypeReference.METHOD_FORMAL_PARAMETER -> "%s %d".formatted(name, typeReference.getFormalParameterIndex());
            case TypeReference.THROWS -> "%s %d".formatted(name, typeReference.getExceptionIndex());
            case TypeReference.EXCEPTION_PARAMETER ->
                    "%s,%d".formatted(name, typeReference.getTryCatchBlockIndex());
            case TypeReference.CAST,
                    TypeReference.CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT,
                    TypeReference.METHOD_INVOCATION_TYPE_ARGUMENT,
                    TypeReference.CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT,
                    TypeReference.METHOD_REFERENCE_TYPE_ARGUMENT ->
                    "%s,%d".formatted(name, typeReference.getTypeArgumentIndex());
            default -> throw new IllegalArgumentException("Unknown type reference: %d".formatted(typeRef));
        };
    }
}
