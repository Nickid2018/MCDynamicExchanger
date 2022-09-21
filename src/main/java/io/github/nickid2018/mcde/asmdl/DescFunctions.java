package io.github.nickid2018.mcde.asmdl;

import io.github.nickid2018.mcde.asmdl.block.ClassDescBlock;
import io.github.nickid2018.mcde.asmdl.block.FieldDescBlock;
import io.github.nickid2018.mcde.asmdl.block.LabelDescBlock;
import io.github.nickid2018.mcde.asmdl.block.MethodDescBlock;
import io.github.nickid2018.mcde.asmdl.function.*;
import org.objectweb.asm.Opcodes;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class DescFunctions {

    public static final Map<String, DescFunction<?>> FUNCTIONS = new HashMap<>();

    public static final ClassDescBlock CLASS = register(new ClassDescBlock());
    public static final FieldDescBlock FIELD = register(new FieldDescBlock());
    public static final MethodDescBlock METHOD = register(new MethodDescBlock());
    public static final LabelDescBlock LABEL = register(new LabelDescBlock());

    public static final MethodZeroArgumentFunction RETURN = register(
            new MethodZeroArgumentFunction("return", Opcodes.RETURN));
    public static final MethodZeroArgumentFunction IRETURN = register(
            new MethodZeroArgumentFunction("ireturn", Opcodes.IRETURN));
    public static final MethodZeroArgumentFunction LRETURN = register(
            new MethodZeroArgumentFunction("lreturn", Opcodes.LRETURN));
    public static final MethodZeroArgumentFunction FRETURN = register(
            new MethodZeroArgumentFunction("freturn", Opcodes.FRETURN));
    public static final MethodZeroArgumentFunction DRETURN = register(
            new MethodZeroArgumentFunction("dreturn", Opcodes.DRETURN));
    public static final MethodZeroArgumentFunction ARETURN = register(
            new MethodZeroArgumentFunction("areturn", Opcodes.ARETURN));
    public static final MethodZeroArgumentFunction ATHROW = register(
            new MethodZeroArgumentFunction("athrow", Opcodes.ATHROW));
    public static final MethodZeroArgumentFunction MONITORENTER = register(
            new MethodZeroArgumentFunction("monitorenter", Opcodes.MONITORENTER));
    public static final MethodZeroArgumentFunction MONITOREXIT = register(
            new MethodZeroArgumentFunction("monitorexit", Opcodes.MONITOREXIT));
    public static final MethodZeroArgumentFunction NOP = register(
            new MethodZeroArgumentFunction("nop", Opcodes.NOP));
    public static final MethodZeroArgumentFunction ACONST_NULL = register(
            new MethodZeroArgumentFunction("aconst_null", Opcodes.ACONST_NULL));
    public static final MethodZeroArgumentFunction ICONST_M1 = register(
            new MethodZeroArgumentFunction("iconst_m1", Opcodes.ICONST_M1));
    public static final MethodZeroArgumentFunction ICONST_0 = register(
            new MethodZeroArgumentFunction("iconst_0", Opcodes.ICONST_0));
    public static final MethodZeroArgumentFunction ICONST_1 = register(
            new MethodZeroArgumentFunction("iconst_1", Opcodes.ICONST_1));
    public static final MethodZeroArgumentFunction ICONST_2 = register(
            new MethodZeroArgumentFunction("iconst_2", Opcodes.ICONST_2));
    public static final MethodZeroArgumentFunction ICONST_3 = register(
            new MethodZeroArgumentFunction("iconst_3", Opcodes.ICONST_3));
    public static final MethodZeroArgumentFunction ICONST_4 = register(
            new MethodZeroArgumentFunction("iconst_4", Opcodes.ICONST_4));
    public static final MethodZeroArgumentFunction ICONST_5 = register(
            new MethodZeroArgumentFunction("iconst_5", Opcodes.ICONST_5));
    public static final MethodZeroArgumentFunction LCONST_0 = register(
            new MethodZeroArgumentFunction("lconst_0", Opcodes.LCONST_0));
    public static final MethodZeroArgumentFunction LCONST_1 = register(
            new MethodZeroArgumentFunction("lconst_1", Opcodes.LCONST_1));
    public static final MethodZeroArgumentFunction FCONST_0 = register(
            new MethodZeroArgumentFunction("fconst_0", Opcodes.FCONST_0));
    public static final MethodZeroArgumentFunction FCONST_1 = register(
            new MethodZeroArgumentFunction("fconst_1", Opcodes.FCONST_1));
    public static final MethodZeroArgumentFunction FCONST_2 = register(
            new MethodZeroArgumentFunction("fconst_2", Opcodes.FCONST_2));
    public static final MethodZeroArgumentFunction DCONST_0 = register(
            new MethodZeroArgumentFunction("dconst_0", Opcodes.DCONST_0));
    public static final MethodZeroArgumentFunction DCONST_1 = register(
            new MethodZeroArgumentFunction("dconst_1", Opcodes.DCONST_1));
    public static final MethodZeroArgumentFunction IALOAD = register(
            new MethodZeroArgumentFunction("iaload", Opcodes.IALOAD));
    public static final MethodZeroArgumentFunction LALOAD = register(
            new MethodZeroArgumentFunction("laload", Opcodes.LALOAD));
    public static final MethodZeroArgumentFunction FALOAD = register(
            new MethodZeroArgumentFunction("faload", Opcodes.FALOAD));
    public static final MethodZeroArgumentFunction DALOAD = register(
            new MethodZeroArgumentFunction("daload", Opcodes.DALOAD));
    public static final MethodZeroArgumentFunction AALOAD = register(
            new MethodZeroArgumentFunction("aaload", Opcodes.AALOAD));
    public static final MethodZeroArgumentFunction BALOAD = register(
            new MethodZeroArgumentFunction("baload", Opcodes.BALOAD));
    public static final MethodZeroArgumentFunction CALOAD = register(
            new MethodZeroArgumentFunction("caload", Opcodes.CALOAD));
    public static final MethodZeroArgumentFunction SALOAD = register(
            new MethodZeroArgumentFunction("saload", Opcodes.SALOAD));
    public static final MethodZeroArgumentFunction IASTORE = register(
            new MethodZeroArgumentFunction("iastore", Opcodes.IASTORE));
    public static final MethodZeroArgumentFunction LASTORE = register(
            new MethodZeroArgumentFunction("lastore", Opcodes.LASTORE));
    public static final MethodZeroArgumentFunction FASTORE = register(
            new MethodZeroArgumentFunction("fastore", Opcodes.FASTORE));
    public static final MethodZeroArgumentFunction DASTORE = register(
            new MethodZeroArgumentFunction("dastore", Opcodes.DASTORE));
    public static final MethodZeroArgumentFunction AASTORE = register(
            new MethodZeroArgumentFunction("aastore", Opcodes.AASTORE));
    public static final MethodZeroArgumentFunction BASTORE = register(
            new MethodZeroArgumentFunction("bastore", Opcodes.BASTORE));
    public static final MethodZeroArgumentFunction CASTORE = register(
            new MethodZeroArgumentFunction("castore", Opcodes.CASTORE));
    public static final MethodZeroArgumentFunction SASTORE = register(
            new MethodZeroArgumentFunction("sastore", Opcodes.SASTORE));
    public static final MethodZeroArgumentFunction POP = register(
            new MethodZeroArgumentFunction("pop", Opcodes.POP));
    public static final MethodZeroArgumentFunction POP2 = register(
            new MethodZeroArgumentFunction("pop2", Opcodes.POP2));
    public static final MethodZeroArgumentFunction DUP = register(
            new MethodZeroArgumentFunction("dup", Opcodes.DUP));
    public static final MethodZeroArgumentFunction DUP_X1 = register(
            new MethodZeroArgumentFunction("dup_x1", Opcodes.DUP_X1));
    public static final MethodZeroArgumentFunction DUP_X2 = register(
            new MethodZeroArgumentFunction("dup_x2", Opcodes.DUP_X2));
    public static final MethodZeroArgumentFunction DUP2 = register(
            new MethodZeroArgumentFunction("dup2", Opcodes.DUP2));
    public static final MethodZeroArgumentFunction DUP2_X1 = register(
            new MethodZeroArgumentFunction("dup2_x1", Opcodes.DUP2_X1));
    public static final MethodZeroArgumentFunction DUP2_X2 = register(
            new MethodZeroArgumentFunction("dup2_x2", Opcodes.DUP2_X2));
    public static final MethodZeroArgumentFunction SWAP = register(
            new MethodZeroArgumentFunction("swap", Opcodes.SWAP));
    public static final MethodZeroArgumentFunction IADD = register(
            new MethodZeroArgumentFunction("iadd", Opcodes.IADD));
    public static final MethodZeroArgumentFunction LADD = register(
            new MethodZeroArgumentFunction("ladd", Opcodes.LADD));
    public static final MethodZeroArgumentFunction FADD = register(
            new MethodZeroArgumentFunction("fadd", Opcodes.FADD));
    public static final MethodZeroArgumentFunction DADD = register(
            new MethodZeroArgumentFunction("dadd", Opcodes.DADD));
    public static final MethodZeroArgumentFunction ISUB = register(
            new MethodZeroArgumentFunction("isub", Opcodes.ISUB));
    public static final MethodZeroArgumentFunction LSUB = register(
            new MethodZeroArgumentFunction("lsub", Opcodes.LSUB));
    public static final MethodZeroArgumentFunction FSUB = register(
            new MethodZeroArgumentFunction("fsub", Opcodes.FSUB));
    public static final MethodZeroArgumentFunction DSUB = register(
            new MethodZeroArgumentFunction("dsub", Opcodes.DSUB));
    public static final MethodZeroArgumentFunction IMUL = register(
            new MethodZeroArgumentFunction("imul", Opcodes.IMUL));
    public static final MethodZeroArgumentFunction LMUL = register(
            new MethodZeroArgumentFunction("lmul", Opcodes.LMUL));
    public static final MethodZeroArgumentFunction FMUL = register(
            new MethodZeroArgumentFunction("fmul", Opcodes.FMUL));
    public static final MethodZeroArgumentFunction DMUL = register(
            new MethodZeroArgumentFunction("dmul", Opcodes.DMUL));
    public static final MethodZeroArgumentFunction IDIV = register(
            new MethodZeroArgumentFunction("idiv", Opcodes.IDIV));
    public static final MethodZeroArgumentFunction LDIV = register(
            new MethodZeroArgumentFunction("ldiv", Opcodes.LDIV));
    public static final MethodZeroArgumentFunction FDIV = register(
            new MethodZeroArgumentFunction("fdiv", Opcodes.FDIV));
    public static final MethodZeroArgumentFunction DDIV = register(
            new MethodZeroArgumentFunction("ddiv", Opcodes.DDIV));
    public static final MethodZeroArgumentFunction IREM = register(
            new MethodZeroArgumentFunction("irem", Opcodes.IREM));
    public static final MethodZeroArgumentFunction LREM = register(
            new MethodZeroArgumentFunction("lrem", Opcodes.LREM));
    public static final MethodZeroArgumentFunction FREM = register(
            new MethodZeroArgumentFunction("frem", Opcodes.FREM));
    public static final MethodZeroArgumentFunction DREM = register(
            new MethodZeroArgumentFunction("drem", Opcodes.DREM));
    public static final MethodZeroArgumentFunction INEG = register(
            new MethodZeroArgumentFunction("ineg", Opcodes.INEG));
    public static final MethodZeroArgumentFunction LNEG = register(
            new MethodZeroArgumentFunction("lneg", Opcodes.LNEG));
    public static final MethodZeroArgumentFunction FNEG = register(
            new MethodZeroArgumentFunction("fneg", Opcodes.FNEG));
    public static final MethodZeroArgumentFunction DNEG = register(
            new MethodZeroArgumentFunction("dneg", Opcodes.DNEG));
    public static final MethodZeroArgumentFunction ISHL = register(
            new MethodZeroArgumentFunction("ishl", Opcodes.ISHL));
    public static final MethodZeroArgumentFunction LSHL = register(
            new MethodZeroArgumentFunction("lshl", Opcodes.LSHL));
    public static final MethodZeroArgumentFunction ISHR = register(
            new MethodZeroArgumentFunction("ishr", Opcodes.ISHR));
    public static final MethodZeroArgumentFunction LSHR = register(
            new MethodZeroArgumentFunction("lshr", Opcodes.LSHR));
    public static final MethodZeroArgumentFunction IUSHR = register(
            new MethodZeroArgumentFunction("iushr", Opcodes.IUSHR));
    public static final MethodZeroArgumentFunction LUSHR = register(
            new MethodZeroArgumentFunction("lushr", Opcodes.LUSHR));
    public static final MethodZeroArgumentFunction IAND = register(
            new MethodZeroArgumentFunction("iand", Opcodes.IAND));
    public static final MethodZeroArgumentFunction LAND = register(
            new MethodZeroArgumentFunction("land", Opcodes.LAND));
    public static final MethodZeroArgumentFunction IOR = register(
            new MethodZeroArgumentFunction("ior", Opcodes.IOR));
    public static final MethodZeroArgumentFunction LOR = register(
            new MethodZeroArgumentFunction("lor", Opcodes.LOR));
    public static final MethodZeroArgumentFunction IXOR = register(
            new MethodZeroArgumentFunction("ixor", Opcodes.IXOR));
    public static final MethodZeroArgumentFunction LXOR = register(
            new MethodZeroArgumentFunction("lxor", Opcodes.LXOR));
    public static final MethodZeroArgumentFunction I2L = register(
            new MethodZeroArgumentFunction("i2l", Opcodes.I2L));
    public static final MethodZeroArgumentFunction I2F = register(
            new MethodZeroArgumentFunction("i2f", Opcodes.I2F));
    public static final MethodZeroArgumentFunction I2D = register(
            new MethodZeroArgumentFunction("i2d", Opcodes.I2D));
    public static final MethodZeroArgumentFunction L2I = register(
            new MethodZeroArgumentFunction("l2i", Opcodes.L2I));
    public static final MethodZeroArgumentFunction L2F = register(
            new MethodZeroArgumentFunction("l2f", Opcodes.L2F));
    public static final MethodZeroArgumentFunction L2D = register(
            new MethodZeroArgumentFunction("l2d", Opcodes.L2D));
    public static final MethodZeroArgumentFunction F2I = register(
            new MethodZeroArgumentFunction("f2i", Opcodes.F2I));
    public static final MethodZeroArgumentFunction F2L = register(
            new MethodZeroArgumentFunction("f2l", Opcodes.F2L));
    public static final MethodZeroArgumentFunction F2D = register(
            new MethodZeroArgumentFunction("f2d", Opcodes.F2D));
    public static final MethodZeroArgumentFunction D2I = register(
            new MethodZeroArgumentFunction("d2i", Opcodes.D2I));
    public static final MethodZeroArgumentFunction D2L = register(
            new MethodZeroArgumentFunction("d2l", Opcodes.D2L));
    public static final MethodZeroArgumentFunction D2F = register(
            new MethodZeroArgumentFunction("d2f", Opcodes.D2F));
    public static final MethodZeroArgumentFunction I2B = register(
            new MethodZeroArgumentFunction("i2b", Opcodes.I2B));
    public static final MethodZeroArgumentFunction I2C = register(
            new MethodZeroArgumentFunction("i2c", Opcodes.I2C));
    public static final MethodZeroArgumentFunction I2S = register(
            new MethodZeroArgumentFunction("i2s", Opcodes.I2S));
    public static final MethodZeroArgumentFunction LCMP = register(
            new MethodZeroArgumentFunction("lcmp", Opcodes.LCMP));
    public static final MethodZeroArgumentFunction FCMPL = register(
            new MethodZeroArgumentFunction("fcmpl", Opcodes.FCMPL));
    public static final MethodZeroArgumentFunction FCMPG = register(
            new MethodZeroArgumentFunction("fcmpg", Opcodes.FCMPG));
    public static final MethodZeroArgumentFunction DCMPL = register(
            new MethodZeroArgumentFunction("dcmpl", Opcodes.DCMPL));
    public static final MethodZeroArgumentFunction DCMPG = register(
            new MethodZeroArgumentFunction("dcmpg", Opcodes.DCMPG));
    public static final MethodZeroArgumentFunction ARRAYLENGTH = register(
            new MethodZeroArgumentFunction("arraylength", Opcodes.ARRAYLENGTH));


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
            new MethodInvokeFunction("invokevirtual", Opcodes.INVOKEVIRTUAL, false));
    public static final MethodInvokeFunction INVOKESPECIAL = register(
            new MethodInvokeFunction("invokespecial", Opcodes.INVOKESPECIAL, false));
    public static final MethodInvokeFunction INVOKESTATIC = register(
            new MethodInvokeFunction("invokestatic", Opcodes.INVOKESTATIC, false));
    public static final MethodInvokeFunction INVOKEINTERFACE = register(
            new MethodInvokeFunction("invokeinterface", Opcodes.INVOKEINTERFACE, true));

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

    public static <T extends DescFunction<?>> T register(T function) {
        FUNCTIONS.put(function.name(), function);
        return function;
    }
}
