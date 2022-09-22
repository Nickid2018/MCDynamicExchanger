package io.github.nickid2018.mcde.asmdl.function;

import io.github.nickid2018.mcde.asmdl.ASMDLSyntaxException;
import io.github.nickid2018.mcde.asmdl.DescFunction;
import io.github.nickid2018.mcde.asmdl.DescFunctionContext;
import org.objectweb.asm.MethodVisitor;

public class ConstantFunction extends DescFunction {

    public ConstantFunction() {
        super("constant");
    }

    @Override
    public void process(DescFunctionContext context) throws ASMDLSyntaxException {
        Object arg = LDCFunction.formatLDC(context.args());
        if (context.additional().size() == 2)
            context.additional().add(null);
        context.additional().add(arg);
    }
}
