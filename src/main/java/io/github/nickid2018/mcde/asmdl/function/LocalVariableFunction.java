package io.github.nickid2018.mcde.asmdl.function;

import io.github.nickid2018.mcde.asmdl.ASMDLSyntaxException;
import io.github.nickid2018.mcde.asmdl.DescFunction;
import io.github.nickid2018.mcde.asmdl.DescFunctionContext;
import io.github.nickid2018.mcde.asmdl.DescFunctions;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

public class LocalVariableFunction extends DescFunction {

    public LocalVariableFunction() {
        super("local");
    }

    @Override
    public void process(DescFunctionContext context) throws ASMDLSyntaxException {
        if (context.environment() != DescFunctions.METHOD)
            throw new ASMDLSyntaxException("line can only be used in method");

        String[] args = context.args();
        if (args.length != 6)
            throw new ASMDLSyntaxException("local requires 6 argument");

        String name = args[0];
        String desc = args[1];
        String signature = args[2];
        Label start = context.labelMap().computeIfAbsent(args[3], k -> new Label());
        Label end = context.labelMap().computeIfAbsent(args[4], k -> new Label());
        try {
            int index = Integer.parseInt(args[5]);
            ((MethodVisitor) context.visitor()).visitLocalVariable(name, desc, signature, start, end, index);
        } catch (NumberFormatException e) {
            throw new ASMDLSyntaxException("invalid index");
        }
    }
}