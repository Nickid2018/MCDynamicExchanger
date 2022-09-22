package io.github.nickid2018.mcde.asmdl.function;

import io.github.nickid2018.mcde.asmdl.ASMDLSyntaxException;
import io.github.nickid2018.mcde.asmdl.DescFunction;
import io.github.nickid2018.mcde.asmdl.DescFunctionContext;
import io.github.nickid2018.mcde.asmdl.DescFunctions;
import org.objectweb.asm.ClassWriter;

public class ClassOuterClassFunction extends DescFunction {

    public ClassOuterClassFunction() {
        super("outerclass");
    }

    @Override
    public void process(DescFunctionContext context) throws ASMDLSyntaxException {
        if (context.environment() != DescFunctions.CLASS)
            throw new ASMDLSyntaxException("outerclass function must be in a class block");
        ClassWriter cw = (ClassWriter) context.visitor();
        String[] args = context.args();
        if (args.length != 3)
            throw new ASMDLSyntaxException("outerclass requires 3 arguments");
        cw.visitOuterClass(args[0], args[1], args[2]);
    }
}
