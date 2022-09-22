package io.github.nickid2018.mcde.asmdl.block;

import io.github.nickid2018.mcde.asmdl.ASMDLSyntaxException;
import io.github.nickid2018.mcde.asmdl.DescBlock;
import io.github.nickid2018.mcde.asmdl.DescFunctionContext;
import io.github.nickid2018.mcde.asmdl.DescFunctions;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

public class LabelDescBlock extends DescBlock {

    public LabelDescBlock() {
        super("label");
    }

    @Override
    public Object processEnd(DescFunctionContext context) {
        return null;
    }

    @Override
    public MethodVisitor processStart(DescFunctionContext context) throws ASMDLSyntaxException {
        if (context.environment() != DescFunctions.METHOD && context.environment() != DescFunctions.LABEL)
            throw new ASMDLSyntaxException("label block must be in a method block or a label block");
        if (context.args().length != 1)
            throw new ASMDLSyntaxException("label block requires one argument");
        String label = context.args()[0];
        Label l = context.labelMap().computeIfAbsent(label, k -> new Label());
        MethodVisitor mv = (MethodVisitor) context.visitor();
        mv.visitLabel(l);
        return mv;
    }
}
