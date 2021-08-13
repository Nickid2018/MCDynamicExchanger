package com.github.nickid2018.asmexecutor.gen;

import org.apache.commons.io.IOUtils;
import org.objectweb.asm.*;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.objectweb.asm.Opcodes.ASM6;

public class MethodTransformer extends ClassVisitor {

    public static final Map<Integer, String> OPCODES = new HashMap<>();
    public static final String[] FRAME_SIG = new String[]{"F_NEW", "F_FULL", "F_APPEND", "F_CHOP", "F_SAME",
            "F_SAME1"};
    public static final String[] FRAME_LOCAL_STACK = new String[]{"TOP", "INTEGER", "FLOAT", "DOUBLE", "LONG", "NULL",
            "UNINITIALIZED_THIS"};
    public static final String[] HANDLE_TAGS = new String[]{"H_GETFIELD", "H_GETSTATIC", "H_PUTFIELD", "H_PUTSTATIC",
            "H_INVOKEVIRTUAL", "H_INVOKESTATIC", "H_INVOKESPECIAL", "H_NEWINVOKESPECIAL", "H_INVOKEINTERFACE"};

    static {
        Field[] allField = Opcodes.class.getDeclaredFields();
        for (Field f : allField) {
            if (f.getName().startsWith("ASM") || f.getName().startsWith("V1_") || f.getName().startsWith("ACC_")
                    || f.getName().startsWith("T_") || f.getName().startsWith("H_") || f.getName().startsWith("F_")
                    || f.getDeclaringClass().equals(Integer.class))
                continue;
            try {
                OPCODES.put(f.getInt(null), f.getName());
            } catch (Exception ignored) {
            }
        }
    }

    private final byte[] asmcode;
    private String clazzName;

    public MethodTransformer(byte[] asmcode) {
        super(ASM6);
        this.asmcode = asmcode;
    }

    public MethodTransformer(String asmcode) throws IOException {
        super(ASM6);
        this.asmcode = IOUtils.toByteArray(new FileInputStream(asmcode));
    }

