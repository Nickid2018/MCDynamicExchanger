package io.github.nickid2018.mcde.asmdl.block;

import io.github.nickid2018.mcde.asmdl.ASMDLSyntaxException;
import io.github.nickid2018.mcde.asmdl.DescBlock;
import io.github.nickid2018.mcde.asmdl.DescFunctionContext;
import io.github.nickid2018.mcde.asmdl.DescFunctions;
import org.objectweb.asm.ConstantDynamic;
import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;

public class ConstantDynamicDescBlock extends DescBlock {

    public ConstantDynamicDescBlock() {
        super("ldc_dynamic");
    }

    @Override
    public MethodVisitor processStart(DescFunctionContext context) throws ASMDLSyntaxException {
        if (context.environment() != DescFunctions.METHOD &&
                context.environment() != DescFunctions.CONSTANTDYNAMIC &&
                context.environment() != DescFunctions.INVOKEDYNAMIC &&
                context.environment() != DescFunctions.LABEL)
            throw new ASMDLSyntaxException("ldc_dynamic block must be in a method block," +
                    " a label block, a invokedynamic block or a ldc_dynamic block");
        MethodVisitor mv = (MethodVisitor) context.visitor();
        String[] args = context.args();
        if (args.length != 2)
            throw new ASMDLSyntaxException("ldc_dynamic requires 2 arguments");
        context.additional().add(args[0]);
        context.additional().add(args[1]);
        return mv;
    }

    @Override
    public Object processEnd(DescFunctionContext context) throws ASMDLSyntaxException {
        Object[] array = context.additional().toArray();
        if (array.length < 3 || array[2] == null)
            throw new ASMDLSyntaxException("ldc_dynamic requires a bootstrap method argument");

        String desc = (String) array[1];
        String name = (String) array[0];
        Handle bootstrap = (Handle) array[2];

        Object[] methodArgs = new Object[array.length - 3];
        System.arraycopy(array, 3, methodArgs, 0, array.length - 3);

        return new ConstantDynamic(name, desc, bootstrap, methodArgs);
    }

    @Override
    public void process(DescFunctionContext context) throws ASMDLSyntaxException {
        if (context.environment() == DescFunctions.METHOD)
            ((MethodVisitor) context.visitor()).visitLdcInsn(context.blockReturns());
        else {
            if (context.additional().size() == 2)
                context.additional().add(null);
            context.additional().add(context.blockReturns());
        }
    }
}
