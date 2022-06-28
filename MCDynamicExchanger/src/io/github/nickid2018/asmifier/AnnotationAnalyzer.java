package io.github.nickid2018.asmifier;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class AnnotationAnalyzer extends AnnotationVisitor implements Opcodes {

    private final ASMifier asmifier;
    private final int level;
    private final String name;

    public AnnotationAnalyzer(ASMifier asmifier, int level) {
        super(ASM9);
        this.asmifier = asmifier;
        this.level = level;
        name = level == 0 ? "av" : "av" + level;
    }

    @Override
    public void visit(String name, Object value) {
        String valueStr;
        if(value instanceof Type)
            valueStr = String.format("Type.getType(%s)", asmifier.quote(value.toString()));
        else if (value instanceof String)
            valueStr = asmifier.quote(value.toString());
        else if (value instanceof Character)
            valueStr = "'" + value + "'";
        else if (value instanceof Float)
            valueStr = value + "F";
        else if (value instanceof Long)
            valueStr = value + "L";
        else
            valueStr = value.toString();
        asmifier.line("%s.visit(%s, %s);", this.name, asmifier.quote(name), valueStr);
    }

    @Override
    public void visitEnum(String name, String descriptor, String value) {
        asmifier.line("%s.visitEnum(%s, %s, %s);",
                this.name, asmifier.quote(name), asmifier.quote(descriptor), asmifier.quote(value));
    }

    @Override
    public AnnotationVisitor visitAnnotation(String name, String descriptor) {
        asmifier.line("{");
        asmifier.indent++;
        asmifier.line("AnnotationVisitor av%d = %s.visitAnnotation(%s, %s);",
                level + 1, this.name, asmifier.quote(name), asmifier.quote(descriptor));
        return new AnnotationAnalyzer(asmifier, level + 1);
    }

    @Override
    public AnnotationVisitor visitArray(String name) {
        asmifier.line("{");
        asmifier.indent++;
        asmifier.line("AnnotationVisitor av%d = %s.visitArray(%s);",
                level + 1, this.name, asmifier.quote(name));
        return new AnnotationAnalyzer(asmifier, level + 1);
    }

    @Override
    public void visitEnd() {
        asmifier.line("%s.visitEnd();", name);
        asmifier.indent--;
        asmifier.line("}");
    }
}
