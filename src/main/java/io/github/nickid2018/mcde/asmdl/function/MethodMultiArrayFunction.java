package io.github.nickid2018.mcde.asmdl.function;

import io.github.nickid2018.mcde.asmdl.ASMDLSyntaxException;
import io.github.nickid2018.mcde.asmdl.DescFunction;
import io.github.nickid2018.mcde.asmdl.DescFunctionContext;
import io.github.nickid2018.mcde.asmdl.DescFunctions;
import org.objectweb.asm.MethodVisitor;

public class MethodMultiArrayFunction extends DescFunction {

    public MethodMultiArrayFunction() {
        super("multianewarray");
    }

    @Override
    public void process(DescFunctionContext context) throws ASMDLSyntaxException {
        if (context.environment() != DescFunctions.METHOD && context.environment() != DescFunctions.LABEL)
            throw new ASMDLSyntaxException("multianewarray function must be in a method block or a label block");

        String[] args = context.args();
        if (args.length != 2)
            throw new ASMDLSyntaxException("multianewarray function requires two arguments");

        String desc = args[0];
        int dims;
        try {
            dims = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            throw new ASMDLSyntaxException("invalid dimension");
        }

        MethodVisitor mv = (MethodVisitor) context.visitor();
        mv.visitMultiANewArrayInsn(desc, dims);
    }
}
