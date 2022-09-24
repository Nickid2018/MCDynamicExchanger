package io.github.nickid2018.mcde.asmdl;

import io.github.nickid2018.mcde.asmdl.block.*;
import io.github.nickid2018.mcde.asmdl.function.*;
import org.objectweb.asm.Opcodes;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class DescFunctions {

    public static final Map<String, DescFunction> FUNCTIONS = new HashMap<>();

    public static final ClassDescBlock CLASS = register(new ClassDescBlock());
    public static final FieldDescBlock FIELD = register(new FieldDescBlock());
    public static final RecordComponentDescBlock RECORD_COMPONENT = register(new RecordComponentDescBlock());
    public static final MethodDescBlock METHOD = register(new MethodDescBlock());
    public static final LabelDescBlock LABEL = register(new LabelDescBlock());
    public static final InvokeDynamicDescBlock INVOKEDYNAMIC = register(new InvokeDynamicDescBlock());
    public static final ConstantDynamicDescBlock CONSTANTDYNAMIC = register(new ConstantDynamicDescBlock());
    public static final AnnotationDescBlock ANNOTATION = register(new AnnotationDescBlock());
    public static final AnnotationArrayDescBlock ANNOTATION_ARRAY = register(new AnnotationArrayDescBlock());

    public static final MethodInsnFunction RETURN = register(
            new MethodInsnFunction("return", Opcodes.RETURN));
    public static final MethodInsnFunction IRETURN = register(
            new MethodInsnFunction("ireturn", Opcodes.IRETURN));
    public static final MethodInsnFunction LRETURN = register(
            new MethodInsnFunction("lreturn", Opcodes.LRETURN));
    public static final MethodInsnFunction FRETURN = register(
            new MethodInsnFunction("freturn", Opcodes.FRETURN));
    public static final MethodInsnFunction DRETURN = register(
            new MethodInsnFunction("dreturn", Opcodes.DRETURN));
    public static final MethodInsnFunction ARETURN = register(
            new MethodInsnFunction("areturn", Opcodes.ARETURN));
    public static final MethodInsnFunction ATHROW = register(
            new MethodInsnFunction("athrow", Opcodes.ATHROW));
    public static final MethodInsnFunction MONITORENTER = register(
            new MethodInsnFunction("monitorenter", Opcodes.MONITORENTER));
    public static final MethodInsnFunction MONITOREXIT = register(
            new MethodInsnFunction("monitorexit", Opcodes.MONITOREXIT));
    public static final MethodInsnFunction NOP = register(
            new MethodInsnFunction("nop", Opcodes.NOP));
    public static final MethodInsnFunction ACONST_NULL = register(
            new MethodInsnFunction("aconst_null", Opcodes.ACONST_NULL));
    public static final MethodInsnFunction ICONST_M1 = register(
            new MethodInsnFunction("iconst_m1", Opcodes.ICONST_M1));
    public static final MethodInsnFunction ICONST_0 = register(
            new MethodInsnFunction("iconst_0", Opcodes.ICONST_0));
    public static final MethodInsnFunction ICONST_1 = register(
            new MethodInsnFunction("iconst_1", Opcodes.ICONST_1));
    public static final MethodInsnFunction ICONST_2 = register(
            new MethodInsnFunction("iconst_2", Opcodes.ICONST_2));
    public static final MethodInsnFunction ICONST_3 = register(
            new MethodInsnFunction("iconst_3", Opcodes.ICONST_3));
    public static final MethodInsnFunction ICONST_4 = register(
            new MethodInsnFunction("iconst_4", Opcodes.ICONST_4));
    public static final MethodInsnFunction ICONST_5 = register(
            new MethodInsnFunction("iconst_5", Opcodes.ICONST_5));
    public static final MethodInsnFunction LCONST_0 = register(
            new MethodInsnFunction("lconst_0", Opcodes.LCONST_0));
    public static final MethodInsnFunction LCONST_1 = register(
            new MethodInsnFunction("lconst_1", Opcodes.LCONST_1));
    public static final MethodInsnFunction FCONST_0 = register(
            new MethodInsnFunction("fconst_0", Opcodes.FCONST_0));
    public static final MethodInsnFunction FCONST_1 = register(
            new MethodInsnFunction("fconst_1", Opcodes.FCONST_1));
    public static final MethodInsnFunction FCONST_2 = register(
            new MethodInsnFunction("fconst_2", Opcodes.FCONST_2));
    public static final MethodInsnFunction DCONST_0 = register(
            new MethodInsnFunction("dconst_0", Opcodes.DCONST_0));
    public static final MethodInsnFunction DCONST_1 = register(
            new MethodInsnFunction("dconst_1", Opcodes.DCONST_1));
    public static final MethodInsnFunction IALOAD = register(
            new MethodInsnFunction("iaload", Opcodes.IALOAD));
    public static final MethodInsnFunction LALOAD = register(
            new MethodInsnFunction("laload", Opcodes.LALOAD));
    public static final MethodInsnFunction FALOAD = register(
            new MethodInsnFunction("faload", Opcodes.FALOAD));
    public static final MethodInsnFunction DALOAD = register(
            new MethodInsnFunction("daload", Opcodes.DALOAD));
    public static final MethodInsnFunction AALOAD = register(
            new MethodInsnFunction("aaload", Opcodes.AALOAD));
    public static final MethodInsnFunction BALOAD = register(
            new MethodInsnFunction("baload", Opcodes.BALOAD));
    public static final MethodInsnFunction CALOAD = register(
            new MethodInsnFunction("caload", Opcodes.CALOAD));
    public static final MethodInsnFunction SALOAD = register(
            new MethodInsnFunction("saload", Opcodes.SALOAD));
    public static final MethodInsnFunction IASTORE = register(
            new MethodInsnFunction("iastore", Opcodes.IASTORE));
    public static final MethodInsnFunction LASTORE = register(
            new MethodInsnFunction("lastore", Opcodes.LASTORE));
    public static final MethodInsnFunction FASTORE = register(
            new MethodInsnFunction("fastore", Opcodes.FASTORE));
    public static final MethodInsnFunction DASTORE = register(
            new MethodInsnFunction("dastore", Opcodes.DASTORE));
    public static final MethodInsnFunction AASTORE = register(
            new MethodInsnFunction("aastore", Opcodes.AASTORE));
    public static final MethodInsnFunction BASTORE = register(
            new MethodInsnFunction("bastore", Opcodes.BASTORE));
    public static final MethodInsnFunction CASTORE = register(
            new MethodInsnFunction("castore", Opcodes.CASTORE));
    public static final MethodInsnFunction SASTORE = register(
            new MethodInsnFunction("sastore", Opcodes.SASTORE));
    public static final MethodInsnFunction POP = register(
            new MethodInsnFunction("pop", Opcodes.POP));
    public static final MethodInsnFunction POP2 = register(
            new MethodInsnFunction("pop2", Opcodes.POP2));
    public static final MethodInsnFunction DUP = register(
            new MethodInsnFunction("dup", Opcodes.DUP));
    public static final MethodInsnFunction DUP_X1 = register(
            new MethodInsnFunction("dup_x1", Opcodes.DUP_X1));
    public static final MethodInsnFunction DUP_X2 = register(
            new MethodInsnFunction("dup_x2", Opcodes.DUP_X2));
    public static final MethodInsnFunction DUP2 = register(
            new MethodInsnFunction("dup2", Opcodes.DUP2));
    public static final MethodInsnFunction DUP2_X1 = register(
            new MethodInsnFunction("dup2_x1", Opcodes.DUP2_X1));
    public static final MethodInsnFunction DUP2_X2 = register(
            new MethodInsnFunction("dup2_x2", Opcodes.DUP2_X2));
    public static final MethodInsnFunction SWAP = register(
            new MethodInsnFunction("swap", Opcodes.SWAP));
    public static final MethodInsnFunction IADD = register(
            new MethodInsnFunction("iadd", Opcodes.IADD));
    public static final MethodInsnFunction LADD = register(
            new MethodInsnFunction("ladd", Opcodes.LADD));
    public static final MethodInsnFunction FADD = register(
            new MethodInsnFunction("fadd", Opcodes.FADD));
    public static final MethodInsnFunction DADD = register(
            new MethodInsnFunction("dadd", Opcodes.DADD));
    public static final MethodInsnFunction ISUB = register(
            new MethodInsnFunction("isub", Opcodes.ISUB));
    public static final MethodInsnFunction LSUB = register(
            new MethodInsnFunction("lsub", Opcodes.LSUB));
    public static final MethodInsnFunction FSUB = register(
            new MethodInsnFunction("fsub", Opcodes.FSUB));
    public static final MethodInsnFunction DSUB = register(
            new MethodInsnFunction("dsub", Opcodes.DSUB));
    public static final MethodInsnFunction IMUL = register(
            new MethodInsnFunction("imul", Opcodes.IMUL));
    public static final MethodInsnFunction LMUL = register(
            new MethodInsnFunction("lmul", Opcodes.LMUL));
    public static final MethodInsnFunction FMUL = register(
            new MethodInsnFunction("fmul", Opcodes.FMUL));
    public static final MethodInsnFunction DMUL = register(
            new MethodInsnFunction("dmul", Opcodes.DMUL));
    public static final MethodInsnFunction IDIV = register(
            new MethodInsnFunction("idiv", Opcodes.IDIV));
    public static final MethodInsnFunction LDIV = register(
            new MethodInsnFunction("ldiv", Opcodes.LDIV));
    public static final MethodInsnFunction FDIV = register(
            new MethodInsnFunction("fdiv", Opcodes.FDIV));
    public static final MethodInsnFunction DDIV = register(
            new MethodInsnFunction("ddiv", Opcodes.DDIV));
    public static final MethodInsnFunction IREM = register(
            new MethodInsnFunction("irem", Opcodes.IREM));
    public static final MethodInsnFunction LREM = register(
            new MethodInsnFunction("lrem", Opcodes.LREM));
    public static final MethodInsnFunction FREM = register(
            new MethodInsnFunction("frem", Opcodes.FREM));
    public static final MethodInsnFunction DREM = register(
            new MethodInsnFunction("drem", Opcodes.DREM));
    public static final MethodInsnFunction INEG = register(
            new MethodInsnFunction("ineg", Opcodes.INEG));
    public static final MethodInsnFunction LNEG = register(
            new MethodInsnFunction("lneg", Opcodes.LNEG));
    public static final MethodInsnFunction FNEG = register(
            new MethodInsnFunction("fneg", Opcodes.FNEG));
    public static final MethodInsnFunction DNEG = register(
            new MethodInsnFunction("dneg", Opcodes.DNEG));
    public static final MethodInsnFunction ISHL = register(
            new MethodInsnFunction("ishl", Opcodes.ISHL));
    public static final MethodInsnFunction LSHL = register(
            new MethodInsnFunction("lshl", Opcodes.LSHL));
    public static final MethodInsnFunction ISHR = register(
            new MethodInsnFunction("ishr", Opcodes.ISHR));
    public static final MethodInsnFunction LSHR = register(
            new MethodInsnFunction("lshr", Opcodes.LSHR));
    public static final MethodInsnFunction IUSHR = register(
            new MethodInsnFunction("iushr", Opcodes.IUSHR));
    public static final MethodInsnFunction LUSHR = register(
            new MethodInsnFunction("lushr", Opcodes.LUSHR));
    public static final MethodInsnFunction IAND = register(
            new MethodInsnFunction("iand", Opcodes.IAND));
    public static final MethodInsnFunction LAND = register(
            new MethodInsnFunction("land", Opcodes.LAND));
    public static final MethodInsnFunction IOR = register(
            new MethodInsnFunction("ior", Opcodes.IOR));
    public static final MethodInsnFunction LOR = register(
            new MethodInsnFunction("lor", Opcodes.LOR));
    public static final MethodInsnFunction IXOR = register(
            new MethodInsnFunction("ixor", Opcodes.IXOR));
    public static final MethodInsnFunction LXOR = register(
            new MethodInsnFunction("lxor", Opcodes.LXOR));
    public static final MethodInsnFunction I2L = register(
            new MethodInsnFunction("i2l", Opcodes.I2L));
    public static final MethodInsnFunction I2F = register(
            new MethodInsnFunction("i2f", Opcodes.I2F));
    public static final MethodInsnFunction I2D = register(
            new MethodInsnFunction("i2d", Opcodes.I2D));
    public static final MethodInsnFunction L2I = register(
            new MethodInsnFunction("l2i", Opcodes.L2I));
    public static final MethodInsnFunction L2F = register(
            new MethodInsnFunction("l2f", Opcodes.L2F));
    public static final MethodInsnFunction L2D = register(
            new MethodInsnFunction("l2d", Opcodes.L2D));
    public static final MethodInsnFunction F2I = register(
            new MethodInsnFunction("f2i", Opcodes.F2I));
    public static final MethodInsnFunction F2L = register(
            new MethodInsnFunction("f2l", Opcodes.F2L));
    public static final MethodInsnFunction F2D = register(
            new MethodInsnFunction("f2d", Opcodes.F2D));
    public static final MethodInsnFunction D2I = register(
            new MethodInsnFunction("d2i", Opcodes.D2I));
    public static final MethodInsnFunction D2L = register(
            new MethodInsnFunction("d2l", Opcodes.D2L));
    public static final MethodInsnFunction D2F = register(
            new MethodInsnFunction("d2f", Opcodes.D2F));
    public static final MethodInsnFunction I2B = register(
            new MethodInsnFunction("i2b", Opcodes.I2B));
    public static final MethodInsnFunction I2C = register(
            new MethodInsnFunction("i2c", Opcodes.I2C));
    public static final MethodInsnFunction I2S = register(
            new MethodInsnFunction("i2s", Opcodes.I2S));
    public static final MethodInsnFunction LCMP = register(
            new MethodInsnFunction("lcmp", Opcodes.LCMP));
    public static final MethodInsnFunction FCMPL = register(
            new MethodInsnFunction("fcmpl", Opcodes.FCMPL));
    public static final MethodInsnFunction FCMPG = register(
            new MethodInsnFunction("fcmpg", Opcodes.FCMPG));
    public static final MethodInsnFunction DCMPL = register(
            new MethodInsnFunction("dcmpl", Opcodes.DCMPL));
    public static final MethodInsnFunction DCMPG = register(
            new MethodInsnFunction("dcmpg", Opcodes.DCMPG));
    public static final MethodInsnFunction ARRAYLENGTH = register(
            new MethodInsnFunction("arraylength", Opcodes.ARRAYLENGTH));


    public static final MethodVarIntFunction ALOAD = register(
            new MethodVarIntFunction("aload", Opcodes.ALOAD));
    public static final MethodVarIntFunction ILOAD = register(
            new MethodVarIntFunction("iload", Opcodes.ILOAD));
    public static final MethodVarIntFunction LLOAD = register(
            new MethodVarIntFunction("lload", Opcodes.LLOAD));
    public static final MethodVarIntFunction FLOAD = register(
            new MethodVarIntFunction("fload", Opcodes.FLOAD));
    public static final MethodVarIntFunction DLOAD = register(
            new MethodVarIntFunction("dload", Opcodes.DLOAD));
    public static final MethodVarIntFunction ASTORE = register(
            new MethodVarIntFunction("astore", Opcodes.ASTORE));
    public static final MethodVarIntFunction ISTORE = register(
            new MethodVarIntFunction("istore", Opcodes.ISTORE));
    public static final MethodVarIntFunction LSTORE = register(
            new MethodVarIntFunction("lstore", Opcodes.LSTORE));
    public static final MethodVarIntFunction FSTORE = register(
            new MethodVarIntFunction("fstore", Opcodes.FSTORE));
    public static final MethodVarIntFunction DSTORE = register(
            new MethodVarIntFunction("dstore", Opcodes.DSTORE));
    public static final MethodVarIntFunction RET = register(
            new MethodVarIntFunction("ret", Opcodes.RET));

    public static final MethodInvokeFunction INVOKEVIRTUAL = register(
            new MethodInvokeFunction("invokevirtual", Opcodes.INVOKEVIRTUAL));
    public static final MethodInvokeFunction INVOKESPECIAL = register(
            new MethodInvokeFunction("invokespecial", Opcodes.INVOKESPECIAL));
    public static final MethodInvokeFunction INVOKESTATIC = register(
            new MethodInvokeFunction("invokestatic", Opcodes.INVOKESTATIC));
    public static final MethodInvokeFunction INVOKEINTERFACE = register(
            new MethodInvokeFunction("invokeinterface", Opcodes.INVOKEINTERFACE));

    public static final MethodFieldFunction GETFIELD = register(
            new MethodFieldFunction("getfield", Opcodes.GETFIELD));
    public static final MethodFieldFunction PUTFIELD = register(
            new MethodFieldFunction("putfield", Opcodes.PUTFIELD));
    public static final MethodFieldFunction GETSTATIC = register(
            new MethodFieldFunction("getstatic", Opcodes.GETSTATIC));
    public static final MethodFieldFunction PUTSTATIC = register(
            new MethodFieldFunction("putstatic", Opcodes.PUTSTATIC));

    public static final MethodIntFunction BIPUSH = register(
            new MethodIntFunction("bipush", Opcodes.BIPUSH));
    public static final MethodIntFunction SIPUSH = register(
            new MethodIntFunction("sipush", Opcodes.SIPUSH));

    public static final MethodNewArrayFunction NEWARRAY = register(new MethodNewArrayFunction());

    public static final MethodTypeFunction NEW = register(
            new MethodTypeFunction("new", Opcodes.NEW));
    public static final MethodTypeFunction ANEWARRAY = register(
            new MethodTypeFunction("anewarray", Opcodes.ANEWARRAY));
    public static final MethodTypeFunction CHECKCAST = register(
            new MethodTypeFunction("checkcast", Opcodes.CHECKCAST));
    public static final MethodTypeFunction INSTANCEOF = register(
            new MethodTypeFunction("instanceof", Opcodes.INSTANCEOF));

    public static final MethodIINCFunction IINC = register(new MethodIINCFunction());

    public static final MethodMultiArrayFunction MULTIANEWARRAY = register(new MethodMultiArrayFunction());

    public static final MethodJumpFunction GOTO = register(
            new MethodJumpFunction("goto", Opcodes.GOTO));
    public static final MethodJumpFunction JSR = register(
            new MethodJumpFunction("jsr", Opcodes.JSR));
    public static final MethodJumpFunction IFNULL = register(
            new MethodJumpFunction("ifnull", Opcodes.IFNULL));
    public static final MethodJumpFunction IFNONNULL = register(
            new MethodJumpFunction("ifnonnull", Opcodes.IFNONNULL));
    public static final MethodJumpFunction IFEQ = register(
            new MethodJumpFunction("ifeq", Opcodes.IFEQ));
    public static final MethodJumpFunction IFNE = register(
            new MethodJumpFunction("ifne", Opcodes.IFNE));
    public static final MethodJumpFunction IFLT = register(
            new MethodJumpFunction("iflt", Opcodes.IFLT));
    public static final MethodJumpFunction IFGE = register(
            new MethodJumpFunction("ifge", Opcodes.IFGE));
    public static final MethodJumpFunction IFGT = register(
            new MethodJumpFunction("ifgt", Opcodes.IFGT));
    public static final MethodJumpFunction IFLE = register(
            new MethodJumpFunction("ifle", Opcodes.IFLE));
    public static final MethodJumpFunction IF_ICMPEQ = register(
            new MethodJumpFunction("if_icmpeq", Opcodes.IF_ICMPEQ));
    public static final MethodJumpFunction IF_ICMPNE = register(
            new MethodJumpFunction("if_icmpne", Opcodes.IF_ICMPNE));
    public static final MethodJumpFunction IF_ICMPLT = register(
            new MethodJumpFunction("if_icmplt", Opcodes.IF_ICMPLT));
    public static final MethodJumpFunction IF_ICMPGE = register(
            new MethodJumpFunction("if_icmpge", Opcodes.IF_ICMPGE));
    public static final MethodJumpFunction IF_ICMPGT = register(
            new MethodJumpFunction("if_icmpgt", Opcodes.IF_ICMPGT));
    public static final MethodJumpFunction IF_ICMPLE = register(
            new MethodJumpFunction("if_icmple", Opcodes.IF_ICMPLE));
    public static final MethodJumpFunction IF_ACMPEQ = register(
            new MethodJumpFunction("if_acmpeq", Opcodes.IF_ACMPEQ));
    public static final MethodJumpFunction IF_ACMPNE = register(
            new MethodJumpFunction("if_acmpne", Opcodes.IF_ACMPNE));

    public static final MethodLookupSwitchFunction LOOKUPSWITCH = register(new MethodLookupSwitchFunction());

    public static final MethodTableSwitchFunction TABLESWITCH = register(new MethodTableSwitchFunction());

    public static final BootstrapFunction BOOTSTRAP = register(new BootstrapFunction());

    public static final ConstantFunction CONSTANT = register(new ConstantFunction());

    public static final MethodLDCFunction LDC = register(new MethodLDCFunction());

    public static final HandleFunction HANDLE = register(new HandleFunction());

    public static final MethodParameterFunction PARAMETER = register(new MethodParameterFunction());

    public static final MethodTryCatchFunction TRYCATCH = register(new MethodTryCatchFunction());

    public static final MethodLocalVariableFunction LOCALVARIABLE = register(new MethodLocalVariableFunction());

    public static final MethodLineFunction LINENUMBER = register(new MethodLineFunction());

    public static final AnnotationValueFunction ANNOTATION_VALUE = register(new AnnotationValueFunction());

    public static final AnnotationEnumFunction ANNOTATION_ENUM = register(new AnnotationEnumFunction());

    public static final AnnotationParameterCountFunction ANNOTATION_PARAMETER_COUNT = register(new AnnotationParameterCountFunction());

    public static final ClassNestHostFunction CLASS_NEST_HOST = register(new ClassNestHostFunction());

    public static final ClassNestMemberFunction CLASS_NEST_MEMBER = register(new ClassNestMemberFunction());

    public static final ClassInnerClassFunction CLASS_INNER_CLASS = register(new ClassInnerClassFunction());

    public static final ClassOuterClassFunction CLASS_OUTER_CLASS = register(new ClassOuterClassFunction());

    public static final ClassPermittedSubClassFunction CLASS_PERMITTED_SUBCLASS = register(new ClassPermittedSubClassFunction());

    public static <T extends DescFunction> T register(T function) {
        FUNCTIONS.put(function.name(), function);
        return function;
    }
}
