package io.github.nickid2018.mcremap.optimize;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.Remapper;

public class OptimizedClassRemapper extends ClassRemapper {

    public OptimizedClassRemapper(ClassVisitor classVisitor, Remapper remapper) {
        super(classVisitor, remapper);
    }

    @Override
    public void visitSource(String source, String debug) {
        super.visitSource(source, "Remapped by Nickid2018");
    }

    @Override
    protected MethodVisitor createMethodRemapper(MethodVisitor methodVisitor) {
        return new OptimizedMethodRemapper(methodVisitor, remapper);
    }
}
