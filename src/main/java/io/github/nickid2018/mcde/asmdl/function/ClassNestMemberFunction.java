package io.github.nickid2018.mcde.asmdl.function;

import io.github.nickid2018.mcde.asmdl.ASMDLSyntaxException;
import io.github.nickid2018.mcde.asmdl.DescFunction;
import io.github.nickid2018.mcde.asmdl.DescFunctionContext;
import io.github.nickid2018.mcde.asmdl.DescFunctions;
import org.objectweb.asm.ClassWriter;

public class ClassNestMemberFunction extends DescFunction {

    public ClassNestMemberFunction() {
        super("nestmember");
    }

    @Override
    public void process(DescFunctionContext context) throws ASMDLSyntaxException {
        if (context.environment() != DescFunctions.CLASS)
            throw new ASMDLSyntaxException("nestmember function must be in a class block");
        String[] args = context.args();
        if (args.length != 1)
            throw new ASMDLSyntaxException("nestmember requires 1 argument");
        ((ClassWriter) context.visitor()).visitNestMember(context.args()[0]);
    }
}
