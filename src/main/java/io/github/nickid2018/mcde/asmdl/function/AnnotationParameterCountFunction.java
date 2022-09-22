package io.github.nickid2018.mcde.asmdl.function;

import io.github.nickid2018.mcde.asmdl.ASMDLSyntaxException;
import io.github.nickid2018.mcde.asmdl.DescFunction;
import io.github.nickid2018.mcde.asmdl.DescFunctionContext;
import io.github.nickid2018.mcde.asmdl.DescFunctions;
import org.objectweb.asm.MethodVisitor;

public class AnnotationParameterCountFunction extends DescFunction {

    public AnnotationParameterCountFunction() {
        super("annotation_param_count");
    }


    @Override
    public void process(DescFunctionContext context) throws ASMDLSyntaxException {
        if (context.environment() != DescFunctions.METHOD && context.environment() != DescFunctions.LABEL)
            throw new ASMDLSyntaxException("annotation_param_count function must be in a method block or a label block");
        String[] args = context.args();
        if (args.length != 2)
            throw new ASMDLSyntaxException("annotation_param_count requires 2 arguments");
        MethodVisitor mv = (MethodVisitor) context.visitor();
        mv.visitAnnotableParameterCount(Integer.parseInt(args[0]), Boolean.parseBoolean(args[1]));
    }
}
