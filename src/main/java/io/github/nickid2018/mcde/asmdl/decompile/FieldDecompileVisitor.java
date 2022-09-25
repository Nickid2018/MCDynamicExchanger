package io.github.nickid2018.mcde.asmdl.decompile;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.TypePath;

public class FieldDecompileVisitor extends FieldVisitor {

    private final DecompileContext context;

    public FieldDecompileVisitor(DecompileContext context) {
        super(Opcodes.ASM9);
        this.context = context;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        TextBlockElement root = new TextBlockElement("annotation field %s %s".formatted(descriptor, visible));
        context.pushBlock(root);
        return new AnnotationDecompileVisitor(context);
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        TextBlockElement root = new TextBlockElement("annotation field_type %s %s %s".formatted(typePath, descriptor, visible));
        context.pushBlock(root);
        return new AnnotationDecompileVisitor(context);
    }

    @Override
    public void visitEnd() {
        context.popBlock();
    }
}
