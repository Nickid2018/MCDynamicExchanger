package io.github.nickid2018.mcde.asmdl.function;

import io.github.nickid2018.mcde.asmdl.ASMDLSyntaxException;
import io.github.nickid2018.mcde.asmdl.DescFunction;
import io.github.nickid2018.mcde.asmdl.DescFunctionContext;
import io.github.nickid2018.mcde.asmdl.DescFunctions;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.util.Arrays;

public class LDCFunction extends DescFunction {

    public LDCFunction() {
        super("ldc");
    }

    public static Object formatLDC(String[] args) throws ASMDLSyntaxException {
        return switch (args[0]) {
            case "int" -> Integer.parseInt(args[1]);
            case "long" -> Long.parseLong(args[1]);
            case "float" -> Float.parseFloat(args[1]);
            case "double" -> Double.parseDouble(args[1]);
            case "string" -> String.join(" ", Arrays.copyOfRange(args, 1, args.length));
            case "type" -> Type.getType(args[1]);
            case "handle" -> HandleFunction.formatAsHandle(args[1], args[2]);
            default -> throw new ASMDLSyntaxException("invalid constant type");
        };
    }

    @Override
    public void process(DescFunctionContext context) throws ASMDLSyntaxException {
        if (context.environment() != DescFunctions.METHOD && context.environment() != DescFunctions.LABEL)
            throw new ASMDLSyntaxException("ldc function must be in a method block or a label block");

        String[] args = context.args();
        if (args.length < 2)
            throw new ASMDLSyntaxException("ldc function requires 2 arguments");

        MethodVisitor mv = (MethodVisitor) context.visitor();
        mv.visitLdcInsn(formatLDC(args));
    }
}
