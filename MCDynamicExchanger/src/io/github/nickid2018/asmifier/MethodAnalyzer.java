package io.github.nickid2018.asmifier;

import org.objectweb.asm.*;

import java.util.ArrayList;
import java.util.List;

public class MethodAnalyzer extends MethodVisitor implements Opcodes {

    private final ASMifier asmifier;
    private final List<Label> labels = new ArrayList<>();

    public MethodAnalyzer(ASMifier asmifier) {
        super(ASM9);
        this.asmifier = asmifier;
    }

    @Override
    public void visitParameter(String name, int access) {
        asmifier.line("mv.visitParameter(%s, %s);",
                asmifier.quote(name), AccessFlagCategory.fromIntToAccessFlag(access, AccessFlagCategory.PARAMETER));
    }

    @Override
    public AnnotationVisitor visitAnnotationDefault() {
        asmifier.line("{");
        asmifier.indent++;
        asmifier.line("AnnotationVisitor av = mv.visitAnnotationDefault();");
        return new AnnotationAnalyzer(asmifier, 0);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        asmifier.line("{");
        asmifier.indent++;
        asmifier.line("AnnotationVisitor av = mv.visitAnnotation(%s, %s);", asmifier.quote(descriptor), visible);
        return new AnnotationAnalyzer(asmifier, 0);
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        String typeRefStr;
        switch (typeRef) {
            case TypeReference.METHOD_TYPE_PARAMETER:
                typeRefStr = "TypeReference.METHOD_TYPE_PARAMETER";
                break;
            case TypeReference.METHOD_TYPE_PARAMETER_BOUND:
                typeRefStr = "TypeReference.METHOD_TYPE_PARAMETER_BOUND";
                break;
            case TypeReference.METHOD_RETURN:
                typeRefStr = "TypeReference.METHOD_RETURN";
                break;
            case TypeReference.METHOD_RECEIVER:
                typeRefStr = "TypeReference.METHOD_RECEIVER";
                break;
            case TypeReference.METHOD_FORMAL_PARAMETER:
                typeRefStr = "TypeReference.METHOD_FORMAL_PARAMETER";
                break;
            case TypeReference.THROWS:
                typeRefStr = "TypeReference.THROWS";
                break;
            default:
                typeRefStr = typeRef + "";
        }
        asmifier.line("{");
        asmifier.indent++;
        asmifier.line("AnnotationVisitor av = mv.visitTypeAnnotation(%s, TypePath.fromString(%s), %s, %s);",
                typeRefStr, asmifier.quote(typePath.toString()), asmifier.quote(descriptor), visible);
        return new AnnotationAnalyzer(asmifier, 0);
    }

    @Override
    public void visitAnnotableParameterCount(int parameterCount, boolean visible) {
        asmifier.line("mv.visitAnnotableParameterCount(%s, %s);", parameterCount, visible);
    }

    @Override
    public AnnotationVisitor visitParameterAnnotation(int parameter, String descriptor, boolean visible) {
        asmifier.line("{");
        asmifier.indent++;
        asmifier.line("AnnotationVisitor av = mv.visitParameterAnnotation(%s, %s, %s);", parameter, asmifier.quote(descriptor), visible);
        return new AnnotationAnalyzer(asmifier, 0);
    }

    @Override
    public void visitAttribute(Attribute attribute) {
        // Unsupported
    }

    @Override
    public void visitCode() {
        asmifier.line("mv.visitCode();");
    }

    @Override
    public void visitFrame(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
        StringBuilder localStr = new StringBuilder("new Object[]{ ");
        if(local != null && local.length != 0) {
            localStr.append(MethodOpcodes.getAsFrameLocal(local[0]));
            for(int i = 1; i < local.length; i++)
                localStr.append(", ").append(MethodOpcodes.getAsFrameLocal(local[i]));
        }
        localStr.append(" }");
        StringBuilder stackStr = new StringBuilder("new Object[]{ ");
        if(stack != null && stack.length != 0) {
            stackStr.append(MethodOpcodes.getAsFrameStack(stack[0]));
            for(int i = 1; i < stack.length; i++)
                stackStr.append(", ").append(MethodOpcodes.getAsFrameStack(stack[i]));
        }
        stackStr.append(" }");
        asmifier.line("mv.visitFrame(%d, %d, %s, %d, %s);", type, numLocal, localStr, numStack, stackStr);
    }

