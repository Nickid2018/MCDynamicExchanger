package io.github.nickid2018.mcde.asmdl.function;

import io.github.nickid2018.mcde.asmdl.ASMDLSyntaxException;
import io.github.nickid2018.mcde.asmdl.DescFunction;
import io.github.nickid2018.mcde.asmdl.DescFunctionContext;
import io.github.nickid2018.mcde.asmdl.DescFunctions;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import java.util.stream.Stream;

public class MethodTableSwitchFunction extends DescFunction {

    public MethodTableSwitchFunction() {
        super("tableswitch");
    }

    @Override
    public void process(DescFunctionContext context) throws ASMDLSyntaxException {
        if (context.environment() != DescFunctions.METHOD && context.environment() != DescFunctions.LABEL)
            throw new ASMDLSyntaxException("tableswitch function must be in a method block or a label block");

        MethodVisitor mv = (MethodVisitor) context.visitor();
        String[] args = context.args();
        if (args.length != 4)
            throw new ASMDLSyntaxException("tableswitch requires 4 arguments");

        int min;
        int max;
        try {
            min = Integer.parseInt(args[0]);
            max = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            throw new ASMDLSyntaxException("tableswitch requires two integer arguments");
        }

        Label defaultLabel = context.labelMap().computeIfAbsent(args[2], k -> new Label());
        Label[] labels = Stream.of(args[3].split(","))
                .map(s -> context.labelMap().computeIfAbsent(s, k -> new Label()))
                .toArray(Label[]::new);

        if (labels.length != max - min + 1)
            throw new ASMDLSyntaxException("tableswitch requires " + (max - min + 1) + " labels");

        mv.visitTableSwitchInsn(min, max, defaultLabel, labels);
    }
}
