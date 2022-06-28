package io.github.nickid2018.asmifier;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

import java.lang.reflect.Field;

public class MethodOpcodes {

    public static final String[] OPCODES_TABLE = new String[256];
    public static final String[] FRAME_LOCAL = new String[]{"F_NEW", "F_FULL", "F_APPEND", "F_CHOP", "F_SAME", "F_SAME1"};
    public static final String[] FRAME_STACK = new String[]{"TOP", "INTEGER", "FLOAT", "DOUBLE", "LONG", "NULL", "UNINITIALIZED_THIS"};
    public static final String[] HANDLE_TYPES = new String[]{
            "H_GETFIELD", "H_GETSTATIC", "H_PUTFIELD", "H_PUTSTATIC", "H_INVOKEVIRTUAL", "H_INVOKESTATIC",
            "H_INVOKESPECIAL", "H_NEWINVOKESPECIAL", "H_INVOKEINTERFACE"};
    public static final String[] ARRAY_TYPES = new String[]{
            "T_BOOLEAN", "T_CHAR", "T_FLOAT", "T_DOUBLE", "T_BYTE", "T_SHORT", "T_INT", "T_LONG"};

    public static String getAsFrameType(int obj) {
        return FRAME_LOCAL[obj + 1];
    }

    public static String getAsFrameObject(Object obj, MethodAnalyzer analyzer) {
        if (obj == null)
            return "null";
        if (obj instanceof String)
            return "\"" + obj + "\"";
        if(obj instanceof Label)
            return "l" + analyzer.checkLabel((Label)obj);
        return FRAME_STACK[(int) obj];
    }

    public static String getAsHandleType(int tag, ASMifier asmifier) {
        return asmifier.noConvertConstants ? tag + "" : HANDLE_TYPES[tag - 1];
    }

    public static String getAsArrayType(int type) {
        return ARRAY_TYPES[type - 4];
    }

    static {
        Class<?> OpcodeClass = Opcodes.class;
        for (Field field : OpcodeClass.getDeclaredFields()) {
            String name = field.getName();
            if (name.startsWith("ASM")
                    || name.startsWith("V")
                    || name.startsWith("T_")
                    || name.startsWith("F_")
                    || name.startsWith("H_")
                    || name.startsWith("ACC_")
                    || name.startsWith("SOURCE_")
                    || !field.getType().equals(int.class))
                continue;
            try {
                OPCODES_TABLE[field.getInt(null)] = name;
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Cannot put constants at " + name + "! Check the asm library version!", e);
            }
        }
    }
}
