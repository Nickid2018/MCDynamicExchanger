package io.github.nickid2018.mcde.asmdl.block;

import io.github.nickid2018.mcde.asmdl.ASMDLSyntaxException;
import io.github.nickid2018.mcde.asmdl.DescBlock;
import io.github.nickid2018.mcde.asmdl.DescFunctionContext;
import io.github.nickid2018.mcde.asmdl.DescFunctions;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.RecordComponentVisitor;

public class RecordComponentDescBlock extends DescBlock {

    public RecordComponentDescBlock() {
        super("record_component");
    }

    @Override
    public RecordComponentVisitor processStart(DescFunctionContext context) throws ASMDLSyntaxException {
        if (context.environment() != DescFunctions.CLASS)
            throw new ASMDLSyntaxException("record block must be in class block");
        String[] args = context.args();
        if (args.length != 3)
            throw new ASMDLSyntaxException("record block must have 3 arguments");
        ClassWriter cw = (ClassWriter) context.visitor();
        return cw.visitRecordComponent(args[0], args[1], args[2]);
    }

    @Override
    public Object processEnd(DescFunctionContext context) throws ASMDLSyntaxException {
        ((RecordComponentVisitor) context.visitor()).visitEnd();
        return null;
    }
}
