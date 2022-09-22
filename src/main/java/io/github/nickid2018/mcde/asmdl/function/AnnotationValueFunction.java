package io.github.nickid2018.mcde.asmdl.function;

import io.github.nickid2018.mcde.asmdl.ASMDLSyntaxException;
import io.github.nickid2018.mcde.asmdl.DescFunction;
import io.github.nickid2018.mcde.asmdl.DescFunctionContext;
import io.github.nickid2018.mcde.asmdl.DescFunctions;
import org.objectweb.asm.AnnotationVisitor;

import java.util.Arrays;

public class AnnotationValueFunction extends DescFunction {

    public AnnotationValueFunction() {
        super("value");
    }

    @Override
    public void process(DescFunctionContext context) throws ASMDLSyntaxException {
        if (context.environment() != DescFunctions.ANNOTATION)
            throw new ASMDLSyntaxException("value function can only be used in annotation");
        String[] args = context.args();
        if (args.length < 2)
            throw new ASMDLSyntaxException("value needs at least 2 arguments");
        ((AnnotationVisitor) context.visitor()).visit(args[0], MethodLDCFunction.formatLDC(Arrays.copyOfRange(args, 1, args.length)));
    }
}