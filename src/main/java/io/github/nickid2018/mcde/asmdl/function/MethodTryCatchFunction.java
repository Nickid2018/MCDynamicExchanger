package io.github.nickid2018.mcde.asmdl.function;

import io.github.nickid2018.mcde.asmdl.ASMDLSyntaxException;
import io.github.nickid2018.mcde.asmdl.DescFunction;
import io.github.nickid2018.mcde.asmdl.DescFunctionContext;
import io.github.nickid2018.mcde.asmdl.DescFunctions;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

public class MethodTryCatchFunction extends DescFunction {

    public MethodTryCatchFunction() {
        super("try_catch");
    }

    @Override
    public void process(DescFunctionContext context) throws ASMDLSyntaxException {
        if (context.environment() != DescFunctions.METHOD)
            throw new ASMDLSyntaxException("try_catch block must be in a method block");
        String[] args = context.args();
        if (args.length != 4)
            throw new ASMDLSyntaxException("try_catch requires 4 arguments");

        Label start = context.labelMap().computeIfAbsent(context.args()[0], k -> new Label());
        Label end = context.labelMap().computeIfAbsent(context.args()[1], k -> new Label());
        Label handler = context.labelMap().computeIfAbsent(context.args()[2], k -> new Label());

        ((MethodVisitor) context.visitor()).visitTryCatchBlock(start, end, handler, args[3]);
    }
}
