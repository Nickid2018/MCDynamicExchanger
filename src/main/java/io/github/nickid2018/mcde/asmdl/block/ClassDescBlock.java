package io.github.nickid2018.mcde.asmdl.block;

import io.github.nickid2018.mcde.asmdl.ASMDLSyntaxException;
import io.github.nickid2018.mcde.asmdl.DescBlock;
import io.github.nickid2018.mcde.asmdl.DescFunctionContext;
import org.objectweb.asm.ClassWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.objectweb.asm.Opcodes.*;

public class ClassDescBlock extends DescBlock {

    public ClassDescBlock() {
        super("class");
    }

    public static final Map<String, Integer> ACCESS_FLAGS;

    static {
        ACCESS_FLAGS = new HashMap<>();
        ACCESS_FLAGS.put("public", ACC_PUBLIC);
        ACCESS_FLAGS.put("private", ACC_PRIVATE);
        ACCESS_FLAGS.put("protected", ACC_PROTECTED);
        ACCESS_FLAGS.put("final", ACC_FINAL);
        ACCESS_FLAGS.put("super", ACC_SUPER);
        ACCESS_FLAGS.put("interface", ACC_INTERFACE);
        ACCESS_FLAGS.put("abstract", ACC_ABSTRACT);
        ACCESS_FLAGS.put("synthetic", ACC_SYNTHETIC);
        ACCESS_FLAGS.put("annotation", ACC_ANNOTATION);
        ACCESS_FLAGS.put("enum", ACC_ENUM);
        ACCESS_FLAGS.put("record", ACC_RECORD);
    }

    @Override
    // class <version> [access] <name> [extends <super>] [implements <interfaces...>] [signature <signature>]
    public ClassWriter processStart(DescFunctionContext context) throws ASMDLSyntaxException {
        if (context.environment() != null)
            throw new ASMDLSyntaxException("class environment shouldn't be defined in any environment");

        String[] args = context.args();

        if (args.length < 1)
            throw new ASMDLSyntaxException("class version is required");
        int version = Integer.parseInt(args[0]);
        version += 44;
        if (version < 49 || version > 64)
            throw new ASMDLSyntaxException("class version should be between 5(Java 1.5) and 20(Java 20)");

        if (args.length < 2)
            throw new ASMDLSyntaxException("class name is required");
        int pointer = 1;

        int access = 0;
        for (; ; pointer++) {
            if (pointer >= args.length)
                throw new ASMDLSyntaxException("class name is required");
            if (ACCESS_FLAGS.containsKey(args[pointer]))
                access += ACCESS_FLAGS.get(args[pointer]);
            else break;
        }

        String className = args[pointer++];

        String superName = "java/lang/Object";
        List<String> interfaces = new ArrayList<>();
        String signature = null;
        while (pointer < args.length) {
            switch (args[pointer]) {
                case "extends" -> {
                    if (pointer + 1 >= args.length)
                        throw new ASMDLSyntaxException("super class name is required");
                    superName = args[pointer + 1];
                    pointer += 2;
                }
                case "implements" -> {
                    if (pointer + 1 >= args.length)
                        throw new ASMDLSyntaxException("interface name is required");
                    interfaces = new ArrayList<>(List.of(args[pointer + 1].split(",")));
                    pointer += 2;
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

        ClassWriter writer = (ClassWriter) context.visitor();
        writer.visit(version, access, className, signature, superName, interfaces.toArray(String[]::new));

        return writer;
    }

    @Override
    public Object processEnd(DescFunctionContext context) {
        ((ClassWriter) context.visitor()).visitEnd();
        return null;
    }
}