    @Override
    public void visitInsn(int opcode) {
        asmifier.line("mv.visitInsn(%s);", MethodOpcodes.OPCODES_TABLE[opcode]);
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
        if(opcode == NEWARRAY)
            asmifier.line("mv.visitIntInsn(%s, %s);", "NEWARRAY", MethodOpcodes.getAsArrayType(operand));
        else
            asmifier.line("mv.visitIntInsn(%s, %d);", MethodOpcodes.OPCODES_TABLE[opcode], operand);
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        asmifier.line("mv.visitVarInsn(%s, %d);", MethodOpcodes.OPCODES_TABLE[opcode], var);
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        asmifier.line("mv.visitTypeInsn(%s, %s);", MethodOpcodes.OPCODES_TABLE[opcode], asmifier.quote(type));
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        asmifier.line("mv.visitFieldInsn(%s, %s, %s, %s);", MethodOpcodes.OPCODES_TABLE[opcode],
                asmifier.quote(owner), asmifier.quote(name), asmifier.quote(descriptor));
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        asmifier.line("mv.visitMethodInsn(%s, %s, %s, %s, %s);", MethodOpcodes.OPCODES_TABLE[opcode],
                asmifier.quote(owner), asmifier.quote(name), asmifier.quote(descriptor), isInterface);
    }

    private String parseHandleAsString(Handle handle) {
        return String.format("new Handle(%s, %s, %s, %s, %s)",
                MethodOpcodes.getAsHandleType(handle.getTag()), asmifier.quote(handle.getOwner()),
                asmifier.quote(handle.getName()), asmifier.quote(handle.getDesc()), handle.isInterface());
    }

    private String parseObject(Object obj) {
        if(obj == null)
            return "null";
        if(obj instanceof Handle)
            return parseHandleAsString((Handle) obj);
        if(obj instanceof String)
            return asmifier.quote((String) obj);
        if (obj instanceof Float)
            return obj + "F";
        if (obj instanceof Long)
            return obj + "L";
        if(obj instanceof Type)
            return String.format("Type.getType(%s)", asmifier.quote(obj.toString()));
        if(obj instanceof ConstantDynamic) {
            ConstantDynamic dynamic = (ConstantDynamic) obj;
            Object[] bootstraps = new Object[dynamic.getBootstrapMethodArgumentCount()];
            for(int i = 0; i < bootstraps.length; i++)
                bootstraps[i] = dynamic.getBootstrapMethodArgument(i);
            return String.format("new ConstantDynamic(%s, %s, %s%s)",
                    asmifier.quote(dynamic.getName()), asmifier.quote(dynamic.getDescriptor()),
                    parseHandleAsString(dynamic.getBootstrapMethod()), parseAsUncountedObjects(bootstraps));
        }
        return obj.toString();
    }

    private String parseAsUncountedObjects(Object... array) {
        StringBuilder builder = new StringBuilder();
        for(Object obj : array)
            builder.append(", ").append(parseObject(obj));
        return builder.toString();
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
        asmifier.line("mv.visitInvokeDynamicInsn(%s, %s, %s%s);", asmifier.quote(name),
                asmifier.quote(descriptor), parseHandleAsString(bootstrapMethodHandle), parseAsUncountedObjects(bootstrapMethodArguments));
    }

    private void checkLabel(Label label) {
        if(!labels.contains(label)) {
            asmifier.line("Label l%d = new Label();", labels.size());
            labels.add(label);
        }
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
        checkLabel(label);
        asmifier.line("mv.visitJumpInsn(%s, l%d)", MethodOpcodes.OPCODES_TABLE[opcode], labels.indexOf(label));
    }

    @Override
    public void visitLabel(Label label) {
        checkLabel(label);
        asmifier.line("mv.visitLabel(l%d)", labels.indexOf(label));
    }

    @Override
    public void visitLdcInsn(Object value) {
        String valueStr;
        if(value instanceof Type)
            valueStr = String.format("Type.getType(%s)", asmifier.quote(value.toString()));
        else if (value instanceof String)
            valueStr = asmifier.quote(value.toString());
        else if (value instanceof Character)
            valueStr = "'" + value + "'";
        else if (value instanceof Float)
            valueStr = value + "F";
        else if (value instanceof Long)
            valueStr = value + "L";
        else
            valueStr = value.toString();
        asmifier.line("mv.visitLdcInsn(%s);", valueStr);
    }

    @Override
    public void visitIincInsn(int var, int increment) {
        asmifier.line("mv.visitIincInsn(%d, %d);", var, increment);
    }

    private String makeLabelArray(Label[] labelArray) {
        StringBuilder builder = new StringBuilder("new Label[]{ ");
        if(labelArray != null && labelArray.length != 0) {
            checkLabel(labelArray[0]);
            builder.append("l").append(labels.indexOf(labelArray[0]));
            for(int i = 1; i < labelArray.length; i++) {
                checkLabel(labelArray[i]);
                builder.append(", l").append(labels.indexOf(labelArray[i]));
            }
        }
        builder.append(" }");
        return builder.toString();
    }

    private String makeIntArray(int[] intArray) {
        StringBuilder builder = new StringBuilder("new int[]{ ");
        if(intArray != null && intArray.length != 0) {
            builder.append(intArray[0]);
            for(int i = 1; i < intArray.length; i++)
                builder.append(", ").append(intArray[i]);
        }
        builder.append(" }");
        return builder.toString();
    }

