package io.github.nickid2018.asmifier;

import org.objectweb.asm.*;

public class RecordComponentAnalyzer extends RecordComponentVisitor implements Opcodes {

    private final ASMifier asmifier;

    public RecordComponentAnalyzer(ASMifier asmifier) {
        super(ASM9);
        this.asmifier = asmifier;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        asmifier.line("{");
        asmifier.indent++;
        asmifier.line("AnnotationVisitor av = rcv.visitAnnotation(%s, %s);", asmifier.quote(descriptor), visible);
        return new AnnotationAnalyzer(asmifier, 0);
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        String typeRefStr = typeRef + "";
        if (!asmifier.noConvertConstants)
            switch (typeRef) {
                case TypeReference.CLASS_TYPE_PARAMETER:
                    typeRefStr = "TypeReference.CLASS_TYPE_PARAMETER";
                    break;
                case TypeReference.CLASS_TYPE_PARAMETER_BOUND:
                    typeRefStr = "TypeReference.CLASS_TYPE_PARAMETER_BOUND";
                    break;
                case TypeReference.CLASS_EXTENDS:
                    typeRefStr = "TypeReference.CLASS_EXTENDS";
                    break;
            }
        asmifier.line("{");
        asmifier.indent++;
        asmifier.line("AnnotationVisitor av = rcv.visitTypeAnnotation(%s, TypePath.fromString(%s), %s, %s);",
                typeRefStr, asmifier.quote(typePath.toString()), asmifier.quote(descriptor), visible);
        return new AnnotationAnalyzer(asmifier, 0);
    }

    @Override
    public void visitAttribute(Attribute attribute) {
        // Unsupported
    }

    @Override
    public void visitEnd() {
        asmifier.line("rcv.visitEnd();");
        asmifier.indent--;
        asmifier.line("}");
    }
}
