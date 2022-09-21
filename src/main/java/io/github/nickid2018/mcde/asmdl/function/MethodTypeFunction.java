package io.github.nickid2018.mcde.asmdl.function;

import io.github.nickid2018.mcde.asmdl.ASMDLSyntaxException;
import io.github.nickid2018.mcde.asmdl.DescFunction;
import io.github.nickid2018.mcde.asmdl.DescFunctionContext;
import io.github.nickid2018.mcde.asmdl.DescFunctions;
import org.objectweb.asm.MethodVisitor;

public class MethodTypeFunction extends DescFunction<MethodVisitor> {

    private final int opcode;

    public MethodTypeFunction(String name, int opcode) {
        super(name);
        this.opcode = opcode;
    }

    @Override
    public <F> MethodVisitor process(DescFunctionContext<F> context) throws ASMDLSyntaxException {
        if (context.environment() != DescFunctions.METHOD && context.environment() != DescFunctions.LABEL)
            throw new ASMDLSyntaxException(name + " function must be in a method block or a label block");

        MethodVisitor visitor = (MethodVisitor) context.visitor();
        if (context.args().length != 1)
            throw new ASMDLSyntaxException(name + " function requires one argument");
        visitor.visitTypeInsn(opcode, context.args()[0]);
        return visitor;
    }
}
