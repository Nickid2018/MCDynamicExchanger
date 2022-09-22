package io.github.nickid2018.mcde.asmdl.function;

import io.github.nickid2018.mcde.asmdl.ASMDLSyntaxException;
import io.github.nickid2018.mcde.asmdl.DescFunction;
import io.github.nickid2018.mcde.asmdl.DescFunctionContext;
import io.github.nickid2018.mcde.asmdl.DescFunctions;
import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;

public class BootstrapFunction extends DescFunction {

    public BootstrapFunction() {
        super("bootstrap");
    }

    @Override
    public void process(DescFunctionContext context) throws ASMDLSyntaxException {
        if (context.environment() != DescFunctions.INVOKEDYNAMIC && context.environment() != DescFunctions.CONSTANTDYNAMIC)
            throw new ASMDLSyntaxException("bootstrap can only be used in invokedynamic or ldc_dynamic");
        String[] args = context.args();
        if (args.length != 2)
            throw new ASMDLSyntaxException("bootstrap requires 2 arguments");

        Handle handle = HandleFunction.formatAsHandle(args[0], args[1]);
        if (context.additional().size() == 2)
            context.additional().add(handle);
        else
            context.additional().set(2, handle);
    }
}
