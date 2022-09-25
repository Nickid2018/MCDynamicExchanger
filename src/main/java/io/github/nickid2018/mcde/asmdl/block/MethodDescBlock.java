package io.github.nickid2018.mcde.asmdl.block;

import io.github.nickid2018.mcde.asmdl.ASMDLSyntaxException;
import io.github.nickid2018.mcde.asmdl.DescBlock;
import io.github.nickid2018.mcde.asmdl.DescFunctionContext;
import io.github.nickid2018.mcde.asmdl.DescFunctions;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.util.HashMap;
import java.util.Map;

import static org.objectweb.asm.Opcodes.*;

public class MethodDescBlock extends DescBlock {

    public static final Map<String, Integer> ACCESS_FLAGS;

    static {
        ACCESS_FLAGS = new HashMap<>();
        ACCESS_FLAGS.put("public", ACC_PUBLIC);
        ACCESS_FLAGS.put("private", ACC_PRIVATE);
        ACCESS_FLAGS.put("protected", ACC_PROTECTED);
        ACCESS_FLAGS.put("final", ACC_FINAL);
        ACCESS_FLAGS.put("static", ACC_STATIC);
        ACCESS_FLAGS.put("synchronized", ACC_SYNCHRONIZED);
        ACCESS_FLAGS.put("bridge", ACC_BRIDGE);
        ACCESS_FLAGS.put("varargs", ACC_VARARGS);
        ACCESS_FLAGS.put("native", ACC_NATIVE);
        ACCESS_FLAGS.put("abstract", ACC_ABSTRACT);
        ACCESS_FLAGS.put("strict", ACC_STRICT);
        ACCESS_FLAGS.put("synthetic", ACC_SYNTHETIC);
    }

    public MethodDescBlock() {
        super("method");
    }

    @Override
    // method [access] <name> <desc> [signature <signature>] [throws <exceptions>]
    public MethodVisitor processStart(DescFunctionContext context) throws ASMDLSyntaxException {
        if (context.environment() != DescFunctions.CLASS)
            throw new ASMDLSyntaxException("method block must be in class block");
        int pointer = 0;

        String[] args = context.args();
        if (args.length < 1)
            throw new ASMDLSyntaxException("method name is required");

        int access = 0;
        for (; ; pointer++) {
            if (pointer >= args.length)
                throw new ASMDLSyntaxException("method name is required");
            if (ACCESS_FLAGS.containsKey(args[pointer]))
                access += ACCESS_FLAGS.get(args[pointer]);
            else break;
        }

        String name = args[pointer++];
        if (pointer >= args.length)
            throw new ASMDLSyntaxException("method descriptor is required");

        String desc = args[pointer++];
        String signature = null;
        String[] exceptions = null;

        while (pointer < args.length) {
            switch (args[pointer]) {
                case "signature" -> {
                    if (pointer + 1 >= args.length)
                        throw new ASMDLSyntaxException("signature is required");
                    signature = args[++pointer];
                }
                case "throws" -> {
                    if (pointer + 1 >= args.length)
                        throw new ASMDLSyntaxException("exceptions are required");
                    exceptions = args[++pointer].split(",");
                }
                default -> throw new ASMDLSyntaxException("unknown argument: " + args[pointer]);
            }
            pointer++;
        }

        return ((ClassWriter) context.visitor()).visitMethod(access, name, desc, signature, exceptions);
    }

    @Override
    public Object processEnd(DescFunctionContext context) throws ASMDLSyntaxException {
        if (context.environment() != DescFunctions.CLASS)
            throw new ASMDLSyntaxException("method block must be in class block");
        ((MethodVisitor) context.visitor()).visitMaxs(0, 0);
        ((MethodVisitor) context.visitor()).visitEnd();
        return null;
    }
}
