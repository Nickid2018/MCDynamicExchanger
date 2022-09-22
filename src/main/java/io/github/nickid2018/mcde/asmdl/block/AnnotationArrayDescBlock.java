package io.github.nickid2018.mcde.asmdl.block;

import io.github.nickid2018.mcde.asmdl.ASMDLSyntaxException;
import io.github.nickid2018.mcde.asmdl.DescBlock;
import io.github.nickid2018.mcde.asmdl.DescFunctionContext;
import io.github.nickid2018.mcde.asmdl.DescFunctions;
import org.objectweb.asm.AnnotationVisitor;

public class AnnotationArrayDescBlock extends DescBlock {

    public AnnotationArrayDescBlock() {
        super("array");
    }

    @Override
    public AnnotationVisitor processStart(DescFunctionContext context) throws ASMDLSyntaxException {
        if (context.environment() != DescFunctions.ANNOTATION)
            throw new ASMDLSyntaxException("array block can only be used in annotation");
        if (context.args().length != 1)
            throw new ASMDLSyntaxException("array needs 1 argument");
        return ((AnnotationVisitor) context.visitor()).visitArray(context.args()[0]);
    }

    @Override
    public Object processEnd(DescFunctionContext context) throws ASMDLSyntaxException {
        ((AnnotationVisitor) context.visitor()).visitEnd();
        return null;
    }
}
