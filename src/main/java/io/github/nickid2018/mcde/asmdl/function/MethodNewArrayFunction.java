package io.github.nickid2018.mcde.asmdl.function;

import io.github.nickid2018.mcde.asmdl.ASMDLSyntaxException;
import io.github.nickid2018.mcde.asmdl.DescFunction;
import io.github.nickid2018.mcde.asmdl.DescFunctionContext;
import io.github.nickid2018.mcde.asmdl.DescFunctions;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Map;

public class MethodNewArrayFunction extends DescFunction<MethodVisitor> {

    public MethodNewArrayFunction() {
        super("newarray");
    }

    public static final Map<String, Integer> TYPES = Map.of(
            "boolean", Opcodes.T_BOOLEAN,
            "char", Opcodes.T_CHAR,
            "float", Opcodes.T_FLOAT,
            "double", Opcodes.T_DOUBLE,
            "byte", Opcodes.T_BYTE,
            "short", Opcodes.T_SHORT,
            "int", Opcodes.T_INT,
            "long", Opcodes.T_LONG
    );

    @Override
    public <F> MethodVisitor process(DescFunctionContext<F> context) throws ASMDLSyntaxException {
        if (context.environment() != DescFunctions.METHOD && context.environment() != DescFunctions.LABEL)
            throw new ASMDLSyntaxException("newarray function must be in a method block or a label block");

        MethodVisitor mv = (MethodVisitor) context.visitor();
        String[] args = context.args();
        if (args.length < 1)
            throw new ASMDLSyntaxException(name + "function requires an argument");
        if (!TYPES.containsKey(args[0]))
            throw new ASMDLSyntaxException("invalid type");
        mv.visitIntInsn(Opcodes.NEWARRAY, TYPES.get(args[0]));
        return mv;
    }
}