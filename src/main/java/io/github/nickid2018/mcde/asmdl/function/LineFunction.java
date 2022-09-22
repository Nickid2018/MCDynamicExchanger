package io.github.nickid2018.mcde.asmdl.function;

import io.github.nickid2018.mcde.asmdl.ASMDLSyntaxException;
import io.github.nickid2018.mcde.asmdl.DescFunction;
import io.github.nickid2018.mcde.asmdl.DescFunctionContext;
import io.github.nickid2018.mcde.asmdl.DescFunctions;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

public class LineFunction extends DescFunction {

    public LineFunction() {
        super("line");
    }

    @Override
    public void process(DescFunctionContext context) throws ASMDLSyntaxException {
        if (context.environment() != DescFunctions.METHOD)
            throw new ASMDLSyntaxException("line can only be used in method");

        String[] args = context.args();
        if (args.length != 2)
            throw new ASMDLSyntaxException("line requires 2 argument");

        Label start = context.labelMap().computeIfAbsent(args[0], k -> new Label());

        try {
            ((MethodVisitor) context.visitor()).visitLineNumber(Integer.parseInt(args[1]), start);
        } catch (NumberFormatException e) {
            throw new ASMDLSyntaxException("invalid line number");
        }
    }
}
