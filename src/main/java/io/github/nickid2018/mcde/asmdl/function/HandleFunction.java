package io.github.nickid2018.mcde.asmdl.function;

import io.github.nickid2018.mcde.asmdl.ASMDLSyntaxException;
import io.github.nickid2018.mcde.asmdl.DescFunction;
import io.github.nickid2018.mcde.asmdl.DescFunctionContext;
import io.github.nickid2018.mcde.asmdl.DescFunctions;
import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.HashMap;
import java.util.Map;

public class HandleFunction extends DescFunction {

    public static final Map<String, Integer> HANDLE_TYPES = new HashMap<>();

    public HandleFunction() {
        super("handle");
    }

    static {
        HANDLE_TYPES.put("getfield", Opcodes.H_GETFIELD);
        HANDLE_TYPES.put("getstatic", Opcodes.H_GETSTATIC);
        HANDLE_TYPES.put("putfield", Opcodes.H_PUTFIELD);
        HANDLE_TYPES.put("putstatic", Opcodes.H_PUTSTATIC);
        HANDLE_TYPES.put("invokevirtual", Opcodes.H_INVOKEVIRTUAL);
        HANDLE_TYPES.put("invokestatic", Opcodes.H_INVOKESTATIC);
        HANDLE_TYPES.put("invokespecial", Opcodes.H_INVOKESPECIAL);
        HANDLE_TYPES.put("newinvokespecial", Opcodes.H_NEWINVOKESPECIAL);
        HANDLE_TYPES.put("invokeinterface", Opcodes.H_INVOKEINTERFACE);
    }

    public static Handle formatAsHandle(String type, String nameAndDesc) throws ASMDLSyntaxException {
        if (!HANDLE_TYPES.containsKey(type))
            throw new ASMDLSyntaxException("Invalid handle type: " + type);
        int opcode = HANDLE_TYPES.get(type);

        String[] splitClassName = nameAndDesc.split("\\.", 2);
        if (splitClassName.length != 2)
            throw new ASMDLSyntaxException("invalid handle statement");

        String className = splitClassName[0];
        String[] methodAndDesc = splitClassName[1].split("\\(", 2);
        if (methodAndDesc.length != 2)
            throw new ASMDLSyntaxException("invalid handle statement");

        String methodName = methodAndDesc[0];
        String desc = "(" + methodAndDesc[1];

        return new Handle(opcode, className, methodName, desc, opcode == Opcodes.H_INVOKEINTERFACE);
    }

    @Override
    public void process(DescFunctionContext context) throws ASMDLSyntaxException {
        if (context.environment() != DescFunctions.INVOKEDYNAMIC && context.environment() != DescFunctions.CONSTANTDYNAMIC)
            throw new ASMDLSyntaxException("handle can only be used in invokedynamic or ldc_dynamic");
        String[] args = context.args();
        if (args.length != 2)
            throw new ASMDLSyntaxException("handle requires 2 arguments");

        Handle handle = formatAsHandle(args[0], args[1]);

        if (context.additional().size() == 2)
            context.additional().add(null);
        context.additional().add(handle);
    }
}
