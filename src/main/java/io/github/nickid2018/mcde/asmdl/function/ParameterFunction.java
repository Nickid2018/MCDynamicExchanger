package io.github.nickid2018.mcde.asmdl.function;

import io.github.nickid2018.mcde.asmdl.ASMDLSyntaxException;
import io.github.nickid2018.mcde.asmdl.DescFunction;
import io.github.nickid2018.mcde.asmdl.DescFunctionContext;
import io.github.nickid2018.mcde.asmdl.DescFunctions;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Map;

public class ParameterFunction extends DescFunction {

    public ParameterFunction() {
        super("parameter");
    }

    public static final Map<String, Integer> ACCESS_FLAGS = Map.of(
            "final", Opcodes.ACC_FINAL,
            "synthetic", Opcodes.ACC_SYNTHETIC
    );

    @Override
    public void process(DescFunctionContext context) throws ASMDLSyntaxException {
        if (context.environment() != DescFunctions.METHOD)
            throw new ASMDLSyntaxException("parameter function must be in a method block");

        String[] args = context.args();
        if (args.length < 2)
            throw new ASMDLSyntaxException("parameter name is required");

        int pointer = 0;
        int access = 0;
        for (;;pointer++) {
            if (pointer >= args.length)
                throw new ASMDLSyntaxException("parameter name is required");
            if (ACCESS_FLAGS.containsKey(args[pointer]))
                access += ACCESS_FLAGS.get(args[pointer]);
            else break;
        }

        String name = args[pointer];

        ((MethodVisitor) context.visitor()).visitParameter(name, access);
    }
}
