package io.github.nickid2018.mcde.asmdl.block;

import io.github.nickid2018.mcde.asmdl.ASMDLSyntaxException;
import io.github.nickid2018.mcde.asmdl.DescBlock;
import io.github.nickid2018.mcde.asmdl.DescFunctionContext;
import io.github.nickid2018.mcde.asmdl.DescFunctions;
import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;

public class InvokeDynamicDescBlock extends DescBlock {

    public InvokeDynamicDescBlock() {
        super("invokedynamic");
    }

    @Override
    public MethodVisitor processStart(DescFunctionContext context) throws ASMDLSyntaxException {
        if (context.environment() != DescFunctions.METHOD && context.environment() != DescFunctions.LABEL)
            throw new ASMDLSyntaxException("invokedynamic block must be in a method block or a label block");
        MethodVisitor mv = (MethodVisitor) context.visitor();
        String[] args = context.args();
        if (args.length != 2)
            throw new ASMDLSyntaxException("invokedynamic requires 2 arguments");
        context.additional().add(args[0]);
        context.additional().add(args[1]);
        return mv;
    }

    @Override
    public Object processEnd(DescFunctionContext context) throws ASMDLSyntaxException {
        MethodVisitor mv = (MethodVisitor) context.visitor();
        Object[] array = context.additional().toArray();
        if (array.length < 3 || array[2] == null)
            throw new ASMDLSyntaxException("invokedynamic requires a bootstrap method argument");

        String name = (String) array[0];
        String desc = (String) array[1];
        Handle bootstrap = (Handle) array[2];

        Object[] methodArgs = new Object[array.length - 3];
        System.arraycopy(array, 3, methodArgs, 0, array.length - 3);
        mv.visitInvokeDynamicInsn(name, desc, bootstrap, methodArgs);
        return null;
    }
}
