package io.github.nickid2018.mcde.asmdl.decompile;

import org.objectweb.asm.Opcodes;

import java.util.Map;

public enum AccessFlags {

    CLASS(Map.of(
            Opcodes.ACC_FINAL, "final",
            Opcodes.ACC_SUPER, "super",
            Opcodes.ACC_INTERFACE, "interface",
            Opcodes.ACC_ABSTRACT, "abstract",
            Opcodes.ACC_SYNTHETIC, "synthetic",
            Opcodes.ACC_ANNOTATION, "annotation",
            Opcodes.ACC_ENUM, "enum",
            Opcodes.ACC_RECORD, "record"
    )),
    FIELD(Map.of(
            Opcodes.ACC_STATIC, "static",
            Opcodes.ACC_FINAL, "final",
            Opcodes.ACC_VOLATILE, "volatile",
            Opcodes.ACC_TRANSIENT, "transient",
            Opcodes.ACC_SYNTHETIC, "synthetic",
            Opcodes.ACC_ENUM, "enum"
    )),
    METHOD(Map.of(
            Opcodes.ACC_STATIC, "static",
            Opcodes.ACC_FINAL, "final",
            Opcodes.ACC_SYNCHRONIZED, "synchronized",
            Opcodes.ACC_BRIDGE, "bridge",
            Opcodes.ACC_VARARGS, "varargs",
            Opcodes.ACC_NATIVE, "native",
            Opcodes.ACC_ABSTRACT, "abstract",
            Opcodes.ACC_STRICT, "strict",
            Opcodes.ACC_SYNTHETIC, "synthetic"
    )),
    PARAMETER(Map.of(
            Opcodes.ACC_FINAL, "final",
            Opcodes.ACC_SYNTHETIC, "synthetic"
    ));

    public static final Map<Integer, String> ACCESS = Map.of(
            Opcodes.ACC_PUBLIC, "public",
            Opcodes.ACC_PRIVATE, "private",
            Opcodes.ACC_PROTECTED, "protected"
    );

    private final Map<Integer, String> flags;

    AccessFlags(Map<Integer, String> flags) {
        this.flags = flags;
    }

    public String getFlags(int access) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<Integer, String> entry : ACCESS.entrySet()) {
            if ((access & entry.getKey()) != 0)
                builder.append(entry.getValue()).append(' ');
        }
        for (Map.Entry<Integer, String> entry : flags.entrySet()) {
            if ((access & entry.getKey()) != 0)
                builder.append(entry.getValue()).append(' ');
        }
        return builder.toString().trim();
    }
}
