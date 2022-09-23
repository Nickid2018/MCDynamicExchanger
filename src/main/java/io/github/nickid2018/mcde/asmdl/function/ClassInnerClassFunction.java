package io.github.nickid2018.mcde.asmdl.function;

import io.github.nickid2018.mcde.asmdl.ASMDLSyntaxException;
import io.github.nickid2018.mcde.asmdl.DescFunction;
import io.github.nickid2018.mcde.asmdl.DescFunctionContext;
import io.github.nickid2018.mcde.asmdl.DescFunctions;
import io.github.nickid2018.mcde.asmdl.block.ClassDescBlock;
import org.objectweb.asm.ClassWriter;

import java.util.HashMap;
import java.util.Map;

import static org.objectweb.asm.Opcodes.*;

public class ClassInnerClassFunction extends DescFunction {

    public ClassInnerClassFunction() {
        super("innerclass");
    }

    @Override
    public void process(DescFunctionContext context) throws ASMDLSyntaxException {
        if (context.environment() != DescFunctions.CLASS)
            throw new ASMDLSyntaxException("innerclass function must be in a class block");
        ClassWriter cw = (ClassWriter) context.visitor();
        String[] args = context.args();
        int pointer = 0;
        if (args.length < 1)
            throw new ASMDLSyntaxException("inner class name is required");

        int access = 0;
        for (;;pointer++) {
            if (pointer >= args.length)
                throw new ASMDLSyntaxException("inner class name is required");
            if (ClassDescBlock.ACCESS_FLAGS.containsKey(args[pointer]))
                access += ClassDescBlock.ACCESS_FLAGS.get(args[pointer]);
            else break;
        }

        String name = args[pointer++];
        if (pointer >= args.length) {
            cw.visitInnerClass(name, null, null, access);
            return;
        }
        String outerName = args[pointer++];
        if (pointer >= args.length) {
            cw.visitInnerClass(name, outerName, null, access);
            return;
        }
        String innerName = args[pointer];

        cw.visitInnerClass(name, outerName, innerName, access);
    }
}