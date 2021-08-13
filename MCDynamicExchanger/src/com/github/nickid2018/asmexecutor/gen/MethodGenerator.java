package com.github.nickid2018.asmexecutor.gen;

import com.github.nickid2018.asmexecutor.ASMClass;
import org.objectweb.asm.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.objectweb.asm.Opcodes.*;

public class MethodGenerator {

    public final Class<?>[] params;
    public final String details;
    public final String[] dynamics;
    public ASMClass clazz;
    public String name;
    public String cname;

    public MethodGenerator(String details, Class<?>[] params, String... dynamics) {
        this.params = params;
        this.dynamics = dynamics;
        this.details = details;
    }

    public static void genMethod(String details, ClassWriter writer, boolean main, int access, MethodGenerator gen) {
        String[] split = details.split("\n", 2);
        details = split[1];
        details.trim();
        MethodVisitor mwriter;
        if (main)
            mwriter = writer.visitMethod(access,
                    gen.name = "_ASM_gen_meth_" + (int) (Integer.MAX_VALUE * Math.random()), split[0].trim(), null,
                    null);
        else
            mwriter = writer.visitMethod(access, split[0].trim().split(" ")[0].trim(),
                    split[0].trim().split(" ")[1].trim(), null, null);
        mwriter.visitCode();
        details = details.replaceAll("[Tt][Hh][Ii][Ss]_[Cc][Ll][Aa][Ss][Ss]", gen.cname);
        details = details.replaceAll("[Tt][Hh][Ii][Ss]_[Mm][Ee][Tt][Hh][Oo][Dd]", gen.name);
        Map<Integer, Label> labels = new HashMap<>();
        int line = 0;
        for (String detail : details.split("\n")) {
            line++;
            if (!genNextLine(mwriter, detail, labels))
                break;
        }
    }

