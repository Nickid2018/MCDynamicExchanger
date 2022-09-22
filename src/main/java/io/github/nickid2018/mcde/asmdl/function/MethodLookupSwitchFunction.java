package io.github.nickid2018.mcde.asmdl.function;

import io.github.nickid2018.mcde.asmdl.ASMDLSyntaxException;
import io.github.nickid2018.mcde.asmdl.DescFunction;
import io.github.nickid2018.mcde.asmdl.DescFunctionContext;
import io.github.nickid2018.mcde.asmdl.DescFunctions;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import java.util.stream.Stream;

public class MethodLookupSwitchFunction extends DescFunction {

    public MethodLookupSwitchFunction() {
        super("lookupswitch");
    }


    @Override
    public void process(DescFunctionContext context) throws ASMDLSyntaxException {
        if (context.environment() != DescFunctions.METHOD && context.environment() != DescFunctions.LABEL)
            throw new ASMDLSyntaxException("lookupswitch function must be in a method block or a label block");

        MethodVisitor mv = (MethodVisitor) context.visitor();
        String[] args = context.args();
        if (args.length != 3)
            throw new ASMDLSyntaxException("lookupswitch requires 3 arguments");

        int[] labelsInts;
        try {
            labelsInts = Stream.of(args[1].split(","))
                    .mapToInt(Integer::parseInt)
                    .toArray();
        } catch (Exception e) {
            throw new ASMDLSyntaxException("lookupswitch requires an integer sequence");
        }

        Label defaultLabel = context.labelMap().computeIfAbsent(args[0], k -> new Label());
        Label[] labels = Stream.of(args[3].split(","))
                .map(s -> context.labelMap().computeIfAbsent(s, k -> new Label()))
                .toArray(Label[]::new);

        if (labels.length != labelsInts.length)
            throw new ASMDLSyntaxException("count of labels and keys must be equal");

        mv.visitLookupSwitchInsn(defaultLabel, labelsInts, labels);
    }
}
