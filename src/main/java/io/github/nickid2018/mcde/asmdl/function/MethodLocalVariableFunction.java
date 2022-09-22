package io.github.nickid2018.mcde.asmdl.function;

import io.github.nickid2018.mcde.asmdl.ASMDLSyntaxException;
import io.github.nickid2018.mcde.asmdl.DescFunction;
import io.github.nickid2018.mcde.asmdl.DescFunctionContext;
import io.github.nickid2018.mcde.asmdl.DescFunctions;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

public class MethodLocalVariableFunction extends DescFunction {

    public MethodLocalVariableFunction() {
        super("local");
    }

    @Override
    public void process(DescFunctionContext context) throws ASMDLSyntaxException {
        if (context.environment() != DescFunctions.METHOD && context.environment() != DescFunctions.LABEL)
            throw new ASMDLSyntaxException("line can only be used in method or label");

        String[] args = context.args();
        if (args.length != 5 && args.length != 6)
            throw new ASMDLSyntaxException("local requires 5 or 6 argument");

        String name = args[3];
        String desc = args[4];
        Label start = context.labelMap().computeIfAbsent(args[0], k -> new Label());
        Label end = context.labelMap().computeIfAbsent(args[1], k -> new Label());
        try {
            int index = Integer.parseInt(args[2]);
            String signature = args.length == 6 ? args[5] : null;
            ((MethodVisitor) context.visitor()).visitLocalVariable(name, desc, signature, start, end, index);
        } catch (NumberFormatException e) {
            throw new ASMDLSyntaxException("invalid index");
        }
    }
}