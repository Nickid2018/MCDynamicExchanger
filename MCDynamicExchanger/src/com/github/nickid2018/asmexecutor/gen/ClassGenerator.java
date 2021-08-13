package com.github.nickid2018.asmexecutor.gen;

import com.github.nickid2018.asmexecutor.ASMClass;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import java.util.HashMap;
import java.util.Map;

import static org.objectweb.asm.Opcodes.*;

public class ClassGenerator {

    public final String cname;
    public String details;
    public ASMClass clazz;
    public byte[] generated;

    public ClassGenerator(String details, String cname) {
        this.details = details;
        this.cname = cname;
    }

    public ClassGenerator(String details) {
        this.details = details;
        cname = "_ASM_gen_" + (int) (Integer.MAX_VALUE * Math.random());
    }

    public static int genInnerClass(ClassWriter writer, String detail) {
        String[] all = detail.split(" ");
        writer.visitInnerClass(all[1], all[2], all[3], Integer.parseInt(all[4]));
        return 1;
    }

    public static int genOuterClass(ClassWriter writer, String detail) {
        String[] all = detail.split(" ");
        writer.visitOuterClass(all[1], all[2], all[3]);
        return 1;
    }

    public static int genMethod(ClassWriter writer, String[] details, int offset) {
        int counter = 1;
        String description = details[offset];
        String[] aftSplit = description.split(" ");
        MethodVisitor mwriter = writer.visitMethod(Integer.parseInt(aftSplit[1]), aftSplit[2], aftSplit[3].trim(), null,
                null);
        Map<Integer, Label> labels = new HashMap<>();
        for (int i = offset; ; ) {
            String now = details[++i];
            counter++;
            if (!MethodGenerator.genNextLine(mwriter, now, labels))
                break;
        }
        return counter;
    }

    public static int genField(ClassWriter writer, String detail) {
        String[] aftSplit = detail.split(" ", 5);
        FieldVisitor fwriter;
        if (aftSplit.length == 4) {
            fwriter = writer.visitField(Integer.parseInt(aftSplit[1]), aftSplit[2], aftSplit[3].trim(), null, null);
        } else {
            try {
                fwriter = writer.visitField(Integer.parseInt(aftSplit[1]), aftSplit[2], aftSplit[3].trim(), null,
                        MethodGenerator.parseObjects("{" + aftSplit[4].replaceFirst(" ", ",") + "}"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        fwriter.visitEnd();
        return 1;
    }

    public static boolean genFieldNextLine(FieldVisitor writer, String detail) {
        return false;
    }

    public static int genDefaultInit(ClassWriter writer) {
        MethodVisitor mv = writer.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        mv.visitInsn(RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
        return 1;
    }

    public void make() {
        String[] splits = details.split("\n", 2);
        details = splits[1];
        String[] splitss = splits[0].split(" ", 3);
        ClassWriter writer = new ClassWriter(0);
        writer.visit(V1_8, Integer.parseInt(splitss[0]), cname, null, splitss[1],
                splitss[2].trim().isEmpty() ? null : splitss[2].trim().split(" "));
        details = details.replaceAll("[Tt][Hh][Ii][Ss]_[Cc][Ll][Aa][Ss][Ss]", cname);
        String[] arrdetail = details.split("\n");
        for (int index = 0; index < arrdetail.length; ) {
            String detail = arrdetail[index];
            String type = detail.split(" ")[0];
            switch (type.toUpperCase()) {
                case "METHOD":
                    index += genMethod(writer, arrdetail, index);
                    break;
                case "FIELD":
                    index += genField(writer, detail);
                    break;
                case "INNERCLASS":
                    index += genInnerClass(writer, detail);
                    break;
                case "OUTERCLASS":
                    index += genOuterClass(writer, detail);
                    break;
                case "DEFAULT_INIT":
                    index += genDefaultInit(writer);
                    break;
                default:
                    throw new IllegalArgumentException("Uncongnized Tag:" + type);
            }
        }
        writer.visitEnd();
        generated = writer.toByteArray();
    }

    public ASMClass generateClass() {
        return clazz = ASMClass.newClass(generated);
    }

    public Class<?> getGeneratedClass() {
        return clazz.clazz;
    }
}