    public void execute() {
        ClassReader reader = new ClassReader(asmcode);
        reader.accept(this, ClassReader.SKIP_DEBUG);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        clazzName = name;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        if (name.equals("<init>") && desc.equals("()V"))
            return super.visitMethod(access, name, desc, signature, exceptions);
        try {
            return new MethodWriter(desc, name, clazzName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static class MethodWriter extends MethodVisitor {

        protected final String clazzName;
        protected final List<Label> labels = new ArrayList<>();
        protected StringWriter writer;
        protected String desc;
        protected String name;
        protected File file;
        protected boolean makeFile;

        public MethodWriter(String desc, String name, String clazzName) throws IOException {
            this(desc, name, clazzName, true);
        }

        public MethodWriter(String desc, String name, String clazzName, boolean makeFile) throws IOException {
            super(ASM6);
            this.clazzName = clazzName;
            if (makeFile) {
                String[] sps = clazzName.split("/");
                file = new File(sps[sps.length - 1] + "$" + name + ".asm");
                if (!file.exists())
                    file.createNewFile();
                writer = new StringWriter();
            }
            this.makeFile = makeFile;
            this.desc = desc;
            this.name = name;
            if (makeFile)
                if (name.startsWith("lambda$"))
                    IOUtils.write(name + " " + desc + "\n", writer);
                else
                    IOUtils.write(desc + "\n", writer);
        }

        @Override
        public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
            String locals = formatForFrame(local);
            String stacks = formatForFrame(stack);
            try {
                IOUtils.write("FRAME " + FRAME_SIG[type + 1] + " " + nLocal + " " + locals + " " + nStack + " " + stacks
                        + "\n", writer);
            } catch (IOException ignored) {
            }
        }

        private String formatForFrame(Object[] obj) {
            StringBuilder sb = new StringBuilder();
            boolean isNull = true;
            for (Object o : obj) {
                isNull &= o == null;
            }
            if (obj == null || obj.length == 0 || isNull)
                sb.append("-");
            else {
                for (Object o : obj) {
                    if (o == null)
                        continue;
                    if (o instanceof Integer)
                        sb.append(FRAME_LOCAL_STACK[(Integer) o]).append(",");
                    else
                        sb.append(o).append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
            }
            return sb.toString();
        }

        @Override
        public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
            return super.visitTypeAnnotation(typeRef, typePath, desc, visible);
        }

        @Override
        public void visitInsn(int opcode) {
            try {
                IOUtils.write(OPCODES.get(opcode) + "\n", writer);
            } catch (IOException ignored) {
            }
        }

        @Override
        public void visitIntInsn(int opcode, int operand) {
            try {
                IOUtils.write(OPCODES.get(opcode) + " " + operand + "\n", writer);
            } catch (IOException ignored) {
            }
        }

        @Override
        public void visitVarInsn(int opcode, int var) {
            try {
                IOUtils.write(OPCODES.get(opcode) + " " + var + "\n", writer);
            } catch (IOException ignored) {
            }
        }

        @Override
        public void visitTypeInsn(int opcode, String type) {
            try {
                IOUtils.write(OPCODES.get(opcode) + " " + type + "\n", writer);
            } catch (IOException ignored) {
            }
        }

        @Override
        public void visitFieldInsn(int opcode, String owner, String name, String desc) {
            try {
                IOUtils.write(OPCODES.get(opcode) + " " + owner + " " + name + " " + desc + "\n", writer);
            } catch (IOException ignored) {
            }
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
            try {
                IOUtils.write(OPCODES.get(opcode) + " " + (owner.equals(clazzName) ? "THIS_CLASS" : owner) + " "
                        + (owner.equals(clazzName) && name.equals(this.name) && desc.equals(this.desc) ? "THIS_METHOD"
                        : name)
                        + " " + desc + " " + (itf ? "TRUE" : "FALSE") + "\n", writer);
            } catch (IOException ignored) {
            }
        }

        @Override
        public void visitJumpInsn(int opcode, Label label) {
            try {
                if (!labels.contains(label)) {
                    labels.add(label);
                    IOUtils.write("DEFLABEL " + labels.indexOf(label) + "\n", writer);
                }
                IOUtils.write(OPCODES.get(opcode) + " " + labels.indexOf(label) + "\n", writer);
            } catch (IOException ignored) {
            }
        }

        @Override
        public void visitLabel(Label label) {
            try {
                if (!labels.contains(label)) {
                    labels.add(label);
                    IOUtils.write("DEFLABEL " + labels.indexOf(label) + "\n", writer);
                }
                IOUtils.write("VISLABEL " + labels.indexOf(label) + "\n", writer);
            } catch (IOException ignored) {
            }
        }

        @Override
        public void visitLdcInsn(Object cst) {
            // Types:Class,String
            try {
                if (cst instanceof String)
                    IOUtils.write("LDC STRING " + ((String) cst).replaceAll("\n", "\\\\NEWLINE\\\\") + "\n", writer);
                if (cst instanceof Type)
                    IOUtils.write("LDC CLASS " + ((Type) cst).getInternalName() + "\n", writer);
                if (cst instanceof Integer)
                    IOUtils.write("LDC INTEGER " + cst + "\n", writer);
                if (cst instanceof Float)
                    IOUtils.write("LDC FLOAT " + cst + "\n", writer);
                if (cst instanceof Long)
                    IOUtils.write("LDC LONG " + cst + "\n", writer);
                if (cst instanceof Double)
                    IOUtils.write("LDC DOUBLE " + cst + "\n", writer);
                if (cst instanceof Handle)
                    IOUtils.write("LDC HANDLE " + parseString((Handle) cst) + "\n", writer);
            } catch (IOException ignored) {
            }
        }

        @Override
        public void visitIincInsn(int var, int increment) {
            try {
                IOUtils.write("IINC " + var + " " + increment + "\n", writer);
            } catch (IOException ignored) {
            }
        }

        @Override
        public void visitMultiANewArrayInsn(String desc, int dims) {
            try {
                IOUtils.write("MULTIANEWARRAY " + desc + " " + dims + "\n", writer);
            } catch (IOException ignored) {
            }
        }

        @Override
        public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
            try {
                if (!this.labels.contains(dflt)) {
                    this.labels.add(dflt);
                    IOUtils.write("DEFLABEL " + this.labels.indexOf(dflt) + "\n", writer);
                }
                for (Label label : labels) {
                    if (!this.labels.contains(label)) {
                        this.labels.add(label);
                        IOUtils.write("DEFLABEL " + this.labels.indexOf(label) + "\n", writer);
                    }
                }
                if (labels.length > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (Label label : labels) {
                        sb.append(this.labels.indexOf(label)).append(",");
                    }
                    sb.deleteCharAt(sb.length() - 1);
                    IOUtils.write("TABLESWITCH " + min + " " + max + " " + this.labels.indexOf(dflt) + " " + sb + "\n",
                            writer);
                } else
                    IOUtils.write("TABLESWITCH " + min + " " + max + " " + this.labels.indexOf(dflt) + " -" + "\n",
                            writer);
            } catch (IOException ignored) {
            }
        }

        @Override
        public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
            try {
                if (!this.labels.contains(dflt)) {
                    this.labels.add(dflt);
                    IOUtils.write("DEFLABEL " + this.labels.indexOf(dflt) + "\n", writer);
                }
                for (Label label : labels) {
                    if (!this.labels.contains(label)) {
                        this.labels.add(label);
                        IOUtils.write("DEFLABEL " + this.labels.indexOf(label) + "\n", writer);
                    }
                }
                StringBuilder sb2 = new StringBuilder();
                if (keys.length > 0) {
                    for (int i : keys) {
                        sb2.append(i).append(",");
                    }
                    sb2.deleteCharAt(sb2.length() - 1);
                } else
                    sb2.append("-");
                if (labels.length > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (Label label : labels) {
                        sb.append(this.labels.indexOf(label)).append(",");
                    }
                    sb.deleteCharAt(sb.length() - 1);
                    IOUtils.write("LOOKUPSWITCH " + this.labels.indexOf(dflt) + " " + sb2 + " " + sb + "\n", writer);
                } else
                    IOUtils.write("LOOKUPSWITCH " + " " + this.labels.indexOf(dflt) + " " + sb2 + " -" + "\n", writer);
            } catch (IOException ignored) {
            }
        }

        @Override
        public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
            try {
                IOUtils.write("INVOKEDYNAMIC " + name + " " + desc + " " + parseString(bsm) + " ", writer);
                StringBuilder sb = new StringBuilder();
                for (Object o : bsmArgs) {
                    sb.append("{");
                    if (o instanceof Integer)
                        sb.append("INTEGER,").append(o);
                    else if (o instanceof Float)
                        sb.append("FLOAT,").append(o);
                    else if (o instanceof Long)
                        sb.append("LONG,").append(o);
                    else if (o instanceof Double)
                        sb.append("DOUBLE,").append(o);
                    else if (o instanceof String)
                        sb.append("STRING,").append(o);
                    else if (o instanceof Type)
                        sb.append("TYPE,").append(o);
                    else if (o instanceof Handle)
                        sb.append("HANDLE,").append(parseString((Handle) o));
                    sb.append("},");
                }
                if (bsmArgs.length > 0)
                    sb.deleteCharAt(sb.length() - 1);
                IOUtils.write(sb + "\n", writer);
            } catch (IOException ignored) {
            }
        }

