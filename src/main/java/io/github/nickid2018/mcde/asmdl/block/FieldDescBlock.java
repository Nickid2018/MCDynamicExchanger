package io.github.nickid2018.mcde.asmdl.block;

import io.github.nickid2018.mcde.asmdl.ASMDLSyntaxException;
import io.github.nickid2018.mcde.asmdl.DescBlock;
import io.github.nickid2018.mcde.asmdl.DescFunctionContext;
import io.github.nickid2018.mcde.asmdl.DescFunctions;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.objectweb.asm.Opcodes.*;

public class FieldDescBlock extends DescBlock {

    public FieldDescBlock() {
        super("field");
    }

    public static final Map<String, Integer> ACCESS_FLAGS;

    static {
        ACCESS_FLAGS = new HashMap<>();
        ACCESS_FLAGS.put("public", ACC_PUBLIC);
        ACCESS_FLAGS.put("private", ACC_PRIVATE);
        ACCESS_FLAGS.put("protected", ACC_PROTECTED);
        ACCESS_FLAGS.put("final", ACC_FINAL);
        ACCESS_FLAGS.put("synthetic", ACC_SYNTHETIC);
        ACCESS_FLAGS.put("enum", ACC_ENUM);
        ACCESS_FLAGS.put("volatile", ACC_VOLATILE);
        ACCESS_FLAGS.put("transient", ACC_TRANSIENT);
        ACCESS_FLAGS.put("static", ACC_STATIC);
    }

    @Override
    // field [access] <name> <desc> [signature <signature>] [value <value>]
    public FieldVisitor processStart(DescFunctionContext context) throws ASMDLSyntaxException {
        if (context.environment() != DescFunctions.CLASS)
            throw new ASMDLSyntaxException("field block must be in class block");
        int pointer = 0;

        String[] args = context.args();
        if (args.length < 1)
            throw new ASMDLSyntaxException("field name is required");

        int access = 0;
        for (; ; pointer++) {
            if (pointer >= args.length)
                throw new ASMDLSyntaxException("field name is required");
            if (ACCESS_FLAGS.containsKey(args[pointer]))
                access += ACCESS_FLAGS.get(args[pointer]);
            else break;
        }

        String name = args[pointer++];
        if (pointer >= args.length)
            throw new ASMDLSyntaxException("field descriptor is required");

        String desc = args[pointer++];
        String signature = null;
        Object value = null;

        while (pointer < args.length) {
            switch (args[pointer]) {
                case "value" -> {
                    // BILSCZFD + String
                    if ((access & ACC_STATIC) == 0 || (access & ACC_FINAL) == 0)
                        throw new ASMDLSyntaxException("value can be assigned only if the field is static and final");
                    switch (desc) {
                        case "B" -> value = Byte.parseByte(args[++pointer]);
                        case "I" -> value = Integer.parseInt(args[++pointer]);
                        case "L" -> value = Long.parseLong(args[++pointer]);
                        case "S" -> value = Short.parseShort(args[++pointer]);
                        case "C" -> value = args[++pointer].charAt(0);
                        case "Z" -> value = Boolean.parseBoolean(args[++pointer]);
                        case "F" -> value = Float.parseFloat(args[++pointer]);
                        case "D" -> value = Double.parseDouble(args[++pointer]);
                        case "Ljava/lang/String;" -> {
                            value = String.join(" ", Arrays.copyOfRange(args, ++pointer, args.length));
                            pointer = args.length;
                        }
                        default ->
                                throw new ASMDLSyntaxException("field with a non-primitive type cannot be assigned a value");
                    }
                    pointer++;
                }
                case "signature" -> {
                    if (pointer + 1 >= args.length)
                        throw new ASMDLSyntaxException("signature is required");
                    signature = args[pointer + 1];
                    pointer += 2;
                }
                default -> throw new ASMDLSyntaxException("unknown argument: " + args[pointer]);
            }
        }

        return ((ClassWriter) context.visitor()).visitField(access, name, desc, signature, value);
    }

    @Override
    public Object processEnd(DescFunctionContext context) throws ASMDLSyntaxException {
        if (context.environment() != DescFunctions.CLASS)
            throw new ASMDLSyntaxException("field block must be in class block");
        ((FieldVisitor) context.visitor()).visitEnd();
        return null;
    }
}
