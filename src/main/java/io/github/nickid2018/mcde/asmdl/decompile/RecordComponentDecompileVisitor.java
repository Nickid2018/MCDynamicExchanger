package io.github.nickid2018.mcde.asmdl.decompile;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.RecordComponentVisitor;
import org.objectweb.asm.TypePath;

public class RecordComponentDecompileVisitor extends RecordComponentVisitor {

    private final DecompileContext context;

    public RecordComponentDecompileVisitor(DecompileContext context) {
        super(Opcodes.ASM9);
        this.context = context;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        TextBlockElement root = new TextBlockElement("annotation record_component %s %s".formatted(descriptor, visible));
        context.pushBlock(root);
        return new AnnotationDecompileVisitor(context);
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        TextBlockElement root = new TextBlockElement("annotation record_component_type %s %s %s %s"
                .formatted(ClassDecompileVisitor.CLASS_TYPE_REFERENCE_KINDS.get(typeRef), typePath, descriptor, visible));
        context.pushBlock(root);
        return new AnnotationDecompileVisitor(context);
    }

    @Override
    public void visitEnd() {
        context.popBlock();
    }
}
