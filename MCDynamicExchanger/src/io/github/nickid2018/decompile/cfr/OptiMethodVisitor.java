package io.github.nickid2018.decompile.cfr;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class OptiMethodVisitor extends MethodVisitor implements Opcodes {

    private boolean shouldDeleteValues;
    private boolean nowDelete = false;

    public OptiMethodVisitor(boolean shouldDeleteValues, MethodVisitor methodVisitor) {
        super(ASM9, methodVisitor);
        this.shouldDeleteValues = shouldDeleteValues;
    }

    @Override
    public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
        // Delete Local Variables (CFR bug)
    }

    // Delete $VALUE assignment
    // ASM Code:
    // INVOKEVIRTUAL XXX $values ()[XXX
    // PUTSTATIC XXX

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        if(name.equals("$values") && shouldDeleteValues){
            nowDelete = true;
            return;
        }
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        if(nowDelete){
            nowDelete = false;
            return;
        }
        super.visitFieldInsn(opcode, owner, name, descriptor);
    }
}
