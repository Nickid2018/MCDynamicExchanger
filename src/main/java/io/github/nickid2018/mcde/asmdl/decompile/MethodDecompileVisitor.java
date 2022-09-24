package io.github.nickid2018.mcde.asmdl.decompile;

import org.objectweb.asm.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MethodDecompileVisitor extends MethodVisitor {

    private static final String[] ARRAY_TYPES = new String[] {
            "boolean", "char", "float", "double", "byte", "short", "int", "long"
    };

    public static final Map<Integer, String> METHOD_TYPE_REFERENCE_KINDS = Map.of(
            TypeReference.METHOD_TYPE_PARAMETER, "method_type_parameter",
            TypeReference.METHOD_TYPE_PARAMETER_BOUND, "method_type_parameter_bound",
            TypeReference.METHOD_RETURN, "method_return",
            TypeReference.METHOD_RECEIVER, "method_receiver",
            TypeReference.METHOD_FORMAL_PARAMETER, "method_formal_parameter",
            TypeReference.THROWS, "throws"
    );

    public static final Map<Integer, String> LOCAL_VARIABLE_TYPE_REFERENCE_KINDS = Map.of(
            TypeReference.LOCAL_VARIABLE, "local_variable",
            TypeReference.RESOURCE_VARIABLE, "resource_variable"
    );

    public static final Map<Integer, String> INSN_TYPE_REFERENCE_KINDS = Map.of(
            TypeReference.INSTANCEOF, "instanceof",
            TypeReference.NEW, "new",
            TypeReference.CONSTRUCTOR_REFERENCE, "constructor_reference",
            TypeReference.METHOD_REFERENCE, "method_reference",
            TypeReference.CAST, "cast",
            TypeReference.CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT, "constructor_invocation_type_argument",
            TypeReference.METHOD_INVOCATION_TYPE_ARGUMENT, "method_invocation_type_argument",
            TypeReference.CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT, "constructor_reference_type_argument",
            TypeReference.METHOD_REFERENCE_TYPE_ARGUMENT, "method_reference_type_argument"
    );

    public static final Map<Integer, String> INSN_LIST = new HashMap<>();

    static {
        INSN_LIST.put(Opcodes.NOP, "nop");
        INSN_LIST.put(Opcodes.ACONST_NULL, "aconst_null");
        INSN_LIST.put(Opcodes.ICONST_M1, "iconst_m1");
        INSN_LIST.put(Opcodes.ICONST_0, "iconst_0");
        INSN_LIST.put(Opcodes.ICONST_1, "iconst_1");
        INSN_LIST.put(Opcodes.ICONST_2, "iconst_2");
        INSN_LIST.put(Opcodes.ICONST_3, "iconst_3");
        INSN_LIST.put(Opcodes.ICONST_4, "iconst_4");
        INSN_LIST.put(Opcodes.ICONST_5, "iconst_5");
        INSN_LIST.put(Opcodes.LCONST_0, "lconst_0");
        INSN_LIST.put(Opcodes.LCONST_1, "lconst_1");
        INSN_LIST.put(Opcodes.FCONST_0, "fconst_0");
        INSN_LIST.put(Opcodes.FCONST_1, "fconst_1");
        INSN_LIST.put(Opcodes.FCONST_2, "fconst_2");
        INSN_LIST.put(Opcodes.DCONST_0, "dconst_0");
        INSN_LIST.put(Opcodes.DCONST_1, "dconst_1");
        INSN_LIST.put(Opcodes.IALOAD, "iaload");
        INSN_LIST.put(Opcodes.LALOAD, "laload");
        INSN_LIST.put(Opcodes.FALOAD, "faload");
        INSN_LIST.put(Opcodes.DALOAD, "daload");
        INSN_LIST.put(Opcodes.AALOAD, "aaload");
        INSN_LIST.put(Opcodes.BALOAD, "baload");
        INSN_LIST.put(Opcodes.CALOAD, "caload");
        INSN_LIST.put(Opcodes.SALOAD, "saload");
        INSN_LIST.put(Opcodes.IASTORE, "iastore");
        INSN_LIST.put(Opcodes.LASTORE, "lastore");
        INSN_LIST.put(Opcodes.FASTORE, "fastore");
        INSN_LIST.put(Opcodes.DASTORE, "dastore");
        INSN_LIST.put(Opcodes.AASTORE, "aastore");
        INSN_LIST.put(Opcodes.BASTORE, "bastore");
        INSN_LIST.put(Opcodes.CASTORE, "castore");
        INSN_LIST.put(Opcodes.SASTORE, "sastore");
        INSN_LIST.put(Opcodes.POP, "pop");
        INSN_LIST.put(Opcodes.POP2, "pop2");
        INSN_LIST.put(Opcodes.DUP, "dup");
        INSN_LIST.put(Opcodes.DUP_X1, "dup_x1");
        INSN_LIST.put(Opcodes.DUP_X2, "dup_x2");
        INSN_LIST.put(Opcodes.DUP2, "dup2");
        INSN_LIST.put(Opcodes.DUP2_X1, "dup2_x1");
        INSN_LIST.put(Opcodes.DUP2_X2, "dup2_x2");
        INSN_LIST.put(Opcodes.SWAP, "swap");
        INSN_LIST.put(Opcodes.IADD, "iadd");
        INSN_LIST.put(Opcodes.LADD, "ladd");
        INSN_LIST.put(Opcodes.FADD, "fadd");
        INSN_LIST.put(Opcodes.DADD, "dadd");
        INSN_LIST.put(Opcodes.ISUB, "isub");
        INSN_LIST.put(Opcodes.LSUB, "lsub");
        INSN_LIST.put(Opcodes.FSUB, "fsub");
        INSN_LIST.put(Opcodes.DSUB, "dsub");
        INSN_LIST.put(Opcodes.IMUL, "imul");
        INSN_LIST.put(Opcodes.LMUL, "lmul");
        INSN_LIST.put(Opcodes.FMUL, "fmul");
        INSN_LIST.put(Opcodes.DMUL, "dmul");
        INSN_LIST.put(Opcodes.IDIV, "idiv");
        INSN_LIST.put(Opcodes.LDIV, "ldiv");
        INSN_LIST.put(Opcodes.FDIV, "fdiv");
        INSN_LIST.put(Opcodes.DDIV, "ddiv");
        INSN_LIST.put(Opcodes.IREM, "irem");
        INSN_LIST.put(Opcodes.LREM, "lrem");
        INSN_LIST.put(Opcodes.FREM, "frem");
        INSN_LIST.put(Opcodes.DREM, "drem");
        INSN_LIST.put(Opcodes.INEG, "ineg");
        INSN_LIST.put(Opcodes.LNEG, "lneg");
        INSN_LIST.put(Opcodes.FNEG, "fneg");
        INSN_LIST.put(Opcodes.DNEG, "dneg");
        INSN_LIST.put(Opcodes.ISHL, "ishl");
        INSN_LIST.put(Opcodes.LSHL, "lshl");
        INSN_LIST.put(Opcodes.ISHR, "ishr");
        INSN_LIST.put(Opcodes.LSHR, "lshr");
        INSN_LIST.put(Opcodes.IUSHR, "iushr");
        INSN_LIST.put(Opcodes.LUSHR, "lushr");
        INSN_LIST.put(Opcodes.IAND, "iand");
        INSN_LIST.put(Opcodes.LAND, "land");
        INSN_LIST.put(Opcodes.IOR, "ior");
        INSN_LIST.put(Opcodes.LOR, "lor");
        INSN_LIST.put(Opcodes.IXOR, "ixor");
        INSN_LIST.put(Opcodes.LXOR, "lxor");
        INSN_LIST.put(Opcodes.I2L, "i2l");
        INSN_LIST.put(Opcodes.I2F, "i2f");
        INSN_LIST.put(Opcodes.I2D, "i2d");
        INSN_LIST.put(Opcodes.L2I, "l2i");
        INSN_LIST.put(Opcodes.L2F, "l2f");
        INSN_LIST.put(Opcodes.L2D, "l2d");
        INSN_LIST.put(Opcodes.F2I, "f2i");
        INSN_LIST.put(Opcodes.F2L, "f2l");
        INSN_LIST.put(Opcodes.F2D, "f2d");
        INSN_LIST.put(Opcodes.D2I, "d2i");
        INSN_LIST.put(Opcodes.D2L, "d2l");
        INSN_LIST.put(Opcodes.D2F, "d2f");
        INSN_LIST.put(Opcodes.I2B, "i2b");
        INSN_LIST.put(Opcodes.I2C, "i2c");
        INSN_LIST.put(Opcodes.I2S, "i2s");
        INSN_LIST.put(Opcodes.LCMP, "lcmp");
        INSN_LIST.put(Opcodes.FCMPL, "fcmpl");
        INSN_LIST.put(Opcodes.FCMPG, "fcmpg");
        INSN_LIST.put(Opcodes.DCMPL, "dcmpl");
        INSN_LIST.put(Opcodes.DCMPG, "dcmpg");
        INSN_LIST.put(Opcodes.IRETURN, "ireturn");
        INSN_LIST.put(Opcodes.LRETURN, "lreturn");
        INSN_LIST.put(Opcodes.FRETURN, "freturn");
        INSN_LIST.put(Opcodes.DRETURN, "dreturn");
        INSN_LIST.put(Opcodes.ARETURN, "areturn");
        INSN_LIST.put(Opcodes.RETURN, "return");
        INSN_LIST.put(Opcodes.ARRAYLENGTH, "arraylength");
        INSN_LIST.put(Opcodes.ATHROW, "athrow");
        INSN_LIST.put(Opcodes.MONITORENTER, "monitorenter");
        INSN_LIST.put(Opcodes.MONITOREXIT, "monitorexit");

        INSN_LIST.put(Opcodes.IFNULL, "ifnull");
        INSN_LIST.put(Opcodes.IFNONNULL, "ifnonnull");
        INSN_LIST.put(Opcodes.IFEQ, "ifeq");
        INSN_LIST.put(Opcodes.IFNE, "ifne");
        INSN_LIST.put(Opcodes.IFLT, "iflt");
        INSN_LIST.put(Opcodes.IFGE, "ifge");
        INSN_LIST.put(Opcodes.IFGT, "ifgt");
        INSN_LIST.put(Opcodes.IFLE, "ifle");
        INSN_LIST.put(Opcodes.IF_ACMPEQ, "if_acmpeq");
        INSN_LIST.put(Opcodes.IF_ACMPNE, "if_acmpne");
        INSN_LIST.put(Opcodes.IF_ICMPEQ, "if_icmpeq");
        INSN_LIST.put(Opcodes.IF_ICMPNE, "if_icmpne");
        INSN_LIST.put(Opcodes.IF_ICMPLT, "if_icmplt");
        INSN_LIST.put(Opcodes.IF_ICMPGE, "if_icmpge");
        INSN_LIST.put(Opcodes.IF_ICMPGT, "if_icmpgt");
        INSN_LIST.put(Opcodes.IF_ICMPLE, "if_icmple");
        INSN_LIST.put(Opcodes.GOTO, "goto");
        INSN_LIST.put(Opcodes.JSR, "jsr");
        INSN_LIST.put(Opcodes.RET, "ret");

        INSN_LIST.put(Opcodes.GETSTATIC, "getstatic");
        INSN_LIST.put(Opcodes.PUTSTATIC, "putstatic");
        INSN_LIST.put(Opcodes.GETFIELD, "getfield");
        INSN_LIST.put(Opcodes.PUTFIELD, "putfield");

        INSN_LIST.put(Opcodes.INVOKEVIRTUAL, "invokevirtual");
        INSN_LIST.put(Opcodes.INVOKESPECIAL, "invokespecial");
        INSN_LIST.put(Opcodes.INVOKESTATIC, "invokestatic");
        INSN_LIST.put(Opcodes.INVOKEINTERFACE, "invokeinterface");

        INSN_LIST.put(Opcodes.NEW, "new");
        INSN_LIST.put(Opcodes.NEWARRAY, "newarray");
        INSN_LIST.put(Opcodes.ANEWARRAY, "anewarray");
        INSN_LIST.put(Opcodes.CHECKCAST, "checkcast");
        INSN_LIST.put(Opcodes.INSTANCEOF, "instanceof");

        INSN_LIST.put(Opcodes.BIPUSH, "bipush");
        INSN_LIST.put(Opcodes.SIPUSH, "sipush");

        INSN_LIST.put(Opcodes.ALOAD, "aload");
        INSN_LIST.put(Opcodes.LLOAD, "lload");
        INSN_LIST.put(Opcodes.FLOAD, "fload");
        INSN_LIST.put(Opcodes.DLOAD, "dload");
        INSN_LIST.put(Opcodes.ILOAD, "iload");
        INSN_LIST.put(Opcodes.ASTORE, "astore");
        INSN_LIST.put(Opcodes.LSTORE, "lstore");
        INSN_LIST.put(Opcodes.FSTORE, "fstore");
        INSN_LIST.put(Opcodes.DSTORE, "dstore");
        INSN_LIST.put(Opcodes.ISTORE, "istore");
    }

    private final DecompileContext context;

    private Label currentLabel;
    private final Map<Label, String> labelNames = new HashMap<>();

    public MethodDecompileVisitor(DecompileContext context) {
        super(Opcodes.ASM9);
        this.context = context;
    }

    private String getOrNameLabel(Label label) {
        if (labelNames.containsKey(label))
            return labelNames.get(label);
        String name = "L" + labelNames.size();
        labelNames.put(label, name);
        return name;
    }

    @Override
    public void visitLabel(Label label) {
        if (currentLabel != null)
            context.popBlock();
        currentLabel = label;
        context.pushBlock(new TextBlockElement("label %s".formatted(getOrNameLabel(label))));
    }

    @Override
    public void visitInsn(int opcode) {
        context.addElement(new TextElement(INSN_LIST.get(opcode)));
    }

    @Override
    public void visitVarInsn(int opcode, int varIndex) {
        context.addElement(new TextElement("%s %d".formatted(INSN_LIST.get(opcode), varIndex)));
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
        if (opcode != Opcodes.NEWARRAY)
            context.addElement(new TextElement("%s %d".formatted(INSN_LIST.get(opcode), operand)));
        else
            context.addElement(new TextElement("%s %s".formatted(INSN_LIST.get(opcode), ARRAY_TYPES[operand - 4])));
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        context.addElement(new TextElement("%s %s".formatted(INSN_LIST.get(opcode), type)));
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
        context.addElement(new TextElement("%s %s".formatted(INSN_LIST.get(opcode), getOrNameLabel(label))));
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        context.addElement(new TextElement("%s %s.%s%s %s".formatted(INSN_LIST.get(opcode), owner, name, descriptor, isInterface)));
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
        TextBlockElement block = new TextBlockElement("invokedynamic %s %s".formatted(name, descriptor));
        block.addElement(new TextElement("bootstrap %s".formatted(TextElement.getHandleValue(bootstrapMethodHandle))));
        for (Object arg : bootstrapMethodArguments) {
            if (arg instanceof ConstantDynamic)
                block.addElement(TextElement.getConstantDynamicValue("dynamic", (ConstantDynamic) arg));
            else
                block.addElement(new TextElement("constant %s".formatted(TextElement.getPlainValue(arg))));
        }
        context.addElement(block);
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        context.addElement(new TextElement("%s %s.%s %s".formatted(INSN_LIST.get(opcode), owner, name, descriptor)));
    }

    @Override
    public void visitLdcInsn(Object value) {
        if (value instanceof ConstantDynamic)
            context.addElement(TextElement.getConstantDynamicValue("ldc_dynamic", (ConstantDynamic) value));
        else
            context.addElement(new TextElement("ldc %s".formatted(TextElement.getPlainValue(value))));
    }

    @Override
    public void visitIincInsn(int var, int increment) {
        context.addElement(new TextElement("iinc %d %d".formatted(var, increment)));
    }

    @Override
    public void visitMultiANewArrayInsn(String descriptor, int numDimensions) {
        context.addElement(new TextElement("multianewarray %s %d".formatted(descriptor, numDimensions)));
    }

    @Override
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        String keysStr = Arrays.stream(keys).mapToObj(String::valueOf).collect(Collectors.joining(","));
        String labelsStr = Arrays.stream(labels).map(this::getOrNameLabel).collect(Collectors.joining(","));
        context.addElement(new TextElement("lookupswitch %s %s %s".formatted(getOrNameLabel(dflt), keysStr, labelsStr)));
    }

    @Override
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
        String labelsStr = Arrays.stream(labels).map(this::getOrNameLabel).collect(Collectors.joining(","));
        context.addElement(new TextElement("tableswitch %d %d %s %s".formatted(min, max, getOrNameLabel(dflt), labelsStr)));
    }

    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        context.addElement(new TextElement("try_catch %s %s %s %s".formatted(
                getOrNameLabel(start), getOrNameLabel(end), getOrNameLabel(handler), type)));
    }

    @Override
    public void visitLineNumber(int line, Label start) {
        context.addElement(new TextElement("line %s %d".formatted(getOrNameLabel(start), line)));
    }

    @Override
    public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
        String base = "local %s %s %d %s %s".formatted(
                getOrNameLabel(start), getOrNameLabel(end), index, name, descriptor);
        if (signature != null)
            base += " %s".formatted(signature);
        context.addElement(new TextElement(base));
    }

    @Override
    public void visitParameter(String name, int access) {
        context.addElement(new TextElement("parameter %s %s".formatted(AccessFlags.PARAMETER.getFlags(access), name)));
    }

    @Override
    public void visitAnnotableParameterCount(int parameterCount, boolean visible) {
        context.addElement(new TextElement("annotation_param_count %d %s".formatted(parameterCount, visible)));
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        TextBlockElement block = new TextBlockElement("annotation method %s %s".formatted(descriptor, visible));
        context.pushBlock(block);
        return new AnnotationDecompileVisitor(context);
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        TextBlockElement block = new TextBlockElement("annotation method_type %s %s %s %s".formatted(
                METHOD_TYPE_REFERENCE_KINDS.get(typeRef), typePath, descriptor, visible));
        context.pushBlock(block);
        return new AnnotationDecompileVisitor(context);
    }

    @Override
    public AnnotationVisitor visitAnnotationDefault() {
        TextBlockElement block = new TextBlockElement("annotation default");
        context.pushBlock(block);
        return new AnnotationDecompileVisitor(context);
    }

    @Override
    public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath,
                                                          Label[] start, Label[] end, int[] index, String descriptor, boolean visible) {
        String startStr = Arrays.stream(start).map(this::getOrNameLabel).collect(Collectors.joining(","));
        String endStr = Arrays.stream(end).map(this::getOrNameLabel).collect(Collectors.joining(","));
        String indexStr = Arrays.stream(index).mapToObj(String::valueOf).collect(Collectors.joining(","));
        TextBlockElement block = new TextBlockElement("annotation local %s %s %s %s %s %s %s".formatted(
                LOCAL_VARIABLE_TYPE_REFERENCE_KINDS.get(typeRef), typePath, startStr, endStr, indexStr, descriptor, visible));
        context.pushBlock(block);
        return new AnnotationDecompileVisitor(context);
    }

    @Override
    public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        TextBlockElement block = new TextBlockElement("annotation try_catch %s %s %s".formatted(
                typePath, descriptor, visible));
        context.pushBlock(block);
        return new AnnotationDecompileVisitor(context);
    }

    @Override
    public AnnotationVisitor visitParameterAnnotation(int parameter, String descriptor, boolean visible) {
        TextBlockElement block = new TextBlockElement("annotation parameter %d %s %s".formatted(
                parameter, descriptor, visible));
        context.pushBlock(block);
        return new AnnotationDecompileVisitor(context);
    }

    @Override
    public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        TextBlockElement block = new TextBlockElement("annotation insn %s %s %s %s".formatted(
                INSN_TYPE_REFERENCE_KINDS.get(typeRef), typePath, descriptor, visible));
        context.pushBlock(block);
        return new AnnotationDecompileVisitor(context);
    }

    @Override
    public void visitEnd() {
        if (currentLabel != null)
            context.popBlock();
        context.popBlock();
    }
}
