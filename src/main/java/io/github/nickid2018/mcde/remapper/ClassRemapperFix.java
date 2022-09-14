package io.github.nickid2018.mcde.remapper;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.Remapper;

public class ClassRemapperFix extends ClassRemapper {

    public ClassRemapperFix(ClassVisitor classVisitor, Remapper remapper) {
        super(classVisitor, remapper);
    }

    @Override
    protected MethodVisitor createMethodRemapper(MethodVisitor methodVisitor) {
        return new MethodRemapperFix(methodVisitor, remapper);
    }
}
