package io.github.nickid2018.mcde.asmdl.function;

import io.github.nickid2018.mcde.asmdl.ASMDLSyntaxException;
import io.github.nickid2018.mcde.asmdl.DescFunction;
import io.github.nickid2018.mcde.asmdl.DescFunctionContext;
import io.github.nickid2018.mcde.asmdl.DescFunctions;
import org.objectweb.asm.MethodVisitor;

public class MethodInvokeFunction extends DescFunction {

    private final int opcode;

    public MethodInvokeFunction(String name, int opcode) {
        super(name);
        this.opcode = opcode;
    }

    @Override
    public void process(DescFunctionContext context) throws ASMDLSyntaxException {
        if (context.environment() != DescFunctions.METHOD && context.environment() != DescFunctions.LABEL)
            throw new ASMDLSyntaxException(name + " function must be in a method block or a label block");

        String[] args = context.args();
        if (args.length != 2)
            throw new ASMDLSyntaxException(name + " function requires two arguments");

        String[] splitClassName = args[0].split("\\.", 2);
        if (splitClassName.length != 2)
            throw new ASMDLSyntaxException("invalid method statement");

        String className = splitClassName[0];
        String[] methodAndDesc = splitClassName[1].split("\\(", 2);
        if (methodAndDesc.length != 2)
            throw new ASMDLSyntaxException("invalid method statement");

        String methodName = methodAndDesc[0];
        String desc = "(" + methodAndDesc[1];

        MethodVisitor mv = (MethodVisitor) context.visitor();
        mv.visitMethodInsn(opcode, className, methodName, desc, Boolean.parseBoolean(args[1]));
    }
}