    @Override
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
        checkLabel(dflt);
        StringBuilder builder = new StringBuilder();
        for(Label label : labels) {
            checkLabel(label);
            builder.append(", l").append(this.labels.indexOf(label));
        }
        asmifier.line("mv.visitTableSwitchInsn(%d, %d, l%d%s)", min, max, this.labels.indexOf(dflt), builder);
    }

    @Override
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        checkLabel(dflt);
        asmifier.line("mv.visitLookupSwitchInsn(l%d, %s, %s);",
                this.labels.indexOf(dflt), makeIntArray(keys), makeLabelArray(labels));
    }

    @Override
    public void visitMultiANewArrayInsn(String descriptor, int numDimensions) {
        asmifier.line("mv.visitMultiANewArrayInsn(%s, %d);", asmifier.quote(descriptor), numDimensions);
    }

    @Override
    public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        String typeRefStr;
        switch (typeRef) {
            case TypeReference.INSTANCEOF:
                typeRefStr = "TypeReference.INSTANCEOF";
                break;
            case TypeReference.NEW:
                typeRefStr = "TypeReference.NEW";
                break;
            case TypeReference.CONSTRUCTOR_REFERENCE:
                typeRefStr = "TypeReference.CONSTRUCTOR_REFERENCE";
                break;
            case TypeReference.METHOD_REFERENCE:
                typeRefStr = "TypeReference.METHOD_REFERENCE";
                break;
            case TypeReference.CAST:
                typeRefStr = "TypeReference.CAST";
                break;
            case TypeReference.CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT:
                typeRefStr = "TypeReference.CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT";
                break;
            case TypeReference.METHOD_INVOCATION_TYPE_ARGUMENT:
                typeRefStr = "TypeReference.METHOD_INVOCATION_TYPE_ARGUMENT";
                break;
            case TypeReference.CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT:
                typeRefStr = "TypeReference.CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT";
                break;
            case TypeReference.METHOD_REFERENCE_TYPE_ARGUMENT:
                typeRefStr = "TypeReference.METHOD_REFERENCE_TYPE_ARGUMENT";
                break;
            default:
                typeRefStr = typeRef + "";
        }
        asmifier.line("{");
        asmifier.indent++;
        asmifier.line("AnnotationVisitor av = mv.visitInsnAnnotation(%s, TypePath.fromString(%s), %s, %s);",
                typeRefStr, asmifier.quote(typePath.toString()), asmifier.quote(descriptor), visible);
        return new AnnotationAnalyzer(asmifier, 0);
    }

    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        checkLabel(start);
        checkLabel(end);
        checkLabel(handler);
        asmifier.line("mv.visitTryCatchBlock(l%d, l%d, l%d, %s);",
                labels.indexOf(start), labels.indexOf(end), labels.indexOf(handler), asmifier.quote(type));
    }

    @Override
    public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        String typeRefStr;
        if (typeRef == TypeReference.EXCEPTION_PARAMETER)
            typeRefStr = "TypeReference.EXCEPTION_PARAMETER";
        else
            typeRefStr = typeRef + "";
        asmifier.line("{");
        asmifier.indent++;
        asmifier.line("AnnotationVisitor av = mv.visitTryCatchAnnotation(%s, TypePath.fromString(%s), %s, %s);",
                typeRefStr, asmifier.quote(typePath.toString()), asmifier.quote(descriptor), visible);
        return new AnnotationAnalyzer(asmifier, 0);
    }

    @Override
    public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
        checkLabel(start);
        checkLabel(end);
        asmifier.line("mv.visitLocalVariable(%s, %s, %s, l%d, l%d, %d);",
                asmifier.quote(name), asmifier.quote(descriptor), asmifier.quote(signature),
                labels.indexOf(start), labels.indexOf(end), index);
    }

    @Override
    public AnnotationVisitor visitLocalVariableAnnotation(
            int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String descriptor, boolean visible) {
        String typeRefStr;
        switch (typeRef) {
            case TypeReference.LOCAL_VARIABLE:
                typeRefStr = "TypeReference.LOCAL_VARIABLE";
                break;
            case TypeReference.RESOURCE_VARIABLE:
                typeRefStr = "TypeReference.RESOURCE_VARIABLE";
                break;
            default:
                typeRefStr = typeRef + "";
        }
        asmifier.line("{");
        asmifier.indent++;
        asmifier.line("AnnotationVisitor av = mv.visitLocalVariableAnnotation(%s, TypePath.fromString(%s), %s, %s, %s, %s, %s);",
                typeRefStr, asmifier.quote(typePath.toString()), makeLabelArray(start),
                makeLabelArray(end), makeIntArray(index), asmifier.quote(descriptor), visible);
        return new AnnotationAnalyzer(asmifier, 0);
    }

    @Override
    public void visitLineNumber(int line, Label start) {
        checkLabel(start);
        asmifier.line("mv.visitLineNumber(%d, l%d);", line, labels.indexOf(start));
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        asmifier.line("mv.visitMaxs(%d, %d);", maxStack, maxLocals);
    }

    @Override
    public void visitEnd() {
        asmifier.line("mv.visitEnd();");
        asmifier.indent--;
        asmifier.line("}");
    }
}
