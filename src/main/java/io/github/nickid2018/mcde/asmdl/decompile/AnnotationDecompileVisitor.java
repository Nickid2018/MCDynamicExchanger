package io.github.nickid2018.mcde.asmdl.decompile;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Opcodes;

public class AnnotationDecompileVisitor extends AnnotationVisitor {

    private final DecompileContext context;

    public AnnotationDecompileVisitor(DecompileContext context) {
        super(Opcodes.ASM9);
        this.context = context;
    }

    @Override
    public void visit(String name, Object value) {
        context.addElement(new TextElement("value %s %s".formatted(name, TextElement.getPlainValue(value))));
    }

    @Override
    public void visitEnum(String name, String descriptor, String value) {
        context.addElement(new TextElement("enum %s %s %s".formatted(name, descriptor, value)));
    }

    @Override
    public AnnotationVisitor visitAnnotation(String name, String descriptor) {
        TextBlockElement root = new TextBlockElement("annotation %s %s".formatted(name, descriptor));
        context.pushBlock(root);
        return new AnnotationDecompileVisitor(context);
    }

    @Override
    public AnnotationVisitor visitArray(String name) {
        TextBlockElement root = new TextBlockElement("array %s".formatted(name));
        context.pushBlock(root);
        return new AnnotationDecompileVisitor(context);
    }

    @Override
    public void visitEnd() {
        context.popBlock();
    }
}
