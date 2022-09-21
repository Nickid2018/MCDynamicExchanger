package io.github.nickid2018.mcde.asmdl.function;

import io.github.nickid2018.mcde.asmdl.ASMDLSyntaxException;
import io.github.nickid2018.mcde.asmdl.DescFunction;
import io.github.nickid2018.mcde.asmdl.DescFunctionContext;
import io.github.nickid2018.mcde.asmdl.DescFunctions;
import org.objectweb.asm.MethodVisitor;

public class MethodFieldFunction extends DescFunction<MethodVisitor> {

    private final int opcode;

    public MethodFieldFunction(String name, int opcode) {
        super(name);
        this.opcode = opcode;
    }

    @Override
    public <F> MethodVisitor process(DescFunctionContext<F> context) throws ASMDLSyntaxException {
        if (context.environment() != DescFunctions.METHOD && context.environment() != DescFunctions.LABEL)
            throw new ASMDLSyntaxException(name + " function must be in a method block or a label block");

        String[] args = context.args();
        if (args.length != 2)
            throw new ASMDLSyntaxException(name + " function requires two arguments");

        String[] splitClassName = args[0].split("\\.", 2);
        if (splitClassName.length != 2)
            throw new ASMDLSyntaxException("invalid field statement");

        String className = splitClassName[0];
        String fieldName = splitClassName[1];

        String desc = args[1];

        MethodVisitor mv = (MethodVisitor) context.visitor();
        mv.visitFieldInsn(opcode, className, fieldName, desc);
        return mv;
    }
}
