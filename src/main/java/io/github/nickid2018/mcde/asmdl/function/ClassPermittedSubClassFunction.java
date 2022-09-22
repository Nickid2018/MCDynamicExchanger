package io.github.nickid2018.mcde.asmdl.function;

import io.github.nickid2018.mcde.asmdl.ASMDLSyntaxException;
import io.github.nickid2018.mcde.asmdl.DescFunction;
import io.github.nickid2018.mcde.asmdl.DescFunctionContext;
import io.github.nickid2018.mcde.asmdl.DescFunctions;
import org.objectweb.asm.ClassWriter;

public class ClassPermittedSubClassFunction extends DescFunction {

    public ClassPermittedSubClassFunction() {
        super("permitted");
    }

    @Override
    public void process(DescFunctionContext context) throws ASMDLSyntaxException {
        if (context.environment() != DescFunctions.CLASS)
            throw new ASMDLSyntaxException("permitted function must be in a class block");
        ClassWriter cw = (ClassWriter) context.visitor();
        String[] args = context.args();
        if (args.length != 1)
            throw new ASMDLSyntaxException("permitted requires 1 argument");
        cw.visitPermittedSubclass(args[0]);
    }
}
