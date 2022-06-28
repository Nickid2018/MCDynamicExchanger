package io.github.nickid2018.asmifier;

import org.objectweb.asm.*;

public class FieldAnalyzer extends FieldVisitor implements Opcodes {

    private final ASMifier asmifier;

    public FieldAnalyzer(ASMifier asmifier) {
        super(ASM9);
        this.asmifier = asmifier;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        asmifier.line("{");
        asmifier.indent++;
        asmifier.line("AnnotationVisitor av = av.visitAnnotation(%s, %s);", asmifier.quote(descriptor), visible);
        return new AnnotationAnalyzer(asmifier, 0);
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        String typeRefStr;
        if (!asmifier.noConvertConstants && typeRef == TypeReference.FIELD)
            typeRefStr = "TypeReference.FIELD";
        else
            typeRefStr = typeRef + "";
        asmifier.line("{");
        asmifier.indent++;
        asmifier.line("AnnotationVisitor av = fv.visitTypeAnnotation(%s, TypePath.fromString(%s), %s, %s);",
                typeRefStr, asmifier.quote(typePath.toString()), asmifier.quote(descriptor), visible);
        return new AnnotationAnalyzer(asmifier, 0);
    }

    @Override
    public void visitAttribute(Attribute attribute) {
        // Unsupported
    }

    @Override
    public void visitEnd() {
        asmifier.line("fv.visitEnd();");
        asmifier.indent--;
        asmifier.line("}");
    }
}