    public static boolean genNextLine(MethodVisitor mwriter, String detail, Map<Integer, Label> labels) {
        if (detail.trim().isEmpty())
            return true;
        String[] infos = detail.replaceAll("\\\\NEWLINE\\\\", "\n").split(" ");
        try {
            if (infos[0].equalsIgnoreCase("end")) {
                mwriter.visitMaxs(Integer.parseInt(infos[1]), Integer.parseInt(infos[2].trim()));
                mwriter.visitEnd();
                return false;
            }
            if (infos[0].equalsIgnoreCase("deflabel")) {
                Label label = new Label();
                labels.put(Integer.parseInt(infos[1].trim()), label);
                return true;
            }
            if (infos[0].equalsIgnoreCase("vislabel")) {
                mwriter.visitLabel(labels.get(Integer.parseInt(infos[1].trim())));
                return true;
            }
            if (infos[0].equalsIgnoreCase("frame")) {
                Object[] local;
                if (infos[3].equals("-")) {
                    local = null;
                } else {
                    String[] objects = infos[3].split(",");
                    local = new Object[objects.length];
                    for (int i = 0; i < objects.length; i++) {
                        try {
                            local[i] = Opcodes.class.getField(objects[i].toUpperCase()).get(null);
                        } catch (Exception e) {
                            local[i] = objects[i];
                        }
                    }
                }
                Object[] stack;
                if (infos[5].equals("-")) {
                    stack = null;
                } else {
                    String[] objects = infos[5].trim().split(",");
                    stack = new Object[objects.length];
                    for (int i = 0; i < objects.length; i++) {
                        try {
                            stack[i] = Opcodes.class.getField(objects[i].toUpperCase()).get(null);
                        } catch (Exception e) {
                            stack[i] = objects[i];
                        }
                    }
                }
                mwriter.visitFrame(Opcodes.class.getField(infos[1].toUpperCase()).getInt(null),
                        Integer.parseInt(infos[2]), local, Integer.parseInt(infos[4]), stack);
                return true;
            }
            if (infos[0].equalsIgnoreCase("try")) {
                mwriter.visitTryCatchBlock(labels.get(Integer.parseInt(infos[1].trim())),
                        labels.get(Integer.parseInt(infos[2].trim())), labels.get(Integer.parseInt(infos[3].trim())),
                        infos[4].trim());
                return true;
            }
            int nowOpcode = Opcodes.class.getField(infos[0].toUpperCase().trim()).getInt(null);
            switch (nowOpcode) {
                case NOP:
                case ACONST_NULL:
                case ICONST_M1:
                case ICONST_0:
                case ICONST_1:
                case ICONST_2:
                case ICONST_3:
                case ICONST_4:
                case ICONST_5:
                case LCONST_0:
                case LCONST_1:
                case FCONST_0:
                case FCONST_1:
                case FCONST_2:
                case DCONST_0:
                case DCONST_1:
                case IALOAD:
                case LALOAD:
                case FALOAD:
                case DALOAD:
                case AALOAD:
                case BALOAD:
                case CALOAD:
                case SALOAD:
                case IASTORE:
                case LASTORE:
                case FASTORE:
                case DASTORE:
                case AASTORE:
                case BASTORE:
                case CASTORE:
                case SASTORE:
                case POP:
                case POP2:
                case DUP:
                case DUP_X1:
                case DUP_X2:
                case DUP2:
                case DUP2_X1:
                case DUP2_X2:
                case SWAP:
                case IADD:
                case LADD:
                case FADD:
                case DADD:
                case ISUB:
                case LSUB:
                case FSUB:
                case DSUB:
                case IMUL:
                case LMUL:
                case FMUL:
                case DMUL:
                case IDIV:
                case LDIV:
                case FDIV:
                case DDIV:
                case IREM:
                case LREM:
                case FREM:
                case DREM:
                case INEG:
                case LNEG:
                case FNEG:
                case DNEG:
                case ISHL:
                case LSHL:
                case ISHR:
                case LSHR:
                case IUSHR:
                case LUSHR:
                case IAND:
                case LAND:
                case IOR:
                case LOR:
                case IXOR:
                case LXOR:
                case I2L:
                case I2F:
                case I2D:
                case L2I:
                case L2F:
                case L2D:
                case F2I:
                case F2L:
                case F2D:
                case D2I:
                case D2L:
                case D2F:
                case I2B:
                case I2C:
                case I2S:
                case LCMP:
                case FCMPL:
                case FCMPG:
                case DCMPL:
                case DCMPG:
                case IRETURN:
                case LRETURN:
                case FRETURN:
                case DRETURN:
                case ARETURN:
                case RETURN:
                case ARRAYLENGTH:
                case ATHROW:
                case MONITORENTER:
                case MONITOREXIT:
                    mwriter.visitInsn(nowOpcode);
                    break;
                case BIPUSH:
                case SIPUSH:
                case NEWARRAY:
                    mwriter.visitIntInsn(nowOpcode, Integer.parseInt(infos[1].trim()));
                    break;
                case LDC:
                    String[] sps = detail.split(" ", 3);
                    switch (sps[1].toLowerCase()) {
                        case "string":
                            mwriter.visitLdcInsn(
                                    sps.length == 3 ? sps[2].replaceAll("\r", "").replaceAll("\\\\NEWLINE\\\\", "\n") : "");
                            break;
                        case "class":
                            mwriter.visitLdcInsn(Type.getObjectType(sps[2].trim()));
                            break;
                        case "integer":
                            mwriter.visitLdcInsn(Integer.parseInt(sps[2].trim()));
                            break;
                        case "float":
                            mwriter.visitLdcInsn(Float.parseFloat(sps[2].trim()));
                            break;
                        case "long":
                            mwriter.visitLdcInsn(Long.parseLong(sps[2].trim()));
                            break;
                        case "double":
                            mwriter.visitLdcInsn(Double.parseDouble(sps[2].trim()));
                            break;
                        case "handle":
                            mwriter.visitLdcInsn(parseHandle(sps[2].trim()));
                            break;
                        default:
                            throw new IllegalArgumentException("Unknown LDC type");
                    }
                    break;
                case IINC:
                    mwriter.visitIincInsn(Integer.parseInt(infos[1]), Integer.parseInt(infos[2].trim()));
                    break;
                case IFEQ:
                case IFNE:
                case IFLT:
                case IFGE:
                case IFGT:
                case IFLE:
                case IF_ICMPEQ:
                case IF_ICMPNE:
                case IF_ICMPLT:
                case IF_ICMPGE:
                case IF_ICMPGT:
                case IF_ICMPLE:
                case IF_ACMPEQ:
                case IF_ACMPNE:
                case GOTO:
                case JSR:
                case IFNULL:
                case IFNONNULL:
                    mwriter.visitJumpInsn(nowOpcode, labels.get(Integer.parseInt(infos[1].trim())));
                    break;
                case ILOAD:
                case LLOAD:
                case FLOAD:
                case DLOAD:
                case ALOAD:
                case ISTORE:
                case LSTORE:
                case FSTORE:
                case DSTORE:
                case ASTORE:
                case RET:
                    mwriter.visitVarInsn(nowOpcode, Integer.parseInt(infos[1].trim()));
                    break;
                case GETSTATIC:
                case PUTSTATIC:
                case GETFIELD:
                case PUTFIELD:
                    mwriter.visitFieldInsn(nowOpcode, infos[1].trim(), infos[2].trim(), infos[3].trim());
                    break;
                case TABLESWITCH:
                    String[] tos = infos[4].split(",");
                    if (infos[4].equals("-"))
                        mwriter.visitTableSwitchInsn(Integer.parseInt(infos[1]), Integer.parseInt(infos[2]),
                                labels.get(Integer.parseInt(infos[3])));
                    else {
                        Label[] alls = new Label[tos.length];
                        for (int i = 0; i < alls.length; i++) {
                            alls[i] = labels.get(Integer.parseInt(tos[i].trim()));
                        }
                        mwriter.visitTableSwitchInsn(Integer.parseInt(infos[1].trim()), Integer.parseInt(infos[2].trim()),
                                labels.get(Integer.parseInt(infos[3].trim())), alls);
                    }
                    break;
                case LOOKUPSWITCH:
                    String[] ints = infos[2].split(",");
                    int[] keys;
                    if (infos[2].equals("-"))
                        keys = null;
                    else {
                        keys = new int[ints.length];
                        for (int i = 0; i < ints.length; i++) {
                            keys[i] = Integer.parseInt(ints[i].trim());
                        }
                    }
                    String[] labelss = infos[3].split(",");
                    Label[] labelall;
                    if (infos[3].equals("-"))
                        labelall = null;
                    else {
                        labelall = new Label[ints.length];
                        for (int i = 0; i < ints.length; i++) {
                            labelall[i] = labels.get(Integer.parseInt(labelss[i].trim()));
                        }
                    }
                    mwriter.visitLookupSwitchInsn(labels.get(Integer.parseInt(infos[1].trim())), keys, labelall);
                    break;
                case INVOKEVIRTUAL:
                case INVOKESPECIAL:
                case INVOKESTATIC:
                case INVOKEINTERFACE:
                    mwriter.visitMethodInsn(nowOpcode, infos[1].trim(), infos[2].trim(), infos[3].trim(),
                            Boolean.parseBoolean(infos[4].trim()));
                    break;
                case INVOKEDYNAMIC:
                    String[] special = detail.split(" ", 5);
                    mwriter.visitInvokeDynamicInsn(infos[1].trim(), infos[2].trim(), parseHandle(infos[3].trim()),
                            parseObjects(special[4].trim()));
                    break;
                case NEW:
                case ANEWARRAY:
                case CHECKCAST:
                case INSTANCEOF:
                    mwriter.visitTypeInsn(nowOpcode, infos[1].trim());
                    break;
                case MULTIANEWARRAY:
                    mwriter.visitMultiANewArrayInsn(infos[1].trim(), Integer.parseInt(infos[2].trim()));
                    break;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public static Handle parseHandle(String str)
            throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        String[] sps = str.split(",");
        int tag = Opcodes.class.getDeclaredField(sps[0]).getInt(null);
        return new Handle(tag, sps[1], sps[2], sps[3], Boolean.parseBoolean(sps[4]));
    }

    public static Object[] parseObjects(String str)
            throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        String[] all = split(str);
        Object[] objs = new Object[all.length];
        for (int i = 0; i < all.length; i++) {
            String[] subsplit = all[i].substring(1, all[i].length() - 1).split(",", 2);
            switch (subsplit[0].toUpperCase()) {
                case "INTEGER":
                    objs[i] = Integer.parseInt(subsplit[1]);
                    break;
                case "FLOAT":
                    objs[i] = Float.parseFloat(subsplit[1]);
                    break;
                case "LONG":
                    objs[i] = Long.parseLong(subsplit[1]);
                    break;
                case "DOUBLE":
                    objs[i] = Double.parseDouble(subsplit[1]);
                    break;
                case "STRING":
                    objs[i] = subsplit[1];
                    break;
                case "TYPE":
                    objs[i] = Type.getType(subsplit[1]);
                    break;
                case "HANDLE":
                    objs[i] = parseHandle(subsplit[1]);
                    break;
                default:
                    throw new IllegalArgumentException("Uncompile Tag");
            }
        }
        return objs;
    }

    public static String[] split(String in) {
        ArrayList<String> al = new ArrayList<>();
        boolean isRound = false;
        int begin = 0;
        for (int i = 0; i < in.length(); i++) {
            char at = in.charAt(i);
            if (at == '{')
                isRound = true;
            if (at == '}')
                isRound = false;
            if (at == ',' && !isRound) {
                al.add(in.substring(begin, i).trim());
                begin = i + 1;
            }
        }
        if (begin != in.length())
            al.add(in.substring(begin).trim());
        // To array
        Object[] o = al.toArray();
        return Arrays.copyOf(o, o.length, String[].class);
    }

    public void make() {
        ClassWriter writer = new ClassWriter(0);
        writer.visit(V1_8, ACC_PUBLIC | ACC_SUPER, cname = "_ASM_gen_" + (int) (Integer.MAX_VALUE * Math.random()),
                null, "java/lang/Object", null);
        genMethod(details, writer, true, ACC_PUBLIC | ACC_STATIC, this);
        for (String dynamic : dynamics) {
            genMethod(dynamic, writer, false, ACC_PRIVATE | ACC_STATIC | ACC_SYNTHETIC, this);
        }
        writer.visitEnd();
        clazz = ASMClass.newClass(writer.toByteArray());
    }

    public Object invoke(Object... obj) throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, SecurityException, NoSuchMethodException {
        return clazz.clazz.getDeclaredMethod(name, params).invoke(null, obj);
    }

    public String getName() {
        return name;
    }
}
