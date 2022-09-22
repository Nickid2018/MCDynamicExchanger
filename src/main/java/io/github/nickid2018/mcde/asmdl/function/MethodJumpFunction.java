package io.github.nickid2018.mcde.asmdl.function;

import io.github.nickid2018.mcde.asmdl.ASMDLSyntaxException;
import io.github.nickid2018.mcde.asmdl.DescFunction;
import io.github.nickid2018.mcde.asmdl.DescFunctionContext;
import io.github.nickid2018.mcde.asmdl.DescFunctions;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

public class MethodJumpFunction extends DescFunction {

    private final int opcode;

    public MethodJumpFunction(String name, int opcode) {
        super(name);
        this.opcode = opcode;
    }

    @Override
    public void process(DescFunctionContext context) throws ASMDLSyntaxException {
        if (context.environment() != DescFunctions.METHOD && context.environment() != DescFunctions.LABEL)
            throw new ASMDLSyntaxException(name + " function must be in a method block or a label block");

        MethodVisitor visitor = (MethodVisitor) context.visitor();
        if (context.args().length != 1)
            throw new ASMDLSyntaxException(name + " function requires one argument");
        String label = context.args()[0];
        Label l = context.labelMap().computeIfAbsent(label, k -> new Label());
        visitor.visitJumpInsn(opcode, l);
    }
}