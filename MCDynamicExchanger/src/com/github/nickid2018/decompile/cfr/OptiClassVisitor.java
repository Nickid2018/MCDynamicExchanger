package com.github.nickid2018.decompile.cfr;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class OptiClassVisitor extends ClassVisitor implements Opcodes {

    public OptiClassVisitor(ClassVisitor classVisitor) {
        super(ASM9, classVisitor);
    }

    private boolean isEnum;

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        isEnum = (access & ACC_ENUM) != 0;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if (isEnum && (name.equals("$values") || name.equals("valueOf") || name.equals("values")))
            return null;
        return new OptiMethodVisitor(isEnum && name.equals("<clinit>"), super.visitMethod(access, name, descriptor, signature, exceptions));
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        if (isEnum && name.equals("$VALUES"))
            return null;
        return super.visitField(access, name, descriptor, signature, value);
    }
}
