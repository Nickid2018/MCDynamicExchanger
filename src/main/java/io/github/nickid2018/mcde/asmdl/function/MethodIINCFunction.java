package io.github.nickid2018.mcde.asmdl.function;

import io.github.nickid2018.mcde.asmdl.ASMDLSyntaxException;
import io.github.nickid2018.mcde.asmdl.DescFunction;
import io.github.nickid2018.mcde.asmdl.DescFunctionContext;
import io.github.nickid2018.mcde.asmdl.DescFunctions;
import org.objectweb.asm.MethodVisitor;

public class MethodIINCFunction extends DescFunction<MethodVisitor> {

    public MethodIINCFunction() {
        super("iinc");
    }

    @Override
    public <F> MethodVisitor process(DescFunctionContext<F> context) throws ASMDLSyntaxException {
        if (context.environment() != DescFunctions.METHOD && context.environment() != DescFunctions.LABEL)
            throw new ASMDLSyntaxException("iinc function must be in a method block or a label block");

        String[] args = context.args();
        if (args.length != 2)
            throw new ASMDLSyntaxException("iinc function requires two arguments");

        int var = Integer.parseInt(args[0]);
        int increment = Integer.parseInt(args[1]);

        MethodVisitor mv = (MethodVisitor) context.visitor();
        mv.visitIincInsn(var, increment);
        return mv;
    }
}