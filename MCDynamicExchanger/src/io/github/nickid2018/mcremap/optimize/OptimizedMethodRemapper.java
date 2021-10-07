package io.github.nickid2018.mcremap.optimize;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.MethodRemapper;
import org.objectweb.asm.commons.Remapper;

public class OptimizedMethodRemapper extends MethodRemapper {

    private static boolean lineNumberOutput;

    protected OptimizedMethodRemapper(MethodVisitor methodVisitor, Remapper remapper) {
        super(methodVisitor, remapper);
    }

    public static void setNoLineNumbers() {
        lineNumberOutput = false;
    }

    @Override
    public void visitLineNumber(int line, Label start) {
        if (lineNumberOutput)
            super.visitLineNumber(line, start);
    }
}