        private String parseString(Handle h) {
            return HANDLE_TAGS[h.getTag() - 1] + "," + (h.getOwner().equals(clazzName) ? "THIS_CLASS" : h.getOwner())
                    + "," + h.getName() + "," + h.getDesc() + "," + h.isInterface();
        }

        @Override
        public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
            try {
                if (!labels.contains(start)) {
                    labels.add(start);
                    IOUtils.write("DEFLABEL " + labels.indexOf(start) + "\n", writer);
                }
                if (!labels.contains(end)) {
                    labels.add(end);
                    IOUtils.write("DEFLABEL " + labels.indexOf(end) + "\n", writer);
                }
                if (!labels.contains(handler)) {
                    labels.add(handler);
                    IOUtils.write("DEFLABEL " + labels.indexOf(handler) + "\n", writer);
                }
                IOUtils.write("TRY " + labels.indexOf(start) + " " + labels.indexOf(end) + " " + labels.indexOf(handler)
                        + " " + type + "\n", writer);
            } catch (IOException ignored) {
            }
        }

        @Override
        public void visitMaxs(int maxStack, int maxLocals) {
            try {
                IOUtils.write("END " + maxStack + " " + maxLocals + "\n", writer);
                writer.flush();
                if (makeFile) {
                    FileWriter fw = new FileWriter(file);
                    fw.write(writer.toString());
                    fw.close();
                }
            } catch (IOException ignored) {
            }
        }

    }
}
