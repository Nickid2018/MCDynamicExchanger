package io.github.nickid2018.mcde.asmdl.decompile;

import io.github.nickid2018.mcde.asmdl.ASMDLParser;
import org.apache.commons.io.IOUtils;
import org.objectweb.asm.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

public class ClassDecompileVisitor extends ClassVisitor {

    public static final Map<Integer, String> CLASS_TYPE_REFERENCE_KINDS = Map.of(
            TypeReference.CLASS_TYPE_PARAMETER, "class_type_parameter",
            TypeReference.CLASS_TYPE_PARAMETER_BOUND, "class_type_parameter_bound",
            TypeReference.CLASS_EXTENDS, "class_extends"
    );

    private DecompileContext context;

    public ClassDecompileVisitor() {
        super(Opcodes.ASM9);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        String base = "class %d %s %s".formatted(version - 44, AccessFlags.CLASS.getFlags(access), name);
        if (superName != null)
            base += " extends %s".formatted(superName);
        if (interfaces != null && interfaces.length > 0)
            base += " implements %s".formatted(String.join(",", interfaces));
        if (signature != null)
            base += " signature %s".formatted(signature);
        TextBlockElement root = new TextBlockElement(base);
        context = new DecompileContext(root);
    }

    @Override
    public void visitNestHost(String nestHost) {
        context.addElement(new TextElement("nesthost %s".formatted(nestHost)));
    }

    @Override
    public void visitOuterClass(String owner, String name, String descriptor) {
        context.addElement(new TextElement("outerclass %s %s %s".formatted(owner, name, descriptor)));
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        TextBlockElement root = new TextBlockElement("annotation class %s %s".formatted(descriptor, visible));
        context.pushBlock(root);
        return new AnnotationDecompileVisitor(context);
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        TextBlockElement root = new TextBlockElement("annotation class_type %s %s %s %s".formatted(
                CLASS_TYPE_REFERENCE_KINDS.get(typeRef), typePath, descriptor, visible));
        context.pushBlock(root);
        return new AnnotationDecompileVisitor(context);
    }

    @Override
    public void visitNestMember(String nestMember) {
        context.addElement(new TextElement("nestmember %s".formatted(nestMember)));
    }

    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        context.addElement(new TextElement("innerclass %s %s %s %s".formatted(
                AccessFlags.CLASS.getFlags(access), name, outerName, innerName)));
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        String base = "field %s %s %s".formatted(AccessFlags.FIELD.getFlags(access), name, descriptor);
        if (signature != null)
            base += " signature %s".formatted(signature);
        if (value != null)
            base += " value %s".formatted(TextElement.getPlainValue(value));
        TextBlockElement root = new TextBlockElement(base);
        context.pushBlock(root);
        return new FieldDecompileVisitor(context);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        String base = "method %s %s %s".formatted(AccessFlags.METHOD.getFlags(access), name, descriptor);
        if (signature != null)
            base += " signature %s".formatted(signature);
        if (exceptions != null && exceptions.length > 0)
            base += " throws %s".formatted(String.join(",", exceptions));
        TextBlockElement root = new TextBlockElement(base);
        context.pushBlock(root);
        return new MethodDecompileVisitor(context);
    }

    @Override
    public RecordComponentVisitor visitRecordComponent(String name, String descriptor, String signature) {
        String base = "record_component %s %s".formatted(name, descriptor);
        if (signature != null)
            base += " signature %s".formatted(signature);
        TextBlockElement root = new TextBlockElement(base);
        context.pushBlock(root);
        return new RecordComponentDecompileVisitor(context);
    }

    @Override
    public void visitPermittedSubclass(String permittedSubclass) {
        context.addElement(new TextElement("permitted %s".formatted(permittedSubclass)));
    }

    public String decompiledString() {
        StringWriter writer = new StringWriter();
        context.getRoot().append(writer, 0);
        return writer.toString();
    }
}
