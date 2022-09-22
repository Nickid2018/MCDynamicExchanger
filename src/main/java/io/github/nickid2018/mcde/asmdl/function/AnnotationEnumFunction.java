package io.github.nickid2018.mcde.asmdl.function;

import io.github.nickid2018.mcde.asmdl.ASMDLSyntaxException;
import io.github.nickid2018.mcde.asmdl.DescFunction;
import io.github.nickid2018.mcde.asmdl.DescFunctionContext;
import io.github.nickid2018.mcde.asmdl.DescFunctions;
import org.objectweb.asm.AnnotationVisitor;

public class AnnotationEnumFunction extends DescFunction {

    public AnnotationEnumFunction() {
        super("enum");
    }

    @Override
    public void process(DescFunctionContext context) throws ASMDLSyntaxException {
        if (context.environment() != DescFunctions.ANNOTATION)
            throw new ASMDLSyntaxException("enum function can only be used in annotation");
        String[] args = context.args();
        if (args.length != 3)
            throw new ASMDLSyntaxException("enum needs 3 arguments");
        ((AnnotationVisitor) context.visitor()).visitEnum(args[0], args[1], args[2]);
    }
}
